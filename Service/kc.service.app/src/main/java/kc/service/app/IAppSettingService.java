package kc.service.app;

import java.util.List;

import kc.dto.PaginatedBaseDTO;
import kc.dto.app.AppSettingDTO;
import kc.dto.app.AppSettingPropertyDTO;
import kc.framework.enums.ConfigType;
import kc.service.base.IServiceBase;

public interface IAppSettingService  extends IServiceBase {
	List<AppSettingDTO> findAll();
	PaginatedBaseDTO<AppSettingDTO> findPaginatedAppSettingsByAppIdAndName(int pageIndex, int pageSize, String applicationId, String name);
	AppSettingDTO GetAppSettingById(int id);
	boolean SaveAppSetting(AppSettingDTO model);
	boolean SoftRemoveAppSettingById(int id, String currentUserId, String currentUserName);

	List<AppSettingPropertyDTO> GetAppSettingPropertiesBySettingId(int id);
    AppSettingPropertyDTO GetPropertyById(int id);
    boolean SaveAppSettingProperty(AppSettingPropertyDTO data);
    boolean SoftRemoveAppSettingPropertyById(int id);

}
