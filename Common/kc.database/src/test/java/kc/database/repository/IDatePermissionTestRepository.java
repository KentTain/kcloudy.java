package kc.database.repository;

import kc.database.DataPermissionTestObj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface IDatePermissionTestRepository extends JpaRepository<DataPermissionTestObj, Long> , JpaSpecificationExecutor<DataPermissionTestObj> {

	@Query(value = "Select dp from test_datePermission dp where dp.orgIds like %?1%", nativeQuery = true)
    List<DataPermissionTestObj> findByOrgIds(String orgId);

    List<DataPermissionTestObj> findByOrgIdsIn(List<String> orgId);

	DataPermissionTestObj findByName(String name);
}

