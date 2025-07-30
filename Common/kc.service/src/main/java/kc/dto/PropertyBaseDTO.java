package kc.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.MappedSuperclass;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public abstract class PropertyBaseDTO<T> extends EntityDTO implements java.io.Serializable{

	private static final long serialVersionUID = -5757366713262066873L;

	private int propertyId;
	/**
	 * 属性名称
	 */
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String name;
	/**
	 * 显示名称：
	 */
	@Length(min = 0, max = 512, message = "属性名称不能超过512个字符")
	private String displayName;
	/**
	 * 属性描述
	 */
	@Length(min = 0, max = 4000, message = "描述不能超过4000个字符")
	private String description;
	/**
	 * 能否编辑
	 */
	private Boolean canEdit = false;
	/**
	 * 是否必填
	 */
	@com.fasterxml.jackson.annotation.JsonProperty("isRequire")
	private Boolean isRequire = false;
	
	private int index;
	
	private List<T> propertyAttributeList = new ArrayList<T>();
}
