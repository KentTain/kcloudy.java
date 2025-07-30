package kc.dataaccess.dict;

import kc.database.repository.ITreeNodeRepository;
import kc.model.dict.DictValue;
import kc.model.dict.IndustryClassfication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IIndustryClassficationRepository extends ITreeNodeRepository<IndustryClassfication, Integer>{

	IndustryClassfication findByName(String name);
	
	@Query("FROM ConfigAttribute c where c.configEntity.configId = :configId")
	List<IndustryClassfication> findByConfigId(@Param("configId") int configId);
}
