package kc.service.app;

import kc.dataaccess.app.IApplicationLogRepository;
import kc.dataaccess.app.IApplicationRepository;
import kc.dto.PaginatedBaseDTO;
import kc.dto.app.ApplicationDTO;
import kc.dto.app.ApplicationLogDTO;
import kc.enums.app.AppLogType;
import kc.framework.extension.StringExtensions;
import kc.mapping.app.ApplicationLogMapping;
import kc.mapping.app.ApplicationMapping;
import kc.model.app.Application;
import kc.model.app.ApplicationLog;
import kc.service.base.ServiceBase;
import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ApplicationService extends ServiceBase implements IApplicationService {

    private IApplicationRepository _appRepository;
    private IApplicationLogRepository _applicationLogRepository;
    private ApplicationLogMapping _applicationLogMapping;
    private ApplicationMapping _appMapping;

    @Autowired
    public ApplicationService(IGlobalConfigApiService globalConfigApiService,
                              IApplicationRepository _appRepository,
                              IApplicationLogRepository _applicationLogRepository,
                              ApplicationLogMapping _applicationLogMapping,
                              ApplicationMapping _appMapping) {
        super(globalConfigApiService);
        this._appRepository = _appRepository;
        this._applicationLogRepository = _applicationLogRepository;
        this._applicationLogMapping = _applicationLogMapping;
        this._appMapping = _appMapping;
    }

    @Override
    public List<ApplicationDTO> findAll() {
        List<Application> data = _appRepository.findAll();
        return _appMapping.fromApplicationDtoList(data);
    }

    @Override
    public PaginatedBaseDTO<ApplicationDTO> findPaginatedApplicationsByAppName(int pageIndex, int pageSize, String name) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Application> data = null;
        if (!StringExtensions.isNullOrEmpty(name)) {
            data = _appRepository.findAllByApplicationNameContains(pageable, name);
        } else {
            data = _appRepository.findAll(pageable);
        }

        int total = data.getSize();
        List<ApplicationDTO> rows = _appMapping.fromApplicationDtoList(data.getContent());
        return new PaginatedBaseDTO<>(pageIndex, pageSize, total, rows);
    }

    @Override
    public ApplicationDTO GetApplicationById(String id) {
        Application data = _appRepository.findById(id).orElse(null);
        return _appMapping.fromApplication(data);
    }

    @Override
    @Transactional
    public boolean SaveApplication(ApplicationDTO model) {
        Application data = _appMapping.toApplication(model);
        if (StringExtensions.isNullOrEmpty(data.getApplicationId())) {
            data.setApplicationId(UUID.randomUUID().toString());
        }
        _appRepository.save(data);

        //添加日志
        ApplicationLog log = new ApplicationLog();
        log.setAppLogType(AppLogType.Application.getIndex());
        log.setReferenceId(String.valueOf(data.getApplicationId()));
        log.setReferenceName(String.valueOf(data.getApplicationName()));
        log.setOperatorId(model.getCreatedBy());
        log.setOperator(model.getCreatedName());
        log.setRemark(model.isEditMode()
                ? String.format("编辑应用数据: %s", data.getApplicationName())
                : String.format("新增应用数据: %s", data.getApplicationName()));
        _applicationLogRepository.saveAndFlush(log);
        return true;
    }

    @Override
    public boolean SoftRemoveApplicationById(String id, String currentUserId, String currentUserName) {
        Application data = _appRepository.findById(id).orElse(null);
        data.setDeleted(true);
        _appRepository.save(data);

        //添加日志
        ApplicationLog log = new ApplicationLog();
        log.setAppLogType(AppLogType.Application.getIndex());
        log.setReferenceId(String.valueOf(data.getApplicationId()));
        log.setReferenceName(String.valueOf(data.getApplicationName()));
        log.setOperatorId(data.getCreatedBy());
        log.setOperator(data.getCreatedName());
        log.setRemark(String.format("删除应用数据: %s", data.getApplicationName()));
        _applicationLogRepository.saveAndFlush(log);
        return true;
    }

    @Override
    public PaginatedBaseDTO<ApplicationLogDTO> FindPaginatedApplicationLogs(int pageIndex, int pageSize, AppLogType appLogType, String name) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<ApplicationLog> data = _applicationLogRepository.findAll(new Specification<ApplicationLog>() {
            private static final long serialVersionUID = 924729964744553669L;

            @Override
            public Predicate toPredicate(Root<ApplicationLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringExtensions.isNullOrEmpty(name)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + name + "%")));
                }
                if (null != appLogType) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("appLogType"), appLogType.getIndex())));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);

        long total = data.getTotalElements();
        List<ApplicationLogDTO> rows = _applicationLogMapping.fromApplicationLogList(data.getContent());
        return new PaginatedBaseDTO<>(pageIndex, pageSize, total, rows);
    }
}
