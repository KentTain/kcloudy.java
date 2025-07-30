package kc.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kc.framework.base.BlobInfo;

public class BlobCache extends BlobBase{
	private Logger logger = LoggerFactory.getLogger(BlobCache.class);
	private final String TempStorageFolder;
	
    public BlobCache(String tempStorageFolder)
    {
    	super();
        this.TempStorageFolder = tempStorageFolder;
    }

    private Map<String, String> GetCacheMetadata(String filename)
    {
        String metadataFileName = filename + ".metadata";
        Path path = Paths.get(metadataFileName);
        if (!Files.exists(path))
            return new HashMap<String, String>();

        try
        {
        	List<String> lines = Files.readAllLines(path);
            String metadataXml = lines.stream().collect(Collectors.joining(""));
            return ParseMetadataXml(metadataXml);
        }
        catch (Exception ex)
        {
        	logger.error(ex.getMessage());
            return new HashMap<String, String>();
        }
    }

    public void Insert(String container, String blobId, byte[] blobContent, byte[] blobMetadata)
    {
    	try {
			Path path = Paths.get(this.TempStorageFolder);
			if (!Files.exists(path))
			{
				Files.createDirectory(path);
			}

			String filename = this.TempStorageFolder + container + "__" + blobId;
			Path tempPath = Paths.get(filename);
			Path tempMetaPath = Paths.get(filename + ".metadata");
			Files.write(tempPath, blobContent);
			Files.write(tempMetaPath, blobMetadata);
		} catch (IOException ex) {
			logger.error(ex.getMessage());
		}
    }

    public void InsertWithoutEncryption(String container, String blobId, byte[] blobContent)
    {
        try {
        	String filename = this.TempStorageFolder + container + "__" + blobId;
            Path path = Paths.get(filename);
            Files.write(path, blobContent);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
    }

    public boolean Exists(String container, String blobId)
    {
        String cacheFilename = this.TempStorageFolder + container + "__" + blobId;
        Path path = Paths.get(cacheFilename);
        return Files.exists(path);
    }

    public BlobInfo GetBlob(String container, String encryptionKey, boolean isInternal, String userId, String blobId) throws Exception
    {
        String filePath = this.TempStorageFolder + container + "__" + blobId;
        Path path = Paths.get(filePath);
        Map<String, String> blobMetadata = GetCacheMetadata(filePath);
        CheckBlobAccess(container, isInternal, userId, blobId, blobMetadata);

        String type = blobMetadata.containsKey("Type") ? blobMetadata.get("Type") : "Unknown";
        String format = blobMetadata.containsKey("Format") ? blobMetadata.get("Format") : "Unknown";
        String fileName = blobMetadata.containsKey("FileName") ? blobMetadata.get("FileName") : "Unknown";
        byte[] data = Files.readAllBytes(path);
        byte[] blobActualData = DecryptIfNeeded(encryptionKey, isInternal, userId, data, blobMetadata);

        if (blobActualData.length == 0)
            throw new Exception("Blob " + blobId + " has zero length from cache");

        return new BlobInfo(blobId, type, format, fileName, blobActualData);
    }

    public BlobInfo GetBlobWithoutEncryption(String container, String blobId)
    {
        try {
        	String filename = this.TempStorageFolder + container + "__" + blobId;
            Path path = Paths.get(filename);
            return new BlobInfo(blobId, null, null, null, Files.readAllBytes(path));
		} catch (IOException e) {
			logger.error(e.getMessage());
			return null;
		}
    }

    public void Delete(String container, String blobId)
    {
        try {
			String filename = this.TempStorageFolder + container + "__" + blobId;
			Path path = Paths.get(filename);
			Path metaPath = Paths.get(filename+ ".metadata");
			Files.deleteIfExists(path);
			Files.deleteIfExists(metaPath);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
    }
}
