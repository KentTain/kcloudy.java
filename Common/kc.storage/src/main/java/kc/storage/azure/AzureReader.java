package kc.storage.azure;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.ResultSegment;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.ListBlobItem;

import kc.framework.base.BlobInfo;
import kc.framework.extension.DateExtensions;
import kc.framework.extension.StringExtensions;
import kc.storage.BlobCache;
import kc.storage.BlobResult;
import kc.storage.ReaderBase;

class AzureReader extends ReaderBase{
	private Logger logger = LoggerFactory.getLogger(AzureReader.class);
	private final CloudStorageAccount Account;
    private final CloudBlobClient Client;
    private final BlobCache Cache;

    public AzureReader(String connectionString, BlobCache cache) throws InvalidKeyException, URISyntaxException
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
            return null;
            //return isBlockBlob ? this.Client.GetBlockBlobReference(blobId) : this.Client.GetBlobReference(blobId);
        }

        try {
			CloudBlobContainer container = this.Client.getContainerReference(containerName);
			return isBlockBlob ? container.getBlockBlobReference(blobId) : container.getBlobReferenceFromServer(blobId);
		} catch (Exception e) {
			logger.error(String.format(
                    "--Container[%s]--AzureReader.GetBlob throw error: %s", 
                    containerName, e.getMessage()));
			return null;
		}
    }
    
    private boolean DoesContainerExist(CloudBlobContainer container)
    {
        try
        {
            container.downloadAttributes();
            return true;
        }
        catch (StorageException e)
        {
        	logger.error(e.getMessage());
        	return false;
        }
    }
    @Override
    public List<String> ListContainers()
    {
        try {
			List<String> containerNames = new ArrayList<String>();
			// Retrieve a segment (up to 1,000 entities).
			ResultSegment<CloudBlobContainer> result = this.Client.listContainersSegmented();
			for (CloudBlobContainer listBlobItem : result.getResults())
			{
				//String absolutePath = listBlobItem.getUri().getPath();
			    //String url = absolutePath.substring(absolutePath.lastIndexOf("/") + 1);
			    containerNames.add(listBlobItem.getName());
			}

			return containerNames;
		} catch (StorageException e) {
			logger.error(String.format(
                    "--Container--AzureReader.ListContainers throw error: %s", 
                    e.getMessage()));
			return new ArrayList<String>();
		}
    }
    @Override
    public boolean ExistContainer(String containerName)
    {
        if (StringExtensions.isNullOrEmpty(containerName))
            return false;

        try {
			CloudBlobContainer container = this.Client.getContainerReference(containerName);
			return DoesContainerExist(container);
		} catch (Exception e) {
			logger.error(String.format(
                    "--Container[%s]--AzureReader.ExistContainer throw error: %s", 
                    containerName, e.getMessage()));
			return false;
		}
    }
    @Override
    public List<String> ListBlobIds(String containerName)
    {
        if (StringExtensions.isNullOrEmpty(containerName)) 
        	return new ArrayList<String>();

        try {
			CloudBlobContainer container = this.Client.getContainerReference(containerName);

			List<String> blobIds = new ArrayList<String>();

			// Retrieve a segment (up to 1,000 entities).
			ResultSegment<ListBlobItem> result = container.listBlobsSegmented();
			for (ListBlobItem listBlobItem : result.getResults())
			{
				String absolutePath = listBlobItem.getUri().getPath();
			    blobIds.add(absolutePath.substring(absolutePath.lastIndexOf("/") + 1));
			}

			return blobIds;
		} catch (Exception e) {
			logger.error(String.format(
                    "--Container[%s]--AzureReader.ListBlobIds throw error: %s", 
                    containerName, e.getMessage()));
			return new ArrayList<String>();
		}
    }
    @Override
    public List<String> ListBlobIdsWithMetadata(String containerName)
    {
        if (StringExtensions.isNullOrEmpty(containerName)) return new ArrayList<String>();

        try {
			CloudBlobContainer container = this.Client.getContainerReference(containerName);

			List<String> blobIds = new ArrayList<String>();
			// Retrieve a segment (up to 1,000 entities).
			ResultSegment<ListBlobItem> result = container.listBlobsSegmented();
			for (ListBlobItem listBlobItem : result.getResults())
			{
				String absolutePath = listBlobItem.getUri().getPath();
			    blobIds.add(absolutePath.substring(absolutePath.lastIndexOf("/") + 1));
			}

			return blobIds;
		} catch (Exception e) {
			logger.error(String.format(
                    "--Container[%s]--AzureReader.ListBlobIdsWithMetadata throw error: %s", 
                    containerName, e.getMessage()));
			return new ArrayList<String>();
		}
    }
    @Override
    public boolean ExistBlob(String containerName, String blobId)
    {
        if (StringExtensions.isNullOrEmpty(blobId))
            return false;
        try
        {
        	CloudBlobContainer container = this.Client.getContainerReference(containerName);
            if (!this.DoesContainerExist(container))
                return false;

            String realBlobId = GetActualBlobId(blobId);
            CloudBlob blob = container.getBlobReferenceFromServer(realBlobId);
            
            blob.downloadAttributes();
            return true;
        }
        catch (Exception e)
        {
        	logger.error(String.format(
                    "--Container[%s]--AzureReader.ExistBlob throw error: %s", 
                    containerName, e.getMessage()));
            return false;
        }
    }
    @Override
    public BlobInfo GetBlob(String container, String encryptionKey, boolean isInternal, String userId, String blobId, boolean useCacheIfAvailable)
    {
        if (StringExtensions.isNullOrEmpty(blobId) || StringExtensions.isNullOrEmpty(container)) 
        	return null;

        int offset = -1;
        int length = -1;
        BlobResult bResult = GetActualBlobIdAndOffset(blobId, offset, length);
        String realBlobId = bResult.getBlobId();
        offset = bResult.getOffset();
        length = bResult.getLength();
        
        if (useCacheIfAvailable && this.Cache.Exists(container, realBlobId))
        {
			try {
				BlobInfo bi = this.Cache.GetBlob(container, encryptionKey, isInternal, userId, realBlobId);
				if (offset != -1)
	            {
	                bi = GetBlobSegment(bi, offset, length, blobId);
	            }
	            return bi;
			} catch (Exception e) {
				logger.error(String.format(
	                    "--Container[%s]--AzureReader.ExistBlob throw error: %s",
                        container, e.getMessage()));
				return null;
			}
        }
        else
        {
            try {
				CloudBlob blob = this.Client.getContainerReference(container).getAppendBlobReference(realBlobId);
				if(blob == null) return null;
				
//				byte[] cacheData = null;
//				byte[] cacheMetadata = null;
			    BlobInfo bi = GetBlobWithCache(container, encryptionKey, isInternal, userId, realBlobId, useCacheIfAvailable);
			    //LogUtil.LogDebug("Retrieve Blob", blobId, sw.ElapsedMilliseconds);

//			    if (useCacheIfAvailable && cacheData != null)
//			    {
//			        this.Cache.Insert(container, realBlobId, cacheData, cacheMetadata);
//			    }
			    if (offset != -1)
			    {
			        bi = GetBlobSegment(bi, offset, length, blobId);
			    }
			    return bi;
			} catch (Exception e) {
				logger.error(String.format(
	                    "[%s]--Container--AzureReader.GetBlob throw error: %s",
                        container, e.getMessage()));
				return null;
			}
        }
    }
    @Override
    public Date GetBlobLastModifiedTime(String containerName, String blobId)
    {
        try
        {
            CloudBlob blob = this.GetBlob(containerName, blobId, false);
            blob.downloadAttributes();
            return blob.getProperties().getLastModified() != null 
                ? blob.getProperties().getLastModified() 
                : DateExtensions.getMinValue();
        }
        catch(Exception e)
        {
            return DateExtensions.getMinValue();
        }
    }
    @Override
    public Map<String, String> GetBlobMetadata(String containerName, String blobId)
    {
        CloudBlob blob = this.GetBlob(containerName, blobId, false);
        return GetBlobMetadata(blob);
    }
    @Override
    public BlobInfo GetBlobWithoutEncryption(String containerName, String blobId, boolean useCacheIfAvailable)
    {
        if (StringExtensions.isNullOrEmpty(blobId) || StringExtensions.isNullOrEmpty(containerName)) return null;

        int offset = -1;
        int length = -1;
        BlobResult bResult = GetActualBlobIdAndOffset(blobId, offset, length);
        String realBlobId = bResult.getBlobId();
        offset = bResult.getOffset();
        length = bResult.getLength();
        if (useCacheIfAvailable && this.Cache.Exists(containerName, realBlobId))
        {
            BlobInfo bi = this.Cache.GetBlobWithoutEncryption(containerName, realBlobId);
            //LogUtil.LogDebug("Retrieve Blob from local store", blobId, sw.ElapsedMilliseconds);
            if (offset != -1)
            {
                bi = GetBlobSegment(bi, offset, length, blobId);
            }
            return bi;
        }
        else
        {
            CloudBlob blob = GetBlob(containerName, realBlobId, false);
            if(blob == null) return null;
            
            try {
				String type = (blob.getMetadata() != null && blob.getMetadata().get("Type") != null) ? blob.getMetadata().get("Type") : "Unknown";
				String format = (blob.getMetadata() != null && blob.getMetadata().get("Format") != null) ? blob.getMetadata().get("Format") : "Unknown";
				String fileName = (blob.getMetadata() != null && blob.getMetadata().get("FileName") != null) ? blob.getMetadata().get("FileName") : "Unknown";

				blob.downloadAttributes();
				int fileByteLength = (int)blob.getProperties().getLength();
				byte[] blobData = new byte[fileByteLength];
				blob.downloadToByteArray(blobData, 0);
				BlobInfo blobInfo = new BlobInfo(realBlobId, type, format, fileName, blobData);
				blobInfo.setSize (blobData.length);
				//LogUtil.LogDebug("Retrieve Blob", blobId, sw.ElapsedMilliseconds);

				if (useCacheIfAvailable)
				{
				    this.Cache.InsertWithoutEncryption(containerName, blobId, blobInfo.getData());
				}
				if (offset != -1)
				{
				    blobInfo = GetBlobSegment(blobInfo, offset, length, blobId);
				}
				return blobInfo;
			} catch (StorageException e) {
				logger.error(String.format(
	                    "--Container[%s]--AzureReader.GetBlobWithoutEncryption throw error: %s", 
	                    containerName, e.getMessage()));
				return null;
			}
        }
    }

    public List<BlobInfo> GetBlobs(String container, String encryptionKey, boolean isInternal, String userId, List<String> blobIds, boolean returnPlaceholderIfNotFound, boolean isUserLevel)
    {
        List<BlobInfo> blobInfos = new ArrayList<BlobInfo>();
        if(blobIds == null || blobIds.size() <= 0) 
        	return blobInfos; 
        
        for (String blobId : blobIds)
        {
            int offset = -1;
            int length = -1;
            BlobResult bResult = GetActualBlobIdAndOffset(blobId, offset, length);
            String realBlobId = bResult.getBlobId();
            offset = bResult.getOffset();
            length = bResult.getLength();
            
            BlobInfo bi = GetBlobWithCache(container, encryptionKey, isInternal, userId, realBlobId, false);
            if (bi != null)
            {
                if (offset != -1)
                {
                    bi = GetBlobSegment(bi, offset, length, blobId);
                }
                blobInfos.add(bi);
            }
        }
        return blobInfos;
    }

    private byte[] cacheData;
    private byte[] cacheMetadata;
    private BlobInfo GetBlobWithCache(String containerName, String encryptionKey, boolean isInternal, String userId, String blobId, boolean generateCacheData)
    {
        cacheData = null;
        cacheMetadata = null;

        if (StringExtensions.isNullOrEmpty(blobId))
            return null;

        CloudBlob blob = GetBlob(containerName, blobId, false);
        if (blob == null)
            return null;

        Map<String, String> blobMetadata = GetBlobMetadata(blob);
        try {
			CheckBlobAccess(containerName, isInternal, userId, blobId, blobMetadata);
		} catch (Exception e) {
			cacheData = null;
            cacheMetadata = null;
            logger.error(String.format(
                    "[%s]--Container[%s]--AzureReader.GetBlobWithCache throw error: %s",
                    containerName, containerName, e.getMessage()));
		}

        try {
			blob.downloadAttributes();
		} catch (StorageException e) {
			cacheData = null;
            cacheMetadata = null;
            logger.error(String.format(
                    "[%s]--Container[%s]--AzureReader.blob.downloadAttributes throw error: %s",
                    containerName, containerName, e.getMessage()));
		}
        
        int fileByteLength = (int)blob.getProperties().getLength();
        byte[] data = new byte[fileByteLength];
        try
        {
            blob.downloadToByteArray(data, 0); 
        }
        catch (Exception e)
        {
            cacheData = null;
            cacheMetadata = null;
            logger.error(String.format(
                "[%s]--Container[%s]--AzureReader.blob.downloadToByteArray throw error: %s",
                    containerName, containerName, e.getMessage()));
            return null;
        }

        // Store Cache Data
        if (generateCacheData)
        {
            cacheData = new byte[data.length];
            System.arraycopy(data, 0, cacheData, 0, data.length);

            String metadataXml = MetadataToXml(blobMetadata);
            try {
				cacheMetadata = metadataXml.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error(String.format(
	                    "[%s]--Container[%s]--AzureReader.metadataXml.getBytes throw error: %s",
                        containerName, containerName, e.getMessage()));
			}
        }
        else
        {
            cacheData = null;
            cacheMetadata = null;
        }

        String type = (blob.getMetadata() != null && blob.getMetadata().get("Type") != null) ? blob.getMetadata().get("Type") : "Unknown";
        String format = (blob.getMetadata() != null && blob.getMetadata().get("Format") != null) ? blob.getMetadata().get("Format") : "Unknown";
        String fileName = (blob.getMetadata() != null && blob.getMetadata().get("FileName") != null) ? blob.getMetadata().get("FileName") : "Unknown";
        byte[] blobActualData = DecryptIfNeeded(encryptionKey, isInternal, userId, data, blobMetadata);
        BlobInfo bi = new BlobInfo(blobId, type, format, fileName, blobActualData);
        bi.setSize(bi.getData().length);
        return bi;
    }

    private Map<String, String> GetBlobMetadata(CloudBlob blob)
    {
        try
        {
            blob.downloadAttributes();
        }
        catch (Exception e)
        {
        	logger.error(String.format(
                    "--Container--AzureReader.GetBlobMetadata throw error: %s", 
                    e.getMessage()));
        	
        	return new HashMap<String, String>();
            // Somehow this is happening in debug mode sometimes.
        }

        Map<String, String> metadataDict = new HashMap<String, String>();
        for (String key : blob.getMetadata().keySet())
        {
            metadataDict.put(key, blob.getMetadata().get(key));
        }
        return metadataDict;
    }
}
