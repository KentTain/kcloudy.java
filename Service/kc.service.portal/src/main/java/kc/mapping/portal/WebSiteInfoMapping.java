package kc.mapping.portal;

import kc.dto.portal.WebSiteInfoDTO;
import kc.model.portal.WebSiteInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface WebSiteInfoMapping {
    WebSiteInfoMapping INSTANCE = Mappers.getMapper(WebSiteInfoMapping.class);

    WebSiteInfo to(WebSiteInfoDTO source);

    List<WebSiteInfo> to(List<WebSiteInfoDTO> source);

    WebSiteInfoDTO from(WebSiteInfo source);

    List<WebSiteInfoDTO> from(List<WebSiteInfo> source);

}
