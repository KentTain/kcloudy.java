package kc.service.webapiservice;

import java.util.List;
import java.util.UUID;

import kc.dto.PaginatedBaseDTO;
import kc.dto.account.*;
import kc.dto.search.RootOrgWithUsersPaginatedSearchDTO;
import kc.dto.search.RootOrgWithUsersSearchDTO;
import kc.service.base.ServiceResult;

public interface IAccountApiService {
	ServiceResult<Boolean> ChangePassword(String userId, String currentPassword, String newPassword);
	ServiceResult<Boolean> ChangeMailPhone(String userId, String email, String phone);
	
	List<MenuNodeSimpleDTO> LoadUserMenusByRoleIds(List<String> roleIds);
	List<PermissionSimpleDTO> LoadUserPermissionsByRoleIds(List<String> roleIds);

	Boolean SavePermissionsAsync(List<PermissionDTO> permissions, UUID appGuid);
	Boolean SaveMenusAsync(List<MenuNodeDTO> menus, UUID appGuid);

	List<OrganizationSimpleDTO> LoadOrgTreesWithUsers();
	List<OrganizationSimpleDTO> LoadOrganizationsWithUsersByRoleIds(List<String> roleIds);
	List<OrganizationSimpleDTO> LoadOrganizationsWithUsersByUserId(String userId);
	List<OrganizationSimpleDTO> LoadOrgsWithUsersByRoleIdsAndOrgids(RootOrgWithUsersSearchDTO searchModel);
	PaginatedBaseDTO<OrganizationSimpleDTO> LoadPaginatedOrganizationsWithUsersByFilter(RootOrgWithUsersPaginatedSearchDTO searchModel);
}
