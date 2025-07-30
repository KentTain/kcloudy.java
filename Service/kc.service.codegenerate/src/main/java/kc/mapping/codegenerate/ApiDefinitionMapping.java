package kc.mapping.codegenerate;

import kc.dto.codegenerate.*;
import kc.enums.codegenerate.*;
import kc.framework.enums.*;
import kc.model.codegenerate.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface ApiDefinitionMapping {
	// ApiDefinitionMapping INSTANCE = Mappers.getMapper(ApiDefinitionMapping.class);

	@Mappings({
			@Mapping(source = "apiStatus", target = "apiStatus"),
			@Mapping(source = "apiMethodType", target = "apiMethodType"),
			@Mapping(source = "apiHttpType", target = "apiHttpType"),
			@Mapping(target = "modelCategory", ignore = true)})
	ApiDefinition toApiDefinition(ApiDefinitionDTO source);
	List<ApiDefinition> toApiDefinitionList(List<ApiDefinitionDTO> source);

	@Mappings({
			@Mapping(source = "modelCategory.id", target = "categoryId"),
			@Mapping(source = "modelCategory.name", target = "categoryName"),

			@Mapping(source = "apiStatus", target = "apiStatus"),
			@Mapping(source = "apiMethodType", target = "apiMethodType"),
			@Mapping(source = "apiHttpType", target = "apiHttpType"),

			@Mapping(target = "isEditMode", ignore = true),
			@Mapping(target = "apiStatusString", ignore = true),
			@Mapping(target = "apiMethodTypeString", ignore = true),
			@Mapping(target = "apiHttpTypeString", ignore = true)})
	ApiDefinitionDTO fromApiDefinition(ApiDefinition source);
	List<ApiDefinitionDTO> fromApiDefinitionList(List<ApiDefinition> source);

	
	@Mappings({
			@Mapping(source = "dataType", target = "dataType"),
			@Mapping(source = "requestType", target = "requestType"),
			@Mapping(source = "bodyType", target = "bodyType"),
			@Mapping(target = "apiDefinition", ignore = true) })
	ApiInputParam toApiInputParam(ApiInputParamDTO source);
	List<ApiInputParam> toApiInputParamList(List<ApiInputParamDTO> source);

	@Mappings({ @Mapping(source = "apiDefinition.id", target = "apiDefId"),
			@Mapping(source = "apiDefinition.name", target = "apiDefName"),
			@Mapping(source = "dataType", target = "dataType"),
			@Mapping(source = "requestType", target = "requestType"),
			@Mapping(source = "bodyType", target = "bodyType"),
			@Mapping(target = "dataTypeString", ignore = true),
			@Mapping(target = "requestTypeString", ignore = true),
			@Mapping(target = "bodyTypeString", ignore = true)})
	ApiInputParamDTO fromApiInputParam(ApiInputParam source);
	List<ApiInputParamDTO> fromApiInputParamList(List<ApiInputParam> source);

	@Mappings({
			@Mapping(source = "dataType", target = "dataType"),
			@Mapping(source = "returnType", target = "returnType"),
			@Mapping(target = "apiDefinition", ignore = true) })
	ApiOutParam toApiOutParam(ApiOutParamDTO source);
	List<ApiOutParam> toApiOutParamList(List<ApiOutParamDTO> source);

	@Mappings({ @Mapping(source = "apiDefinition.id", target = "apiDefId"),
			@Mapping(source = "apiDefinition.name", target = "apiDefName"),
			@Mapping(source = "dataType", target = "dataType"),
			@Mapping(source = "returnType", target = "returnType"),
			@Mapping(target = "dataTypeString", ignore = true),
			@Mapping(target = "returnTypeString", ignore = true)})
	ApiOutParamDTO fromApiOutParam(ApiOutParam source);
	List<ApiOutParamDTO> fromApiOutParamList(List<ApiOutParam> source);


	default ApiStatus fromApiStatus(int type) {
		return ApiStatus.valueOf(type);
	}
	default Integer toApiStatus(ApiStatus type) {
		if (null == type) return null;
		return type.getIndex();
	}

	default ApiMethodType fromApiMethodType(int type) {
		return ApiMethodType.valueOf(type);
	}
	default Integer toApiMethodType(ApiMethodType type) {
		if (null == type) return null;
		return type.getIndex();
	}

	default ApiHttpType fromApiHttpType(int type) {
		return ApiHttpType.valueOf(type);
	}
	default Integer toApiHttpType(ApiHttpType type) {
		if (null == type) return null;
		return type.getIndex();
	}

	default AttributeDataType fromAttributeDataType(int type) {
		return AttributeDataType.valueOf(type);
	}
	default Integer toAttributeDataType(AttributeDataType type) {
		if (null == type) return null;
		return type.getIndex();
	}

	default ApiInputRequestType fromApiInputRequestType(int type) {
		return ApiInputRequestType.valueOf(type);
	}
	default Integer toApiInputRequestType(ApiInputRequestType type) {
		if (null == type) return null;
		return type.getIndex();
	}

	default ApiInputBodyType fromApiInputBodyType(int type) {
		return ApiInputBodyType.valueOf(type);
	}
	default Integer toApiInputBodyType(ApiInputBodyType type) {
		if (null == type) return null;
		return type.getIndex();
	}

	default ApiOutReturnType fromApiOutReturnType(int type) {
		return ApiOutReturnType.valueOf(type);
	}
	default Integer toApiOutReturnType(ApiOutReturnType type) {
		if (null == type) return null;
		return type.getIndex();
	}
}
