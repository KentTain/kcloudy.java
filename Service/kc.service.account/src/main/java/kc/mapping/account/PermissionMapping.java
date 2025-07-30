package kc.mapping.account;

import kc.dto.account.PermissionDTO;
import kc.dto.account.PermissionSimpleDTO;
import kc.dto.account.RoleDTO;
import kc.framework.enums.BusinessType;
import kc.framework.enums.ResultType;
import kc.mapping.CycleAvoidingMappingContext;
import kc.model.account.Permission;
import kc.model.account.Role;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PermissionMapping {
    PermissionMapping INSTANCE = Mappers.getMapper(PermissionMapping.class);

    @Mappings({
            @Mapping(source = "text", target = "name"),
            @Mapping(source = "children", target = "childNodes"),
            //@Mapping(source = "parentId", target = "parentNode.id" ),
            @Mapping(target = "parentNode", ignore = true)
    })
    Permission to(PermissionDTO source, @Context CycleAvoidingMappingContext context);

    List<Permission> to(List<PermissionDTO> source, @Context CycleAvoidingMappingContext context);

    @Mappings({
            @Mapping(source = "name", target = "text"),
            @Mapping(source = "childNodes", target = "children"),
            @Mapping(source = "parentNode.id", target = "parentId"),
            @Mapping(source = "parentNode.name", target = "parentName"),
            @Mapping(target = "resultTypeString", ignore = true),
            @Mapping(target = "defaultRoleId", ignore = true),
            @Mapping(target = "editMode", ignore = true),
            @Mapping(target = "parentNode", ignore = true)
    })
    PermissionDTO from(Permission source, @Context CycleAvoidingMappingContext context);

    List<PermissionDTO> from(List<Permission> source, @Context CycleAvoidingMappingContext context);

    @Mappings({
            @Mapping(source = "id", target = "roleId"),
            @Mapping(source = "name", target = "roleName"),
            @Mapping(source = "businessType", target = "businessType"),
            @Mapping(target = "editMode", ignore = true),
            @Mapping(target = "checked", ignore = true),
            @Mapping(target = "userIds", ignore = true),
            @Mapping(target = "users", ignore = true),
            @Mapping(target = "menuNodes", ignore = true),
            @Mapping(target = "permissions", ignore = true),
            @Mapping(target = "businessTypeString", ignore = true)
    })
    RoleDTO fromRole(Role source, @Context CycleAvoidingMappingContext context);

    List<RoleDTO> fromRole(List<Role> source, @Context CycleAvoidingMappingContext context);

    @Mappings({
            @Mapping(source = "name", target = "text"),
            @Mapping(source = "parentNode.id", target = "parentId"),
            @Mapping(source = "parentNode.name", target = "parentName"),
            @Mapping(source = "childNodes", target = "children"),
            @Mapping(source = "resultType", target = "resultType"),
            @Mapping(target = "resultTypeString", ignore = true),
            @Mapping(target = "defaultRoleId", ignore = true),
            @Mapping(target = "editMode", ignore = true),
            @Mapping(target = "parentNode", ignore = true)
    })
    PermissionSimpleDTO simpleFrom(Permission source, @Context CycleAvoidingMappingContext context);

    List<PermissionSimpleDTO> simpleFrom(List<Permission> source, @Context CycleAvoidingMappingContext context);

    @AfterMapping
    default void setUrl(@MappingTarget PermissionSimpleDTO target, Permission source) {
        target.setUrl(source.getPermissionURL());
    }

    @AfterMapping
    default void setUrl(@MappingTarget PermissionDTO target, Permission source) {
        target.setUrl(source.getPermissionURL());
    }

    default ResultType fromResultType(int type) {
        return ResultType.valueOf(type);
    }

    default Integer toResultType(ResultType type) {
        if (null == type) return null;
        return type.getIndex();
    }

    default BusinessType fromBusinessType(int type) {
        return BusinessType.valueOf(type);
    }

    default Integer toBusinessType(BusinessType type) {
        if (null == type) return null;
        return type.getIndex();
    }

//	@Mapping(source = "id", target = "RoleId")
//	@Mapping(source = "name", target = "RoleName")
//	RoleDTO to(Role source);
}
