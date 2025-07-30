package kc.service.codegenerate;

import kc.dataaccess.codegenerate.IModelCategoryRepository;
import kc.dto.codegenerate.ModelCategoryDTO;
import kc.dto.codegenerate.ModelCategoryNameExistsDTO;
import kc.enums.codegenerate.ModelType;
import kc.framework.extension.StringExtensions;
import kc.mapping.CycleAvoidingMappingContext;
import kc.mapping.codegenerate.ModelCategoryMapping;
import kc.model.codegenerate.ModelCategory;
import kc.service.base.ServiceBase;
import kc.service.util.TreeNodeUtil;
import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@lombok.extern.slf4j.Slf4j
public class ModelCategoryService extends ServiceBase implements IModelCategoryService {
    private final IModelCategoryRepository _categoryRepository;
    private final ModelCategoryMapping _categoryMapping;

    public ModelCategoryService(IGlobalConfigApiService globalConfigApiService,
                                IModelCategoryRepository _categoryRepository,
                                ModelCategoryMapping _categoryMapping) {
        super(globalConfigApiService);
        this._categoryRepository = _categoryRepository;
        this._categoryMapping = _categoryMapping;
    }

    @Override
    public List<ModelCategoryDTO> findRootModelCategoriesByName(String name) {
        List<ModelCategory> data = _categoryRepository.findAllTreeNodesWithNestParentAndChildByName(ModelCategory.class, name);
        return _categoryMapping.from(data, new CycleAvoidingMappingContext());
    }

    @Override
    public List<ModelCategoryDTO> findRootModelCategoriesByName(ModelType type, String name) {
        List<ModelCategory> data = _categoryRepository.findAll(new Specification<ModelCategory>() {
            private static final long serialVersionUID = 924729964744553669L;

            @Override
            public Predicate toPredicate(Root<ModelCategory> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (null != type) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("modelType"), type.getIndex())));
                }
                if (!StringExtensions.isNullOrEmpty(name)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + name + "%")));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });

        //根据模型类型，将List结构转换为Tree结构
        List<ModelCategoryDTO> allTreeNodes = _categoryMapping.from(data, new CycleAvoidingMappingContext());
        List<ModelCategoryDTO> result = allTreeNodes.stream().filter(m -> m.getParentId() == null).collect(Collectors.toList());
        for (ModelCategoryDTO level1 : result) {
            java.util.function.Predicate<ModelCategoryDTO> predicate = m -> m.getModelType() == type;
            TreeNodeUtil.NestTreeNodeDTO(level1, allTreeNodes, null == type ? null : predicate );
        }
        return result;
    }

    @Override
    public List<ModelCategoryDTO> findByModelTypeAndApplicationId(String applicationId, ModelType modelType, String name) {
        List<ModelCategory> data = _categoryRepository.findAllByModelTypeAndApplicationId(applicationId, modelType.getIndex(), name);

        List<ModelCategory> result = new ArrayList<>();
        for (ModelCategory org : data) {
            String treeCode = org.getTreeCode();
            List<ModelCategory> tree = _categoryRepository.findTreeNodesWithNestParentAndChildByTreeCode(ModelCategory.class, treeCode);
            result.addAll(tree);
        }

        return _categoryMapping.from(result, new CycleAvoidingMappingContext());
    }

    @Override
    public ModelCategoryDTO getModelCategoryById(int id) {
        ModelCategory data = _categoryRepository.findWithParentById(id);
        return _categoryMapping.from(data, new CycleAvoidingMappingContext());
    }

    @Override
    public boolean saveModelCategory(ModelCategoryDTO data) {
        int id = data.getId();
        int pId = data.getParentId();
        String name = data.getText();
        ModelCategoryNameExistsDTO search = ModelCategoryNameExistsDTO.builder()
                .id(id)
                .pId(pId)
                .name(name)
                .modelType(data.getModelType())
                .build();
        boolean exist = existsModelCategoryName(search);
        if (exist)
            throw new IllegalArgumentException("名称【" + name + "】已存在,请重新输入！");

        ModelCategory parent = _categoryRepository.findWithParentById(pId);
        ModelCategory model = _categoryMapping.to(data, new CycleAvoidingMappingContext());
        //新增树结构时，先保存生成Id后，再更新TreeCode（树的层级结构）、Level、Leaf字段
        model.setParentNode(parent);
        if (model.getId() == 0) {
            model.setLeaf(true);
            _categoryRepository.saveAndFlush(model);
        }

        if (null != parent) {
            model.setLevel(parent.getLevel() + 1);
            model.setTreeCode(parent.getTreeCode() + _categoryRepository.getTreeCodeSplitIdWithChar() + model.getId());
        } else {
            model.setLevel(1);
            model.setTreeCode(String.valueOf(model.getId()));
        }

        _categoryRepository.saveAndFlush(model);
        return true;
    }

    @Override
    public boolean removeModelCategory(int id) {
        ModelCategory model = _categoryRepository.findWithParentById(id);
        model.setDeleted(true);
        _categoryRepository.saveAndFlush(model);
        return true;
    }

    @Override
    public boolean existsModelCategoryName(ModelCategoryNameExistsDTO search) {
        long count = _categoryRepository.count((Specification<ModelCategory>) (root, query, criteriaBuilder) -> {
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
            if (null != search.getModelType()) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("modelType"), search.getModelType().getIndex())));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

        return count > 0;
    }
}
