package kc.model.account;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
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
@Table(name="sys_Permission")
@NamedEntityGraph(name = "Graph.Permission.Roles", 
	attributeNodes = {@NamedAttributeNode("roles")})
public class Permission extends TreeNode<Permission> implements java.io.Serializable {

	private static final long serialVersionUID = 6362006093819856408L;

	@Column(name = "AreaName")
	private String areaName;
    
	@Column(name = "ControllerName")
	private String controllerName;
    
	@Column(name = "ActionName")
	private String actionName;
    
	@Column(name = "Parameters")
	private String parameters ;
	/**
	 * 业务类型
	 * kc.framework.enums.ResultType
	 */
	//@Enumerated(EnumType.ActionResult)
	@Column(name = "ResultType")
	private int resultType;

	@Column(name = "Description", length=4000)
	private String description;

	@Column(name = "ApplicationId")
	private UUID applicationId;

	@Column(name = "ApplicationName")
	private String applicationName;

	@Column(name = "AuthorityId")
	private String authorityId;
    
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy ="permissions")
	private Set<Role> roles = new HashSet<Role>();

	public String getPermissionURL()
	{
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

		Permission node = (Permission) o;

		if (!Objects.equals(areaName, node.areaName))
			return false;
		if (!Objects.equals(controllerName, node.controllerName))
			return false;
		if (!Objects.equals(actionName, node.actionName))
			return false;
		if (!Objects.equals(applicationId, node.applicationId))
			return false;
		return Objects.equals(authorityId, node.authorityId);

	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (areaName != null ? areaName.hashCode() : 0 );
		result = 31 * result + (controllerName != null ? controllerName.hashCode() : 0 );
		result = 31 * result + (actionName != null ? actionName.hashCode() : 0 );
		result = 31 * result + (applicationId != null ? applicationId.hashCode() : 0 );
		result = 31 * result + (authorityId != null ? authorityId.hashCode() : 0 );
		return result;
	}
}
