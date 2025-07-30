package kc.dataaccess.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import kc.model.account.UserTracingLog;

@Repository
public interface IUserTracingLogRepository extends JpaRepository<UserTracingLog, Integer>, JpaSpecificationExecutor<UserTracingLog> {

}
