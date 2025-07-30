package kc.web.controller;


import kc.dto.PaginatedBaseDTO;
import kc.dto.account.UserSimpleDTO;
import kc.dto.offering.CategoryDTO;
import kc.dto.offering.CategoryManagerDTO;
import kc.dto.offering.OfferingSpecificationDTO;
import kc.dto.offering.PropertyProviderDTO;
import kc.enums.ResultType;
import kc.enums.offering.OfferingStatus;
import kc.enums.offering.OfferingVersion;
import kc.framework.enums.BusinessType;
import kc.framework.extension.DateExtensions;
import kc.framework.tenant.ApplicationConstant;
import kc.framework.tenant.RoleConstants;
import kc.framework.tenant.TenantConstant;
import kc.service.base.ServiceResult;
import kc.service.offering.ICategoryService;
import kc.web.Model.CategoryManagerViewModel;
import kc.web.Model.CategorySpecificationViewModel;
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
@RequestMapping("/CategoryManage")
public class CategoryManageController extends WebBaseController {
    @Autowired
    private ICategoryService categoryService;

    /*------------------------------------------产品分类-----------------------------------------------*/
    /**
     * 二级级菜单：产品管理/产品分类
     */
    @PreAuthorize("hasAuthority('B626F1C2-8571-49F8-854B-12CEA4482171')")
    @MenuAnnotation(ParentMenuName = "产品管理", MenuName = "产品分类", Url = "/CategoryManage/Index",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-list", AuthorityId = "B626F1C2-8571-49F8-854B-12CEA4482171",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 2)
    @PermissionAnnotation(MenuName = "产品分类", PermissionName = "产品分类", Url = "/CategoryManage/Index",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "B626F1C2-8571-49F8-854B-12CEA4482171")
    @GetMapping("/Index")
    public String Index(ModelMap model, Integer id) {
        // 产品分类-编辑产品分类
        model.addAttribute("canEdit", hasAuthority("3918fd48-5851-11ea-861f-7085c2d210f2"));
        // 产品分类-删除产品分类负责人
        model.addAttribute("canRemoveManager", hasAuthority("B25FFD5F-12D6-4E3C-8084-5F5C89A68C89"));
        // 产品分类-添加及保存产品分类规格
        model.addAttribute("canSaveSpecification", hasAuthority("3B9630CE-A93B-4158-9B44-FE08F9AC03B4"));
        // 产品分类-删除产品分类规格
        model.addAttribute("canRemoveSpecification", hasAuthority("A597D719-EFC0-4F03-949F-2C481BC7B1A6"));

        // 下拉列表
        //model.addAttribute("versions", OfferingVersion.getList(null));
        //model.addAttribute("status", OfferingStatus.getList(null));

        model.addAttribute("selectCategoryId", id != null ? id : 0);

        return "CategoryManage/index";
    }

    @PreAuthorize("hasAuthority('" + ApplicationConstant.DefaultAuthorityId + "')")
    @GetMapping("/LoadCategoryList")
    public @ResponseBody
    List<CategoryDTO> LoadCategoryList(String name) {
        List<CategoryDTO> result = categoryService.GetRootOfferingCategoriesByName(name);
        return result;
    }

    @PreAuthorize("hasAuthority('" + ApplicationConstant.DefaultAuthorityId + "')")
    @GetMapping("/LoadCategoryTree")
    public @ResponseBody List<CategoryDTO> LoadCategoryTree(Integer pid)
    {
        List<CategoryDTO> tree = categoryService.GetRootOfferingCategoriesByName("");
        CategoryDTO rootMenu = new CategoryDTO();
        rootMenu.setText("顶级菜单");
        rootMenu.getChildren().addAll(tree);//给set填充

        List<CategoryDTO> result = new ArrayList<CategoryDTO>();
        result.add(rootMenu);
        return  result;
    }
    
    @GetMapping("/GetCategoryForm")
    public String GetCategoryForm(int id, int parentId, ModelMap model) {
        model.addAttribute("BusinessTypes", BusinessType
                .getList(Arrays.asList(BusinessType.None)));
        //model.addAttribute("CategoryTypes", CategoryType.getList(null));

        CategoryDTO vm = new CategoryDTO();
        vm.setParentId(parentId);
        if (id != 0) {
            vm = categoryService.GetCategoryById(id);
            vm.setEditMode(true);
        }

        model.addAttribute("CategoryEntity", vm);

        return "CategoryManage/_categoryForm";
    }

    @PreAuthorize("hasAuthority('3918fd48-5851-11ea-861f-7085c2d210f2')")
    @PermissionAnnotation(MenuName = "产品分类", PermissionName = "保存产品分类", Url = "/CategoryManage/SaveCategory",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "3918fd48-5851-11ea-861f-7085c2d210f2")
    @PostMapping("/SaveCategory")
    public @ResponseBody
    ServiceResult<Boolean> SaveCategory(CategoryDTO model) {
        return GetServiceResult(() -> {
            if (!model.isEditMode())
            {
                model.setCreatedBy(CurrentUserId());
                model.setCreatedName(CurrentUserName());
                model.setCreatedDate(DateExtensions.getDateTimeUtcNow());
            }
            model.setModifiedBy(CurrentUserId());
            model.setModifiedName(CurrentUserName());
            model.setModifiedDate(DateExtensions.getDateTimeUtcNow());
            return categoryService.SaveCategory(model);
        }, log);
    }

    @PreAuthorize("hasAuthority('5c063e24-5851-11ea-861f-7085c2d210f2')")
    @PermissionAnnotation(MenuName = "产品分类", PermissionName = "删除产品分类", Url = "/CategoryManage/RemoveCategory",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "5c063e24-5851-11ea-861f-7085c2d210f2")
    @GetMapping("/RemoveCategory")
    public @ResponseBody ServiceResult<Boolean> RemoveCategory(int id) {
        return GetServiceResult(() -> {
            return categoryService.RemoveCategory(id);
        }, log);
    }

    @GetMapping("/ExistCategoryName")
    public @ResponseBody Boolean ExistCategoryName(int id, String name) {
        return categoryService.ExistCategoryName(id, name);
    }

    /*------------------------------------------类别负责人设置-----------------------------------------------*/

    // @PreAuthorize("hasAuthority('A291F728-B9B3-448B-A793-B3DAFA1BA126')")
    // @PermissionAnnotation(MenuName = "产品分类", PermissionName = "加载产品列表",
    // Url="/User/LoadUserList",
    // DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = false,
    // ResultType = ResultType.JsonResult, AuthorityId =
    // "A291F728-B9B3-448B-A793-B3DAFA1BA126")
    @GetMapping("/LoadManagerList")
    public @ResponseBody
    PaginatedBaseDTO<CategoryManagerDTO> LoadManagerList(int page, int rows, int categoryId) {
        List<CategoryManagerDTO> result = categoryService.findAllManagersByCategoryId(categoryId);
        return new PaginatedBaseDTO<CategoryManagerDTO>(page, rows, result.size(), result);
    }

    @PreAuthorize("hasAuthority('192956FD-63EA-4A72-9745-8982125771CB')")
    @PermissionAnnotation(MenuName = "产品分类", PermissionName = "添加产品分类负责人", Url = "/CategoryManage/AddManagers",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "192956FD-63EA-4A72-9745-8982125771CB")
    @PostMapping("/AddManagers")
    public @ResponseBody
    ServiceResult<Boolean> AddManagers(@RequestBody CategoryManagerViewModel model) {
        return GetServiceResult(() -> {
            int categoryId = model.getCategoryId();
            List<UserSimpleDTO> users = model.getUsers();
            return categoryService.AddCategoryManagers(categoryId, users, CurrentUserId(), CurrentUserName());
        }, log);
    }

    @PreAuthorize("hasAuthority('B25FFD5F-12D6-4E3C-8084-5F5C89A68C89')")
    @PermissionAnnotation(MenuName = "产品分类", PermissionName = "删除产品分类负责人", Url = "/CategoryManage/RemoveManager",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "B25FFD5F-12D6-4E3C-8084-5F5C89A68C89")
    @GetMapping("/RemoveManager")
    public @ResponseBody
    ServiceResult<Boolean> RemoveManager(int id) {
        return GetServiceResult(() -> {
            return categoryService.RemoveCategoryManagerById(id);
        }, log);
    }

    /*------------------------------------------规格设置-----------------------------------------------*/
    @GetMapping("/LoadSpecificationList")
    public @ResponseBody
    PaginatedBaseDTO<OfferingSpecificationDTO> LoadSpecificationList(int page, int rows, int categoryId) {
        List<OfferingSpecificationDTO> result = categoryService.findAllSpecificationsByCategoryId(categoryId);
        return new PaginatedBaseDTO<OfferingSpecificationDTO>(page, rows, result.size(), result);
    }

    @GetMapping("/loadAllProvidersByCategoryId")
    public @ResponseBody
    List<PropertyProviderDTO> loadAllProvidersByCategoryId(int categoryId) {
        return categoryService.findAllProvidersByCategoryId(categoryId);
    }

    @PreAuthorize("hasAuthority('3B9630CE-A93B-4158-9B44-FE08F9AC03B4')")
    @PermissionAnnotation(MenuName = "产品分类", PermissionName = "添加及保存产品分类规格", Url = "/CategoryManage/SaveSpecifications",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "3B9630CE-A93B-4158-9B44-FE08F9AC03B4")
    @PostMapping("/SaveSpecifications")
    public @ResponseBody
    ServiceResult<Boolean> SaveSpecifications(@RequestBody CategorySpecificationViewModel model) {
        return GetServiceResult(() -> {
            int categoryId = model.getCategoryId();
            List<OfferingSpecificationDTO> specs = model.getSpecifications();
            return categoryService.SaveSpecifications(categoryId, specs, CurrentUserId(), CurrentUserName());
        }, log);
    }

    @PreAuthorize("hasAuthority('A597D719-EFC0-4F03-949F-2C481BC7B1A6')")
    @PermissionAnnotation(MenuName = "产品分类", PermissionName = "删除产品分类规格", Url = "/CategoryManage/RemoveSpecification",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 4, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "A597D719-EFC0-4F03-949F-2C481BC7B1A6")
    @GetMapping("/RemoveSpecification")
    public @ResponseBody
    ServiceResult<Boolean> RemoveSpecification(int id) {
        return GetServiceResult(() -> {
            return categoryService.RemoveSpecificationById(id);
        }, log);
    }
}
