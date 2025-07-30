package kc.web.controller;

import kc.dto.PaginatedBaseDTO;
import kc.dto.portal.*;
import kc.enums.ResultType;
import kc.enums.portal.*;
import kc.framework.enums.BusinessType;
import kc.framework.extension.DateExtensions;
import kc.framework.tenant.RoleConstants;
import kc.framework.tenant.TenantConstant;
import kc.service.base.ServiceResult;
import kc.service.portal.IArticleService;
import kc.web.annotation.MenuAnnotation;
import kc.web.annotation.PermissionAnnotation;
import kc.web.base.WebBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping("/NewsManage")
public class NewsManageController extends WebBaseController {
    @Autowired
    private IArticleService ArticleService;

    /*-----------------------------------------新闻管理-----------------------------------------------*/
    /**
     * 三级级菜单：门户管理/新闻管理/新闻管理
     */
    @PreAuthorize("hasAuthority('C3BDB15F-C7A3-4805-8E10-DBFC1F9D97B1')")
    @MenuAnnotation(ParentMenuName = "新闻管理", MenuName = "新闻管理", Url = "/NewsManage/Index",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-bars", AuthorityId = "C3BDB15F-C7A3-4805-8E10-DBFC1F9D97B1",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsExtPage = false, Level = 2)
    @PermissionAnnotation(MenuName = "新闻管理", PermissionName = "新闻管理", Url = "/NewsManage/Index",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "C3BDB15F-C7A3-4805-8E10-DBFC1F9D97B1")
    @GetMapping("/Index")
    public String Index(ModelMap model) {
        // 新闻管理-编辑新闻
        model.addAttribute("canEdit", hasAuthority("4a1ca9f0-5d51-4f6f-adf7-4a3fda57ef4d"));
        // 新闻管理-下架新闻
        model.addAttribute("canDelete", hasAuthority("a1698b89-9c7a-4e38-ae11-e5ee8d4cdc90"));
        // 产品管理-上架产品
        model.addAttribute("canPublish", hasAuthority("c3772390-de54-46c8-9df8-2e6fb0a653a6"));
        // 新闻管理-新闻预览
        model.addAttribute("canPreview", hasAuthority("69fa2710-e407-40e3-b842-31f7584d8857"));
        // 下拉列表
        model.addAttribute("status", ArticleStatus.getList(null));
        return "newsManage/index";
    }

    @GetMapping("/LoadArticleList")
    public @ResponseBody
    PaginatedBaseDTO<ArticleDTO> LoadArticleList(
            int page, int rows, String title, String content, String author, String authorEmail, ArticleStatus status) {
        PaginatedBaseDTO<ArticleDTO> result = ArticleService.findPaginatedArticleByFilter(page, rows, title, content, author, authorEmail, status, ArticleType.News);
        return result;
    }

    @MenuAnnotation(ParentMenuName = "新闻管理", MenuName = "新增/编辑新闻", Url = "/NewsManage/GetArticleForm",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-bars", AuthorityId = "53d13340-65ac-11ea-8a8e-7085c2d210f2",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsExtPage = true, Level = 3)
    @PermissionAnnotation(MenuName = "新闻管理", PermissionName = "新增/编辑新闻", Url = "/NewsManage/GetArticleForm",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "53d13340-65ac-11ea-8a8e-7085c2d210f2")
    @GetMapping("/GetArticleForm")
    public String GetArticleForm(Integer id, ModelMap model) {
        model.addAttribute("ArticleTypes", ArticleType.getList(Arrays.asList(ArticleType.Info, ArticleType.SinglePage, ArticleType.Activity)));

        ArticleDTO vm = new ArticleDTO();
        vm.setArticleType(ArticleType.News);
        if (id != null && id != 0) {
            vm = ArticleService.GetArticleById(id);
            vm.setEditMode(true);
        }

        model.addAttribute("Entity", vm);

        return "newsManage/_articleForm";
    }

    @PreAuthorize("hasAuthority('4a1ca9f0-5d51-4f6f-adf7-4a3fda57ef4d')")
    @PermissionAnnotation(MenuName = "新闻管理", PermissionName = "编辑新闻", Url = "/NewsManage/SaveArticle",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "4a1ca9f0-5d51-4f6f-adf7-4a3fda57ef4d")
    @PostMapping("/SaveArticle")
    public @ResponseBody
    ServiceResult<Boolean> SaveArticle(ArticleDTO model) {
        return GetServiceResult(() -> {
            if (!model.isEditMode()) {
                model.setCreatedBy(CurrentUserName());
                model.setCreatedDate(DateExtensions.getDateTimeUtcNow());
            }
            model.setAuthor(CurrentUserDisplayName());
            model.setAuthorEmail(CurrentUserEmail());
            model.setModifiedBy(CurrentUserName());
            model.setModifiedDate(DateExtensions.getDateTimeUtcNow());
            model.setArticleType(ArticleType.News);
            return ArticleService.SaveArticle(model);
        }, log);
    }

    @PreAuthorize("hasAuthority('a1698b89-9c7a-4e38-ae11-e5ee8d4cdc90')")
    @PermissionAnnotation(MenuName = "新闻管理", PermissionName = "删除新闻", Url = "/NewsManage/RemoveArticle",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "a1698b89-9c7a-4e38-ae11-e5ee8d4cdc90")
    @GetMapping("/RemoveArticle")
    public @ResponseBody
    ServiceResult<Boolean> RemoveArticle(int id) {
        return GetServiceResult(() -> {
            return ArticleService.RemoveArticle(id);
        }, log);
    }

    @PreAuthorize("hasAuthority('c3772390-de54-46c8-9df8-2e6fb0a653a6')")
    @PermissionAnnotation(MenuName = "新闻管理", PermissionName = "审核上架", Url = "/NewsManage/PublishArticle",
                    DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false,
                    ResultType = ResultType.JsonResult, AuthorityId = "c3772390-de54-46c8-9df8-2e6fb0a653a6")
    @PostMapping("/PublishArticle")
    public @ResponseBody
    ServiceResult<Boolean> PublishArticle(int id, Boolean isAggree, String content) {
        return GetServiceResult(() -> {
            return ArticleService.PublishArticle(id, isAggree, content);
            }, log);
    }

    @PreAuthorize("hasAuthority('69fa2710-e407-40e3-b842-31f7584d8857')")
    @PermissionAnnotation(MenuName = "新闻管理", PermissionName = "新闻预览", Url = "/NewsManage/PreviewArticle",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "69fa2710-e407-40e3-b842-31f7584d8857")
    @GetMapping("/PreviewArticle")
    public @ResponseBody
    ServiceResult<Boolean> PreviewArticle(int id) {
        return GetServiceResult(() -> {
            return true;
        }, log);
    }

    @GetMapping("/ExistArticleName")
    public @ResponseBody
    Boolean ExistArticleName(int id, String name, String orginalName, boolean isEditMode) {
        if (isEditMode && name.equalsIgnoreCase(orginalName)) {
            return true;
        }
        return ArticleService.ExistArticleName(id, name);
    }

    /*------------------------------------------新闻动态类别-----------------------------------------------*/
    /**
     * 三级级菜单：门户管理/新闻管理/新闻动态类别
     */
    @PreAuthorize("hasAuthority('C575337C-4263-4275-9F2D-1141A818F6CD')")
    @MenuAnnotation(ParentMenuName = "新闻管理", MenuName = "新闻动态类别", Url = "/NewsManage/OfferingCategory",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-file-code-o", AuthorityId = "C575337C-4263-4275-9F2D-1141A818F6CD",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsExtPage = false, Level = 2)
    @PermissionAnnotation(MenuName = "新闻动态类别", PermissionName = "新闻动态类别", Url = "/NewsManage/OfferingCategory",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "C575337C-4263-4275-9F2D-1141A818F6CD")
    @GetMapping("/Category")
    public String Category(String id, ModelMap model) {
        return "NewsManage/category";
    }

    @GetMapping("/LoadArticleCategoryList")
    public @ResponseBody
    List<ArticleCategoryDTO> LoadArticleCategoryList(String name) {
        List<ArticleCategoryDTO> result = ArticleService.GetRootArticleCategorysByName(name, ArticleType.News);
        return result;
    }

    @GetMapping("/LoadArticleCategoryTree")
    public @ResponseBody
    List<ArticleCategoryDTO> LoadArticleCategoryTree(Integer pid) {
        List<ArticleCategoryDTO> tree = ArticleService.GetRootArticleCategorysByName("", ArticleType.News);
        ArticleCategoryDTO rootMenu = new ArticleCategoryDTO();
        rootMenu.setText("顶级菜单");
        rootMenu.getChildren().addAll(tree);//给set填充

        List<ArticleCategoryDTO> result = new ArrayList<ArticleCategoryDTO>();
        result.add(rootMenu);
        return  result;
    }

    @GetMapping("/GetArticleCategoryForm")
    public String GetArticleCategoryForm(int id, int parentId, ModelMap model) {
        model.addAttribute("BusinessTypes", BusinessType
                .getList(Arrays.asList(BusinessType.None)));
        model.addAttribute("ArticleTypes", ArticleType.getList(Arrays.asList(ArticleType.Info, ArticleType.SinglePage, ArticleType.Activity)));

        ArticleCategoryDTO vm = new ArticleCategoryDTO();
        vm.setArticleType(ArticleType.News);
        vm.setParentId(parentId);
        if (id != 0) {
            vm = ArticleService.GetArticleCategoryById(id);
            vm.setEditMode(true);
        }

        model.addAttribute("ArticleCategoryEntity", vm);

        return "newsManage/_categoryForm";
    }

    @PreAuthorize("hasAuthority('e81ae819-f28e-4d44-bdaa-a45bc3548883')")
    @PermissionAnnotation(MenuName = "新闻动态类别管理", PermissionName = "保存新闻动态类别", Url = "/NewsManage/SaveArticleCategory",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "94EB3E21-03FF-4E90-A211-AEA23B1023DD")
    @PostMapping("/SaveArticleCategory")
    public @ResponseBody
    ServiceResult<Boolean> SaveArticleCategory(ArticleCategoryDTO model) {
        return GetServiceResult(() -> {
            if (!model.isEditMode())
            {
                model.setCreatedBy(CurrentUserName());
                model.setCreatedDate(DateExtensions.getDateTimeUtcNow());
            }
            model.setModifiedBy(CurrentUserName());
            model.setModifiedDate(DateExtensions.getDateTimeUtcNow());
            return ArticleService.SaveArticleCategory(model);
        }, log);
    }

    @PreAuthorize("hasAuthority('5050c23c-d905-402f-b952-cad792d6fe8f')")
    @PermissionAnnotation(MenuName = "新闻动态类别管理", PermissionName = "删除新闻动态类别", Url = "/NewsManage/RemoveArticleCategory",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "32BBFB54-E724-4951-8BC5-C39492162FD3")
    @GetMapping("/RemoveArticleCategory")
    public @ResponseBody
    ServiceResult<Boolean> RemoveArticleCategory(int id) {
        return GetServiceResult(() -> {
            return ArticleService.RemoveArticleCategory(id);
        }, log);
    }

    @GetMapping("/ExistArticleCategoryName")
    public @ResponseBody
    Boolean ExistArticleCategoryName(int id, String name) {
        return ArticleService.ExistArticleCategoryName(id, name);
    }
}
