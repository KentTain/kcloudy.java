package kc.web.controller;

import kc.dto.PaginatedBaseDTO;
import kc.dto.app.ApplicationDTO;
import kc.dto.app.ApplicationLogDTO;
import kc.enums.ResultType;
import kc.enums.app.AppLogType;
import kc.framework.GlobalConfig;
import kc.framework.enums.ConfigStatus;
import kc.framework.enums.ConfigType;
import kc.framework.extension.DateExtensions;
import kc.framework.tenant.ApplicationConstant;
import kc.framework.tenant.RoleConstants;
import kc.framework.tenant.TenantConstant;
import kc.service.app.IApplicationService;
import kc.service.base.ServiceResult;
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
import java.util.UUID;

/**
 * 三级菜单：应用管理/应用业务管理/应用管理
 */
@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping("/Application")
public class ApplicationController extends WebBaseController {
	@Autowired
	private IApplicationService appSettingService;

	@PreAuthorize("hasAuthority('42911D0D-8CF4-421C-9CE3-21E5DFCC78B3')")
	@MenuAnnotation(ParentMenuName = "应用业务管理", MenuName = "应用管理", Url = "/Application/Index",
			Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
			SmallIcon = "fa fa-dropbox", AuthorityId = "42911D0D-8CF4-421C-9CE3-21E5DFCC78B3",
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 3)
	@PermissionAnnotation(MenuName = "应用管理", PermissionName = "应用管理", Url = "/Application/Index",
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
			ResultType = ResultType.ActionResult, AuthorityId = "42911D0D-8CF4-421C-9CE3-21E5DFCC78B3")
	@GetMapping("/Index")
	public String Index(ModelMap model) {
		model.addAttribute("resWebDomain", GlobalConfig.ResWebDomain);
		// 应用管理-保存应用属性
		model.addAttribute("canSaveAtt", hasAuthority("9C65146A-A0BE-4E15-A6C7-C3FDCF897121"));
		// 应用管理-删除应用属性
		model.addAttribute("canRemoveAtt", hasAuthority("8542C34A-C616-4534-A17F-615B873C5A46"));
		return "Application/index";
	}

	@GetMapping("/LoadApplicationList")
	public @ResponseBody PaginatedBaseDTO<ApplicationDTO> LoadApplicationList(int page, int rows, String searchValue) {
		PaginatedBaseDTO<ApplicationDTO> result = appSettingService.findPaginatedApplicationsByAppName(page, rows,
				searchValue);
		return result;
	}

	@GetMapping("/GetApplicationForm")
	public String GetApplicationForm(@RequestParam(value = "id")String id, Model model) {
		model.addAttribute("ConfigTypeList", ConfigType
				.getList(Arrays.asList(ConfigType.Default, ConfigType.PaymentMethod, ConfigType.WeixinConfig)));
		model.addAttribute("StateList", ConfigStatus.getList(null));

		ApplicationDTO vm = new ApplicationDTO();
		if (null !=id  && !ApplicationConstant.UUID_NIL.equals(id)) {
			vm = appSettingService.GetApplicationById(id);
			vm.setEditMode(true);
		}

		model.addAttribute("ApplicationEntity", vm);

		return "Application/_applicationForm";
	}

	@PreAuthorize("hasAuthority('9C65146A-A0BE-4E15-A6C7-C3FDCF897121')")
	@PermissionAnnotation(MenuName = "应用管理", PermissionName = "保存应用", Url = "/Application/SaveConfig",
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false, 
			ResultType = ResultType.JsonResult, AuthorityId = "9C65146A-A0BE-4E15-A6C7-C3FDCF897121")
	@PostMapping("/SaveApplication")
	public @ResponseBody ServiceResult<Boolean> SaveApplication(ApplicationDTO model) {
		return GetServiceResult(() -> {
			if (!model.isEditMode()) {
				model.setCreatedBy(CurrentUserId());
				model.setCreatedName(CurrentUserDisplayName());
				model.setCreatedDate(DateExtensions.getDateTimeUtcNow());
			}
			model.setModifiedBy(CurrentUserId());
			model.setModifiedName(CurrentUserDisplayName());
			model.setModifiedDate(DateExtensions.getDateTimeUtcNow());
			return appSettingService.SaveApplication(model);
		}, log);
	}

	@PreAuthorize("hasAuthority('0E86F5D6-F93B-47FD-BBC1-39CBBAF17178')")
	@PermissionAnnotation(MenuName = "应用管理", PermissionName = "删除应用", Url = "/Application/RemoveApplication",
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false, 
		ResultType = ResultType.JsonResult, AuthorityId = "0E86F5D6-F93B-47FD-BBC1-39CBBAF17178")
	@GetMapping("/RemoveApplication")
	public @ResponseBody ServiceResult<Boolean> RemoveApplication(@RequestParam(value = "id")String id) {
		return GetServiceResult(() -> {
			return appSettingService.SoftRemoveApplicationById(id, CurrentUserId(), CurrentUserDisplayName());
		}, log);
	}

	/**
	 * 二级菜单：应用管理/应用日志
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasAuthority('8A30C5D5-D0C2-4CAF-B3BF-088825E6E3A4')")
	@MenuAnnotation(ParentMenuName = "应用管理", MenuName = "应用日志", Url = "/Application/AppLog",
			Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
			SmallIcon = "fa fa-dropbox", AuthorityId = "8A30C5D5-D0C2-4CAF-B3BF-088825E6E3A4",
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 3)
	@PermissionAnnotation(MenuName = "应用日志", PermissionName = "应用日志", Url = "/Application/AppLog",
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
			ResultType = ResultType.ActionResult, AuthorityId = "8A30C5D5-D0C2-4CAF-B3BF-088825E6E3A4")
	@GetMapping("/AppLog")
	public String AppLog(ModelMap model) {
		return "Application/appLog";
	}

	@GetMapping("/LoadApplicationLogList")
	public @ResponseBody PaginatedBaseDTO<ApplicationLogDTO> LoadApplicationLogList(int page, int rows, AppLogType type, String searchValue) {
		PaginatedBaseDTO<ApplicationLogDTO> result = appSettingService.FindPaginatedApplicationLogs(page, rows, type,  searchValue);
		return result;
	}
}
