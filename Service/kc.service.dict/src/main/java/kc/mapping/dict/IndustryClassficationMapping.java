package kc.mapping.dict;

import kc.dto.dict.IndustryClassficationDTO;
import kc.mapping.CycleAvoidingMappingContext;
import kc.model.dict.IndustryClassfication;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface IndustryClassficationMapping {
    IndustryClassficationMapping INSTANCE = Mappers.getMapper(IndustryClassficationMapping.class);

    @Mappings({
            @Mapping(source = "text", target = "name"),
            @Mapping(source = "children", target = "childNodes"),
            @Mapping(target = "parentNode", ignore = true)
    })
    IndustryClassfication to(IndustryClassficationDTO source, @Context CycleAvoidingMappingContext context);

    List<IndustryClassfication> to(List<IndustryClassficationDTO> source, @Context CycleAvoidingMappingContext context);

    @Mappings({
            @Mapping(source = "name", target = "text"),
            @Mapping(source = "childNodes", target = "children"),
            @Mapping(source = "parentNode.id", target = "parentId"),
            @Mapping(source = "parentNode.name", target = "parentName"),
            @Mapping(target = "parentNode", ignore = true),
            @Mapping(target = "select", ignore = true)
    })
    IndustryClassficationDTO from(IndustryClassfication source, @Context CycleAvoidingMappingContext context);

    List<IndustryClassficationDTO> from(List<IndustryClassfication> source, @Context CycleAvoidingMappingContext context);

}
