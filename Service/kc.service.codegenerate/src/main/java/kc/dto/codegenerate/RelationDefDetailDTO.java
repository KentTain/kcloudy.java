package kc.dto.codegenerate;

import kc.enums.codegenerate.RelationType;
import kc.model.codegenerate.ModelDefinition;
import lombok.*;

import javax.validation.constraints.Max;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RelationDefDetailDTO extends kc.dto.EntityDTO implements java.io.Serializable  {

	@com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
	private Boolean isEditMode = false;

	private int id;
	/**
	 * 关系类型：
	 */
	private RelationType relationType;
	private String relationTypeString;
	public String getRelationTypeString() {
		if (relationType == null)
			return RelationType.OneByMany.getDesc();
		return relationType.getDesc();
	}

	/**
	 * 属性名
	 */
	@Max(50)
	private String name;
	/**
	 * 显示名
	 */
	@Max(200)
	private String displayName;

    /**
     * 主表属性Id
     */
    private int mainModelDefFieldId;
    private ModelDefFieldDTO mainModelDefField;

    /**
     * 子表Id
     */
    private int subModelDefId;
    private ModelDefinitionDTO subModelDef;

    /**
     * 子表属性Id
     */
    private int subModelDefFieldId;
    private ModelDefFieldDTO subModelDefField;

	private Integer relationDefId;

	private String relationDefName;
}
