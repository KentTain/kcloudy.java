package kc.service.offering;

import kc.dataaccess.offering.*;
import kc.dto.PaginatedBaseDTO;
import kc.dto.account.UserSimpleDTO;
import kc.dto.offering.*;
import kc.enums.offering.*;
import kc.framework.extension.*;
import kc.mapping.CycleAvoidingMappingContext;
import kc.mapping.offering.*;
import kc.model.offering.*;
import kc.service.base.ServiceBase;
import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.*;

@Service
@lombok.extern.slf4j.Slf4j
public class CategoryService extends ServiceBase implements ICategoryService {
    private final ICategoryRepository _CategoryRepository;
    private final CategoryMapping _CategoryMapping;

    private final ICategoryManagerRepository _CategoryManagerRepository;
    private final CategoryManagerMapping _CategoryManagerMapping;

    private final IPropertyProviderRepository _PropertyProviderRepository;
    private final PropertyProviderMapping _PropertyProviderMapping;

    public CategoryService(IGlobalConfigApiService globalConfigApiService,
                           ICategoryManagerRepository _CategoryManagerRepository,
                           ICategoryRepository _CategoryRepository,
                           CategoryMapping _CategoryMapping,
                           CategoryManagerMapping _CategoryManagerMapping,
                           IPropertyProviderRepository _PropertyProviderRepository,
                           PropertyProviderMapping _PropertyProviderMapping) {
        super(globalConfigApiService);
        this._CategoryManagerRepository = _CategoryManagerRepository;
        this._CategoryRepository = _CategoryRepository;
        this._CategoryMapping = _CategoryMapping;
        this._CategoryManagerMapping = _CategoryManagerMapping;
        this._PropertyProviderRepository = _PropertyProviderRepository;
        this._PropertyProviderMapping = _PropertyProviderMapping;
    }


    /*------------------------------------------商品分类-----------------------------------------------*/
    @Override
    public List<CategoryDTO> findAllByPriceTypeAndPropertyTypes(OfferingPriceType priceType, List<OfferingPropertyType> propertyTypes){
        Integer s = priceType != null
                ? priceType.getIndex()
                : OfferingPriceType.NegotiablePrice.getIndex();
        int v = 0;
        for (OfferingPropertyType version : propertyTypes) {
            v = v | version.getIndex();
        }

        List<Category> data = _CategoryRepository.findAllByPriceTypeAndPropertyTypes(s, v);
        return _CategoryMapping.from(data, new CycleAvoidingMappingContext());
    }

    @Override
    public List<CategoryDTO> GetRootOfferingCategoriesByName(String name) {
        List<Category> data = _CategoryRepository.findAllTreeNodesWithNestParentAndChildByName(Category.class, name);
        return _CategoryMapping.from(data, new CycleAvoidingMappingContext());
    }

    @Override
    public CategoryDTO GetCategoryById(int id) {
        Category data = _CategoryRepository.findWithParentById(id);
        return _CategoryMapping.from(data, new CycleAvoidingMappingContext());
    }

    @Override
    public boolean SaveCategory(CategoryDTO data) {
        int id = data.getId();
        String name = data.getText();
        int parnetId = data.getParentId();

        Category model = _CategoryMapping.to(data, new CycleAvoidingMappingContext());
        Category parent = _CategoryRepository.findWithParentById(parnetId);
        if (parent != null) model.setParentNode(parent);

        boolean exist = ExistCategoryName(id, name);
        if (exist)
            throw new IllegalArgumentException("名称【" + name + "】已存在,请重新输入！");
        try {
            if (model.getId() != 0) {
                if (parent != null)
                    model.setTreeCode(parent.getTreeCode() + _CategoryRepository.getTreeCodeSplitIdWithChar() + model.getId());

                _CategoryRepository.save(model);
                _CategoryRepository.flush();
            } else {
                _CategoryRepository.save(model);
                if (parent != null) {
                    model.setLevel(parent.getLevel() + 1);
                    model.setTreeCode(parent.getTreeCode() + _CategoryRepository.getTreeCodeSplitIdWithChar() + model.getId());
                } else {
                    model.setLevel(1);
                    model.setTreeCode(String.valueOf(model.getId()));
                }

                model.setLeaf(true);

                _CategoryRepository.save(model);
                _CategoryRepository.flush();
            }

            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public boolean RemoveCategory(int id) {
        Category model = _CategoryRepository.findWithParentById(id);
        model.setDeleted(true);
        return _CategoryRepository.saveAndFlush(model) != null;
    }

    @Override
    public boolean ExistCategoryName(int id, String name) {
        if (id == 0) {
            return _CategoryRepository.existsByName(name);
        } else {
            return _CategoryRepository.existsByIdAndName(id, name);
        }
    }

    //https://blog.wuwii.com/jpa-specification.html
    /*------------------------------------------商品负责人设置-----------------------------------------------*/
    @Override
    public List<CategoryManagerDTO> findAllManagersByCategoryId(int categoryId) {
        List<CategoryManager> dbManagers = _CategoryManagerRepository.findAllByCategory_Id(categoryId);
        return _CategoryManagerMapping.from(dbManagers);
    }

    @Override
    public PaginatedBaseDTO<CategoryManagerDTO> findPaginatedManagersByCategoryId(int pageIndex, int pageSize, int categoryId) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<CategoryManager> data = _CategoryManagerRepository.findAll(new Specification<CategoryManager>() {

            @Override
            public Predicate toPredicate(Root<CategoryManager> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                //一对多的两张表关联查询：CategoryManager：Category  =  M：1
                Join<CategoryManager, Category> categoryJoin = root.join("category", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(categoryJoin.get("id").as(Integer.class), categoryId));

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);

        long total = data.getTotalElements();
        List<CategoryManagerDTO> rows = _CategoryManagerMapping.from(data.getContent());
        return new PaginatedBaseDTO<CategoryManagerDTO>(pageIndex, pageSize, total, rows);
    }

    @Override
    @Transactional
    public boolean AddCategoryManagers(int categoryId, List<UserSimpleDTO> users, String userId, String userName) {
        Category category = _CategoryRepository.findWithParentById(categoryId);
        List<CategoryManager> dbManagers = category.getCategoryManagers();
        if (dbManagers.size() > 0) {
            _CategoryManagerRepository.deleteInBatch(dbManagers);
        }

        List<CategoryManager> managers = _CategoryManagerMapping.UsersTo(users);
        managers.forEach(m -> {
            m.setCategory(category);
            m.setCreatedBy(userId);
            m.setCreatedDate(DateExtensions.getDateTimeUtcNow());
            m.setModifiedBy(userId);
            m.setModifiedDate(DateExtensions.getDateTimeUtcNow());
        });
        _CategoryManagerRepository.saveAll(managers);

        //TODO: add category operation logs

        return true;
    }

    @Override
    public boolean RemoveCategoryManagerById(int managerId) {
        _CategoryManagerRepository.deleteById(managerId);
        return true;
    }


    /*------------------------------------------商品规格设置-----------------------------------------------*/
    @Override
    public List<OfferingSpecificationDTO> findAllSpecificationsByCategoryId(int categoryId) {
        List<PropertyProvider> propertyProviders = _PropertyProviderRepository.findAllByCategory_Id(categoryId);
        List<OfferingSpecificationDTO> result = new ArrayList<>();
        propertyProviders.forEach(m -> result.add(convertProperty(m)));
        return result;
    }

    @Override
    public boolean RemoveSpecificationById(int specId) {
        _PropertyProviderRepository.deleteById(specId);
        return true;
    }

    @Override
    @Transactional
    public boolean SaveSpecifications(int categoryId, List<OfferingSpecificationDTO> specs, String userId, String userName) {
        Category category = _CategoryRepository.findWithParentById(categoryId);
        if (category == null)
            return true;
        List<PropertyProvider> dbProviders = category.getPropertyProviders();
        if (dbProviders.size() > 0) {
            _PropertyProviderRepository.deleteInBatch(dbProviders);
        }

        List<PropertyProvider> newProviders = new ArrayList<>();
        int currentIndex = 1;
        for (OfferingSpecificationDTO m : specs) {
            m.setSpecId(null);
            m.setAttrId1(null);
            m.setAttrId2(null);
            m.setAttrId3(null);
            m.setAttrId4(null);
            m.setAttrId5(null);
            m.setAttrId6(null);
            m.setAttrId7(null);
            m.setAttrId8(null);
            m.setAttrId9(null);
            m.setAttrId10(null);

            PropertyProvider provider = convertSpecification(m);
            provider.setServiceDataType(ServiceAttrDataType.Text.getIndex());
            provider.setCanEdit(true);
            provider.setRequire(true);
            provider.setServiceDataType(ServiceDataType.DropdownList.getIndex());
            provider.setCategory(category);
            provider.setIndex(currentIndex);
            newProviders.add(provider);
            currentIndex++;
        }

        _PropertyProviderRepository.saveAll(newProviders);

        //TODO: add category operation logs

        return true;
    }

    private OfferingSpecificationDTO convertProperty(PropertyProvider model) {
        OfferingSpecificationDTO result = new OfferingSpecificationDTO();
        result.setSpecId(model.getId());
        result.setSpecName(model.getName());
        List<PropertyProviderAttr> attrs = model.getServiceProviderAttrs();
        for (int i = 0; i < attrs.size(); i++) {
            PropertyProviderAttr attr = IterableExtensions.getByIndex(attrs, i);
            Integer id = attr != null ? attr.getId() : null;
            String name = attr != null ? attr.getName() : null;
            if (i == 0) {
                result.setAttrId1(id);
                result.setAttrName1(name);
            } else if (i == 1) {
                result.setAttrId2(id);
                result.setAttrName2(name);
            } else if (i == 2) {
                result.setAttrId3(id);
                result.setAttrName3(name);
            } else if (i == 3) {
                result.setAttrId4(id);
                result.setAttrName4(name);
            } else if (i == 4) {
                result.setAttrId5(id);
                result.setAttrName5(name);
            } else if (i == 5) {
                result.setAttrId6(id);
                result.setAttrName6(name);
            } else if (i == 6) {
                result.setAttrId7(id);
                result.setAttrName7(name);
            } else if (i == 7) {
                result.setAttrId8(id);
                result.setAttrName8(name);
            } else if (i == 8) {
                result.setAttrId9(id);
                result.setAttrName9(name);
            } else if (i == 9) {
                result.setAttrId10(id);
                result.setAttrName10(name);
            }
        }
        return result;
    }

    private PropertyProvider convertSpecification(OfferingSpecificationDTO model) {
        PropertyProvider result = new PropertyProvider();
        if (model.getSpecId() != null)
            result.setId(model.getSpecId());
        result.setName(model.getSpecName());
        result.setServiceDataType(ServiceDataType.TextList.getIndex());
        if (!StringExtensions.isNullOrEmpty(model.getAttrName1())) {
            PropertyProviderAttr attr = new PropertyProviderAttr();
            if (model.getAttrId1() != null)
                attr.setId(model.getAttrId1());
            attr.setName(model.getAttrName1());
            attr.setServiceAttrDataType(ServiceAttrDataType.Text.getIndex());
            attr.setIndex(1);
            attr.setServiceProvider(result);
            result.getServiceProviderAttrs().add(attr);
        }
        if (!StringExtensions.isNullOrEmpty(model.getAttrName2())) {
            PropertyProviderAttr attr = new PropertyProviderAttr();
            if (model.getAttrId2() != null)
                attr.setId(model.getAttrId2());
            attr.setName(model.getAttrName2());
            attr.setServiceAttrDataType(ServiceAttrDataType.Text.getIndex());
            attr.setIndex(2);
            attr.setServiceProvider(result);
            result.getServiceProviderAttrs().add(attr);
        }
        if (!StringExtensions.isNullOrEmpty(model.getAttrName3())) {
            PropertyProviderAttr attr = new PropertyProviderAttr();
            if (model.getAttrId3() != null)
                attr.setId(model.getAttrId3());
            attr.setName(model.getAttrName3());
            attr.setServiceAttrDataType(ServiceAttrDataType.Text.getIndex());
            attr.setIndex(3);
            attr.setServiceProvider(result);
            result.getServiceProviderAttrs().add(attr);
        }
        if (!StringExtensions.isNullOrEmpty(model.getAttrName4())) {
            PropertyProviderAttr attr = new PropertyProviderAttr();
            if (model.getAttrId4() != null)
                attr.setId(model.getAttrId4());
            attr.setName(model.getAttrName4());
            attr.setServiceAttrDataType(ServiceAttrDataType.Text.getIndex());
            attr.setIndex(4);
            attr.setServiceProvider(result);
            result.getServiceProviderAttrs().add(attr);
        }
        if (!StringExtensions.isNullOrEmpty(model.getAttrName5())) {
            PropertyProviderAttr attr = new PropertyProviderAttr();
            if (model.getAttrId5() != null)
                attr.setId(model.getAttrId5());
            attr.setName(model.getAttrName5());
            attr.setServiceAttrDataType(ServiceAttrDataType.Text.getIndex());
            attr.setIndex(5);
            attr.setServiceProvider(result);
            result.getServiceProviderAttrs().add(attr);
        }
        if (!StringExtensions.isNullOrEmpty(model.getAttrName6())) {
            PropertyProviderAttr attr = new PropertyProviderAttr();
            if (model.getAttrId6() != null)
                attr.setId(model.getAttrId6());
            attr.setName(model.getAttrName6());
            attr.setServiceAttrDataType(ServiceAttrDataType.Text.getIndex());
            attr.setIndex(6);
            attr.setServiceProvider(result);
            result.getServiceProviderAttrs().add(attr);
        }
        if (!StringExtensions.isNullOrEmpty(model.getAttrName7())) {
            PropertyProviderAttr attr = new PropertyProviderAttr();
            if (model.getAttrId7() != null)
                attr.setId(model.getAttrId7());
            attr.setName(model.getAttrName7());
            attr.setServiceAttrDataType(ServiceAttrDataType.Text.getIndex());
            attr.setIndex(7);
            attr.setServiceProvider(result);
            result.getServiceProviderAttrs().add(attr);
        }
        if (!StringExtensions.isNullOrEmpty(model.getAttrName8())) {
            PropertyProviderAttr attr = new PropertyProviderAttr();
            if (model.getAttrId8() != null)
                attr.setId(model.getAttrId8());
            attr.setName(model.getAttrName8());
            attr.setServiceAttrDataType(ServiceAttrDataType.Text.getIndex());
            attr.setIndex(8);
            attr.setServiceProvider(result);
            result.getServiceProviderAttrs().add(attr);
        }
        if (!StringExtensions.isNullOrEmpty(model.getAttrName9())) {
            PropertyProviderAttr attr = new PropertyProviderAttr();
            if (model.getAttrId9() != null)
                attr.setId(model.getAttrId9());
            attr.setName(model.getAttrName9());
            attr.setServiceAttrDataType(ServiceAttrDataType.Text.getIndex());
            attr.setIndex(9);
            attr.setServiceProvider(result);
            result.getServiceProviderAttrs().add(attr);
        }
        if (!StringExtensions.isNullOrEmpty(model.getAttrName10())) {
            PropertyProviderAttr attr = new PropertyProviderAttr();
            if (model.getAttrId10() != null)
                attr.setId(model.getAttrId10());
            attr.setName(model.getAttrName10());
            attr.setServiceAttrDataType(ServiceAttrDataType.Text.getIndex());
            attr.setIndex(10);
            attr.setServiceProvider(result);
            result.getServiceProviderAttrs().add(attr);
        }
        return result;
    }


    @Override
    public PaginatedBaseDTO<PropertyProviderDTO> findPaginatedProvidersByCategoryId(int pageIndex, int pageSize, int categoryId) {

        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<PropertyProvider> data = _PropertyProviderRepository.findAll(new Specification<PropertyProvider>() {

            @Override
            public Predicate toPredicate(Root<PropertyProvider> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                //两张表关联查询
                Join<PropertyProvider, Category> categoryJoin = root.join(root.getModel().getSingularAttribute("category", Category.class), JoinType.INNER);
                predicates.add(criteriaBuilder.equal(categoryJoin.get("id").as(Integer.class), categoryId));

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);

        long total = data.getTotalElements();
        List<PropertyProviderDTO> rows = _PropertyProviderMapping.from(data.getContent());
        return new PaginatedBaseDTO<PropertyProviderDTO>(pageIndex, pageSize, total, rows);
    }

    @Override
    public List<PropertyProviderDTO> findAllProvidersByCategoryId(int categoryId){
        List<PropertyProvider> data = _PropertyProviderRepository.findAllByCategory_Id(categoryId);
        return _PropertyProviderMapping.from(data);
    }
}
