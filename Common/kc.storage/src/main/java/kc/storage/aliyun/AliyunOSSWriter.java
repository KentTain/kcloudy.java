package kc.storage.aliyun;


import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import kc.framework.extension.StringExtensions;
import kc.framework.tenant.ConnectionKeyConstant;
import kc.storage.BlobCache;
import kc.storage.WriterBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class AliyunOSSWriter extends WriterBase{
	private Logger logger = LoggerFactory.getLogger(AliyunOSSReader.class);
	//private final OSSConfig Config;
    //private final AliyunOSSCredentials Credentials;
    //private final IOSS client;
    private BlobCache Cache;
    private String endpoint;
    private String accessKey;
    private String secretKey;
	private final String preBucket = "kcloudy-";

    public AliyunOSSWriter(String connectionString, BlobCache cache)
    {
    	if (StringExtensions.isNullOrEmpty(connectionString))
			throw new IllegalArgumentException("Aliyun OSS's NAS AliyunOSSWriter connect String is empy or null.");

		Dictionary<String, String> keyValues = StringExtensions.keyValuePairFromConnectionString(connectionString);
		endpoint = keyValues.get(ConnectionKeyConstant.BlobEndpoint);
		accessKey = keyValues.get(ConnectionKeyConstant.AccessName);
		secretKey = keyValues.get(ConnectionKeyConstant.AccessKey);
		if (StringExtensions.isNullOrEmpty(endpoint))
			throw new IllegalArgumentException(
					"Aliyun OSS's NAS AliyunOSSWriter connect String is wrong. It can't set the BlobEndpoint Value.");
		if (StringExtensions.isNullOrEmpty(accessKey))
			throw new IllegalArgumentException(
					"Aliyun OSS's NAS AliyunOSSWriter connect String is wrong. It can't set the AccessKey Value.");
		if (StringExtensions.isNullOrEmpty(secretKey))
			throw new IllegalArgumentException(
					"Aliyun OSS's NAS AliyunOSSWriter connect String is wrong. It can't set the SecretAccountKey Value.");

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
	protected void CreateContainer(String container)
    {
		String bucket = preBucket + container;
		OSS client = GetAliyunOSSClient();
        try {
        	if (!client.doesBucketExist(bucket)) {
                // Because the CreateBucketRequest object doesn't specify a region, the
                // bucket is created in the region specified in the client.
        		client.createBucket(new CreateBucketRequest(bucket));
                
                // Verify that the bucket was created by retrieving it and checking its location.
                String bucketLocation = client.getBucketLocation(new GenericRequest(bucket));
                logger.debug(bucket + "--Bucket location: " + bucketLocation);
            }
        } catch (OSSException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSWriter.CreateContainer throw error: %s",
					bucket, e.getMessage()));
		} catch(Exception e) {
			logger.error(String.format("--Container[%s]--AliyunOSSWriter.CreateContainer throw error: %s",
					bucket, e.getMessage()));
        } finally {
           if(client != null) {
        	   client.shutdown();
           }           
       }
    }
    @Override
    protected void DeleteContainer(String container)
    {
		String bucket = preBucket + container;
		OSS client = GetAliyunOSSClient();
        try {
        	ObjectListing listResponse = client.listObjects(bucket);
		    // Recursively delete all the objects inside given bucket
		    if (listResponse != null && listResponse.getObjectSummaries() != null) {
				List<String> keys = listResponse.getObjectSummaries().stream().map(m -> m.getKey()).collect(Collectors.toList());
				GenericRequest request = new DeleteObjectsRequest(bucket).withKeys(keys);
				client.deleteObject(request);

//				Iterator<OSSObjectSummary> versionIter = listResponse.getObjectSummaries().iterator();
//				while (versionIter.hasNext()) {
//					OSSObjectSummary vs = versionIter.next();
//					client.deleteObject(bucket, vs.getKey());
//				}
//
//				if (listResponse.isTruncated()) {
//					//versions = client.listNextBatchOfVersions(versions);
//				}
			}
		    
		    // Get list of versions in a given bucket
		    VersionListing versions = client.listVersions(new ListVersionsRequest().withBucketName(bucket));
		    // Recursively delete all the versions inside given bucket
		    if(versions != null && versions.getVersionSummaries() != null) {
				Iterator<OSSVersionSummary> versionIter = versions.getVersionSummaries().iterator();
				while (versionIter.hasNext()) {
					OSSVersionSummary vs = versionIter.next();
					client.deleteVersion(bucket, vs.getKey(), vs.getVersionId());
				}

				if (versions.isTruncated()) {
					//versions = client.listNextBatchOfVersions(versions);
				}
			}

		    // Send Delete Bucket Request
		    client.deleteBucket(bucket);
		} catch (OSSException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSWriter.DeleteContainer throw error: %s",
					bucket, e.getMessage()));
		} catch(ClientException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSWriter.CopyBlob throw error: %s",
					bucket, e.getMessage()));
		} finally {
           if(client != null) {
        	   client.shutdown();
           }           
       }
    }
    @Override
    protected void WriteBlob(String container, String blobId, byte[] blobData, Map<String, String> blobMetadata)
    {
		String bucket = preBucket + container;
		OSS client = GetAliyunOSSClient();
        try{
        	if (!client.doesBucketExist(bucket)) {
                // Because the CreateBucketRequest object doesn't specify a region, the
                // bucket is created in the region specified in the client.
        		client.createBucket(new CreateBucketRequest(bucket));
        	}
            
        	ObjectMetadata metadata = new ObjectMetadata();
        	metadata.setContentLength(blobData.length);
        	if (blobMetadata != null)
			{
			    for (String metadataKey : blobMetadata.keySet())
			    {
			    	metadata.addUserMetadata(metadataKey, blobMetadata.get(metadataKey));
			    }
			}
        	
            InputStream fileStream = new ByteArrayInputStream(blobData);
            PutObjectRequest request = new PutObjectRequest(bucket, blobId, fileStream, metadata);

            PutObjectResult response = client.putObject(request);
            if (response == null)
            {
                
            }
        } catch (OSSException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSWriter.WriteBlob throw error: %s",
					bucket, e.getMessage()));
		} catch(ClientException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSWriter.CopyBlob throw error: %s",
					bucket, e.getMessage()));
		} finally {
           if(client != null) {
        	   client.shutdown();
           }           
       }
    }
    @Override
    protected void DeleteBlob(String container, String blobId)
    {
		String bucket = preBucket + container;
		OSS client = GetAliyunOSSClient();
        try{
			GenericRequest request = new DeleteObjectsRequest(bucket).withKey(blobId);
            client.deleteObject(request);
        } catch (OSSException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSWriter.DeleteBlob throw error: %s",
					bucket, e.getMessage()));
		} catch(ClientException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSWriter.CopyBlob throw error: %s",
					bucket, e.getMessage()));
		} finally {
           if(client != null) {
        	   client.shutdown();
           } 
       }
    }
    @Override
    public void CopyBlob(String container, String desContainer, String blobId)
    {
        if (StringExtensions.isNullOrEmpty(blobId)) return;

		String bucket = preBucket + container;
		OSS client = GetAliyunOSSClient();
        try{
        	if (!client.doesBucketExist(bucket)) {
                // Because the CreateBucketRequest object doesn't specify a region, the
                // bucket is created in the region specified in the client.
        		client.createBucket(new CreateBucketRequest(bucket));
        	}

            client.copyObject(bucket, blobId, preBucket + desContainer, blobId);
            
        } catch (OSSException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSWriter.CopyBlob throw error: %s",
					bucket, e.getMessage()));
		} catch(ClientException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSWriter.CopyBlob throw error: %s",
					bucket, e.getMessage()));
        } finally {
           if(client != null) {
        	   client.shutdown();
           }           
       }
    }

	@Override
	protected void WriteBlobMetadata(String container, String blobId, boolean clearExisting,
			Map<String, String> blobMetadata) {
		String bucket = preBucket + container;
		OSS client = GetAliyunOSSClient();
        try{
        	ObjectMetadata metadata = new ObjectMetadata();
        	if (blobMetadata != null)
			{
			    for (String metadataKey : blobMetadata.keySet())
			    {
			    	metadata.addUserMetadata(metadataKey, blobMetadata.get(metadataKey));
			    }
			}
        	
        	CopyObjectRequest request = new CopyObjectRequest(bucket, blobId, bucket, blobId);
			request.setNewObjectMetadata(metadata);

        	client.copyObject(request);
        } catch (OSSException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSWriter.WriteBlobMetadata throw error: %s",
					bucket, e.getMessage()));
		} catch(ClientException e) {
			logger.error(String.format("--Container[%s]--AliyunOSSWriter.CopyBlob throw error: %s",
					bucket, e.getMessage()));
		} finally {
           if(client != null) {
        	   client.shutdown();
           }           
       }
	}

	@Override
	protected void WriteBlobBlock(String container, String blobId, String blobBlockId, byte[] blobData,
			String contentMD5) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void SubmitBlobBlock(String container, String blobId, String[] blobBlockIds,
			Map<String, String> blobMetadata) {
		// TODO Auto-generated method stub
		
	}

}
