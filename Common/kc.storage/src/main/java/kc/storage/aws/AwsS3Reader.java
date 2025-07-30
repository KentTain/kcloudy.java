package kc.storage.aws;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import kc.framework.base.BlobInfo;
import kc.framework.extension.DateExtensions;
import kc.framework.extension.StringExtensions;
import kc.framework.tenant.ConnectionKeyConstant;
import kc.storage.BlobCache;
import kc.storage.BlobResult;
import kc.storage.ReaderBase;

class AwsS3Reader extends ReaderBase {
	private Logger logger = LoggerFactory.getLogger(AwsS3Reader.class);
	// private AmazonS3Config Config;
	// private AWSCredentials Credentials;
	// private IAmazonS3 client;
	private final BlobCache Cache;
	private final String endpoint;
	private final String accessKey;
	private final String secretKey;
	private final String region;

	public AwsS3Reader(String connectionString, BlobCache cache) {
		if (StringExtensions.isNullOrEmpty(connectionString))
			throw new IllegalArgumentException("AWS S3's NAS reader connect String is empy or null.");

		Dictionary<String, String> keyValues = StringExtensions.keyValuePairFromConnectionString(connectionString);
		endpoint = keyValues.get(ConnectionKeyConstant.BlobEndpoint);
		accessKey = keyValues.get(ConnectionKeyConstant.AccessName);
		secretKey = keyValues.get(ConnectionKeyConstant.AccessKey);
		region = keyValues.get(ConnectionKeyConstant.VALUE_1);
		if (StringExtensions.isNullOrEmpty(endpoint))
			throw new IllegalArgumentException(
					"AWS S3's NAS reader connect String is wrong. It can't set the BlobEndpoint Value.");
		if (StringExtensions.isNullOrEmpty(accessKey))
			throw new IllegalArgumentException(
					"AWS S3's NAS reader connect String is wrong. It can't set the AccessKey Value.");
		if (StringExtensions.isNullOrEmpty(secretKey))
			throw new IllegalArgumentException(
					"AWS S3's NAS reader connect String is wrong. It can't set the SecretAccountKey Value.");

		// var config = new AmazonS3Config()
		// {
		// ServiceURL = endpoint,
		// ForcePathStyle = true,
		// SignatureVersion = "2",
		// BufferSize = 15 * 1024 * 1024, //15M
		// //RegionEndpoint = Amazon.RegionEndpoint.CNNorth1
		// //RegionEndpoint = Amazon.RegionEndpoint.USEast1,
		// };
		// var credentials = new BasicAWSCredentials(accessKey, secretKey);
		// client = new AmazonS3Client(credentials, config);

		// ServicePointManager.SecurityProtocol =
		// SecurityProtocolType.Tls
		// | SecurityProtocolType.Tls11
		// | SecurityProtocolType.Tls12;
		//// ServicePointManager.CertificatePolicy = new TrustAllCertificatePolicy();
		// ServicePointManager.ServerCertificateValidationCallback =
		// CheckValidationResult;

		this.Cache = cache;
	}

	protected AmazonS3 GetAWSClient() {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		EndpointConfiguration endpointConfig = new EndpointConfiguration(endpoint, null);
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(region).withClientConfiguration(clientConfig)
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withEndpointConfiguration(endpointConfig).withPathStyleAccessEnabled(true).build();
		return s3Client;
	}

	@Override
	public boolean ExistContainer(String containerName) {
		AmazonS3 client = GetAWSClient();
		try {
			return client.doesBucketExistV2(containerName);
		} catch (AmazonServiceException e) {
			logger.error(String.format("--Container[%s]--AwsS3Reader.ExistContainer throw error: %s", containerName,
					e.getMessage()));
			return false;
		} catch (SdkClientException e) {
			logger.error(String.format("--Container[%s]--AwsS3Reader.ExistContainer throw error: %s", containerName,
					e.getMessage()));
			return false;
		} finally {
			if (client != null) {
				client.shutdown();
			}
		}
	}

	@Override
	public boolean ExistBlob(String containerName, String blobId) {
		AmazonS3 client = GetAWSClient();
		try {
			return client.doesObjectExist(containerName, blobId);
		} catch (AmazonServiceException e) {
			logger.error(String.format("--Container[%s]--AwsS3Reader.ExistBlob throw error: %s", containerName,
					e.getMessage()));
			return false;
		} catch (SdkClientException e) {
			logger.error(String.format("--Container[%s]--AwsS3Reader.ExistBlob throw error: %s", containerName,
					e.getMessage()));
			return false;
		} finally {
			if (client != null) {
				client.shutdown();
			}
		}
	}

	@Override
	public List<String> ListContainers() {
		AmazonS3 client = GetAWSClient();
		try {
			List<Bucket> response = client.listBuckets();
			if (response == null || response.size() <= 0)
				return new ArrayList<String>();

			return response.stream().map(m -> m.getName()).collect(Collectors.toList());
		} catch (AmazonServiceException e) {
			logger.error(String.format("--Container--AwsS3Reader.ListContainers throw error: %s", e.getMessage()));
			return new ArrayList<String>();
		} catch (SdkClientException e) {
			logger.error(String.format("--Container--AwsS3Reader.ListContainers throw error: %s", e.getMessage()));
			return new ArrayList<String>();
		} finally {
			if (client != null) {
				client.shutdown();
			}
		}
	}

	@Override
	public List<String> ListBlobIds(String containerName) {
		AmazonS3 client = GetAWSClient();
		try {
			ListObjectsV2Result response = client.listObjectsV2(containerName);
			if (response == null || response.getObjectSummaries().size() <= 0)
				return new ArrayList<String>();

			return response.getObjectSummaries().stream().map(m -> m.getKey()).collect(Collectors.toList());
		} catch (AmazonServiceException e) {
			logger.error(String.format("--Container[%s]--AwsS3Reader.ListBlobIds throw error: %s", containerName,
					e.getMessage()));
			return new ArrayList<String>();
		} catch (SdkClientException e) {
			logger.error(String.format("--Container[%s]--AwsS3Reader.ListBlobIds throw error: %s", containerName,
					e.getMessage()));
			return new ArrayList<String>();
		} finally {
			if (client != null) {
				client.shutdown();
			}
		}
	}

	@Override
	public List<String> ListBlobIdsWithMetadata(String container) {
		return ListBlobIds(container);
	}

	@Override
	public BlobInfo GetBlob(String container, String encryptionKey, boolean isInternal, String userId,
			String blobId, boolean useCacheIfAvailable) {
		if (StringExtensions.isNullOrEmpty(blobId) || StringExtensions.isNullOrEmpty(container))
			return null;

		int offset = -1;
		int length = -1;
		BlobResult bResult = GetActualBlobIdAndOffset(blobId, offset, length);
		String realBlobId = bResult.getBlobId();
		offset = bResult.getOffset();
		length = bResult.getLength();
		if (useCacheIfAvailable && this.Cache.Exists(container, realBlobId)) {
			try {
				BlobInfo bi = this.Cache.GetBlob(container, encryptionKey, isInternal, userId,  realBlobId);
				if (offset != -1) {
					bi = GetBlobSegment(bi, offset, length, blobId);
				}
				return bi;
			} catch (Exception e) {
				logger.error(
						String.format("--Container[%s]--AwsS3Reader.GetBlob throw error: %s", container, e.getMessage()));
				return null;
			}
		} else {
//			byte[] cacheData = null;
//			byte[] cacheMetadata = null;
			BlobInfo bi = GetBlobWithCache(container, encryptionKey, isInternal, userId, realBlobId,
					useCacheIfAvailable);
			// LogUtil.LogDebug("Retrieve Blob from AWS S3 store. ", blobId,
			// sw.ElapsedMilliseconds);

//			if (useCacheIfAvailable && cacheData != null) {
//				this.Cache.Insert(container, realBlobId, cacheData, cacheMetadata);
//			}
			if (offset != -1) {
				bi = GetBlobSegment(bi, offset, length, blobId);
			}
			return bi;
		}
	}

	@SuppressWarnings("null")
	@Override
	public BlobInfo GetBlobWithoutEncryption(String containerName, String blobId, boolean useCacheIfAvailable) {
		if (StringExtensions.isNullOrEmpty(blobId) || StringExtensions.isNullOrEmpty(containerName))
			return null;

		int offset = -1;
		int length = -1;
		BlobResult bResult = GetActualBlobIdAndOffset(blobId, offset, length);
		String realBlobId = bResult.getBlobId();
		offset = bResult.getOffset();
		length = bResult.getLength();

		if (useCacheIfAvailable && this.Cache.Exists(containerName, realBlobId)) {
			BlobInfo bi = this.Cache.GetBlobWithoutEncryption(containerName, realBlobId);
			// LogUtil.LogDebug("Retrieve Blob from local cache store. ", blobId,
			// sw.ElapsedMilliseconds);
			if (offset != -1) {
				bi = GetBlobSegment(bi, offset, length, blobId);
			}
			return bi;
		} else {
			AmazonS3 client = GetAWSClient();
			try {
				try (S3Object response = client.getObject(containerName, blobId)) {
					if (null == response)
						return null;

					S3ObjectInputStream s3is = response.getObjectContent();

					String rootPath = System.getProperty("user.dir");
					String filePath = StringExtensions.endWithSlash(rootPath) + "File/" + UUID.randomUUID();
					// TODO：先下载到本地，再读取成字节流，需要替换成直接读取流的方式：response.ResponseStream
					FileOutputStream fos = new FileOutputStream(new File(filePath));

					byte[] data = new byte[1024];
					int read_len = 0;
					while ((read_len = s3is.read(data)) > 0) {
						fos.write(data, 0, read_len);
					}
					s3is.close();
					fos.close();

					ObjectMetadata objMetaData = response.getObjectMetadata();

					Map<String, String> blobMetadata = new HashMap<String, String>();
					ObjectMetadata objMetadata = response.getObjectMetadata();
					if (null != objMetadata || objMetadata.getUserMetadata().size() > 0) {
						Map<String, String> userMetadata = objMetadata.getUserMetadata();
						for (String key : userMetadata.keySet()) {
							//String k = key.replace("x-amz-meta-", "");
							blobMetadata.put(key, userMetadata.get(key));
						}
					}

					String type = StringExtensions.isNullOrEmpty(blobMetadata.get("Type")) ? blobMetadata.get("Type")
							: "Unknown";
					String format = StringExtensions.isNullOrEmpty(blobMetadata.get("Format"))
							? blobMetadata.get("Format")
							: "Unknown";
					String fileName = StringExtensions.isNullOrEmpty(blobMetadata.get("FileName"))
							? blobMetadata.get("FileName")
							: "Unknown";

					if (objMetaData != null && !StringExtensions.isNullOrEmpty(objMetaData.getContentType())) {
						type = objMetaData.getContentType();
					}
					BlobInfo blobInfo = new BlobInfo(blobId, type, format, fileName, data);
					blobInfo.setSize(blobInfo.getData().length);
					// LogUtil.LogDebug("Retrieve Blob from AWS S3 store. ", blobId,
					// sw.ElapsedMilliseconds);

					if (useCacheIfAvailable) {
						this.Cache.InsertWithoutEncryption(containerName, blobId, blobInfo.getData());
					}
					if (offset != -1) {
						blobInfo = GetBlobSegment(blobInfo, offset, length, blobId);
					}
					return blobInfo;
				}
			} catch (SdkClientException e) {
				logger.error(String.format("--Container[%s]--AwsS3Reader.GetBlobWithoutEncryption throw error: %s",
						containerName, e.getMessage()));
				return null;
			} catch (Exception e) {
				logger.error(String.format("--Container[%s]--AwsS3Reader.GetBlobWithoutEncryption throw error: %s",
						containerName, e.getMessage()));
				return null;
			} finally {
				if (client != null) {
					client.shutdown();
				}
			}

		}
	}

	@Override
	public Date GetBlobLastModifiedTime(String containerName, String blobId) {
		AmazonS3 client = GetAWSClient();
		try {
			ObjectMetadata response = client.getObjectMetadata(containerName, blobId);
			return response.getLastModified();
		} catch (AmazonServiceException e) {
			logger.error(String.format("--Container[%s]--AwsS3Reader.GetBlobLastModifiedTime throw error: %s",
					containerName, e.getMessage()));
			return DateExtensions.getMinValue();
		} catch (SdkClientException e) {
			logger.error(String.format("--Container[%s]--AwsS3Reader.GetBlobLastModifiedTime throw error: %s",
					containerName, e.getMessage()));
			return DateExtensions.getMinValue();
		} finally {
			if (client != null) {
				client.shutdown();
			}
		}
	}

	@Override
	public Map<String, String> GetBlobMetadata(String containerName, String blobId) {
		Map<String, String> blobMetadata = new HashMap<String, String>();
		AmazonS3 client = GetAWSClient();
		try {

			ObjectMetadata response = client.getObjectMetadata(containerName, blobId);
			if (null == response || response.getUserMetadata().size() <= 0)
				return blobMetadata;

			Map<String, String> userMetadata = response.getUserMetadata();
			for (String key : userMetadata.keySet()) {
				blobMetadata.put(key, userMetadata.get(key));
			}

			return blobMetadata;
		} catch (AmazonServiceException e) {
			logger.error(String.format("--Container[%s]--AwsS3Reader.GetBlobMetadata throw error: %s", containerName,
					e.getMessage()));
			return blobMetadata;
		} catch (SdkClientException e) {
			logger.error(String.format("--Container[%s]--AwsS3Reader.GetBlobMetadata throw error: %s", containerName,
					e.getMessage()));
			return blobMetadata;
		} finally {
			if (client != null) {
				client.shutdown();
			}
		}
	}

	private byte[] cacheData = null;
	private byte[] cacheMetadata = null;
	@SuppressWarnings("null")
	private BlobInfo GetBlobWithCache(String containerName, String encryptionKey, boolean isInternal, String userId,
			 String blobId, boolean generateCacheData) {
		cacheData = null;
		cacheMetadata = null;

		if (StringExtensions.isNullOrEmpty(blobId))
			return null;

		AmazonS3 client = GetAWSClient();
		try {
			try (S3Object response = client.getObject(containerName, blobId)) {
				if (response == null) {
					return null;
				}

				Map<String, String> blobMetadata = new HashMap<String, String>();
				ObjectMetadata objMetadata = response.getObjectMetadata();
				if (null != objMetadata || objMetadata.getUserMetadata().size() > 0) {
					Map<String, String> userMetadata = objMetadata.getUserMetadata();
					for (String key : userMetadata.keySet()) {
						//String k = key.replace("x-amz-meta-", "");
						blobMetadata.put(key, userMetadata.get(key));
					}
				}

				CheckBlobAccess(containerName, isInternal, userId, blobId, blobMetadata);

				S3ObjectInputStream s3is = response.getObjectContent();

				String rootPath = System.getProperty("user.dir");
				String filePath = StringExtensions.endWithSlash(rootPath) + "File/" + UUID.randomUUID();
				// TODO：先下载到本地，再读取成字节流，需要替换成直接读取流的方式：response.ResponseStream
				FileOutputStream fos = new FileOutputStream(new File(filePath));

				byte[] data = new byte[1024];
				int read_len = 0;
				while ((read_len = s3is.read(data)) > 0) {
					fos.write(data, 0, read_len);
				}
				s3is.close();
				fos.close();

				// Store Cache Data
				if (generateCacheData) {
					cacheData = new byte[data.length];
					System.arraycopy(data, 0, cacheData, 0, data.length);

					String metadataXml = MetadataToXml(blobMetadata);
					cacheMetadata = metadataXml.getBytes("UTF-8");
				} else {
					cacheData = null;
					cacheMetadata = null;
				}

				String type = StringExtensions.isNullOrEmpty(blobMetadata.get("Type")) ? blobMetadata.get("Type")
						: "Unknown";
				String format = StringExtensions.isNullOrEmpty(blobMetadata.get("Format")) ? blobMetadata.get("Format")
						: "Unknown";
				String fileName = StringExtensions.isNullOrEmpty(blobMetadata.get("FileName"))
						? blobMetadata.get("FileName")
						: "Unknown";

				byte[] blobActualData = DecryptIfNeeded(encryptionKey, isInternal, userId, data, blobMetadata);
				BlobInfo bi = new BlobInfo(blobId, type, format, fileName, blobActualData);
				bi.setSize(bi.getData().length);
				return bi;
			}
		} catch (AmazonServiceException e) {
			logger.error(String.format("--Container[%s]--AwsS3Reader.GetBlobWithCache throw error: %s", containerName,
					e.getMessage()));
			return null;
		} catch (SdkClientException e) {
			logger.error(String.format("--Container[%s]--AwsS3Reader.GetBlobWithCache throw error: %s", containerName,
					e.getMessage()));
			return null;
		} catch (Exception e) {
			logger.error(String.format("--Container[%s]--AwsS3Reader.GetBlobWithCache throw error: %s", containerName,
					e.getMessage()));
			return null;
		} finally {
			if (client != null) {
				client.shutdown();
			}
		}
	}
}
