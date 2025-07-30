package kc.model.account;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;

import kc.framework.base.TreeNode;
import kc.framework.extension.StringExtensions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity
@Table(name="sys_MenuNode")
@Inheritance(strategy=InheritanceType.JOINED)
@NamedEntityGraph(name = "Graph.MenuNode.Roles", 
	attributeNodes = {@NamedAttributeNode("roles")})
public class MenuNode extends TreeNode<MenuNode> implements java.io.Serializable {

	private static final long serialVersionUID = 8837645220903070780L;

	/**
	 * 请求地址
	 */
	@Column(name = "AreaName")
	private String areaName;

	/**
	 * 请求地址
	 */
	@Column(name = "ControllerName")
	private String controllerName;

	/**
	 * 请求地址
	 */
	@Column(name = "ActionName")
	private String actionName;

	/**
	 * 菜单参数
	 */
	@Column(name = "Parameters")
	private String parameters;

	/**
	 * 描述
	 */
	@Column(name = "Description", length=4000)
	private String description;

	/**
	 * 小图标
	 */
	@Column(name = "SmallIcon")
	private String smallIcon;

	/**
	 * 大图标
	 */
	@Column(name = "BigIcon")
	private String bigIcon;

	/**
	 * 租户类型，值定义：kc.framework.enums.TenantType
	 */
	@Column(name = "TenantType")
	private int tenantType;

	/**
	 * 租户版本，值定义：kc.framework.enums.TenantVersion
	 */
	@Column(name = "Version")
	private int version;

	/**
	 * 是否Ext页面
	 */
	@Column(name = "IsExtPage")
	private boolean isExtPage;

	@Column(name = "ApplicationId")
	private UUID applicationId;

	@Column(name = "ApplicationName")
	private String applicationName;

	/**
	 * 菜单的权限控制Id
	 */
	@Column(name = "AuthorityId")
	private String authorityId;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy ="menuNodes")
	private Set<Role> roles = new HashSet<>();

	public String getMenuURL() {
		if (StringExtensions.isNullOrEmpty(controllerName) && StringExtensions.isNullOrEmpty(actionName))
		{
			return "";
		}

		if(StringExtensions.isNullOrEmpty(areaName))
			return String.format("/%s/%s", controllerName.trim(), actionName.trim());

		return String.format("/%s/%s/%s", areaName.trim(), controllerName.trim(), actionName.trim());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		MenuNode node = (MenuNode) o;

		if (!Objects.equals(areaName, node.areaName))
			return false;
		if (!Objects.equals(controllerName, node.controllerName))
			return false;
		if (!Objects.equals(actionName, node.actionName))
			return false;
		return Objects.equals(authorityId, node.authorityId);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (areaName != null ? areaName.hashCode() : 0);
		result = 31 * result + (controllerName != null ? controllerName.hashCode() : 0);
		result = 31 * result + (actionName != null ? actionName.hashCode() : 0);
		result = 31 * result + (authorityId != null ? authorityId.hashCode() : 0);
		return result;
	}
}
