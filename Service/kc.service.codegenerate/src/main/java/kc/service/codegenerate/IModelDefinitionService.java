package kc.service.codegenerate;

import kc.dto.PaginatedBaseDTO;
import kc.dto.codegenerate.ModelDefFieldDTO;
import kc.dto.codegenerate.ModelDefinitionDTO;
import kc.enums.codegenerate.ModelBaseType;
import kc.service.base.IServiceBase;

import java.util.List;

public interface IModelDefinitionService extends IServiceBase {
    List<ModelDefinitionDTO> findAllModelDefinitions(
            Integer categoryId, ModelBaseType type, String name, String displayName, String tableName, String appId);

    PaginatedBaseDTO<ModelDefinitionDTO> findPaginatedModelDefinitions(
            int pageIndex, int pageSize, Integer categoryId, ModelBaseType type, String name, String displayName, String tableName, String appId);

    boolean existsModelDefName(String appId, Integer id, String name);

    boolean existsModelDefTableName(String appId, Integer id, String tableName);

    void validateModelDefinition(ModelDefinitionDTO model);

    ModelDefinitionDTO getModelDefinitionById(int id);

    boolean saveModelDefinitionWithFields(ModelDefinitionDTO model);

    boolean removeModelDefinitionById(int id, String currentUserId, String currentUserName);



    List<ModelDefFieldDTO> findAllModelDefFieldsByDefId(int id);

    ModelDefFieldDTO getModelDefFieldById(int id);

    boolean saveModelDefField(ModelDefFieldDTO data);

    boolean removeModelDefFieldById(int id, String currentUserId, String currentUserName);

}
