package kc.dataaccess.account;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kc.model.account.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User>{
	@Query("FROM User u "
			+ "LEFT OUTER JOIN FETCH u.roles r "
			+ "LEFT OUTER JOIN FETCH u.organizations o "
			+ "WHERE u.id = :userId")
	User findWithOrgsAndRolesById(@Param("userId") String userId);
	
	@Query("FROM User u "
			+ "LEFT OUTER JOIN FETCH u.organizations o "
			+ "WHERE u.id = :userId")
	User findById(@Param("userId") String userId);
	
	@Query("FROM User u "
			+ "LEFT OUTER JOIN FETCH u.roles r "
			+ "LEFT OUTER JOIN FETCH u.organizations o "
			+ "WHERE u.userName = :userName")
	User findByUserName(@Param("userName") String userName);
	
	@EntityGraph(value = "Graph.User.Roles", type = EntityGraph.EntityGraphType.FETCH)
	@Query("FROM User u WHERE u.email like :email "
			+ "and u.phoneNumber like :phone "
			+ "and u.displayName like :name "
			+ "and u.status = :status "
			+ "and u.positionLevel = :position")
	Page<User> findAllByFilter(Pageable pageable, @Param("email") String email, @Param("phone") String phone, 
			@Param("name") String name, @Param("status") String status, @Param("position") Integer position);

	@Query("FROM User d JOIN d.roles r WHERE r.id IN (:roleIds) ")
	List<User> findUsersByRoleIds(@Param("roleIds") List<String> roleIds);
	@Query("FROM User u "
			+ "LEFT OUTER JOIN FETCH u.roles r "
			+ "LEFT OUTER JOIN FETCH u.organizations o ")
	List<User> FindAllDetailUsers();

	boolean existsByUserName(String userName);
	boolean existsByEmail(String email);
	boolean existsByPhoneNumber(String phoneNumber);
}
