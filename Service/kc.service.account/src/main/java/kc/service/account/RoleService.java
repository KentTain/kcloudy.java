package kc.service.account;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kc.framework.enums.BusinessType;
import kc.service.base.ServiceBase;
import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kc.dataaccess.account.IRoleRepository;
import kc.dto.PaginatedBaseDTO;
import kc.dto.account.RoleDTO;
import kc.dto.account.RoleSimpleDTO;
import kc.framework.extension.StringExtensions;
import kc.mapping.account.RoleMapping;
import kc.model.account.MenuNode;
import kc.model.account.Permission;
import kc.model.account.Role;
import kc.model.account.User;

@Service
@lombok.extern.slf4j.Slf4j
public class RoleService extends ServiceBase implements IRoleService {
    private IRoleRepository _RoleRepository;
    private RoleMapping _RoleMapping;

    @Autowired
    public RoleService(IGlobalConfigApiService globalConfigApiService,
                       IRoleRepository _RoleRepository,
                       RoleMapping _RoleMapping) {
        super(globalConfigApiService);
        this._RoleRepository = _RoleRepository;
        this._RoleMapping = _RoleMapping;
    }

    @Override
    public List<RoleDTO> GetAllDetailRoles() {
        List<Role> data = _RoleRepository.findAll();
        return _RoleMapping.from(data);
    }

    @Override
    public List<RoleSimpleDTO> GetAllSimpleRoles() {
        List<Role> data = _RoleRepository.findAll();

        return _RoleMapping.fromSimple(data);
    }

    @Override
    public List<RoleDTO> GetRolesByIds(List<String> roleIds) {
        List<Role> data = _RoleRepository.findByIdIn(roleIds);

        return _RoleMapping.from(data);
    }

    @Override
    public PaginatedBaseDTO<RoleDTO> GetPagenatedRoleList(final int pageIndex, final int pageSize, final String displayName) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Role> data = _RoleRepository.findAll(new Specification<Role>() {
            private static final long serialVersionUID = 924729964744553669L;

            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringExtensions.isNullOrEmpty(displayName)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("displayName"), "%" + displayName + "%")));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);

        long total = data.getTotalElements();
        List<RoleDTO> rows = _RoleMapping.from(data.getContent());
        return new PaginatedBaseDTO<>(pageIndex, pageSize, total, rows);
    }

    @Override
    public RoleDTO findById(final String id) {
        Role model = _RoleRepository.findById(id);
        return _RoleMapping.from(model);
    }

    @Override
    public RoleDTO GetDetailRoleByRoleId(final String roleId) {
        Role model = _RoleRepository.GetDetailRoleById(roleId);
        return _RoleMapping.from(model);
    }

    @Override
    public boolean SaveRole(RoleDTO data) {
        String id = data.getRoleId();
        String name = data.getRoleName();

        Role model = _RoleMapping.to(data);

        boolean exist = ExistRoleName(id, name);
        if (exist)
            throw new IllegalArgumentException("名称【" + name + "】已存在,请重新输入！");
        try {
            if (StringExtensions.isNullOrEmpty(model.getId())) {

                _RoleRepository.save(model);
                _RoleRepository.flush();
            } else {
                _RoleRepository.save(model);

                _RoleRepository.save(model);
                _RoleRepository.flush();
            }

            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public boolean RemoveRoleById(final String id) {
        Role model = _RoleRepository.findById(id);
        model.setDeleted(true);
        return _RoleRepository.saveAndFlush(model) != null;
    }

    @Override
    public boolean ExistRoleName(final String id, final String name) {
        if (StringExtensions.isNullOrEmpty(id)) {
            return _RoleRepository.existsByName(name);
        } else {
            return _RoleRepository.existsByIdAndName(id, name);
        }
    }

    @Override
    @Transactional
    public boolean UpdateMenuInRole(final String roleId, final List<Integer> menuIds, final String operatorId, final String operatorName) {
        Role role = entityManager.createQuery("from Role r LEFT OUTER JOIN FETCH r.menuNodes m where r.id = :id", Role.class)
                .setParameter("id", roleId)
                .getSingleResult();

        if (role == null)
            throw new IllegalArgumentException(String.format("系统不存在菜单roleId=%s的角色。", roleId));

        if (menuIds == null)
            throw new IllegalArgumentException("传入参数--menuIds：菜单Id列表为空。");

        List<Integer> dbIdList = role.getMenuNodes().stream().map(m -> m.getId()).collect(Collectors.toList());
        List<Integer> delList = dbIdList.stream().filter(m -> !menuIds.contains(m)).collect(Collectors.toList());
        List<Integer> addList = menuIds.stream().filter(m -> !dbIdList.contains(m)).collect(Collectors.toList());

        if (addList.size() == 0 && delList.size() == 0)
            return true;

        List<Integer> combinedList = java.util.stream.Stream.of(delList, addList)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());

        List<MenuNode> allr = entityManager.createQuery("from MenuNode m LEFT OUTER JOIN FETCH m.roles r where m.id in (:ids)", MenuNode.class)
                .setParameter("ids", combinedList)
                .getResultList();
        List<MenuNode> delr = allr.stream()
                .filter(m -> delList.contains(m.getId()))
                .collect(Collectors.toList());
        for (MenuNode menu : delr) {
            menu.getRoles().remove(role);
            if (delList.contains(menu.getId()))
                role.getMenuNodes().remove(menu);
        }
        List<MenuNode> addr = allr.stream()
                .filter(m -> addList.contains(m.getId()))
                .collect(Collectors.toList());
        for (MenuNode menu : addr) {
            menu.getRoles().add(role);
            role.getMenuNodes().add(menu);
        }

        entityManager.merge(role);
        entityManager.flush();

        return true;
    }

    @Override
    @Transactional
    public boolean UpdatePermissionInRole(final String roleId, final List<Integer> permissionIds, final String operatorId, final String operatorName) {
        Role role = entityManager.createQuery("from Role r LEFT OUTER JOIN FETCH r.permissions p where r.id = :id", Role.class)
                .setParameter("id", roleId)
                .getSingleResult();

        if (role == null)
            throw new IllegalArgumentException(String.format("系统不存在菜单roleId=%s的角色。", roleId));

        if (permissionIds == null)
            throw new IllegalArgumentException("传入参数--permissionIds：菜单Id列表为空。");

        List<Integer> dbIdList = role.getPermissions().stream().map(m -> m.getId()).collect(Collectors.toList());
        List<Integer> delList = dbIdList.stream().filter(m -> !permissionIds.contains(m)).collect(Collectors.toList());
        List<Integer> addList = permissionIds.stream().filter(m -> !dbIdList.contains(m)).collect(Collectors.toList());

        if (addList.size() == 0 && delList.size() == 0)
            return true;

        List<Integer> combinedList = java.util.stream.Stream.of(delList, addList)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());

        List<Permission> allr = entityManager.createQuery("from Permission p LEFT OUTER JOIN FETCH p.roles r where p.id in (:ids)", Permission.class)
                .setParameter("ids", combinedList)
                .getResultList();
        List<Permission> delr = allr.stream()
                .filter(m -> delList.contains(m.getId()))
                .collect(Collectors.toList());
        for (Permission permission : delr) {
            permission.getRoles().remove(role);
            if (delList.contains(permission.getId()))
                role.getPermissions().remove(permission);
        }
        List<Permission> addr = allr.stream()
                .filter(m -> addList.contains(m.getId()))
                .collect(Collectors.toList());
        for (Permission permission : addr) {
            permission.getRoles().add(role);
            role.getPermissions().add(permission);
        }

        entityManager.merge(role);
        entityManager.flush();

        return true;
    }

    @Override
    @Transactional
    public boolean UpdateUserInRole(final String roleId, final List<String> addList, final List<String> delList, final String operatorId, final String operatorName) {
        Role role = entityManager.createQuery("from Role r LEFT OUTER JOIN FETCH r.users u where r.id = :id", Role.class)
                .setParameter("id", roleId)
                .getSingleResult();

        if (role == null)
            throw new IllegalArgumentException(String.format("系统不存在菜单roleId=%s的角色。", roleId));

        if (addList == null && delList == null)
            throw new IllegalArgumentException("传入参数--addList和delList：用户Id列表为空。");

        if (addList.size() == 0 && delList.size() == 0)
            return true;

        List<String> combinedList = java.util.stream.Stream.of(delList, addList)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());

        List<User> allr = entityManager.createQuery("from User u LEFT OUTER JOIN FETCH u.roles r where u.id in (:ids)", User.class)
                .setParameter("ids", combinedList)
                .getResultList();
        List<User> delr = allr.stream()
                .filter(m -> delList.contains(m.getId()))
                .collect(Collectors.toList());
        for (User user : delr) {
            user.getRoles().remove(role);
            if (delList.contains(user.getId()))
                role.getUsers().remove(user);
        }
        List<User> addr = allr.stream()
                .filter(m -> addList.contains(m.getId()))
                .collect(Collectors.toList());
        for (User user : addr) {
            user.getRoles().add(role);
            role.getUsers().add(user);
        }

        entityManager.merge(role);
        entityManager.flush();

        return true;
    }
}
