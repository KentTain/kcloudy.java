package kc.mapping.account;

import kc.dto.account.*;
import kc.framework.enums.BusinessType;
import kc.framework.enums.ResultType;
import kc.model.account.MenuNode;
import kc.model.account.Permission;
import kc.model.account.Role;
import kc.model.account.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface RoleMapping {
    RoleMapping INSTANCE = Mappers.getMapper(RoleMapping.class);

    @Mappings({
            @Mapping(source = "roleId", target = "id"),
            @Mapping(source = "roleName", target = "name"),
            @Mapping(source = "businessType", target = "businessType"),
            @Mapping(target = "normalizedName", ignore = true),
            @Mapping(target = "concurrencyStamp", ignore = true)
    })
    Role to(RoleDTO source);

    List<Role> to(List<RoleDTO> source);

    @Mappings({
            @Mapping(source = "id", target = "roleId"),
            @Mapping(source = "name", target = "roleName"),
            @Mapping(source = "businessType", target = "businessType"),
            @Mapping(target = "editMode", ignore = true),
            @Mapping(target = "checked", ignore = true),
            @Mapping(target = "businessTypeString", ignore = true)
    })
    RoleDTO from(Role source);

    List<RoleDTO> from(List<Role> source);

    @Mappings({
            @Mapping(source = "id", target = "userId"),
            @Mapping(target = "editMode", ignore = true),

            @Mapping(target = "systemAdmin", ignore = true),
            @Mapping(target = "roleIds", ignore = true),
            @Mapping(target = "roleNames", ignore = true),
            @Mapping(target = "organizationNames", ignore = true),
            @Mapping(target = "organizationIds", ignore = true)
    })
    UserDTO userFrom(User source);

    List<UserDTO> userFrom(List<User> source);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "text"),
            @Mapping(source = "parentNode.id", target = "parentId"),
            @Mapping(source = "parentNode.name", target = "parentName"),

            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "children", ignore = true),
            @Mapping(target = "parentNode", ignore = true),
            @Mapping(target = "url", ignore = true)
    })
    MenuNodeDTO menuFrom(MenuNode source);

    List<MenuNodeDTO> menuFrom(List<MenuNode> source);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "text"),
            @Mapping(source = "parentNode.id", target = "parentId"),
            @Mapping(source = "parentNode.name", target = "parentName"),
            @Mapping(source = "resultType", target = "resultType"),
            @Mapping(target = "resultTypeString", ignore = true),
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "children", ignore = true),
            @Mapping(target = "editMode", ignore = true),
            @Mapping(target = "parentNode", ignore = true)
    })
    PermissionDTO permissionFrom(Permission source);

    List<PermissionDTO> permissionFrom(List<Permission> source);

    @Mappings({
            @Mapping(source = "id", target = "roleId"),
            @Mapping(source = "name", target = "roleName"),
            //@Mapping(source = "roleMenuNodes", target = "menuNodes" ),
            //@Mapping(source = "rolePermissions", target = "permissions" ),
            @Mapping(source = "businessType", target = "businessType"),
            @Mapping(target = "editMode", ignore = true),
            @Mapping(target = "checked", ignore = true),
            @Mapping(target = "businessTypeString", ignore = true)
    })
    RoleSimpleDTO fromSimple(Role source);

    List<RoleSimpleDTO> fromSimple(List<Role> source);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "text"),
            @Mapping(source = "parentNode.id", target = "parentId"),
            @Mapping(source = "parentNode.name", target = "parentName"),

            @Mapping(target = "children", ignore = true),
            @Mapping(target = "parentNode", ignore = true),
            @Mapping(target = "url", ignore = true)
    })
    MenuNodeSimpleDTO menuFromSimple(MenuNode source);

    List<MenuNodeSimpleDTO> menusFromSimple(List<MenuNode> source);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "text"),
            @Mapping(source = "parentNode.id", target = "parentId"),
            @Mapping(source = "parentNode.name", target = "parentName"),
            @Mapping(source = "resultType", target = "resultType"),
            @Mapping(target = "resultTypeString", ignore = true),
            @Mapping(target = "children", ignore = true),
            @Mapping(target = "editMode", ignore = true),
            @Mapping(target = "parentNode", ignore = true)
    })
    PermissionSimpleDTO permissionFromSimple(Permission source);

    List<PermissionSimpleDTO> permissionsFromSimple(List<Permission> source);

    default BusinessType fromBusinessType(int type) {
        return BusinessType.valueOf(type);
    }

    default Integer toBusinessType(BusinessType type) {
        if (null == type) return null;
        return type.getIndex();
    }

    default ResultType fromResultType(int type) {
        return ResultType.valueOf(type);
    }

    default Integer toResultType(ResultType type) {
        if (null == type) return null;
        return type.getIndex();
    }
}
