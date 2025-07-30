package kc.model.codegenerate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name=Tables.RelationDefDetail)
public class RelationDefDetail extends kc.framework.base.Entity implements java.io.Serializable  {
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Id", unique=true, nullable=false)
	private int id;
	/**
	 * 业务类型：
	 */
	@Column(name = "RelationType")
	private int relationType;
	/**
	 * 属性名
	 */
	@Column(name = "Name", length = 50)
	private String name;
	/**
	 * 显示名
	 */
	@Column(name = "DisplayName", length = 200)
	private String displayName;

	/**
	 * 主表属性Id
	 */
	@Column(name = "MainModelDefFieldId")
	private int mainModelDefFieldId;

	/**
	 * 子表Id
	 */
	@Column(name = "SubModelDefId")
	private int subModelDefId;

	/**
	 * 子表属性Id
	 */
	@Column(name = "SubModelDefFieldId")
	private int subModelDefFieldId;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "RelationDefId", nullable = false, foreignKey = @ForeignKey(name="FK_app_RelationDefDetail_app_RelationDefinition_RelationDefId"))
	private RelationDefinition relationDefinition;
}
