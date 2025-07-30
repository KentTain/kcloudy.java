package kc.dataaccess.account;


import java.util.List;

//import javax.persistence.LockModeType;

//import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kc.database.repository.ITreeNodeRepository;
import kc.model.account.Organization;

@Repository
public interface IOrganizationRepository extends ITreeNodeRepository<Organization, Integer>, JpaSpecificationExecutor<Organization> {
	//@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT d FROM Organization d LEFT OUTER JOIN FETCH d.parentNode WHERE d.id = :id")
    Organization findWithParentById(int id);

	@Query("SELECT d FROM Organization d WHERE d.organizationCode = :code")
	Organization findOneByOrganizationCode(String code);
	
	@Query("FROM Organization d JOIN d.users u WHERE u.id = :userId")
    List<Organization> findWithUsersByUserId(String userId);
	
	@Query("SELECT CASE WHEN COUNT(c.name) > 0 THEN true ELSE false END FROM Organization c WHERE c.name = :name")
    boolean existsByName(@Param("name") String name);
	
	@Query("SELECT CASE WHEN COUNT(c.name) > 0 THEN true ELSE false END FROM Organization c WHERE c.name = :name and c.id != :id")
    boolean existsByIdAndName(@Param("id") int id, @Param("name") String name);
}
