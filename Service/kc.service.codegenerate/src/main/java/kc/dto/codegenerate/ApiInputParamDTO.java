package kc.dto.codegenerate;

import kc.enums.codegenerate.*;
import kc.framework.enums.AttributeDataType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApiInputParamDTO extends kc.dto.TreeNodeDTO<ApiInputParamDTO> {

	/**
	 * 属性数据类型
	 */
	private AttributeDataType dataType;

	private String dataTypeString;
	public String getAttributeDataTypeString(){
		if(dataType == null)
			return AttributeDataType.String.getDesc();
		return dataType.getDesc();
	}
	/**
	 * 显示名
	 */
	@Length(min = 0, max = 200, message = "显示名不能超过200个字符")
	private String displayName;
	/**
	 * 描述
	 */
	@Length(min = 0, max = 4000, message = "显示名不能超过4000个字符")
	private String description;

	/**
	 * 是否唯一
	 */
	@com.fasterxml.jackson.annotation.JsonProperty("isArray")
	private Boolean isArray;
	/**
	 * 是否必填
	 */
	@com.fasterxml.jackson.annotation.JsonProperty("isNotNull")
	private Boolean isNotNull;

	/**
	 * 请求类型：Body、Query、Header
	 */
	private ApiInputRequestType requestType;

	private String requestTypeString;
	public String getApiInputRequestTypeString(){
		if(requestType == null)
			return ApiInputRequestType.Body.getDesc();
		return requestType.getDesc();
	}

	/**
	 * Body、Query类型
	 */
	private ApiInputBodyType bodyType;

	private String bodyTypeString;
	public String getApiInputBodyTypeString(){
		if(bodyType == null)
			return ApiInputBodyType.Json.getDesc();
		return bodyType.getDesc();
	}

	private int apiDefId;
	private String apiDefName;
}
