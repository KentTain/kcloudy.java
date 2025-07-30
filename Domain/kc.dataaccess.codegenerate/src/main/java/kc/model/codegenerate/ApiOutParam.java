package kc.model.codegenerate;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name=Tables.ApiInputParam)
public class ApiOutParam extends kc.framework.base.TreeNode<ApiOutParam> {

	/**
	 * 属性数据类型
	 */
	@Column(name = "DataType")
	private int dataType;
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
	 * 是否唯一
	 */
	@Column(name = "IsArray")
	private Boolean isArray;
	/**
	 * 请求类型：Body、Query、Header
	 */
	@Column(name = "RequestType")
	private Integer returnType;

	@ManyToOne
	@JoinColumn(name = "ApiDefId", nullable = false, foreignKey = @ForeignKey(name="FK_code_ApiOutParam_code_ApiDefinition_ApiDefId"))
	private ApiDefinition apiDefinition;
}
