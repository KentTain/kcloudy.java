package kc.service.codegenerate;

import kc.dto.PaginatedBaseDTO;
import kc.dto.codegenerate.RelationDefDetailDTO;
import kc.dto.codegenerate.RelationDefinitionDTO;
import kc.enums.codegenerate.ModelBaseType;
import kc.service.base.IServiceBase;

import java.util.List;

public interface IRelationDefinitionService extends IServiceBase {
    List<RelationDefinitionDTO> findAllRelationDefinitions(
            Integer categoryId, String name, String displayName, String appId);

    PaginatedBaseDTO<RelationDefinitionDTO> findPaginatedRelationDefinitions(int pageIndex, int pageSize,
            Integer categoryId, String name, String displayName, String appId);

    boolean existsRelationDefName(String appId, Integer id, String name);

    void validateRelationDefinition(RelationDefinitionDTO model);

    RelationDefinitionDTO getRelationDefinitionById(int id);

    boolean saveRelationDefinitionWithFields(RelationDefinitionDTO model);

    boolean removeRelationDefinitionById(int id, String currentUserId, String currentUserName);



    List<RelationDefDetailDTO> findAllRelationDefDetailsByDefId(int id);

    RelationDefDetailDTO getRelationDefDetailById(int id);

    boolean saveRelationDefDetail(RelationDefDetailDTO data);

    boolean removeRelationDefDetailById(int id, String currentUserId, String currentUserName);

}
