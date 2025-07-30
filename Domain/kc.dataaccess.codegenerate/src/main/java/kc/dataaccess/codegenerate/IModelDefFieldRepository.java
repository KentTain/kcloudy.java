package kc.dataaccess.codegenerate;

import kc.model.codegenerate.ModelDefField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface IModelDefFieldRepository extends JpaRepository<ModelDefField, Integer>, JpaSpecificationExecutor<ModelDefField> {

	ModelDefField findByName(String name);

	List<ModelDefField> findAllByPropertyAttributeIdIn(List<Integer> ids);

	List<ModelDefField> findAllByModelDefinition_PropertyId(@Param("defId") int defId);

	@Query("FROM ModelDefField f where f.modelDefinition.propertyId = :defId")
	List<ModelDefField> findAllByModelDefId(@Param("defId") int defId);

	@Modifying
	@Transactional
	@Query("delete from ModelDefField f where f.modelDefinition.propertyId = :defId")
	int deleteAllByModelDefId(int defId);

	@Modifying
	@Transactional
	@Query("delete from ModelDefField f where f.modelDefinition.propertyId in (?1)")
	int deleteAllByModelDefIds(List<Integer> modelDefIds);

}
