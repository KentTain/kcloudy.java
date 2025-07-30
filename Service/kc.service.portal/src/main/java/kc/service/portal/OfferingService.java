package kc.service.portal;

import kc.dataaccess.portal.*;
import kc.dto.PaginatedBaseDTO;
import kc.dto.portal.*;
import kc.enums.portal.*;
import kc.framework.base.SeedEntity;
import kc.framework.enums.ProcessLogType;
import kc.framework.extension.DateExtensions;
import kc.framework.extension.StringExtensions;
import kc.mapping.CycleAvoidingMappingContext;
import kc.mapping.portal.*;
import kc.model.portal.*;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@lombok.extern.slf4j.Slf4j
public class OfferingService extends ServiceBase implements IOfferingService {
    private final ICategoryRepository _CategoryRepository;
    private final OfferingCategoryMapping _CategoryMapping;

    private final IOfferingRepository _OfferingRepository;
    private final OfferingMapping _OfferingMapping;

    private final IOfferingPropertyRepository _OfferingPropertyRepository;

    private final IOfferingOperationLogRepository _OfferingLogRepository;
    private final OfferingOperationLogMapping _OfferingLogMapping;

    @Autowired
    public OfferingService(IGlobalConfigApiService globalConfigApiService,
                           ICategoryRepository categoryRepository,
                           OfferingCategoryMapping categoryMapping,
                           IOfferingRepository offeringRepository,
                           OfferingMapping offeringMapping,
                           IOfferingPropertyRepository offeringPropertyRepository,
                           IOfferingOperationLogRepository offeringLogRepository,
                           OfferingOperationLogMapping offeringLogMapping) {
        super(globalConfigApiService);

        this._CategoryRepository = categoryRepository;
        this._CategoryMapping = categoryMapping;
        this._OfferingRepository = offeringRepository;
        this._OfferingMapping = offeringMapping;
        this._OfferingPropertyRepository = offeringPropertyRepository;
        this._OfferingLogRepository = offeringLogRepository;
        this._OfferingLogMapping = offeringLogMapping;
    }

    /*------------------------------------------商品分类-----------------------------------------------*/
    @Override
    public List<OfferingCategoryDTO> GetRootOfferingCategoriesByName(String name) {
        List<OfferingCategory> data = _CategoryRepository.findAllTreeNodesWithNestParentAndChildByName(OfferingCategory.class, name);
        return _CategoryMapping.from(data, new CycleAvoidingMappingContext());
    }

    @Override
    public OfferingCategoryDTO GetCategoryById(int id) {
        OfferingCategory data = _CategoryRepository.findWithParentById(id);
        return _CategoryMapping.from(data, new CycleAvoidingMappingContext());
    }

    @Override
    public boolean SaveCategory(OfferingCategoryDTO data) {
        int id = data.getId();
        String name = data.getText();
        int parnetId = data.getParentId();

        OfferingCategory model = _CategoryMapping.to(data, new CycleAvoidingMappingContext());
        OfferingCategory parent = _CategoryRepository.findWithParentById(parnetId);
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
        OfferingCategory model = _CategoryRepository.findWithParentById(id);
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

    /*------------------------------------------商品-----------------------------------------------*/

    @Override
    public PaginatedBaseDTO<OfferingDTO> findPaginatedOfferingsByFilter(
            int pageIndex, int pageSize, Integer categoryId, String code, String name, OfferingStatus status) {
        Integer s = null;
        if (status != null)
            s = status.getIndex();

        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Offering> data = null;
        if (categoryId == null && status == null)
            data = _OfferingRepository.findAllByCodeAndName(pageable, code == null ? "" : code, name == null ? "" : name);
        if (categoryId != null && status == null)
            data = _OfferingRepository.findAllByCodeAndNameAndCategoryId(pageable, code == null ? "" : code, name == null ? "" : name, categoryId);
        if (categoryId == null && status != null)
            data = _OfferingRepository.findAllByCodeAndNameAndStatus(pageable, code == null ? "" : code, name == null ? "" : name, s);
        if (categoryId != null && status != null)
            data = _OfferingRepository.findAllByCodeAndNameAndStatusAndCategoryId(pageable, code == null ? "" : code, name == null ? "" : name, categoryId, s);

        long total = data.getTotalElements();
        List<OfferingDTO> rows = _OfferingMapping.from(data.getContent());
        return new PaginatedBaseDTO<OfferingDTO>(pageIndex, pageSize, total, rows);
    }


    @Override
    public OfferingDTO GetOfferingById(int id) {
        Optional<Offering> data = _OfferingRepository.findById(id);
        return data.map(offering -> _OfferingMapping.from(offering)).orElse(null);
    }

    @Override
    public boolean RemoveOffering(int id, String currentUserId, String currentUserName) {
        Optional<Offering> optModel = _OfferingRepository.findById(id);
        if (!optModel.isPresent())
            throw new IllegalArgumentException("与Id【" + id + "】相匹配的商品不存在,请重新输入！");

        Offering model = optModel.get();
        model.setDeleted(true);

        OfferingOperationLog log = getBasicLog(model, currentUserId, currentUserName);
        log.setRemark(String.format("用户【%s】于【%tF %tT】删除商品【%s--%s】成功", currentUserName, log.getOperateDate(), log.getOperateDate(), model.getOfferingCode(), model.getOfferingName()));

        model.getOfferingOperationLogs().add(log);
        return _OfferingRepository.saveAndFlush(model) != null;
    }

    @Override
    public boolean PublishOffering(int id, boolean isAgree, String content, String currentUserId, String currentUserName) {
        Optional<Offering> optModel = _OfferingRepository.findById(id);
        if (!optModel.isPresent())
            throw new IllegalArgumentException("与Id【" + id + "】相匹配的商品不存在,请重新输入！");

        Offering model = optModel.get();
        OfferingOperationLog log = getBasicLog(model, currentUserId, currentUserName);

        if (model.getStatus() == OfferingStatus.Draft.getIndex()
                || model.getStatus() == OfferingStatus.Disagree.getIndex()) {
            model.setStatus(OfferingStatus.AuditPending.getIndex());
            log.setRemark(String.format("用户【%s】于【%tF %tT】提交商品【%s--%s】审核", currentUserName, log.getOperateDate(), log.getOperateDate(), model.getOfferingCode(), model.getOfferingName()));
        } else if (model.getStatus() == OfferingStatus.AuditPending.getIndex()) {
            if (isAgree) {
                model.setStatus(OfferingStatus.Approved.getIndex());
                log.setRemark(String.format("用户【%s】于【%tF %tT】通过商品【%s--%s】审核", currentUserName, log.getOperateDate(), log.getOperateDate(), model.getOfferingCode(), model.getOfferingName()));
            } else {
                model.setStatus(OfferingStatus.Disagree.getIndex());
                log.setRemark(String.format("用户【%s】于【%tF %tT】商品【%s--%s】审核不通过，已退回，原因：%s", currentUserName, log.getOperateDate(), log.getOperateDate(), model.getOfferingCode(), model.getOfferingName(), content));
            }
        } else if (model.getStatus() == OfferingStatus.Approved.getIndex()
                || model.getStatus() == OfferingStatus.Disagree.getIndex()) {
            model.setStatus(OfferingStatus.Trash.getIndex());
            log.setRemark(String.format("用户【%s】于【%tF %tT】已下架商品【%s--%s】", currentUserName, log.getOperateDate(), log.getOperateDate(), model.getOfferingCode(), model.getOfferingName()));
        }
        model.getOfferingOperationLogs().add(log);
        return _OfferingRepository.saveAndFlush(model) != null;
    }

    private OfferingOperationLog getBasicLog(Offering model, String currentUserId, String currentUserName) {
        Date currentDate = DateExtensions.getDateTimeNow();
        OfferingOperationLog log = new OfferingOperationLog();
        log.setOfferingCode(model.getOfferingCode());
        log.setOfferingName(model.getOfferingName());
        log.setType(ProcessLogType.Success.getIndex());
        log.setOperatorId(currentUserId);
        log.setOperator(currentUserName);
        log.setOperateDate(currentDate);
        log.setOffering(model);

        return log;
    }

    @Override
    public boolean ExistOfferingName(int id, String name) {
        if (id == 0) {
            return _OfferingRepository.existsByName(name);
        } else {
            return _OfferingRepository.existsByIdAndName(id, name);
        }
    }

    @Override
    public boolean SaveOffering(OfferingDTO data, String currentUserId, String currentUserName) {
        int id = data.getOfferingId();
        String name = data.getOfferingName();

        Offering model = _OfferingMapping.to(data);
        boolean exist = ExistOfferingName(id, name);
        if (exist)
            throw new IllegalArgumentException("名称【" + name + "】已存在,请重新输入！");

        int categoryId = data.getCategoryId();
        Optional<OfferingCategory> categoryOpt = _CategoryRepository.findById(categoryId);
        if (!categoryOpt.isPresent())
            throw new IllegalArgumentException("未找到Id【" + categoryId + "】的商品分类,请重新输入！");

        //设置商品分类与商品的关系
        OfferingCategory offeringCategory = categoryOpt.get();
        model.setCategory(offeringCategory);
        //设置商品编码
        if (StringExtensions.isNullOrEmpty(model.getOfferingCode())) {
            SeedEntity seedEntity = getRegularDateVal("Offering", 1);
            String offeringCode = seedEntity.getSeedValue();
            model.setOfferingCode(offeringCode);
        }

        //设置商品与商品属性的关系，包括：添加新的商品属性、更新老的商品属性
        if (model.getOfferingProperties() != null && model.getOfferingProperties().size() > 0) {
            for (OfferingProperty property : model.getOfferingProperties()) {
                property.setOffering(model);
                if (property.getId() != 0) {
                    //已经存在与数据库，重新attach到session中进行更新数据
                    entityManager.merge(property);
                }
            }
        }
        //设置商品与商品属性的关系，包括：删除商品图片属性
        if (data.getDeletedOfferingPropertyIds() != null && data.getDeletedOfferingPropertyIds().size() > 0) {
            _OfferingPropertyRepository.deleteByIdIn(data.getDeletedOfferingPropertyIds());
        }

        //新增或是更新商品数据
        if (model.getOfferingId() == 0) {
            //新增
            offeringCategory.getOfferings().add(model);
        } else {
            //已经存在与数据库，重新attach到session中进行更新数据
            entityManager.merge(model);
        }

        //保存数据至数据库
        _OfferingRepository.save(model);

        //设置商品日志
        OfferingOperationLog log = getBasicLog(model, currentUserId, currentUserName);
        String logMsg = model.getOfferingId() == 0
                ? String.format("用户【%s】于【%tF %tT】创建商品【%s--%s】成功",
                currentUserName, log.getOperateDate(), log.getOperateDate(),
                model.getOfferingCode(), model.getOfferingName())
                : String.format("用户【%s】于【%tF %tT】修改商品【%s--%s】成功",
                currentUserName, log.getOperateDate(), log.getOperateDate(),
                model.getOfferingCode(), model.getOfferingName());
        log.setRemark(logMsg);
        //model.getOfferingOperationLogs().add(log);
        _OfferingLogRepository.save(log);

        entityManager.flush();
        return true;

    }

    @Override
    public boolean RemoveOfferingImageById(boolean isSingleOffering, int id) {
        _OfferingPropertyRepository.deleteById(id);
        return true;
    }

    /*------------------------------------------商品日志-----------------------------------------------*/
    @Override
    public PaginatedBaseDTO<OfferingOperationLogDTO> findPaginatedOfferingLogsByFilter(
            int pageIndex, int pageSize, String code, String name) {

        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<OfferingOperationLog> data = _OfferingLogRepository.findAll(new Specification<OfferingOperationLog>() {
            private static final long serialVersionUID = 924729964744553669L;

            @Override
            public Predicate toPredicate(Root<OfferingOperationLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringExtensions.isNullOrEmpty(code)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("offeringCode"), "%" + code + "%")));
                }
                if (!StringExtensions.isNullOrEmpty(name)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("offeringName"), "%" + name + "%")));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);

        long total = data.getTotalElements();
        List<OfferingOperationLogDTO> rows = _OfferingLogMapping.from(data.getContent());
        return new PaginatedBaseDTO<OfferingOperationLogDTO>(pageIndex, pageSize, total, rows);
    }
}
