package kc.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kc.dto.account.*;
import kc.enums.ResultType;
import kc.service.account.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import kc.service.base.ServiceResult;
import kc.service.account.IMenuNodeService;
import kc.service.account.IPermissionService;
import kc.service.account.IUserService;
import kc.web.annotation.MenuAnnotation;
import kc.web.annotation.PermissionAnnotation;
import kc.web.base.WebBaseController;
import kc.framework.tenant.TenantConstant;
import kc.dto.PaginatedBaseDTO;
import kc.enums.account.PositionLevel;
import kc.framework.util.SerializeHelper;
import kc.framework.extension.DateExtensions;
import kc.framework.extension.StringExtensions;
import kc.framework.tenant.RoleConstants;

@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping("/User")
public class UserController extends WebBaseController {
    @Autowired
    private IUserService UserService;
    @Autowired
    private IRoleService RoleService;
    @Autowired
    private IMenuNodeService MenuNodeService;
    @Autowired
    private IPermissionService PermissionService;

    /**
     * 三级级菜单：系统管理/用户管理/用户管理
     */
    //@PreAuthorize("hasAuthority('730D5415-C702-4948-A209-A077AD20D4DA')")
    @MenuAnnotation(ParentMenuName = "用户管理", MenuName = "用户管理", Url = "/User/Index",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-file-code-o", AuthorityId = "730D5415-C702-4948-A209-A077AD20D4DA",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 3)
    @PermissionAnnotation(MenuName = "用户管理", PermissionName = "用户管理", Url = "/User/Index",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "730D5415-C702-4948-A209-A077AD20D4DA")
    @GetMapping("/Index")
    public String Index(ModelMap model) {
        // 用户管理-冻结/激活用户
        model.addAttribute("canDongJie", hasAuthority("B15DDB6E-6922-4E59-AC07-D8773BB42D0A"));
        // 用户管理-删除用户
        model.addAttribute("canDeleteUser", hasAuthority("E617F833-311B-4B4F-A04A-EC17214D6A1B"));
        // 用户管理-角色用户
        model.addAttribute("canRoleInUser", hasAuthority("993C40B4-0981-4294-BDBF-E19CE7C9B392"));
        // 用户管理-用户详情
        model.addAttribute("canUserDetail", hasAuthority("A92352D4-C288-4B53-8CDE-16DF6646B47F"));
        // 下拉列表
        model.addAttribute("PositionLevels", PositionLevel.getList(null));
        model.addAttribute("meUserId", CurrentUserId());
        model.addAttribute("adminUserId", RoleConstants.AdminUserId);

        return "User/index";
    }

    // @PreAuthorize("hasAuthority('9B47D623-2E38-4AF9-A3D4-B954D84D372D')")
    // @PermissionAnnotation(MenuName = "用户管理", PermissionName = "加载用户列表",
    // Url="/User/LoadUserList",
    // DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = false,
    // ResultType = kc.enums.ResultType.JsonResult, AuthorityId =
    // "9B47D623-2E38-4AF9-A3D4-B954D84D372D")
    @GetMapping("/LoadUserList")
    public @ResponseBody
    PaginatedBaseDTO<UserDTO> LoadUserList(int page, int rows,
                                           String email, String phone, String name, Integer status, Integer position, Integer orgId) {
        PaginatedBaseDTO<UserDTO> result = UserService.findPaginatedUsersByFilter(page, rows, email, phone, name, status, position, orgId, false);
        return result;
    }

    @GetMapping("/GetUserForm")
    public String GetUserForm(String userId, ModelMap model) {
        model.addAttribute("PositionLevels", PositionLevel.getList(null));

        UserDTO vm = new UserDTO();
        if (!StringExtensions.isNullOrEmpty(userId)) {
            vm = UserService.findById(userId);
            vm.setEditMode(true);
        }

        model.addAttribute("UserEntity", vm);

        return "User/_userForm";
    }

    @GetMapping("/ExistUserName")
    public @ResponseBody
    Boolean ExistUserName(String userName, String orginalUserName, boolean isEditMode) {
        if (isEditMode && userName.equalsIgnoreCase(orginalUserName)) {
            return true;
        }

        return UserService.ExistUserName(userName);
    }

    @GetMapping("/ExistUserEmail")
    public @ResponseBody
    Boolean ExistUserEmail(String email, String orginalEmail, boolean isEditMode) {
        if (isEditMode && email.equalsIgnoreCase(orginalEmail)) {
            return true;
        }

        return UserService.ExistUserEmail(email);
    }

    @GetMapping("/ExistUserPhone")
    public @ResponseBody
    Boolean ExistUserPhone(String phoneNumber, String orginalPhone, boolean isEditMode) {
        if (isEditMode && phoneNumber.equalsIgnoreCase(orginalPhone)) {
            return true;
        }

        return UserService.ExistUserPhone(phoneNumber);
    }

    @PreAuthorize("hasAuthority('55BD23BA-D892-41A0-A2D3-5E773D832E79')")
    @PermissionAnnotation(MenuName = "用户管理", PermissionName = "保存用户", Url = "/User/SaveUser",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "55BD23BA-D892-41A0-A2D3-5E773D832E79")
    @PostMapping("/SaveUser")
    public @ResponseBody
    ServiceResult<Boolean> SaveUser(UserDTO model) {
        return GetServiceResult(() -> {
            if (!model.isEditMode()) {
                model.setCreateDate(DateExtensions.getDateTimeUtcNow());
            }
            return UserService.SaveUser(model);
        }, log);
    }

    @PreAuthorize("hasAuthority('E617F833-311B-4B4F-A04A-EC17214D6A1B')")
    @PermissionAnnotation(MenuName = "用户管理", PermissionName = "删除用户", Url = "/User/RemoveUser",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "E617F833-311B-4B4F-A04A-EC17214D6A1B")
    @GetMapping("/RemoveUser")
    public @ResponseBody
    ServiceResult<Boolean> RemoveUser(int id) {
        return GetServiceResult(() -> {
            return UserService.RemoveUserById(id);
        }, log);
    }

    @PreAuthorize("hasAuthority('B15DDB6E-6922-4E59-AC07-D8773BB42D0A')")
    @PermissionAnnotation(MenuName = "用户管理", PermissionName = "冻结/激活用户", Url = "/User/FreezeOrActivation",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 4, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "B15DDB6E-6922-4E59-AC07-D8773BB42D0A")
    @GetMapping("/FreezeOrActivation")
    public @ResponseBody
    ServiceResult<Boolean> FreezeOrActivation(int id) {
        return GetServiceResult(() -> {
            return UserService.RemoveUserById(id);
        }, log);
    }

    /**
     * 三级级菜单：系统管理/用户管理/用户详情
     */
    @PreAuthorize("hasAuthority('A92352D4-C288-4B53-8CDE-16DF6646B47F')")
    @MenuAnnotation(ParentMenuName = "用户管理", MenuName = "用户详情", Url = "/User/UserDetail",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-file-code-o", AuthorityId = "A92352D4-C288-4B53-8CDE-16DF6646B47F",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsExtPage = true, Level = 3)
    @PermissionAnnotation(MenuName = "用户管理", PermissionName = "用户详情", Url = "/User/UserDetail",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 5, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "A92352D4-C288-4B53-8CDE-16DF6646B47F")
    @GetMapping("/UserDetail")
    public String UserDetail(String id, ModelMap model) {
        UserSimpleDTO data = UserService.findSimpleUserWithOrgAndRoleNameByUserId(id);
        List<String> roleIds = data.getUserRoleIds();
        model.addAttribute("roleIds", SerializeHelper.ToJson(roleIds));
        model.addAttribute("Model", data);

        return "User/UserDetail";
    }

    @PostMapping("/GetUserMenusByRoleIds")
    public @ResponseBody
    List<MenuNodeSimpleDTO> GetUserMenusByRoleIds(
            @RequestParam(value = "roleIds[]", required = true) List<String> roleIds) {
        List<MenuNodeSimpleDTO> menus = MenuNodeService.GetUserMenusByRoleIds(roleIds);
        List<MenuNodeSimpleDTO> menuTree = new ArrayList<MenuNodeSimpleDTO>();
        for (MenuNodeSimpleDTO parent : menus.stream().filter(m -> m.getParentId() == null).collect(Collectors.toList())) {
            GetSimpleTreeWithChild(parent, menus, null);
            menuTree.add(parent);
        }

        return menuTree;
    }

    @PostMapping("/GetUserPermissionsByRoleIds")
    public @ResponseBody
    List<PermissionSimpleDTO> GetUserPermissionsByRoleIds(
            @RequestParam(value = "roleIds[]", required = true) List<String> roleIds) {
        List<PermissionSimpleDTO> permissions = PermissionService.GetUserPermissionsByRoleIds(roleIds);
        List<PermissionSimpleDTO> PermissionTree = new ArrayList<PermissionSimpleDTO>();
        for (PermissionSimpleDTO parent : permissions.stream().filter(m -> m.getParentId() == null).collect(Collectors.toList())) {
            GetSimpleTreeWithChild(parent, permissions, null);
            PermissionTree.add(parent);
        }

        return PermissionTree;
    }

    @PreAuthorize("hasAuthority('15E50D59-D883-4D31-9382-F7517F0A16B9')")
    @PermissionAnnotation(MenuName = "用户管理", PermissionName = "下载用户Excel模板", Url = "/User/DownLoadExcelTemplate",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 5, IsPage = false,
            ResultType = ResultType.ImageResult, AuthorityId = "15E50D59-D883-4D31-9382-F7517F0A16B9")
    @GetMapping("/DownLoadExcelTemplate")
    public ResponseEntity<InputStreamResource> DownLoadExcelTemplate()
            throws IOException {
        String filePath = "/excels/UserTemplate.xlsx";
        FileSystemResource file = new FileSystemResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }

    /**
     * 三级级菜单：系统管理/用户管理/用户日志
     */
    @PreAuthorize("hasAuthority('A92352D4-C288-4B53-8CDE-16DF6646B47F')")
    @MenuAnnotation(ParentMenuName = "用户管理", MenuName = "用户日志", Url = "/User/UserTracingLogList",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-file-code-o", AuthorityId = "A92352D4-C288-4B53-8CDE-16DF6646B47F",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsExtPage = true, Level = 3)
    @PermissionAnnotation(MenuName = "用户管理", PermissionName = "用户日志", Url = "/User/UserTracingLogList",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "A92352D4-C288-4B53-8CDE-16DF6646B47F")
    @GetMapping("/UserTracingLogList")
    public String UserTracingLogList(ModelMap model) {
        return "User/UserTracingLogList";
    }

    @GetMapping("/LoadUserTracingLogList")
    public @ResponseBody
    PaginatedBaseDTO<UserTracingLogDTO> LoadUserTracingLogList(int page, int rows, String name) {
        PaginatedBaseDTO<UserTracingLogDTO> result = UserService.findPaginatedUserLogsByName(page, rows, name);
        return result;
    }

    /**
     * 三级级菜单：系统管理/用户管理/用户角色管理
     */
    @PreAuthorize("hasAuthority('993C40B4-0981-4294-BDBF-E19CE7C9B392')")
    @MenuAnnotation(ParentMenuName = "用户管理", MenuName = "用户角色管理", Url = "/User/RoleInUser",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-file-code-o", AuthorityId = "993C40B4-0981-4294-BDBF-E19CE7C9B392",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 4, IsExtPage = true, Level = 3)
    @PermissionAnnotation(MenuName = "用户管理", PermissionName = "用户角色管理", Url = "/User/RoleInUser",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "993C40B4-0981-4294-BDBF-E19CE7C9B392")
    @GetMapping("/RoleInUser")
    public String RoleInUser(String id, ModelMap model) {
//        UserSimpleDTO data = UserService.findSimpleUserWithOrgAndRoleNameByUserId(id);
//        List<String> roleIds = data.getUserRoleIds();
//        model.addAttribute("Model", data);
        model.addAttribute("userId", id);
        return "User/RoleInUser";
    }

    @GetMapping("/GetRoleInUserUserList")
    public @ResponseBody
    List<UserDTO> GetRoleInUserUserList(String userId) {
        UserDTO user = UserService.findById(userId);
        List<UserDTO> result = new ArrayList<UserDTO>();
        result.add(user);
        return result;
    }

    @GetMapping("/GetRoleInUserRoleList")
    public @ResponseBody
    List<RoleSimpleDTO> GetRoleInUserRoleList(int page, int rows, String userId) {
        UserSimpleDTO user = UserService.findSimpleUserWithOrgAndRoleNameByUserId(userId);

        List<RoleDTO> checkRole = new ArrayList<RoleDTO>();
        if (user.getUserRoleIds().size() > 0) {
            checkRole = RoleService.GetRolesByIds(user.getUserRoleIds());
        }
        List<RoleSimpleDTO> roleList = RoleService.GetAllSimpleRoles();

        List<RoleSimpleDTO> roleData = new ArrayList<RoleSimpleDTO>();
        for (RoleSimpleDTO roleDto : roleList) {
            for (RoleDTO dto : checkRole) {
                if (roleDto.getRoleId().equals(dto.getRoleId())) {
                    roleDto.setChecked(true);
                }
            }
            roleData.add(roleDto);
        }
        if (!IsSystemAdmin()) {
            List<RoleSimpleDTO> list = new ArrayList<RoleSimpleDTO>();
            //查询当前登录用户具有哪些角色(不是系统管理员)
            List<String> rolesIds = CurrentUserRoleIds();

            List<RoleSimpleDTO> datalist = roleData.stream().filter(c -> rolesIds.contains(c.getRoleId())).collect(Collectors.toList());
            //查询出自己创建的角色
            List<RoleSimpleDTO> datalist2 = roleData.stream().filter(c -> c.getCreatedBy().equals(CurrentUserId())).collect(Collectors.toList());
            list.addAll(datalist);
            list.addAll(datalist2);
            roleData = list;
        }
        return roleData;
    }


    @PreAuthorize("hasAuthority('E95A9A8A-3368-4BCA-9D24-05369B1C7D15')")
    @PermissionAnnotation(MenuName = "用户角色管理", PermissionName = "保存用户角色数据", Url = "/User/SubmitRoleInUser",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "E95A9A8A-3368-4BCA-9D24-05369B1C7D15")
    @PostMapping("/SubmitRoleInUser")
    public @ResponseBody
    ServiceResult<Boolean> SubmitRoleInUser(
            @RequestParam("userId") String userId, @RequestParam("addList[]") List<String> addList) {
        return GetServiceResult(() -> {
            if (userId.contains(RoleConstants.AdminUserId) && (addList.size() == 0)) {
                throw new IllegalArgumentException("管理员用户必须选中管理员角色");
            }

            List<String> add = new ArrayList<String>();
            if (addList.size() > 0)
                add.addAll(addList.stream().distinct().collect(Collectors.toList()));

            if (userId.contains(RoleConstants.AdminUserId)) {
                //if(!(addList.Count <= 1 && item.ToString().Contains("管理员")))
                //{
                //    throw new IllegalArgumentException("系统管理员用户不能分配给其他角色");
                //}
                if (!addList.contains(RoleConstants.AdminRoleId)) {
                    throw new IllegalArgumentException("管理员用户必须选中管理员角色");
                }
            }
            List<String> dbIdList = new ArrayList<String>();
            boolean result = false;
            //var appId = CurrentOperationApplicationId;
            if (StringExtensions.isNullOrEmpty(userId))
                throw new IllegalArgumentException("选中的用户Id为空");

            //用户分配角色
            result = UserService.UpdateRoleInUser(userId, add, CurrentUserId(), CurrentUserDisplayName());
            //分配角色成功，清除用户缓存，以便重新获取角色权限

            return result;
        }, log);
    }
}
