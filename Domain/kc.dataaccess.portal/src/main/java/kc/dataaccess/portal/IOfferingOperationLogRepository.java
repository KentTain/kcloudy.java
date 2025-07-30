package kc.dataaccess.portal;

import kc.model.portal.OfferingOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IOfferingOperationLogRepository extends JpaRepository<OfferingOperationLog, Integer>, JpaSpecificationExecutor<OfferingOperationLog> {

}
