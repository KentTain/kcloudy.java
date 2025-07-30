package kc.service.account;

import java.util.List;

import kc.dto.PaginatedBaseDTO;
import kc.dto.account.RoleDTO;
import kc.dto.account.RoleSimpleDTO;

public interface IRoleService {
	List<RoleSimpleDTO> GetAllSimpleRoles();
	List<RoleDTO> GetAllDetailRoles();
	List<RoleDTO> GetRolesByIds(List<String> roleIds);

	PaginatedBaseDTO<RoleDTO> GetPagenatedRoleList(int pageIndex, int pageSize, String displayName);
	
	RoleDTO findById(String id);
	RoleDTO GetDetailRoleByRoleId(String roleId);


	boolean SaveRole(RoleDTO data);
	
	boolean RemoveRoleById(String id);
	
	boolean ExistRoleName(String id, String name);

	boolean UpdateMenuInRole(String roleId, List<Integer> menuIds, String operatorId, String operatorName);

	boolean UpdatePermissionInRole(String roleId, List<Integer> permissionIds, String operatorId, String operatorName);

	boolean UpdateUserInRole(String roleId, List<String> addList, List<String> delList, String operatorId,
			String operatorName);

	
}
