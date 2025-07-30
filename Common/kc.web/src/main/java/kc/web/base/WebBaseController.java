package kc.web.base;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import kc.framework.enums.PositionLevel;
import kc.framework.enums.UserType;
import kc.framework.tenant.Tenant;
import kc.framework.util.MimeTypeHelper;
import kc.framework.base.BlobInfo;
import kc.framework.enums.DocFormat;
import kc.framework.enums.FileType;
import kc.framework.extension.ListExtensions;
import kc.framework.tenant.TenantContext;
import kc.framework.util.FileUtil;
import kc.web.models.UploadViewModel;
import kc.storage.util.BlobUtil;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import kc.dto.TreeNodeDTO;
import kc.dto.TreeNodeSimpleDTO;
import kc.dto.account.MenuNodeSimpleDTO;
import kc.dto.account.PermissionSimpleDTO;
import kc.framework.GlobalConfig;
import kc.framework.base.ApplicationInfo;
import kc.framework.extension.StringExtensions;
import kc.framework.tenant.RoleConstants;
import kc.framework.tenant.TenantConstant;
import kc.service.ServiceWrapper;
import kc.service.base.ServiceResult;
import kc.web.multitenancy.IdSrvOidcUser;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * Controller的基类，包含当前登录用户信息及菜单、权限数据
 * 
 * @author 田长军
 *
 */
@lombok.extern.slf4j.Slf4j
public class WebBaseController {
	// 自定义类型转换器
	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"), true));
		binder.registerCustomEditor(java.math.BigDecimal.class, new CustomerBigDecimalEditor());
	}

	private static class CustomerBigDecimalEditor extends PropertyEditorSupport {
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if (text == null || text.equals("")) {
				text = "0";
			}
			setValue(new java.math.BigDecimal(text));
		}

		@Override
		public String getAsText() {
			Object decObj = getValue();
			if (decObj == null)
				return "0";
			return getValue().toString();
		}
	}

	@Autowired
	protected kc.service.webapiservice.IAccountApiService accountApiService;

	@Autowired
	protected kc.service.webapiservice.thridparty.IGlobalConfigApiService globalConfigApiService;

	/*------------------------------------------当前租户信息----------------------------------------------*/
	protected Tenant getTenant(){
		return TenantContext.getCurrentTenant();
	}
	protected String getTenantName(){
		Tenant tenant = TenantContext.getCurrentTenant();
		if (tenant == null) return "";

		return tenant.getTenantName();
	}
	protected String getTenantDisplayName(){
		Tenant tenant = TenantContext.getCurrentTenant();
		if (tenant == null) return "";

		return tenant.getTenantDisplayName();
	}

	/*------------------------------------------当前用户信息----------------------------------------------*/
    protected Authentication authentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	protected IdSrvOidcUser userDetails() {
		if (authentication().getPrincipal() instanceof IdSrvOidcUser) {
			return (IdSrvOidcUser) authentication().getPrincipal();
		}

		return null;
	}

	protected boolean hasAuthority(String authorityId) {
		Collection<? extends GrantedAuthority> auths = authentication().getAuthorities();
		return auths.parallelStream().anyMatch(m -> m.getAuthority().equalsIgnoreCase(authorityId));
	}

	/**
	 * 当前登录用户所属企业名
	 * 
	 * @return String
	 */
	protected String CurrentUserTenantName() {
		IdSrvOidcUser user = userDetails();
		return user != null ? user.getTenantName() : "";
	}

	/**
	 * 当前登录用户是否授权
	 * 
	 * @return String
	 */
	protected boolean IsAuthenticated() {
		return authentication().isAuthenticated();
	}

	/**
	 * 当前登录用户Id
	 * 
	 * @return String
	 */
	protected String CurrentUserId() {
		IdSrvOidcUser user = userDetails();
		return user != null ? user.getId() : "";
	}

	/**
	 * 当前登录用户名
	 *
	 * @return String
	 */
	protected UserType CurrentUserType() {
		IdSrvOidcUser user = userDetails();
		return user != null ? user.getUserType() : null;
	}

	/**
	 * 当前登录用户名
	 * 
	 * @return String
	 */
	protected String CurrentUserName() {
		return authentication().getName();

	}

	/**
	 * 当前登录用户显示名
	 * 
	 * @return String
	 */
	protected String CurrentUserDisplayName() {
		IdSrvOidcUser user = userDetails();
		return user != null ? user.getDisplayName() : "";
	}

	/**
	 * 当前登录用户邮箱
	 * 
	 * @return String
	 */
	protected String CurrentUserEmail() {
		IdSrvOidcUser user = userDetails();
		return user != null ? user.getEmail() : "";
	}

	/**
	 * 当前登录用户手机号
	 * 
	 * @return String
	 */
	protected String CurrentUserPhone() {
		IdSrvOidcUser user = userDetails();
		return user != null ? user.getPhone() : "";
	}

	/**
	 * 当前登录用户组织Id列表
	 *
	 * @return List<Integer>
	 */
	protected List<Integer> CurrentUserOrgIds() {
		IdSrvOidcUser user = userDetails();
		return user != null ? user.getOrgIds() : new ArrayList<>();
	}

	/**
	 * 当前登录用户组织编码列表
	 *
	 * @return List<String>
	 */
	protected List<String> CurrentUserOrgCodes() {
		IdSrvOidcUser user = userDetails();
		return user != null ? user.getOrgCodes() : new ArrayList<>();
	}

	/**
	 * 当前登录用户角色Id列表
	 * 
	 * @return List<String>
	 */
	protected List<String> CurrentUserRoleIds() {
		IdSrvOidcUser user = userDetails();
		return user != null ? user.getRoleIds() : new ArrayList<>();
	}

	/**
	 * 当前登录用户角色名列表
	 * 
	 * @return List<String>
	 */
	protected List<String> CurrentUserRoleNames() {
		IdSrvOidcUser user = userDetails();
		return user != null ? user.getRoleNames() : new ArrayList<>();
	}

	/**
	 * 当前登录用户岗位
	 *
	 * @return PositionLevel
	 */
	protected PositionLevel CurrentUserPosition() {
		IdSrvOidcUser user = userDetails();
		return user != null ? PositionLevel.Staff : PositionLevel.Staff;
	}
	
	/**
	 * 是否为系统管理员
	 * 
	 * @return List<String>
	 */
	protected boolean IsSystemAdmin() {
		return CurrentUserRoleIds().contains(RoleConstants.AdminRoleId);
	}

	/**
	 * 返回boolean类型的快捷方式
	 * 
	 * @param success 是否成功
	 * @param message 失败后的消息
	 * @return
	 */
	protected String ThrowErrorJsonMessage(boolean success, String message) {
		return "{\"success\":\"" + success + "\",\"message\":\"\"" + message + "\"\"}";
	}

	/**
	 * 获取ServiceResult<T>对象
	 * 
	 * @param func   Lamdba表达式: Supplier<T>
	 * @param logger 记录错误日志
	 * @return ServiceResult<T>
	 */
		protected <T> ServiceResult<T> GetServiceResult(Supplier<T> func, Logger logger) {
		return ServiceWrapper.Invoke("ControllerBase", func.getClass().getName(), func, logger);
	}

	/*------------------------------------------当前用户菜单及权限----------------------------------------------*/

	/**
	 * 获取归属于当前登录用户的菜单列表
	 * 
	 * @return List<MenuNodeSimpleDTO>
	 */
	protected List<MenuNodeSimpleDTO> GetCachedCurrentUserMenus() {
		// String lowerTenant = CurrentUserTenantName().toLowerCase();
		// String cacheKey = lowerTenant + "-CurrentUserMenus-" + CurrentUserId();
		// List<MenuNodeSimpleDTO> cache =
		// Service.Core.CacheUtil.GetCacheAsync<List<MenuNodeSimpleDTO>>(cacheKey);
		// Logger.LogDebug(string.Format("-----Get CurrentUser cache is null? %s",
		// cache == null ? "True" : "Flase"));
		// if (cache == null || !cache.Any())
		{
			List<MenuNodeSimpleDTO> currentUserMenus = new ArrayList<MenuNodeSimpleDTO>();
			// 后台用户需要权限、角色、组织、菜单等信息
			List<MenuNodeSimpleDTO> menus = accountApiService.LoadUserMenusByRoleIds(CurrentUserRoleIds());
			if (menus == null)
				return currentUserMenus;

			menus.sort(Comparator.comparingInt(MenuNodeSimpleDTO::getLevel).thenComparingInt(MenuNodeSimpleDTO::getIndex));
			currentUserMenus = menus;
			// await Service.Core.CacheUtil.SetCacheAsync(cacheKey, currentUserMenus,
			// TimeSpan.FromMinutes(TimeOutConstants.CacheTimeOut));
			return currentUserMenus;
		}
		// else
		// {
		// return cache;
		// }
	}

	/**
	 * 根据条件，获取归属于当前登录用户的菜单树（树结构）
	 * 
	 * @return List<MenuNodeSimpleDTO>
	 */
	protected List<MenuNodeSimpleDTO> GetCurrentUserMenuTree(Predicate<MenuNodeSimpleDTO> predict) {
		List<MenuNodeSimpleDTO> allmenus = GetCachedCurrentUserMenus();
		List<MenuNodeSimpleDTO> menus = allmenus.stream().filter(predict)
				.sorted(Comparator.comparingInt(MenuNodeSimpleDTO::getLevel).thenComparingInt(MenuNodeSimpleDTO::getIndex))
				.collect(Collectors.toList());

		List<MenuNodeSimpleDTO> result = new ArrayList<MenuNodeSimpleDTO>();
		for (MenuNodeSimpleDTO parent : menus.stream().filter(m -> m.getParentId() == null)
				//.sorted(Comparator.comparing(MenuNodeSimpleDTO::getIndex))
				.collect(Collectors.toList())) {
			SetUrlWithDomain(parent, menus);
			result.add(parent);
		}
		return result;
	}

	/**
	 * 拼凑菜单树，同时将菜单的URL更新为包含Domain信息的路径
	 * 
	 * @param parent   需要
	 * @param nodeList
	 */
	private void SetUrlWithDomain(MenuNodeSimpleDTO parent, List<MenuNodeSimpleDTO> nodeList) {
		List<MenuNodeSimpleDTO> child = nodeList.stream()
				.filter(m -> m.getParentId() != null && m.getParentId() == parent.getId())
				.sorted(Comparator.comparingInt(MenuNodeSimpleDTO::getLevel).thenComparingInt(MenuNodeSimpleDTO::getIndex))
				.collect(Collectors.toList());
		parent.getChildren().addAll(child);

		for (MenuNodeSimpleDTO children : child) {
			SetUrlWithDomain(children, nodeList);
		}
		
		UUID appId = parent.getApplicationId();
		Optional<ApplicationInfo> app = GlobalConfig.Applications.stream()
				.filter(m -> m.getAppId().equals(appId)).findFirst();
		if (app.isPresent() && !StringExtensions.isNullOrEmpty(app.get().getAppDomain())) {
			String domain = app.get().getAppDomain().replace(TenantConstant.SubDomain, CurrentUserTenantName());
			parent.setUrl(domain.endsWith("/") ? domain.substring(0, domain.length() - 1) + parent.getUrl()
					: domain + parent.getUrl());
		}
	}

	/**
	 * 获取归属于当前登录用户的权限列表
	 * 
	 * @return List<PermissionSimpleDTO>
	 */
	protected List<PermissionSimpleDTO> GetCachedCurrentUserPermissions() {
		// String lowerTenant = CurrentUserTenantName().toLowerCase();
		// String cacheKey = lowerTenant + "-CurrentUserPermissions-" + CurrentUserId();
		// List<PermissionSimpleDTO> cache =
		// Service.Core.CacheUtil.GetCacheAsync<List<PermissionSimpleDTO>>(cacheKey);
		// Logger.LogInformation(string.Format("-----Get CurrentUser cache is null?
		// %s", cache == null ? "True" : "Flase"));
		// if (cache == null)
		{
			List<PermissionSimpleDTO> currentUserPermissions = new ArrayList<PermissionSimpleDTO>();
			// 后台用户需要权限、角色、组织、菜单等信息
			List<PermissionSimpleDTO> permissions = accountApiService.LoadUserPermissionsByRoleIds(CurrentUserRoleIds());
			if (permissions == null)
				return currentUserPermissions;

			currentUserPermissions = permissions;
			// Service.Core.CacheUtil.SetCacheAsync(cacheKey, currentUserPermissions,
			// TimeSpan.FromMinutes(TimeOutConstants.CacheTimeOut));

			return currentUserPermissions;
		}
		// else
		// {
		// return cache;
		// }
	}
	
	/**
	 * 根据条件，获取归属于当前登录用户的菜单树（树结构）
	 * 
	 * @return List<PermissionSimpleDTO>
	 */
	protected List<PermissionSimpleDTO> GetCurrentPermissionTree(Predicate<PermissionSimpleDTO> predict) {
		List<PermissionSimpleDTO> allmenus = GetCachedCurrentUserPermissions();
		List<PermissionSimpleDTO> menus = allmenus.stream().filter(predict)
				.sorted(Comparator.comparing(PermissionSimpleDTO::getIndex))
				.collect(Collectors.toList());

		List<PermissionSimpleDTO> result = new ArrayList<PermissionSimpleDTO>();
		for (PermissionSimpleDTO parent : menus.stream().filter(m -> m.getParentId() == null)
				//.sorted(Comparator.comparing(PermissionSimpleDTO::getIndex))
				.collect(Collectors.toList())) {
			GetActionPermissionWithChild(parent, menus);
			result.add(parent);
		}
		return result;
	}

	private void GetActionPermissionWithChild(PermissionSimpleDTO parent, List<PermissionSimpleDTO> allOrgs)
    {
		List<PermissionSimpleDTO> child = allOrgs.stream().filter(m -> m.getParentNode() != null && m.getParentNode().getId() == parent.getId())
				//.sorted(Comparator.comparing(PermissionSimpleDTO::getIndex).reversed())
				.collect(Collectors.toList());
		parent.getChildren().addAll(child);
        for (PermissionSimpleDTO children : child)
        {
            children.setParentName(parent.getText());
            GetActionPermissionWithChild(children, allOrgs);
        }
    }
	/**
	 * 根据条件，将树结构列表中，归属于父节点的所有的递归子节点拼凑至父节点，形成一颗以父节点为顶层的树
	 * 
	 * @param          <T> TreeNodeSimpleDTO<T>
	 * @param parent   树形结构的父节点
	 * @param nodeList 树结构列表
	 * @param predict  拼凑条件
	 * 
	 */
	protected <T extends TreeNodeDTO<T>> void GetTreeWithChild(T parent, List<T> nodeList,
			Predicate<T> predict) {
		if (predict == null) {
			List<T> child = nodeList.stream().filter(m -> m.getParentNode() != null && m.getParentNode().getId() == parent.getId())
					.sorted(Comparator.comparing(T::getIndex).reversed())
					.collect(Collectors.toList());
			parent.getChildren().addAll(child);
			for (T children : child) {
				GetTreeWithChild(children, nodeList, predict);
			}
		} else {
			List<T> child = nodeList.stream().filter(m -> m.getParentNode() != null && m.getParentNode().getId() == parent.getId()).filter(predict)
					.sorted(Comparator.comparing(T::getIndex).reversed())
					.collect(Collectors.toList());
			parent.getChildren().addAll(child);
			for (T children : child) {
				GetTreeWithChild(children, nodeList, predict);
			}
		}
	}
	
	/**
	 * 根据条件，将树结构列表中，归属于父节点的所有的递归子节点拼凑至父节点，形成一颗以父节点为顶层的树
	 * 
	 * @param          <T> TreeNodeSimpleDTO<T>
	 * @param parent   树形结构的父节点
	 * @param nodeList 树结构列表
	 * @param predict  拼凑条件
	 * 
	 */
	protected <T extends TreeNodeSimpleDTO<T>> void GetSimpleTreeWithChild(T parent, List<T> nodeList, Predicate<T> predict) {
		if (predict == null) {
			List<T> child = nodeList.stream().filter(m -> m.getParentId() != null && m.getParentId().equals(parent.getId()))
					//.sorted(Comparator.comparing(T::getIndex).reversed())
					.collect(Collectors.toList());
			parent.getChildren().addAll(child);
			for (T children : child) {
				GetSimpleTreeWithChild(children, nodeList, predict);
			}
		} else {
			List<T> child = nodeList.stream().filter(m -> m.getParentId() != null && m.getParentId().equals(parent.getId())).filter(predict)
					//.sorted(Comparator.comparing(T::getIndex).reversed())
					.collect(Collectors.toList());
			parent.getChildren().addAll(child);
			for (T children : child) {
				GetSimpleTreeWithChild(children, nodeList, predict);
			}
		}
	}

	/*------------------------------------------文件操作----------------------------------------------*/
	/**
	 * 获取临时文件夹
	 *
	 * @param request
	 * @return
	 */
	protected String getTempDir(HttpServletRequest request) {
		return StringExtensions.endWithSlash(getServerPath(request)) + "temp";
	}

	/**
	 * 获取Web站点根目录
	 *
	 * @param request
	 * @return
	 */
	protected String getServerPath(HttpServletRequest request) {
		return request.getServletContext().getRealPath("");// 获取项目动态绝对路径
	}

	/**
	 *  * 获取客户端请求参数中所有的信息  * @param request  * @return  
	 */
	protected Map<String, String> getAllRequestParam(final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
				// 如果字段的值为空，判断若值为空，则删除这个字段>
				if (null == res.get(en) || "".equals(res.get(en))) {
					res.remove(en);
				}
			}
		}
		return res;
	}

	/**
	 * 获取上传文件的配置（WebUploader前端组件）
	 *
	 * @param parm
	 * @return
	 */
	protected String Upload(String parm) {
		if ("config".equals(parm)) {
			String domainName = GlobalConfig.SSOWebDomain;
//				String serverpath = ResourceUtils.getURL("classpath:static").getPath()
//						.replace("%20", " ").replaceFirst("/", "").replace('/', '\\');

			//String configPath = serverpath + "\\js\\ueditor\\config.json";
			String configContent = null;
			//List<String> configList = Files.readAllLines(Paths.get(configPath));
			//configContent = String.join("", configList);

			configContent = globalConfigApiService.GetUEditorConfig();
			configContent = configContent.replace("{ApiDomain}", domainName)
					.replace("{imageMaxSize}", Integer.toString(GlobalConfig.UploadConfig.getImageMaxSize() * 1024 * 1024))
					.replace("{fileMaxSize}", Integer.toString(GlobalConfig.UploadConfig.getFileMaxSize() * 1024 * 1024));
			return configContent;
		}

		UploadViewModel result = new UploadViewModel().builder()
				.success(false).message("请选择上传文件").state("请选择上传文件")
				.url("").id("").title("")
				.original("").error("请选择上传文件").build();

		return "{\"success\":\"" + false + "\"," + "\"message\":\"请选择上传文件\"," + "\"state\":\"请选择上传文件\","
				+ "\"url\":\"\"," + "\"id\":\"\"," + "\"title\":\"\"," + "\"original\":\"\","
				+ "\"error\":\"请选择上传文件\"}";
	}

	/**
	 * 文件分片检查（WebUploader前端组件）
	 *
	 * @param name       文件md5
	 * @param chunkIndex 分片序列
	 * @param size       分片文件的大小
	 * @return
	 */
	protected String ChunkCheck(HttpServletRequest request, String name, Integer chunkIndex, long size) {
		String tempDir = getTempDir(request);
		Path chunkFile = Paths.get(tempDir, name, chunkIndex.toString());
		// 分片已存在
		if (Files.exists(chunkFile) && size == chunkFile.toFile().length()) {
			return "{\"ifExist\": true}";
		}
		// 分片不存在
		return "{\"ifExist\": false}";
	}

	/**
	 * 文件分片合并（WebUploader前端组件）
	 *
	 * @param request
	 * @param folder
	 * @param name
	 * @param chunks
	 * @param type
	 * @param blobId
	 * @param ext
	 * @param userId
	 * @return
	 */
	protected String ChunksMerge(HttpServletRequest request, String folder, String name, int chunks, String type, String blobId, String ext, String userId) {
		String tempDir = getTempDir(request);
		// 分片存储目录
		Path chunkFileDir = Paths.get(tempDir, folder);

		// 分片列表
		List<Path> chunkFiles = FileUtil.getAllFilePaths(chunkFileDir);

		// 检查是否满足合并条件：分片数量是否足够
		if (chunks == chunkFiles.size()) {
			// 按照名称排序文件，这里分片都是按照数字命名的
			chunkFiles = chunkFiles.stream().sorted(Comparator.comparing(m -> m.getFileName()))
					.collect(Collectors.toList());
			Path newFilePath = Paths.get(tempDir, name);
			try {
				if (!Files.exists(newFilePath))
					Files.createFile(newFilePath);

				for (Path chunkFile : chunkFiles) {
					byte[] heByte = Files.readAllBytes(chunkFile);

					Files.write(newFilePath, heByte, StandardOpenOption.APPEND);
					Files.deleteIfExists(chunkFile);
				}

				// Copy
				byte[] heByte = Files.readAllBytes(newFilePath);

				String blobUserId = StringExtensions.isNullOrEmpty(userId) ? CurrentUserId() : userId;
				blobId = StringExtensions.isNullOrEmpty(blobId) ? UUID.randomUUID().toString() : blobId;
				String fileType = MimeTypeHelper.GetFileType(ext).toString();
				String fileFormat = MimeTypeHelper.GetFileFormatByExt(ext);

				BlobUtil.SaveBlob(TenantContext.getCurrentTenant(), blobUserId, heByte, blobId, fileType, fileFormat, name,
						true, null, null);

				Files.deleteIfExists(newFilePath);
			} catch (Exception ex) {
				UploadViewModel result = new UploadViewModel().builder()
						.success(false).message(ex.getMessage()).state("")
						.url("").id("").title("")
						.original("").error(ex.getMessage()).build();

				return "{\"success\":\"" + false + "\",\"message\":\"" + ex.getMessage() + "\"}";
			}
			// 清理：文件夹，tmp文件夹
			cleanFileFolder(chunkFileDir.toAbsolutePath().toString());

			UploadViewModel result = new UploadViewModel().builder()
					.success(false).message("").state("")
					.url("").id("").title("")
					.original("").error("分片文件个数与所需合并的文件个数不一致").build();

			return "{\"success\":\"" + false + "\",\"message\":\"\"}";
		}

		UploadViewModel result = new UploadViewModel().builder()
				.success(true).message("").state("")
				.url("").id("").title("")
				.original("").error("").build();

		return "{\"success\":\"" + true + "\",\"message\":\"\"}";
	}

	/**
	 * 上传文件
	 *
	 * @param request
	 * @return
	 */
	protected UploadViewModel Upload(HttpServletRequest request, boolean isTempAuth) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<MultipartFile> files = multipartRequest.getFiles("file");
		MultipartFile file = files.size() != 0 ? files.get(0) : null;
		if (file == null) {
			return new UploadViewModel().builder()
					.success(false).message("请选择上传文件").state("")
					.url("").id("").title("")
					.original("").error("请选择上传文件").build();
		}

		Map<String, String> parms = getAllRequestParam(request);
		String parm = parms.containsKey("parm") ? parms.get("parm") : "";
		String md5 = parms.containsKey("md5") ? parms.get("md5") : "";
		String type = parms.containsKey("uploadType") ? parms.get("uploadType") : "";
		String extensionStr = parms.containsKey("ext") ? parms.get("ext") : "";
		String userId = parms.containsKey("userId") ? parms.get("userId") : "";
		Integer start = parms.containsKey("start") ? Integer.parseInt(parms.get("start")) : 0;
		String blobId = parms.containsKey("blobId") ? parms.get("blobId") : "";
		String fileName = file.getOriginalFilename();
		if ("listimage".equalsIgnoreCase(parm)) {
			return ListImage(CurrentUserId(), start);
		} else if ("uplistfile".equalsIgnoreCase(parm)) {
			return ListFile(CurrentUserId(), start);
		}

		userId = !StringExtensions.isNullOrEmpty(userId) ? userId : CurrentUserId();
		blobId = !StringExtensions.isNullOrEmpty(blobId) ? blobId : UUID.randomUUID().toString();
		extensionStr = !StringExtensions.isNullOrEmpty(extensionStr)
				? extensionStr
				: StringExtensions.remove(fileName, 0, fileName.lastIndexOf('.') + 1).toLowerCase();
		String fileType = MimeTypeHelper.GetFileType(extensionStr).toString();
		String fileFormat = MimeTypeHelper.GetFileFormatByExt(extensionStr);

		boolean isImageUpload = "uploadimage".equalsIgnoreCase(type);
		List<String> allowExtension = new ArrayList<>();
		String errMsg = "";
		long fileSize = 10 * 1024 * 1024;
		if (GlobalConfig.UploadConfig != null) {
			allowExtension = isImageUpload
					? Arrays.asList(GlobalConfig.UploadConfig.getImageExt().split(","))
					: Arrays.asList(ListExtensions.concat(GlobalConfig.UploadConfig.getFileExt().split(","), GlobalConfig.UploadConfig.getImageExt().split(",")));
			errMsg = isImageUpload
					? "不允许上传此类型的文件。 \n只允许" + GlobalConfig.UploadConfig.getImageExt() + " 格式。"
					: "不允许上传此类型的文件。 \n只允许" + GlobalConfig.UploadConfig.getImageExt() + "," + GlobalConfig.UploadConfig.getFileExt() + " 格式。";
			fileSize = isImageUpload
					? GlobalConfig.UploadConfig.getImageMaxSize() * 1024 * 1024
					: GlobalConfig.UploadConfig.getFileMaxSize() * 1024 * 1024;
		}

		// 检查扩展名
		if (allowExtension.size() > 0 && !allowExtension.contains(extensionStr)) {
			return new UploadViewModel().builder()
					.success(false).message("").state("请选择上传文件")
					.url("").id("").title("")
					.original("").error(errMsg).build();
		}

		// 文件大小
		if (file.getSize() > fileSize) {
			return UploadViewModel.builder()
					.success(false).message("").state("文件大小超过限制")
					.url("").id("").title("")
					.original("").error("文件大小超过限制").build();
		}

		try {
			boolean success = true;
			if (parms.containsKey("chunk")) {
				//获得上传的分片数据流
				Path path = Paths.get(getTempDir(), md5);
				String pathString = path.toAbsolutePath().toString();
				if(createFileFolder(pathString)){
					//取得chunk和chunks
					Integer chunk = Integer.parseInt(parms.get("chunk"));//当前分片在上传分片中的顺序（从0开始）
					//int chunks = Integer.parseInt(parms.get("chunks"));//总分片数

					Path checkfileName = Paths.get(pathString, chunk.toString());
					String checkfileString = path.toAbsolutePath().toString();
					file.transferTo(new File(checkfileString));
				}

			} else {
				byte[] buffer = file.getBytes();
				success = BlobUtil.SaveBlob(TenantContext.getCurrentTenant(), userId, buffer, blobId, fileType, fileFormat, fileName, isTempAuth,
						null, null);

			}

			return UploadViewModel.builder()
					.success(success).message("").state("")
					.url("?id=" + blobId + "&uId=" + userId).id(blobId).title(fileName)
					.original(fileName).error("").build();

		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);

			return UploadViewModel.builder()
					.success(false).message(ex.getMessage()).state("")
					.url("").id("").title(fileName)
					.original(fileName).error("").build();
		}
	}

	protected String getTempDir(){
		return  "";
	}

	protected String TempImgUrl(String blobId)
	{
		return "/Home/ShowTempImg?id=" + blobId;
		//return Url.Action("ShowTempImg", new { id = blobId });
	}
	protected String TempFileUrl(String blobId) {
		return "/Home/DownloadTempFile?id=" + blobId;
		//return Url.Action("DownloadTempFile", new { id = blobId });
	}

	protected UploadViewModel ListImage(String userId, int start) {
		List<String> fileList = BlobUtil.GetTenantBlobIdsWithType(TenantContext.getCurrentTenant(), userId, FileType.Image);

		List<String> urls = fileList.stream().map(m -> String.format("?id={%s}&uId={%s}", m, userId))
				.collect(Collectors.toList());
		List<String> ids = fileList.stream().collect(Collectors.toList());

		return UploadViewModel.builder()
				.success(true).message("").state("SUCCESS")
				.url("").id("").title("")
				.list(urls).ids(ids).start(start).size(20).total(fileList.size())
				.original("").error("").build();
	}

	protected UploadViewModel ListFile(String userId, int start) {
		List<String> fileList = BlobUtil.GetTenantBlobIdsWithType(TenantContext.getCurrentTenant(), userId, FileType.Excel);

		List<String> urls = fileList.stream().map(m -> String.format("?id={%s}&uId={%s}", m, userId))
				.collect(Collectors.toList());
		List<String> ids = fileList.stream().collect(Collectors.toList());

		return UploadViewModel.builder()
				.success(true).message("").state("SUCCESS")
				.url("").id("").title("")
				.list(urls).ids(ids).start(start).size(20).total(fileList.size())
				.original("").error("").build();
	}

	private boolean createFileFolder(String folder) {
		Path path = Paths.get(folder);
		// 创建存放分片文件的临时文件夹
		if (!Files.notExists(path)) {
			try {
				Files.createDirectory(path);
			} catch (IOException ex) {
				return false;
			}
		}

		Path tempFile = Paths.get(folder + ".tmp");
		if (Files.notExists(tempFile)) {
			try {
				// 创建一个对应的文件，用来记录上传分片文件的修改时间，用于清理长期未完成的垃圾分片
				Files.createDirectory(tempFile);
			} catch (IOException ex) {
				return false;
			}
		}

		return true;
	}

	private void cleanFileFolder(String folder) {
		try {
			// 删除分片文件夹
			Files.deleteIfExists(Paths.get(folder));
			// 删除tmp文件
			Files.deleteIfExists(Paths.get(folder + ".tmp"));
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	protected ResponseEntity<byte[]> getFile(String blobId, String userId, boolean isTempAuth) {
		if (StringExtensions.isNullOrEmpty(userId))
			userId = CurrentUserId();
		BlobInfo blob = BlobUtil.GetBlobById(TenantContext.getCurrentTenant(), userId, blobId, isTempAuth);
		if (blob == null || blob.getData() == null)
			return null;

		DocFormat docFormat = DocFormat.valueOf(blob.getFileFormat());
		byte[] buffer = blob.getData();
		String contentType = MimeTypeHelper.GetMineType(docFormat);
		String decodeFileName = blob.getFileName() != null
				? StringExtensions.unicodeToChinese(blob.getFileName())
				: blobId;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentLength(buffer.length);
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename(decodeFileName).build());
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());

		ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(buffer, headers, HttpStatus.OK);
		return responseEntity;
	}

}
