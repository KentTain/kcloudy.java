package kc.web.controller;

import java.util.*;

import kc.dto.PaginatedBaseDTO;
import kc.dto.TreeNodeSimpleDTO;
import kc.dto.account.OrganizationSimpleDTO;
import kc.dto.search.RootOrgWithUsersPaginatedSearchDTO;
import kc.dto.search.RootOrgWithUsersSearchDTO;
import kc.service.base.ServiceResult;
import kc.web.models.SelectUsersViewModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import kc.dto.account.MenuNodeSimpleDTO;
import kc.framework.GlobalConfig;
import kc.framework.tenant.ApplicationConstant;
import kc.web.base.WebBaseController;

import javax.servlet.http.HttpServletRequest;

@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping(value="/")
public class HomeController extends WebBaseController {
	@RequestMapping("/")
	public String index(Locale locale, ModelMap model) {
		model.addAttribute("title", GlobalConfig.CurrentApplication.getAppName());
		model.addAttribute("tenantDisplayName", CurrentUserTenantName());
		model.addAttribute("userName", CurrentUserName());
		model.addAttribute("userDisplayName", CurrentUserDisplayName());
		model.addAttribute("userEmail", CurrentUserEmail());
		model.addAttribute("userPhone", CurrentUserPhone());
		return "index";
	}

	@GetMapping("/Home/GetMenuIdByUrl")
	public @ResponseBody
	ServiceResult<Integer> GetMenuIdByUrl(String url) {
		return GetServiceResult(() -> {
			List<MenuNodeSimpleDTO> allMenus = GetCachedCurrentUserMenus();
			UUID appId = GlobalConfig.ApplicationGuid;
			Optional<MenuNodeSimpleDTO> menu = allMenus.stream()
					.filter(m -> m.getApplicationId().equals(appId) && m.getUrl().contains(url)).findFirst();
			return menu.map(TreeNodeSimpleDTO::getId).orElse(0);
		}, log);
	}


	/*------------------------------------------文件上传-----------------------------------------------*/
	@PostMapping("/Home/UploadFileToTemp")
	public @ResponseBody
	kc.web.models.UploadViewModel UploadFileToTemp(HttpServletRequest request) {

		return super.Upload(request, true);
	}

	@PostMapping("/Home/Upload")
	@PreAuthorize("hasAuthority('" + ApplicationConstant.DefaultAuthorityId + "')")
	public @ResponseBody
	kc.web.models.UploadViewModel Upload(HttpServletRequest request) {

		return super.Upload(request, false);
	}

	@GetMapping("/Home/ChunkCheck")
	public @ResponseBody
	String ChunkCheck(HttpServletRequest request, String name, Integer chunkIndex, long size) {

		return ChunkCheck(request, name, chunkIndex, size);
	}

	@GetMapping("/Home/ChunksMerge")
	public @ResponseBody
	String ChunksMerge(HttpServletRequest request, String folder, String name, int chunks, String type, String blobId, String ext, String userId) {

		return ChunksMerge(request, folder, name, chunks, type, blobId, ext, userId);
	}

	@GetMapping("/Home/Upload")
	public @ResponseBody
	String Upload(String parm) {

		return super.Upload(parm);
	}

	@GetMapping("/Home/ShowTempImg")
	public ResponseEntity<byte[]> showImageTemp(String id) {
		return super.getFile(id, CurrentUserId(), true);
	}

	@GetMapping("/Home/ShowImage")
	public ResponseEntity<byte[]> showImage(String blobId, String userId) {
		return super.getFile(blobId, CurrentUserId(), false);
	}

	/*----------------------------------选取部门小控件：selectUserPartial------------------------------------*/

	/**
	 * 获取所有的部门信息及下属员工
	 *
	 * @return
	 */
	@PostMapping("/Home/GetRootOrganizationsWithUsers")
	public @ResponseBody
	SelectUsersViewModel GetRootOrganizationsWithUsers() {
		List<OrganizationSimpleDTO> res = accountApiService.LoadOrgTreesWithUsers();
		Optional<OrganizationSimpleDTO> firstOrg = res.stream().findFirst();
		firstOrg.ifPresent(organizationDTO -> organizationDTO.setChecked(true));
		return new SelectUsersViewModel(res);
	}

	/**
	 * 获取所有的部门信息及下属员工
	 *
	 * @param roleIds
	 * @return
	 */
	@PostMapping("/Home/GetRootOrganizationsWithUsersByRoleIds")
	public @ResponseBody
	SelectUsersViewModel GetRootOrganizationsWithUsersByRoleIds(
			@RequestParam(value = "roleIds[]", required = false) List<String> roleIds) {
		List<OrganizationSimpleDTO> res = new ArrayList<>();
		if (roleIds != null && roleIds.size() > 0) {
			res = accountApiService.LoadOrganizationsWithUsersByRoleIds(roleIds);
		} else {
			res = accountApiService.LoadOrgTreesWithUsers();
		}

		Optional<OrganizationSimpleDTO> firstOrg = res.stream().findFirst();
		firstOrg.ifPresent(organizationDTO -> organizationDTO.setChecked(true));
		return new SelectUsersViewModel(res);
	}


	/**
	 * 获取所有的部门信息及下属员工(erp)
	 *
	 * @param roleIds
	 * @param departName
	 * @param departCode
	 * @param userName
	 * @param userCode
	 * @param searchKey
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@PostMapping("/Home/GetRootOrganizationsAndUsers")
	public @ResponseBody
	PaginatedBaseDTO<OrganizationSimpleDTO> GetRootOrganizationsAndUsers(
			List<String> roleIds, String departName, String departCode, String userName,
			String userCode, String searchKey, int pageIndex, int pageSize) {
		RootOrgWithUsersPaginatedSearchDTO searchModel = new RootOrgWithUsersPaginatedSearchDTO();
		{
			//searchModel.setAppid(CurrentOperationApplicationId);
			searchModel.setRoleIds(roleIds);
			searchModel.setDepartName(departName);
			searchModel.setDepartCode(departCode);
			searchModel.setUserName(userName);
			searchModel.setUserCode(userCode);
			searchModel.setSearchKey(searchKey);
			searchModel.setPageIndex(pageIndex);
			searchModel.setPageSize(pageSize);
		}
		;
		PaginatedBaseDTO<OrganizationSimpleDTO> res = accountApiService.LoadPaginatedOrganizationsWithUsersByFilter(searchModel);
		Optional<OrganizationSimpleDTO> firstOrg = res.getRows().stream().findFirst();
		firstOrg.ifPresent(organizationSimpleDTO -> organizationSimpleDTO.setChecked(true));
		return res;
	}

	/**
	 * 获取相关部门以及角色信息及下属员工
	 *
	 * @param roleIds
	 * @param depIds
	 * @return
	 */
	@PostMapping("/Home/GetRootOrgWithUsersByRoleIdsAndOrgids")
	public @ResponseBody
	SelectUsersViewModel GetRootOrgWithUsersByRoleIdsAndOrgids(List<String> roleIds, List<Integer> depIds) {
		RootOrgWithUsersSearchDTO searchModel = new RootOrgWithUsersSearchDTO();
		searchModel.setRoleIds(roleIds);
		searchModel.setOrgIds(depIds);

		List<OrganizationSimpleDTO> res = accountApiService.LoadOrgsWithUsersByRoleIdsAndOrgids(searchModel);
		Optional<OrganizationSimpleDTO> firstOrg = res.stream().findFirst();
		firstOrg.ifPresent(organizationSimpleDTO -> organizationSimpleDTO.setChecked(true));
		return new SelectUsersViewModel(res);
	}


}
