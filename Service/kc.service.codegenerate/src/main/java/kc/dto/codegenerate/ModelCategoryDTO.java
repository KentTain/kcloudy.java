package kc.dto.codegenerate;

import kc.enums.codegenerate.ModelType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ModelCategoryDTO extends kc.dto.TreeNodeDTO<ModelCategoryDTO> {

	@com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
	private Boolean isEditMode = false;

	private Integer parentId;

	private String parentName;

    /**
     * 模型类型
	 */
	private ModelType modelType;

	private String modelTypeString;
	public String getModelTypeString(){
		if(modelType == null)
			return ModelType.Other.getDesc();
		return modelType.getDesc();
	}

	@Length(min = 0, max = 64, message = "显示名不能超过64个字符")
	private String applicationId;

}
