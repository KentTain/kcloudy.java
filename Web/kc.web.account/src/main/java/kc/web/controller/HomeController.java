package kc.web.controller;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;

import kc.dto.PaginatedBaseDTO;
import kc.dto.TreeNodeSimpleDTO;
import kc.dto.account.OrganizationSimpleDTO;
import kc.dto.message.NewsBulletinDTO;
import kc.dto.search.RootOrgWithUsersPaginatedSearchDTO;
import kc.dto.search.RootOrgWithUsersSearchDTO;
import kc.dto.search.WorkflowTaskSearchDTO;
import kc.dto.workflow.WorkflowProTaskDTO;
import kc.enums.NewsBulletinType;
import kc.framework.extension.StringExtensions;
import kc.service.base.echarts.Option;
import kc.web.models.SelectUsersViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import kc.dto.account.MenuNodeSimpleDTO;
import kc.dto.message.MemberRemindMessageDTO;
import kc.framework.GlobalConfig;
import kc.framework.tenant.ApplicationConstant;
import kc.framework.tenant.RoleConstants;
import kc.framework.tenant.TenantConstant;
import kc.service.base.ServiceResult;
import kc.web.annotation.MenuAnnotation;
import kc.web.base.WebBaseController;

import javax.servlet.http.HttpServletRequest;

@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping(value = "")
public class HomeController extends WebBaseController {

    @Autowired
    protected kc.service.webapiservice.IMessageApiService messageApiService;

    @Autowired
    protected kc.service.webapiservice.IWorkflowApiService workflowApiService;

    @Autowired
    protected kc.service.account.IUserService userService;

    @PreAuthorize("hasAuthority('" + ApplicationConstant.DefaultAuthorityId + "')")
//    @PermissionAnnotation(MenuName = "配置管理首页", PermissionName = "首页页面", Url = "Home/Index",
//            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
//            ResultType = ResultType.ActionResult, AuthorityId = ApplicationConstant.DefaultAuthorityId)
    @RequestMapping("")
    public String index(Locale locale, ModelMap model) {

        model.addAttribute("title", GlobalConfig.CurrentApplication.getAppName());
        model.addAttribute("tenantDisplayName", getTenantDisplayName());
        model.addAttribute("userId", CurrentUserId());
        model.addAttribute("userName", CurrentUserName());
        model.addAttribute("userDisplayName", CurrentUserDisplayName());
        model.addAttribute("userEmail", CurrentUserEmail());
        model.addAttribute("userPhone", CurrentUserPhone());
        model.addAttribute("canOrgManager", super.IsSystemAdmin());
        model.addAttribute("msgWebDomain", GlobalConfig.GetTenantWebDomain(GlobalConfig.MsgWebDomain, getTenantName()));
        model.addAttribute("workflowWebDomain", GlobalConfig.GetTenantWebDomain(GlobalConfig.WorkflowWebDomain, getTenantName()));
        model.addAttribute("accWebDomain", GlobalConfig.GetTenantWebDomain(GlobalConfig.AccWebDomain, getTenantName()));
        return "index";
    }

    /*------------------------------------------菜单设置-----------------------------------------------*/

    /**
     * 一级级菜单：系统管理
     */
    @MenuAnnotation(ParentMenuName = "系统管理", MenuName = "系统管理", Url = "/Home/Enterprise",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-cogs", AuthorityId = "8D4AB971-7EB6-42DC-B40E-6CF7590381F1",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 1)
    @RequestMapping("/Home/Enterprise")
    public String Enterprise() {
        return super.ThrowErrorJsonMessage(true, "系统管理-系统管理");
    }

    /**
     * 二级级菜单：系统管理/组织管理
     */
    @MenuAnnotation(ParentMenuName = "系统管理", MenuName = "组织管理", Url = "/Home/Organization",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-sitemap", AuthorityId = "6A773F5C-8BE0-4381-8036-1B6F948015CB",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsExtPage = false, Level = 2)
    @RequestMapping("/Home/Organization")
    public String Organization() {
        return super.ThrowErrorJsonMessage(true, "系统管理-组织管理");
    }

    /**
     * 二级级菜单：系统管理/用户管理
     */
    @MenuAnnotation(ParentMenuName = "系统管理", MenuName = "用户管理", Url = "/Home/User",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-user", AuthorityId = "495C2EB0-8772-4430-9952-5B528F41605F",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsExtPage = false, Level = 2)
    @RequestMapping("/Home/User")
    public String User() {
        return super.ThrowErrorJsonMessage(true, "系统管理-组织管理");
    }

    /**
     * 二级级菜单：系统管理/菜单权限管理
     */
    @MenuAnnotation(ParentMenuName = "系统管理", MenuName = "菜单权限管理", Url = "/Home/MenuPermission",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-bars", AuthorityId = "986CA9CE-7AB2-411D-83C5-CFB3C65D2FFB",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 4, IsExtPage = false, Level = 2)
    @RequestMapping("/Home/MenuPermission")
    public String MenuPermission() {
        return super.ThrowErrorJsonMessage(true, "系统管理-菜单权限管理");
    }

    /*------------------------------------------菜单数据-----------------------------------------------*/
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

    /*------------------------------------------主页数据-----------------------------------------------*/
    @GetMapping("/Home/LoadNewsBulletins")
    public @ResponseBody
    List<NewsBulletinDTO> LoadNewsBulletins() {
        if (!IsAuthenticated()) //IllegalAccessException
            return new ArrayList<>();

        return messageApiService.LoadLatestNewsBulletins(NewsBulletinType.Internal);
    }

    @GetMapping("/Home/LoadMessages")
    public @ResponseBody
    List<MemberRemindMessageDTO> LoadMessages() {
        if (!IsAuthenticated()) //IllegalAccessException
            return new ArrayList<MemberRemindMessageDTO>();

        return messageApiService.LoadTop10UserMessages(CurrentUserId(), null);
    }

    @GetMapping("/Home/ReadMessage")
    public @ResponseBody
    boolean ReadMessage(int id) {
        if (!IsAuthenticated()) //IllegalAccessException
            return false;

        return messageApiService.ReadRemindMessage(id);
    }

    @GetMapping("/Home/LoadWorkflowTasks")
    public @ResponseBody
    List<WorkflowProTaskDTO> LoadWorkflowTasks() {
        if (!IsAuthenticated()) //IllegalAccessException
            return new ArrayList<WorkflowProTaskDTO>();

        WorkflowTaskSearchDTO search = new WorkflowTaskSearchDTO();
        search.setUserId(CurrentUserId());
        search.setRoleIds(CurrentUserRoleIds());
        search.setOrgCodes(CurrentUserOrgCodes());
        return workflowApiService.LoadUserWorkflowTasks(search);
    }

    @GetMapping("/Home/GetChartData")
    public @ResponseBody
    Option GetChartData(Date startDate, Date endDate) {
        if (!IsAuthenticated()) //IllegalAccessException
            return null;

        return userService.GetUserLoginReportData(startDate, endDate);
    }

    /*------------------------------------------上传-----------------------------------------------*/
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

    @GetMapping("/Home/test")
    public @ResponseBody
    String test(String path) {
        if (StringExtensions.isNullOrEmpty(path))
            path = "json/test1/";
        StringBuilder sbResult = new StringBuilder();
        try {
            String path1 = ResourceUtils.getURL("classpath:" + path).getPath();
            sbResult.append(String.format("\r\nResourceUtils path1=%s ; \r\n files=", path1));
            File file1 = new File(path1);
            if (file1.isDirectory())
                sbResult.append(String.join(", ", file1.list()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            String path2 = this.getClass().getClassLoader().getResource(path).getPath();
            sbResult.append(String.format("\r\nClassUtils path2=%s ;\r\n files=", path2));
            File file2 = new File(path2);
            if (file2.isDirectory())
                sbResult.append(String.join(", ", file2.list()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            ClassLoader cl = this.getClass().getClassLoader();
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
            Resource[] resources = resolver.getResources("classpath:" + path + "*.json");
            sbResult.append(String.format("\r\nClassLoader path3=%s ; \r\n files=", path));
            Arrays.asList(resources)
                    .forEach(resource -> {
                        sbResult.append(readFile(resource));
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(sbResult);
        return sbResult.toString();
    }

    private String readFile(Resource resource) {
        StringBuilder sbResult = new StringBuilder(100);
        try{
            InputStream is = resource.getInputStream();
            BufferedReader  fileReader = new BufferedReader(new InputStreamReader(is));
            fileReader.lines().forEach(data -> sbResult.append(data));

            return sbResult.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String readFile(InputStream is) {
        StringBuilder sbResult = new StringBuilder(100);
        BufferedReader  fileReader = new BufferedReader(new InputStreamReader(is));
        fileReader.lines().forEach(data -> sbResult.append(data));

        return sbResult.toString();
    }
}
