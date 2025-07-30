package kc.mapping.account;

import kc.dto.account.OrganizationDTO;
import kc.enums.OrganizationType;
import kc.framework.enums.BusinessType;
import kc.framework.enums.WorkflowBusStatus;
import kc.mapping.CycleAvoidingMappingContext;
import kc.model.account.Organization;
import kc.model.account.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface OrganizationMapping {
    OrganizationMapping INSTANCE = Mappers.getMapper(OrganizationMapping.class);

    @Mappings({
            @Mapping(source = "text", target = "name"),
            @Mapping(source = "children", target = "childNodes"),
            //@Mapping(source = "parentId", target = "parentNode.id" ),
            @Mapping(target = "parentNode", ignore = true),
            @Mapping(target = "users", ignore = true)
    })
    Organization to(OrganizationDTO source, @Context CycleAvoidingMappingContext context);

    List<Organization> to(List<OrganizationDTO> source, @Context CycleAvoidingMappingContext context);


    @Mappings({
            @Mapping(source = "name", target = "text"),
            @Mapping(source = "childNodes", target = "children"),
            @Mapping(source = "parentNode.id", target = "parentId"),
            @Mapping(source = "parentNode.name", target = "parentName"),
            @Mapping(source = "organizationType", target = "organizationType"),
            //@Mapping(source = "organizationType", target = "organizationTypeString"),
            @Mapping(source = "businessType", target = "businessType"),
            //@Mapping(source = "businessType", target = "businessTypeString"),
            @Mapping(source = "status", target = "status"),
            //@Mapping(source = "status", target = "statusString"),
            @Mapping(source = "users", target = "userNamesInfo"),
            @Mapping(target = "editMode", ignore = true),
            @Mapping(target = "organizationTypeString", ignore = true),
            @Mapping(target = "businessTypeString", ignore = true),
            @Mapping(target = "statusString", ignore = true),
            @Mapping(target = "parentNode", ignore = true),
            @Mapping(target = "createdByAndName", ignore = true)
    })
    @InheritInverseConfiguration
    OrganizationDTO from(Organization source, @Context CycleAvoidingMappingContext context);

    List<OrganizationDTO> from(List<Organization> source, @Context CycleAvoidingMappingContext context);

    default OrganizationType fromOrganizationType(int type) {
        return OrganizationType.valueOf(type);
    }

    default Integer toOrganizationType(OrganizationType type) {
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

    default WorkflowBusStatus fromWorkflowStatus(int type) {
        return WorkflowBusStatus.valueOf(type);
    }

    default Integer toWorkflowStatus(WorkflowBusStatus type) {
        if (null == type) return null;
        return type.getIndex();
    }

    default String ConvertUsers(java.util.Set<User> users) {
        return users.stream().map(m -> m.getDisplayName()).collect(Collectors.joining(","));
    }

}
