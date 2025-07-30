package kc.storage.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import kc.storage.StorageBase;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kc.framework.base.BlobInfo;
import kc.framework.enums.*;
import kc.framework.security.EncryptPasswordUtil;
import kc.framework.tenant.RoleConstants;
import kc.framework.tenant.Tenant;

@Disabled
class BlobUtilTest extends StorageBase {
	private static Logger logger = LoggerFactory.getLogger(BlobUtilTest.class);
	private final String imagePath = "src/test/resources/";
	private final String fileName = "TestImage";
	private final String fileType = FileType.Image.toString();
	private final String fileFormat = ImageFormat.Png.toString();

	private final String userId = RoleConstants.AdminUserId.toLowerCase();
	// private static string blobId = Guid.NewGuid().ToString().ToLower();
	private final String blobId = "ef35812e-a2aa-4e51-9186-624e9d1f9a0b";

	// cTest：Local File; Copy to cBuy: Azure blob
	@Test
	void AllBlobOperator_LocalFile_Test() {
		String uploadFile = imagePath + "UploadImage.png";
		boolean success = BlobUtil.SaveBlob(TestTenant, userId, uploadFile, blobId,
				fileType, fileFormat, fileName, false, null, null);
		logger.debug("cTest use storage connection: " + GetStorageConnectionString(TestTenant));
		assertTrue(success);

		boolean existResult = BlobUtil.ExistBlob(TestTenant, blobId);
		assertTrue(existResult);

		BlobInfo blob = BlobUtil.GetBlobById(TestTenant, userId, blobId, false);
		assertTrue(blob != null && blob.getData().length > 0);

		String downFile = imagePath + "downloadImage-" + blobId + "-file.png";
		boolean isSave = byteArrayToFile(blob.getData(), downFile);
		assertTrue(isSave);

		List<String> blobIdList = Collections.singletonList(blobId);
		boolean copyResult = BlobUtil.CopyBlobsToOtherClient(TestTenant, BuyTenant, blobIdList, userId, true);
		logger.debug("Copy to cBuy use storage connection: " + GetStorageConnectionString(BuyTenant));
		assertTrue(copyResult);
		
		boolean removeResult = BlobUtil.RemoveBlob(TestTenant, userId, blobId);
		assertTrue(removeResult);
	}
	
	//cBuy：Azure blob; Copy to cTest：Local File
	@Test
    void AllBlobOperator_AzureBlob_Test() {
        String uploadFile = imagePath + "UploadImage.png";
        boolean success = BlobUtil.SaveBlob(BuyTenant, userId, uploadFile, blobId, fileType, fileFormat,
            fileName, false, null, null);
        logger.debug("cBuy use storage connection: " + GetStorageConnectionString(BuyTenant));
        assertTrue(success);

        boolean existResult = BlobUtil.ExistBlob(BuyTenant, blobId);
        assertTrue(existResult);

        BlobInfo blob = BlobUtil.GetBlobById(BuyTenant, userId, blobId, false);
        assertTrue(blob != null && blob.getData().length > 0);

        String downFile = imagePath + "downloadImage-" + blobId + "-blob.png";
        boolean isSave = byteArrayToFile(blob.getData(), downFile);
        assertTrue(isSave);
        
        List<String> blobIdList = Collections.singletonList(blobId);
        boolean copyResult = BlobUtil.CopyBlobsToOtherClient(BuyTenant, TestTenant, blobIdList, userId, false);
		logger.debug("Copy to cTest use storage connection: " + GetStorageConnectionString(TestTenant));
        assertTrue(copyResult);
        
        boolean removeResult = BlobUtil.RemoveBlob(BuyTenant, userId, blobId);
        assertTrue(removeResult);
    }

	//cSale：Aliyun OSS; Copy to cTest：Local File
	@Test
	void AllBlobOperator_AliyunOSS_Test() {
		String uploadFile = imagePath + "UploadImage.png";
		boolean success = BlobUtil.SaveBlob(SaleTenant, userId, uploadFile, blobId, fileType, fileFormat,
				fileName, false, null, null);
		logger.debug("cSale use storage connection: " + GetStorageConnectionString(SaleTenant));
		assertTrue(success);

		boolean existResult = BlobUtil.ExistBlob(SaleTenant, blobId);
		assertTrue(existResult);

		BlobInfo blob = BlobUtil.GetBlobById(SaleTenant, userId, blobId, false);
		assertTrue(blob != null && blob.getData().length > 0);

		String downFile = imagePath + "downloadImage-" + blobId + "-blob.png";
		boolean isSave = byteArrayToFile(blob.getData(), downFile);
		assertTrue(isSave);

		List<String> blobIdList = Collections.singletonList(blobId);
		boolean copyResult = BlobUtil.CopyBlobsToOtherClient(SaleTenant, TestTenant, blobIdList, userId, false);
		logger.debug("Copy to cTest use storage connection: " + GetStorageConnectionString(TestTenant));
		assertTrue(copyResult);

		boolean removeResult = BlobUtil.RemoveBlob(SaleTenant, userId, blobId);
		assertTrue(removeResult);
	}

	private String GetStorageConnectionString(Tenant tenant) {
		try {
			return String.format("BlobEndpoint=%s;DefaultEndpointsProtocol=https;AccountName=%s;AccountKey=%s",
					tenant.getStorageEndpoint(), tenant.getStorageAccessName(),
					EncryptPasswordUtil.DecryptPassword(tenant.getStorageAccessKeyPasswordHash(), tenant.getPrivateEncryptKey()));
		} catch (Exception ex) {
			return "";
		}
	}

	private boolean byteArrayToFile(byte[] buff, String filePath) {
		OutputStream os = null;
		try {
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			os = new BufferedOutputStream(new FileOutputStream(file));
			os.write(buff);
			os.flush();
			return true;
		} catch (IOException ex) {
			logger.error(ex.getMessage());
		} finally {
			try {
				os.close();
			} catch (IOException ex) {
				logger.error(ex.getMessage());
			}
		}
		return false;
	}
}
