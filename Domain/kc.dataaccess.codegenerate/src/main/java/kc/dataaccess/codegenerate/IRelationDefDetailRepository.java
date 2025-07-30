package kc.dataaccess.codegenerate;

import kc.model.codegenerate.RelationDefDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface IRelationDefDetailRepository extends JpaRepository<RelationDefDetail, Integer>, JpaSpecificationExecutor<RelationDefDetail> {

	List<RelationDefDetail> findAllByRelationDefinition_Id(@Param("defId") int defId);

	@Modifying
	@Transactional
	@Query("delete from RelationDefDetail f where f.relationDefinition.id = :defId")
	int deleteAllByRelationDefId(int defId);

	@Modifying
	@Transactional
	@Query("delete from RelationDefDetail f where f.relationDefinition.id in (?1)")
	boolean deleteAllByRelationDefIds(List<Integer> defIds);
}
