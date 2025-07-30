package kc.dataaccess.codegenerate;

import kc.model.codegenerate.ModelChangeLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IModelChangeLogRepository extends JpaRepository<ModelChangeLog, Integer>, JpaSpecificationExecutor<ModelChangeLog> {

	List<ModelChangeLog> findAllByReferenceNameContains(String name);

	Page<ModelChangeLog> findAllByModelType(Pageable pageable, int modelType);

	Page<ModelChangeLog> findAllByReferenceNameContains(Pageable pageable, String name);

	Page<ModelChangeLog> findAllByModelTypeAndReferenceNameContains(Pageable pageable, int modelType, String name);
}
