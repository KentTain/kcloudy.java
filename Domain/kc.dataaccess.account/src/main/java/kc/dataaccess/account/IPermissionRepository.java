package kc.dataaccess.account;


import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kc.database.repository.ITreeNodeRepository;
import kc.model.account.Permission;

@Repository
public interface IPermissionRepository extends ITreeNodeRepository<Permission, Integer>, JpaSpecificationExecutor<Permission> {
	//@EntityGraph(value = "Graph.TreeNode.ChildNodes", type = EntityGraph.EntityGraphType.FETCH)
	//@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	Permission findById(int id);
	
	@Query("FROM Permission m LEFT OUTER JOIN FETCH m.parentNode "
			+ "LEFT OUTER JOIN FETCH m.roles r"
			+ " WHERE m.id = :id")
    Permission GetDetailPermissionById(int id);
	
	@Query("FROM Permission d JOIN d.roles r WHERE r.id in (:roleIds)")
	List<Permission> findPermissionsByRoleIds(List<String> roleIds);
	
	@Query("SELECT CASE WHEN COUNT(c.name) > 0 THEN true ELSE false END FROM Permission c WHERE c.name = :name")
    boolean existsByName(@Param("name") String name);
	
	@Query("SELECT CASE WHEN COUNT(c.name) > 0 THEN true ELSE false END FROM Permission c WHERE c.name = :name and c.id != :id")
    boolean existsByIdAndName(@Param("id") int id, @Param("name") String name);
}
