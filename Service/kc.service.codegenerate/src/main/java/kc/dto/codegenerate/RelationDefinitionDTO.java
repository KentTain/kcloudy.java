package kc.dto.codegenerate;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RelationDefinitionDTO extends kc.dto.EntityDTO implements java.io.Serializable {
	@com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
	private Boolean isEditMode = false;

	private int id;

	/**
	 * 类名
	 */
	@NotNull
	@Length(min = 1, max = 50, message = "类名不能超过50个字符")
	private String name;
	/**
	 * 显示名
	 */
	@NotNull
	@Length(min = 1, max = 200, message = "显示名不能超过200个字符")
	private String displayName;
	/**
	 * 主表Id
	 */
	private int mainModelDefId;
	private ModelDefinitionDTO mainModelDef;
	/**
	 * 描述
	 */
	@Length(min = 0, max = 4000, message = "描述不能超过200个字符")
	private String description;

	@Length(min = 0, max = 64, message = "显示名不能超过64个字符")
	private String applicationId;

	private Integer categoryId;

	private String categoryName;

	private List<RelationDefDetailDTO> defDetails = new ArrayList<>();

}
