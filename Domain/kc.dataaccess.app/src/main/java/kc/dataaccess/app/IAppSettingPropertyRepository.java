package kc.dataaccess.app;

import java.util.List;

import kc.model.app.AppSettingProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface IAppSettingPropertyRepository extends JpaRepository<AppSettingProperty, Integer>{

	AppSettingProperty findByName(String name);
	
	@Query("FROM AppSettingProperty c where c.appSetting.propertyId = :appSettingId")
	List<AppSettingProperty> findByAppSettingId(@Param("appSettingId") int appSettingId);
}
