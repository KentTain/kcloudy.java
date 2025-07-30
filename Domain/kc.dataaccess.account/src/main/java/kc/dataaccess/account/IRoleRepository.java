package kc.dataaccess.account;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

//import javax.persistence.LockModeType;

//import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kc.model.account.Role;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role>{
	//@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	@Query("FROM Role r WHERE r.isDeleted != 1")
	List<Role> findAll();
	List<Role> findByIdIn(List<String> addList);
	
	@Query("FROM Role r "
			+ "LEFT OUTER JOIN FETCH r.menuNodes m "
			+ "WHERE r.id in (:roleIds)")
	List<Role> findWithMenuNodesByIdIn(@Param("roleIds") List<String> roleIds);
	
	@Query("FROM Role r "
			+ "LEFT OUTER JOIN FETCH r.permissions p "
			+ " WHERE r.id in (:roleIds)")
	List<Role> findWithPermissionsByIdIn(@Param("roleIds") List<String> roleIds);
	
	Role findById(String id);
	
	@Query("SELECT d FROM Role d "
			+ "JOIN FETCH d.users "
			+ "JOIN FETCH d.menuNodes "
			+ "JOIN FETCH d.permissions "
			+ " WHERE d.id = :id")
	Role GetDetailRoleById(String id);
	
	@Query("SELECT CASE WHEN COUNT(c.name) > 0 THEN true ELSE false END FROM Organization c WHERE c.name = :name")
    boolean existsByName(@Param("name") String name);
	
	@Query("SELECT CASE WHEN COUNT(c.name) > 0 THEN true ELSE false END FROM Organization c WHERE c.name = :name and c.id != :id")
    boolean existsByIdAndName(@Param("id") String id, @Param("name") String name);

	
}
