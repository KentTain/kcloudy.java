package kc.dataaccess.config;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kc.framework.base.ConfigEntity;
import kc.framework.enums.ConfigType;

@Repository
public interface IConfigEntityRepository extends JpaRepository<ConfigEntity, Integer>{
	@EntityGraph(value = "Graph.ConfigEntity.ConfigAttributes", type = EntityGraph.EntityGraphType.FETCH)
	ConfigEntity findByConfigId(int configId);
	
	ConfigEntity findByConfigName(String configName);
	
	@EntityGraph(value = "Graph.ConfigEntity.ConfigAttributes", type = EntityGraph.EntityGraphType.FETCH)
	Page<ConfigEntity> findAll(Pageable pageable);

	@EntityGraph(value = "Graph.ConfigEntity.ConfigAttributes", type = EntityGraph.EntityGraphType.FETCH)
	Page<ConfigEntity> findAllByConfigNameContainingAndConfigType(Pageable pageable, String configName, ConfigType configType);
	
	@EntityGraph(value = "Graph.ConfigEntity.ConfigAttributes", type = EntityGraph.EntityGraphType.FETCH)
	Page<ConfigEntity> findAllByConfigNameContaining(Pageable pageable, String configName);
	
	@EntityGraph(value = "Graph.ConfigEntity.ConfigAttributes", type = EntityGraph.EntityGraphType.FETCH)
	Page<ConfigEntity> findAllByConfigNameContainingAndConfigTypeIn(Pageable pageable, String configName, Collection<ConfigType> configTypes);
}
