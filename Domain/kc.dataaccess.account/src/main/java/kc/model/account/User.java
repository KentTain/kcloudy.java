package kc.model.account;

import java.util.HashSet;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import kc.framework.base.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity
@Table(name = "sys_User")
@NamedEntityGraph(name = "Graph.User.Organizations", attributeNodes = { @NamedAttributeNode("organizations") })
@NamedEntityGraph(name = "Graph.User.Roles", attributeNodes = { @NamedAttributeNode("roles") })
@NamedEntityGraph(name = "Graph.User.UserSettings", attributeNodes = { @NamedAttributeNode("userSettings") })
public class User extends EntityBase implements java.io.Serializable {

	private static final long serialVersionUID = -7713768356376622996L;

	/**
	 * 会员Guid
	 */
	@javax.persistence.Id
	@Column(name = "Id")
	private String id;
	/**
	 * 用户类型：kc.enums.base.UserType
	 */
	@Column(name = "UserType")
	private int userType;
	/**
	 * 会员登陆名
	 */
	@Column(name = "UserName")
	private String userName;

	@Column(name = "NormalizedUserName")
	private String normalizedUserName;

	/**
	 * 会员编号
	 */
	@Column(name = "MemberId")
	private String memberId;

	/**
	 * 会员显示名
	 */
	@Column(name = "DisplayName")
	private String displayName;

	/**
	 * 岗位类型：kc.enums.base.PositionLevel
	 */
	@Column(name = "PositionLevel")
	private int positionLevel;

	/**
	 * 会员Email
	 */
	@Column(name = "Email")
	private String email;

	@Column(name = "NormalizedEmail")
	private String normalizedEmail;

	/**
	 * 是否为客户
	 */
	@Column(name = "IsClient")
	private Boolean client = false;

	@Column(name = "PasswordHash")
	private String passwordHash;

	/**
	 * 邮箱是否验证
	 */
	@Column(name = "EmailConfirmed")
	private Boolean emailConfirmed = false;

	/**
	 * 邮箱验证过期时间
	 */
	@Column(name = "EmailConfirmedExpired")
	private Date emailConfirmedExpired;

	/**
	 * 邮箱验证时间
	 */
	@Column(name = "EmailConfirmedDate")
	private Date emailConfirmedDate;

	/**
	 * 会员手机号
	 */
	@Column(name = "PhoneNumber")
	private String phoneNumber;

	/**
	 * 手机号是否验证
	 */
	@Column(name = "PhoneNumberConfirmed")
	private Boolean phoneNumberConfirmed = false;

	@Column(name = "LockoutEnd")
	private Date lockoutEnd;

	/**
	 * 是否锁定
	 */
	@Column(name = "LockoutEnabled")
	private Boolean lockoutEnabled = false;

	@Column(name = "SecurityStamp")
	private String securityStamp;

	@Column(name = "ConcurrencyStamp")
	private String concurrencyStamp;

	@Column(name = "TwoFactorEnabled")
	private Boolean twoFactorEnabled = false;

	/**
	 * 登陆错误次数
	 */
	@Column(name = "AccessFailedCount")
	private int accessFailedCount;

	/**
	 * 创建日期
	 */
	@Column(name = "CreateDate")
	private Date createDate;

	@Column(name = "ContactQQ")
	private String contactQQ;

	@Column(name = "Telephone")
	private String telephone;

	@Column(name = "IsDefaultMobile")
	private Boolean isDefaultMobile = false;

	/**
	 * 微信公众号
	 */
	@Column(name = "OpenId")
	private String openId;

	/**
	 * 外部编号1
	 */
	@Column(name = "ReferenceId1")
	private String referenceId1;

	/**
	 * 外部编号2
	 */
	@Column(name = "ReferenceId2")
	private String referenceId2;
	/**
	 * 外部编号3
	 */
	@Column(name = "ReferenceId3")
	private String referenceId3;

	/**
	 * 个人状态：kc.framework.enums.WorkflowBusStatus
	 */
	@Column(name = "Status")
	private int status;

	@Column(name = "IsModifyPassword")
	private Boolean isModifyPassword = false;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sys_UsersInOrganizations", 
		joinColumns = { @JoinColumn(name = "UserId", foreignKey = @ForeignKey(name="FK_sys_UsersInOrganizations_sys_User_UserId")) },
		inverseJoinColumns = { @JoinColumn(name = "OrganizationId", foreignKey = @ForeignKey(name="FK_sys_UsersInOrganizations_sys_Organization_OrganizationId")) })
	private Set<Organization> organizations = new HashSet<Organization>();

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
	private Set<Role> roles = new HashSet<Role>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<UserSetting> userSettings = new HashSet<UserSetting>();
}
