package kc.storage.file;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kc.framework.base.BlobInfo;
import kc.framework.extension.StringExtensions;
import kc.storage.BlobResult;
import kc.storage.ReaderBase;

class FileSystemReader extends ReaderBase{
	private Logger logger = LoggerFactory.getLogger(FileSystemReader.class);

	private final String RootPath;

    public FileSystemReader(String rootPath)
    {
    	super();
        this.RootPath = rootPath;
    }
    
	@Override
	public boolean ExistContainer(String containerName) {
		if (StringExtensions.isNullOrEmpty(containerName)) 
			return false; 

        String filePath = this.RootPath + containerName + "\\";
        Path path = Paths.get(filePath);
        
        return Files.exists(path);
	}

	@Override
	public boolean ExistBlob(String containerName, String blobId) {
		if (StringExtensions.isNullOrEmpty(blobId)) 
			return false;
		
		int offset = -1;
        int length = -1;
		BlobResult bResult = GetActualBlobIdAndOffset(blobId, offset, length);
        String realBlobId = bResult.getBlobId();
        offset = bResult.getOffset();
        length = bResult.getLength();
        
        String filePath = this.RootPath + containerName + "\\" + realBlobId;
        Path path = Paths.get(filePath);
        
        return Files.exists(path);
	}

	@Override
	public List<String> ListContainers() {
		String dirName = this.RootPath;
		
		File file = new File(dirName);
		String[] directories = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
        
        return Arrays.asList(directories);
	}

	@Override
	public List<String> ListBlobIds(String containerName) {
		if (StringExtensions.isNullOrEmpty(containerName))
            return new ArrayList<String>();

        String dirName = this.RootPath + containerName;
        File file = new File(dirName);
        String[] fileList = file.list(new FilenameFilter() {
  		  @Override
  		  public boolean accept(File current, String name) {
  		    return new File(current, name).isFile();
  		  }
  		});

        return Arrays.asList(fileList);
	}

	@Override
	public List<String> ListBlobIdsWithMetadata(String container) {
		if (StringExtensions.isNullOrEmpty(container))
            return new ArrayList<String>();

        String dirName = this.RootPath + container;
        File file = new File(dirName);
        String[] fileList = file.list(new FilenameFilter() {
  		  @Override
  		  public boolean accept(File current, String name) {
  		    return new File(current, name).isFile();
  		  }
  		});
        
        return Arrays.asList(fileList);
	}

	@Override
	public BlobInfo GetBlob(String container, String encryptionKey, boolean isInternal, String userId,
			String blobId, boolean attemptCache) {
        int offset = -1;
        int length = -1;
        BlobResult bResult = GetActualBlobIdAndOffset(blobId, offset, length);
        String realBlobId = bResult.getBlobId();
        offset = bResult.getOffset();
        length = bResult.getLength();

        BlobInfo bi = ReadBlob(container, encryptionKey, isInternal, userId, realBlobId);

        if (offset != -1)
        {
            GetBlobSegment(bi, offset, length, blobId);
        }

        logger.debug("Retrieve Blob: " + blobId);
        return bi;
	}

	@Override
	public BlobInfo GetBlobWithoutEncryption(String containerName, String blobId, boolean useCacheIfAvailable) {
		if (StringExtensions.isNullOrEmpty(blobId) || StringExtensions.isNullOrEmpty(containerName)) 
			return null;

        try {
			int offset = -1;
			int length = -1;
			BlobResult bResult = GetActualBlobIdAndOffset(blobId, offset, length);
			String realBlobId = bResult.getBlobId();
			offset = bResult.getOffset();
			length = bResult.getLength();

			BlobInfo bi = new BlobInfo(realBlobId, null, null, null);

			String filePath = this.RootPath + containerName + "\\" + realBlobId;
			Path path = Paths.get(filePath);
			if (!Files.exists(path))
				throw new Exception("Blob " + blobId + " not found");
			
			bi.setData(Files.readAllBytes(path));
			bi.setSize (bi.getData().length);
			
			if (bi.getData().length == 0)
			{
			    throw new Exception("Blob " + blobId + " has zero length");
			}

			if (offset != -1)
			{
			    bi = GetBlobSegment(bi, offset, length, blobId);
			}

			logger.debug("Retrieve Blob: " + blobId);
			return bi;
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			return null;
		}
	}

	@Override
	public Date GetBlobLastModifiedTime(String containerName, String blobId) {
		String fileName = this.RootPath + containerName + "\\" + blobId;
		File file = new File(fileName);
        if (!file.exists())
        {
        	Calendar calendar = Calendar.getInstance();
        	calendar.setTime(new Date(Long.MIN_VALUE));
        	return calendar.getTime();
        }

        return new Date(file.lastModified());
	}

	@Override
	public Map<String, String> GetBlobMetadata(String containerName, String blobId) {
		if (StringExtensions.isNullOrEmpty(blobId) || StringExtensions.isNullOrEmpty(containerName)) 
			return new HashMap<String,String>();

		try {
			String fileName = this.RootPath + containerName + "\\" + blobId + ".metadata";
			Path path = Paths.get(fileName);
			if (!Files.exists(path))
			    return new HashMap<String,String>();

			List<String> lines = Files.readAllLines(path);
			String metadataXml = lines.stream().collect(Collectors.joining(""));
			Map<String, String> blobMetadata = ParseMetadataXml(metadataXml);
			return blobMetadata;
		} catch (IOException e) {
			logger.error(e.getMessage());
			
			return new HashMap<String,String>();
		}
	}

	private BlobInfo ReadBlob(String containerName, String encryptionKey, boolean isInternal, String userId,  String blobId)
    {
        if (StringExtensions.isNullOrEmpty(blobId) || StringExtensions.isNullOrEmpty(containerName)) return null;

        try {
			String filePath = this.RootPath + containerName + "\\" + blobId;
			Path path = Paths.get(filePath);
			if (!Files.exists(path))
			    return null;

			Map<String, String> blobMetadata = GetBlobMetadata(containerName, blobId);
			CheckBlobAccess(containerName, isInternal, userId, blobId, blobMetadata);

			String type = blobMetadata.containsKey("Type") ? blobMetadata.get("Type") : "Unknown";
			String format = blobMetadata.containsKey("Format") ? blobMetadata.get("Format") : "Unknown";
			String fileName = blobMetadata.containsKey("FileName") ? blobMetadata.get("FileName") : "Unknown";
			byte[] data = Files.readAllBytes(path);
			byte[] blobActualData = DecryptIfNeeded(encryptionKey, isInternal, userId, data, blobMetadata);

			if (blobActualData.length == 0)
			    throw new Exception("Blob " + blobId + " has zero length");

			return new BlobInfo(blobId, type, format, fileName, blobActualData);
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			return null;
		}
    }
}
