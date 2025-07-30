package kc.dataaccess.dict;

import kc.framework.base.ConfigAttribute;
import kc.model.dict.DictType;
import kc.model.dict.DictValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDictValueRepository extends JpaRepository<DictValue, Integer>{

	DictValue findByName(String name);
	
	@Query("FROM DictValue c where c.dictType.id = :id")
	List<DictValue> findByDictTypeId(@Param("id") int id);

	Page<DictValue> findAll(Pageable pageable);

	Page<DictValue> findAllByNameContainingAndDictTypeId(Pageable pageable, String name, Integer typeId);

	Page<DictValue> findAllByNameContaining(Pageable pageable, String name);
}
