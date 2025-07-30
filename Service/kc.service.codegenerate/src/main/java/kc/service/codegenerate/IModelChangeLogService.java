package kc.service.codegenerate;

import kc.dto.PaginatedBaseDTO;
import kc.dto.codegenerate.ModelChangeLogDTO;
import kc.enums.codegenerate.ModelType;
import kc.service.base.IServiceBase;

import java.util.List;

public interface IModelChangeLogService extends IServiceBase {
	PaginatedBaseDTO<ModelChangeLogDTO> findPaginatedModelLogsByFilter(
			int pageIndex, int pageSize, ModelType type, String name, String code);
}
