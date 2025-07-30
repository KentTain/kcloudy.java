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
@Table(name=Tables.ApiDefinition)
public class ApiDefinition extends kc.framework.base.Entity {
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Id", unique=true, nullable=false)
	private int id;

	/**
	 * 接口状态：
	 */
	@Column(name = "ApiStatus")
	private int apiStatus;

	/**
	 * Api接口类型：
	 */
	@Column(name = "ApiMethodType")
	private int apiMethodType;

	/**
	 * http类型：
	 */
	@Column(name = "ApiHttpType")
	private int apiHttpType;

	/**
	 * 接口名称
	 */
	@Column(name = "Name", length = 50)
	private String name;
	/**
	 * 显示名：
	 */
	@Column(name = "DisplayName", length = 200)
	private String displayName;
	/**
	 * Url地址：
	 */
	@Column(name = "Url", length = 2000)
	private String url;

	/**
	 * 描述：
	 */
	@Column(name = "Description", length = 4000)
	private String description;

	/**
	 * 排序：
	 */
	@Column(name = "Index")
	private int Index;

	/**
	 * 应用Id
	 */
	@Column(name = "ApplicationId", length = 64)
	private String applicationId;

	@ManyToOne
	@JoinColumn(name = "CategoryId", nullable = true, foreignKey = @ForeignKey(name="FK_code_ModelDefinition_code_ModelCategory_CategoryId"))
	private ModelCategory modelCategory;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "apiDefinition", cascade = CascadeType.ALL)
	private List<ApiInputParam> apiInputParams = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "apiDefinition", cascade = CascadeType.ALL)
	private List<ApiOutParam> apiOutParams = new ArrayList<>();
}
