package kc.storage.azure;

import java.io.ByteArrayInputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.storage.AccessCondition;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlockEntry;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import kc.framework.extension.StringExtensions;
import kc.storage.BlobCache;
import kc.storage.WriterBase;

class AzureWriter extends WriterBase{
	private Logger logger = LoggerFactory.getLogger(AzureWriter.class);
	private CloudStorageAccount Account = null;
    private CloudBlobClient Client = null;
    private BlobCache Cache = null;

    public AzureWriter(String connectionString, BlobCache cache) throws InvalidKeyException, URISyntaxException
    {
    	super();
        
    	if (StringExtensions.isNullOrEmpty(connectionString))
            throw new IllegalArgumentException("Azure Storage's connect String is empy or null.");

		this.Account = CloudStorageAccount.parse(connectionString);
		this.Client = this.Account.createCloudBlobClient();
		this.Cache = cache;
    }

    private CloudBlob GetBlob(String containerName, String blobId, boolean isBlockBlob)
    {
        if (StringExtensions.isNullOrEmpty(containerName))
        {
            //return isBlockBlob ? this.Client.GetBlockBlobReference(blobId) : this.Client.GetBlobReference(blobId);
            return null;
        }
        else
        {
            try {
				CloudBlobContainer container = this.Client.getContainerReference(containerName);
				container.createIfNotExists();
				return isBlockBlob ? container.getBlockBlobReference(blobId) : container.getBlobReferenceFromServer(blobId);
			} catch (Exception e) {
				logger.error(String.format(
	                    "--Container[%s]--AzureWriter.GetBlob throw error: %s", 
	                    containerName, e.getMessage()));
				
				return null;
			}
        }
    }
    @Override
    protected void CreateContainer(String containerName)
    {
        try {
			CloudBlobContainer container = this.Client.getContainerReference(containerName);
			container.createIfNotExists();
		} catch (Exception e) {
			logger.error(String.format(
                    "--Container[%s]--AzureWriter.CreateContainer throw error: %s", 
                    containerName, e.getMessage()));
		}
    }
    @Override
    protected void DeleteContainer(String containerName)
    {
        try
        {
        	CloudBlobContainer container = this.Client.getContainerReference(containerName);
            container.delete();
        }
        catch (Exception e)
        {
        	logger.error(String.format(
                    "--Container[%s]--AzureWriter.DeleteContainer throw error: %s", 
                    containerName, e.getMessage()));
        }
    }
    @Override
    protected void WriteBlob(String container, String blobId, byte[] blobData, Map<String, String> blobMetadata)
    {
    	try {
			CloudBlockBlob blob = (CloudBlockBlob)GetBlob(container, blobId, true);
			blob.uploadFromByteArray(blobData, 0, blobData.length);
			if (blobMetadata != null)
			{
			    for (String metadataKey : blobMetadata.keySet())
			    {
			        blob.getMetadata().replace(metadataKey, blobMetadata.get(metadataKey));
			    }
			    blob.uploadMetadata();
			}
		} catch (Exception e) {
			logger.error(String.format(
                    "--Container[%s]--AzureWriter.WriteBlob throw error: %s", 
                    container, e.getMessage()));
		}
    }
    @Override
    protected void WriteBlobMetadata(String container, String blobId, boolean clearExisting, Map<String, String> blobMetadata)
    {
        CloudBlob blob = GetBlob(container, blobId, false);
        try
        {
            blob.downloadAttributes();
        }
        catch (Exception e) {
        	logger.error(String.format(
                    "--Container[%s]--AzureWriter.WriteBlobMetadata throw error: %s", 
                    container, e.getMessage()));
        }
        if (clearExisting)
        {
            blob.getMetadata().clear();
        }
        for (String metadataKey : blobMetadata.keySet())
        {
            blob.getMetadata().replace(metadataKey, blobMetadata.get(metadataKey));
        }
        try {
			blob.uploadMetadata();
		} catch (StorageException e) {
			logger.error(String.format(
                    "--Container[%s]--AzureWriter.WriteBlobMetadata throw error: %s", 
                    container, e.getMessage()));
		}
    }

	protected void WriteBlobBlock(String container, String blobId, String blobBlockId, byte[] blobBlockData, String contentMD5)
    {
        CloudBlockBlob blob = (CloudBlockBlob) GetBlob(container, blobId, true);
        try {
			blob.uploadBlock(blobBlockId, new ByteArrayInputStream(blobBlockData), blobBlockData.length, null, null, null);
		} catch (Exception e) {
			logger.error(String.format(
                    "--Container[%s]--AzureWriter.WriteBlobBlock throw error: %s", 
                    container, e.getMessage()));
		}
    }
    @Override
    protected void SubmitBlobBlock(String container, String blobId, String[] blobBlockIds, Map<String, String> blobMetadata)
    {
        CloudBlockBlob blob = (CloudBlockBlob)GetBlob(container, blobId, true);
        List<BlockEntry> blockEntries = new ArrayList<BlockEntry>();
        for(String id : blobBlockIds)
        {
        	blockEntries.add(new BlockEntry(id));
        }
        
        try {
			blob.commitBlockList(blockEntries);
			if (blobMetadata != null)
			{
			    for (String metadataKey : blobMetadata.keySet())
			    {
			    	blob.getMetadata().replace(metadataKey, blobMetadata.get(metadataKey));
			    }
			    blob.uploadMetadata();
			}
		} catch (StorageException e) {
			logger.error(String.format(
                    "--Container[%s]--AzureWriter.SubmitBlobBlock throw error: %s", 
                    container, e.getMessage()));
		}
    }
    @Override
    protected void DeleteBlob(String container, String blobId)
    {
        String realBlobId = GetActualBlobId(blobId);
        if (!StringExtensions.isNullOrEmpty(realBlobId))
        {
            CloudBlob blob = GetBlob(container, realBlobId, false);
            if (blob != null)
            {
                try
                {
                    blob.deleteIfExists();
                }
                catch (Exception ex)
                {
                    try
                    {
                        // try again
                        blob.breakLease(null);
                        blob.deleteIfExists();
                    }
                    catch (Exception e)
                    {
                    	logger.error(String.format(
                                "--Container[%s]--AzureWriter.DeleteBlob throw error: %s", 
                                container, e.getMessage()));
                    }
                }

            }
            this.Cache.Delete(container, realBlobId);
        }
    }
    @Override
    public void CopyBlob(String containerName, String desContainerName, String blobId)
    {
        if (StringExtensions.isNullOrEmpty(blobId)) return;

        try {
			//this.Client.RetryPolicy = RetryPolicies.Retry(20, TimeSpan.Zero);
			CloudBlobContainer container = this.Client.getContainerReference(containerName);
			if (container != null)
			{
				CloudBlockBlob blob = (CloudBlockBlob)GetBlob(containerName, blobId, true);
			    if (blob != null)
			    {
			    	CloudBlockBlob newBlob = (CloudBlockBlob)GetBlob(desContainerName, blobId, true);
			        //newBlob.UploadFromByteArrayAsync(new byte[] { }, 0, 0);
			        newBlob.startCopy(blob);
			    }
			}
		} catch (URISyntaxException e) {
			logger.error(String.format(
                    "--Container[%s]--AzureWriter.CopyBlob throw error: %s", 
                    containerName, e.getMessage()));
		} catch (StorageException e) {
			logger.error(String.format(
                    "--Container[%s]--AzureWriter.CopyBlob throw error: %s", 
                    containerName, e.getMessage()));
		}
    }

    private static Map<String, AutoRenewLease> _acquiredAutoRenewLeaseMap = new HashMap<String, AutoRenewLease>();
    private Lock lock = new ReentrantLock();// 锁对象

    public boolean IsBlobLocked(String containerName, String blobId)
    {
    	lock.lock();// 得到锁
        //lock (_acquiredAutoRenewLeaseMap)
    	try
        {
            if (_acquiredAutoRenewLeaseMap.containsKey(containerName + "|" + blobId))
            {
                return false; // if current process has already get lease, it is not "locked" for itself
            }
        } finally {
			lock.unlock();// 释放锁
		}

        try {
			CloudBlockBlob blob = (CloudBlockBlob)GetBlob(containerName, blobId, false);
			String tempLease = blob.acquireLease();
			if (StringExtensions.isNullOrEmpty(tempLease))
			{
			    return true;
			}
			blob.releaseLease(AccessCondition.generateIfMatchCondition(tempLease));
			return false;
		} catch (StorageException e) {
			logger.error(String.format(
                    "--Container[%s]--AzureWriter.IsBlobLocked throw error: %s", 
                    containerName, e.getMessage()));
			return false;
		}
    }

    @SuppressWarnings("resource")
	public boolean ObtainBlobLock(String containerName, String blobId)
    {
    	CloudBlockBlob blob = (CloudBlockBlob)GetBlob(containerName, blobId, true);
        AutoRenewLease arl = new AutoRenewLease(blob);
        if (arl.HasLease())
        {
        	lock.lock();// 得到锁
            //lock (_acquiredAutoRenewLeaseMap)
        	try
            {
                if (!_acquiredAutoRenewLeaseMap.containsKey(containerName + "|" + blobId))
                {
                    _acquiredAutoRenewLeaseMap.put(containerName + "|" + blobId, arl);
                }
            } finally {
    			lock.unlock();// 释放锁
    		}
            return true;
        }
        
        return false;
    }

    public void ReleaseBlobLock(String containerName, String blobId)
    {
    	lock.lock();// 得到锁
        //lock (_acquiredAutoRenewLeaseMap)
    	try
        {
            if (_acquiredAutoRenewLeaseMap.containsKey(containerName + "|" + blobId))
            {
                AutoRenewLease arl = _acquiredAutoRenewLeaseMap.get(containerName + "|" + blobId);
                _acquiredAutoRenewLeaseMap.remove(containerName + "|" + blobId);
                arl.close();
            }
        } catch (Exception e) {
        	logger.error(String.format(
                    "--Container[%s]--AzureWriter.ReleaseBlobLock throw error: %s", 
                    containerName, e.getMessage()));
		} finally {
			lock.unlock();// 释放锁
		}
    }

    public void BreakBlobLock(String containerName, String blobId)
    {
        try {
			CloudBlob blob = GetBlob(containerName, blobId, false);
			blob.breakLease(null);
		} catch (StorageException e) {
			logger.error(String.format(
                    "--Container[%s]--AzureWriter.BreakBlobLock throw error: %s", 
                    containerName, e.getMessage()));
		}
    }
}
