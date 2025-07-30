package kc.dto.codegenerate;

import kc.enums.codegenerate.ApiOutReturnType;
import kc.framework.enums.AttributeDataType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApiOutParamDTO extends kc.dto.TreeNodeDTO<ApiOutParamDTO> {

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
	 * 请求类型：Body、Query、Header
	 */
	private ApiOutReturnType returnType;

	private String returnTypeString;
	public String getApiOutReturnTypeString(){
		if(returnType == null)
			return ApiOutReturnType.Json.getDesc();
		return returnType.getDesc();
	}

	private int apiDefId;
	private String apiDefName;
}
