package kc.web.controller;

import java.util.Arrays;

import kc.enums.ResultType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kc.service.base.ServiceResult;
import kc.service.training.ITeacherService;
import kc.web.annotation.MenuAnnotation;
import kc.web.annotation.PermissionAnnotation;
import kc.web.base.WebBaseController;
import kc.framework.tenant.TenantConstant;
import kc.dto.PaginatedBaseDTO;
import kc.dto.training.TeacherDTO;
import kc.enums.training.AccountStatus;
import kc.framework.GlobalConfig;
import kc.framework.enums.ConfigStatus;
import kc.framework.enums.ConfigType;
import kc.framework.tenant.RoleConstants;

@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping("/Teacher")
@MenuAnnotation(ParentMenuName = "教师管理", MenuName = "教师管理", Url = "/Teacher/Index", Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType, SmallIcon = "fa fa-file-code-o", AuthorityId = "7D931A51-18DF-439D-BBE9-173576711980", DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 2)
public class TeacherController extends WebBaseController {
	@Autowired
	private ITeacherService _teachService;

	// @PreAuthorize("hasAuthority('7D931A51-18DF-439D-BBE9-173576711980')")
	@PermissionAnnotation(MenuName = "教师管理", PermissionName = "教师管理", Url = "/Teacher/Index", 
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = true, 
			ResultType = ResultType.ActionResult, AuthorityId = "7D931A51-18DF-439D-BBE9-173576711980")
	@GetMapping("/index")
	public String Index(ModelMap model) {
		model.addAttribute("resWebDomain", GlobalConfig.ResWebDomain);
		// 教师管理-保存教师属性
		model.addAttribute("cansaveAtt", hasAuthority("1557E240-6D84-4AB5-ABC6-7F54F3EBC9C2"));
		// 教师管理-删除教师属性
		model.addAttribute("canremoveAtt", hasAuthority("8542C34A-C616-4534-A17F-615B873C5A46"));
		// 下拉列表
		model.addAttribute("ConfigTypeList", ConfigType.getList(Arrays.asList(ConfigType.Default)));
		return "/Teacher/index";
	}

	// @PreAuthorize("hasAuthority('A291F728-B9B3-448B-A793-B3DAFA1BA126')")
	// @PermissionAnnotation(MenuName = "教师管理", PermissionName = "加载教师列表",
	// Url="/Teacher/LoadConfigList",
	// DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = false,
	// ResultType = kc.enums.ResultType.JsonResult, AuthorityId =
	// "A291F728-B9B3-448B-A793-B3DAFA1BA126")
	@GetMapping("/LoadConfigList")
	public @ResponseBody PaginatedBaseDTO<TeacherDTO> LoadConfigList(int page, int rows, String searchValue,
			AccountStatus searchType) {
		PaginatedBaseDTO<TeacherDTO> result = _teachService.findPaginatedTeachersByNameAndType(page, rows,
				searchValue, searchType);
		return result;
	}

	@GetMapping("/GetConfigForm")
	public String GetConfigForm(int configId, ModelMap model) {
		model.addAttribute("ConfigTypeList", ConfigType
				.getList(Arrays.asList(ConfigType.Default, ConfigType.PaymentMethod, ConfigType.WeixinConfig)));
		model.addAttribute("StateList", ConfigStatus.getList(null));

		TeacherDTO vm = new TeacherDTO();
		if (configId != 0) {
			vm = _teachService.GetTeacherById(configId);
			vm.setEditMode(true);
		}

		model.addAttribute("Teacher", vm);

		return "/Teacher/_teacherForm";
	}

	// @PreAuthorize("hasAuthority('420BFA23-BAC5-4EA2-88D9-A5D060A0C600')")
	@PermissionAnnotation(MenuName = "教师管理", PermissionName = "保存教师", Url = "/Teacher/SaveConfig", 
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false, 
			ResultType = ResultType.JsonResult, AuthorityId = "420BFA23-BAC5-4EA2-88D9-A5D060A0C600")
	@PostMapping("/SaveConfig")
	public @ResponseBody ServiceResult<Boolean> SaveConfig(TeacherDTO model) {
		return GetServiceResult(() -> {
			return _teachService.SaveTeacher(model);
		}, log);
	}

	@PreAuthorize("hasAuthority('51DE1887-5C57-4C17-984D-F23456499652')")
	@PermissionAnnotation(MenuName = "教师管理", PermissionName = "删除教师", Url = "/Teacher/RemoveConfig", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false, 
		ResultType = ResultType.JsonResult, AuthorityId = "51DE1887-5C57-4C17-984D-F23456499652")
	@GetMapping("/RemoveConfig")
	public @ResponseBody ServiceResult<Boolean> RemoveConfig(int configId) {
		return GetServiceResult(() -> {
			return _teachService.SoftRemoveTeacherById(configId);
		}, log);
	}

}
