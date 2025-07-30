package kc.storage;

import java.util.Date;
import java.util.List;
import java.util.Map;

import kc.framework.base.BlobInfo;

public abstract class ReaderBase extends BlobBase {
	
	public ReaderBase() {
		
	}
	
	public abstract boolean ExistContainer(String containerName);
    public abstract boolean ExistBlob(String containerName, String blobId);
    public abstract List<String> ListContainers();
    public abstract List<String> ListBlobIds(String containerName);
    public abstract List<String> ListBlobIdsWithMetadata(String container);
    public abstract BlobInfo GetBlob(String containerName, String encryptionKey, boolean isInternal, String userId, String blobId, boolean attemptCache);
    public abstract BlobInfo GetBlobWithoutEncryption(String containerName, String blobId, boolean useCacheIfAvailable);
    public abstract Date GetBlobLastModifiedTime(String containerName, String blobId);
    public abstract Map<String, String> GetBlobMetadata(String containerName, String blobId);
}
