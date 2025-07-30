package kc.web.controller;

import kc.dto.PaginatedBaseDTO;
import kc.dto.codegenerate.ModelChangeLogDTO;
import kc.enums.ResultType;
import kc.enums.codegenerate.ModelType;
import kc.framework.tenant.ApplicationConstant;
import kc.framework.tenant.RoleConstants;
import kc.framework.tenant.TenantConstant;
import kc.service.codegenerate.IModelChangeLogService;
import kc.web.annotation.MenuAnnotation;
import kc.web.annotation.PermissionAnnotation;
import kc.web.base.WebBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;


@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping("/ModelChangeLog")
public class ModelLogController extends WebBaseController {
    @Autowired
    private IModelChangeLogService logService;

    /**
     * 二级级菜单：代码生成管理/日志管理
     */
    @PreAuthorize("hasAuthority('350F432F-5E06-4FE9-B8D9-A8FD1FA7E13D')")
    @MenuAnnotation(ParentMenuName = "代码生成管理", MenuName = "日志管理", Url = "/ModelChangeLog/Index",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-file-code-o", AuthorityId = "350F432F-5E06-4FE9-B8D9-A8FD1FA7E13D",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 9, IsExtPage = false, Level = 2)
    @PermissionAnnotation(MenuName = "日志管理", PermissionName = "日志管理", Url = "/ModelChangeLog/Index",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
            ResultType = ResultType.ActionResult, AuthorityId = "350F432F-5E06-4FE9-B8D9-A8FD1FA7E13D")
    @GetMapping("/Index")
    public String ModelChangeLog(String code, ModelMap model) {
        // 继承类型
        model.addAttribute("ModelTypeList", ModelType
                .getList(Arrays.asList(ModelType.Other)));
        model.addAttribute("code", code);
        return "ModelChangeLog/index";
    }

    @PreAuthorize("hasAuthority('" + ApplicationConstant.DefaultAuthorityId + "')")
    @GetMapping("/LoadModelChangeLogList")
    public @ResponseBody
    PaginatedBaseDTO<ModelChangeLogDTO> LoadModelChangeLogList(int page, int rows, ModelType type, String name, String code) {
        return logService.findPaginatedModelLogsByFilter(page, rows, type, name, code);
    }

}
