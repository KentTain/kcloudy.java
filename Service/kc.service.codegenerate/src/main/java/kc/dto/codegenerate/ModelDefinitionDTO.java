package kc.dto.codegenerate;

import kc.dto.PropertyBaseDTO;
import kc.enums.codegenerate.ModelBaseType;
import kc.enums.codegenerate.ModelType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ModelDefinitionDTO extends PropertyBaseDTO<ModelDefFieldDTO> {

	@com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
	private Boolean isEditMode = false;
	/**
	 * 显示名：
	 */
	@Length(min = 1, max = 200, message = "显示名不能超过200个字符")
	private String displayName;
	/**
	 * 数据表名：
	 */
	@Length(min = 1, max = 50, message = "数据表名不能超过50个字符")
	private String tableName;
	/**
	 * 是否使用业务日志：
	 */
	@com.fasterxml.jackson.annotation.JsonProperty("isUseLog")
	private Boolean isUseLog = false;
	/**
	 * 继承类型：
	 */
	private ModelBaseType modelBaseType;

	private String modelBaseTypeString;
	public String getModelBaseTypeString(){
		if(modelBaseType == null)
			return ModelBaseType.EntityBase.getDesc();
		return modelBaseType.getDesc();
	}

	@Length(min = 0, max = 64, message = "显示名不能超过64个字符")
	private String applicationId;

	private Integer categoryId;

	private String categoryName;

	//private List<ModelDefFieldDTO> propertyAttributeList = new ArrayList<>();

}
