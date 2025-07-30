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

import kc.dataaccess.account.IMenuNodeRepository;
import kc.dto.account.MenuNodeDTO;
import kc.dto.account.MenuNodeSimpleDTO;
import kc.mapping.CycleAvoidingMappingContext;
import kc.mapping.account.MenuNodeMapping;
import kc.model.account.MenuNode;
import kc.model.account.Role;

@Service
@lombok.extern.slf4j.Slf4j
public class MenuNodeService extends ServiceBase implements IMenuNodeService{
	private final IMenuNodeRepository _menuNodeRepository;

	private final MenuNodeMapping _menuNodeMapping;

	@Autowired
	public MenuNodeService(IGlobalConfigApiService globalConfigApiService,
						   IMenuNodeRepository _menuNodeRepository,
						   MenuNodeMapping _menuNodeMapping) {
		super(globalConfigApiService);
		this._menuNodeRepository = _menuNodeRepository;
		this._menuNodeMapping = _menuNodeMapping;
	}

	@Override
	public List<MenuNodeDTO> GetRootMenusByName(String name)
    {
		List<MenuNode> data = _menuNodeRepository.findAllTreeNodesWithNestParentAndChildByName(MenuNode.class, name);
        return _menuNodeMapping.from(data, new CycleAvoidingMappingContext());
    }
	
	@Override
	public List<MenuNodeSimpleDTO> GetRootMenuTrees()
    {
		List<MenuNode> data = _menuNodeRepository.findAllTreeNodesWithNestParentAndChildByName(MenuNode.class, null);
        return _menuNodeMapping.simpleFrom(data, new CycleAvoidingMappingContext());
    }
	
	@Override
	public List<MenuNodeSimpleDTO> GetUserMenusByRoleIds(List<String> roleIds)
	{
		List<MenuNode> data = _menuNodeRepository.findMenusByRoleIds(roleIds);
		return _menuNodeMapping.simpleFrom(data, new CycleAvoidingMappingContext());
	}
	
	@Override
	public MenuNodeDTO GetMenuNodeById(int id)
    {
		MenuNode data = _menuNodeRepository.findById(id);
        return _menuNodeMapping.from(data, new CycleAvoidingMappingContext());
    }
	@Override
	public MenuNodeDTO GetDetailMenuById(int id)
    {
		MenuNode data = _menuNodeRepository.GetDetailMenuById(id);
        return _menuNodeMapping.from(data, new CycleAvoidingMappingContext());
    }
	@Override
	public boolean SaveMenuNode(MenuNodeDTO data)
    {
		int id = data.getId();
        String name = data.getText();
        int parnetId = data.getParentId();
        
		MenuNode model = _menuNodeMapping.to(data, new CycleAvoidingMappingContext());
		MenuNode parent = _menuNodeRepository.findById(parnetId);
		model.setParentNode(parent);
        
        boolean exist = ExistMenuNodeName(id, name);
        if(exist)
        	throw new IllegalArgumentException("名称【" + name + "】已存在,请重新输入！");
        try {
        	if(model.getId() != 0){
        		_menuNodeRepository.save(model);
                _menuNodeRepository.SetTreeCodeWithNestChild(model);
                _menuNodeRepository.flush();
        	} else {
        		_menuNodeRepository.save(model);
        		model.setTreeCode(String.valueOf(model.getId()));
        		model.setLeaf(true);
        		model.setLevel(parent.getLevel() + 1);
        		_menuNodeRepository.save(model);
        		_menuNodeRepository.flush();
        	}
        	
            return true;
        }
        catch(Exception ex)
        {
        	log.error(ex.getMessage(), ex);
        	return false;
        }
    }
	@Override
	public boolean RemoveMenuNode(int id)
    {
		MenuNode model = _menuNodeRepository.findById(id);
		model.setDeleted(true);
        return _menuNodeRepository.saveAndFlush(model) != null;
    }
	@Override
	public boolean ExistMenuNodeName(int id, String name)
    {
		if (id == 0)
        {
            return _menuNodeRepository.existsByName(name);
        }
        else
        {
        	return _menuNodeRepository.existsByIdAndName(id, name);
        }
    }
	@Override
	@Transactional
	public boolean UpdateRoleInMenu(int id, List<String> roleIds, String operatorId, String operatorName)
	{
		//MenuNode menu = entityManager.find(MenuNode.class, id);
		MenuNode menu = entityManager.createQuery("from MenuNode m LEFT OUTER JOIN FETCH m.roles r where m.id = :id", MenuNode.class)
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
        
        List<Role> allr = entityManager.createQuery("from Role r LEFT OUTER JOIN FETCH r.menuNodes m where r.id in (:ids)", Role.class)
        		.setParameter("ids", combinedList)
        		.getResultList();
        List<Role> delr = allr.stream()
                .filter(m -> delList.contains(m.getId()))
                .collect(Collectors.toList());
        for (Role role : delr)
        {
        	role.getMenuNodes().remove(menu);
        	if (delList.contains(role.getId()))
        		menu.getRoles().remove(role);
        }
        List<Role> addr = allr.stream()
                .filter(m -> addList.contains(m.getId()))
                .collect(Collectors.toList());
        for (Role role : addr)
        {
        	role.getMenuNodes().add(menu);
            menu.getRoles().add(role);
        }

    	entityManager.merge(menu);
    	entityManager.flush();
		
		return true;
	}
}
