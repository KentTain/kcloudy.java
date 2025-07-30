package kc.service.account;

import java.util.List;

import kc.dto.account.MenuNodeDTO;
import kc.dto.account.MenuNodeSimpleDTO;

public interface IMenuNodeService {
	List<MenuNodeDTO> GetRootMenusByName(String name);
	List<MenuNodeSimpleDTO> GetRootMenuTrees();
	List<MenuNodeSimpleDTO> GetUserMenusByRoleIds(List<String> roleIds);
	
	MenuNodeDTO GetMenuNodeById(int id);
	
	MenuNodeDTO GetDetailMenuById(int id);
	
	boolean SaveMenuNode(MenuNodeDTO data);
	
	boolean RemoveMenuNode(int id);
	
	boolean ExistMenuNodeName(int id, String name);
	
	boolean UpdateRoleInMenu(int id, List<String> roleIds, String operatorId, String operatorName);
	
	
}
