package kc.web.controller;

import kc.dto.PaginatedBaseDTO;
import kc.dto.dict.DictTypeDTO;
import kc.dto.dict.DictValueDTO;
import kc.enums.ResultType;
import kc.framework.GlobalConfig;
import kc.framework.enums.ConfigType;
import kc.framework.tenant.RoleConstants;
import kc.framework.tenant.TenantConstant;
import kc.service.base.ServiceResult;
import kc.service.dict.IDictionaryService;
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
import java.util.List;

/**
 * 二级级菜单：字典管理/字典管理
 */
@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping("/Dictionary")
@MenuAnnotation(ParentMenuName = "字典管理", MenuName = "字典管理", Url = "/Dictionary/Index",
		Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
		SmallIcon = "fa fa-list-alt", AuthorityId = "9CBD7FE5-E00E-4784-A232-CE7080B19B80",
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 2)
public class DictionaryController extends WebBaseController {
	@Autowired
	private IDictionaryService _dictionaryService;

	@PreAuthorize("hasAuthority('9CBD7FE5-E00E-4784-A232-CE7080B19B80')")
	@PermissionAnnotation(MenuName = "字典管理", PermissionName = "字典管理", Url = "/Dictionary/Index",
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
			ResultType = ResultType.ActionResult, AuthorityId = "9CBD7FE5-E00E-4784-A232-CE7080B19B80")
	@GetMapping("/index")
	public String Index(ModelMap model) {
		model.addAttribute("resWebDomain", GlobalConfig.ResWebDomain);
		// 字典管理-保存字典类型
		model.addAttribute("canSave", hasAuthority("B88E5DD9-9C29-49FC-8CF6-2C401D86C585"));
		// 字典管理-删除字典类型
		model.addAttribute("canRemove", hasAuthority("78F9C5F1-519E-4E74-A34B-341AB8C0BC1E"));
		// 字典管理-保存字典值
		model.addAttribute("canSaveAtt", hasAuthority("C607C002-D378-4D48-A445-DDDB47379550"));
		// 字典管理-删除字典值
		model.addAttribute("canRemoveAtt", hasAuthority("2EE04641-EE06-4E8B-AA63-BE05CA11F836"));
		return "Dictionary/index";
	}

	@GetMapping("/LoadDictTypeList")
	public @ResponseBody List<DictTypeDTO> LoadDictTypeList(String name) {
		List<DictTypeDTO> result = new ArrayList<>();
		DictTypeDTO allType = new DictTypeDTO();
		allType.setId(0);
		allType.setName("所有字典类型");
		result.add(allType);
		List<DictTypeDTO> data = _dictionaryService.findDictTypeList(name);
		if (data != null && data.size() > 0)
			result.addAll(data);

		return result;
	}

	@PreAuthorize("hasAuthority('B88E5DD9-9C29-49FC-8CF6-2C401D86C585')")
	@PermissionAnnotation(MenuName = "字典管理", PermissionName = "保存字典类型", Url = "/Dictionary/SaveDictType",
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsPage = false, 
			ResultType = ResultType.JsonResult, AuthorityId = "B88E5DD9-9C29-49FC-8CF6-2C401D86C585")
	@PostMapping("/SaveDictType")
	public @ResponseBody ServiceResult<Boolean> SaveDictType(List<DictTypeDTO> models) {
		return GetServiceResult(() -> {
			return _dictionaryService.saveDictType(models);
		}, log);
	}

	@PreAuthorize("hasAuthority('78F9C5F1-519E-4E74-A34B-341AB8C0BC1E')")
	@PermissionAnnotation(MenuName = "字典管理", PermissionName = "删除字典类型", Url = "/Dictionary/RemoveDictType",
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsPage = false, 
		ResultType = ResultType.JsonResult, AuthorityId = "78F9C5F1-519E-4E74-A34B-341AB8C0BC1E")
	@GetMapping("/RemoveDictType")
	public @ResponseBody ServiceResult<Boolean> RemoveDictType(int id) {
		return GetServiceResult(() -> {
			return _dictionaryService.removeDictType(id);
		}, log);
	}

	@GetMapping("/LoadDictValueList")
	public @ResponseBody
	PaginatedBaseDTO<DictValueDTO> LoadDictValueList(int page, int rows, String name, Integer typeId) {
		return _dictionaryService.findPaginatedDictValuesByFilter(page,rows,name, typeId);
	}


	@PreAuthorize("hasAuthority('C607C002-D378-4D48-A445-DDDB47379550')")
	@PermissionAnnotation(MenuName = "字典管理", PermissionName = "保存数据字典值", Url = "/Dictionary/SaveDictValue",
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 5, IsPage = false,
			ResultType = ResultType.JsonResult, AuthorityId = "C607C002-D378-4D48-A445-DDDB47379550")
	@PostMapping("/SaveDictValue")
	public @ResponseBody ServiceResult<Boolean> SaveDictValue(List<DictValueDTO> models, Integer typeId) {
		return GetServiceResult(() -> {
			return _dictionaryService.saveDictValue(models, typeId, CurrentUserId(), CurrentUserDisplayName());
		}, log);
	}

	@PreAuthorize("hasAuthority('2EE04641-EE06-4E8B-AA63-BE05CA11F836')")
	@PermissionAnnotation(MenuName = "字典管理", PermissionName = "删除数据字典值", Url = "/Dictionary/RemoveDictValue",
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 6, IsPage = false,
		ResultType = ResultType.JsonResult, AuthorityId = "2EE04641-EE06-4E8B-AA63-BE05CA11F836")
	@GetMapping("/RemoveDictValue")
	public @ResponseBody ServiceResult<Boolean> RemoveDictValue(int configId) {
		return GetServiceResult(() -> {
			return _dictionaryService.removeDictValue(configId);
		}, log);
	}
}
