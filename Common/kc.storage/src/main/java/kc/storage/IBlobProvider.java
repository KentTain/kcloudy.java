package kc.storage;

import java.util.Date;
import java.util.List;
import java.util.Map;

import kc.framework.base.BlobInfo;

public interface IBlobProvider {
	WriterBase GetWriter();

    List<String> GetContainers();
    void CreateContainer(String containerName);
    void DeleteContainer(String containerName);
    boolean DoesContainerExist(String containerName);
    List<String> GetBlobIds(String containerName);
    List<String> GetBlobIdsWithMetadata(String containerName);
    boolean DoesBlobExist(String containerName, String blobId);
    BlobInfo GetBlob(String containerName, String encryptionKey, boolean isInternal, String userId, String blobId, boolean useCacheIfAvailable);
    BlobInfo GetBlobWithoutEncryption(String containerName, String blobId, boolean useCacheIfAvailable);
    Date GetBlobLastModifiedTime(String containerName, String blobId);
    Map<String, String> GetBlobMetadata(String containerName, String blobId);
    List<BlobInfo> GetBlobs(String containerName, String encryptionKey, boolean isInternal, String userId,  List<String> blobIds, boolean returnPlaceholderIfNotFound, boolean isUserLevel);
    void SaveBlob(String containerName, String encryptionKey, boolean isInternal, String userId,  String blobId,String type, String format, String fileName, byte[] fileBytes, boolean isUserLevelBlob, long expireAfterSecond);
    void SaveBlobWithoutEncryption(String containerName, BlobInfo blobObject);
    void SaveBlobBlock(String containerName, String blobId, String blockId, byte[] blockBytes, String contentMD5);
    void CommitBlobBlock(String containerName, String blobId, String[] blockIds, Map<String, String> metadata);
    void DeleteBlob(String containerName, String blobId);
    void DeleteBlobByRelativePath(String blobRelativePath);

    void AppendBlobMetadata(String containerName, String blobId, Map<String, String> extraMetadata);

    boolean IsBlobLocked(String containerName, String blobId);
    boolean ObtainBlobLock(String containerName, String blobId);
    void ReleaseBlobLock(String containerName, String blobId);
    void BreakBlobLock(String containerName, String blobId);

    void CopyBlob(String containerName, String desContainerName, String blobId);
}
