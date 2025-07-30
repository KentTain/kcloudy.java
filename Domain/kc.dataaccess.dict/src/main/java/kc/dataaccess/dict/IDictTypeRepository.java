package kc.dataaccess.dict;

import kc.model.dict.DictType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDictTypeRepository extends JpaRepository<DictType, Integer>{

	@EntityGraph(value = "Graph.DictType.DictValues", type = EntityGraph.EntityGraphType.FETCH)
	DictType findDetailByName(String name);

	@EntityGraph(value = "Graph.DictType.DictValues", type = EntityGraph.EntityGraphType.FETCH)
	DictType findDetailById(int it);
}
