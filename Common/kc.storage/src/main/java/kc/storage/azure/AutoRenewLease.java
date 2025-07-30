package kc.storage.azure;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

import com.microsoft.azure.storage.AccessCondition;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import kc.framework.extension.DateExtensions;

public class AutoRenewLease implements AutoCloseable {
	public boolean HasLease() {  return leaseId != null; }

    private CloudBlob blob;
    private String leaseId;
    private Thread renewalThread;
    private boolean disposed = false;

    public static void DoOnce(CloudBlockBlob blob, Function<?, ?> action) { DoOnce(blob, action, 5 * 1000l); }
    public static void DoOnce(CloudBlockBlob blob, Function<?, ?> action, long pollingFrequency)
    {
        try {
			// blob.Exists has the side effect of calling blob.FetchAttributes, which populates the metadata collection
			while (!blob.exists() || blob.getMetadata().get("progress") != "done")
			{
			    try (AutoRenewLease arl = new AutoRenewLease(blob))
			    {
			        if (arl.HasLease())
			        {
			        	action.apply(null);
			            blob.getMetadata().replace("progress", "done");
			            //blob.SetMetadata(arl.leaseId);
			            blob.uploadMetadata();;
			        }
			        else
			        {
			        	Thread.sleep(pollingFrequency);
			        }
			    }
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void DoEvery(CloudBlockBlob blob, long interval, Function<?, ?> action)
    {
        while (true)
        {
        	
        	LocalDateTime lastPerformed = LocalDateTime.MIN;
            try (AutoRenewLease arl = new AutoRenewLease(blob))
            {
                if (arl.HasLease())
                {
                    blob.downloadAttributes();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    lastPerformed = LocalDateTime.parse(blob.getMetadata().get("lastPerformed"),formatter);
                    if (DateExtensions.getLocalDateTimeUtcNow().isAfter(lastPerformed.plus(interval, ChronoUnit.MILLIS)))
                    {
                    	action.apply(null);
                    	
                        lastPerformed = DateExtensions.getLocalDateTimeUtcNow();
                        blob.getMetadata().replace("lastPerformed", lastPerformed.format(formatter));
                        //blob.SetMetadata(arl.leaseId);
                        blob.uploadMetadata();
                    }
                }
            } catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
            long timeLeft = DateExtensions.countMilliSeconds(lastPerformed, DateExtensions.getLocalDateTimeUtcNow()) + interval;
            long minimum = 5 * 1000l; // so we're not polling the leased blob too fast
            try {
				Thread.sleep(
				    timeLeft > minimum
				    ? timeLeft
				    : minimum);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    public AutoRenewLease(CloudBlockBlob blob)
    {
        this.blob = blob;
        try
        {
        	blob.getContainer().createIfNotExists();
            blob.uploadFromByteArray(new byte[0], 0, 1);
            //blob.UploadByteArray(new byte[0], new BlobRequestOptions { AccessCondition = AccessCondition.IfNoneMatch("*") });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        try {
			leaseId = blob.acquireLease();
		} catch (StorageException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
        if (HasLease())
        {
            renewalThread = new Thread(() ->
            {
                while (true)
                {
                    try {
						Thread.sleep(30 * 1000);
						blob.renewLease(AccessCondition.generateLeaseCondition(leaseId));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (StorageException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						
					}
                }
            });
            renewalThread.start();
        }
    }
	@Override
	public void close() throws Exception {
		if (!disposed)
        {
			if (renewalThread != null)
            {
                try {
					//renewalThread.destroy();
					renewalThread.interrupt();
					blob.releaseLease(AccessCondition.generateLeaseCondition(leaseId));
					renewalThread = null;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            disposed = true;
        }
	}
}
