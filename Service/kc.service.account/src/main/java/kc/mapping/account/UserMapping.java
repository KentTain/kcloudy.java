package kc.mapping.account;

import kc.dto.account.UserDTO;
import kc.dto.account.UserSimpleDTO;
import kc.enums.account.PositionLevel;
import kc.framework.enums.UserType;
import kc.model.account.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface UserMapping {
    UserMapping INSTANCE = Mappers.getMapper(UserMapping.class);

    @Mappings({
            //@Mapping(source = "positionLevel", target = "positionLevel"),
            //@Mapping(source = "userType", target = "userType"),
            @Mapping(source = "userId", target = "id"),
            @Mapping(target = "organizations", ignore = true),
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "userSettings", ignore = true)
    })
    User to(UserDTO source);

    List<User> to(List<UserDTO> source);

    @Mappings({
            @Mapping(source = "id", target = "userId"),
            @Mapping(source = "positionLevel", target = "positionLevelName"),
            @Mapping(source = "userType", target = "userTypeName"),
            //@Mapping(source = "positionLevel", target = "positionLevel"),
            //@Mapping(source = "userType", target = "userType"),
            @Mapping(target = "editMode", ignore = true),
            @Mapping(target = "systemAdmin", ignore = true),
            @Mapping(target = "roleIds", ignore = true),
            @Mapping(target = "roleNames", ignore = true),
            @Mapping(target = "organizationIds", ignore = true),
            @Mapping(target = "organizationNames", ignore = true)
    })
    UserDTO from(User source);

    List<UserDTO> from(List<User> source);

    @Mappings({
            //@Mapping(source = "positionLevel", target = "positionLevel"),
            //@Mapping(source = "userType", target = "userType"),
            @Mapping(target = "userRoleIds", ignore = true),
            @Mapping(target = "userRoleNames", ignore = true),
            @Mapping(target = "userOrgIds", ignore = true),
            @Mapping(target = "userOrgNames", ignore = true)
    })
    UserSimpleDTO simpleFrom(User source);

    List<UserSimpleDTO> simpleFrom(List<User> source);

    //default String ConvertPositionLevelName(int positionLevel) { return PositionLevel.getDesc(positionLevel); }
    default PositionLevel fromPositionLevel(int type) {
        return PositionLevel.valueOf(type);
    }

    default Integer toPositionLevel(PositionLevel type) {
        if (null == type) return null;
        return type.getIndex();
    }

    //default String ConvertUserTypeName(int userType) { return UserType.getDesc(userType); }
    default UserType fromUserType(int type) {
        return UserType.valueOf(type);
    }

    default Integer toUserType(UserType type) {
        if (null == type) return null;
        return type.getIndex();
    }
}
