package kc.storage.aws;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.Map;

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
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;
import com.amazonaws.services.s3.model.ListVersionsRequest;

import kc.framework.extension.StringExtensions;
import kc.framework.tenant.ConnectionKeyConstant;
import kc.storage.BlobCache;
import kc.storage.WriterBase;

class AwsS3Writer extends WriterBase{
	private Logger logger = LoggerFactory.getLogger(AwsS3Reader.class);
	//private final AmazonS3Config Config;
    //private final AWSCredentials Credentials;
    //private final IAmazonS3 client;
    private BlobCache Cache;
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String region;

    public AwsS3Writer(String connectionString, BlobCache cache)
    {
    	if (StringExtensions.isNullOrEmpty(connectionString))
			throw new IllegalArgumentException("AWS S3's NAS AwsS3Writer connect String is empy or null.");

		Dictionary<String, String> keyValues = StringExtensions.keyValuePairFromConnectionString(connectionString);
		endpoint = keyValues.get(ConnectionKeyConstant.BlobEndpoint);
		accessKey = keyValues.get(ConnectionKeyConstant.AccessName);
		secretKey = keyValues.get(ConnectionKeyConstant.AccessKey);
		region = keyValues.get(ConnectionKeyConstant.VALUE_1);
		if (StringExtensions.isNullOrEmpty(endpoint))
			throw new IllegalArgumentException(
					"AWS S3's NAS AwsS3Writer connect String is wrong. It can't set the BlobEndpoint Value.");
		if (StringExtensions.isNullOrEmpty(accessKey))
			throw new IllegalArgumentException(
					"AWS S3's NAS AwsS3Writer connect String is wrong. It can't set the AccessKey Value.");
		if (StringExtensions.isNullOrEmpty(secretKey))
			throw new IllegalArgumentException(
					"AWS S3's NAS AwsS3Writer connect String is wrong. It can't set the SecretAccountKey Value.");
		if (StringExtensions.isNullOrEmpty(region))
			region = Regions.US_WEST_2.getName();
        //ServicePointManager.SecurityProtocol =
        //        SecurityProtocolType.Tls
        //        | SecurityProtocolType.Tls11
        //        | SecurityProtocolType.Tls12;
        ////ServicePointManager.CertificatePolicy = new TrustAllCertificatePolicy();
        //ServicePointManager.ServerCertificateValidationCallback = CheckValidationResult;

        //var Config = new AmazonS3Config()
        //{
        //    ServiceURL = endpoint,
        //    ForcePathStyle = true,
        //    SignatureVersion = "2",
        //    //BufferSize = 15 * 1024 * 1024, //15M
        //    //RegionEndpoint = Amazon.RegionEndpoint.CNNorth1
        //    //RegionEndpoint = Amazon.RegionEndpoint.USEast1,
        //};
        //var Credentials = new BasicAWSCredentials(accessKey, secretKey);
        //client = new AmazonS3Client(Credentials, Config);

        this.Cache = cache;
    }

    protected AmazonS3 GetAWSClient() {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		EndpointConfiguration endpointConfig = new EndpointConfiguration(endpoint, null);
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);

		return AmazonS3ClientBuilder.standard()
				.withRegion(region)
				.withClientConfiguration(clientConfig)
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withEndpointConfiguration(endpointConfig)
				.withPathStyleAccessEnabled(true)
				.build();
	}

    @Override
	protected void CreateContainer(String container)
    {
        AmazonS3 client = GetAWSClient();
        try {
        	
        	if (!client.doesBucketExistV2(container)) {
                // Because the CreateBucketRequest object doesn't specify a region, the
                // bucket is created in the region specified in the client.
        		client.createBucket(new CreateBucketRequest(container));
                
                // Verify that the bucket was created by retrieving it and checking its location.
                String bucketLocation = client.getBucketLocation(new GetBucketLocationRequest(container));
                logger.debug(container + "--Bucket location: " + bucketLocation);
            }
        } catch (AmazonServiceException e) {
			logger.error(String.format("--Container[%s]--AwsS3Writer.CreateContainer throw error: %s", 
					container, e.getMessage()));
		} catch(SdkClientException e) {
			logger.error(String.format("--Container[%s]--AwsS3Writer.CreateContainer throw error: %s", 
					container, e.getMessage()));
        } finally {
           if(client != null) {
        	   client.shutdown();
           }           
       }
    }
    @Override
    protected void DeleteContainer(String container)
    {
    	AmazonS3 client = GetAWSClient();
        try {
        	ObjectListing listResponse = client.listObjects(container);
		    // Recursively delete all the objects inside given bucket
		    if(listResponse != null && listResponse.getObjectSummaries() != null) {
		        while (true) {
		        	Iterator<S3ObjectSummary> objIter = listResponse.getObjectSummaries().iterator();
	                while (objIter.hasNext()) {
	                	client.deleteObject(container, objIter.next().getKey());
	                }
		            
		            if (listResponse.isTruncated()) {
		            	listResponse = client.listNextBatchOfObjects(listResponse);
		            } else {
		                break;
		            }                   
		        }
		    }
		    
		    // Get list of versions in a given bucket
		    VersionListing versions = client.listVersions(new ListVersionsRequest().withBucketName(container));
		    // Recursively delete all the versions inside given bucket
		    if(versions != null && versions.getVersionSummaries() != null) {                
		        while (true) {                  
		        	Iterator<S3VersionSummary> versionIter = versions.getVersionSummaries().iterator();
	                while (versionIter.hasNext()) {
	                    S3VersionSummary vs = versionIter.next();
	                    client.deleteVersion(container, vs.getKey(), vs.getVersionId());
	                }
	                
		            if (versions.isTruncated()) {
		                versions = client.listNextBatchOfVersions(versions);
		            } else {
		                break;
		            }                   
		        }
		    }

		    // Send Delete Bucket Request
		    client.deleteBucket(container);
		} catch (AmazonServiceException e) {
			logger.error(String.format("--Container[%s]--AwsS3Writer.DeleteContainer throw error: %s", 
					container, e.getMessage()));
		} catch(SdkClientException e) {
			logger.error(String.format("--Container[%s]--AwsS3Writer.DeleteContainer throw error: %s", 
					container, e.getMessage()));
        } finally {
           if(client != null) {
        	   client.shutdown();
           }           
       }
    }
    @Override
    protected void WriteBlob(String container, String blobId, byte[] blobData, Map<String, String> blobMetadata)
    {
        AmazonS3 client = GetAWSClient();
        try{
        	if (!client.doesBucketExistV2(container)) {
                // Because the CreateBucketRequest object doesn't specify a region, the
                // bucket is created in the region specified in the client.
        		client.createBucket(new CreateBucketRequest(container));
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
            PutObjectRequest request = new PutObjectRequest(container, blobId, fileStream, metadata)
            		.withCannedAcl(CannedAccessControlList.PublicRead);

            PutObjectResult response = client.putObject(request);
            if (response == null)
            {
                
            }
        } catch (AmazonServiceException e) {
			logger.error(String.format("--Container[%s]--AwsS3Writer.WriteBlob throw error: %s", 
					container, e.getMessage()));
		} catch(SdkClientException e) {
			logger.error(String.format("--Container[%s]--AwsS3Writer.WriteBlob throw error: %s", 
					container, e.getMessage()));
        } finally {
           if(client != null) {
        	   client.shutdown();
           }           
       }
    }
    @Override
    protected void DeleteBlob(String container, String blobId)
    {
        AmazonS3 client = GetAWSClient();
        try{
        	DeleteObjectRequest request = new DeleteObjectRequest(container, blobId);
            client.deleteObject(request);
        } catch (AmazonServiceException e) {
			logger.error(String.format("--Container[%s]--AwsS3Writer.DeleteBlob throw error: %s", 
					container, e.getMessage()));
		} catch(SdkClientException e) {
			logger.error(String.format("--Container[%s]--AwsS3Writer.DeleteBlob throw error: %s", 
					container, e.getMessage()));
        } finally {
           if(client != null) {
        	   client.shutdown();
           } 
       }
    }
    @Override
    public void CopyBlob(String containerName, String desContainerName, String blobId)
    {
        if (StringExtensions.isNullOrEmpty(blobId)) return;

        AmazonS3 client = GetAWSClient();
        try{
        	if (!client.doesBucketExistV2(containerName)) {
                // Because the CreateBucketRequest object doesn't specify a region, the
                // bucket is created in the region specified in the client.
        		client.createBucket(new CreateBucketRequest(containerName));
        	}

            client.copyObject(containerName, blobId, desContainerName, blobId);
            
        } catch (AmazonServiceException e) {
			logger.error(String.format("--Container[%s]--AwsS3Writer.CopyBlob throw error: %s", 
					containerName, e.getMessage()));
		} catch(SdkClientException e) {
			logger.error(String.format("--Container[%s]--AwsS3Writer.CopyBlob throw error: %s", 
					containerName, e.getMessage()));
        } finally {
           if(client != null) {
        	   client.shutdown();
           }           
       }
    }

	@Override
	protected void WriteBlobMetadata(String container, String blobId, boolean clearExisting,
			Map<String, String> blobMetadata) {
		AmazonS3 client = GetAWSClient();
        try{
        	ObjectMetadata metadata = new ObjectMetadata();
        	if (blobMetadata != null)
			{
			    for (String metadataKey : blobMetadata.keySet())
			    {
			    	metadata.addUserMetadata(metadataKey, blobMetadata.get(metadataKey));
			    }
			}
        	
        	CopyObjectRequest request = new CopyObjectRequest(container, blobId, container, blobId)
        		      .withNewObjectMetadata(metadata);

        	client.copyObject(request);
        } catch (AmazonServiceException e) {
			logger.error(String.format("--Container[%s]--AwsS3Writer.WriteBlobMetadata throw error: %s", 
					container, e.getMessage()));
		} catch(SdkClientException e) {
			logger.error(String.format("--Container[%s]--AwsS3Writer.WriteBlobMetadata throw error: %s", 
					container, e.getMessage()));
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
