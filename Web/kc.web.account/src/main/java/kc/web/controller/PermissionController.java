package kc.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import kc.enums.ResultType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kc.service.base.ServiceResult;
import kc.service.account.IPermissionService;
import kc.service.account.IRoleService;
import kc.web.annotation.MenuAnnotation;
import kc.web.annotation.PermissionAnnotation;
import kc.web.base.WebBaseController;
import kc.framework.tenant.TenantConstant;
import kc.dto.account.PermissionDTO;
import kc.dto.account.RoleSimpleDTO;
import kc.framework.enums.TenantType;
import kc.framework.enums.TenantVersion;
import kc.framework.extension.DateExtensions;
import kc.framework.tenant.RoleConstants;

/**
 * 三级级菜单：系统管理/菜单权限管理/权限管理
 */
@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping("/Permission")
@MenuAnnotation(ParentMenuName = "菜单权限管理", MenuName = "权限管理", Url = "/Permission/Index",
	Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType, 
	SmallIcon = "fa fa-file-code-o", AuthorityId = "8F11A258-26DA-4415-BE93-5D2DA0B6BBED", 
	DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsExtPage = false, Level = 3)
public class PermissionController extends WebBaseController {
	@Autowired
	private IPermissionService PermissionService;

	@Autowired
	private IRoleService RoleService;

	@PreAuthorize("hasAuthority('8F11A258-26DA-4415-BE93-5D2DA0B6BBED')")
	@PermissionAnnotation(MenuName = "权限管理", PermissionName = "权限管理", Url = "/Permission/Index", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = true, 
		ResultType = ResultType.ActionResult, AuthorityId = "8F11A258-26DA-4415-BE93-5D2DA0B6BBED")
	@GetMapping("/Index")
	public String Index(ModelMap model) {
		// model.addAttribute("resWebDomain", GlobalConfig.ResWebDomain);
		// 配置管理-保存配置属性
		model.addAttribute("canRoleInPermission", hasAuthority("3A3D7B20-7EFF-49DE-B7ED-71DF57C2C9FC"));
		// 下拉列表
		model.addAttribute("TenantTypes", TenantType.getList(null));
		model.addAttribute("Versions", TenantVersion.getList(null));
		return "Permission/index";
	}

	// @PreAuthorize("hasAuthority('A291F728-B9B3-448B-A793-B3DAFA1BA126')")
	// @PermissionAnnotation(MenuName = "权限管理", PermissionName = "加载权限列表数据",
	// Url="/Permission/LoadPermissionList",
	// DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = false,
	// ResultType = kc.enums.ResultType.JsonResult, AuthorityId =
	// "A291F728-B9B3-448B-A793-B3DAFA1BA126")
	@GetMapping("/LoadPermissionList")
	public @ResponseBody List<PermissionDTO> LoadPermissionList(String name) {
		List<PermissionDTO> result = PermissionService.GetRootPermissionsByName(name);
		return result;
	}

	@GetMapping("/LoadPermissionTree")
	public @ResponseBody List<PermissionDTO> LoadPermissionTree(Integer pid) {
		List<PermissionDTO> tree = PermissionService.GetRootPermissionsByName("");
		PermissionDTO rootPermission = new PermissionDTO();
		rootPermission.setText("顶级权限");
		rootPermission.getChildren().addAll(tree);// 给set填充

		List<PermissionDTO> result = new ArrayList<PermissionDTO>();
		result.add(rootPermission);
		return result;
	}

	@GetMapping("/GetPermissionForm")
	public String GetPermissionForm(int id, int parentId, ModelMap model) {
		model.addAttribute("TenantTypes", TenantType.getList(null));

		java.util.Map<Integer, String> versions = TenantVersion.getList(Arrays
				.asList(TenantVersion.Standard, TenantVersion.Professional, TenantVersion.Customized));
		model.addAttribute("Versions", versions);

		PermissionDTO vm = new PermissionDTO();
		vm.setParentId(parentId);
		if (id != 0) {
			vm = PermissionService.GetPermissionById(id);
			vm.setEditMode(true);
		}

		// final Integer vmVersion = vm.getVersion();
		// List<Integer> selectIds = versions.keySet().stream().filter(m -> (m &
		// vmVersion) != 0).collect(Collectors.toList());
		// model.addAttribute("SelectedVersionIds", "[" +
		// ListExtensions.toCommaSeparatedInt(selectIds) + "]");

		model.addAttribute("PermissionEntity", vm);

		return "Permission/_permissionForm";
	}

	@PreAuthorize("hasAuthority('58679DF4-30DB-42E3-A344-07D4169F932D')")
	@PermissionAnnotation(MenuName = "权限管理", PermissionName = "保存权限数据", Url = "/Permission/SavePermission",
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false,
			ResultType = ResultType.JsonResult, AuthorityId = "58679DF4-30DB-42E3-A344-07D4169F932D")
	@PostMapping("/SavePermission")
	public @ResponseBody ServiceResult<Boolean> SavePermission(PermissionDTO model) {
		return GetServiceResult(() -> {
			if (!model.isEditMode()) {
				model.setCreatedBy(CurrentUserId());
				model.setCreatedName(CurrentUserDisplayName());
				model.setCreatedDate(DateExtensions.getDateTimeUtcNow());
			}
			model.setModifiedBy(CurrentUserId());
			model.setModifiedName(CurrentUserDisplayName());
			model.setModifiedDate(DateExtensions.getDateTimeUtcNow());
			return PermissionService.SavePermission(model);
		}, log);
	}

	@PreAuthorize("hasAuthority('353C94F6-BC38-44F4-A599-2A56BE939D8E')")
	@PermissionAnnotation(MenuName = "权限管理", PermissionName = "删除权限数据", Url = "/Permission/RemovePermission", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false, 
		ResultType = ResultType.JsonResult, AuthorityId = "353C94F6-BC38-44F4-A599-2A56BE939D8E")
	@GetMapping("/RemovePermission")
	public @ResponseBody ServiceResult<Boolean> RemovePermission(int id) {
		return GetServiceResult(() -> {
			return PermissionService.RemovePermission(id);
		}, log);
	}

	/**
	 * 三级级菜单：系统管理/菜单权限管理/权限角色管理
	 */
	@PreAuthorize("hasAuthority('3A3D7B20-7EFF-49DE-B7ED-71DF57C2C9FC')")
	@MenuAnnotation(ParentMenuName = "菜单权限管理", MenuName = "权限角色管理", Url = "/Permission/RoleInPermission", 
		Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType, 
		SmallIcon = "fa fa-sitemap", AuthorityId = "3A3D7B20-7EFF-49DE-B7ED-71DF57C2C9FC", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsExtPage = true, Level = 3)
	@PermissionAnnotation(MenuName = "权限角色管理", PermissionName = "权限角色管理", Url = "/Permission/RoleInPermission", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
		ResultType = ResultType.ActionResult, AuthorityId = "3A3D7B20-7EFF-49DE-B7ED-71DF57C2C9FC")
	@GetMapping("/RoleInPermission")
	public String RoleInPermission(int id, ModelMap model) {
		// model.addAttribute("resWebDomain", GlobalConfig.ResWebDomain);
		model.addAttribute("id", id);
		// 配置管理-保存配置属性
		model.addAttribute("canEditRole", IsSystemAdmin());
		return "Permission/RoleInPermission";
	}

	@GetMapping("/GetRoleInPermission")
	public @ResponseBody List<RoleSimpleDTO> GetRoleInPermission(Integer id) {
		PermissionDTO menu = PermissionService.GetDetailPermissionById(id);
		List<RoleSimpleDTO> role = RoleService.GetAllSimpleRoles();
		if (menu == null)
			return role;

		List<RoleSimpleDTO> roleList = new ArrayList<RoleSimpleDTO>();

		if (menu.getRoles().size() > 0) {
			List<String> checkIds = menu.getRoles().stream().map(m -> m.getRoleId()).collect(Collectors.toList());
			roleList = GetCheckRoleList(checkIds, role);
		} else {
			roleList = role;
		}
		if (!IsSystemAdmin() && roleList != null) {
			List<RoleSimpleDTO> list = new ArrayList<RoleSimpleDTO>();
			// 查询当前登录用户具有哪些角色(不是系统管理员)
			List<String> rolesIdlist = CurrentUserRoleIds();

            List<RoleSimpleDTO> datalist = roleList.stream().filter(c -> rolesIdlist.contains(c.getRoleId())).collect(Collectors.toList());
            //查询出自己创建的角色
            List<RoleSimpleDTO> datalist2 = roleList.stream().filter(c -> c.getCreatedBy() == CurrentUserId()).collect(Collectors.toList());
            list.addAll(datalist);
            list.addAll(datalist2);
            return list;
        }

		return roleList;
	}

	protected List<RoleSimpleDTO> GetCheckRoleList(List<String> checkList, List<RoleSimpleDTO> rolelist) {
		for (RoleSimpleDTO dto : rolelist) {
			if (checkList.contains(dto.getRoleId())) {
				dto.setChecked(true);
			}
		}

		return rolelist;
	}

	@PreAuthorize("hasAuthority('1B152A1B-231A-4576-8C66-22F142284207')")
	@PermissionAnnotation(MenuName = "权限角色管理", PermissionName = "保存权限角色数据", Url = "/Permission/SubmitRoleInPermission", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false, 
		ResultType = ResultType.JsonResult, AuthorityId = "1B152A1B-231A-4576-8C66-22F142284207")
	@PostMapping("/SubmitRoleInPermission")
	public @ResponseBody ServiceResult<Boolean> SubmitRoleInPermission(
			@RequestParam(value = "addList", required = true) List<String> addList, int permissionId) {
		if (addList == null)
			addList = new ArrayList<String>();
		List<String> add = new ArrayList<String>();
		if (addList.size() > 0)
			add.addAll(addList.stream().distinct().collect(Collectors.toList()));

		return GetServiceResult(() -> {
			return PermissionService.UpdateRoleInPermission(permissionId, add, CurrentUserId(),
					CurrentUserDisplayName());
		}, log);
	}
}
