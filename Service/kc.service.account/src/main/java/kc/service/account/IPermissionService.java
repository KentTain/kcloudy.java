package kc.service.account;

import java.util.List;

import kc.dto.account.PermissionDTO;
import kc.dto.account.PermissionSimpleDTO;

public interface IPermissionService {
	List<PermissionDTO> GetRootPermissionsByName(String name);
	List<PermissionSimpleDTO> GetRootPermissionTrees();
	List<PermissionSimpleDTO> GetUserPermissionsByRoleIds(List<String> roleIds);
	
	PermissionDTO GetPermissionById(int id);
	
	PermissionDTO GetDetailPermissionById(int id);
	
	boolean SavePermission(PermissionDTO data);
	boolean SaveExtendFields();
	
	boolean RemovePermission(int id);
	
	boolean ExistPermissionName(int id, String name);
	
	boolean UpdateRoleInPermission(int id, List<String> roleIds, String operatorId, String operatorName);
	
	
}
