package kc.dto.codegenerate;

import kc.enums.codegenerate.ModelType;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ModelCategorySearchDTO extends kc.dto.search.TreeNodeSearchDTO implements java.io.Serializable {

	@Builder
	private ModelCategorySearchDTO(
			String name, Integer excludeId, Integer selectedId,
			Boolean hasAll, Boolean hasRoot, Integer maxLevel, ModelType modelType, String applicationId ) {
		super(name,excludeId, selectedId, hasAll, hasRoot, maxLevel);
		this.modelType = modelType;
		this.applicationId = applicationId;
	}

	private ModelType modelType;

	private String applicationId;

}
