package kc.framework.base;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import kc.framework.enums.AttributeDataType;
import lombok.*;

import java.util.Objects;

/**
 * 属性值基础类型：
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EqualsAndHashCode(callSuper = false)
//@javax.persistence.Entity
@AttributeOverride(name="propertyattribute_id",column=@Column(name="PropertyAttributeId"))
public abstract class PropertyAttributeBase extends Entity implements java.io.Serializable{

	private static final long serialVersionUID = -299509474244820226L;

	/**
	 * 属性值Id：
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PropertyAttributeId", unique = true, nullable = false)
	private int propertyAttributeId;

	/**
	 * 属性类型：kc.framework.enums.AttributeDataType
	 *		0：字符串；1：布尔型；2：整型；3：数值型；4：日期型；5：文本型；6：对象；7：列表
	 */
	@Column(name = "DataType")
	private int dataType;

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
	 * 属性值：
	 */
	private String value;

	/**
	 * 属性扩展值1：
	 */
	@Column(name = "Ext1", length = 4000)
	private String ext1;

	/**
	 * 属性扩展值2：
	 */
	@Column(name = "Ext2", length = 4000)
	private String ext2;

	/**
	 * 属性扩展值3：
	 */
	@Column(name = "Ext3", length = 4000)
	private String ext3;
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

}
