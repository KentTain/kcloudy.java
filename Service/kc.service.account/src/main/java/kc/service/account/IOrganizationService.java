package kc.service.account;

import java.util.List;

import kc.dto.account.OrganizationDTO;
import kc.dto.search.TreeNodeNameExistsDTO;

public interface IOrganizationService {
	List<OrganizationDTO> findRootOrganizationsByName(String name);
	List<OrganizationDTO> findOrganizationsByUserId(String userId);
	
	OrganizationDTO getOrganizationById(int id);
	
	boolean saveOrganization(OrganizationDTO data);
	
	boolean removeOrganization(int id);
	
	boolean existOrganizationName(TreeNodeNameExistsDTO search);
}
