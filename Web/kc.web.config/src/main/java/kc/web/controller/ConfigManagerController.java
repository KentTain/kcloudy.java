package kc.web.controller;

import java.util.Arrays;
import java.util.List;

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
import kc.service.config.IConfigService;
import kc.web.annotation.MenuAnnotation;
import kc.web.annotation.PermissionAnnotation;
import kc.web.base.WebBaseController;
import kc.framework.tenant.TenantConstant;
import kc.dto.PaginatedBaseDTO;
import kc.dto.config.ConfigAttributeDTO;
import kc.dto.config.ConfigEntityDTO;
import kc.framework.GlobalConfig;
import kc.framework.enums.ConfigStatus;
import kc.framework.enums.ConfigType;
import kc.framework.tenant.RoleConstants;

/**
 * 二级级菜单：配置管理/配置管理
 */
@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping("/ConfigManager")
@MenuAnnotation(ParentMenuName = "配置管理", MenuName = "配置管理", Url = "/ConfigManager/Index",
		Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
		SmallIcon = "fa fa-file-code-o", AuthorityId = "7D931A51-18DF-439D-BBE9-173576711980",
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 2)
public class ConfigManagerController extends WebBaseController {
	@Autowired
	private IConfigService ConfigService;

	@PreAuthorize("hasAuthority('7D931A51-18DF-439D-BBE9-173576711980')")
	@PermissionAnnotation(MenuName = "配置管理", PermissionName = "配置管理", Url = "/ConfigManager/Index", 
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = true, 
			ResultType = ResultType.ActionResult, AuthorityId = "7D931A51-18DF-439D-BBE9-173576711980")
	@GetMapping("/index")
	public String Index(ModelMap model) {
		model.addAttribute("resWebDomain", GlobalConfig.ResWebDomain);
		// 配置管理-保存配置属性
		model.addAttribute("cansaveAtt", hasAuthority("1557E240-6D84-4AB5-ABC6-7F54F3EBC9C2"));
		// 配置管理-删除配置属性
		model.addAttribute("canremoveAtt", hasAuthority("8542C34A-C616-4534-A17F-615B873C5A46"));
		// 下拉列表
		model.addAttribute("ConfigTypeList", ConfigType.getList(Arrays.asList(ConfigType.Default)));
		return "ConfigManager/index";
	}

	// @PreAuthorize("hasAuthority('A291F728-B9B3-448B-A793-B3DAFA1BA126')")
	// @PermissionAnnotation(MenuName = "配置管理", PermissionName = "加载配置列表",
	// Url="/ConfigManager/LoadConfigList",
	// DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = false,
	// ResultType = kc.enums.ResultType.JsonResult, AuthorityId =
	// "A291F728-B9B3-448B-A793-B3DAFA1BA126")
	@GetMapping("/LoadConfigList")
	public @ResponseBody PaginatedBaseDTO<ConfigEntityDTO> LoadConfigList(int page, int rows, String searchValue,
			ConfigType searchType) {
		PaginatedBaseDTO<ConfigEntityDTO> result = ConfigService.findPaginatedConfigsByNameAndType(page, rows,
				searchValue, searchType);
		return result;
	}

	@GetMapping("/GetConfigForm")
	public String GetConfigForm(int configId, ModelMap model) {
		model.addAttribute("ConfigTypeList", ConfigType
				.getList(Arrays.asList(ConfigType.Default, ConfigType.PaymentMethod, ConfigType.WeixinConfig)));
		model.addAttribute("StateList", ConfigStatus.getList(null));

		ConfigEntityDTO vm = new ConfigEntityDTO();
		if (configId != 0) {
			vm = ConfigService.GetConfigById(configId);
			vm.setEditMode(true);
		}

		model.addAttribute("ConfigEntity", vm);

		return "ConfigManager/_configForm";
	}

	@PreAuthorize("hasAuthority('420BFA23-BAC5-4EA2-88D9-A5D060A0C600')")
	@PermissionAnnotation(MenuName = "配置管理", PermissionName = "保存配置", Url = "/ConfigManager/SaveConfig", 
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false, 
			ResultType = ResultType.JsonResult, AuthorityId = "420BFA23-BAC5-4EA2-88D9-A5D060A0C600")
	@PostMapping("/SaveConfig")
	public @ResponseBody ServiceResult<Boolean> SaveConfig(ConfigEntityDTO model) {
		return GetServiceResult(() -> {
			return ConfigService.SaveConfig(model);
		}, log);
	}

	@PreAuthorize("hasAuthority('51DE1887-5C57-4C17-984D-F23456499652')")
	@PermissionAnnotation(MenuName = "配置管理", PermissionName = "删除配置", Url = "/ConfigManager/RemoveConfig", 
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false, 
		ResultType = ResultType.JsonResult, AuthorityId = "51DE1887-5C57-4C17-984D-F23456499652")
	@GetMapping("/RemoveConfig")
	public @ResponseBody ServiceResult<Boolean> RemoveConfig(int configId) {
		return GetServiceResult(() -> {
			return ConfigService.SoftRemoveConfigEntityById(configId);
		}, log);
	}

	// @PreAuthorize("hasAuthority('E50A0E97-1107-44D9-93C0-CD75120DDCF2')")
	// @PermissionAnnotation(MenuName = "配置管理", PermissionName = "加载配置属性列表",
	// Url="/ConfigManager/LoadPropertyList",
	// DefaultRoleId = RoleConstants.AdminRoleId, Order = 4, IsPage = false,
	// ResultType = kc.enums.ResultType.JsonResult, AuthorityId =
	// "E50A0E97-1107-44D9-93C0-CD75120DDCF2")
	@GetMapping("/LoadPropertyList")
	public @ResponseBody List<ConfigAttributeDTO> LoadPropertyList(int configId) {
		List<ConfigAttributeDTO> result = ConfigService.GetConfigAttributesByConfigId(configId);
		return result;
	}

	@GetMapping("/GetPropertyForm")
	public String GetPropertyForm(int cId, int pId, ModelMap model) {
		ConfigAttributeDTO vm = new ConfigAttributeDTO();
		if (pId != 0) {
			vm = ConfigService.GetPropertyById(pId);
		}
		vm.setConfigId(cId);

		model.addAttribute("ConfigAttribute", vm);

		return "ConfigManager/_configAttributeForm";
	}

	// @PreAuthorize("hasAuthority('1557E240-6D84-4AB5-ABC6-7F54F3EBC9C2')")
	@PermissionAnnotation(MenuName = "配置管理", PermissionName = "保存配置属性", Url = "/ConfigManager/SaveConfigAttribute", 
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 4, IsPage = false, 
			ResultType = ResultType.JsonResult, AuthorityId = "1557E240-6D84-4AB5-ABC6-7F54F3EBC9C2")
	@PostMapping("/SaveConfigAttribute")
	public @ResponseBody ServiceResult<Boolean> SaveConfigAttribute(ConfigAttributeDTO model) {
		return GetServiceResult(() -> {
			return ConfigService.SaveConfigAttribute(model);
		}, log);
	}

	@PreAuthorize("hasAuthority('8542C34A-C616-4534-A17F-615B873C5A46')")
	@PermissionAnnotation(MenuName = "配置管理", PermissionName = "删除配置属性", Url = "/ConfigManager/RemoveConfigAttribute",
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 5, IsPage = false, 
		ResultType = ResultType.JsonResult, AuthorityId = "8542C34A-C616-4534-A17F-615B873C5A46")
	@GetMapping("/RemoveConfigAttribute")
	public @ResponseBody ServiceResult<Boolean> RemoveConfigAttribute(int configId) {
		return GetServiceResult(() -> {
			return ConfigService.SoftRemoveConfigEntityById(configId);
		}, log);
	}
}
