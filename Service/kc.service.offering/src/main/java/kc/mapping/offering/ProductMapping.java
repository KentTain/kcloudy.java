package kc.mapping.offering;

import kc.dto.BlobInfoDTO;
import kc.dto.offering.ProductDTO;
import kc.enums.offering.*;
import kc.framework.util.SerializeHelper;
import kc.mapping.CycleAvoidingMappingContext;
import kc.model.offering.Product;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {ProductPropertyMapping.class},
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductMapping {
    ProductMapping INSTANCE = Mappers.getMapper(ProductMapping.class);

    @Mappings({
            @Mapping(target = "offering", ignore = true)
    })
    Product to(ProductDTO source, @Context CycleAvoidingMappingContext context);

    List<Product> to(List<ProductDTO> source, @Context CycleAvoidingMappingContext context);

    @Mappings({
            @Mapping(source = "offering.offeringId", target = "offeringId"),
            @Mapping(source = "offering.offeringName", target = "offeringName"),
            @Mapping(target = "productImageBlobs", ignore = true),
            @Mapping(target = "productFileBlob", ignore = true),
            @Mapping(target = "specificationProperties", ignore = true),
            @Mapping(target = "editMode", ignore = true)
    })
    @InheritInverseConfiguration
    ProductDTO from(Product source, @Context CycleAvoidingMappingContext context);

    List<ProductDTO> from(List<Product> source, @Context CycleAvoidingMappingContext context);

    default BlobInfoDTO ConvertBlob(String blobJson){
        return SerializeHelper.FromJson(blobJson, BlobInfoDTO.class);
    }
    default String ConvertBlob(BlobInfoDTO blob){
        return SerializeHelper.ToJson(blob);
    }

    default ProductPropertyType ConvertProductPropertyType(int type) {
        return ProductPropertyType.valueOf(type);
    }
    default int ConvertProductPropertyType(ProductPropertyType type) { return type.getIndex(); }
}
