package kc.service.codegenerate;

import kc.dataaccess.codegenerate.IModelCategoryRepository;
import kc.dataaccess.codegenerate.IModelChangeLogRepository;
import kc.dataaccess.codegenerate.IModelDefFieldRepository;
import kc.dataaccess.codegenerate.IModelDefinitionRepository;
import kc.dto.PaginatedBaseDTO;
import kc.dto.codegenerate.ModelDefFieldDTO;
import kc.dto.codegenerate.ModelDefinitionDTO;
import kc.enums.codegenerate.ModelBaseType;
import kc.enums.codegenerate.ModelType;
import kc.framework.base.PropertyAttributeBase;
import kc.framework.extension.ListExtensions;
import kc.framework.extension.StringExtensions;
import kc.framework.util.SerializeHelper;
import kc.mapping.codegenerate.ModelDefinitionMapping;
import kc.model.codegenerate.ModelCategory;
import kc.model.codegenerate.ModelChangeLog;
import kc.model.codegenerate.ModelDefField;
import kc.model.codegenerate.ModelDefinition;
import kc.service.base.ServiceBase;
import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModelDefinitionService extends ServiceBase implements IModelDefinitionService {

    private final IModelCategoryRepository _modelCategoryRepository;
    private final IModelDefinitionRepository _modelDefinitionRepository;
    private final IModelDefFieldRepository _modelDefFieldRepository;
    private final IModelChangeLogRepository _applicationLogRepository;
    private final ModelDefinitionMapping _modelDefinitionMapping;

    public ModelDefinitionService(IGlobalConfigApiService globalConfigApiService,
                                  IModelCategoryRepository _modelCategoryRepository,
                                  IModelDefinitionRepository _modelDefinitionRepository,
                                  IModelDefFieldRepository _modelDefFieldRepository,
                                  IModelChangeLogRepository _applicationLogRepository,
                                  ModelDefinitionMapping _modelDefinitionMapping) {
        super(globalConfigApiService);
        this._modelCategoryRepository = _modelCategoryRepository;
        this._modelDefinitionRepository = _modelDefinitionRepository;
        this._modelDefFieldRepository = _modelDefFieldRepository;
        this._applicationLogRepository = _applicationLogRepository;
        this._modelDefinitionMapping = _modelDefinitionMapping;
    }

    @Override
    public List<ModelDefinitionDTO> findAllModelDefinitions(
            Integer categoryId, ModelBaseType type, String name, String displayName, String tableName, String appId) {
        List<ModelDefinition> data = _modelDefinitionRepository.findAll(new Specification<ModelDefinition>() {
            private static final long serialVersionUID = 924729964744553669L;

            @Override
            public Predicate toPredicate(Root<ModelDefinition> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return getModelDefPredicate(root, criteriaBuilder, categoryId, type, name, displayName, tableName, appId);
            }
        });

        return _modelDefinitionMapping.simpleFromModelDefinitionList(data);
    }

    @Override
    public PaginatedBaseDTO<ModelDefinitionDTO> findPaginatedModelDefinitions(
            int pageIndex, int pageSize, Integer categoryId, ModelBaseType type, String name, String displayName, String tableName, String appId) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<ModelDefinition> data = _modelDefinitionRepository.findAll(new Specification<ModelDefinition>() {
            private static final long serialVersionUID = 924729964744553669L;

            @Override
            public Predicate toPredicate(Root<ModelDefinition> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return getModelDefPredicate(root, criteriaBuilder, categoryId, type, name, displayName, tableName, appId);
            }
        }, pageable);

        long total = data.getTotalElements();
        List<ModelDefinitionDTO> rows = _modelDefinitionMapping.simpleFromModelDefinitionList(data.getContent());
        return new PaginatedBaseDTO<>(pageIndex, pageSize, total, rows);
    }


    private Predicate getModelDefPredicate(Root<ModelDefinition> root, CriteriaBuilder criteriaBuilder,
                                           Integer categoryId, ModelBaseType type, String name, String displayName, String tableName, String appId) {
        List<Predicate> predicates = new ArrayList<>();
        if (null != categoryId && 0 != categoryId) {
            root.join("modelCategory", JoinType.INNER);
            predicates.add(criteriaBuilder.equal(root.get("modelCategory").get("id"), categoryId));
            //predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("modelCategory.id"), categoryId)));
        }
        if (null != type) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("modelBaseType"), type.getIndex())));
        }
        if (!StringExtensions.isNullOrEmpty(name)) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + name + "%")));
        }
        if (!StringExtensions.isNullOrEmpty(displayName)) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("displayName"), "%" + displayName + "%")));
        }
        if (!StringExtensions.isNullOrEmpty(tableName)) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("tableName"), "%" + tableName + "%")));
        }

        if (!StringExtensions.isNullOrEmpty(appId)) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("applicationId"), appId)));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }

    @Override
    public ModelDefinitionDTO getModelDefinitionById(int id) {
        ModelDefinition data = _modelDefinitionRepository.findByPropertyId(id);
        return _modelDefinitionMapping.fromModelDefinition(data);
    }

    @Override
    public void validateModelDefinition(ModelDefinitionDTO model) {
        String appId = model.getApplicationId();
        //判断name是否重复，tableName是否重复
        if (!model.getIsEditMode() || 0 == model.getPropertyId()) {
            boolean existName = existsModelDefName(appId, null, model.getName());
            if (existName) {
                throw new IllegalArgumentException(String.format("应用下已经存在相同的数据模型名称：%s",
                        model.getName()));
            }

            boolean existTableName = existsModelDefTableName(appId, null, model.getTableName());
            if (existTableName) {
                throw new IllegalArgumentException(String.format("应用下已经存在相同的数据模型的表名称：%s",
                        model.getTableName()));
            }
        } else {
            boolean existName = existsModelDefName(appId, model.getPropertyId(), model.getName());
            if (existName) {
                throw new IllegalArgumentException(String.format("应用下已经存在相同的数据模型名称：%s",
                        model.getName()));
            }

            boolean existTableName = existsModelDefTableName(appId, model.getPropertyId(), model.getTableName());
            if (existTableName) {
                throw new IllegalArgumentException(String.format("应用已经存在相同的数据模型的表名称：%s",
                        model.getTableName()));
            }
        }
    }

    @Override
    public boolean existsModelDefName(String appId, Integer id, String name) {
        long count = _modelDefinitionRepository.count((Specification<ModelDefinition>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (null != id && 0 != id) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.notEqual(root.get("propertyId"), id)));
            }
            if (!StringExtensions.isNullOrEmpty(appId)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("applicationId"), appId)));
            }
            if (!StringExtensions.isNullOrEmpty(name)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("name"), name)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

        return count > 0;
    }

    @Override
    public boolean existsModelDefTableName(String appId, Integer id, String tableName) {
        long count = _modelDefinitionRepository.count((Specification<ModelDefinition>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (null != id && 0 != id) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.notEqual(root.get("propertyId"), id)));
            }
            if (!StringExtensions.isNullOrEmpty(appId)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("applicationId"), appId)));
            }
            if (!StringExtensions.isNullOrEmpty(tableName)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("tableName"), tableName)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

        return count > 0;
    }

    @Override
    @Transactional
    public boolean saveModelDefinitionWithFields(ModelDefinitionDTO model) {
        validateModelDefinition(model);
        ModelDefinition data = _modelDefinitionMapping.toModelDefinition(model);

        Integer categoryId = model.getCategoryId();
        if (null != categoryId) {
            ModelCategory category = _modelCategoryRepository.getById(categoryId);
            data.setModelCategory(category);
        }

        //添加日志
        ModelChangeLog log = new ModelChangeLog();
        log.setModelType(ModelType.ModelDefinition.getIndex());
        log.setReferenceId(String.valueOf(data.getPropertyId()));
        log.setReferenceName(String.valueOf(data.getName()));
        log.setOperatorId(model.getCreatedBy());
        log.setOperator(model.getCreatedName());

        if (model.getIsEditMode()) {
            // 获取数据库的所有数据表及其字段属性的数据，用于比较新传入数据中，处于更新、删除、新增的那种状态
            ModelDefinition dbData = _modelDefinitionRepository.findByPropertyId(data.getPropertyId());
            dbData.setDisplayName(data.getDisplayName());
            dbData.setModelBaseType(data.getModelBaseType());
            dbData.setDescription(data.getDescription());
            List<Integer> newIdList = data.getPropertyAttributeList().stream().map(PropertyAttributeBase::getPropertyAttributeId).collect(Collectors.toList());
            List<Integer> dbIdList = dbData.getPropertyAttributeList().stream().map(PropertyAttributeBase::getPropertyAttributeId).collect(Collectors.toList());
            List<Integer> addList = newIdList.stream().filter(m -> !dbIdList.contains(m)).collect(Collectors.toList());
            List<Integer> delList = dbIdList.stream().filter(m -> !newIdList.contains(m)).collect(Collectors.toList());
            List<Integer> updateList = dbIdList.stream().filter(m -> newIdList.contains(m)).collect(Collectors.toList());

            System.out.println("---add ids: " + ListExtensions.toCommaSeparatedInt(addList)
                    + "---delete ids: " + ListExtensions.toCommaSeparatedInt(delList)
                    + "---update ids: " + ListExtensions.toCommaSeparatedInt(updateList));

            //添加新的字段属性
            List<ModelDefField> addFields = data.getPropertyAttributeList().stream()
                    .filter(m -> addList.contains(m.getPropertyAttributeId()))
                    .collect(Collectors.toList());
            for (ModelDefField addField : addFields) {
                attachField(data, addField);
                dbData.getPropertyAttributeList().add(addField);
            }

            //更新原有的字段属性
            List<ModelDefField> updateFields = dbData.getPropertyAttributeList().stream()
                    .filter(m -> updateList.contains(m.getPropertyAttributeId()))
                    .collect(Collectors.toList());
            for (ModelDefField dbUpdateField : updateFields) {
                Optional<ModelDefField> newFieldOpt = data.getPropertyAttributeList().stream()
                        .filter(m -> dbUpdateField.getPropertyAttributeId() == m.getPropertyAttributeId())
                        .findFirst();
                if (newFieldOpt.isPresent()) {
                    ModelDefField newField = newFieldOpt.get();
                    dbUpdateField.setDataType(newField.getDataType());
                    dbUpdateField.setName(newField.getName());
                    dbUpdateField.setDisplayName(newField.getDisplayName());
                    dbUpdateField.setIsPrimaryKey(newField.getIsPrimaryKey());
                    dbUpdateField.setIsNotNull(newField.getIsNotNull());
                    dbUpdateField.setIsUnique(newField.getIsUnique());
                    dbUpdateField.setDescription(newField.getDescription());
                    dbUpdateField.setModifiedBy(data.getModifiedBy());
                    dbUpdateField.setModifiedName(data.getModifiedName());
                    dbUpdateField.setModifiedDate(data.getModifiedDate());
                }
            }

            //删除已经不存在的字段属性
            List<ModelDefField> delFields = dbData.getPropertyAttributeList().stream()
                    .filter(m -> delList.contains(m.getPropertyAttributeId()))
                    .collect(Collectors.toList());
            for (ModelDefField delField : delFields) {
                dbData.getPropertyAttributeList().remove(delField);
            }

            System.out.println("---add item: " + ListExtensions.toCommaSeparatedStringByFilter(addFields, ModelDefField::getDisplayName)
                    + "---delete item: " + ListExtensions.toCommaSeparatedStringByFilter(delFields, ModelDefField::getDisplayName)
                    + "---update item: " + ListExtensions.toCommaSeparatedStringByFilter(updateFields, ModelDefField::getDisplayName));

            if (delFields.size() > 0)
                _modelDefFieldRepository.deleteAll(delFields);
            _modelDefinitionRepository.save(dbData);

            log.setRemark(String.format("编辑数据模型数据: %s", data.getName()));
            log.setRefObjectJson(SerializeHelper.ToJson(dbData));
            _applicationLogRepository.saveAndFlush(log);
        } else {
            //新增数据模型（新增数据模型及相关表单数据）
            data.getPropertyAttributeList().forEach(field -> {
                attachField(data, field);
            });

            _modelDefinitionRepository.save(data);

            log.setRemark(String.format("新增数据模型数据: %s", data.getName()));
            log.setRefObjectJson(SerializeHelper.ToJson(data));
            _applicationLogRepository.saveAndFlush(log);
        }
        return true;
    }

    private void attachField(ModelDefinition data, ModelDefField addField) {
        addField.setPropertyAttributeId(0);
        addField.setModelDefinition(data);
        addField.setDeleted(data.isDeleted());
        addField.setCreatedBy(data.getCreatedBy());
        addField.setCreatedName(data.getCreatedName());
        addField.setCreatedDate(data.getCreatedDate());
        addField.setModifiedBy(data.getModifiedBy());
        addField.setModifiedName(data.getModifiedName());
        addField.setModifiedDate(data.getModifiedDate());
    }

    @Override
    @Transactional
    public boolean removeModelDefinitionById(int id, String currentUserId, String currentUserName) {
        Optional<ModelDefinition> dataOpt = _modelDefinitionRepository.findById(id);
        if (!dataOpt.isPresent()) {
            throw new IllegalArgumentException(String.format("未找到Id为【%s】相关的模型定义", id));
        }

        _modelDefinitionRepository.deleteById(id);

        //添加日志
        ModelChangeLog log = new ModelChangeLog();
        log.setModelType(ModelType.ModelDefinition.getIndex());
        log.setReferenceId(String.valueOf(dataOpt.get().getPropertyId()));
        log.setReferenceName(String.valueOf(dataOpt.get().getName()));
        log.setOperatorId(currentUserId);
        log.setOperator(currentUserName);
        log.setRemark(String.format("删除数据模型数据: %s", dataOpt.get().getName()));
        log.setRefObjectJson(SerializeHelper.ToJson(dataOpt.get()));
        _applicationLogRepository.saveAndFlush(log);
        return true;
    }

    @Override
    public List<ModelDefFieldDTO> findAllModelDefFieldsByDefId(int modelDefId) {
        List<ModelDefField> data = _modelDefFieldRepository.findAllByModelDefinition_PropertyId(modelDefId);
        return _modelDefinitionMapping.fromModelDefFieldList(data);
    }

    @Override
    public ModelDefFieldDTO getModelDefFieldById(int id) {
        ModelDefField data = _modelDefFieldRepository.getById(id);
        return _modelDefinitionMapping.fromModelDefField(data);
    }

    @Override
    public boolean saveModelDefField(ModelDefFieldDTO data) {
        ModelDefinition modelDef = _modelDefinitionRepository.getById(data.getModelDefId());
        ModelDefField model = _modelDefinitionMapping.toModelDefField(data);
        model.setModelDefinition(modelDef);
        _modelDefFieldRepository.saveAndFlush(model);
        return true;
    }

    @Override
    public boolean removeModelDefFieldById(int id, String currentUserId, String currentUserName) {
        Optional<ModelDefField> dataOpt = _modelDefFieldRepository.findById(id);
        if (!dataOpt.isPresent()) {
            throw new IllegalArgumentException(String.format("未找到Id为【%s】相关的模型属性", id));
        }

        _modelDefFieldRepository.deleteById(id);

        //添加日志
        ModelChangeLog log = new ModelChangeLog();
        log.setModelType(ModelType.ModelDefinition.getIndex());
        log.setReferenceId(String.valueOf(dataOpt.get().getPropertyAttributeId()));
        log.setReferenceName(String.valueOf(dataOpt.get().getName()));
        log.setOperatorId(currentUserId);
        log.setOperator(currentUserName);
        log.setRemark(String.format("删除数据模型字段数据: %s", dataOpt.get().getName()));
        log.setRefObjectJson(SerializeHelper.ToJson(dataOpt.get()));
        _applicationLogRepository.saveAndFlush(log);
        return true;
    }

}
