package kc.model.codegenerate;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name=Tables.RelationDefinition)
@NamedEntityGraph(name = "Graph.RelationDefinition.RelationDefDetails",
		attributeNodes = {@NamedAttributeNode("defDetails")})
public class RelationDefinition extends kc.framework.base.Entity implements java.io.Serializable {
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Id", unique=true, nullable=false)
	private int id;

	/**
	 * 类名
	 */
	@Column(name = "Name", length = 128)
	private String name;
	/**
	 * 显示名
	 */
	@Column(name = "DisplayName", length = 200)
	private String displayName;
	/**
	 * 主表Id
	 */
	@Column(name = "MainModelDefId")
	private int mainModelDefId;
	/**
	 * 描述
	 */
	@Column(name = "Description", length = 4000)
	private String description;

	/**
	 * 应用Id
	 */
	@Column(name = "ApplicationId", length = 64)
	private String applicationId;

	@ManyToOne
	@JoinColumn(name = "CategoryId", nullable = true, foreignKey = @ForeignKey(name="FK_code_RelationDefinition_code_ModelCategory_CategoryId"))
	private ModelCategory modelCategory;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "relationDefinition", cascade = CascadeType.ALL)
	private List<RelationDefDetail> defDetails = new ArrayList<>();

}
