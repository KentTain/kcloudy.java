package kc.framework.base;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import kc.framework.enums.ConfigStatus;
import kc.framework.enums.ConfigType;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@javax.persistence.Entity
@Table(name="cfg_ConfigEntity")
@NamedEntityGraph(name = "Graph.ConfigEntity.ConfigAttributes", 
	attributeNodes = {@NamedAttributeNode("configAttributes")})
public class ConfigEntity extends Entity implements java.io.Serializable{

	private static final long serialVersionUID = -4052400408764598999L;

	/**
	 * 配置Id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ConfigId", unique = true, nullable = false)
	private int configId;

	/**
	 * 配置类型：KC.Enums.Core.ConfigType
	 */
	@Column(name = "ConfigType")
	private int configType;

	/**
	 * 配置标记
	 */
	@Column(name = "ConfigSign")
	private int configSign;

	/**
	 * 配置名称
	 */
	@Column(name = "ConfigName", length = 50)
	private String configName;

	/**
	 * 配置描述
	 */
	@Column(name = "ConfigDescription", length = 4000)
	private String configDescription;

	/**
	 * 配置生成的XML
	 */
	@Column(name = "ConfigXml")
	private String configXml;

	/**
	 * 配置图片链接
	 */
	@Column(name = "ConfigImgUrl")
	private String configImgUrl;

	/**
	 * 配置状态：KC.Enums.Core.ConfigStatus
	 */
	@Column(name = "State")
	private int state;
	/**
	 * 配置代码
	 */
	@Column(name = "ConfigCode", length = 128)
	private String configCode;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy ="configEntity")
	//@JoinColumn(name="PropertyAttributeId")
	private List<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
}
