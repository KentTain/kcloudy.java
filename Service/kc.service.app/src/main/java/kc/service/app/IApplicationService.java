package kc.service.app;

import kc.dto.PaginatedBaseDTO;
import kc.dto.app.ApplicationDTO;
import kc.dto.app.ApplicationLogDTO;
import kc.enums.app.AppLogType;
import kc.service.base.IServiceBase;

import java.util.List;

public interface IApplicationService  extends IServiceBase {
	List<ApplicationDTO> findAll();
	PaginatedBaseDTO<ApplicationDTO> findPaginatedApplicationsByAppName(int pageIndex, int pageSize, String name);
	ApplicationDTO GetApplicationById(String id);
	boolean SaveApplication(ApplicationDTO model);
	boolean SoftRemoveApplicationById(String id, String currentUserId, String currentUserName);

	PaginatedBaseDTO<ApplicationLogDTO> FindPaginatedApplicationLogs(int pageIndex, int pageSize, AppLogType appLogType, String name);
}
