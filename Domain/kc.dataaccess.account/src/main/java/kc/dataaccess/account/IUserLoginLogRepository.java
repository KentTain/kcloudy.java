package kc.dataaccess.account;

import kc.model.account.UserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserLoginLogRepository extends JpaRepository<UserLoginLog, Integer>, JpaSpecificationExecutor<UserLoginLog> {

}
