package kc.web.controller;

import kc.dto.PaginatedBaseDTO;
import kc.dto.portal.ArticleCategoryDTO;
import kc.dto.portal.ArticleDTO;
import kc.enums.ResultType;
import kc.enums.portal.ArticleStatus;
import kc.enums.portal.ArticleType;
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
@RequestMapping("/CompanyInfo")
public class CompanyInfoController extends WebBaseController {
    @Autowired
    private IArticleService ArticleService;

    /*-----------------------------------------公司基本信息管理-----------------------------------------------*/
    /**
     * 三级级菜单：门户管理/门户信息管理/公司基本信息
     */
    @PreAuthorize("hasAuthority('6B58AA63-009A-4E4D-ACDA-581EDDFD4981')")
    @MenuAnnotation(ParentMenuName = "门户信息管理", MenuName = "公司基本信息", Url = "/CompanyInfo/Index",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-bars", AuthorityId = "6B58AA63-009A-4E4D-ACDA-581EDDFD4981",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsExtPage = false, Level = 2)
    @PermissionAnnotation(MenuName = "公司基本信息", PermissionName = "公司基本信息", Url = "/CompanyInfo/Index",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "6B58AA63-009A-4E4D-ACDA-581EDDFD4981")
    @GetMapping("/Index")
    public String Index(ModelMap model) {
        // 公司基本信息-编辑新闻
        model.addAttribute("canEdit", hasAuthority("4a1ca9f0-5d51-4f6f-adf7-4a3fda57ef4d"));
        // 公司基本信息-下架新闻
        model.addAttribute("canDelete", hasAuthority("a1698b89-9c7a-4e38-ae11-e5ee8d4cdc90"));
        // 产品管理-上架产品
        model.addAttribute("canPublish", hasAuthority("c3772390-de54-46c8-9df8-2e6fb0a653a6"));
        // 公司基本信息-新闻预览
        model.addAttribute("canPreview", hasAuthority("69fa2710-e407-40e3-b842-31f7584d8857"));
        // 下拉列表
        model.addAttribute("status", ArticleStatus.getList(null));
        return "CompanyInfo/index";
    }

    @GetMapping("/LoadArticleList")
    public @ResponseBody
    PaginatedBaseDTO<ArticleDTO> LoadArticleList(
            int page, int rows, String title, String content, String author, String authorEmail, ArticleStatus status) {
        PaginatedBaseDTO<ArticleDTO> result = ArticleService.findPaginatedArticleByFilter(page, rows, title, content, author, authorEmail, status, ArticleType.News);
        return result;
    }
    

    @PreAuthorize("hasAuthority('4a1ca9f0-5d51-4f6f-adf7-4a3fda57ef4d')")
    @PermissionAnnotation(MenuName = "公司基本信息", PermissionName = "编辑公司基本信息", Url = "/CompanyInfo/SaveArticle",
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


    /*------------------------------------------联系人管理-----------------------------------------------*/
    @GetMapping("/LoadContractList")
    public @ResponseBody
    List<ArticleCategoryDTO> LoadContractList(String name) {
        List<ArticleCategoryDTO> result = ArticleService.GetRootArticleCategorysByName(name, ArticleType.News);
        return result;
    }


    @GetMapping("/GetContractForm")
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

        return "CompanyInfo/_contractForm";
    }

    @PreAuthorize("hasAuthority('e81ae819-f28e-4d44-bdaa-a45bc3548883')")
    @PermissionAnnotation(MenuName = "新闻动态类别管理", PermissionName = "保存新闻动态类别", Url = "/CompanyInfo/SaveArticleCategory",
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
    @PermissionAnnotation(MenuName = "新闻动态类别管理", PermissionName = "删除新闻动态类别", Url = "/CompanyInfo/RemoveArticleCategory",
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
