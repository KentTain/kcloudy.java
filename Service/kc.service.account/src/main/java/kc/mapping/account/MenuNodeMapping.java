package kc.mapping.account;

import kc.dto.account.MenuNodeDTO;
import kc.dto.account.MenuNodeSimpleDTO;
import kc.dto.account.RoleDTO;
import kc.framework.enums.BusinessType;
import kc.mapping.CycleAvoidingMappingContext;
import kc.model.account.MenuNode;
import kc.model.account.Role;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface MenuNodeMapping {
    MenuNodeMapping INSTANCE = Mappers.getMapper(MenuNodeMapping.class);

    @Mappings({
            @Mapping(source = "text", target = "name"),
            @Mapping(source = "children", target = "childNodes"),
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "parentNode", ignore = true)
    })
    MenuNode to(MenuNodeDTO source, @Context CycleAvoidingMappingContext context);

    List<MenuNode> to(List<MenuNodeDTO> source, @Context CycleAvoidingMappingContext context);

    @Mappings({
            @Mapping(source = "name", target = "text"),
            @Mapping(source = "childNodes", target = "children"),
            @Mapping(source = "parentNode.id", target = "parentId"),
            @Mapping(source = "parentNode.name", target = "parentName"),
            @Mapping(target = "defaultRoleId", ignore = true),
            @Mapping(target = "editMode", ignore = true),
            @Mapping(target = "parentNode", ignore = true),
            @Mapping(target = "url", ignore = true)
    })
    MenuNodeDTO from(MenuNode source, @Context CycleAvoidingMappingContext context);

    List<MenuNodeDTO> from(List<MenuNode> source, @Context CycleAvoidingMappingContext context);

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
    RoleDTO fromRole(Role source);

    List<RoleDTO> fromRole(List<Role> source);

    @Mappings({
            @Mapping(source = "name", target = "text"),
            @Mapping(source = "parentNode.id", target = "parentId"),
            @Mapping(source = "parentNode.name", target = "parentName"),
            @Mapping(source = "childNodes", target = "children"),
            //@Mapping(target = "children", ignore = true),
            @Mapping(target = "defaultRoleId", ignore = true),
            @Mapping(target = "parentNode", ignore = true),
            @Mapping(target = "url", ignore = true)
    })
    MenuNodeSimpleDTO simpleFrom(MenuNode source, @Context CycleAvoidingMappingContext context);

    List<MenuNodeSimpleDTO> simpleFrom(List<MenuNode> source, @Context CycleAvoidingMappingContext context);

    @AfterMapping
    default void setUrl(@MappingTarget MenuNodeSimpleDTO target, MenuNode source) {
        target.setUrl(source.getMenuURL());
    }

    @AfterMapping
    default void setUrl(@MappingTarget MenuNodeDTO target, MenuNode source) {
        target.setUrl(source.getMenuURL());
    }

    default BusinessType fromBusinessType(int type) {
        return BusinessType.valueOf(type);
    }

    default Integer toBusinessType(BusinessType type) {
        if (null == type) return null;
        return type.getIndex();
    }
}
