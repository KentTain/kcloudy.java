package kc.web.controller;


import kc.dto.PaginatedBaseDTO;
import kc.dto.portal.OfferingCategoryDTO;
import kc.dto.portal.OfferingDTO;
import kc.dto.portal.OfferingOperationLogDTO;
import kc.enums.ResultType;
import kc.enums.portal.OfferingStatus;
import kc.framework.util.SerializeHelper;
import kc.framework.enums.BusinessType;
import kc.framework.extension.DateExtensions;
import kc.framework.tenant.ApplicationConstant;
import kc.framework.tenant.RoleConstants;
import kc.framework.tenant.TenantConstant;
import kc.service.base.ServiceResult;
import kc.service.portal.IOfferingService;
import kc.web.annotation.MenuAnnotation;
import kc.web.annotation.PermissionAnnotation;
import kc.web.base.WebBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping("/OfferingManage")
public class OfferingManageController extends WebBaseController {

    @Autowired
    private IOfferingService OfferingService;

    /*------------------------------------------商品管理-----------------------------------------------*/
    /**
     * 三级级菜单：门户管理/商品管理/商品管理
     */
    @PreAuthorize("hasAuthority('7F6FCA65-007B-4CC0-B866-D92E11409DC9')")
    @MenuAnnotation(ParentMenuName = "商品管理", MenuName = "商品管理", Url = "/OfferingManage/Index",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-bars", AuthorityId = "7F6FCA65-007B-4CC0-B866-D92E11409DC9",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsExtPage = false, Level = 3)
    @PermissionAnnotation(MenuName = "商品管理", PermissionName = "商品管理", Url = "/OfferingManage/Index",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "7F6FCA65-007B-4CC0-B866-D92E11409DC9")
    @GetMapping("/Index")
    public String Index(ModelMap model, Integer id) {
        // 商品管理-删除商品分类负责人
        model.addAttribute("canRemoveManager", hasAuthority("70012987-7DB1-4050-9692-570DC890121A"));
        // 商品管理-添加及保存商品分类规格
        model.addAttribute("canSaveSpecification", hasAuthority("1E26A9F4-EDF0-4162-9A9A-BF76B9248BE3"));
        // 商品管理-删除商品分类规格
        model.addAttribute("canRemoveSpecification", hasAuthority("EAC5CA8E-5264-461D-92EE-D5C026664F2D"));

        // 商品管理-编辑商品
        model.addAttribute("canEdit", hasAuthority("52DFF617-3BB5-4A43-B807-73DB4A571924"));
        // 商品管理-下架商品
        model.addAttribute("canDelete", hasAuthority("45771690-C428-49EB-B853-FF826F199309"));
        // 商品管理-上架商品
        model.addAttribute("canPublish", hasAuthority("BAEBC859-303E-4CD9-AAC5-CBBB36FF6677"));
        // 商品管理-商品预览
        model.addAttribute("canPreview", hasAuthority("8DD724F9-A311-4508-A475-23054FE5E258"));
        // 商品管理-商品日志
        model.addAttribute("canReadLog", hasAuthority("D186BDC3-8F72-462D-94DB-F5F2B6D8375E"));

        // 下拉列表
        //model.addAttribute("versions", OfferingVersion.getList(null));
        model.addAttribute("status", OfferingStatus.getList(null));

        model.addAttribute("selectCategoryId", id != null ? id : 0);

        return "offeringManage/index";
    }

    /*------------------------------------------商品分类-----------------------------------------------*/
    @PreAuthorize("hasAuthority('" + ApplicationConstant.DefaultAuthorityId + "')")
    @GetMapping("/LoadCategoryList")
    public @ResponseBody
    List<OfferingCategoryDTO> LoadCategoryList(String name) {
        List<OfferingCategoryDTO> result = OfferingService.GetRootOfferingCategoriesByName(name);
        return result;
    }

    @PreAuthorize("hasAuthority('" + ApplicationConstant.DefaultAuthorityId + "')")
    @GetMapping("/LoadCategoryTree")
    public @ResponseBody
    List<OfferingCategoryDTO> LoadCategoryTree(Integer pid) {
        List<OfferingCategoryDTO> tree = OfferingService.GetRootOfferingCategoriesByName("");
        OfferingCategoryDTO rootMenu = new OfferingCategoryDTO();
        rootMenu.setText("顶级菜单");
        rootMenu.getChildren().addAll(tree);//给set填充

        List<OfferingCategoryDTO> result = new ArrayList<OfferingCategoryDTO>();
        result.add(rootMenu);
        return result;
    }

    @GetMapping("/GetCategoryForm")
    public String GetCategoryForm(int id, int parentId, ModelMap model) {
        model.addAttribute("BusinessTypes", BusinessType
                .getList(Arrays.asList(BusinessType.None)));
        //model.addAttribute("CategoryTypes", CategoryType.getList(null));

        OfferingCategoryDTO vm = new OfferingCategoryDTO();
        vm.setParentId(parentId);
        if (id != 0) {
            vm = OfferingService.GetCategoryById(id);
            vm.setEditMode(true);
        }

        model.addAttribute("CategoryEntity", vm);

        return "offeringManage/_categoryForm";
    }

    @PreAuthorize("hasAuthority('94EB3E21-03FF-4E90-A211-AEA23B1023DD')")
    @PermissionAnnotation(MenuName = "商品管理", PermissionName = "保存商品分类", Url = "/OfferingManage/SaveCategory",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "94EB3E21-03FF-4E90-A211-AEA23B1023DD")
    @PostMapping("/SaveCategory")
    public @ResponseBody
    ServiceResult<Boolean> SaveCategory(OfferingCategoryDTO model) {
        return GetServiceResult(() -> {
            if (!model.isEditMode()) {
                model.setCreatedBy(CurrentUserName());
                model.setCreatedDate(DateExtensions.getDateTimeUtcNow());
            }
            model.setModifiedBy(CurrentUserName());
            model.setModifiedDate(DateExtensions.getDateTimeUtcNow());
            return OfferingService.SaveCategory(model);
        }, log);
    }

    @PreAuthorize("hasAuthority('32BBFB54-E724-4951-8BC5-C39492162FD3')")
    @PermissionAnnotation(MenuName = "商品管理", PermissionName = "删除商品分类", Url = "/OfferingManage/RemoveCategory",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "32BBFB54-E724-4951-8BC5-C39492162FD3")
    @GetMapping("/RemoveCategory")
    public @ResponseBody
    ServiceResult<Boolean> RemoveCategory(int id) {
        return GetServiceResult(() -> {
            return OfferingService.RemoveCategory(id);
        }, log);
    }

    @GetMapping("/ExistCategoryName")
    public @ResponseBody
    Boolean ExistCategoryName(int id, String name) {
        return OfferingService.ExistCategoryName(id, name);
    }


    /*------------------------------------------商品管理-----------------------------------------------*/
    @PreAuthorize("hasAuthority('" + ApplicationConstant.DefaultAuthorityId + "')")
    @GetMapping("/LoadOfferingList")
    public @ResponseBody
    PaginatedBaseDTO<OfferingDTO> LoadOfferingList(
            int page, int rows, Integer categoryId, String code, String name, OfferingStatus status) {
        return OfferingService.findPaginatedOfferingsByFilter(page, rows, categoryId, code, name, status);
    }

    @PreAuthorize("hasAuthority('45771690-C428-49EB-B853-FF826F199309')")
    @PermissionAnnotation(MenuName = "商品管理", PermissionName = "删除商品", Url = "/OfferingManage/RemoveOffering",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "45771690-C428-49EB-B853-FF826F199309")
    @GetMapping("/RemoveOffering")
    public @ResponseBody
    ServiceResult<Boolean> RemoveOffering(int id) {
        return GetServiceResult(() -> {
            return OfferingService.RemoveOffering(id, CurrentUserId(), CurrentUserDisplayName());
        }, log);
    }

    @PreAuthorize("hasAuthority('BAEBC859-303E-4CD9-AAC5-CBBB36FF6677')")
    @PermissionAnnotation(MenuName = "商品管理", PermissionName = "审核上架商品", Url = "/OfferingManage/PublishOffering",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "BAEBC859-303E-4CD9-AAC5-CBBB36FF6677")
    @PostMapping("/PublishOffering")
    public @ResponseBody
    ServiceResult<Boolean> PublishOffering(int id, Boolean isAgree, String content) {
        return GetServiceResult(() -> {
            return OfferingService.PublishOffering(id, isAgree, content, CurrentUserId(), CurrentUserDisplayName());
        }, log);
    }

    @PreAuthorize("hasAuthority('8DD724F9-A311-4508-A475-23054FE5E258')")
    @PermissionAnnotation(MenuName = "商品管理", PermissionName = "商品预览", Url = "/OfferingManage/PreviewOffering",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "8DD724F9-A311-4508-A475-23054FE5E258")
    @GetMapping("/PreviewOffering")
    public @ResponseBody
    ServiceResult<Boolean> PreviewOffering(int id) {
        return GetServiceResult(() -> {
            return true;
        }, log);
    }

    /*------------------------------------------新增/编辑商品-----------------------------------------------*/
    @PreAuthorize("hasAuthority('C432F578-F980-4EE7-9E18-8C7C4F84D07D')")
    @MenuAnnotation(ParentMenuName = "商品管理", MenuName = "新增/编辑商品", Url = "/OfferingManage/GetOfferingForm",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-bars", AuthorityId = "C432F578-F980-4EE7-9E18-8C7C4F84D07D",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsExtPage = true, Level = 3)
    @PermissionAnnotation(MenuName = "新增/编辑商品", PermissionName = "新增/编辑商品", Url = "/OfferingManage/GetOfferingForm",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "C432F578-F980-4EE7-9E18-8C7C4F84D07D")
    @GetMapping("/GetOfferingForm")
    public String GetOfferingForm(Integer id, ModelMap model) {
        //model.addAttribute("OfferingTypes", OfferingType.getList(Arrays.asList(OfferingType.Equipment, OfferingType.Gift)));
        //model.addAttribute("OfferingVersions", OfferingVersion.getList(null));

        // 商品管理-商品日志
        model.addAttribute("canRemoveProduct", hasAuthority("16457D06-E973-47CA-9E92-8481AA8C5D9E"));

        String fileBlobJsonString = "";
        String blobsJsonString = "";
        OfferingDTO vm = new OfferingDTO();
        vm.setCategoryId(1);
        if (id != null && id != 0) {
            vm = OfferingService.GetOfferingById(id);
            vm.setEditMode(true);
            if (vm.getOfferingFileBlob() != null)
                fileBlobJsonString = SerializeHelper.ToJson(vm.getOfferingFileBlob()).replaceAll("\r|\n", "");
            if (vm.getOfferingImageBlobs() != null && vm.getOfferingImageBlobs().size() > 0)
                blobsJsonString = SerializeHelper.ToJson(vm.getOfferingImageBlobs()).replaceAll("\r|\n", "");
        }
        model.addAttribute("Entity", vm);
        model.addAttribute("fileBlobJsonString", fileBlobJsonString);
        model.addAttribute("imageBlobsJsonString", blobsJsonString);

        return "offeringManage/offeringForm";
    }

    @GetMapping("/ExistOfferingName")
    public @ResponseBody
    Boolean ExistOfferingName(int id, String name, String orginalName, boolean isEditMode) {
        if (isEditMode && name.equalsIgnoreCase(orginalName)) {
            return true;
        }
        return OfferingService.ExistOfferingName(id, name);
    }

    @PreAuthorize("hasAuthority('52DFF617-3BB5-4A43-B807-73DB4A571924')")
    @PermissionAnnotation(MenuName = "新增/编辑商品", PermissionName = "保存商品数据", Url = "/OfferingManage/SaveOffering",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "52DFF617-3BB5-4A43-B807-73DB4A571924")
    @PostMapping("/SaveOffering")
    public @ResponseBody
    ServiceResult<Boolean> SaveOffering(@RequestBody OfferingDTO model) {
        return GetServiceResult(() -> {
            if (!model.isEditMode()) {
                model.setCreatedBy(CurrentUserName());
                model.setCreatedDate(DateExtensions.getDateTimeUtcNow());
            }
            model.setModifiedBy(CurrentUserName());
            model.setModifiedDate(DateExtensions.getDateTimeUtcNow());
            return OfferingService.SaveOffering(model, CurrentUserId(), CurrentUserDisplayName());
        }, log);
    }

    @PreAuthorize("hasAuthority('33CED704-D816-4B05-89FB-ED25E52CD149')")
    @PermissionAnnotation(MenuName = "新增/编辑商品", PermissionName = "删除商品图片", Url = "/OfferingManage/RemoveOfferingImageById",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 4, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "33CED704-D816-4B05-89FB-ED25E52CD149")
    @GetMapping("/RemoveOfferingImageById")
    public @ResponseBody
    ServiceResult<Boolean> RemoveOfferingImageById(boolean isSingleOffering, Integer id) {
        return GetServiceResult(() -> {
            if (id == null || id == 0)
                return true;
            return OfferingService.RemoveOfferingImageById(isSingleOffering, id);
        }, log);
    }

    /*------------------------------------------商品日志-----------------------------------------------*/

    /**
     * 三级级菜单：门户管理/商品管理/商品日志
     */
    @PreAuthorize("hasAuthority('D186BDC3-8F72-462D-94DB-F5F2B6D8375E')")
    @MenuAnnotation(ParentMenuName = "商品管理", MenuName = "商品日志", Url = "/OfferingManage/OfferingLog",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-file-code-o", AuthorityId = "D186BDC3-8F72-462D-94DB-F5F2B6D8375E",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 4, IsExtPage = false, Level = 3)
    @PermissionAnnotation(MenuName = "商品日志", PermissionName = "商品日志", Url = "/OfferingManage/OfferingLog",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "D186BDC3-8F72-462D-94DB-F5F2B6D8375E")
    @GetMapping("/OfferingLog")
    public String OfferingLog(String code, ModelMap model) {
        model.addAttribute("code", code);
        return "offeringManage/offeringLog";
    }

    @PreAuthorize("hasAuthority('" + ApplicationConstant.DefaultAuthorityId + "')")
    @GetMapping("/LoadOfferingLogList")
    public @ResponseBody
    PaginatedBaseDTO<OfferingOperationLogDTO> LoadOfferingLogList(int page, int rows, String code, String name) {
        PaginatedBaseDTO<OfferingOperationLogDTO> result = OfferingService.findPaginatedOfferingLogsByFilter(page, rows, code, name);
        return result;
    }

}
