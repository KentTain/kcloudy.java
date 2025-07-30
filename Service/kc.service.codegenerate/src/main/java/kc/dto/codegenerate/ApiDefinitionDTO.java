package kc.dto.codegenerate;

import kc.enums.codegenerate.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApiDefinitionDTO extends kc.dto.EntityDTO {

	@com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
	private Boolean isEditMode = false;

	private int id;

	/**
	 * 接口状态：
	 */
	private ApiStatus apiStatus;

	private String apiStatusString;
	public String getApiStatusString(){
		if(apiStatus == null)
			return ApiStatus.Definition.getDesc();
		return apiStatus.getDesc();
	}

	/**
	 * Api接口类型：
	 */
	private ApiMethodType apiMethodType;

	private String apiMethodTypeString;
	public String getApiMethodTypeString(){
		if(apiMethodType == null)
			return ApiMethodType.Create.getDesc();
		return apiMethodType.getDesc();
	}

	/**
	 * http类型：
	 */
	private ApiHttpType apiHttpType;

	private String apiHttpTypeString;
	public String getApiHttpTypeString(){
		if(apiHttpType == null)
			return ApiHttpType.Get.getDesc();
		return apiHttpType.getDesc();
	}

	/**
	 * 接口名称
	 */
	@Length(min = 1, max = 50, message = "显示名不能超过50个字符")
	private String name;
	/**
	 * 显示名：
	 */
	@Length(min = 1, max = 200, message = "显示名不能超过200个字符")
	private String displayName;
	/**
	 * Url地址：
	 */
	@Length(min = 0, max = 2000, message = "显示名不能超过2000个字符")
	private String url;
	/**
	 * 描述：
	 */
	@Length(min = 0, max = 4000, message = "显示名不能超过4000个字符")
	private String description;

	/**
	 * 排序：
	 */
	private int Index;

	/**
	 * 应用Id
	 */
	@Length(min = 0, max = 64, message = "显示名不能超过64个字符")
	private String applicationId;

	private Integer categoryId;

	private String categoryName;

	private List<ApiInputParamDTO> apiInputParams = new ArrayList<>();

	private List<ApiOutParamDTO> apiOutParams = new ArrayList<>();
}
