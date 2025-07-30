package kc.dataaccess.app;

import kc.model.app.ApplicationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IApplicationLogRepository extends JpaRepository<ApplicationLog, Integer>, JpaSpecificationExecutor<ApplicationLog> {

	List<ApplicationLog> findAllByReferenceNameContains(String name);

	Page<ApplicationLog> findAllByAppLogType(Pageable pageable, int appLogType);

	Page<ApplicationLog> findAllByReferenceNameContains(Pageable pageable, String name);

	Page<ApplicationLog> findAllByAppLogTypeAndReferenceNameContains(Pageable pageable, int appLogType, String name);
}
