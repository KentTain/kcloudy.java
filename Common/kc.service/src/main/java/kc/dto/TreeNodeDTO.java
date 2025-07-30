package kc.dto;

import java.util.*;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.hibernate.validator.constraints.Length;

/**
 * @author tianc 树结构基类
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public abstract class TreeNodeDTO<T> extends EntityDTO implements java.io.Serializable{
	private static final long serialVersionUID = 3862416351900991824L;

	/**
	 * 子节点Id
	 */
	private int id;
	
	/**
	 * 父节点Id
	 */
	//private Integer ParentId;

	/**
	 * 名称
	 */
	@NotBlank(message = "名称不能为空")
	@Length(min = 1, max = 128, message = "名称不能超过128个字符")
	private String text;

	/**
	 * 标识树形结构的编码: 
	 * 		一级树节点Id-二级树节点Id-三级树节点Id-四级树节点Id 
	 * 		1-1-1-1 ~~ 999-999-999-999
	 */
	@Length(min = 0, max = 128, message = "路径不能超过128个字符")
	private String treeCode;

	/**
	 * 是否叶节点
	 */
	private boolean leaf = false;

	/**
	 * 节点深度
	 */
	private int level;

	/**
	 * 排序
	 */
	private int index;

	@com.fasterxml.jackson.annotation.JsonProperty("checked")
	private Boolean checked;
	
	@JsonIgnore
	private T parentNode;
	
	private List<T> children = new ArrayList<T>();
}
