package kc.database.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kc.database.TestObj;

@Repository
public interface ITestRepository  extends JpaRepository<TestObj, Long> {
	@Transactional(timeout = 10)
	@Query(value = "select top 1 * from test_user u WITH (ROWLOCK) where u.user_name = :userName", nativeQuery = true)
	TestObj findByUserName(@Param("userName") String userName);

	@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	TestObj findByUserId(String userId);
}

