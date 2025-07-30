package kc.dataaccess.config;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kc.framework.base.ConfigAttribute;

@Repository
public interface IConfigAttributeRepository extends JpaRepository<ConfigAttribute, Integer>{

	ConfigAttribute findByName(String name);
	
	@Query("FROM ConfigAttribute c where c.configEntity.configId = :configId")
	List<ConfigAttribute> findByConfigId(@Param("configId") int configId);
}
