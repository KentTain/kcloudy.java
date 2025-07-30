package kc.dataaccess.codegenerate;

import kc.model.codegenerate.ModelDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IModelDefinitionRepository extends JpaRepository<ModelDefinition, Integer>, JpaSpecificationExecutor<ModelDefinition> {
	@Query("SELECT CASE WHEN COUNT(c.name) > 0 THEN true ELSE false END FROM ModelDefinition c WHERE c.applicationId=:appId and c.name = :name ")
	boolean existsByName(@Param("appId") String appId, @Param("name") String name);

	@Query("SELECT CASE WHEN COUNT(c.tableName) > 0 THEN true ELSE false END FROM ModelDefinition c WHERE c.applicationId=:appId and c.tableName = :tableName")
	boolean existsByTableName(@Param("appId") String appId, @Param("tableName") String tableName);

	@EntityGraph(value = "Graph.ModelDefinition.PropertyAttributeList", type = EntityGraph.EntityGraphType.FETCH)
	ModelDefinition findByPropertyId(int id);

	@EntityGraph(value = "Graph.ModelDefinition.PropertyAttributeList", type = EntityGraph.EntityGraphType.FETCH)
	Page<ModelDefinition> findAll(Pageable pageable);

	@EntityGraph(value = "Graph.ModelDefinition.PropertyAttributeList", type = EntityGraph.EntityGraphType.FETCH)
	Page<ModelDefinition> findAllByNameContaining(Pageable pageable, String name);

	@EntityGraph(value = "Graph.ModelDefinition.PropertyAttributeList", type = EntityGraph.EntityGraphType.FETCH)
	Page<ModelDefinition> findAllByNameContainingAndDisplayName(Pageable pageable, String name, String displayName);

	@EntityGraph(value = "Graph.ModelDefinition.PropertyAttributeList", type = EntityGraph.EntityGraphType.FETCH)
	List<ModelDefinition> findAllByPropertyIdIn(List<Integer> ids);
}
