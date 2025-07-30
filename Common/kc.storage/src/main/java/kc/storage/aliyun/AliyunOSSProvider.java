package kc.storage.aliyun;

import kc.framework.GlobalConfig;
import kc.framework.base.BlobInfo;
import kc.framework.extension.StringExtensions;
import kc.framework.tenant.Tenant;
import kc.storage.BlobCache;
import kc.storage.IBlobProvider;
import kc.storage.ProviderBase;
import kc.storage.WriterBase;
import kc.storage.util.Encryption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AliyunOSSProvider extends ProviderBase implements IBlobProvider {
	private Logger logger = LoggerFactory.getLogger(AliyunOSSProvider.class);
	private String TempFilePath = "";
	private String StorageConnectionString = "";
	private BlobCache Cache = null;
	private AliyunOSSReader Reader = null;
	private AliyunOSSWriter Writer = null;

	// public AliyunOSSProvider()
	// {
	// this.TempFilePath = ConfigUtil.GetConfigItem("TempFilePath") + "\\";
	// this.StorageConnectionString =
	// ConfigUtil.GetConfigItem("StorageConnectionString");
	// this.Cache = new AzureCache(this.TempFilePath);
	// Reader = new AliyunOSSReader(StorageConnectionString, Cache);
	// Writer = new AliyunOSSWriter(StorageConnectionString, Cache);
	// }
	public AliyunOSSProvider(Tenant tenant) {
		try {
			this.TempFilePath = GlobalConfig.TempFilePath + "\\";
			this.StorageConnectionString = tenant.GetDecryptStorageConnectionString();
			this.Cache = new BlobCache(this.TempFilePath);
			Reader = new AliyunOSSReader(StorageConnectionString, Cache);
			Writer = new AliyunOSSWriter(StorageConnectionString, Cache);
		} catch (Exception ex) {
			logger.error("Initilize the AWS S3 Provider connection throw error. " + ex.getMessage());
		}
	}

	public AliyunOSSProvider(String connectionString) {
		this.TempFilePath = GlobalConfig.TempFilePath + "\\";
		this.StorageConnectionString = connectionString;
		this.Cache = new BlobCache(this.TempFilePath);
		Reader = new AliyunOSSReader(StorageConnectionString, Cache);
		Writer = new AliyunOSSWriter(StorageConnectionString, Cache);
	}

	@Override
	public WriterBase GetWriter() {
		return this.Writer;
	}

	@Override
	public List<String> GetContainers() {
		return this.Reader.ListContainers();
	}

	@Override
	public boolean DoesContainerExist(String containerName) {
		return this.Reader.ExistContainer(containerName);
	}

	@Override
	public void CreateContainer(String containerName) {
		this.Writer.AddContainer(containerName);
	}

	@Override
	public void DeleteContainer(String containerName) {
		this.Writer.RemoveContainer(containerName);
	}

	@Override
	public List<String> GetBlobIds(String containerName) {
		return this.Reader.ListBlobIds(containerName);
	}

	@Override
	public List<String> GetBlobIdsWithMetadata(String containerName) {
		return this.Reader.ListBlobIdsWithMetadata(containerName);
	}

	@Override
	public boolean DoesBlobExist(String containerName, String blobId) {
		return this.Reader.ExistBlob(containerName, blobId);
	}

	@Override
	public BlobInfo GetBlob(String containerName, String encryptionKey, boolean isInternal, String userId,
			String blobId, boolean useCacheIfAvailable) {
		return this.Reader.GetBlob(containerName, encryptionKey, isInternal, userId,  blobId,
				useCacheIfAvailable);
	}

	@Override
	public BlobInfo GetBlobWithoutEncryption(String containerName, String blobId, boolean useCacheIfAvailable) {
		return this.Reader.GetBlobWithoutEncryption(containerName, blobId, useCacheIfAvailable);
	}

	@Override
	public Date GetBlobLastModifiedTime(String containerName, String blobId) {
		return this.Reader.GetBlobLastModifiedTime(containerName, blobId);
	}

	@Override
	public Map<String, String> GetBlobMetadata(String containerName, String blobId) {
		return this.Reader.GetBlobMetadata(containerName, blobId);
	}

	@Override
	public List<BlobInfo> GetBlobs(String containerName, String encryptionKey, boolean isInternal, String userId,
			List<String> ids, boolean returnPlaceholderIfNotFound, boolean isUserLevel) {
		boolean attemptCache = false;
		List<BlobInfo> blobInfos = new ArrayList<BlobInfo>();
		for (String blobId : ids) {
			BlobInfo blob = this.Reader.GetBlob(containerName, encryptionKey, isInternal, userId,  blobId,
					attemptCache);
			blobInfos.add(blob);
		}
		return blobInfos;
	}

	@Override
	public void SaveBlob(String containerName, String encryptionKey, boolean isInternal, String userId,
			String blobId, String type, String format, String fileName, byte[] fileBytes, boolean isUserLevelBlob,
			long expireAfterSecond) {
		encryptionKey = Encryption.GetEncryptionKey(encryptionKey, isUserLevelBlob, isInternal, userId);

		boolean isEncypted = false;
		byte[] dataToWrite;
		if (!StringExtensions.isNullOrEmpty(encryptionKey)) {
			isEncypted = true;
			dataToWrite = Encryption.Encrypt(fileBytes, encryptionKey);
			return;
		} else {
			dataToWrite = fileBytes;
		}

		long fileSize = fileBytes.length;
		Map<String, String> extraMetadata = null;
		Map<String, String> metadata = BuildMetadata(containerName, isInternal, userId, blobId, type, format, fileName,
				fileSize, expireAfterSecond, isEncypted, isUserLevelBlob, extraMetadata);
		this.Writer.SaveBlob(containerName, blobId, dataToWrite, metadata);
	}

	@Override
	public void SaveBlobWithoutEncryption(String containerName, BlobInfo blobObject) {
		this.Writer.SaveBlob(containerName, blobObject.getId(), blobObject.getData(), null);
	}

	@Override
	public void AppendBlobMetadata(String containerName, String blobId, Map<String, String> metadata) {
		this.Writer.AppendBlobMetadata(containerName, blobId, metadata);
	}

	@Override
	public void SaveBlobBlock(String containerName, String blobId, String blobBlockId, byte[] blockBytes,
			String contentMD5) {
		this.Writer.SaveBlobBlock(containerName, blobId, blobBlockId, blockBytes, contentMD5);
	}

	@Override
	public void CommitBlobBlock(String containerName, String blobId, String[] blockIds, Map<String, String> metadata) {
		this.Writer.CommitBlobBlock(containerName, blobId, blockIds, metadata);
	}

	@Override
	public void DeleteBlob(String containerName, String blobId) {
		this.Writer.RemoveBlob(containerName, blobId);
	}

	@Override
	public void DeleteBlobByRelativePath(String blobRelativePath) {
		this.Writer.RemoveBlob(null, blobRelativePath);
	}

	@Override
	public boolean IsBlobLocked(String containerName, String blobId) {
		String fileName = this.TempFilePath + containerName + "\\" + blobId;
		Path path = Paths.get(fileName);
		if (!Files.exists(path))
			return false;
		File fi = null;
		try {
			fi = new File(fileName);
			fi.setReadable(false);
			
			return false;
		} catch (Exception ex) {
			return true;
		} finally {
			if(fi != null)
				fi.setReadable(true);
		}
	}

	@Override
	public boolean ObtainBlobLock(String containerName, String blobId) {
		while (IsBlobLocked(containerName, blobId)) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public void ReleaseBlobLock(String containerName, String blobId) {
		// Do nothing
	}

	@Override
	public void BreakBlobLock(String containerName, String blobId) {
		// Do nothing
	}

	@Override
	public void CopyBlob(String containerName, String desContainerName, String blobId) {
		this.Writer.CopyBlob(containerName, desContainerName, blobId);
	}
}
