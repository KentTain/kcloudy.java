package kc.web.controller;

import kc.dto.codegenerate.ModelCategoryDTO;
import kc.dto.codegenerate.ModelCategoryNameExistsDTO;
import kc.dto.codegenerate.ModelCategorySearchDTO;
import kc.enums.ResultType;
import kc.enums.codegenerate.ModelType;
import kc.framework.extension.DateExtensions;
import kc.framework.tenant.RoleConstants;
import kc.service.base.ServiceResult;
import kc.service.codegenerate.IModelCategoryService;
import kc.service.util.TreeNodeUtil;
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
import java.util.Collections;
import java.util.List;


@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping("/ModelCategory")
public class ModelCategoryController extends WebBaseController {
    @Autowired
    private IModelCategoryService categoryService;

    @GetMapping("/LoadModelCategoryList")
    public @ResponseBody
    List<ModelCategoryDTO> LoadModelCategoryList(String name) {
        List<ModelCategoryDTO> result = categoryService.findRootModelCategoriesByName(name);
        return result;
    }

    @GetMapping("/LoadModelCategoryTree")
    public @ResponseBody
    List<ModelCategoryDTO> LoadModelCategoryTree(ModelCategorySearchDTO search) {
        List<ModelCategoryDTO> result = new ArrayList<>();
        if (null != search.getHasAll() && search.getHasAll()) {
            ModelCategoryDTO allCategory = new ModelCategoryDTO();
            allCategory.setId(0);
            allCategory.setText("所有模型");
            allCategory.setChildren(null);
            allCategory.setLevel(1);
            allCategory.setLeaf(true);
            result.add(allCategory);

            ModelCategoryDTO unCategory = new ModelCategoryDTO();
            unCategory.setId(-1);
            unCategory.setText("未分类模型");
            unCategory.setChildren(null);
            unCategory.setLevel(1);
            unCategory.setLeaf(true);
            result.add(unCategory);
        }

        List<ModelCategoryDTO> data = categoryService.findRootModelCategoriesByName(search.getModelType(), search.getName());
        if (data != null && data.size() > 0)
            result.addAll(data);

        if (null != search.getHasRoot() && search.getHasRoot()) {
            ModelCategoryDTO rootMenu = new ModelCategoryDTO();
            rootMenu.setId(0);
            rootMenu.setText("顶级菜单");
            rootMenu.setChildren(result);
            ModelCategoryDTO root = TreeNodeUtil.GetNeedLevelTreeNodeDTO(
                    rootMenu, search.getMaxLevel(),
                    Collections.singletonList(search.getExcludeId()),
                    Collections.singletonList(search.getSelectedId()));
            return Collections.singletonList(root);
        }

        return TreeNodeUtil.LoadNeedLevelTreeNodeDTO(
                result, search.getMaxLevel(),
                Collections.singletonList(search.getExcludeId()),
                Collections.singletonList(search.getSelectedId()));
    }

    @GetMapping("/GetModelCategoryForm")
    public String GetModelCategoryForm(Integer id, Integer pid, ModelType modelType, ModelMap model) {
        model.addAttribute("ModelTypes", ModelType.getList(null));

        ModelCategoryDTO vm = new ModelCategoryDTO();
        vm.setParentId(pid);
        vm.setModelType(modelType);
        if (null != id && id != 0) {
            vm = categoryService.getModelCategoryById(id);
            vm.setIsEditMode(true);
        }

        model.addAttribute("ModelCategoryVM", vm);

        return "ModelCategory/_categoryForm";
    }

    @PreAuthorize("hasAuthority('376E7F91-C48A-40DF-B3BB-C69903AD0358')")
    @PermissionAnnotation(MenuName = "数据模型", PermissionName = "保存模型分类", Url = "/ModelCategory/SaveModelCategory",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 5, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "376E7F91-C48A-40DF-B3BB-C69903AD0358")
    @PostMapping("/SaveModelCategory")
    public @ResponseBody
    ServiceResult<Boolean> SaveModelCategory(ModelCategoryDTO model) {
        return GetServiceResult(() -> {
            if (!model.getIsEditMode()) {
                model.setCreatedBy(CurrentUserId());
                model.setCreatedName(CurrentUserDisplayName());
                model.setCreatedDate(DateExtensions.getDateTimeUtcNow());
            }
            model.setModifiedBy(CurrentUserId());
            model.setModifiedName(CurrentUserDisplayName());
            model.setModifiedDate(DateExtensions.getDateTimeUtcNow());
            return categoryService.saveModelCategory(model);
        }, log);
    }

    @PreAuthorize("hasAuthority('2519E41E-211F-4529-B5B8-A42BF55E0172')")
    @PermissionAnnotation(MenuName = "数据模型", PermissionName = "删除模型分类", Url = "/ModelCategory/RemoveModelCategory",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 6, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "2519E41E-211F-4529-B5B8-A42BF55E0172")
    @GetMapping("/RemoveModelCategory")
    public @ResponseBody
    ServiceResult<Boolean> RemoveModelCategory(int id) {
        return GetServiceResult(() -> {
            return categoryService.removeModelCategory(id);
        }, log);
    }

    @PostMapping("/ExistsModelCategoryName")
    public @ResponseBody
    Boolean ExistsModelCategoryName(ModelCategoryNameExistsDTO search) {
        return categoryService.existsModelCategoryName(search);
    }

}
