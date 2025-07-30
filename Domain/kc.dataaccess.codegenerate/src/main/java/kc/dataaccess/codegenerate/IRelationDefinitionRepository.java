package kc.dataaccess.codegenerate;

import kc.model.codegenerate.RelationDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IRelationDefinitionRepository extends JpaRepository<RelationDefinition, Integer>, JpaSpecificationExecutor<RelationDefinition> {
	@EntityGraph(value = "Graph.RelationDefinition.RelationDefDetails", type = EntityGraph.EntityGraphType.FETCH)
	RelationDefinition findDetailById(int id);

	@EntityGraph(value = "Graph.RelationDefinition.RelationDefDetails", type = EntityGraph.EntityGraphType.FETCH)
	Page<RelationDefinition> findAllByNameContaining(Pageable pageable, String name);

	@EntityGraph(value = "Graph.RelationDefinition.RelationDefDetails", type = EntityGraph.EntityGraphType.FETCH)
	Page<RelationDefinition> findAllByNameContainingAndDisplayName(Pageable pageable, String name, String displayName);

}
