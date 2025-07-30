package kc.web.controller;

import java.util.*;
import java.util.function.Predicate;
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
import kc.service.account.IMenuNodeService;
import kc.service.account.IPermissionService;
import kc.service.account.IRoleService;
import kc.service.account.IUserService;
import kc.web.annotation.MenuAnnotation;
import kc.web.annotation.PermissionAnnotation;
import kc.web.base.WebBaseController;
import kc.web.models.RoleData;
import kc.web.models.RoleData.Dictionarys;
import kc.framework.tenant.TenantConstant;
import kc.dto.PaginatedBaseDTO;
import kc.dto.TreeNodeSimpleDTO;
import kc.dto.account.MenuNodeSimpleDTO;
import kc.dto.account.PermissionDTO;
import kc.dto.account.PermissionSimpleDTO;
import kc.dto.account.RoleDTO;
import kc.dto.account.RoleSimpleDTO;
import kc.dto.account.UserDTO;
import kc.dto.account.UserSimpleDTO;
import kc.framework.enums.BusinessType;
import kc.framework.extension.DateExtensions;
import kc.framework.extension.StringExtensions;
import kc.framework.tenant.RoleConstants;

@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping("/Role")
public class RoleController extends WebBaseController {
	@Autowired
	private IRoleService RoleService;
	@Autowired
	private IMenuNodeService MenuNodeService;
	@Autowired
	private IPermissionService PermissionService;
	@Autowired
	private IUserService UserService;

	/**
	 * 三级级菜单：系统管理/组织管理/角色管理
	 */
	@PreAuthorize("hasAuthority('EF1E670D-BA19-4536-82F4-7D78DB779C3B')")
	@MenuAnnotation(ParentMenuName = "组织管理", MenuName = "角色管理", Url = "/Role/Index", 
		Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType, 
		SmallIcon = "fa fa-file-code-o", AuthorityId = "EF1E670D-BA19-4536-82F4-7D78DB779C3B", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsExtPage = false, Level = 3)
	@PermissionAnnotation(MenuName = "角色管理", PermissionName = "角色管理", Url = "/Role/Index", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = true, 
		ResultType = ResultType.ActionResult, AuthorityId = "EF1E670D-BA19-4536-82F4-7D78DB779C3B")
	@GetMapping("/Index")
	public String Index(ModelMap model) {
		// 角色菜单管理-角色菜单管理
		model.addAttribute("canMenuInRole", hasAuthority("C9385B3E-4FCA-46E7-ABFA-5EE6042F8787"));
		// 角色权限管理-角色权限管理
		model.addAttribute("canPermissionInRole", hasAuthority("0DE8B69B-75BD-42AC-B0F1-C957F52FF021"));
		// 角色用户管理-角色用户管理
		model.addAttribute("canUserInRole", hasAuthority("F448F3D1-47D9-4066-9A42-D405F311C309"));
		// 角色用户管理-角色详情
		model.addAttribute("canRoleDetail", hasAuthority("0C53ECAA-D62D-4EAD-9419-A6F904857E85"));
		// 下拉列表
		model.addAttribute("BusinessTypes", BusinessType.getList(null));
		model.addAttribute("meRoleId", CurrentUserId());
		model.addAttribute("adminRoleId", RoleConstants.AdminRoleId);

		return "Role/index";
	}

	@PostMapping("/LoadRoleList")
	public @ResponseBody PaginatedBaseDTO<RoleDTO> LoadRoleList(int page, int rows, String name) {
		PaginatedBaseDTO<RoleDTO> result = RoleService.GetPagenatedRoleList(page, rows, name);
		return result;
	}

	@GetMapping("/GetRoleForm")
	public String GetRoleForm(String id, ModelMap model) {
		model.addAttribute("BusinessTypes", BusinessType.getList(null));

		RoleDTO vm = new RoleDTO();
		if (!StringExtensions.isNullOrEmpty(id)) {
			vm = RoleService.findById(id);
			vm.setEditMode(true);
		}

		model.addAttribute("RoleEntity", vm);

		return "Role/_roleForm";
	}

	@PreAuthorize("hasAuthority('D1ADCD2E-DDAE-420E-BFC8-E4EE08EAFDB5')")
	@PermissionAnnotation(MenuName = "角色管理", PermissionName = "保存角色数据", Url = "/Role/EditRole", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false, 
		ResultType = ResultType.JsonResult, AuthorityId = "D1ADCD2E-DDAE-420E-BFC8-E4EE08EAFDB5")
	@PostMapping("/EditRole")
	public @ResponseBody ServiceResult<Boolean> EditRole(RoleDTO model) {
		return GetServiceResult(() -> {
			if (!model.isEditMode()) {
				model.setCreatedBy(CurrentUserId());
				model.setCreatedName(CurrentUserDisplayName());
				model.setCreatedDate(DateExtensions.getDateTimeUtcNow());
			}
			model.setModifiedBy(CurrentUserId());
			model.setModifiedName(CurrentUserDisplayName());
			model.setModifiedDate(DateExtensions.getDateTimeUtcNow());
			return RoleService.SaveRole(model);
		}, log);
	}

	@PreAuthorize("hasAuthority('6759EA9B-4F19-4A17-ACFF-0FED6CFFEF12')")
	@PermissionAnnotation(MenuName = "角色管理", PermissionName = "删除角色数据", Url = "/Role/RemoveRole", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false, 
		ResultType = ResultType.JsonResult, AuthorityId = "6759EA9B-4F19-4A17-ACFF-0FED6CFFEF12")
	@GetMapping("/RemoveRole")
	public @ResponseBody ServiceResult<Boolean> RemoveRole(String id) {
		return GetServiceResult(() -> {
			return RoleService.RemoveRoleById(id);
		}, log);
	}

	/**
	 * 三级级菜单：系统管理/组织管理/角色详情
	 */
	@PreAuthorize("hasAuthority('0C53ECAA-D62D-4EAD-9419-A6F904857E85')")
	@MenuAnnotation(ParentMenuName = "组织管理", MenuName = "角色详情", Url = "/Role/RoleDetail",
		Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType, 
		SmallIcon = "fa fa-file-code-o", AuthorityId = "0C53ECAA-D62D-4EAD-9419-A6F904857E85", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsExtPage = true, Level = 3)
	@PermissionAnnotation(MenuName = "角色管理", PermissionName = "角色详情", Url = "/Role/RoleDetail", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 4, IsPage = true, 
		ResultType = ResultType.ActionResult, AuthorityId = "0C53ECAA-D62D-4EAD-9419-A6F904857E85")
	@GetMapping("/RoleDetail")
	public String RoleDetail(String roleId, boolean isSystemRole, ModelMap model) {
		RoleDTO data = RoleService.findById(roleId);

		model.addAttribute("roleId", roleId);
		model.addAttribute("isSystemRole", isSystemRole);
		model.addAttribute("canEditRole", IsSystemAdmin());

		model.addAttribute("Model", data);

		return "Role/RoleDetail";
	}

	@PostMapping("/GetUsersByRoleId")
	public @ResponseBody List<UserSimpleDTO> GetUsersByRoleId(
			@RequestParam(value = "roleId", required = true) String roleId) {
		List<UserSimpleDTO> users = UserService.GetUserUsersByRoleIds(Arrays.asList(roleId));
		return users;
	}

	@PostMapping("/GetMenusByRoleId")
	public @ResponseBody List<MenuNodeSimpleDTO> GetMenusByRoleId(
			@RequestParam(value = "roleId", required = true) String roleId) {
		List<MenuNodeSimpleDTO> menus = MenuNodeService.GetUserMenusByRoleIds(Arrays.asList(roleId));
		List<MenuNodeSimpleDTO> menuTree = new ArrayList<MenuNodeSimpleDTO>();
		for (MenuNodeSimpleDTO parent : menus.stream().filter(m -> m.getParentId() == null)
				.collect(Collectors.toList())) {
			GetSimpleTreeWithChild(parent, menus, null);
			menuTree.add(parent);
		}

		return menuTree;
	}

	@GetMapping("/GetRoleData")
	public @ResponseBody RoleData GetRoleData(String roleId) {
		RoleData model = new RoleData();
		model.RoleId = UUID.fromString(roleId);

		List<RoleSimpleDTO> role = RoleService.GetAllSimpleRoles();
		List<RoleSimpleDTO> data = new ArrayList<RoleSimpleDTO>();
		if (!IsSystemAdmin() && role != null) {
			List<RoleSimpleDTO> roleDate = new ArrayList<RoleSimpleDTO>();
			// 查询当前登录用户具有哪些角色(不是系统管理员)
			List<String> rolesIdlist = CurrentUserRoleIds();
			List<RoleSimpleDTO> datalist = role.stream().filter(c -> rolesIdlist.contains(c.getRoleId()))
					.collect(Collectors.toList());
			roleDate.addAll(datalist);
			// 查询出自己创建的角色
			List<RoleSimpleDTO> datalist2 = role.stream().filter(c -> c.getCreatedBy() == CurrentUserId())
					.collect(Collectors.toList());
			roleDate.addAll(datalist2);
			data = roleDate;
		} else {
			data = role;
		}
		model.Permissions = new ArrayList<PermissionDTO>();
		model.RoleList = new ArrayList<RoleData.Dictionarys>();
		if (data != null)
			for (RoleSimpleDTO itme : data) {
				// 角色列表
				RoleData.Dictionarys roleitme = model.new Dictionarys();
				if (itme.getRoleId() == roleId) {
					roleitme.checked = true;
				}
				roleitme.Key = itme.getDisplayName();
				roleitme.Value = itme.getRoleId();
				model.RoleList.add(roleitme);
			}
		return model;
	}

	/**
	 * 三级级菜单：系统管理/组织管理/角色菜单管理
	 */
	@PreAuthorize("hasAuthority('C9385B3E-4FCA-46E7-ABFA-5EE6042F8787')")
	@MenuAnnotation(ParentMenuName = "组织管理", MenuName = "角色菜单管理", Url = "/Role/MenuInRole", 
		Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType, 
		SmallIcon = "fa fa-file-code-o", AuthorityId = "C9385B3E-4FCA-46E7-ABFA-5EE6042F8787", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 4, IsExtPage = true, Level = 3)
	@PermissionAnnotation(MenuName = "角色菜单管理", PermissionName = "角色菜单管理", Url = "/Role/MenuInRole", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = true, 
		ResultType = ResultType.ActionResult, AuthorityId = "C9385B3E-4FCA-46E7-ABFA-5EE6042F8787")
	@GetMapping("/MenuInRole")
	public String MenuInRole(String roleId, boolean isSystemRole, ModelMap model) {
		model.addAttribute("roleId", roleId);
		model.addAttribute("isSystemRole", isSystemRole);
		model.addAttribute("canEditRole", IsSystemAdmin());

		return "Role/MenuInRole";
	}

	@GetMapping("/GetMenuInRole")
	public @ResponseBody List<MenuNodeSimpleDTO> GetMenuInRole(String rid) {
		List<MenuNodeSimpleDTO> data;
		if (IsSystemAdmin()) {
			data = MenuNodeService.GetRootMenuTrees();
		} else {
			Predicate<MenuNodeSimpleDTO> predict = m -> true;
			data = GetCurrentUserMenuTree(predict);
		}

		if (StringExtensions.isNullOrEmpty(rid)) {
			return data;
		}

		List<MenuNodeSimpleDTO> selectedMenus = new ArrayList<MenuNodeSimpleDTO>();
		List<MenuNodeSimpleDTO> menus = MenuNodeService.GetUserMenusByRoleIds(Collections.singletonList(rid));
		if (menus != null && menus.size() > 0) {
			List<Integer> checkIds = menus.stream().map(TreeNodeSimpleDTO::getId).collect(Collectors.toList());
			List<MenuNodeSimpleDTO> checkItems = GetTreeNodeSimpleWithChildren(checkIds,
					new ArrayList<MenuNodeSimpleDTO>(data));
			selectedMenus.addAll(checkItems);
		} else {
			selectedMenus = data;
		}

		return selectedMenus;
	}

	@PreAuthorize("hasAuthority('49AB8484-621B-4518-9049-191ADF948A2C')")
	@PermissionAnnotation(MenuName = "角色菜单管理", PermissionName = "保存角色菜单数据", Url = "/Role/SubmitMenuInRole", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false, 
		ResultType = ResultType.JsonResult, AuthorityId = "49AB8484-621B-4518-9049-191ADF948A2C")
	@PostMapping("/SubmitMenuInRole")
	public @ResponseBody ServiceResult<Boolean> SubmitMenuInRole(
			@RequestParam(value = "addList[]", required = true) List<Integer> addList, String roleId) {
		if (addList == null)
			addList = new ArrayList<Integer>();
		List<Integer> add = new ArrayList<Integer>();
		if (addList.size() > 0)
			add.addAll(addList.stream().distinct().collect(Collectors.toList()));

		return GetServiceResult(() -> {
			if (StringExtensions.isNullOrEmpty(roleId))
				throw new IllegalArgumentException("角色Id为空。");

			return RoleService.UpdateMenuInRole(roleId, add, CurrentUserId(), CurrentUserDisplayName());
		}, log);
	}

	/**
	 * 三级级菜单：系统管理/组织管理/角色权限管理
	 */
	@PreAuthorize("hasAuthority('0DE8B69B-75BD-42AC-B0F1-C957F52FF021')")
	@MenuAnnotation(ParentMenuName = "组织管理", MenuName = "角色权限管理", Url = "/Role/PermissionInRole", 
		Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType, 
		SmallIcon = "fa fa-file-code-o", AuthorityId = "0DE8B69B-75BD-42AC-B0F1-C957F52FF021", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 5, IsExtPage = true, Level = 3)
	@PermissionAnnotation(MenuName = "角色权限管理", PermissionName = "角色权限管理", Url = "/Role/PermissionInRole", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = true, 
		ResultType = ResultType.ActionResult, AuthorityId = "0DE8B69B-75BD-42AC-B0F1-C957F52FF021")
	@GetMapping("/PermissionInRole")
	public String PermissionInRole(String roleId, boolean isSystemRole, ModelMap model) {
		model.addAttribute("roleId", roleId);
		model.addAttribute("isSystemRole", isSystemRole);
		model.addAttribute("canEditRole", IsSystemAdmin());

		return "Role/PermissionInRole";
	}

	@GetMapping("/GetPermissionInRole")
	public @ResponseBody List<PermissionSimpleDTO> GetPermissionInRole(String rid) {
		List<PermissionSimpleDTO> data;
		if (IsSystemAdmin()) {
			data = PermissionService.GetRootPermissionTrees();
		} else {
			Predicate<PermissionSimpleDTO> predict = m -> true;
			data = GetCurrentPermissionTree(predict);
		}

		if (StringExtensions.isNullOrEmpty(rid)) {
			return data;
		}

		List<PermissionSimpleDTO> selectedMenus = new ArrayList<PermissionSimpleDTO>();
		List<PermissionSimpleDTO> menus = PermissionService.GetUserPermissionsByRoleIds(Collections.singletonList(rid));
		if (menus != null && menus.size() > 0) {
			List<Integer> checkIds = menus.stream().map(TreeNodeSimpleDTO::getId).collect(Collectors.toList());
			List<PermissionSimpleDTO> checkItems = GetTreeNodeSimpleWithChildren(checkIds,
					new ArrayList<>(data));
			selectedMenus.addAll(checkItems);
		} else {
			selectedMenus = data;
		}

		return selectedMenus;
	}

	@PreAuthorize("hasAuthority('B457B18B-B680-4117-8E72-98284E9151D5')")
	@PermissionAnnotation(MenuName = "角色权限管理", PermissionName = "保存角色权限数据", Url = "/Role/SubmitPermissionInRole", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 4, IsPage = false, 
		ResultType = ResultType.JsonResult, AuthorityId = "B457B18B-B680-4117-8E72-98284E9151D5")
	@PostMapping("/SubmitPermissionInRole")
	public @ResponseBody ServiceResult<Boolean> SubmitPermissionInRole(
			@RequestParam(value = "addList[]", required = true) List<Integer> addList, String roleId) {
		if (addList == null)
			addList = new ArrayList<Integer>();
		List<Integer> add = new ArrayList<Integer>();
		if (addList.size() > 0)
			add.addAll(addList.stream().distinct().collect(Collectors.toList()));

		return GetServiceResult(() -> {
			if (StringExtensions.isNullOrEmpty(roleId))
				throw new IllegalArgumentException("角色Id为空。");

			return RoleService.UpdatePermissionInRole(roleId, add, CurrentUserId(), CurrentUserDisplayName());
		}, log);
	}

	/**
	 * 三级级菜单：系统管理/组织管理/角色用户管理
	 */
	@PreAuthorize("hasAuthority('F448F3D1-47D9-4066-9A42-D405F311C309')")
	@MenuAnnotation(ParentMenuName = "组织管理", MenuName = "角色用户管理", Url = "/Role/UserInRole", 
		Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType, 
		SmallIcon = "fa fa-file-code-o", AuthorityId = "F448F3D1-47D9-4066-9A42-D405F311C309",
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 6, IsExtPage = true, Level = 3)
	@PermissionAnnotation(MenuName = "角色用户管理", PermissionName = "角色用户管理", Url = "/Role/UserInRole", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 4, IsPage = true, 
		ResultType = ResultType.ActionResult, AuthorityId = "F448F3D1-47D9-4066-9A42-D405F311C309")
	@GetMapping("/UserInRole")
	public String UserInRole(String roleId, boolean isSystemRole, ModelMap model) {
		model.addAttribute("roleId", roleId);
		model.addAttribute("isSystemRole", isSystemRole);
		model.addAttribute("adminUserId", RoleConstants.AdminUserId);
		model.addAttribute("systemSsoRoleId", RoleConstants.AdminRoleId);
		model.addAttribute("canSubmitUserInRole", hasAuthority("D03C4451-C709-4EA9-94EF-215845FE744C"));

		return "Role/UserInRole";
	}

	@GetMapping("/LoadUserLeftInRoseList")
	public @ResponseBody PaginatedBaseDTO<UserDTO> LoadUserLeftInRoseList(int page, int rows, String email,
			String phone, String name, String roleId) {
		PaginatedBaseDTO<UserDTO> result = UserService.findPaginatedUsersByFilter(page, rows, email, phone, name, null,
				null, null,false);
		List<UserDTO> resultData = new ArrayList<UserDTO>();
		// 排除已经选择该角色了的用户
		for (UserDTO c : result.getRows()) {
			if (!c.getRoleIds().contains(roleId)) {
				resultData.add(c);
			}
		}

		result.setRows(resultData);

		return result;
	}

	@GetMapping("/GetUserInRole")
	public @ResponseBody RoleData GetUserInRole(String roleId) {
		List<RoleDTO> roleList = RoleService.GetAllDetailRoles();
		List<RoleDTO> data = new ArrayList<RoleDTO>();
		if (!IsSystemAdmin() && roleList != null) {
			List<RoleDTO> roleData = new ArrayList<RoleDTO>();
			// 查询当前登录用户具有哪些角色(不是系统管理员)
			List<String> rolesIdlist = CurrentUserRoleIds();
			List<RoleDTO> datalist = roleList.stream().filter(c -> rolesIdlist.contains(c.getRoleId()))
					.collect(Collectors.toList());
			roleData.addAll(datalist);
			// 查询出自己创建的角色
			List<RoleDTO> datalist2 = roleList.stream().filter(c -> c.getCreatedBy() == CurrentUserId())
					.collect(Collectors.toList());
			roleData.addAll(datalist2);
			data = roleData;
		} else {
			data = roleList;
		}

		List<UserDTO> userlist = UserService.GetAllUsersWithRolesAndOrgs();
		RoleData result = new RoleData();

		for (UserDTO userDto : userlist) {
			if (userDto.getRoleIds().contains(roleId)) {
				result.Users.add(userDto);
			}
		}
		if (data != null)
			for (RoleDTO roledto : data) {
				Dictionarys role = result.new Dictionarys();
				{
					role.Key = roledto.getDisplayName();
					role.Value = roledto.getRoleId();
					role.Itms = roledto.getUserIds();
				}
				;
				result.RoleList.add(role);
			}

		return result;
	}

	@PreAuthorize("hasAuthority('D03C4451-C709-4EA9-94EF-215845FE744C')")
	@PermissionAnnotation(MenuName = "角色用户管理", PermissionName = "保存角色用户数据", Url = "/Role/SubmitUserInRole", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 4, IsPage = false, 
		ResultType = ResultType.JsonResult, AuthorityId = "D03C4451-C709-4EA9-94EF-215845FE744C")
	@PostMapping("/SubmitUserInRole")
	public @ResponseBody ServiceResult<Boolean> SubmitUserInRole(int type, String roleId,
			@RequestParam(value = "userIdList[]") List<String> userIdList) {

		List<String> add = new ArrayList<String>();
		if (type == 0)
			add.addAll(userIdList.stream().distinct().collect(Collectors.toList()));
		List<String> del = new ArrayList<String>();
		if (type == 1)
			del.addAll(userIdList.stream().distinct().collect(Collectors.toList()));

		return GetServiceResult(() -> {
			if (StringExtensions.isNullOrEmpty(roleId))
				throw new IllegalArgumentException("角色Id为空。");

			return RoleService.UpdateUserInRole(roleId, add, del, CurrentUserId(), CurrentUserDisplayName());
		}, log);
	}

	/**
	 * 树形选中
	 * 
	 * @param           <T>
	 * @param checkList 选中的节点
	 * @param scoreList 所有节点
	 * @return
	 */
	protected <T extends TreeNodeSimpleDTO<T>> List<T> GetTreeNodeSimpleWithChildren(List<Integer> checkList,
			List<T> scoreList) {
		for (T dto : scoreList) {
			if (dto.getChildren().size() > 0) {
				if (checkList.contains(dto.getId()))
					dto.setChecked(true);
				GetTreeNodeSimpleWithChildren(checkList, dto.getChildren());
			} else {
				if (checkList.contains(dto.getId())) {
					dto.setChecked(true);
				}
			}
		}
		return scoreList;
	}
}
