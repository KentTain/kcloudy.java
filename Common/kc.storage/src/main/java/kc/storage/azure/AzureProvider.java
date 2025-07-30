package kc.storage.azure;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kc.framework.GlobalConfig;
import kc.framework.base.BlobInfo;
import kc.framework.extension.StringExtensions;
import kc.framework.tenant.Tenant;
import kc.storage.BlobCache;
import kc.storage.IBlobProvider;
import kc.storage.ProviderBase;
import kc.storage.WriterBase;
import kc.storage.util.Encryption;

public class AzureProvider extends ProviderBase implements IBlobProvider{
	private Logger logger = LoggerFactory.getLogger(AzureProvider.class);
	private String TempFilePath = "";
    private String StorageConnectionString = "";
    private BlobCache Cache = null;
    private AzureReader Reader = null;
    private AzureWriter Writer = null;

    //public AzureProvider()
    //{
    //    this.TempFilePath = ConfigUtil.GetConfigItem("TempFilePath") + "\\";
    //    this.StorageConnectionString = ConfigUtil.GetConfigItem("StorageConnectionString");
    //    this.Cache = new AzureCache(this.TempFilePath);
    //    this.Reader = new AzureReader(this.StorageConnectionString, this.Cache);
    //    this.Writer = new AzureWriter(this.StorageConnectionString, this.Cache);
    //}

    public AzureProvider(Tenant tenant)
    {
        try
        {
            this.StorageConnectionString = tenant.GetDecryptStorageConnectionString();
            this.TempFilePath = GlobalConfig.TempFilePath + "\\";
            this.Cache = new BlobCache(this.TempFilePath);
            this.Reader = new AzureReader(this.StorageConnectionString, this.Cache);
            this.Writer = new AzureWriter(this.StorageConnectionString, this.Cache);
        }
        catch (Exception ex)
        {
        	logger.error(String.format("Initilize the Azure Provider connection:[{%s}] throw error: %s", this.StorageConnectionString, ex.getMessage()));
        }
    }

    public AzureProvider(String connectionString) throws InvalidKeyException, URISyntaxException
    {
        this.TempFilePath = GlobalConfig.TempFilePath + "\\";
        this.StorageConnectionString = connectionString;
        this.Cache = new BlobCache(this.TempFilePath);
        this.Reader = new AzureReader(this.StorageConnectionString, this.Cache);
        this.Writer = new AzureWriter(this.StorageConnectionString, this.Cache);
    }
    @Override
    public WriterBase GetWriter()
    {
        return this.Writer;
    }
    @Override
    public List<String> GetContainers()
    {
        return this.Reader.ListContainers();
    }
    @Override
    public boolean DoesContainerExist(String containerName)
    {
        return this.Reader.ExistContainer(containerName);
    }
    @Override
    public boolean DoesBlobExist(String containerName, String blobId)
    {
        return this.Reader.ExistBlob(containerName, blobId);
    }
    @Override
    public List<String> GetBlobIds(String containerName)
    {
        return this.Reader.ListBlobIds(containerName);
    }
    @Override
    public List<String> GetBlobIdsWithMetadata(String containerName)
    {
        return this.Reader.ListBlobIdsWithMetadata(containerName);
    }
    @Override
    public BlobInfo GetBlob(String containerName, String encryptionKey, boolean isInternal, String userId, String blobId, boolean useCacheIfAvailable)
    {
        return this.Reader.GetBlob(containerName, encryptionKey, isInternal, userId,  blobId, useCacheIfAvailable);
    }
    @Override
    public BlobInfo GetBlobWithoutEncryption(String containerName, String blobId, boolean useCacheIfAvailable)
    {
        return this.Reader.GetBlobWithoutEncryption(containerName, blobId, useCacheIfAvailable);
    }
    @Override
    public Date GetBlobLastModifiedTime(String containerName, String blobId)
    {
        return this.Reader.GetBlobLastModifiedTime(containerName, blobId);
    }
    @Override
    public Map<String, String> GetBlobMetadata(String containerName, String blobId)
    {
        return this.Reader.GetBlobMetadata(containerName, blobId);
    }
    @Override
    public List<BlobInfo> GetBlobs(String container, String encryptionKey, boolean isInternal, String userId, List<String> blobIds, boolean returnPlaceholderIfNotFound, boolean isUserLevel)
    {
        return this.Reader.GetBlobs(container, encryptionKey, isInternal, userId,  blobIds, returnPlaceholderIfNotFound, isUserLevel);
    }
    @Override
    public void CreateContainer(String containerName)
    {
        this.Writer.AddContainer(containerName);
    }
    @Override
    public void DeleteContainer(String containerName)
    {
        this.Writer.RemoveContainer(containerName);// To make sure delete does not fail.
    }
    @Override
    public void SaveBlob(String containerName, String encryptionKey, boolean isInternal, String userId,  String blobId, String type, String format, String fileName, byte[] fileBytes, boolean isUserLevelBlob, long expireAfterSecond)
    {
        encryptionKey = Encryption.GetEncryptionKey(encryptionKey, isUserLevelBlob, isInternal, userId);

        boolean isEncypted = false;
        byte[] dataToWrite;
        if (!StringExtensions.isNullOrEmpty(encryptionKey))
        {
            isEncypted = true;
            dataToWrite = Encryption.Encrypt(fileBytes, encryptionKey);
            if (dataToWrite == null)
            {
                throw new IllegalArgumentException("Blob encrypted data is empty");
            }
        }
        else
        {
            dataToWrite = fileBytes;
        }

        long size = fileBytes.length;
        Map<String, String> extraMetadata = null;
        Map<String, String> metadata = BuildMetadata(containerName, isInternal, userId, blobId, type, format, fileName, size, expireAfterSecond, isEncypted, isUserLevelBlob, extraMetadata);
        this.Writer.SaveBlob(containerName, blobId, dataToWrite, metadata);
    }
    @Override
    public void SaveBlobWithoutEncryption(String containerName, BlobInfo blobObject)
    {
        String blobId = blobObject.getId();
        byte[] dataToWrite = blobObject.getData();
        Map<String, String> metadata = this.Reader.GetBlobMetadata(containerName, blobId);
        metadata.replace("Id", blobObject.getId());
        metadata.replace("Type",  blobObject.getFileType());
        metadata.replace("Format",  blobObject.getFileFormat());
        metadata.replace("LastModifiedTime",  GetModifiedTime());
        metadata.replace("Size",  String.valueOf(blobObject.getSize()));
        this.Writer.SaveBlob(containerName, blobId, dataToWrite, metadata);
    }
    @Override
    public void AppendBlobMetadata(String containerName, String blobId, Map<String, String> metadata)
    {
        this.Writer.AppendBlobMetadata(containerName, blobId, metadata);
    }


    /// <summary>
    /// Notice 1: The blockId MUST converted to Base64 String!
    /// Notice 2: All the blockIds of one blob MUST have the same length!
    /// </summary>
    /// <param name="token"></param>
    /// <param name="blobId"></param>
    /// <param name="blockId">Please convert to Base64 String.</param>
    /// <param name="blockBytes"></param>
    /// <param name="contentMD5"></param>
    @Override
    public void SaveBlobBlock(String containerName, String blobId, String blockId, byte[] blockBytes, String contentMD5)
    {
        this.Writer.SaveBlobBlock(containerName, blobId, blockId, blockBytes, contentMD5);
    }

    /// <summary>
    /// Save block list to blob, commit blocks.
    /// </summary>
    @Override
    public void CommitBlobBlock(String containerName, String blobId, String[] blockIds, Map<String, String> metadata)
    {
        if (blockIds.length <= 0)
            throw new IllegalArgumentException("The block id array is empty!");

        this.Writer.CommitBlobBlock(containerName, blobId, blockIds, metadata);
    }
    @Override
    public void DeleteBlob(String containerName, String blobId)
    {
        this.Writer.RemoveBlob(containerName, blobId);
    }
    @Override
    public void DeleteBlobByRelativePath(String blobRelativePath)
    {
        this.Writer.RemoveBlob(null, blobRelativePath);
    }
    @Override
    public void CopyBlob(String containerName, String desContainerName, String blobId)
    {
        this.Writer.CopyBlob(containerName, desContainerName, blobId);
    }
    
    @Override
    public boolean IsBlobLocked(String containerName, String blobId)
    {
        return this.Writer.IsBlobLocked(containerName, blobId);
    }
    @Override
    public boolean ObtainBlobLock(String containerName, String blobId)
    {
        return this.Writer.ObtainBlobLock(containerName, blobId);
    }
    @Override
    public void ReleaseBlobLock(String containerName, String blobId)
    {
        this.Writer.ReleaseBlobLock(containerName, blobId);
    }
    @Override
    public void BreakBlobLock(String containerName, String blobId)
    {
        this.Writer.BreakBlobLock(containerName, blobId);
    }
}
