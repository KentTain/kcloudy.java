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
import kc.service.account.IMenuNodeService;
import kc.service.account.IRoleService;
import kc.web.annotation.MenuAnnotation;
import kc.web.annotation.PermissionAnnotation;
import kc.web.base.WebBaseController;
import kc.framework.tenant.TenantConstant;
import kc.dto.account.MenuNodeDTO;
import kc.dto.account.RoleSimpleDTO;
import kc.framework.enums.TenantType;
import kc.framework.enums.TenantVersion;
import kc.framework.extension.DateExtensions;
import kc.framework.extension.ListExtensions;
import kc.framework.tenant.RoleConstants;

@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping("/Menu")
public class MenuController extends WebBaseController {
	@Autowired
	private IMenuNodeService MenuNodeService;
	
	@Autowired
	private IRoleService RoleService;

	/**
	 * 三级级菜单：系统管理/菜单权限管理/菜单管理
	 */
	@PreAuthorize("hasAuthority('08D986E5-CB98-4F2B-B08A-040B4C935224')")
	@MenuAnnotation(ParentMenuName = "菜单权限管理", MenuName = "菜单管理", Url = "/Menu/Index",
			Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
			SmallIcon = "fa fa-file-code-o", AuthorityId = "08D986E5-CB98-4F2B-B08A-040B4C935224",
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 3)
	@PermissionAnnotation(MenuName = "菜单管理", PermissionName = "菜单管理", Url = "/Menu/Index",
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = true, 
			ResultType = ResultType.ActionResult, AuthorityId = "08D986E5-CB98-4F2B-B08A-040B4C935224")
	@GetMapping("/Index")
	public String Index(ModelMap model) {
		//model.addAttribute("resWebDomain", GlobalConfig.ResWebDomain);
		// 配置管理-保存配置属性
		model.addAttribute("canRoleInMenu", hasAuthority("29FC3DD2-E8C7-4EA7-A5CC-AF7F57B106D9"));
		// 下拉列表
		model.addAttribute("TenantTypes", TenantType.getList(null));
		model.addAttribute("Versions", TenantVersion.getList(null));
		return "/Menu/index";
	}

	// @PreAuthorize("hasAuthority('A291F728-B9B3-448B-A793-B3DAFA1BA126')")
	// @PermissionAnnotation(MenuName = "菜单管理", PermissionName = "加载菜单列表数据",
	// 		Url="/Menu/LoadMenuList",
	// 		DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = false,
	// 		ResultType = kc.enums.ResultType.JsonResult, AuthorityId =
	// 		"A291F728-B9B3-448B-A793-B3DAFA1BA126")
	@GetMapping("/LoadMenuList")
	public @ResponseBody List<MenuNodeDTO> LoadMenuList(String name) {
		List<MenuNodeDTO> result = MenuNodeService.GetRootMenusByName(name);
		return result;
	}
	
	@GetMapping("/LoadMenuTree")
	public @ResponseBody List<MenuNodeDTO> LoadMenuTree(Integer pid)
    {
		List<MenuNodeDTO> tree = MenuNodeService.GetRootMenusByName("");
		MenuNodeDTO rootMenu = new MenuNodeDTO();
		rootMenu.setText("顶级菜单");
		rootMenu.getChildren().addAll(tree);//给set填充     
		
		List<MenuNodeDTO> result = new ArrayList<MenuNodeDTO>();
		result.add(rootMenu);
        return  result;
    }

	@GetMapping("/GetMenuForm")
	public String GetMenuForm(int id, int parentId, ModelMap model) {
		model.addAttribute("TenantTypes", TenantType.getList(null));
		
		java.util.Map<Integer, String> versions = TenantVersion.getList(Arrays.asList(TenantVersion.Professional, TenantVersion.Standard));
		model.addAttribute("Versions", versions);
		
		MenuNodeDTO vm = new MenuNodeDTO();
		vm.setParentId(parentId);
		if (id != 0) {
			vm = MenuNodeService.GetMenuNodeById(id);
			vm.setEditMode(true);
		}

		final Integer vmVersion = vm.getVersion();
		List<Integer> selectIds = versions.keySet().stream().filter(m -> (m & vmVersion) != 0).collect(Collectors.toList());
		model.addAttribute("SelectedVersionIds", "[" + ListExtensions.toCommaSeparatedInt(selectIds) + "]");
		
		model.addAttribute("MenuEntity", vm);

		return "Menu/_menuForm";
	}

	@PreAuthorize("hasAuthority('9DFC51A6-E5F9-480C-BB99-D619F9E5E690')")
	@PermissionAnnotation(MenuName = "菜单管理", PermissionName = "保存菜单数据", Url = "/Menu/SaveMenuNode",
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false, 
			ResultType = ResultType.JsonResult, AuthorityId = "9DFC51A6-E5F9-480C-BB99-D619F9E5E690")
	@PostMapping("/SaveMenuNode")
	public @ResponseBody ServiceResult<Boolean> SaveMenuNode(MenuNodeDTO model) {
		return GetServiceResult(() -> {
			if (!model.isEditMode()) {
				model.setCreatedBy(CurrentUserId());
				model.setCreatedName(CurrentUserDisplayName());
				model.setCreatedDate(DateExtensions.getDateTimeUtcNow());
			}
			model.setModifiedBy(CurrentUserId());
			model.setModifiedName(CurrentUserDisplayName());
			model.setModifiedDate(DateExtensions.getDateTimeUtcNow());
			return MenuNodeService.SaveMenuNode(model);
		}, log);
	}

	@PreAuthorize("hasAuthority('E3A5DD3D-A848-42DE-BE53-C46EE1D0D582')")
	@PermissionAnnotation(MenuName = "菜单管理", PermissionName = "删除菜单数据", Url = "/Menu/RemoveMenuNode",
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false, 
		ResultType = ResultType.JsonResult, AuthorityId = "E3A5DD3D-A848-42DE-BE53-C46EE1D0D582")
	@GetMapping("/RemoveMenuNode")
	public @ResponseBody ServiceResult<Boolean> RemoveMenuNode(int id) {
		return GetServiceResult(() -> {
			return MenuNodeService.RemoveMenuNode(id);
		}, log);
	}

	/**
	 * 三级级菜单：系统管理/菜单权限管理/菜单角色管理
	 */
	@PreAuthorize("hasAuthority('29FC3DD2-E8C7-4EA7-A5CC-AF7F57B106D9')")
	@MenuAnnotation(ParentMenuName = "菜单权限管理", MenuName = "菜单角色管理", Url = "/Menu/RoleInMenu",
			Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType, 
			SmallIcon = "fa fa-sitemap", AuthorityId = "29FC3DD2-E8C7-4EA7-A5CC-AF7F57B106D9", 
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsExtPage = true, Level = 3)
	@PermissionAnnotation(MenuName = "菜单角色管理", PermissionName = "菜单角色管理", Url = "/Menu/RoleInMenu",
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true, 
			ResultType = ResultType.ActionResult, AuthorityId = "29FC3DD2-E8C7-4EA7-A5CC-AF7F57B106D9")
	@GetMapping("/RoleInMenu")
	public String RoleInMenu(int id, ModelMap model) {
		//model.addAttribute("resWebDomain", GlobalConfig.ResWebDomain);
		model.addAttribute("id", id);
		// 配置管理-保存配置属性
		model.addAttribute("canEditRole", IsSystemAdmin());
		return "Menu/RoleInMenu";
	}
	
	@GetMapping("/GetRoleInMenu")
	public @ResponseBody List<RoleSimpleDTO> GetRoleInMenu(Integer id)
    {
		MenuNodeDTO menu = MenuNodeService.GetDetailMenuById(id);
		List<RoleSimpleDTO> role = RoleService.GetAllSimpleRoles();
		if (menu == null)
			return role;
		
		List<RoleSimpleDTO> roleList = new ArrayList<RoleSimpleDTO>();

        if (menu.getRoles().size() > 0)
        {
        	List<String> checkIds = menu.getRoles().stream().map(m -> m.getRoleId()).collect(Collectors.toList());
            roleList = GetCheckRoleList(checkIds, role);
        }
        else
        {
            roleList = role;
        }
        if (!IsSystemAdmin() && roleList != null)
        {
            List<RoleSimpleDTO> list = new ArrayList<RoleSimpleDTO>();
            //查询当前登录用户具有哪些角色(不是系统管理员)  
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
	
    protected List<RoleSimpleDTO> GetCheckRoleList(List<String> checkList, List<RoleSimpleDTO> rolelist)
    {
        for (RoleSimpleDTO dto : rolelist)
        {
            if (checkList.contains(dto.getRoleId()))
            {
                dto.setChecked(true);
            }
        }

        return rolelist;
    }
	
	@PreAuthorize("hasAuthority('6862116B-CA67-44DA-9E6A-D5C05DA7F964')")
	@PermissionAnnotation(MenuName = "菜单角色管理", PermissionName = "保存菜单角色数据", Url = "/Menu/SubmitRoleInMenu", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false, 
		ResultType = ResultType.JsonResult, AuthorityId = "6862116B-CA67-44DA-9E6A-D5C05DA7F964")
	@PostMapping("/SubmitRoleInMenu")
	public @ResponseBody ServiceResult<Boolean> SubmitRoleInMenu(
			@RequestParam(value="addList", required=true) List<String> addList, int menuId) {
		if (addList == null)
            addList = new ArrayList<String>();
		List<String> add = new ArrayList<String>();
        if (addList.size() > 0)
            add.addAll(addList.stream().distinct().collect(Collectors.toList()));

		return GetServiceResult(() -> {
			return MenuNodeService.UpdateRoleInMenu(menuId, add, CurrentUserId(),
                    CurrentUserDisplayName());
		}, log);
	}
}
