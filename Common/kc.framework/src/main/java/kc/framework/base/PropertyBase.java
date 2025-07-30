package kc.framework.base;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 属性基础类型：
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
//@javax.persistence.Entity
//@NamedEntityGraph(name = "Graph.PropertyBase.propertyAttributeList",
//	attributeNodes = {@NamedAttributeNode("propertyAttributeList")})
public abstract class PropertyBase<T> extends Entity implements java.io.Serializable{

	private static final long serialVersionUID = -5757366713262066873L;
	/**
	 * 属性Id：
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="PropertyId", unique=true, nullable=false)
	private int propertyId;

	/**
	 * 名称：
	 */
	@Column(name = "Name", length = 256)
	private String name;
	/**
	 * 显示名称：
	 */
	@Column(name = "DisplayName", length = 512)
	private String displayName;
	/**
	 * 描述：
	 */
	@Column(name = "Description", length = 4000)
	private String description;

	/**
	 * 是否能编辑：
	 */

	@Column(name = "CanEdit")
	private Boolean canEdit = false;

	/**
	 * 是否必须：
	 */
	@Column(name = "IsRequire")
	private Boolean isRequire = false;

	/**
	 * 排序：
	 */
	@Column(name = "Index")
	private int index;

//	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
// private List<? super PropertyAttributeBase> propertyAttributeList = new ArrayList<>();

}
