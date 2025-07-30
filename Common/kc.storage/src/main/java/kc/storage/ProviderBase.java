package kc.storage;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.microsoft.azure.storage.blob.CloudBlob;

import kc.framework.base.BlobInfo;
import kc.storage.util.Encryption;

public abstract class ProviderBase extends BlobBase implements IBlobProvider {
	protected byte[] DecryptIfNeeded(byte[] data, Map<String, String> metadata, String token) {
		boolean isEncrypted = metadata.containsKey("IsEncrypted")
				? isEncrypted = Boolean.parseBoolean(metadata.get("IsEncrypted"))
				: false;
		if (isEncrypted) {
			boolean isUserLevelBlob = metadata.containsKey("IsUserLevelBlob")
					? Boolean.parseBoolean(metadata.get("IsUserLevelBlob"))
					: false;
			String encryptionKey = Encryption.GetEncryptionKey(token, isUserLevelBlob, false, null);
			return Encryption.Decrypt(data, encryptionKey);
		} else {
			return data;
		}
	}

	protected Map<String, String> GetMetadata(CloudBlob blob) {
		Map<String, String> metadataDict = new HashMap<String, String>();
		try {
			//blob.FetchAttributesAsync();
			blob.downloadAttributes();
		} catch (Exception ex) {
			// Somehow this is happening in debug mode sometimes.
		}

		if (blob.getMetadata() != null) {
			GetOneMetadata(blob, metadataDict, "Format");
			GetOneMetadata(blob, metadataDict, "Id");
			GetOneMetadata(blob, metadataDict, "LastModifiedTime");
			GetOneMetadata(blob, metadataDict, "CreateTime");
			GetOneMetadata(blob, metadataDict, "Size");
			GetOneMetadata(blob, metadataDict, "Tenant");
			GetOneMetadata(blob, metadataDict, "UserId");
			GetOneMetadata(blob, metadataDict, "FileName");
			GetOneMetadata(blob, metadataDict, "Expiration");
			GetOneMetadata(blob, metadataDict, "IsEncrypted");
			GetOneMetadata(blob, metadataDict, "IsUserLevelBlob");
		}

		return metadataDict;
	}

	protected Map<String, String> GetCacheMetadata(String filename) {
        String metadataFileName = filename + ".metadata";

        File file = new File(metadataFileName);
        if (!file.exists())
        {
            return new HashMap<String, String>();
        }

        Map<String, String> metadataDict = new HashMap<String, String>();

        try
        {
        	// 初始化xml解析工厂
        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         	// 创建DocumentBuilder
        	DocumentBuilder builder = factory.newDocumentBuilder();
        	// 创建解析xml的Document
        	Document metadataXDoc = builder.parse(file);
        	Element root = metadataXDoc.getDocumentElement();
        	
            if (root == null) return metadataDict;
            
            NodeList nodes = root.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					//Element child = (Element) node;
					metadataDict.put(node.getLocalName(), node.getNodeValue());
				}
			}
            
            return metadataDict;
        }
        catch (Exception ex)
        {
            return new HashMap<String, String>();
        }
    }

	protected void GetOneMetadata(CloudBlob blob, Map<String, String> metadataDict, String metadataName) {
		String metadata = blob.getMetadata().get(metadataName);
		if (metadata != null) {
			if (!metadataDict.containsKey(metadataName)) {
				metadataDict.put(metadataName, metadata);
			}
		}
	}

	public abstract WriterBase GetWriter();

	public abstract List<String> GetContainers();

	public abstract boolean DoesContainerExist(String containerName);

	public abstract void CreateContainer(String containerName);

	public abstract void DeleteContainer(String containerName);

	public abstract List<String> GetBlobIds(String containerName);

	public abstract List<String> GetBlobIdsWithMetadata(String containerName);

	public abstract boolean DoesBlobExist(String containerName, String blobId);

	public abstract BlobInfo GetBlob(String containerName, String encryptionKey, boolean isInternal, String userId,
			String blobId, boolean useCacheIfAvailable);

	public abstract BlobInfo GetBlobWithoutEncryption(String containerName, String blobId, boolean useCacheIfAvailable);

	public abstract Date GetBlobLastModifiedTime(String containerName, String blobId);

	public abstract Map<String, String> GetBlobMetadata(String containerName, String blobId);

	public abstract List<BlobInfo> GetBlobs(String containerName, String encryptionKey, boolean isInternal, String userId,
			List<String> blobIds, boolean returnPlaceholderIfNotFound, boolean isUserLevel);

	public abstract void SaveBlob(String containerName, String encryptionKey, boolean isInternal, String userId,
			String blobId, String type, String format, String fileName, byte[] fileBytes,
			boolean isUserLevelBlob, long expireAfterSecond);

	public abstract void SaveBlobWithoutEncryption(String containerName, BlobInfo blobObject);

	public abstract void AppendBlobMetadata(String containerName, String blobId, Map<String, String> extraMetadata);

	public abstract void SaveBlobBlock(String containerName, String blobId, String blockId, byte[] blockBytes,
			String contentMD5);

	public abstract void CommitBlobBlock(String containerName, String blobId, String[] blockIds,
			Map<String, String> metadata);

	public abstract void DeleteBlob(String containerName, String blobId);

	public abstract void DeleteBlobByRelativePath(String blobRelativePath);

	public abstract boolean IsBlobLocked(String containerName, String blobId);

	public abstract boolean ObtainBlobLock(String containerName, String blobId);

	public abstract void ReleaseBlobLock(String containerName, String blobId);

	public abstract void BreakBlobLock(String containerName, String blobId);

	public abstract void CopyBlob(String containerName, String desContainerName, String blobId);
}
