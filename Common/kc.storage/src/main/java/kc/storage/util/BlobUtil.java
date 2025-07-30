package kc.storage.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import kc.framework.base.AuthToken;
import kc.framework.base.BlobInfo;
import kc.framework.enums.FileType;
import kc.framework.extension.ListExtensions;
import kc.framework.extension.StringExtensions;
import kc.framework.security.Base64Provider;
import kc.framework.tenant.RoleConstants;
import kc.framework.tenant.Tenant;
import kc.framework.tenant.TenantConstant;
import kc.thirdparty.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BlobUtil {
	private static Logger logger = LoggerFactory.getLogger(BlobUtil.class);

	public final static String PrefixSmallThumbnailName = "t";
	public final static String BlobTempContainer = "clienttempforregister";

	/**
	 * 保存文件到Azure Blob中：二进制流文件
	 * @param tenant 文档所在的源租户：Tenant
	 * @param userId 上传文档用户Id
	 * @param buffer 文档二进制
	 * @param blobId 文档ID
	 * @param fileType 文档类型：FileType
	 * @param fileFormat 文档格式：如果FileType.Image，那么文档格式为：ImageFormat
	 * @param fileName 文件名称
	 * @param isTempAuth 是否保存到临时文件夹
	 * @param with With of the Thumbnail
	 * @param height Heith of the Thumbnail
	 * @return boolean 是否成功
	 */
	public static boolean SaveBlob(Tenant tenant, String userId, byte[] buffer, String blobId, String fileType,
								   String fileFormat, String fileName, boolean isTempAuth, Integer with, Integer height) {
		try {
			BlobProvider blobProvider = new BlobProvider(tenant);
			String container = tenant.getTenantName().toLowerCase();
			AuthToken authToken = GetAuthToken(container, userId, isTempAuth);
			String encodeFileName = Base64Provider.EncodeString(fileName);
			BlobInfo bi = new BlobInfo(blobId, fileType, fileFormat, encodeFileName, buffer);
			blobProvider.SaveBlob(authToken, bi, false, -1);

			if (fileType.equals(FileType.Image.toString()) && with != null && height != null) {
//				ImageFormat format = ImageFormat.valueOf(fileFormat);
				int iWith = with != 0 ? (int) with : 16;
				int iHeight = height != 0 ? (int) height : 16;
				BufferedImage bufferImage = ImageUtil.fromBytes(buffer);
				BufferedImage thubnailImage = ImageUtil.getThumbnail(bufferImage, iWith, iHeight);
				byte[] bytes = ImageUtil.toBytes(thubnailImage);
				BlobInfo tbi = new BlobInfo(PrefixSmallThumbnailName + blobId, fileType, fileFormat, encodeFileName,
						bytes);
				blobProvider.SaveBlob(authToken, tbi, false, -1);
			}

			return true;
		} catch (Exception ex) {
			logger.error(ex.getMessage() + ", StackTrace: " + Arrays.toString(ex.getStackTrace()));
			return false;
		}
	}
	
	/**
	 * 保存文件到Azure Blob中：本地的文档路径
	 * @param tenant 文档所在的源租户：Tenant
	 * @param userId 上传文档用户Id
	 * @param filePath 本地文档所在的路径
	 * @param blobId 文档ID
	 * @param fileType 文档类型：FileType
	 * @param fileFormat 文档格式：如果FileType.Image，那么文档格式为：ImageFormat
	 * @param fileName 文件名称
	 * @param isTempAuth 是否保存到临时文件夹
	 * @param with With of the Thumbnail
	 * @param height Heith of the Thumbnail
	 * @return boolean 是否成功
	 */
	public static boolean SaveBlob(Tenant tenant, String userId, String filePath, String blobId, String fileType, String fileFormat, String fileName, boolean isTempAuth , Integer with , Integer height)
    {
		try {
			Path path = Paths.get(filePath);
			byte[] heByte = Files.readAllBytes(path);
			return SaveBlob(tenant, userId, heByte, blobId, fileType, fileFormat, fileName, isTempAuth, with, height);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }

	/**
	 * 将临时文件夹中的文件拷贝到用户文件夹下
	 * @param tenant 文档所在的源租户：Tenant
	 * @param blobIds 修转移的文档ID列表
	 * @param userId 上传文档用户Id，可以为空
	 * @param containThumbnail 是否转移Thumbnail文件
	 * @return boolean
	 */
	public static boolean CopyTempsToClientBlob(Tenant tenant, List<String> blobIds, String userId,
			boolean containThumbnail) {
		try {
			BlobProvider blobProvider = new BlobProvider(tenant);
			String container = tenant.getTenantName().toLowerCase();

			AuthToken tempAuthToken = GetAuthToken(container, userId, true);
			AuthToken authToken = GetAuthToken(container, userId, false);

			blobProvider.CopyBlobs(tempAuthToken.getContainerName(), authToken.getContainerName(),
					blobIds.toArray(new String[0]));
			// blobProvider.DeleteBlob(tempAuthToken, blobIds.ToArray());
			if (containThumbnail) {
				List<String> thumbBlobIds = ListExtensions.fixStringList(blobIds, PrefixSmallThumbnailName, "");
				blobProvider.CopyBlobs(tempAuthToken.getContainerName(), authToken.getContainerName(),
						thumbBlobIds.toArray(new String[0]));
				// blobProvider.DeleteBlob(tempAuthToken, thumbBlobIds.ToArray());
			}

			return true;
		} catch (Exception ex) {
			logger.error(ex.getMessage() + ", StackTrace: " + ex.getStackTrace());
			return false;
		}

	}

	/**
	 * 将租户文件夹中的文件拷贝到其他租户文件夹下(非剪切)
	 * @param sourceTenant 文档所在的源租户：Tenant
	 * @param targetTenant 移至指定的目标租户：Tenant
	 * @param blobIds 修转移的文档ID列表
	 * @param userId 上传文档用户Id，可以为空
	 * @param containThumbnail 是否转移Thumbnail文件
	 * @return boolean
	 */
	public static boolean CopyBlobsToOtherClient(Tenant sourceTenant, Tenant targetTenant, List<String> blobIds,
			String userId, boolean containThumbnail) {
		try {
			BlobProvider blobProvider = new BlobProvider(sourceTenant);
			String container = sourceTenant.getTenantName().toLowerCase();

			AuthToken authToken = GetAuthToken(container, userId, false);
			blobProvider.CopyBlobsToOtherTenant(targetTenant, blobIds.toArray(new String[0]), authToken.getId(),
					authToken.getEncryptionKey(), authToken.isInternal(), false);
			// BlobProvider.DeleteBlob(tempAuthToken, blobIds.ToArray());
			if (containThumbnail) {
				List<String> thumbBlobIds = ListExtensions.fixStringList(blobIds, PrefixSmallThumbnailName, "");
				blobProvider.CopyBlobsToOtherTenant(targetTenant, thumbBlobIds.toArray(new String[0]), authToken.getId(),
						authToken.getEncryptionKey(), authToken.isInternal(), false);
				// BlobProvider.DeleteBlob(tempAuthToken, thumbBlobIds.ToArray());
			}

			return true;
		} catch (Exception ex) {
			logger.error(ex.getMessage() + ", StackTrace: " + ex.getStackTrace());
			return false;
		}

	}

	/**
	 * 将租户文件夹中的所有文件拷贝到其他租户文件夹下(非剪切)
	 * @param sourceTenant 文档所在的源租户：Tenant
	 * @param targetTenant 移至指定的目标租户：Tenant
	 * @param userId 上传文档用户Id，可以为空
	 * @param containThumbnail 是否转移Thumbnail文件
	 * @return boolean
	 */
	public static boolean CopyAllBlobsToOtherClient(Tenant sourceTenant, Tenant targetTenant, String userId,
			boolean containThumbnail) {
		try {
			BlobProvider blobProvider = new BlobProvider(sourceTenant);
			String container = sourceTenant.getTenantName().toLowerCase();

			List<String> blobIds = blobProvider.GetAllBlobIds(container);
			AuthToken authToken = GetAuthToken(container, userId, false);
			blobProvider.CopyBlobsToOtherTenant(targetTenant, blobIds.toArray(new String[0]), authToken.getId(), authToken.getEncryptionKey(),
					authToken.isInternal(), false);
			// BlobProvider.DeleteBlob(tempAuthToken, blobIds.ToArray());
			if (containThumbnail) {
				List<String> thumbBlobIds = ListExtensions.fixStringList(blobIds, PrefixSmallThumbnailName, "");
				blobProvider.CopyBlobsToOtherTenant(targetTenant, thumbBlobIds.toArray(new String[0]), authToken.getId(),
						authToken.getEncryptionKey(), authToken.isInternal(), false);
				// BlobProvider.DeleteBlob(tempAuthToken, thumbBlobIds.ToArray());
			}

			return true;
		} catch (Exception ex) {
			logger.error(ex.getMessage() + ", StackTrace: " + ex.getStackTrace());
			return false;
		}

	}

	/// <summary>
	/// 将源文件生产一个新文件
	/// </summary>
	/// <param name="tenant">需要查询的Tenant</param>
	/// <param name="userId">上传文档用户Id，可为空</param>
	/// <param name="blobId">文档Id：BlobId</param>
	/// <returns>新文件BlobId</returns>
	public static String TransfromBlob(Tenant tenant, String userId, String blobId) {
		try {
			BlobProvider blobProvider = new BlobProvider(tenant);
			String container = tenant.getTenantName().toLowerCase();

			String newBlodId = UUID.randomUUID().toString().toLowerCase();
			AuthToken authToken = GetAuthToken(container, userId, false);
			BlobInfo blob = blobProvider.GetBlob(authToken, blobId, false);
			if (blob != null && blob.getData() != null) {
				BlobInfo bi = new BlobInfo(newBlodId, blob.getFileType(), blob.getFileFormat(), blob.getFileName(), blob.getData());
				blobProvider.SaveBlob(authToken, bi, false, -1);

				return newBlodId;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage() + ", StackTrace: " + ex.getStackTrace());
		}

		return "";
	}

	/**
	 * 根据文件Id查询Tenant下的文件
	 * @param tenant 需要查询的Tenant
	 * @param blobId 修转移的文档ID列表
	 * @param userId 上传文档用户Id，可以为空
	 * @param isTempAuth 是否在临时文件夹下
	 * @return BlobInfo 文件BlobInfo
	 */
	public static BlobInfo GetBlobById(Tenant tenant, String userId, String blobId, boolean isTempAuth) {
		try {
			BlobProvider blobProvider = new BlobProvider(tenant);
			String container = tenant.getTenantName().toLowerCase();

			AuthToken authToken = GetAuthToken(container, userId, isTempAuth);
			return blobProvider.GetBlob(authToken, blobId, false);
		} catch (Exception ex) {
			logger.error(ex.getMessage() + ", StackTrace: " + ex.getStackTrace());
			return null;
		}
	}

	/**
	 * 获取Tenant下的所有文件
	 * @param tenant 需要查询的Tenant
	 * @param userId 上传文档用户Id，可以为空
	 * @return List<BlobInfo> 文件BlobInfo列表
	 */
	public static List<BlobInfo> GetContainerBlobs(Tenant tenant, String userId) {
		try {
			BlobProvider blobProvider = new BlobProvider(tenant);
			String container = tenant.getTenantName().toLowerCase();

			AuthToken authToken = GetAuthToken(container, userId, false);
			List<BlobInfo> blobs = blobProvider.GetBlobs(authToken);

			return blobs;
		} catch (Exception ex) {
			logger.error(ex.getMessage() + ", StackTrace: " + ex.getStackTrace());
			return null;
		}
	}

	/**
	 * 删除Tenant下的一个文件
	 * @param tenant 需要查询的Tenant
	 * @param userId 上传文档用户Id，可以为空
	 * @param blobId 文档Id：BlobId
	 * @return boolean 是否成功
	 */
	public static boolean RemoveBlob(Tenant tenant, String userId, String blobId) {
		try {
			BlobProvider blobProvider = new BlobProvider(tenant);
			String container = tenant.getTenantName().toLowerCase();

			AuthToken authToken = GetAuthToken(container, userId, false);
			String[] blobIds = new String[] { blobId };
			blobProvider.DeleteBlob(authToken, blobIds);

			return true;
		} catch (Exception ex) {
			logger.error(ex.getMessage() + ", StackTrace: " + ex.getStackTrace());
			return false;
		}

	}

	/**
	 * 删除Tenant下的多个文件
	 * @param tenant 需要查询的Tenant
	 * @param userId 上传文档用户Id，可以为空
	 * @param blobIds 文档Id列表：List<BlobId>
	 * @return boolean 是否成功
	 */
	public static boolean RemoveBlobs(Tenant tenant, String userId, List<String> blobIds) {
		try {
			BlobProvider blobProvider = new BlobProvider(tenant);
			String container = tenant.getTenantName().toLowerCase();

			AuthToken authToken = GetAuthToken(container, userId, false);
			blobProvider.DeleteBlob(authToken, blobIds.toArray(new String[0]));

			return true;
		} catch (Exception ex) {
			logger.error(ex.getMessage() + ", StackTrace: " + ex.getStackTrace());
			return false;
		}
	}

	/**
	 * 文件Id在Tenant下的是否存在
	 * @param tenant 需要查询的Tenant
	 * @param blobId 文档Id：BlobId
	 * @return boolean 是否存在
	 */
	public static boolean ExistBlob(Tenant tenant, String blobId) {
		try {
			BlobProvider blobProvider = new BlobProvider(tenant);
			String container = tenant.getTenantName().toLowerCase();

			return blobProvider.DoesBlobExist(container, blobId);
		} catch (Exception ex) {
			logger.error(ex.getMessage() + ", StackTrace: " + ex.getStackTrace());
			return false;
		}
	}

	/**
	 * 获取Tenant所在存储中的所有租户的租户列表
	 * @param tenant 需要查询的Tenant
	 * @return List<String>
	 */
	public static List<String> GetContainers(Tenant tenant) {
		try {
			BlobProvider blobProvider = new BlobProvider(tenant);
			return blobProvider.GetContainers();
		} catch (Exception ex) {
			logger.error(ex.getMessage() + ", StackTrace: " + ex.getStackTrace());
			return null;
		}
	}

	/**
	 * 获取Tenant下的所有文件的Id列表
	 * @param tenant 需要查询的Tenant
	 * @param userId 上传文档用户Id，可为空
	 * @return List<String>
	 */
	public static List<String> GetContainerBlobIds(Tenant tenant, String userId) {
		try {
			BlobProvider blobProvider = new BlobProvider(tenant);
			String container = tenant.getTenantName().toLowerCase();

			AuthToken authToken = GetAuthToken(container, userId, false);
			List<String> blobs = blobProvider.GetAllBlobIds(authToken);

			return blobs;
		} catch (Exception ex) {
			logger.error(ex.getMessage() + ", StackTrace: " + ex.getStackTrace());
			return null;
		}
	}

	/**
	 * 获取Tenant下的某种类型的文件的Id列表
	 * @param tenant 需要查询的Tenant
	 * @param userId 上传文档用户Id，可为空
	 * @param fileType 查询的文件类型
	 * @return List<String>
	 */
	public static List<String> GetTenantBlobIdsWithType(Tenant tenant, String userId, FileType fileType)
    {
        try
        {
        	List<BlobInfo> blobs = GetContainerBlobs(tenant, userId);

            return blobs.stream().filter(b -> b.getFileType() == fileType.toString()).map(b -> b.getId()).collect(Collectors.toList());
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage() + ", StackTrace: " + ex.getStackTrace());
            return null;
        }
    }

	/**
	 * 获取Tenant下Blob文件的最后修改时间
	 * @param tenant 需要查询的Tenant
	 * @param blobId 文档Id：BlobId
	 * @return Date
	 */
	public static Date GetBlobLastModifiedTime(Tenant tenant, String blobId) {
		BlobProvider blobProvider = new BlobProvider(tenant);
		String container = tenant.getTenantName().toLowerCase();
		return blobProvider.GetBlobLastModifiedTime(container, blobId);
	}

	/**
	 * 获取Tenant下Blob文件的元数据
	 * @param tenant 需要查询的Tenant
	 * @param blobId 文档Id：BlobId
	 * @return
	 */
	public static Map<String, String> GetBlobMetadata(Tenant tenant, String blobId){
		BlobProvider blobProvider = new BlobProvider(tenant);
		String container = tenant.getTenantName().toLowerCase();
		return blobProvider.GetBlobMetadata(container, blobId);
	}

	/**
	 * 将源文件拷贝至目标文件夹
	 * @param tenant 需要查询的Tenant
	 * @param copyUserId 拷贝源文件的用户Id：UserId
	 * @param copyblobId 拷贝源文件的文档Id：BlobId
	 * @param userId 拷贝目标文件的用户Id：UserId
	 * @param blobId 拷贝目标文件的文档Id：BlobId
	 * @return
	 */
	public static boolean CopyBlobToTemp(Tenant tenant, String copyUserId, String copyblobId, String userId, String blobId)
    {
        BlobInfo blobInfo = GetBlobById(tenant, copyUserId, copyblobId, false);
        if (blobInfo == null || blobInfo.getData() != null)
            return false;
        if (StringExtensions.isNullOrEmpty(userId))
            userId = copyUserId;
        String fileName = Base64Provider.DecodeString(blobInfo.getFileName());
        return SaveBlob(tenant, userId, blobInfo.getData(), blobId, blobInfo.getFileType(), blobInfo.getFileFormat(), StringExtensions.unicodeToChinese(fileName), true, null, null);
    }
	
	private static AuthToken GetAuthToken(String container, String userId, boolean isTempAuth)
    {
    	AuthToken result = new AuthToken();
        if (isTempAuth)
        {
        	result.setId(RoleConstants.AdminUserId.toLowerCase());
        	result.setContainerName(BlobUtil.BlobTempContainer.toLowerCase());
        	//result.setTenant(TenantConstant.DbaTenantName.toLowerCase());
        	result.setDomainName(container.toLowerCase());
        	result.setInternal(true);
        	result.setEncryptionKey("");
        } else {
         	result.setId(userId);
			result.setContainerName(container.toLowerCase());
			//result.setTenant(container.toLowerCase());
			result.setDomainName(container.toLowerCase());
			result.setInternal(true);
			result.setEncryptionKey("");
        }
        return result;
    }
}
