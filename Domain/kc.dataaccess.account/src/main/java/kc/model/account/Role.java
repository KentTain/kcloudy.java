package kc.model.account;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import kc.framework.base.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@javax.persistence.Entity
@Table(name="sys_Role")
@NamedEntityGraph(name = "Graph.Role.Users", 
	attributeNodes = {@NamedAttributeNode("users")})
@NamedEntityGraph(name = "Graph.Role.MenuNodes", 
	attributeNodes = {@NamedAttributeNode("menuNodes")})
@NamedEntityGraph(name = "Graph.Role.Permissions", 
	attributeNodes = {@NamedAttributeNode("permissions")})
public class Role extends Entity implements java.io.Serializable {

	private static final long serialVersionUID = -5382217821353495415L;
	
	@javax.persistence.Id
	@Column(name = "Id")
	private String id;
	
	@Column(name = "Name")
	private String name;
	
	@Column(name = "NormalizedName")
	private String normalizedName;
	
	@Column(name = "ConcurrencyStamp")
	private String concurrencyStamp;
	
	@Column(name = "DisplayName")
	private String displayName;
	
	@Column(name = "Description", length=4000)
	private String description;
	
	@Column(name = "IsSystemRole")
	private boolean isSystemRole;
	
	/**
	 * 业务类型：BusinessType
	 */
	@Column(name = "BusinessType")
	private int businessType;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
			name = "sys_UsersInRoles",
			joinColumns = { @JoinColumn(name = "RoleId", foreignKey = @ForeignKey(name="FK_sys_UsersInRoles_sys_Role_RoleId")) },
			inverseJoinColumns = { @JoinColumn(name = "UserId", foreignKey = @ForeignKey(name="FK_sys_UsersInRoles_sys_User_UserId")) }

		)
	private Set<User> users = new HashSet<User>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
			name = "sys_MenuNodesInRoles",
			joinColumns = { @JoinColumn(name = "RoleId", foreignKey = @ForeignKey(name="FK_sys_MenuNodesInRoles_sys_Role_RoleId")) },
			inverseJoinColumns = { @JoinColumn(name = "MenuNodeId", foreignKey = @ForeignKey(name="FK_sys_MenuNodesInRoles_sys_MenuNode_MenuNodeId")) }
		)
	private Set<MenuNode> menuNodes = new HashSet<MenuNode>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
			name = "sys_PermissionsInRoles",
			joinColumns = { @JoinColumn(name = "RoleId", foreignKey = @ForeignKey(name="FK_sys_PermissionsInRoles_sys_Role_RoleId")) },
			inverseJoinColumns = { @JoinColumn(name = "PermissionId", foreignKey = @ForeignKey(name="FK_sys_PermissionsInRoles_sys_Permission_PermissionId")) }
		)
	private Set<Permission> permissions = new HashSet<Permission>();

}
