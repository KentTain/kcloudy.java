package kc.storage.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kc.framework.extension.StringExtensions;
import kc.storage.WriterBase;

class FileSystemWriter extends WriterBase {
	private Logger logger = LoggerFactory.getLogger(FileSystemWriter.class);
	private final String RootPath;

	public FileSystemWriter(String rootPath) {
		this.RootPath = rootPath;
		Path path = Paths.get(this.RootPath);
		if (!Files.exists(path)) {
			try {
				Files.createDirectory(path);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

	@Override
	protected void CreateContainer(String containerName) {
		try {
			String dirName = this.RootPath + containerName;
			Path path = Paths.get(dirName);
			if (!Files.exists(path)) {
				Files.createDirectory(path);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	protected void DeleteContainer(String containerName) {
		try {
			String dirName = this.RootPath + containerName;
			Path path = Paths.get(dirName);
			Files.deleteIfExists(path);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	protected void WriteBlob(String container, String blobId, byte[] blobData, Map<String, String> blobMetadata) {
		CreateContainer(container);
		
		try {
			String fileName = this.RootPath + container + "\\" + blobId;
			Path path = Paths.get(fileName);
			Files.write(path, blobData);
			if (blobMetadata != null) {
				WriteBlobMetadata(container, blobId, true, blobMetadata);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	protected void WriteBlobBlock(String container, String blobId, String blobBlockId, byte[] blobBlockData, String contentMD5) {
		CreateContainer(container);
		
		try {
			String fileName = this.RootPath + container + "\\" + blobId + ".Uploading." + blobBlockId;
			Path path = Paths.get(fileName);
			Files.write(path, blobBlockData);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
    protected void SubmitBlobBlock(String container, String blobId, String[] blobBlockIds, Map<String, String> blobMetadata) {
        CreateContainer(container);

        
        try 
        {
        	String fileName = this.RootPath + container + "\\" + blobId;
            Path path = Paths.get(fileName);
            Files.deleteIfExists(path);
        	
            String blockFileName;
            Path blockFilePath;
            byte[] blockBytes;
            for (String blockId : blobBlockIds)
            {
                blockFileName = fileName + ".Uploading." + blockId;
                blockFilePath = Paths.get(blockFileName);
                blockBytes = Files.readAllBytes(blockFilePath);
                
                Files.write(path, blockBytes, StandardOpenOption.APPEND);
            	
                Files.deleteIfExists(blockFilePath);
            }
        } catch (IOException e) {
			logger.error(e.getMessage());
		}
        if (blobMetadata != null)
        {
            WriteBlobMetadata(container, blobId, true, blobMetadata);
        }
    }

	@Override
    protected  void WriteBlobMetadata(String container, String blobId, boolean clearExisting, Map<String, String> blobMetadata) {
        try {
			String fileName = this.RootPath + container + "\\" + blobId + ".metadata";
			Path path = Paths.get(fileName);
			if (!clearExisting) {
				if (Files.exists(path)) {
					List<String> lines = Files.readAllLines(path);
					String originalXml = lines.stream().collect(Collectors.joining(""));
			        Map<String, String> originalMetadata = ParseMetadataXml(originalXml);
			        for (String key : originalMetadata.keySet())
			        {
			            if (!blobMetadata.containsKey(key))
			            {
			                blobMetadata.put(key, originalMetadata.get(key));
			            }
			        }
			    }
			}
			String metadataXml = MetadataToXml(blobMetadata);
			Path metaPath = Paths.get(fileName);
			Files.write(metaPath, metadataXml.getBytes("UTF-8"));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
    }

	@Override
	protected void DeleteBlob(String container, String blobId) {
		String realBlobId = GetActualBlobId(blobId);
		String fileName = this.RootPath + container + "\\" + realBlobId;
		Path path = Paths.get(fileName);

		String fileNameMetadata = this.RootPath + container + "\\" + realBlobId + ".metadata";
		Path pathMetadata = Paths.get(fileNameMetadata);
		try {
			Files.deleteIfExists(path);
			Files.deleteIfExists(pathMetadata);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void CopyBlob(String containerName, String desContainerName, String blobId) {
		if (StringExtensions.isNullOrEmpty(blobId))
			return;

		// this.Client.RetryPolicy = RetryPolicies.Retry(20, TimeSpan.Zero);
		CreateContainer(desContainerName);
		
		String fileName = this.RootPath + containerName + "\\" + blobId;
		String desFileName = this.RootPath + desContainerName + "\\" + blobId;
		Path fileNamePath = Paths.get(fileName);
		Path desFileNamePath = Paths.get(desFileName);
		
		String fileMetaName = this.RootPath + containerName + "\\" + blobId + ".metadata";
		String desMetaFileName = this.RootPath + desContainerName + "\\" + blobId + ".metadata";
		Path fileMetaNamePath = Paths.get(fileMetaName);
		Path desMetaFileNamePath = Paths.get(desMetaFileName);
		
		try {
			Files.copy(fileNamePath, desFileNamePath); // 复制文件
			Files.copy(fileMetaNamePath, desMetaFileNamePath); // 复制文件
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

}
