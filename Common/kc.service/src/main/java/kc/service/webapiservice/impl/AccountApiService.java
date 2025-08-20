package kc.service.webapiservice.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import kc.dto.PaginatedBaseDTO;
import kc.dto.account.MenuNodeDTO;
import kc.dto.account.MenuNodeSimpleDTO;
import kc.dto.account.OrganizationSimpleDTO;
import kc.dto.account.PermissionDTO;
import kc.dto.account.PermissionSimpleDTO;
import kc.dto.search.RootOrgWithUsersPaginatedSearchDTO;
import kc.dto.search.RootOrgWithUsersSearchDTO;
import kc.framework.tenant.ApplicationConstant;
import kc.framework.util.SerializeHelper;
import kc.service.base.ServiceResult;
import kc.service.webapiservice.IAccountApiService;

@Service
@lombok.extern.slf4j.Slf4j
public class AccountApiService extends IdSrvWebApiServiceBase implements IAccountApiService {
	private final static String ServiceName = "kc.service.webapi.AccountApiService";

	@Override
	public ServiceResult<Boolean> ChangePassword(String userId, String currentPassword, String newPassword) {
		ServiceResult<Boolean> result = null;
		result = WebSendGet(new TypeReference<ServiceResult<Boolean>>() {}, ServiceName + ".ChangePasswordAsync",
				AccountApiUrl() + "AccountApi/ChangePasswordAsync?userId=" + userId + "&currentPassword="
						+ currentPassword + "&newPassword=" + newPassword,
				ApplicationConstant.AccScope, callback -> {
					return callback;
				}, failCallback -> {
					log.error(ServiceName + " throw error: " + failCallback.toString());
				}, true);

		return result;
	}

	@Override
	public ServiceResult<Boolean> ChangeMailPhone(String userId, String email, String phone) {
		ServiceResult<Boolean> result = null;
		result = WebSendGet(
				new TypeReference<ServiceResult<Boolean>>() {}, ServiceName + ".ChangeMailPhoneAsync", AccountApiUrl()
						+ "AccountApi/ChangeMailPhoneAsync?userId=" + userId + "&email=" + email + "&phone=" + phone,
				ApplicationConstant.AccScope, callback -> {
					return callback;
				}, failCallback -> {
					log.error(ServiceName + " throw error: " + failCallback.toString());
				}, true);

		return result;
	}

	@Override
	public List<MenuNodeSimpleDTO> LoadUserMenusByRoleIds(List<String> roleIds) {
		String jsonData = SerializeHelper.ToJson(roleIds);
		ServiceResult<List<MenuNodeSimpleDTO>> result = null;
		result = WebSendPost(new TypeReference<ServiceResult<List<MenuNodeSimpleDTO>>>() {},
				ServiceName + ".LoadUserMenusByRoleIds", AccountApiUrl() + "AccountApi/LoadUserMenusByRoleIds",
				ApplicationConstant.AccScope, jsonData, callback -> {
					return callback;
				}, failCallback -> {
					log.error(ServiceName + " throw error: " + failCallback.toString());
				}, true);

		if (result != null && result.isSuccess() && result.getResult() != null) {
			return result.getResult();
		}

		return null;
	}

	@Override
	public List<PermissionSimpleDTO> LoadUserPermissionsByRoleIds(List<String> roleIds) {
		String jsonData = SerializeHelper.ToJson(roleIds);
		ServiceResult<List<PermissionSimpleDTO>> result = null;
		result = WebSendPost(new TypeReference<ServiceResult<List<PermissionSimpleDTO>>>() {},
				ServiceName + ".LoadUserPermissionsByRoleIds",
				AccountApiUrl() + "AccountApi/LoadUserPermissionsByRoleIds", ApplicationConstant.AccScope, jsonData,
				callback -> {
					return callback;
				}, failCallback -> {
					log.error(ServiceName + " throw error: " + failCallback.toString());
				}, true);

		if (result != null && result.isSuccess() && result.getResult() != null) {
			return result.getResult();
		}

		return null;
	}

	@Override
	public Boolean SaveMenusAsync(List<MenuNodeDTO> menus, UUID appGuid) {
		String jsonData = SerializeHelper.ToJson(menus);
		ServiceResult<Boolean> result = null;
		result = WebSendPost(new TypeReference<ServiceResult<Boolean>>() {}, ServiceName + ".SaveMenusAsync",
				AccountApiUrl() + "AccountApi/SaveMenus?appId=" + appGuid, ApplicationConstant.AccScope, jsonData,
				callback -> {
					return callback;
				}, failCallback -> {
					log.error(ServiceName + " throw error: " + failCallback.toString());
				}, true);

		if (result != null && result.isSuccess()) {
			return result.getResult();
		}

		return false;
	}

	@Override
	public Boolean SavePermissionsAsync(List<PermissionDTO> permissions, UUID appGuid) {
		String jsonData = SerializeHelper.ToJson(permissions);
		ServiceResult<Boolean> result = null;
		result = WebSendPost(new TypeReference<ServiceResult<Boolean>>() {}, ServiceName + ".SavePermissions",
				AccountApiUrl() + "AccountApi/SavePermissions?appId=" + appGuid, ApplicationConstant.AccScope, jsonData,
				callback -> {
					return callback;
				}, failCallback -> {
					log.error(ServiceName + " throw error: " + failCallback.toString());
				}, true);

		if (result != null && result.isSuccess()) {
			return result.getResult();
		}

		return false;
	}

	@Override
	public List<OrganizationSimpleDTO> LoadOrgTreesWithUsers() {
		ServiceResult<List<OrganizationSimpleDTO>> result = null;
		result = WebSendGet(new TypeReference<ServiceResult<List<OrganizationSimpleDTO>>>() {},
				ServiceName + ".LoadOrgTreesWithUsers", AccountApiUrl() + "AccountApi/LoadOrgTreesWithUsers",
				ApplicationConstant.AccScope, callback -> {
					return callback;
				}, failCallback -> {
					log.error(ServiceName + " throw error: " + failCallback.toString());
				}, true);

		if (result != null && result.isSuccess() && result.getResult() != null) {
			return result.getResult();
		}

		return null;
	}

	@Override
	public List<OrganizationSimpleDTO> LoadOrganizationsWithUsersByRoleIds(List<String> roleIds) {
		String jsonData = SerializeHelper.ToJson(roleIds);
		ServiceResult<List<OrganizationSimpleDTO>> result = null;
		result = WebSendPost(new TypeReference<ServiceResult<List<OrganizationSimpleDTO>>>() {},
				ServiceName + ".LoadOrganizationsWithUsersByRoleIds",
				AccountApiUrl() + "AccountApi/LoadOrganizationsWithUsersByRoleIds", ApplicationConstant.AccScope,
				jsonData, callback -> {
					return callback;
				}, failCallback -> {
					log.error(ServiceName + " throw error: " + failCallback.toString());
				}, true);

		if (result != null && result.isSuccess() && result.getResult() != null) {
			return result.getResult();
		}

		return null;
	}

	@Override
	public List<OrganizationSimpleDTO> LoadOrganizationsWithUsersByUserId(String userId) {
		ServiceResult<List<OrganizationSimpleDTO>> result = null;
		result = WebSendGet(new TypeReference<ServiceResult<List<OrganizationSimpleDTO>>>() {},
				ServiceName + ".LoadOrganizationsWithUsersByUserId",
				AccountApiUrl() + "AccountApi/LoadOrganizationsWithUsersByUserId?userId=" + userId,
				ApplicationConstant.AccScope, callback -> {
					return callback;
				}, failCallback -> {
					log.error(ServiceName + " throw error: " + failCallback.toString());
				}, true);

		if (result != null && result.isSuccess() && result.getResult() != null) {
			return result.getResult();
		}

		return null;
	}

	@Override
	public List<OrganizationSimpleDTO> LoadOrgsWithUsersByRoleIdsAndOrgids(RootOrgWithUsersSearchDTO searchModel) {
		String jsonData = SerializeHelper.ToJson(searchModel);
		ServiceResult<List<OrganizationSimpleDTO>> result = null;
		result = WebSendPost(new TypeReference<ServiceResult<List<OrganizationSimpleDTO>>>() {},
				ServiceName + ".LoadOrgsWithUsersByRoleIdsAndOrgids",
				AccountApiUrl() + "AccountApi/LoadOrgsWithUsersByRoleIdsAndOrgids", ApplicationConstant.AccScope,
				jsonData, callback -> {
					return callback;
				}, failCallback -> {
					log.error(ServiceName + " throw error: " + failCallback.toString());
				}, true);

		if (result != null && result.isSuccess() && result.getResult() != null) {
			return result.getResult();
		}

		return null;
	}

	@Override
	public PaginatedBaseDTO<OrganizationSimpleDTO> LoadPaginatedOrganizationsWithUsersByFilter(
			RootOrgWithUsersPaginatedSearchDTO searchModel) {
		String jsonData = SerializeHelper.ToJson(searchModel);
		ServiceResult<PaginatedBaseDTO<OrganizationSimpleDTO>> result = null;
		result = WebSendPost(new TypeReference<ServiceResult<PaginatedBaseDTO<OrganizationSimpleDTO>>>() {},
				ServiceName + ".GetPaginatedRootOrganizationsWithUsersByFilter",
				AccountApiUrl() + "AccountApi/GetPaginatedRootOrganizationsWithUsersByFilter",
				ApplicationConstant.AccScope, jsonData, callback -> {
					return callback;
				}, failCallback -> {
					log.error(ServiceName + " throw error: " + failCallback.toString());
				}, true);

		if (result != null && result.isSuccess() && result.getResult() != null) {
			return result.getResult();
		}

		return null;
	}

}
