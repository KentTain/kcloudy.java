package kc.service.account;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kc.service.base.ServiceBase;
import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kc.dataaccess.account.IPermissionRepository;
import kc.dto.account.PermissionDTO;
import kc.dto.account.PermissionSimpleDTO;
import kc.mapping.CycleAvoidingMappingContext;
import kc.mapping.account.PermissionMapping;
import kc.model.account.Permission;
import kc.model.account.Role;

@Service
@lombok.extern.slf4j.Slf4j
public class PermissionService extends ServiceBase implements IPermissionService{
	private IPermissionRepository _permissionRepository;

	private PermissionMapping _permissionMapping;

	@Autowired
	public PermissionService(IGlobalConfigApiService globalConfigApiService,
							 IPermissionRepository _permissionRepository,
							 PermissionMapping _permissionMapping) {
		super(globalConfigApiService);
		this._permissionRepository = _permissionRepository;
		this._permissionMapping = _permissionMapping;
	}

	@Override
	public List<PermissionDTO> GetRootPermissionsByName(String name)
    {
		List<Permission> data = _permissionRepository.findAllTreeNodesWithNestParentAndChildByName(Permission.class, name);

        return _permissionMapping.from(data, new CycleAvoidingMappingContext());
    }
	
	@Override
	public List<PermissionSimpleDTO> GetRootPermissionTrees()
    {
		List<Permission> data = _permissionRepository.findAllTreeNodesWithNestParentAndChildByName(Permission.class, null);
        return _permissionMapping.simpleFrom(data, new CycleAvoidingMappingContext());
    }
	
	@Override
	public List<PermissionSimpleDTO> GetUserPermissionsByRoleIds(List<String> roleIds)
	{
		List<Permission> data = _permissionRepository.findPermissionsByRoleIds(roleIds);
		return _permissionMapping.simpleFrom(data, new CycleAvoidingMappingContext());
	}
	@Override
	public PermissionDTO GetPermissionById(int id)
    {
		Permission data = _permissionRepository.findById(id);
        return _permissionMapping.from(data, new CycleAvoidingMappingContext());
    }
	@Override
	public PermissionDTO GetDetailPermissionById(int id)
    {
		Permission data = _permissionRepository.GetDetailPermissionById(id);
        return _permissionMapping.from(data, new CycleAvoidingMappingContext());
    }
	@Override
	public boolean SavePermission(PermissionDTO data)
    {
		int id = data.getId();
        String name = data.getText();
        Integer parnetId = data.getParentId();
        
        Permission parent = null;
		Permission model = _permissionMapping.to(data, new CycleAvoidingMappingContext());
		if(parnetId != null) {
			parent = _permissionRepository.findById((int)parnetId);
			model.setParentNode(parent);
		}
		
        boolean exist = ExistPermissionName(id, name);
        if(exist)
        	throw new IllegalArgumentException("名称【" + name + "】已存在,请重新输入！");
        try {
        	if(model.getId() != 0){
        		_permissionRepository.save(model);
                _permissionRepository.SetTreeCodeWithNestChild(model);
                _permissionRepository.flush();
        	} else {
        		_permissionRepository.save(model);
        		model.setTreeCode(String.valueOf(model.getId()));
        		model.setLeaf(true);
        		if(parent != null)
        			model.setLevel(parent.getLevel() + 1);
        		_permissionRepository.save(model);
        		_permissionRepository.flush();
        	}
        	
            return true;
        }
        catch(Exception ex)
        {
        	log.error(ex.getMessage(), ex);
        	return false;
        }
    }
	
	public boolean SaveExtendFields() {
		List<Permission> data = _permissionRepository.findAll();
		_permissionRepository.saveAllTreeNodeWithExtensionFields(data);
		return true;
	}
	@Override
	public boolean RemovePermission(int id)
    {
		Permission model = _permissionRepository.findById(id);
		model.setDeleted(true);
        return _permissionRepository.saveAndFlush(model) != null;
    }
	@Override
	public boolean ExistPermissionName(int id, String name)
    {
		if (id == 0)
        {
            return _permissionRepository.existsByName(name);
        }
        else
        {
        	return _permissionRepository.existsByIdAndName(id, name);
        }
    }
	@Override
	@Transactional
	public boolean UpdateRoleInPermission(int id, List<String> roleIds, String operatorId, String operatorName)
	{
		//Permission menu = entityManager.find(Permission.class, id);
		Permission menu = entityManager.createQuery("from Permission m LEFT OUTER JOIN FETCH m.roles r where m.id = :id", Permission.class)
        		.setParameter("id", id)
        		.getSingleResult();
		
		if (menu == null)
            throw new IllegalArgumentException(String.format("系统不存在菜单Id=%s的菜单。", id));

        if (roleIds == null)
            throw new IllegalArgumentException("传入参数--roleIds：菜单Id列表为空。");
        
        List<String> dbIdList = menu.getRoles().stream().map(role -> role.getId().toString()).collect(Collectors.toList());
        List<String> delList = dbIdList.stream().filter(m -> !roleIds.contains(m)).collect(Collectors.toList());
        List<String> addList = roleIds.stream().filter(m -> !dbIdList.contains(m)).collect(Collectors.toList());
        
        if (addList.size() == 0 && delList.size() == 0)
        	return true;
        
        List<String> combinedList = java.util.stream.Stream.of(delList, addList)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());
        
        List<Role> allr = entityManager.createQuery("from Role r LEFT OUTER JOIN FETCH r.permissions m where r.id in (:ids)", Role.class)
        		.setParameter("ids", combinedList)
        		.getResultList();
        List<Role> delr = allr.stream()
                .filter(m -> delList.contains(m.getId()))
                .collect(Collectors.toList());
        for (Role role : delr)
        {
        	role.getPermissions().remove(menu);
        	if (delList.contains(role.getId()))
        		menu.getRoles().remove(role);
        }
        List<Role> addr = allr.stream()
                .filter(m -> addList.contains(m.getId()))
                .collect(Collectors.toList());
        for (Role role : addr)
        {
        	role.getPermissions().add(menu);
            menu.getRoles().add(role);
        }

    	entityManager.merge(menu);
    	entityManager.flush();
		
		return true;
	}
}
