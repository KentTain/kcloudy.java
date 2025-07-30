package kc.service.codegenerate;

import kc.dataaccess.codegenerate.IModelChangeLogRepository;
import kc.dto.PaginatedBaseDTO;
import kc.dto.codegenerate.ModelChangeLogDTO;
import kc.enums.codegenerate.ModelType;
import kc.framework.extension.StringExtensions;
import kc.mapping.codegenerate.ModelChangeLogMapping;
import kc.model.codegenerate.ModelChangeLog;
import kc.service.base.ServiceBase;
import kc.service.util.TreeNodeUtil;
import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
@lombok.extern.slf4j.Slf4j
public class ModelChangeLogService extends ServiceBase implements IModelChangeLogService {

    private IModelChangeLogRepository _logRepository;
    private ModelChangeLogMapping _logMapping;

    @Autowired
    public ModelChangeLogService(IGlobalConfigApiService globalConfigApiService,
                                 IModelChangeLogRepository _logRepository,
                                 ModelChangeLogMapping _logMapping) {
        super(globalConfigApiService);
        this._logRepository = _logRepository;
        this._logMapping = _logMapping;
    }

    @Override
    public PaginatedBaseDTO<ModelChangeLogDTO> findPaginatedModelLogsByFilter(
            int pageIndex, int pageSize, ModelType type, String name, String code) {

        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<ModelChangeLog> data = _logRepository.findAll(new Specification<ModelChangeLog>() {
            private static final long serialVersionUID = 924729964744553669L;

            @Override
            public Predicate toPredicate(Root<ModelChangeLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (null != type) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("modelType"), type.getIndex())));
                }
                if (!StringExtensions.isNullOrEmpty(code)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("referenceId"), code)));
                }
                if (!StringExtensions.isNullOrEmpty(name)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("referenceName"), "%" + name + "%")));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);

        long total = data.getTotalElements();
        List<ModelChangeLogDTO> rows = _logMapping.fromModelChangeLogList(data.getContent());
        return new PaginatedBaseDTO<>(pageIndex, pageSize, total, rows);
    }
}
