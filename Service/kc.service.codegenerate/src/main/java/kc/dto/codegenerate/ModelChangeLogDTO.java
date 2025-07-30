package kc.dto.codegenerate;

import kc.dto.ProcessLogBaseDTO;
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
public class ModelChangeLogDTO extends ProcessLogBaseDTO {

	private ModelType modelType;

	private String modelTypeString;
	public String getModelTypeString(){
		if(modelType == null)
			return ModelType.Other.getDesc();
		return modelType.getDesc();
	}

	@Length(min = 0, max = 128, message = "描述不能超过128个字符")
	private String referenceId;

	@Length(min = 0, max = 200, message = "描述不能超过200个字符")
	private String referenceName;

	public String refObjectJson ;
}
