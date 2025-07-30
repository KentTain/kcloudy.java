package kc.storage.aliyun;

import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import kc.framework.base.BlobInfo;
import kc.framework.extension.DateExtensions;
import kc.framework.extension.StringExtensions;
import kc.framework.tenant.ConnectionKeyConstant;
import kc.storage.BlobResult;
import kc.storage.BlobCache;
import kc.storage.ReaderBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

class AliyunOSSReader extends ReaderBase {
	private Logger logger = LoggerFactory.getLogger(AliyunOSSReader.class);
	// private OSSClientConfig Config;
	// private AliyunOSSCredentials Credentials;
	// private IOSSClient client;
	private final BlobCache Cache;
	private final String endpoint;
	private final String accessKey;
	private final String secretKey;
	private final String preBucket = "kcloudy-";

	public AliyunOSSReader(String connectionString, BlobCache cache) {
		if (StringExtensions.isNullOrEmpty(connectionString))
			throw new IllegalArgumentException("Aliyun OSS's NAS reader connect String is empy or null.");

		Dictionary<String, String> keyValues = StringExtensions.keyValuePairFromConnectionString(connectionString);
		endpoint = keyValues.get(ConnectionKeyConstant.BlobEndpoint);
		accessKey = keyValues.get(ConnectionKeyConstant.AccessName);
		secretKey = keyValues.get(ConnectionKeyConstant.AccessKey);
		if (StringExtensions.isNullOrEmpty(endpoint))
			throw new IllegalArgumentException(
					"Aliyun OSS's NAS reader connect String is wrong. It can't set the BlobEndpoint Value.");
		if (StringExtensions.isNullOrEmpty(accessKey))
			throw new IllegalArgumentException(
					"Aliyun OSS's NAS reader connect String is wrong. It can't set the AccessKey Value.");
		if (StringExtensions.isNullOrEmpty(secretKey))
			throw new IllegalArgumentException(
					"Aliyun OSS's NAS reader connect String is wrong. It can't set the SecretAccountKey Value.");

		this.Cache = cache;
	}

	private OSS GetAliyunOSSClient() {
		// 创建ClientConfiguration实例，按照您的需要修改默认参数。
		ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
		// 设置是否支持CNAME。CNAME是指将自定义域名绑定到存储空间上。
		conf.setSupportCname(false);
		conf.setMaxErrorRetry(3);
		conf.setConnectionTimeout(300);

		return new OSSClientBuilder().build(endpoint, accessKey, secretKey, conf);
	}

	@Override
	public boolean ExistContainer(String container) {
		String bucket = preBucket + container;
		OSS client = GetAliyunOSSClient();
		try {
			return client.doesBucketExist(bucket);
		} catch (OSSException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSReader.ExistContainer throw error: %s", bucket, e.getMessage()));
			return false;
		} catch(ClientException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSReader.ExistContainer throw error: %s", bucket, e.getMessage()));
			return false;
		} finally {
			if (client != null) {
				client.shutdown();
			}
		}
	}

	@Override
	public boolean ExistBlob(String container, String blobId) {
		String bucket = preBucket + container;
		OSS client = GetAliyunOSSClient();
		try {
			return client.doesObjectExist(bucket, blobId);
		} catch (OSSException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSReader.ExistBlob throw error: %s", bucket, e.getMessage()));
			return false;
		} catch(ClientException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSReader.ExistBlob throw error: %s", bucket, e.getMessage()));
			return false;
		} finally {
			if (client != null) {
				client.shutdown();
			}
		}
	}

	@Override
	public List<String> ListContainers() {
		OSS client = GetAliyunOSSClient();
		try {
			List<Bucket> response = client.listBuckets();
			if (response == null || response.size() <= 0)
				return new ArrayList<String>();

			return response.stream().map(m -> m.getName()).collect(Collectors.toList());
		} catch (OSSException e) {
			logger.error(String.format("--Container--AliyunOSSReader.ListContainers throw error: %s", e.getMessage()));
			return new ArrayList<String>();
		} catch(ClientException e) {
			logger.error(String.format("--Container--AliyunOSSReader.ListContainers throw error: %s", e.getMessage()));
			return new ArrayList<String>();
		} finally {
			if (client != null) {
				client.shutdown();
			}
		}
	}

	@Override
	public List<String> ListBlobIds(String container) {
		String bucket = preBucket + container;
		OSS client = GetAliyunOSSClient();
		try {
			ListObjectsV2Result response = client.listObjectsV2(bucket);
			if (response == null || response.getObjectSummaries().size() <= 0)
				return new ArrayList<String>();

			return response.getObjectSummaries().stream().map(m -> m.getKey()).collect(Collectors.toList());
		} catch (OSSException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSReader.ListBlobIds throw error: %s", bucket,
					e.getMessage()));
			return new ArrayList<String>();
		} catch(ClientException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSReader.ListBlobIds throw error: %s", bucket,
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

		String bucket = preBucket + container;
		int offset = -1;
		int length = -1;
		BlobResult bResult = GetActualBlobIdAndOffset(blobId, offset, length);
		String realBlobId = bResult.getBlobId();
		offset = bResult.getOffset();
		length = bResult.getLength();
		if (useCacheIfAvailable && this.Cache.Exists(bucket, realBlobId)) {
			try {
				BlobInfo bi = this.Cache.GetBlob(bucket, encryptionKey, isInternal, userId,  realBlobId);
				if (offset != -1) {
					bi = GetBlobSegment(bi, offset, length, blobId);
				}
				return bi;
			} catch (Exception e) {
				logger.error(
						String.format("--Container[%s]--AliyunOSSReader.GetBlob throw error: %s", container, e.getMessage()));
				return null;
			}
		} else {
//			byte[] cacheData = null;
//			byte[] cacheMetadata = null;
			BlobInfo bi = GetBlobWithCache(container, encryptionKey, isInternal, userId, realBlobId,
					useCacheIfAvailable);
			// LogUtil.LogDebug("Retrieve Blob from Aliyun OSS store. ", blobId,
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
	public BlobInfo GetBlobWithoutEncryption(String container, String blobId, boolean useCacheIfAvailable) {
		if (StringExtensions.isNullOrEmpty(blobId) || StringExtensions.isNullOrEmpty(container))
			return null;

		String bucket = preBucket + container;
		int offset = -1;
		int length = -1;
		BlobResult bResult = GetActualBlobIdAndOffset(blobId, offset, length);
		String realBlobId = bResult.getBlobId();
		offset = bResult.getOffset();
		length = bResult.getLength();

		if (useCacheIfAvailable && this.Cache.Exists(bucket, realBlobId)) {
			BlobInfo bi = this.Cache.GetBlobWithoutEncryption(bucket, realBlobId);
			// LogUtil.LogDebug("Retrieve Blob from local cache store. ", blobId,
			// sw.ElapsedMilliseconds);
			if (offset != -1) {
				bi = GetBlobSegment(bi, offset, length, blobId);
			}
			return bi;
		} else {
			OSS client = GetAliyunOSSClient();
			try {
				try (OSSObject response = client.getObject(bucket, blobId)) {
					if (null == response)
						return null;

					InputStream s3is = response.getObjectContent();
					ByteArrayOutputStream  outStream = new ByteArrayOutputStream();

					byte[] buffer = new byte[1024];
					int read_len = -1;
					while ((read_len = s3is.read(buffer)) != -1) {
						outStream.write(buffer, 0, read_len);
					}
					s3is.close();
					outStream.close();
					byte[] data = outStream.toByteArray();

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
					// LogUtil.LogDebug("Retrieve Blob from Aliyun OSS store. ", blobId,
					// sw.ElapsedMilliseconds);

					if (useCacheIfAvailable) {
						this.Cache.InsertWithoutEncryption(bucket, blobId, blobInfo.getData());
					}
					if (offset != -1) {
						blobInfo = GetBlobSegment(blobInfo, offset, length, blobId);
					}
					return blobInfo;
				}
			} catch (OSSException e) {
				logger.error(String.format("--Container[%s]--AliyunOSSReader.GetBlobWithoutEncryption throw error: %s",
						bucket, e.getMessage()));
				return null;
			} catch (Exception e) {
				logger.error(String.format("--Container[%s]--AliyunOSSReader.GetBlobWithoutEncryption throw error: %s",
						bucket, e.getMessage()));
				return null;
			} finally {
				if (client != null) {
					client.shutdown();
				}
			}

		}
	}

	@Override
	public Date GetBlobLastModifiedTime(String container, String blobId) {
		String bucket = preBucket + container;
		OSS client = GetAliyunOSSClient();
		try {
			ObjectMetadata response = client.getObjectMetadata(bucket, blobId);
			return response.getLastModified();
		} catch (OSSException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSReader.GetBlobLastModifiedTime throw error: %s",
					bucket, e.getMessage()));
			return DateExtensions.getMinValue();
		} catch (ClientException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSReader.GetBlobLastModifiedTime throw error: %s",
					bucket, e.getMessage()));
			return DateExtensions.getMinValue();
		} finally {
			if (client != null) {
				client.shutdown();
			}
		}
	}

	@Override
	public Map<String, String> GetBlobMetadata(String container, String blobId) {
		Map<String, String> blobMetadata = new HashMap<String, String>();
		String bucket = preBucket + container;
		OSS client = GetAliyunOSSClient();
		try {

			ObjectMetadata response = client.getObjectMetadata(bucket, blobId);
			if (null == response || response.getUserMetadata().size() <= 0)
				return blobMetadata;

			Map<String, String> userMetadata = response.getUserMetadata();
			for (String key : userMetadata.keySet()) {
				blobMetadata.put(key, userMetadata.get(key));
			}

			return blobMetadata;
		} catch (OSSException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSReader.GetBlobMetadata throw error: %s", bucket,
					e.getMessage()));
			return blobMetadata;
		} catch (ClientException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSReader.GetBlobMetadata throw error: %s", bucket,
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
	private BlobInfo GetBlobWithCache(String container, String encryptionKey, boolean isInternal, String userId,
			 String blobId, boolean generateCacheData) {
		cacheData = null;
		cacheMetadata = null;

		if (StringExtensions.isNullOrEmpty(blobId))
			return null;

		String bucket = preBucket + container;
		OSS client = GetAliyunOSSClient();
		try {
			try (OSSObject response = client.getObject(bucket, blobId)) {
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

				CheckBlobAccess(bucket, isInternal, userId, blobId, blobMetadata);

				InputStream s3is = response.getObjectContent();
				ByteArrayOutputStream  outStream = new ByteArrayOutputStream();

				byte[] buffer = new byte[1024];
				int read_len = -1;
				while ((read_len = s3is.read(buffer)) != -1) {
					outStream.write(buffer, 0, read_len);
				}
				s3is.close();
				outStream.close();
				byte[] data = outStream.toByteArray();

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
		} catch (OSSException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSReader.GetBlobWithCache throw error: %s", bucket,
					e.getMessage()));
			return null;
		} catch (Exception e) {
			logger.error(String.format("--Container[%s]--AliyunOSSReader.GetBlobWithCache throw error: %s", bucket,
					e.getMessage()));
			return null;
		} finally {
			if (client != null) {
				client.shutdown();
			}
		}
	}
}
