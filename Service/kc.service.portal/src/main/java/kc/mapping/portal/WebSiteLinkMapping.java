package kc.mapping.portal;

import kc.dto.portal.WebSiteLinkDTO;
import kc.enums.portal.LinkType;
import kc.model.portal.WebSiteLink;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface WebSiteLinkMapping {
    WebSiteLinkMapping INSTANCE = Mappers.getMapper(WebSiteLinkMapping.class);

    @Mappings({
            @Mapping(source = "linkType", target = "linkType"),
    })
    WebSiteLink to(WebSiteLinkDTO source);

    List<WebSiteLink> to(List<WebSiteLinkDTO> source);

    @Mappings({
            @Mapping(source = "linkType", target = "linkType"),
            @Mapping(target = "linkTypeString", ignore = true)
    })
    WebSiteLinkDTO from(WebSiteLink source);

    List<WebSiteLinkDTO> from(List<WebSiteLink> source);


    default LinkType ConvertLinkType(int type) {
        return LinkType.valueOf(type);
    }
    default int ConvertLinkType(LinkType type) { return type.getIndex(); }

}
