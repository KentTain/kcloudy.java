package kc.web.controller;


import kc.dto.PaginatedBaseDTO;
import kc.dto.offering.*;
import kc.enums.ResultType;
import kc.enums.offering.*;
import kc.framework.util.SerializeHelper;
import kc.framework.extension.DateExtensions;
import kc.framework.tenant.ApplicationConstant;
import kc.framework.tenant.RoleConstants;
import kc.framework.tenant.TenantConstant;
import kc.service.base.ServiceResult;
import kc.service.offering.*;
import kc.web.annotation.MenuAnnotation;
import kc.web.annotation.PermissionAnnotation;
import kc.web.base.WebBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping("/OfferingManage")
public class OfferingManageController extends WebBaseController {
    @Autowired
    private IOfferingService OfferingService;

    /*------------------------------------------产品列表-----------------------------------------------*/
    /**
     * 三级级菜单：产品管理/产品管理/产品列表
     */
    @PreAuthorize("hasAuthority('526216B4-E9F6-4ED4-B5C4-2106DFDDD614')")
    @MenuAnnotation(ParentMenuName = "产品管理", MenuName = "产品列表", Url = "/OfferingManage/Index",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-bars", AuthorityId = "526216B4-E9F6-4ED4-B5C4-2106DFDDD614",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsExtPage = false, Level = 3)
    @PermissionAnnotation(MenuName = "产品列表", PermissionName = "产品列表", Url = "/OfferingManage/Index",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "526216B4-E9F6-4ED4-B5C4-2106DFDDD614")
    @GetMapping("/Index")
    public String Index(ModelMap model) {
        // 产品管理-编辑产品
        model.addAttribute("canEdit", hasAuthority("70eaeb1b-585f-11ea-861f-7085c2d210f2"));
        // 产品管理-下架产品
        model.addAttribute("canDelete", hasAuthority("82503b54-585f-11ea-861f-7085c2d210f2"));
        // 产品管理-上架产品
        model.addAttribute("canPublish", hasAuthority("ae86bd8c-585f-11ea-861f-7085c2d210f2"));
        // 产品管理-产品预览
        model.addAttribute("canPreview", hasAuthority("cb9f7ce3-585f-11ea-861f-7085c2d210f2"));
        // 产品管理-产品日志
        model.addAttribute("canReadLog", hasAuthority("fd55dd28-59f3-11ea-861f-7085c2d210f2"));

        // 下拉列表
        //model.addAttribute("versions", OfferingVersion.getList(null));
        model.addAttribute("status", OfferingStatus.getList(null));

        return "offeringManage/index";
    }

    @PreAuthorize("hasAuthority('" + ApplicationConstant.DefaultAuthorityId + "')")
    @GetMapping("/LoadOfferingList")
    public @ResponseBody
    PaginatedBaseDTO<OfferingDTO> LoadOfferingList(
            int page, int rows, String code, String name, OfferingStatus status) {
        return OfferingService.findPaginatedOfferingsByFilter(page, rows, code, name, status);
    }



    @PreAuthorize("hasAuthority('82503b54-585f-11ea-861f-7085c2d210f2')")
    @PermissionAnnotation(MenuName = "产品列表", PermissionName = "删除产品", Url = "/OfferingManage/RemoveOffering",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "82503b54-585f-11ea-861f-7085c2d210f2")
    @GetMapping("/RemoveOffering")
    public @ResponseBody ServiceResult<Boolean> RemoveOffering(int id) {
        return GetServiceResult(() -> {
            return OfferingService.RemoveOffering(id, CurrentUserId(), CurrentUserDisplayName());
        }, log);
    }

    @PreAuthorize("hasAuthority('ae86bd8c-585f-11ea-861f-7085c2d210f2')")
    @PermissionAnnotation(MenuName = "产品列表", PermissionName = "审核上架产品", Url = "/OfferingManage/PublishOffering",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "ae86bd8c-585f-11ea-861f-7085c2d210f2")
    @PostMapping("/PublishOffering")
    public @ResponseBody ServiceResult<Boolean> PublishOffering(int id, Boolean isAggree, String content) {
        return GetServiceResult(() -> {
            return OfferingService.PublishOffering(id, isAggree, content, CurrentUserId(), CurrentUserDisplayName());
        }, log);
    }

    @PreAuthorize("hasAuthority('cb9f7ce3-585f-11ea-861f-7085c2d210f2')")
    @PermissionAnnotation(MenuName = "产品列表", PermissionName = "产品预览", Url = "/OfferingManage/PreviewOffering",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "cb9f7ce3-585f-11ea-861f-7085c2d210f2")
    @GetMapping("/PreviewOffering")
    public @ResponseBody ServiceResult<Boolean> PreviewOffering(int id) {
        return GetServiceResult(() -> {
            return true;
        }, log);
    }

    /*------------------------------------------新增/编辑产品-----------------------------------------------*/
    /**
     * 三级级菜单：产品管理/产品管理/新增/编辑产品
     */
    @PreAuthorize("hasAuthority('013eb577-6a43-11ea-babb-7085c2d210f2')")
    @MenuAnnotation(ParentMenuName = "产品管理", MenuName = "新增/编辑产品", Url = "/OfferingManage/GetOfferingForm",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-bars", AuthorityId = "013eb577-6a43-11ea-babb-7085c2d210f2",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsExtPage = true, Level = 3)
    @PermissionAnnotation(MenuName = "新增/编辑产品", PermissionName = "新增/编辑产品", Url = "/OfferingManage/GetOfferingForm",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "013eb577-6a43-11ea-babb-7085c2d210f2")
    @GetMapping("/GetOfferingForm")
    public String GetOfferingForm(Integer id, ModelMap model) {
        //model.addAttribute("OfferingTypes", OfferingType.getList(Arrays.asList(OfferingType.Equipment, OfferingType.Gift)));
        //model.addAttribute("OfferingVersions", OfferingVersion.getList(null));

        // 产品管理-产品日志
        model.addAttribute("canRemoveProduct", hasAuthority("16457D06-E973-47CA-9E92-8481AA8C5D9E"));

        String fileBlobJsonString = "";
        String blobsJsonString = "";
        String productsJsonString = "";
        OfferingDTO vm = new OfferingDTO();
        vm.setCategoryId(1);
        if (id != null && id != 0) {
            vm = OfferingService.GetOfferingById(id);
            vm.setEditMode(true);
            if (vm.getOfferingFileBlob() != null )
                fileBlobJsonString = SerializeHelper.ToJson(vm.getOfferingFileBlob()).replaceAll("\r|\n", "");
            if (vm.getOfferingImageBlobs() != null && vm.getOfferingImageBlobs().size() > 0)
                blobsJsonString = SerializeHelper.ToJson(vm.getOfferingImageBlobs()).replaceAll("\r|\n", "");
            if (vm.getProducts() != null && vm.getProducts().size() > 0)
                productsJsonString = SerializeHelper.ToJson(vm.getProducts()).replaceAll("\r|\n", "");
        }
        model.addAttribute("Entity", vm);
        model.addAttribute("fileBlobJsonString", fileBlobJsonString);
        model.addAttribute("imageBlobsJsonString", blobsJsonString);
        model.addAttribute("productsJsonString", productsJsonString);

        return "offeringManage/offeringForm";
    }

    @GetMapping("/ExistOfferingName")
    public @ResponseBody Boolean ExistOfferingName(int id, String name, String orginalName, boolean isEditMode) {
        if (isEditMode && name.equalsIgnoreCase(orginalName)) {
            return true;
        }
        return OfferingService.ExistOfferingName(id, name);
    }

    @PreAuthorize("hasAuthority('70eaeb1b-585f-11ea-861f-7085c2d210f2')")
    @PermissionAnnotation(MenuName = "新增/编辑产品", PermissionName = "保存产品数据", Url = "/OfferingManage/SaveOffering",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "70eaeb1b-585f-11ea-861f-7085c2d210f2")
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


    @PreAuthorize("hasAuthority('16457D06-E973-47CA-9E92-8481AA8C5D9E')")
    @PermissionAnnotation(MenuName = "新增/编辑产品", PermissionName = "删除产品型号", Url = "/OfferingManage/RemoveProduct",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "16457D06-E973-47CA-9E92-8481AA8C5D9E")
    @GetMapping("/RemoveProduct")
    public @ResponseBody
    ServiceResult<Boolean> RemoveProduct(Integer id) {
        return GetServiceResult(() -> {
            if (id == null || id == 0)
                return true;
            return OfferingService.RemoveProductById(id, CurrentUserId(), CurrentUserDisplayName());
        }, log);
    }

    @PreAuthorize("hasAuthority('4BFB5312-E1B1-4838-8DAD-6DCAC3E91930')")
    @PermissionAnnotation(MenuName = "新增/编辑产品", PermissionName = "删除产品图片", Url = "/OfferingManage/RemoveOfferingImageById",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 4, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "4BFB5312-E1B1-4838-8DAD-6DCAC3E91930")
    @GetMapping("/RemoveOfferingImageById")
    public @ResponseBody
    ServiceResult<Boolean> RemoveOfferingImageById(boolean isSingleOffering, Integer id) {
        return GetServiceResult(() -> {
            if (id == null || id == 0)
                return true;
            return OfferingService.RemoveOfferingImageById(isSingleOffering, id);
        }, log);
    }

    /*------------------------------------------产品日志-----------------------------------------------*/
    /**
     * 三级级菜单：产品管理/产品管理/产品日志
     */
    @PreAuthorize("hasAuthority('fd55dd28-59f3-11ea-861f-7085c2d210f2')")
    @MenuAnnotation(ParentMenuName = "产品管理", MenuName = "产品日志", Url = "/OfferingManage/OfferingLog",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-file-code-o", AuthorityId = "fd55dd28-59f3-11ea-861f-7085c2d210f2",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 4, IsExtPage = false, Level = 3)
    @PermissionAnnotation(MenuName = "产品日志", PermissionName = "产品日志", Url = "/OfferingManage/OfferingLog",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "fd55dd28-59f3-11ea-861f-7085c2d210f2")
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
