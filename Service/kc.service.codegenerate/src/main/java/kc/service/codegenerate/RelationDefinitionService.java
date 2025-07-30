package kc.service.codegenerate;

import kc.dataaccess.codegenerate.*;
import kc.dto.PaginatedBaseDTO;
import kc.dto.codegenerate.ModelDefFieldDTO;
import kc.dto.codegenerate.ModelDefinitionDTO;
import kc.dto.codegenerate.RelationDefDetailDTO;
import kc.dto.codegenerate.RelationDefinitionDTO;
import kc.enums.codegenerate.ModelType;
import kc.framework.extension.ListExtensions;
import kc.framework.extension.StringExtensions;
import kc.framework.util.SerializeHelper;
import kc.mapping.codegenerate.ModelDefinitionMapping;
import kc.mapping.codegenerate.RelationDefinitionMapping;
import kc.model.codegenerate.*;
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
public class RelationDefinitionService extends ServiceBase implements IRelationDefinitionService {
    private IModelCategoryRepository _modelCategoryRepository;
    private IModelDefinitionRepository _modelDefinitionRepository;
    private IModelDefFieldRepository _modelDefFieldRepository;
    private ModelDefinitionMapping _modelDefinitionMapping;

    private IRelationDefinitionRepository _relationDefinitionRepository;
    private IRelationDefDetailRepository _relationDefDetailRepository;
    private IModelChangeLogRepository _applicationLogRepository;
    private RelationDefinitionMapping _relationDefinitionMapping;

    @Autowired
    public RelationDefinitionService(IGlobalConfigApiService globalConfigApiService,
                                     IModelCategoryRepository _modelCategoryRepository,
                                     IModelDefinitionRepository _modelDefinitionRepository,
                                     IModelDefFieldRepository _modelDefFieldRepository,
                                     ModelDefinitionMapping _modelDefinitionMapping,
                                     IRelationDefinitionRepository _relationDefinitionRepository,
                                     IRelationDefDetailRepository _relationDefDetailRepository,
                                     IModelChangeLogRepository _applicationLogRepository,
                                     RelationDefinitionMapping _relationDefinitionMapping) {
        super(globalConfigApiService);
        this._modelCategoryRepository = _modelCategoryRepository;
        this._modelDefinitionRepository = _modelDefinitionRepository;
        this._modelDefFieldRepository = _modelDefFieldRepository;
        this._modelDefinitionMapping = _modelDefinitionMapping;
        this._relationDefinitionRepository = _relationDefinitionRepository;
        this._relationDefDetailRepository = _relationDefDetailRepository;
        this._applicationLogRepository = _applicationLogRepository;
        this._relationDefinitionMapping = _relationDefinitionMapping;
    }

    @Override
    public List<RelationDefinitionDTO> findAllRelationDefinitions(Integer categoryId, String name, String displayName, String appId) {
        List<RelationDefinition> data = _relationDefinitionRepository.findAll(new Specification<RelationDefinition>() {
            private static final long serialVersionUID = 924729964744553669L;
            @Override
            public Predicate toPredicate(Root<RelationDefinition> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return getRelationDefPredicate(root, criteriaBuilder, categoryId, name, displayName, appId);
            }
        });

        List<RelationDefinitionDTO> rows = _relationDefinitionMapping.simpleFromRelationDefinitionList(data);
        //设置关系模型的主表数据模型
        appendMainModelDef(rows);

        return rows;
    }

    @Override
    public PaginatedBaseDTO<RelationDefinitionDTO> findPaginatedRelationDefinitions(
            int pageIndex, int pageSize, Integer categoryId, String name, String displayName, String appId) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<RelationDefinition> data = _relationDefinitionRepository.findAll((Specification<RelationDefinition>) (root, query, criteriaBuilder) -> getRelationDefPredicate(root, criteriaBuilder, categoryId, name, displayName, appId), pageable);

        long total = data.getTotalElements();
        List<RelationDefinitionDTO> rows = _relationDefinitionMapping.simpleFromRelationDefinitionList(data.getContent());
        //设置关系模型的主表数据模型
        appendMainModelDef(rows);
        return new PaginatedBaseDTO<>(pageIndex, pageSize, total, rows);
    }


    private Predicate getRelationDefPredicate(Root<RelationDefinition> root, CriteriaBuilder criteriaBuilder,
                                              Integer categoryId, String name, String displayName, String appId) {
        List<Predicate> predicates = new ArrayList<>();
        if (null != categoryId && 0 != categoryId) {
            root.join("modelCategory", JoinType.INNER);
            predicates.add(criteriaBuilder.equal(root.get("modelCategory").get("id"), categoryId));
            //predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("modelCategory.id"), categoryId)));
        }
        if (!StringExtensions.isNullOrEmpty(name)) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + name + "%")));
        }
        if (!StringExtensions.isNullOrEmpty(displayName)) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("displayName"), "%" + displayName + "%")));
        }
        if (!StringExtensions.isNullOrEmpty(appId)) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("applicationId"), appId)));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }


    /**
     * 设置关系模型中的主表数据模型
     *
     * @param rows 需要设置的关系模型列表
     */
    private void appendMainModelDef(List<RelationDefinitionDTO> rows) {
        List<Integer> defIds = rows.stream().map(RelationDefinitionDTO::getMainModelDefId).collect(Collectors.toList());
        List<ModelDefinition> models = _modelDefinitionRepository.findAllByPropertyIdIn(defIds);
        rows.forEach(r -> {
            Optional<ModelDefinition> modelOpt = models.stream().filter(m -> m.getPropertyId() == r.getMainModelDefId()).findFirst();
            if (modelOpt.isPresent()) {
                ModelDefinitionDTO modelDto = _modelDefinitionMapping.simpleFromModelDefinition(modelOpt.get());
                r.setMainModelDef(modelDto);
            }
        });
    }

    @Override
    public RelationDefinitionDTO getRelationDefinitionById(int id) {
        RelationDefinition data = _relationDefinitionRepository.findDetailById(id);
        //设置关系模型的主表数据模型
        RelationDefinitionDTO result = _relationDefinitionMapping.fromRelationDefinition(data);

        //一次获取相关主表、子表的数据模型及其关系属性的数据
        List<Integer> allDefIds = result.getDefDetails().stream()
                .map(RelationDefDetailDTO::getSubModelDefId)
                .collect(Collectors.toList());
        allDefIds.add(data.getMainModelDefId());
        List<ModelDefinition> models = _modelDefinitionRepository.findAllByPropertyIdIn(allDefIds);
        List<ModelDefinitionDTO> modelDtos = _modelDefinitionMapping.fromModelDefinitionList(models);

        //设置主表对象
        Optional<ModelDefinitionDTO> mainModelOpt = modelDtos.stream()
                .filter(m -> m.getPropertyId() == data.getMainModelDefId())
                .findFirst();
        if (mainModelOpt.isPresent()) {
            result.setMainModelDef(mainModelOpt.get());
            List<ModelDefFieldDTO> mainModelFields = mainModelOpt.get().getPropertyAttributeList();
            //设置主表关联属性关系
            result.getDefDetails().forEach(r -> {
                Optional<ModelDefFieldDTO> mainModelFieldOpt = mainModelFields.stream()
                        .filter(m -> m.getPropertyAttributeId() == r.getMainModelDefFieldId())
                        .findFirst();
                mainModelFieldOpt.ifPresent(r::setMainModelDefField);
            });
        }

        //设置子表对象
        result.getDefDetails().forEach(r -> {
            Optional<ModelDefinitionDTO> subModelOpt = modelDtos.stream()
                    .filter(m -> m.getPropertyId() == r.getSubModelDefId())
                    .findFirst();
            if (subModelOpt.isPresent()) {
                r.setSubModelDef(subModelOpt.get());
                List<ModelDefFieldDTO> subModelFields = subModelOpt.get().getPropertyAttributeList();
                //设置子表关联属性关系
                Optional<ModelDefFieldDTO> subModelFieldOpt = subModelFields.stream()
                        .filter(m -> m.getPropertyAttributeId() == r.getSubModelDefFieldId())
                        .findFirst();
                subModelFieldOpt.ifPresent(r::setSubModelDefField);
            }
        });

        return result;
    }

    @Override
    public void validateRelationDefinition(RelationDefinitionDTO model) {
        org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
        org.hibernate.engine.spi.SessionFactoryImplementor sf = (org.hibernate.engine.spi.SessionFactoryImplementor) session.getSessionFactory();

        String appId = model.getApplicationId();
        //判断name是否重复，tableName是否重复
        if (!model.getIsEditMode() || 0 == model.getId()) {
            boolean existName = existsRelationDefName(appId, null, model.getName());
            if (existName) {
                throw new IllegalArgumentException(String.format("应用下已经存在相同的关系模型名称：%s",
                        model.getName()));
            }
        } else {
            boolean existName = existsRelationDefName(appId, model.getId(), model.getName());
            if (existName) {
                throw new IllegalArgumentException(String.format("应用下已经存在相同的关系模型名称：%s",
                        model.getName()));
            }
        }
    }

    @Override
    public boolean existsRelationDefName(String appId, Integer id, String name) {
        long count = _relationDefinitionRepository.count((Specification<RelationDefinition>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (null != id && 0 != id) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.notEqual(root.get("id"), id)));
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
    @Transactional
    public boolean saveRelationDefinitionWithFields(RelationDefinitionDTO model) {
        validateRelationDefinition(model);
        RelationDefinition data = _relationDefinitionMapping.toRelationDefinition(model);

        Integer categoryId = model.getCategoryId();
        if (null != categoryId) {
            ModelCategory category = _modelCategoryRepository.getById(categoryId);
            data.setModelCategory(category);
        }

        //添加日志
        ModelChangeLog log = new ModelChangeLog();
        log.setModelType(ModelType.RelationDefinition.getIndex());
        log.setReferenceId(String.valueOf(data.getId()));
        log.setReferenceName(String.valueOf(data.getName()));
        log.setOperatorId(model.getCreatedBy());
        log.setOperator(model.getCreatedName());

        if (model.getIsEditMode()) {
            // 获取数据库的所有数据表及其字段属性的数据，用于比较新传入数据中，处于更新、删除、新增的那种状态
            RelationDefinition dbData = _relationDefinitionRepository.findDetailById(data.getId());
            dbData.setDisplayName(data.getDisplayName());
            dbData.setDescription(data.getDescription());
            List<Integer> newIdList = data.getDefDetails().stream().map(RelationDefDetail::getId).collect(Collectors.toList());
            List<Integer> dbIdList = dbData.getDefDetails().stream().map(RelationDefDetail::getId).collect(Collectors.toList());
            List<Integer> addList = newIdList.stream().filter(m -> !dbIdList.contains(m)).collect(Collectors.toList());
            List<Integer> delList = dbIdList.stream().filter(m -> !newIdList.contains(m)).collect(Collectors.toList());
            List<Integer> updateList = dbIdList.stream().filter(m -> newIdList.contains(m)).collect(Collectors.toList());

            System.out.println("---add ids: " + ListExtensions.toCommaSeparatedInt(addList)
                    + "---delete ids: " + ListExtensions.toCommaSeparatedInt(delList)
                    + "---update ids: " + ListExtensions.toCommaSeparatedInt(updateList));

            //添加新的字段属性
            List<RelationDefDetail> addFields = data.getDefDetails().stream()
                    .filter(m -> addList.contains(m.getId()))
                    .collect(Collectors.toList());
            for (RelationDefDetail addField : addFields) {
                attachDetail(data, addField);
                dbData.getDefDetails().add(addField);
            }

            //更新原有的字段属性
            List<RelationDefDetail> updateFields = dbData.getDefDetails().stream()
                    .filter(m -> updateList.contains(m.getId()))
                    .collect(Collectors.toList());
            for (RelationDefDetail dbUpdateField : updateFields) {
                Optional<RelationDefDetail> newFieldOpt = data.getDefDetails().stream()
                        .filter(m -> dbUpdateField.getId() == m.getId())
                        .findFirst();
                if (newFieldOpt.isPresent()) {
                    RelationDefDetail newField = newFieldOpt.get();
                    dbUpdateField.setRelationType(newField.getRelationType());
                    dbUpdateField.setName(newField.getName());
                    dbUpdateField.setDisplayName(newField.getDisplayName());
                    dbUpdateField.setMainModelDefFieldId(newField.getMainModelDefFieldId());
                    dbUpdateField.setSubModelDefId(newField.getSubModelDefId());
                    dbUpdateField.setSubModelDefFieldId(newField.getSubModelDefFieldId());
                    dbUpdateField.setModifiedBy(data.getModifiedBy());
                    dbUpdateField.setModifiedName(data.getModifiedName());
                    dbUpdateField.setModifiedDate(data.getModifiedDate());
                }
            }

            //删除已经不存在的字段属性
            List<RelationDefDetail> delFields = dbData.getDefDetails().stream()
                    .filter(m -> delList.contains(m.getId()))
                    .collect(Collectors.toList());
            for (RelationDefDetail delField : delFields) {
                dbData.getDefDetails().remove(delField);
            }

            System.out.println("---add item: " + ListExtensions.toCommaSeparatedStringByFilter(addFields, RelationDefDetail::getDisplayName)
                    + "---delete item: " + ListExtensions.toCommaSeparatedStringByFilter(delFields, RelationDefDetail::getDisplayName)
                    + "---update item: " + ListExtensions.toCommaSeparatedStringByFilter(updateFields, RelationDefDetail::getDisplayName));

            if (delFields.size() > 0)
                _relationDefDetailRepository.deleteAll(delFields);
            _relationDefinitionRepository.save(dbData);

            log.setRemark(String.format("编辑关系模型数据: %s", data.getName()));
            log.setRefObjectJson(SerializeHelper.ToJson(dbData));
            _applicationLogRepository.saveAndFlush(log);
        } else {
            //新增关系模型（新增关系模型及相关表单数据）
            data.getDefDetails().forEach(field -> {
                attachDetail(data, field);
            });
            _relationDefinitionRepository.save(data);

            log.setRemark(String.format("新增关系模型数据: %s", data.getName()));
            log.setRefObjectJson(SerializeHelper.ToJson(data));
            _applicationLogRepository.saveAndFlush(log);
        }

        return true;
    }

    private void attachDetail(RelationDefinition data, RelationDefDetail addField) {
        addField.setId(0);
        addField.setRelationDefinition(data);
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
    public boolean removeRelationDefinitionById(int id, String currentUserId, String currentUserName) {
        Optional<RelationDefinition> dataOpt = _relationDefinitionRepository.findById(id);
        if (!dataOpt.isPresent()) {
            throw new IllegalArgumentException(String.format("未找到Id为【%s】相关的关系模型", id));
        }
        _relationDefinitionRepository.deleteById(id);
        String msg = String.format("删除数据表【%s】关系数据", dataOpt.get().getName());

        //添加日志
        ModelChangeLog log = new ModelChangeLog();
        log.setModelType(ModelType.RelationDefinition.getIndex());
        log.setReferenceId(String.valueOf(dataOpt.get().getId()));
        log.setReferenceName(String.valueOf(dataOpt.get().getName()));
        log.setOperatorId(currentUserId);
        log.setOperator(currentUserName);
        log.setRemark(msg);
        log.setRefObjectJson(SerializeHelper.ToJson(dataOpt.get()));
        _applicationLogRepository.saveAndFlush(log);
        return true;
    }

    @Override
    public List<RelationDefDetailDTO> findAllRelationDefDetailsByDefId(int modelDefId) {
        List<RelationDefDetail> data = _relationDefDetailRepository.findAllByRelationDefinition_Id(modelDefId);
        List<RelationDefDetailDTO> result = _relationDefinitionMapping.fromRelationDefDetailList(data);

        //获取子表相关的数据模型
        List<Integer> allModelIds = data.stream()
                .map(RelationDefDetail::getSubModelDefId)
                .collect(Collectors.toList());
        List<ModelDefinition> subModels = _modelDefinitionRepository.findAllByPropertyIdIn(allModelIds);

        //获取主表和子表相关的关联关系属性
        List<Integer> allModelFieldIds = data.stream()
                .map(RelationDefDetail::getMainModelDefFieldId)
                .collect(Collectors.toList());
        List<Integer> subModelFieldIds = data.stream()
                .map(RelationDefDetail::getSubModelDefFieldId)
                .collect(Collectors.toList());
        allModelFieldIds.addAll(subModelFieldIds);
        List<ModelDefField> allModelFields = _modelDefFieldRepository.findAllByPropertyAttributeIdIn(allModelFieldIds);

        //设置子表模型，主表及子表相关的关联关系属性
        result.forEach(r -> {
            //设置主表模型的关联关系属性
            Optional<ModelDefField> mainModelFieldOpt = allModelFields.stream()
                    .filter(m -> m.getPropertyAttributeId() == r.getMainModelDefFieldId())
                    .findFirst();
            if (mainModelFieldOpt.isPresent()) {
                ModelDefFieldDTO mainModelDto = _modelDefinitionMapping.fromModelDefField(mainModelFieldOpt.get());
                r.setMainModelDefField(mainModelDto);
            }

            //设置子模型的关联关系属性
            Optional<ModelDefinition> subModelOpt = subModels.stream()
                    .filter(m -> m.getPropertyId() == r.getSubModelDefId())
                    .findFirst();
            if (subModelOpt.isPresent()) {
                ModelDefinitionDTO subModelDto = _modelDefinitionMapping.fromModelDefinition(subModelOpt.get());
                r.setSubModelDef(subModelDto);
            }

            //设置子模型的关联关系属性
            Optional<ModelDefField> subModelFieldOpt = allModelFields.stream()
                    .filter(m -> m.getPropertyAttributeId() == r.getSubModelDefFieldId())
                    .findFirst();
            if (subModelFieldOpt.isPresent()) {
                //设置子模型的关联关系属性
                ModelDefFieldDTO subModelDto = _modelDefinitionMapping.fromModelDefField(subModelFieldOpt.get());
                r.setSubModelDefField(subModelDto);
            }
        });

        return result;
    }

    @Override
    public RelationDefDetailDTO getRelationDefDetailById(int id) {
        RelationDefDetail data = _relationDefDetailRepository.getById(id);
        RelationDefDetailDTO result = _relationDefinitionMapping.fromRelationDefDetail(data);
        //设置关联子表的数据模型
        ModelDefinition subModel = _modelDefinitionRepository.getById(data.getSubModelDefId());
        result.setSubModelDef(_modelDefinitionMapping.fromModelDefinition(subModel));

        //获取主表和子表相关的关联关系属性
        List<Integer> allModelFieldIds = new ArrayList<>();
        allModelFieldIds.add(data.getMainModelDefFieldId());
        allModelFieldIds.add(data.getSubModelDefFieldId());
        List<ModelDefField> allModelFields = _modelDefFieldRepository.findAllByPropertyAttributeIdIn(allModelFieldIds);

        //设置主表模型的关联关系属性
        Optional<ModelDefField> mainModelFieldOpt = allModelFields.stream()
                .filter(m -> m.getPropertyAttributeId() == result.getMainModelDefFieldId())
                .findFirst();
        if (mainModelFieldOpt.isPresent()) {
            ModelDefFieldDTO mainModelDto = _modelDefinitionMapping.fromModelDefField(mainModelFieldOpt.get());
            result.setMainModelDefField(mainModelDto);
        }

        //设置子模型的关联关系属性
        Optional<ModelDefField> subModelFieldOpt = allModelFields.stream()
                .filter(m -> m.getPropertyAttributeId() == result.getSubModelDefFieldId())
                .findFirst();
        if (subModelFieldOpt.isPresent()) {
            //设置子模型的关联关系属性
            ModelDefFieldDTO subModelDto = _modelDefinitionMapping.fromModelDefField(subModelFieldOpt.get());
            result.setSubModelDefField(subModelDto);
        }

        return result;
    }

    @Override
    public boolean saveRelationDefDetail(RelationDefDetailDTO data) {
        RelationDefinition relationDef = _relationDefinitionRepository.getById(data.getRelationDefId());
        RelationDefDetail model = _relationDefinitionMapping.toRelationDefDetail(data);
        model.setRelationDefinition(relationDef);
        _relationDefDetailRepository.saveAndFlush(model);
        return true;
    }

    @Override
    public boolean removeRelationDefDetailById(int id, String currentUserId, String currentUserName) {
        Optional<RelationDefDetail> dataOpt = _relationDefDetailRepository.findById(id);
        if (!dataOpt.isPresent()) {
            throw new IllegalArgumentException(String.format("未找到Id为【%s】相关的关系模型", id));
        }

        _relationDefDetailRepository.deleteById(id);

        //添加日志
        ModelChangeLog log = new ModelChangeLog();
        log.setModelType(ModelType.RelationDefinition.getIndex());
        log.setReferenceId(String.valueOf(dataOpt.get().getId()));
        log.setReferenceName(String.valueOf(dataOpt.get().getName()));
        log.setOperatorId(currentUserId);
        log.setOperator(currentUserName);
        log.setRemark(String.format("删除关系模型数据: %s", dataOpt.get().getName()));
        log.setRefObjectJson(SerializeHelper.ToJson(dataOpt.get()));
        _applicationLogRepository.saveAndFlush(log);
        return true;
    }

}
