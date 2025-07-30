package kc.model.codegenerate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kc.framework.base.PropertyAttributeBase;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name=Tables.ModelDefField)
public class ModelDefField extends PropertyAttributeBase {

	/**
	 * 显示名
	 */
	@Column(name = "DisplayName", length = 200)
	private String displayName;
	/**
	 * 描述
	 */
	@Column(name = "Description", length = 4000)
	private String description;
	/**
	 * 是否为主键字段
	 */
	@Column(name = "IsPrimaryKey")
	private Boolean isPrimaryKey;
	/**
	 * 主键类型:kc.framework.enums.PrimaryKeyType
	 */
	@Column(name = "PrimaryKeyType")
	private Integer primaryKeyType;
	/**
	 * 是否必填
	 */
	@Column(name = "IsNotNull")
	private Boolean isNotNull;
	/**
	 * 是否唯一
	 */
	@Column(name = "IsUnique")
	private Boolean isUnique;
	/**
	 * 是否为执行人字段
	 */
	@Column(name = "IsExecutor")
	private Boolean isExecutor;

	/**
	 * 是否为条件判断字段
	 */
	@Column(name = "IsCondition")
	private Boolean isCondition;

	/**
	 * 关联对象id
	 */
	@Column(name = "RelateObjectId", length = 64)
	private String relateObjectId;

	/**
	 * 关联对象表名
	 */
	@Column(name = "RelateObjFieldId", length = 64)
	private String relateObjFieldId;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "ModelDefId", nullable = false, foreignKey = @ForeignKey(name="FK_app_ModelDefField_app_ModelDefinition_ModelDefId"))
	private ModelDefinition modelDefinition;
}
