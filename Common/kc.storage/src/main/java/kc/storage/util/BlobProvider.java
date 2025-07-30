package kc.storage.util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import kc.framework.base.AuthToken;
import kc.framework.base.BlobInfo;
import kc.framework.enums.StorageType;
import kc.framework.extension.StringExtensions;
import kc.framework.security.Base64Provider;
import kc.framework.tenant.Tenant;
import kc.storage.BlobBase;
import kc.storage.IBlobProvider;
import kc.storage.aliyun.AliyunOSSProvider;
import kc.storage.aws.AwsS3Provider;
import kc.storage.azure.AzureProvider;
import kc.storage.file.FileSystemProvider;


class BlobProvider {
	private IBlobProvider Provider;
	public Tenant Tenant;
    public BlobProvider(Tenant tenant)
    {
        Tenant = tenant;
        Provider = GetProvider(tenant);
        Provider.GetWriter();
    }

    public boolean IsBlobLocked(String containerName, String blobId)
    {
        return Provider.IsBlobLocked(containerName, blobId);
    }
    public boolean ObtainBlobLock(String containerName, String blobId)
    {
        return Provider.ObtainBlobLock(containerName, blobId);
    }
    public void ReleaseBlobLock(String containerName, String blobId)
    {
        Provider.ReleaseBlobLock(containerName, blobId);
    }
    public void BreakBlobLock(String containerName, String blobId)
    {
        Provider.BreakBlobLock(containerName, blobId);
    }

    protected IBlobProvider GetProvider(kc.framework.tenant.Tenant tenant)
    {
        IBlobProvider result = null;
        StorageType storageType = tenant.getStorageType();
        switch (storageType)
        {
            case File:
                {
                    result = new FileSystemProvider(tenant);
                }
                break;
            case FTP:
                {
                    //result = new FtpSystemProvider(tenant);
                }
                break;
            case Blob:
                {
                    result = new AzureProvider(tenant);
                }
                break;
            case AWSS3:
                {
                    result = new AwsS3Provider(tenant);
                }
                break;
            case AliOSS:
                {
                    result = new AliyunOSSProvider(tenant);
                }
                break;
            default:
                {
                	result = new FileSystemProvider(tenant);
                }
                break;
        }

        return result;
    }

    public List<String> GetContainers()
    {
        return Provider.GetContainers();
    }
    public boolean DoesContainerExist(String containerName)
    {
        return Provider.DoesContainerExist(containerName);
    }
    public List<String> GetAllBlobIds(String containerName)
    {
        return Provider.GetBlobIds(containerName);
    }

    public List<String> GetAllBlobIds(AuthToken token)
    {
//        String tenant = token.getTenant();
//        String encryptionKey = token.getEncryptionKey();
//        String userId = token.getId();
        String containerName = token.getContainerName();
//        boolean isInternal = token.isInternal();

        return Provider.GetBlobIds(containerName);
    }

    public List<String> GetBlobIdsWithMetadata(String containerName)
    {
        return Provider.GetBlobIdsWithMetadata(containerName);
    }
    public List<BlobInfo> GetBlobs(AuthToken token, List<String> blobIds, boolean returnPlaceholderIfNotFound, boolean isUserLevelBlob)
    {
        String encryptionKey = token.getEncryptionKey();
        String userId = token.getId();
        String containerName = token.getContainerName();
        boolean isInternal = token.isInternal();
        return Provider.GetBlobs(containerName, encryptionKey, isInternal, userId, blobIds, returnPlaceholderIfNotFound, isUserLevelBlob);
    }

    public List<BlobInfo> GetBlobs(AuthToken token)
    {
        String encryptionKey = token.getEncryptionKey();
        String userId = token.getId();
        String containerName = token.getContainerName();
        boolean isInternal = token.isInternal();
        List<String> blobIds = GetAllBlobIds(containerName);

        return Provider.GetBlobs(containerName, encryptionKey, isInternal, userId, blobIds, false, false);
    }

    public boolean DoesBlobExist(AuthToken token, String blobId)
    {
        return DoesBlobExist(token.getContainerName(), blobId);
    }
    public boolean DoesBlobExist(String containerName, String blobId)
    {
        return Provider.DoesBlobExist(containerName, blobId);
    }
    public BlobInfo GetBlob(AuthToken token, String blobId, boolean useCacheIfAvailable)
    {
        String encryptionKey = token.getEncryptionKey();
        String userId = token.getId();
        boolean isInternal = token.isInternal();
        return Provider.GetBlob(token.getContainerName(), encryptionKey, isInternal, userId, blobId, useCacheIfAvailable);
    }
    public BlobInfo GetBlobWithoutEncryption(String containerName, String blobId, boolean useCacheIfAvailable)
    {
        return Provider.GetBlobWithoutEncryption(containerName, blobId, useCacheIfAvailable);
    }
    public Map<String, String> GetBlobMetadata(String containerName, String blobId)
    {
        return Provider.GetBlobMetadata(containerName, blobId);
    }
    public Date GetBlobLastModifiedTime(String containerName, String blobId)
    {
        return Provider.GetBlobLastModifiedTime(containerName, blobId);
    }
    
    
    public void CreateContainer(String containerName)
    {
        Provider.CreateContainer(containerName);
    }
    public void DeleteContainer(String containerName)
    {
        Provider.DeleteContainer(containerName);
    }

    public void SaveBlob(AuthToken token, BlobInfo blobObject, boolean isUserLevelBlob, long expireAfterSecond)
    {
        String containerName = token.getContainerName();
        SaveBlob(token, containerName, blobObject, isUserLevelBlob, expireAfterSecond);
    }
    public void SaveBlob(AuthToken token, String containerName, BlobInfo blobObject, boolean isUserLevelBlob, long expireAfterSecond)
    {
        SaveBlob(token, containerName, blobObject.getId(), blobObject.getFileType(), blobObject.getFileFormat(), blobObject.getFileName(), blobObject.getData(), isUserLevelBlob, expireAfterSecond);
    }
    public void SaveBlob(AuthToken token, String blobId, String type, String format, String fileName, byte[] fileBytes, boolean isUserLevelBlob, long expireAfterSecond)
    {
        SaveBlob(token, token.getContainerName(), blobId, type, format, fileName, fileBytes, isUserLevelBlob, expireAfterSecond);
    }
    public void SaveBlob(AuthToken token, String containerName, String blobId, String type, String format, String fileName, byte[] fileBytes, boolean isUserLevelBlob, long expireAfterSecond)
    {
        String encryptionKey = token.getEncryptionKey();
        String userId = token.getId();
        boolean isInternal = token.isInternal();
        Provider.SaveBlob(containerName, encryptionKey, isInternal, userId, blobId, type, format, fileName, fileBytes, isUserLevelBlob, expireAfterSecond);
    }
    public void SaveBlobWithoutEncryption(String containerName, BlobInfo blobObject)
    {
        Provider.SaveBlobWithoutEncryption(containerName, blobObject);
    }
    public void SaveBlobs(AuthToken token, List<BlobInfo> blobObjects, boolean isUserLevelBlob, long expireAfterSecond)
    {
        for (BlobInfo blobObject : blobObjects)
        {
            SaveBlob(token, blobObject.getId(), blobObject.getFileType(), blobObject.getFileFormat(), blobObject.getFileName(), blobObject.getData(), isUserLevelBlob, expireAfterSecond);
        }
    }

    public void AppendBlobMetadata(String containerName, String blobId, Map<String, String> extraMetadata)
    {
        Provider.AppendBlobMetadata(containerName, blobId, extraMetadata);
    }
    public String SaveBlobBlock(AuthToken token, String blobId, byte[] blockBytes, String contentMD5) throws UnsupportedEncodingException
    {
        String blobBlockId = Base64Provider.EncodeString(UUID.randomUUID().toString());
        SaveBlobBlockWithId(token, blobId, blobBlockId, blockBytes, contentMD5);
        return blobBlockId;
    }
    public void SaveBlobBlockWithId(AuthToken token, String blobId, String blobBlockId, byte[] blockBytes, String contentMD5)
    {
        String containerName = token.getContainerName();
        Provider.SaveBlobBlock(containerName, blobId, blobBlockId, blockBytes, contentMD5);
    }
    public void CommitBlobBlock(AuthToken token, String blobId, String type, String format,String fileName, long blobSize, String[] blockIds, boolean isUserLevelBlob, long expireAfterSecond)
    {
        String tenant = token.getTenant();
        String userId = token.getId();
        String containerName = token.getContainerName();
        boolean isEncypted = false;
        boolean isInternal = token.isInternal();
        Map<String, String> extraMetadata = null;
        Map<String, String> metadata = BlobBase.BuildMetadata(tenant, isInternal, userId, blobId, type, format, fileName, blobSize, expireAfterSecond, isEncypted, isUserLevelBlob, extraMetadata);
        Provider.CommitBlobBlock(containerName, blobId, blockIds, metadata);
    }
    public void DeleteBlob(String containerName, String[] blobId)
    {
        for (String oneBlobId : blobId)
        {
            Provider.DeleteBlob(containerName, oneBlobId);
        }
    }
    public void DeleteBlobByRelativePath(String relativePath)
    {
        Provider.DeleteBlobByRelativePath(relativePath);
    }
    public void DeleteBlob(AuthToken token, String[] blobId)
    {
        if (null != token && !StringExtensions.isNullOrEmpty(token.getContainerName()))
        {
            DeleteBlob(token.getContainerName(), blobId);
        }
    }
    public void CopyBlob(String containerName, String desContainerName, String blobId)
    {
        Provider.CopyBlob(containerName, desContainerName, blobId);
    }

    public void CopyBlobs(String containerName, String desContainerName, String[] blobId)
    {
        for (String oneBlobId : blobId)
        {
            Provider.CopyBlob(containerName, desContainerName, oneBlobId);
        }
    }
    
    public void CopyBlobsToOtherTenant(Tenant targetTenant, String[] blobIds, String userId, String encryptionKey, boolean isInternal, boolean singleHandler)
    {
        String sourceTenant = Tenant.getTenantName().toLowerCase();
        IBlobProvider targetProvider = GetProvider(targetTenant);
        String targetTenantName = targetTenant.getTenantName().toLowerCase();
        if (!singleHandler)
        {
            List<BlobInfo> blobs = Provider.GetBlobs(sourceTenant, encryptionKey, isInternal, userId,
                Arrays.asList(blobIds), false, false);
            
            if (blobs == null || blobs.size() <= 0) return;
            for (BlobInfo blobInfo : blobs)
            {
            	if (blobInfo == null) continue;
                targetProvider.SaveBlob(targetTenantName, encryptionKey, isInternal, userId, blobInfo.getId(),
                    blobInfo.getFileType(), blobInfo.getFileFormat(), blobInfo.getFileName(), blobInfo.getData(), false, -1);
            }
        }
        else
        {
            for (String blobid : blobIds)
            {
                BlobInfo blobInfo = Provider.GetBlob(sourceTenant, encryptionKey, isInternal, userId, blobid,
                    false);
                if (blobInfo == null) continue;
                targetProvider.SaveBlob(targetTenantName, encryptionKey, isInternal, userId, blobInfo.getId(),
                    blobInfo.getFileType(), blobInfo.getFileFormat(), blobInfo.getFileName(), blobInfo.getData(), false, -1);
            }
        }
    }
}
