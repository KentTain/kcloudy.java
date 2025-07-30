package kc.mapping.offering;

import kc.dto.offering.ProductPropertyDTO;
import kc.enums.offering.ProductPropertyType;
import kc.model.offering.ProductProperty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface ProductPropertyMapping {
    ProductPropertyMapping INSTANCE = Mappers.getMapper(ProductPropertyMapping.class);

    @Mappings({
            @Mapping(source = "productPropertyType", target = "productPropertyType"),
            @Mapping(target = "product", ignore = true)
    })
    ProductProperty to(ProductPropertyDTO source);
    List<ProductProperty> to(List<ProductPropertyDTO> source);

    @Mappings({
            @Mapping(source = "productPropertyType", target = "productPropertyType"),
            @Mapping(target = "productPropertyTypeString", ignore = true),
            @Mapping(target = "product", ignore = true)
    })
    ProductPropertyDTO from(ProductProperty source);
    List<ProductPropertyDTO> from(List<ProductProperty> source);

    default ProductPropertyType ConvertProductPropertyType(int type) {
        return ProductPropertyType.valueOf(type);
    }
    default int ConvertProductPropertyType(ProductPropertyType type) { return type.getIndex(); }

}
