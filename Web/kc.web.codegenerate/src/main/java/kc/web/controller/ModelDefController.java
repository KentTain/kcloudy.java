package kc.web.controller;

import kc.dto.PaginatedBaseDTO;
import kc.dto.codegenerate.ModelDefFieldDTO;
import kc.dto.codegenerate.ModelDefinitionDTO;
import kc.enums.ResultType;
import kc.enums.codegenerate.ModelBaseType;
import kc.enums.codegenerate.PrimaryKeyType;
import kc.framework.GlobalConfig;
import kc.framework.enums.AttributeDataType;
import kc.framework.extension.DateExtensions;
import kc.framework.tenant.RoleConstants;
import kc.framework.tenant.TenantConstant;
import kc.service.base.ServiceResult;
import kc.service.codegenerate.IModelDefinitionService;
import kc.web.annotation.MenuAnnotation;
import kc.web.annotation.PermissionAnnotation;
import kc.web.base.WebBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping("/ModelDefinition")
public class ModelDefController extends WebBaseController {
    @Autowired
    private IModelDefinitionService modeDefService;

    /**
     * 三级菜单：代码生成管理/模型管理/数据模型
     */
    @PreAuthorize("hasAuthority('50EED938-2A75-408D-983A-F46A88E83262')")
    @MenuAnnotation(ParentMenuName = "模型管理", MenuName = "数据模型", Url = "/ModelDefinition/Index",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-dropbox", AuthorityId = "50EED938-2A75-408D-983A-F46A88E83262",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 3)
    @PermissionAnnotation(MenuName = "模型管理", PermissionName = "数据模型", Url = "/ModelDefinition/Index",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "50EED938-2A75-408D-983A-F46A88E83262")
    @GetMapping("/Index")
    public String Index(ModelMap model) {
        //model.addAttribute("resWebDomain", GlobalConfig.ResWebDomain);
        // 数据模型-保存数据模型
        model.addAttribute("canSave", hasAuthority("C1396AE0-5CD8-4008-B862-FDA1B34A7CCA"));
        // 数据模型-删除数据模型
        model.addAttribute("canRemove", hasAuthority("7D996D8E-EF5D-4151-A5DF-9C2D2938F447"));

        // 数据模型-保存数据模型属性
        model.addAttribute("canSaveAtt", hasAuthority("9C65146A-A0BE-4E15-A6C7-C3FDCF897121"));
        // 数据模型-删除数据模型属性
        model.addAttribute("canRemoveAtt", hasAuthority("54099A1B-B97E-45EB-9DF6-379D5EE464C0"));

        // 数据模型-保存模型分类
        model.addAttribute("canEditCategory", hasAuthority("376E7F91-C48A-40DF-B3BB-C69903AD0358"));
        // 数据模型-删除模型分类
        model.addAttribute("canDeleteCategory", hasAuthority("2519E41E-211F-4529-B5B8-A42BF55E0172"));

        // 继承类型
        model.addAttribute("ModelBaseTypeList", ModelBaseType
                .getList(Arrays.asList(ModelBaseType.Property, ModelBaseType.PropertyAttribute)));
        return "ModelDefinition/index";
    }

    @GetMapping("/LoadModelDefinitionList")
    public @ResponseBody
    PaginatedBaseDTO<ModelDefinitionDTO> LoadModelDefinitionList(
            int page, int rows, Integer categoryId, String name, String displayName, String tableName, ModelBaseType type) {
        return modeDefService.findPaginatedModelDefinitions(page, rows, categoryId, type, name, displayName, tableName, null);
    }

    @GetMapping("/LoadModelDefFieldList")
    public @ResponseBody
    List<ModelDefFieldDTO> LoadModelDefFieldList(@RequestParam(value = "id") Integer id) {
        List<ModelDefFieldDTO> result = modeDefService.findAllModelDefFieldsByDefId(id);
        return result;
    }

    @PreAuthorize("hasAuthority('7D996D8E-EF5D-4151-A5DF-9C2D2938F447')")
    @PermissionAnnotation(MenuName = "数据模型", PermissionName = "删除数据模型", Url = "/ModelDefinition/RemoveModelDefinition",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "7D996D8E-EF5D-4151-A5DF-9C2D2938F447")
    @GetMapping("/RemoveModelDefinition")
    public @ResponseBody
    ServiceResult<Boolean> RemoveModelDefinition(@RequestParam(value = "id") Integer id) {
        return GetServiceResult(() -> {
            return modeDefService.removeModelDefinitionById(id, CurrentUserId(), CurrentUserDisplayName());
        }, log);
    }

    @GetMapping("/GetModelDefFieldForm")
    public String GetModelDefFieldForm(@RequestParam(value = "defId") Integer defId, @RequestParam(value = "id") Integer id, ModelMap model) {
        ModelDefFieldDTO vm = new ModelDefFieldDTO();
        vm.setModelDefId(defId);
        vm.setDataType(AttributeDataType.String);
        vm.setPrimaryKeyType(null);
        vm.setCanEdit(true);
        if (null != id && 0 != id) {
            vm = modeDefService.getModelDefFieldById(id);
            vm.setIsEditMode(true);
        }

        model.addAttribute("ModelDefFieldVM", vm);
        model.addAttribute("DataTypeList", AttributeDataType
                .getList(Arrays.asList(AttributeDataType.Object, AttributeDataType.List)));

        return "ModelDefinition/_modelDefFieldForm";
    }

    @PreAuthorize("hasAuthority('AA313516-4C42-4D13-A5A5-8FB30E08EFE5')")
    @PermissionAnnotation(MenuName = "数据模型", PermissionName = "保存数据模型属性", Url = "/ModelDefinition/saveModelDefField",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 4, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "AA313516-4C42-4D13-A5A5-8FB30E08EFE5")
    @PostMapping("/SaveModelDefField")
    public @ResponseBody
    ServiceResult<Boolean> SaveModelDefField(@RequestBody ModelDefFieldDTO model) {
        return GetServiceResult(() -> {
            if (0 != model.getPropertyAttributeId()) {
                model.setCreatedBy(CurrentUserId());
                model.setCreatedName(CurrentUserDisplayName());
                model.setCreatedDate(DateExtensions.getDateTimeUtcNow());
            }
            model.setModifiedBy(CurrentUserId());
            model.setModifiedName(CurrentUserDisplayName());
            model.setModifiedDate(DateExtensions.getDateTimeUtcNow());

            return modeDefService.saveModelDefField(model);
        }, log);
    }

    @PreAuthorize("hasAuthority('54099A1B-B97E-45EB-9DF6-379D5EE464C0')")
    @PermissionAnnotation(MenuName = "数据模型", PermissionName = "删除数据模型属性", Url = "/ModelDefinition/RemoveModelDefField",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 5, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "54099A1B-B97E-45EB-9DF6-379D5EE464C0")
    @GetMapping("/RemoveModelDefField")
    public @ResponseBody
    ServiceResult<Boolean> RemoveModelDefField(@RequestParam(value = "id") int id) {
        return GetServiceResult(() -> {
            return modeDefService.removeModelDefFieldById(id, CurrentUserId(), CurrentUserDisplayName());
        }, log);
    }


    /**
     * 三级菜单：应用管理/模型管理/新增/编辑数据模型
     */
    @PreAuthorize("hasAuthority('C1396AE0-5CD8-4008-B862-FDA1B34A7CCA')")
    @MenuAnnotation(ParentMenuName = "模型管理", MenuName = "新增/编辑数据模型", Url = "/ModelDefinition/GetModelDefinitionForm",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-dropbox", AuthorityId = "C1396AE0-5CD8-4008-B862-FDA1B34A7CCA",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsExtPage = true, Level = 3)
    @PermissionAnnotation(MenuName = "模型管理", PermissionName = "新增/编辑数据模型", Url = "/ModelDefinition/GetModelDefinitionForm",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "C1396AE0-5CD8-4008-B862-FDA1B34A7CCA")
    @GetMapping("/GetModelDefinitionForm")
    public String GetModelDefinitionForm(Integer id, Integer categoryId, Model model) {
        model.addAttribute("ModelBaseTypeList", ModelBaseType
                .getList(Arrays.asList(ModelBaseType.Property, ModelBaseType.PropertyAttribute)));
        model.addAttribute("DataTypeList", AttributeDataType
                .getList(Arrays.asList(AttributeDataType.Object)));
        model.addAttribute("PrimaryKeyTypeList", PrimaryKeyType
                .getList(Arrays.asList(PrimaryKeyType.SnowFlake)));
        // 数据模型-删除数据模型属性
        model.addAttribute("canRemoveAtt", hasAuthority("54099A1B-B97E-45EB-9DF6-379D5EE464C0"));

        ModelDefinitionDTO vm = new ModelDefinitionDTO();
        vm.setCategoryId(categoryId);
        vm.setModelBaseType(ModelBaseType.EntityBase);
        if (null != id && id != 0) {
            vm = modeDefService.getModelDefinitionById(id);
            vm.setIsEditMode(true);
        }

        model.addAttribute("ModelDefinitionVM", vm);

        return "ModelDefinition/modelDefinitionForm";
    }

    @PreAuthorize("hasAuthority('624AD3A5-A7AD-4AB3-8B8E-D6916564D927')")
    @PermissionAnnotation(MenuName = "新增/编辑数据模型", PermissionName = "保存数据模型", Url = "/ModelDefinition/SaveModelDefinition",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "624AD3A5-A7AD-4AB3-8B8E-D6916564D927")
    @PostMapping("/SaveModelDefinition")
    public @ResponseBody
    ServiceResult<Boolean> SaveModelDefinition(@RequestBody ModelDefinitionDTO model) {
        return GetServiceResult(() -> {
            if (!model.getIsEditMode()) {
                model.setCreatedBy(CurrentUserId());
                model.setCreatedName(CurrentUserDisplayName());
                model.setCreatedDate(DateExtensions.getDateTimeUtcNow());
            }
            model.setApplicationId(GlobalConfig.CurrentApplication.getAppId());
            model.setModifiedBy(CurrentUserId());
            model.setModifiedName(CurrentUserDisplayName());
            model.setModifiedDate(DateExtensions.getDateTimeUtcNow());
            return modeDefService.saveModelDefinitionWithFields(model);
        }, log);
    }

    @PostMapping("/ExistsModelDefName")
    public @ResponseBody
    Boolean ExistModelDefName(String appId, Integer id, String name) {
        return modeDefService.existsModelDefName(appId, id, name);
    }

    @PostMapping("/ExistsModelDefTableName")
    public @ResponseBody
    Boolean ExistsModelDefTableName(String appId, Integer id, String name) {
        return modeDefService.existsModelDefTableName(appId, id, name);
    }
}
