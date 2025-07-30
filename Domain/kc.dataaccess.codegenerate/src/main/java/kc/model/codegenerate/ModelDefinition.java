package kc.model.codegenerate;

import kc.framework.base.PropertyBase;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name=Tables.ModelDefinition)
@NamedEntityGraph(name = "Graph.ModelDefinition.PropertyAttributeList",
		attributeNodes = {@NamedAttributeNode("propertyAttributeList")})
public class ModelDefinition extends PropertyBase<ModelDefField> {
	/**
	 * 显示名：
	 */
	@Column(name = "DisplayName", length = 500)
	private String displayName;
	/**
	 * 数据表名：
	 */
	@Column(name = "TableName", length = 200)
	private String tableName;
	/**
	 * 业务类型：
	 */
	@Column(name = "IsUseLog")
	private boolean isUseLog;
	/**
	 * 继承类型：
	 */
	@Column(name = "ModelBaseType")
	private int modelBaseType;

	/**
	 * 应用Id
	 */
	@Column(name = "ApplicationId", length = 64)
	private String applicationId;

	@ManyToOne
	@JoinColumn(name = "CategoryId", nullable = true, foreignKey = @ForeignKey(name="FK_code_ModelDefinition_code_ModelCategory_CategoryId"))
	private ModelCategory modelCategory;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelDefinition", cascade = CascadeType.ALL)
	private List<ModelDefField> propertyAttributeList = new ArrayList<>();

}
