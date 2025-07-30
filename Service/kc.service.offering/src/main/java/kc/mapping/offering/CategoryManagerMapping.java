package kc.mapping.offering;


import kc.dto.account.UserSimpleDTO;
import kc.dto.offering.CategoryManagerDTO;
import kc.model.offering.CategoryManager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.EnumSet;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface CategoryManagerMapping {
    CategoryManagerMapping INSTANCE = Mappers.getMapper(CategoryManagerMapping.class);

    @Mappings({
            @Mapping(target = "category", ignore = true)
    })
    CategoryManager to(CategoryManagerDTO source);
    List<CategoryManager> to(List<CategoryManagerDTO> source);


    @Mappings({
            @Mapping(target = "category", ignore = true)
    })
    CategoryManagerDTO from(CategoryManager source);
    List<CategoryManagerDTO> from(List<CategoryManager> source);


    @Mappings({
            @Mapping(target = "default", ignore = true),
            @Mapping(target = "valid", ignore = true),
            @Mapping(target = "category", ignore = true)
    })
    CategoryManager UserTo(UserSimpleDTO source);
    List<CategoryManager> UsersTo(List<UserSimpleDTO> source);


}
