package kc.dataaccess.app;

import kc.model.app.AppSetting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IAppSettingRepository extends JpaRepository<AppSetting, Integer>, JpaSpecificationExecutor<AppSetting> {
	@EntityGraph(value = "Graph.AppSetting.PropertyAttributeList", type = EntityGraph.EntityGraphType.FETCH)
	AppSetting findByCode(String applicationName);

	@EntityGraph(value = "Graph.AppSetting.PropertyAttributeList", type = EntityGraph.EntityGraphType.FETCH)
	AppSetting findByPropertyId(int propertyId);

	@EntityGraph(value = "Graph.AppSetting.PropertyAttributeList", type = EntityGraph.EntityGraphType.FETCH)
	Page<AppSetting> findAll(Pageable pageable);

	@EntityGraph(value = "Graph.AppSetting.PropertyAttributeList", type = EntityGraph.EntityGraphType.FETCH)
	Page<AppSetting> findAllByApplication_ApplicationId(UUID applicationId, Pageable pageable);

	@EntityGraph(value = "Graph.AppSetting.PropertyAttributeList", type = EntityGraph.EntityGraphType.FETCH)
	Page<AppSetting> findAllByNameAndApplication_ApplicationId(String name, UUID application_applicationId, Pageable pageable);

}
