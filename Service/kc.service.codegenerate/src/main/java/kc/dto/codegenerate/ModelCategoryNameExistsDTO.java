package kc.dto.codegenerate;

import kc.dto.search.TreeNodeNameExistsDTO;
import kc.enums.codegenerate.ModelType;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ModelCategoryNameExistsDTO extends TreeNodeNameExistsDTO implements java.io.Serializable {

	@Builder
	private ModelCategoryNameExistsDTO(Integer id, Integer pId, String name, ModelType modelType, String applicationId ) {
		super(id, pId, name);
		this.modelType = modelType;
		this.applicationId = applicationId;
	}

	private ModelType modelType;

	private String applicationId;

}
