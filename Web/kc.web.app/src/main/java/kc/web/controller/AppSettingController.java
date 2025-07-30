package kc.web.controller;

import java.util.Arrays;
import java.util.List;

import kc.dto.app.AppSettingDTO;
import kc.dto.app.AppSettingPropertyDTO;
import kc.enums.ResultType;
import kc.framework.extension.DateExtensions;
import kc.framework.tenant.TenantConstant;
import kc.web.annotation.MenuAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kc.service.base.ServiceResult;
import kc.service.app.IAppSettingService;
import kc.web.annotation.PermissionAnnotation;
import kc.web.base.WebBaseController;
import kc.dto.PaginatedBaseDTO;
import kc.framework.GlobalConfig;
import kc.framework.enums.ConfigStatus;
import kc.framework.enums.ConfigType;
import kc.framework.tenant.RoleConstants;

/**
 * 二级级菜单：应用管理/应用推送管理
 */
@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping("/ApplicationSetting")
public class AppSettingController extends WebBaseController {
	@Autowired
	private IAppSettingService appSettingService;

	@PreAuthorize("hasAuthority('4319F0B9-733B-4DA8-9690-50C502106563')")
	@MenuAnnotation(ParentMenuName = "应用推送管理", MenuName = "应用推送管理", Url = "/ApplicationSetting/Index",
			Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
			SmallIcon = "fa fa-dropbox", AuthorityId = "4319F0B9-733B-4DA8-9690-50C502106563",
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 3)
	@PermissionAnnotation(MenuName = "应用推送管理", PermissionName = "应用推送管理", Url = "/ApplicationSetting/Index",
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = true,
			ResultType = ResultType.ActionResult, AuthorityId = "4319F0B9-733B-4DA8-9690-50C502106563")
	@GetMapping("/index")
	public String Index(ModelMap model) {
		model.addAttribute("resWebDomain", GlobalConfig.ResWebDomain);
		// 配置管理-保存配置属性
		model.addAttribute("cansaveAtt", hasAuthority("1557E240-6D84-4AB5-ABC6-7F54F3EBC9C2"));
		// 配置管理-删除配置属性
		model.addAttribute("canremoveAtt", hasAuthority("8542C34A-C616-4534-A17F-615B873C5A46"));
		// 下拉列表
		model.addAttribute("ConfigTypeList", ConfigType.getList(Arrays.asList(ConfigType.Default)));
		return "Application/index";
	}

	@GetMapping("/LoadPushSettingList")
	public @ResponseBody PaginatedBaseDTO<AppSettingDTO> LoadPushSettingList(int page, int rows, String searchValue,
																		  String searchType) {
		PaginatedBaseDTO<AppSettingDTO> result = appSettingService.findPaginatedAppSettingsByAppIdAndName(page, rows,
				searchValue, searchType);
		return result;
	}

	@GetMapping("/GetPushSettingForm")
	public String GetPushSettingForm(int configId, ModelMap model) {
		model.addAttribute("ConfigTypeList", ConfigType
				.getList(Arrays.asList(ConfigType.Default, ConfigType.PaymentMethod, ConfigType.WeixinConfig)));
		model.addAttribute("StateList", ConfigStatus.getList(null));

		AppSettingDTO vm = new AppSettingDTO();
		if (configId != 0) {
			vm = appSettingService.GetAppSettingById(configId);
			//vm.setEditMode(true);
		}

		model.addAttribute("AppSetting", vm);

		return "Application/_configForm";
	}

	@PreAuthorize("hasAuthority('0ADC6488-A975-4E52-AEC4-85962B9D8988')")
	@PermissionAnnotation(MenuName = "推送设置管理", PermissionName = "保存推送设置", Url = "/ApplicationSetting/SavePushSetting",
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false,
			ResultType = ResultType.JsonResult, AuthorityId = "0ADC6488-A975-4E52-AEC4-85962B9D8988")
	@PostMapping("/SavePushSetting")
	public @ResponseBody ServiceResult<Boolean> SavePushSetting(AppSettingDTO model) {
		return GetServiceResult(() -> {
			if (!model.isEditMode()) {
				model.setCreatedBy(CurrentUserId());
				model.setCreatedName(CurrentUserDisplayName());
				model.setCreatedDate(DateExtensions.getDateTimeUtcNow());
			}
			model.setModifiedBy(CurrentUserId());
			model.setModifiedName(CurrentUserDisplayName());
			model.setModifiedDate(DateExtensions.getDateTimeUtcNow());
			return appSettingService.SaveAppSetting(model);
		}, log);
	}

	@PreAuthorize("hasAuthority('3DC34E69-432B-466D-9104-64039FF632C5')")
	@PermissionAnnotation(MenuName = "推送设置管理", PermissionName = "删除推送设置", Url = "/ApplicationSetting/RemovePushSetting",
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false, 
		ResultType = ResultType.JsonResult, AuthorityId = "3DC34E69-432B-466D-9104-64039FF632C5")
	@GetMapping("/RemovePushSetting")
	public @ResponseBody ServiceResult<Boolean> RemovePushSetting(int id) {
		return GetServiceResult(() -> {
			return appSettingService.SoftRemoveAppSettingById(id, CurrentUserId(), CurrentUserDisplayName());
		}, log);
	}

	@GetMapping("/LoadTargetSettingList")
	public @ResponseBody List<AppSettingPropertyDTO> LoadTargetSettingList(int configId) {
		List<AppSettingPropertyDTO> result = appSettingService.GetAppSettingPropertiesBySettingId(configId);
		return result;
	}

	@MenuAnnotation(ParentMenuName = "应用推送管理", MenuName = "目标接口设置", Url = "/ApplicationSetting/EditTargetSetting",
			Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
			SmallIcon = "fa fa-life-ring", AuthorityId = "DDEB5BB1-6D42-439B-AE30-6807C3852C92",
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsExtPage = true, Level = 3)
	@GetMapping("/EditTargetSetting")
	public String EditTargetSetting(String cId, int pId, ModelMap model) {
		AppSettingPropertyDTO vm = new AppSettingPropertyDTO();
		if (pId != 0) {
			vm = appSettingService.GetPropertyById(pId);
	}
		vm.setAppSettingCode(cId);

		model.addAttribute("ConfigAttribute", vm);

		return "Application/EditTargetSetting";
	}

	@PreAuthorize("hasAuthority('25F11F2A-C036-4BD9-9D12-542DBCED501D')")
	@PermissionAnnotation(MenuName = "目标接口设置", PermissionName = "保存目标接口设置", Url = "/ApplicationSetting/SaveTargetSetting",
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 6, IsPage = false,
			ResultType = ResultType.JsonResult, AuthorityId = "25F11F2A-C036-4BD9-9D12-542DBCED501D")
	@PostMapping("/SaveTargetSetting")
	public @ResponseBody ServiceResult<Boolean> SaveTargetSetting(AppSettingPropertyDTO model) {
		return GetServiceResult(() -> {
			return appSettingService.SaveAppSettingProperty(model);
		}, log);
	}

	@PreAuthorize("hasAuthority('B78D5C65-AF93-492A-BF8E-B9014062B72C')")
	@PermissionAnnotation(MenuName = "目标接口设置", PermissionName = "删除目标接口设置", Url = "/ApplicationSetting/RemoveTargetSetting",
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 5, IsPage = false, 
		ResultType = ResultType.JsonResult, AuthorityId = "B78D5C65-AF93-492A-BF8E-B9014062B72C")
	@GetMapping("/RemoveTargetSetting")
	public @ResponseBody ServiceResult<Boolean> RemoveTargetSetting(int id) {
		return GetServiceResult(() -> {
			return appSettingService.SoftRemoveAppSettingById(id, CurrentUserId(), CurrentUserDisplayName());
		}, log);
	}
}
