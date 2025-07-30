package kc.web.controller;

import kc.dto.PaginatedBaseDTO;
import kc.dto.codegenerate.ModelDefFieldDTO;
import kc.dto.codegenerate.ModelDefinitionDTO;
import kc.dto.codegenerate.RelationDefDetailDTO;
import kc.dto.codegenerate.RelationDefinitionDTO;
import kc.enums.ResultType;
import kc.enums.codegenerate.RelationType;
import kc.framework.GlobalConfig;
import kc.framework.extension.DateExtensions;
import kc.framework.tenant.RoleConstants;
import kc.framework.tenant.TenantConstant;
import kc.service.base.ServiceResult;
import kc.service.codegenerate.IModelDefinitionService;
import kc.service.codegenerate.IRelationDefinitionService;
import kc.web.annotation.MenuAnnotation;
import kc.web.annotation.PermissionAnnotation;
import kc.web.base.WebBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping("/RelationDefinition")
public class RelationDefController extends WebBaseController {
    @Autowired
    private IRelationDefinitionService relationDefService;
    @Autowired
    private IModelDefinitionService modeDefService;

    /**
     * 三级菜单：代码生成管理/模型管理/关系模型
     */
    @PreAuthorize("hasAuthority('9CF893D5-31E7-43C2-8037-201990C16BDB')")
    @MenuAnnotation(ParentMenuName = "模型管理", MenuName = "关系模型", Url = "/RelationDefinition/Index",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-dropbox", AuthorityId = "9CF893D5-31E7-43C2-8037-201990C16BDB",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsExtPage = false, Level = 3)
    @PermissionAnnotation(MenuName = "关系模型", PermissionName = "关系模型", Url = "/RelationDefinition/Index",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "9CF893D5-31E7-43C2-8037-201990C16BDB")
    @GetMapping("/Index")
    public String Index(ModelMap model) {
        // 关系模型-保存关系模型
        model.addAttribute("canSave", hasAuthority("3D20BC51-4A68-458A-A078-9561BCD77500"));
        // 关系模型-删除关系模型
        model.addAttribute("canRemove", hasAuthority("67564645-36BF-42B3-8D24-BF3DB7E6068F"));

        // 关系模型-保存关系模型属性
        model.addAttribute("canSaveAtt", hasAuthority("9C65146A-A0BE-4E15-A6C7-C3FDCF897121"));
        // 关系模型-删除关系模型属性
        model.addAttribute("canRemoveAtt", hasAuthority("7E7E0CB7-49DB-4C2B-8219-8F95996B12C1"));

        // 关系模型-保存模型分类
        model.addAttribute("canEditCategory", hasAuthority("376E7F91-C48A-40DF-B3BB-C69903AD0358"));
        // 关系模型-删除模型分类
        model.addAttribute("canDeleteCategory", hasAuthority("2519E41E-211F-4529-B5B8-A42BF55E0172"));
        return "RelationDefinition/index";
    }

    @GetMapping("/LoadRelationDefinitionList")
    public @ResponseBody
    PaginatedBaseDTO<RelationDefinitionDTO> LoadRelationDefinitionList(int page, int rows, Integer categoryId, String name, String displayName) {
        return relationDefService.findPaginatedRelationDefinitions(page, rows, categoryId, name, displayName, null);
    }

    @GetMapping("/LoadRelationDefDetailList")
    public @ResponseBody
    List<RelationDefDetailDTO> LoadRelationDefDetailList(@RequestParam(value = "id") Integer id) {
        return relationDefService.findAllRelationDefDetailsByDefId(id);
    }

    @PreAuthorize("hasAuthority('67564645-36BF-42B3-8D24-BF3DB7E6068F')")
    @PermissionAnnotation(MenuName = "关系模型", PermissionName = "删除关系模型", Url = "/RelationDefinition/RemoveRelationDefinition",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "67564645-36BF-42B3-8D24-BF3DB7E6068F")
    @GetMapping("/RemoveRelationDefinition")
    public @ResponseBody
    ServiceResult<Boolean> RemoveRelationDefinition(@RequestParam(value = "id") Integer id) {
        return GetServiceResult(() -> {
            return relationDefService.removeRelationDefinitionById(id, CurrentUserId(), CurrentUserDisplayName());
        }, log);
    }


    @GetMapping("/GetRelationDefDetailForm")
    public String GetRelationDefDetailForm(
            @RequestParam(value = "defId") Integer defId,
            @RequestParam(value = "id") Integer id,
            @RequestParam(value = "mainModelId") Integer mainModelId, ModelMap model) {
        RelationDefDetailDTO vm = new RelationDefDetailDTO();
        vm.setRelationDefId(defId);
        if (null != id && 0 != id) {
            vm = relationDefService.getRelationDefDetailById(id);
            vm.setIsEditMode(true);
        }

        model.addAttribute("MainModelId", mainModelId);
        model.addAttribute("RelationDefDetailVM", vm);
        model.addAttribute("RelationTypeList", RelationType.getList(null));

        return "RelationDefinition/_relationDefDetailForm";
    }

    @PreAuthorize("hasAuthority('9E25E12D-D8F4-480A-9472-7E217725F74C')")
    @PermissionAnnotation(MenuName = "关系模型", PermissionName = "保存关系模型属性", Url = "/RelationDefinition/saveRelationDefDetail",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 4, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "9E25E12D-D8F4-480A-9472-7E217725F74C")
    @PostMapping("/SaveRelationDefDetail")
    public @ResponseBody
    ServiceResult<Boolean> SaveRelationDefDetail(@RequestBody RelationDefDetailDTO model) {
        return GetServiceResult(() -> {
            if (0 != model.getId()) {
                model.setCreatedBy(CurrentUserId());
                model.setCreatedName(CurrentUserDisplayName());
                model.setCreatedDate(DateExtensions.getDateTimeUtcNow());
            }
            model.setModifiedBy(CurrentUserId());
            model.setModifiedName(CurrentUserDisplayName());
            model.setModifiedDate(DateExtensions.getDateTimeUtcNow());

            return relationDefService.saveRelationDefDetail(model);
        }, log);
    }

    @PreAuthorize("hasAuthority('7E7E0CB7-49DB-4C2B-8219-8F95996B12C1')")
    @PermissionAnnotation(MenuName = "关系模型", PermissionName = "删除关系模型属性", Url = "/RelationDefinition/RemoveRelationDefDetail",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 5, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "7E7E0CB7-49DB-4C2B-8219-8F95996B12C1")
    @GetMapping("/RemoveRelationDefDetail")
    public @ResponseBody
    ServiceResult<Boolean> RemoveRelationDefDetail(@RequestParam(value = "id") int id) {
        return GetServiceResult(() -> {
            return relationDefService.removeRelationDefDetailById(id, CurrentUserId(), CurrentUserDisplayName());
        }, log);
    }

    /**
     * 三级菜单：代码生成管理/模型管理/新增/编辑关系模型
     */
    @PreAuthorize("hasAuthority('3D20BC51-4A68-458A-A078-9561BCD77500')")
    @MenuAnnotation(ParentMenuName = "模型管理", MenuName = "新增/编辑关系模型", Url = "/RelationDefinition/GetRelationDefinitionForm",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-dropbox", AuthorityId = "3D20BC51-4A68-458A-A078-9561BCD77500",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsExtPage = true, Level = 3)
    @PermissionAnnotation(MenuName = "模型管理", PermissionName = "新增/编辑关系模型", Url = "/RelationDefinition/GetRelationDefinitionForm",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "3D20BC51-4A68-458A-A078-9561BCD77500")
    @GetMapping("/GetRelationDefinitionForm")
    public String GetRelationDefinitionForm(Integer id, Integer categoryId, Model model) {
        model.addAttribute("RelationTypeList", RelationType
                .getList(null));
        // 关系模型-删除关系模型属性
        model.addAttribute("canRemoveAtt", hasAuthority("7E7E0CB7-49DB-4C2B-8219-8F95996B12C1"));

        RelationDefinitionDTO vm = new RelationDefinitionDTO();
        vm.setCategoryId(categoryId);
        //vm.setRelationType(RelationType.EntityBase);
        if (null != id && id != 0) {
            vm = relationDefService.getRelationDefinitionById(id);
            vm.setIsEditMode(true);
        }

        model.addAttribute("RelationDefinitionVM", vm);

        return "RelationDefinition/relationDefinitionForm";
    }

    @PreAuthorize("hasAuthority('0A941AE9-300F-481D-A3DC-6DEE67BD4B30')")
    @PermissionAnnotation(MenuName = "新增/编辑关系模型", PermissionName = "保存关系模型", Url = "/RelationDefinition/SaveRelationDefinition",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "0A941AE9-300F-481D-A3DC-6DEE67BD4B30")
    @PostMapping("/SaveRelationDefinition")
    public @ResponseBody
    ServiceResult<Boolean> SaveRelationDefinition(@RequestBody RelationDefinitionDTO model) {
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
            return relationDefService.saveRelationDefinitionWithFields(model);
        }, log);
    }

    @PostMapping("/ExistsRelationDefName")
    public @ResponseBody
    Boolean ExistsRelationDefName(String appId, Integer id, String name) {
        return relationDefService.existsRelationDefName(appId, id, name);
    }

    @GetMapping("/LoadAllModelDefinitionList")
    public @ResponseBody
    List<ModelDefinitionDTO> LoadAllModelDefinitionList(String name, String displayName) {
        return modeDefService.findAllModelDefinitions(null, null, name, displayName, null, null);
    }

    @GetMapping("/LoadAllModelDefFieldListByDefId")
    public @ResponseBody
    List<ModelDefFieldDTO> LoadAllModelDefFieldListByDefId(Integer defId) {
        return modeDefService.findAllModelDefFieldsByDefId(defId);
    }

}
