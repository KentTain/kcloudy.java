package kc.service.offering;

import kc.dto.PaginatedBaseDTO;
import kc.framework.base.SeedEntity;
import kc.framework.enums.ProcessLogType;
import kc.framework.extension.DateExtensions;
import kc.framework.extension.StringExtensions;
import kc.model.offering.*;
import kc.enums.offering.*;
import kc.dto.offering.*;
import kc.dataaccess.offering.*;
import kc.mapping.offering.*;
import kc.service.base.ServiceBase;

import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
    private final CategoryMapping _CategoryMapping;

    private final IOfferingRepository _OfferingRepository;
    private final OfferingMapping _OfferingMapping;

    private final IOfferingPropertyRepository _OfferingPropertyRepository;

    private final IProductRepository _ProductRepository;
    private final ProductMapping _ProductMapping;

    private final IProductPropertyRepository _ProductPropertyRepository;

    private final IOfferingOperationLogRepository _OfferingLogRepository;
    private final OfferingOperationLogMapping _OfferingLogMapping;

    public OfferingService(IGlobalConfigApiService globalConfigApiService,
                           ICategoryRepository _CategoryRepository,
                           CategoryMapping _CategoryMapping,
                           IOfferingRepository _OfferingRepository,
                           OfferingMapping _OfferingMapping,
                           IOfferingPropertyRepository _OfferingPropertyRepository,
                           IProductRepository _ProductRepository,
                           ProductMapping _ProductMapping,
                           IProductPropertyRepository _ProductPropertyRepository,
                           IOfferingOperationLogRepository _OfferingLogRepository,
                           OfferingOperationLogMapping _OfferingLogMapping) {
        super(globalConfigApiService);
        this._CategoryRepository = _CategoryRepository;
        this._CategoryMapping = _CategoryMapping;
        this._OfferingRepository = _OfferingRepository;
        this._OfferingMapping = _OfferingMapping;
        this._OfferingPropertyRepository = _OfferingPropertyRepository;
        this._ProductRepository = _ProductRepository;
        this._ProductMapping = _ProductMapping;
        this._ProductPropertyRepository = _ProductPropertyRepository;
        this._OfferingLogRepository = _OfferingLogRepository;
        this._OfferingLogMapping = _OfferingLogMapping;
    }

    /*------------------------------------------商品-----------------------------------------------*/
    @Override
    public List<OfferingDTO> findAllOfferingsByIds(List<Integer> ids) {
        List<Offering> data = _OfferingRepository.findAll(new Specification<Offering>() {
            private static final long serialVersionUID = 924729964744553669L;

            @Override
            public Predicate toPredicate(Root<Offering> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (ids != null && ids.size() > 0) {
                    Path<Object> path = root.get("offeringId");//定义查询的字段
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                    for (int i = 0; i < ids.size(); i++) {
                        in.value(ids.get(i));//存入值
                    }
                    predicates.add(criteriaBuilder.and(criteriaBuilder.and(in)));//存入条件集合里
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });

        return _OfferingMapping.from(data);
    }

    @Override
    public PaginatedBaseDTO<OfferingDTO> findPaginatedOfferingsByType(int pageIndex, int pageSize, OfferingType offeringType) {
        Integer s = offeringType != null
                ? offeringType.getIndex()
                : OfferingType.Sofeware.getIndex();

        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Offering> data = _OfferingRepository.findAllByType(pageable, s);

        long total = data.getTotalElements();
        List<OfferingDTO> rows = _OfferingMapping.from(data.getContent());
        return new PaginatedBaseDTO<>(pageIndex, pageSize, total, rows);
    }

    @Override
    public PaginatedBaseDTO<OfferingDTO> findPaginatedOfferingsByFilter(
            int pageIndex, int pageSize, String code, String name, OfferingStatus status) {
        Integer s = null;
        if (status != null)
            s = status.getIndex();

        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Offering> data = status != null
                ? _OfferingRepository.findAllByFilter(pageable, code == null ? "" : code, name == null ? "" : name, s)
                : _OfferingRepository.findAllByFilter(pageable, code == null ? "" : code, name == null ? "" : name);

        long total = data.getTotalElements();
        List<OfferingDTO> rows = _OfferingMapping.from(data.getContent());
        return new PaginatedBaseDTO<>(pageIndex, pageSize, total, rows);
    }


    @Override
    public OfferingDTO GetOfferingById(int id) {
        Optional<Offering> data = _OfferingRepository.findById(id);
        return data.map(_OfferingMapping::from).orElse(null);
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
        _OfferingRepository.saveAndFlush(model);
        return true;
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
        _OfferingRepository.saveAndFlush(model);
        return true;
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
        Optional<Category> categoryOpt = _CategoryRepository.findById(categoryId);
        if (!categoryOpt.isPresent())
            throw new IllegalArgumentException("未找到Id【" + categoryId + "】的商品分类,请重新输入！");

        //设置商品分类与商品的关系
        Category category = categoryOpt.get();
        model.setCategory(category);
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
        if (data.getDeletedOfferingPropertyIds() != null && data.getDeletedOfferingPropertyIds().size() > 0){
            _OfferingPropertyRepository.deleteByIdIn(data.getDeletedOfferingPropertyIds());
        }

        //设置商品与产品的关系，包括：添加新的产品规格及图片属性，更新老的产品规格及图片属性
        if (model.getProducts() != null && model.getProducts().size() > 0) {
            //List<Product> dbProducts = _ProductRepository.findAllByOffering_OfferingId(model.getOfferingId());

            for (Product product : model.getProducts()) {
                product.setOffering(model);

                if (StringExtensions.isNullOrEmpty(product.getProductCode())) {
                    SeedEntity seedEntity = getRegularDateVal("Product", 1);
                    String productCode = seedEntity.getSeedValue();
                    product.setProductCode(productCode);
                }

                //设置产品与产品属性的关系
                if (product.getProductProperties() != null && product.getProductProperties().size() > 0) {
                    for (ProductProperty property : product.getProductProperties()) {
                        property.setProduct(product);
//                        if (property.getId() != 0) {
//                            //已经存在与数据库，重新attach到session中进行更新数据
//                            entityManager.merge(property);
//                        }
                    }
                }


                if (product.getProductId() != 0) {
                    //已经存在与数据库，重新attach到session中进行更新数据
                    entityManager.merge(product);
                }

            }
        }
        //设置商品与产品的关系，包括：删除产品规格及图片性
        if (data.getDeletedProductPropertyIds() != null && data.getDeletedProductPropertyIds().size() > 0){
            _ProductPropertyRepository.deleteByIdIn(data.getDeletedProductPropertyIds());
        }

        //新增或是更新商品数据
        if (model.getOfferingId() == 0) {
            //新增
            category.getOfferings().add(model);
        } else {
            //已经存在与数据库，重新attach到session中进行更新数据
            entityManager.merge(model);
        }

        //保存数据至数据库
        if (model.getProducts() != null && model.getProducts().size() > 0) {
            _OfferingRepository.save(model);
            //_ProductRepository.saveAll(model.getProducts());
        } else {
            _OfferingRepository.save(model);
        }

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
    public boolean RemoveProductById(int id, String currentUserId, String currentUserName) {
        Product product = _ProductRepository.getOne(id);
        Offering offering = product.getOffering();

        OfferingOperationLog log = getBasicLog(offering, currentUserId, currentUserName);
        log.setRemark(String.format("用户【%s】于【%tF %tT】删除商品【%s】子产品【%s】成功",
                currentUserName, log.getOperateDate(), log.getOperateDate(), offering.getOfferingName(), product.getProductId()));
        _OfferingLogRepository.save(log);

        product.setDeleted(true);
        _ProductRepository.saveAndFlush(product);

        return true;
    }

    @Override
    public boolean RemoveOfferingImageById(boolean isSingleOffering, int id) {
        if(isSingleOffering){
            _OfferingPropertyRepository.deleteById(id);
        }else{
            _ProductPropertyRepository.deleteById(id);
        }
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
        return new PaginatedBaseDTO<>(pageIndex, pageSize, total, rows);
    }
}
