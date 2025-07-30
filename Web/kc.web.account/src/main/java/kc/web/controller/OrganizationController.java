package kc.web.controller;

import kc.dto.account.OrganizationDTO;
import kc.dto.search.TreeNodeNameExistsDTO;
import kc.dto.search.TreeNodeSearchDTO;
import kc.enums.OrganizationType;
import kc.enums.ResultType;
import kc.enums.account.PositionLevel;
import kc.framework.GlobalConfig;
import kc.framework.enums.BusinessType;
import kc.framework.extension.DateExtensions;
import kc.framework.tenant.RoleConstants;
import kc.framework.tenant.TenantConstant;
import kc.service.account.IOrganizationService;
import kc.service.base.ServiceResult;
import kc.service.util.TreeNodeUtil;
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
import java.util.Collections;
import java.util.List;

@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping("/Organization")
public class OrganizationController extends WebBaseController {
    @Autowired
    private IOrganizationService organizationService;

    /**
     * 三级级菜单：系统管理/组织管理/组织架构管理
     */
    @PreAuthorize("hasAuthority('4DB22A38-1759-40B2-9926-44FACFA59E68')")
    @MenuAnnotation(ParentMenuName = "组织管理", MenuName = "组织架构管理", Url = "/Organization/Index",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-file-code-o", AuthorityId = "4DB22A38-1759-40B2-9926-44FACFA59E68",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 3)
    @PermissionAnnotation(MenuName = "组织架构管理", PermissionName = "组织架构管理", Url = "/Organization/Index",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "4DB22A38-1759-40B2-9926-44FACFA59E68")
    @GetMapping("/Index")
    public String Index(ModelMap model) {
        model.addAttribute("resWebDomain", GlobalConfig.ResWebDomain);
        // 配置管理-保存配置属性
        model.addAttribute("cansaveAtt", hasAuthority("1557E240-6D84-4AB5-ABC6-7F54F3EBC9C2"));
        // 配置管理-删除配置属性
        model.addAttribute("canremoveAtt", hasAuthority("8542C34A-C616-4534-A17F-615B873C5A46"));
        // 下拉列表
        model.addAttribute("BusinessTypes", BusinessType.getList(Arrays.asList(BusinessType.None)));

        // 用户管理-冻结/激活用户
        model.addAttribute("canDongJie", hasAuthority("B15DDB6E-6922-4E59-AC07-D8773BB42D0A"));
        // 用户管理-删除用户
        model.addAttribute("canDeleteUser", hasAuthority("E617F833-311B-4B4F-A04A-EC17214D6A1B"));
        // 用户管理-角色用户
        model.addAttribute("canRoleInUser", hasAuthority("993C40B4-0981-4294-BDBF-E19CE7C9B392"));
        // 用户管理-用户详情
        model.addAttribute("canUserDetail", hasAuthority("A92352D4-C288-4B53-8CDE-16DF6646B47F"));
        // 下拉列表
        model.addAttribute("PositionLevels", PositionLevel.getList(null));
        model.addAttribute("meUserId", CurrentUserId());
        model.addAttribute("adminUserId", RoleConstants.AdminUserId);

        return "Organization/index";
    }

    @GetMapping("/LoadOrganizationList")
    public @ResponseBody
    List<OrganizationDTO> LoadOrganizationList(String name) {
        List<OrganizationDTO> result = organizationService.findRootOrganizationsByName(name);
        return result;
    }

    @GetMapping("/LoadOrganizationTree")
    public @ResponseBody
    List<OrganizationDTO> LoadOrganizationTree(TreeNodeSearchDTO search) {
        List<OrganizationDTO> result = new ArrayList<>();
        if (null != search.getHasAll() && search.getHasAll()) {
            OrganizationDTO allCategory = new OrganizationDTO();
            allCategory.setId(0);
            allCategory.setText("所有用户");
            allCategory.setChildren(null);
            allCategory.setLevel(1);
            allCategory.setLeaf(true);
            result.add(allCategory);

            OrganizationDTO unCategory = new OrganizationDTO();
            unCategory.setId(-1);
            unCategory.setText("未归属用户");
            unCategory.setChildren(null);
            unCategory.setLevel(1);
            unCategory.setLeaf(true);
            result.add(unCategory);
        }

        List<OrganizationDTO> data = organizationService.findRootOrganizationsByName("");
        if (data != null && data.size() > 0)
            result.addAll(data);

        if (null != search.getHasRoot() && search.getHasRoot()) {
            OrganizationDTO rootMenu = new OrganizationDTO();
            rootMenu.setId(0);
            rootMenu.setText("顶级组织");
            rootMenu.setChildren(result);
            OrganizationDTO root = TreeNodeUtil.GetNeedLevelTreeNodeDTO(
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

    @GetMapping("/GetOrganizationForm")
    public String GetOrganizationForm(int id, int parentId, ModelMap model) {
        model.addAttribute("BusinessTypes", BusinessType.getList(Arrays.asList(BusinessType.None)));
        model.addAttribute("OrganizationTypes", OrganizationType.getList(null));

        OrganizationDTO vm = new OrganizationDTO();
        vm.setParentId(parentId);
        if (id != 0) {
            vm = organizationService.getOrganizationById(id);
            vm.setEditMode(true);
        }

        model.addAttribute("OrganizationEntity", vm);

        return "Organization/_organizationForm";
    }

    @PreAuthorize("hasAuthority('0EBCF1F7-3E2A-4A0E-AF90-36689ABB0AA3')")
    @PermissionAnnotation(MenuName = "组织架构管理", PermissionName = "保存组织架构", Url = "/Organization/saveOrganization",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "0EBCF1F7-3E2A-4A0E-AF90-36689ABB0AA3")
    @PostMapping("/SaveOrganization")
    public @ResponseBody
    ServiceResult<Boolean> SaveOrganization(OrganizationDTO model) {
        return GetServiceResult(() -> {
            if (!model.isEditMode()) {
                model.setCreatedBy(CurrentUserId());
                model.setCreatedName(CurrentUserDisplayName());
                model.setCreatedDate(DateExtensions.getDateTimeUtcNow());
            }
            model.setModifiedBy(CurrentUserId());
            model.setModifiedName(CurrentUserDisplayName());
            model.setModifiedDate(DateExtensions.getDateTimeUtcNow());
            return organizationService.saveOrganization(model);
        }, log);
    }

    @PreAuthorize("hasAuthority('93F6DC06-6426-41DF-9DB8-C0FA0758FBB7')")
    @PermissionAnnotation(MenuName = "组织架构管理", PermissionName = "删除组织架构", Url = "/Organization/removeOrganization",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false,
            ResultType = ResultType.JsonResult, AuthorityId = "93F6DC06-6426-41DF-9DB8-C0FA0758FBB7")
    @GetMapping("/RemoveOrganization")
    public @ResponseBody
    ServiceResult<Boolean> RemoveOrganization(int id) {
        return GetServiceResult(() -> {
            return organizationService.removeOrganization(id);
        }, log);
    }

    @PostMapping("/ExistOrganizationName")
    public @ResponseBody
    Boolean ExistOrganizationName(TreeNodeNameExistsDTO search) {
        return organizationService.existOrganizationName(search);
    }
}
