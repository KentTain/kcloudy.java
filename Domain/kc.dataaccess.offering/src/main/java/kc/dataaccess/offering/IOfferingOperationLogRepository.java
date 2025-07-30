package kc.dataaccess.offering;

import kc.model.offering.OfferingOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IOfferingOperationLogRepository extends JpaRepository<OfferingOperationLog, Integer>, JpaSpecificationExecutor<OfferingOperationLog> {

}
