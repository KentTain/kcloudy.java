package kc.dataaccess.codegenerate;

import kc.database.repository.ITreeNodeRepository;
import kc.model.codegenerate.ModelCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IModelCategoryRepository extends ITreeNodeRepository<ModelCategory, Integer>, JpaSpecificationExecutor<ModelCategory>{
	//@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT d FROM ModelCategory d LEFT OUTER JOIN FETCH d.parentNode WHERE d.id = :id")
	ModelCategory findWithParentById(int id);

	List<ModelCategory> findAllByModelTypeAndNameContains(int modelType, String name);

	@Query("FROM ModelCategory d WHERE d.modelType = :modelType and  d.applicationId = :applicationId and d.name like %:name%")
	List<ModelCategory> findAllByModelTypeAndApplicationId(String applicationId, int modelType, String name);

	@Query("SELECT CASE WHEN COUNT(c.name) > 0 THEN true ELSE false END FROM ModelCategory c WHERE c.name = :name ")
	boolean existsByName(@Param("name") String name);

	@Query("SELECT CASE WHEN COUNT(c.name) > 0 THEN true ELSE false END FROM ModelCategory c WHERE c.name = :name and c.id != :id")
	boolean existsByIdAndName(@Param("id") int id, @Param("name") String name);
}
