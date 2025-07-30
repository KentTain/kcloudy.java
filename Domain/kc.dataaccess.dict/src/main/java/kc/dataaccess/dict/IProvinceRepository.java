package kc.dataaccess.dict;

import kc.model.dict.DictType;
import kc.model.dict.Province;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProvinceRepository extends JpaRepository<Province, Integer>{

	@EntityGraph(value = "Graph.Province.Cities", type = EntityGraph.EntityGraphType.FETCH)
	Province findDetailByName(String name);

	@EntityGraph(value = "Graph.Province.Cities", type = EntityGraph.EntityGraphType.FETCH)
	Province findDetailByProvinceId(int it);
}
