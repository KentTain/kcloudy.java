package kc.dataaccess.config;

import kc.framework.base.SeedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kc.model.config.SysSequence;

import java.util.List;

@Repository
public interface ISysSequenceRepository extends JpaRepository<kc.model.config.SysSequence, String>{

	SysSequence findByPreFixString(String preFix);
	
	SysSequence findByPostFixString(String postFix);

	SysSequence findBySequenceNameAndStepValue(String sequenceName, int step);

	@Query(value = "EXECUTE [Utility_GetRegularDateVal] @seqname=:seqname, @length=:length, @currdate=:currdate, @step=:step", nativeQuery = true)
	Object[] getSeedEntityByQuery(@Param("seqname") String seqname, @Param("length") int length, @Param("currdate") String currdate, @Param("step") int step);

}
