package kc.web.controller;

import java.util.*;
import java.util.function.Predicate;

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
import kc.dto.message.MemberRemindMessageDTO;
import kc.enums.MessageStatus;
import kc.framework.GlobalConfig;
import kc.framework.tenant.ApplicationConstant;
import kc.framework.tenant.RoleConstants;
import kc.framework.tenant.TenantConstant;
import kc.web.annotation.MenuAnnotation;
import kc.web.base.WebBaseController;

import javax.servlet.http.HttpServletRequest;

@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping(value = "")
public class HomeController extends WebBaseController {
    @PreAuthorize("hasAuthority('" + ApplicationConstant.DefaultAuthorityId + "')")
//	@PermissionAnnotation(MenuName = "配置管理首页", PermissionName = "首页页面", Url = "Home/Index",
//			DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
//            ResultType = ResultType.ActionResult, AuthorityId = ApplicationConstant.DefaultAuthorityId)
    @RequestMapping("")
    public String index(Locale locale, ModelMap model) {
        model.addAttribute("title", GlobalConfig.CurrentApplication.getAppName());
        model.addAttribute("tenantDisplayName", CurrentUserTenantName());
        model.addAttribute("userName", CurrentUserName());
        model.addAttribute("userDisplayName", CurrentUserDisplayName());
        model.addAttribute("userEmail", CurrentUserEmail());
        model.addAttribute("userPhone", CurrentUserPhone());
        return "index";
    }

    /**
     * 一级菜单：应用管理
     */
    @MenuAnnotation(ParentMenuName = "应用管理", MenuName = "应用管理", Url = "/Home/Application",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-list-alt", AuthorityId = "87402527-9ED1-4AEC-8F42-CCF31E12A4AB",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 26, IsExtPage = false, Level = 1)
    @RequestMapping("/Home/Application")
    public String Application() {
        return super.ThrowErrorJsonMessage(true, "菜单-配置管理");
    }

    /**
     * 二级菜单：应用管理/应用管理
     */
    @MenuAnnotation(ParentMenuName = "应用管理", MenuName = "应用管理", Url = "/Home/AppManager",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-list-alt", AuthorityId = "CF0B3945-E2D7-4F51-ADB2-740DC85C29AA",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 2)
    @RequestMapping("/Home/AppManager")
    public String AppManager() {
        return super.ThrowErrorJsonMessage(true, "菜单-应用管理");
    }


    /*------------------------------------------主页数据-----------------------------------------------*/
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

    @GetMapping("/Home/LoadMenus")
    public @ResponseBody
    List<MenuNodeSimpleDTO> LoadMenus() {
        if (!IsAuthenticated()) //IllegalAccessException
            return new ArrayList<MenuNodeSimpleDTO>();

        Predicate<MenuNodeSimpleDTO> predicate = m -> true;
        return GetCurrentUserMenuTree(predicate);
    }

    @GetMapping("/Home/LoadMessages")
    public @ResponseBody
    List<MemberRemindMessageDTO> LoadMessages() {
        if (!IsAuthenticated()) //IllegalAccessException
            return new ArrayList<MemberRemindMessageDTO>();

        List<MemberRemindMessageDTO> messages = new ArrayList<MemberRemindMessageDTO>();
        messages.add(MemberRemindMessageDTO.builder()
                .id(1)
                .status(MessageStatus.Unread)
                .messageTitle("Validators")
                .messageContent("In order to provide the best possible experience to old and buggy browsers, Font Awesome uses CSS browser hacks in several places to target special CSS to certain browser versions in order to work around bugs in the browsers themselves. These hacks understandably cause CSS validators to complain that they are invalid. In a couple places, we also use bleeding-edge CSS features that aren't yet fully standardized, but these are used purely for progressive enhancement.<br/>" +
                        "These validation warnings don't matter in practice since the non-hacky portion of our CSS does fully validate and the hacky portions don't interfere with the proper functioning of the non - hacky portion,hence why we deliberately ignore these particular warnings.")
                .build()
        );
        messages.add(MemberRemindMessageDTO.builder()
                .id(2)
                .status(MessageStatus.Unread)
                .messageTitle("Internet Explorer 8 and @font-face")
                .messageContent("IE8 has some issues with @font-face when combined with :before. Font Awesome uses that combination. If a page is cached, and loaded without the mouse over the window (i.e. hit the refresh button or load something in an iframe) then the page gets rendered before the font loads. Hovering over the page (body) will show some of the icons and hovering over the remaining icons will show those as well. See issue #954 for details.")
                .build()
        );
        messages.add(MemberRemindMessageDTO.builder()
                .id(3)
                .status(MessageStatus.Unread)
                .messageTitle("Need IE7 Support")
                .messageContent("IE8 has some issues with @font-face when combined with :before. Font Awesome uses that combination. If a page is cached, and loaded without the mouse over the window (i.e. hit the refresh button or load something in an iframe) then the page gets rendered before the font loads. Hovering over the page (body) will show some of the icons and hovering over the remaining icons will show those as well. See issue #954 for details.")
                .build()
        );
        return messages;
    }

    @PostMapping("/Home/UploadFileToTemp")
    public @ResponseBody
    kc.web.models.UploadViewModel UploadFileToTemp(HttpServletRequest request) {

        return super.Upload(request, true);
    }

    @PostMapping("/Home/Upload")
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
    @GetMapping("/Home/GetRootOrganizationsWithUsers")
    public @ResponseBody
    List<OrganizationSimpleDTO> GetRootOrganizationsWithUsers() {
        List<OrganizationSimpleDTO> res = accountApiService.LoadOrgTreesWithUsers();
        return res;
    }

    /**
     * 获取所有的部门信息及下属员工
     *
     * @param roleIds
     * @return
     */
    @PostMapping("/Home/GetRootOrganizationsWithUsers")
    public @ResponseBody
    SelectUsersViewModel GetRootOrganizationsWithUsers(@RequestParam("roleIds[]") List<String> roleIds) {
        //var roleIdList = new List<string>();
        //if (!string.IsNullOrEmpty(roleIds))
        //{
        //    roleIdList = roleIds.Split(',').ToList();
        //}
        List<OrganizationSimpleDTO> res = accountApiService.LoadOrganizationsWithUsersByRoleIds(roleIds);
        Optional<OrganizationSimpleDTO> firstOrg = res.stream().findFirst();
        firstOrg.ifPresent(organizationSimpleDTO -> organizationSimpleDTO.setChecked(true));
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
        //return this.Json(new { deptInfos = res });
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
