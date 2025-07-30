package kc.service.account;

import kc.dataaccess.account.IOrganizationRepository;
import kc.dto.account.OrganizationDTO;
import kc.dto.search.TreeNodeNameExistsDTO;
import kc.framework.extension.StringExtensions;
import kc.mapping.CycleAvoidingMappingContext;
import kc.mapping.account.OrganizationMapping;
import kc.model.account.Organization;
import kc.service.base.ServiceBase;
import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@lombok.extern.slf4j.Slf4j
public class OrganizationService extends ServiceBase implements IOrganizationService {
    private final IOrganizationRepository _organizationRepository;
    private final OrganizationMapping _organizationMapping;

    public OrganizationService(IGlobalConfigApiService globalConfigApiService,
                               IOrganizationRepository _organizationRepository,
                               OrganizationMapping _organizationMapping) {
        super(globalConfigApiService);
        this._organizationRepository = _organizationRepository;
        this._organizationMapping = _organizationMapping;
    }

    @Override
    public List<OrganizationDTO> findRootOrganizationsByName(String name) {
        List<Organization> data = _organizationRepository.findAllTreeNodesWithNestParentAndChildByName(Organization.class, name);
        return _organizationMapping.from(data, new CycleAvoidingMappingContext());
    }

    @Override
    public List<OrganizationDTO> findOrganizationsByUserId(String userId) {
        List<Organization> data = _organizationRepository.findWithUsersByUserId(userId);

        List<Organization> result = new ArrayList<Organization>();
        for (Organization org : data) {
            String treeCode = org.getTreeCode();
            List<Organization> tree = _organizationRepository.findTreeNodesWithNestParentAndChildByTreeCode(Organization.class, treeCode);
            result.addAll(tree);
        }

        return _organizationMapping.from(result, new CycleAvoidingMappingContext());
    }

    @Override
    public OrganizationDTO getOrganizationById(int id) {
        Organization data = _organizationRepository.findWithParentById(id);
        return _organizationMapping.from(data, new CycleAvoidingMappingContext());
    }

    @Override
    public boolean saveOrganization(OrganizationDTO data) {
        int id = data.getId();
        int pId = data.getParentId();
        String name = data.getText();
        TreeNodeNameExistsDTO search = new TreeNodeNameExistsDTO(id, pId, name);
        boolean exist = existOrganizationName(search);
        if (exist)
            throw new IllegalArgumentException("名称【" + name + "】已存在,请重新输入！");

        Organization model = _organizationMapping.to(data, new CycleAvoidingMappingContext());
        Organization parent = _organizationRepository.findWithParentById(pId);
        //新增树结构时，先保存生成Id后，再更新TreeCode（树的层级结构）、Level、Leaf字段
        model.setParentNode(parent);
        if (model.getId() == 0) {
            model.setLeaf(true);
            _organizationRepository.saveAndFlush(model);
        }

        if (null != parent) {
            model.setLevel(parent.getLevel() + 1);
            model.setTreeCode(parent.getTreeCode() + _organizationRepository.getTreeCodeSplitIdWithChar() + model.getId());
        } else {
            model.setLevel(1);
            model.setTreeCode(String.valueOf(model.getId()));
        }

        _organizationRepository.saveAndFlush(model);
        return true;
    }

    @Override
    public boolean removeOrganization(int id) {
        Organization model = _organizationRepository.findWithParentById(id);
        model.setDeleted(true);
        _organizationRepository.saveAndFlush(model);
        return true;
    }

    @Override
    public boolean existOrganizationName(TreeNodeNameExistsDTO search) {
        long count = _organizationRepository.count((Specification<Organization>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (null != search.getPId() && 0 != search.getPId()) {
                root.join("parentNode", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(root.get("parentNode").get("id"), search.getPId()));
            }
            if (null != search.getId() && 0 != search.getId()) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.notEqual(root.get("id"), search.getId())));
            }
            if (!StringExtensions.isNullOrEmpty(search.getName())) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + search.getName() + "%")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

        return count > 0;
    }
}
