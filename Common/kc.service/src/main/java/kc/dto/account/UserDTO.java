package kc.dto.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.MappedSuperclass;

import kc.dto.EntityBaseDTO;
import kc.framework.enums.PositionLevel;
import kc.framework.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class UserDTO extends EntityBaseDTO implements java.io.Serializable {

	@com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
	private boolean isEditMode = false;

	/**
	 * 用户类型 kc.enums.base.UserType
	 */
	private int userType;

	private String userTypeName;
	/**
	 * 岗位 kc.enums.admin.PositionLevel
	 */
	private int positionLevel;

	private String positionLevelName;


	/**
	 * 会员Guid
	 */
	private String userId;

	/**
	 * 会员登陆名
	 */
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String userName;

	/**
	 * 会员编号
	 */
	@Length(min = 0, max = 128, message = "属性名称不能超过128个字符")
	private String memberId;

	/**
	 * 会员显示名
	 */
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String displayName;
	
	/**
	   *  是否为客户
	 */
	private boolean client;

	/**
	 * 会员Email
	 */
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String email;

	/**
	 * 邮箱是否验证
	 */
	private boolean emailConfirmed = false;

	/**
	 * 邮箱验证过期时间
	 */
	@com.fasterxml.jackson.annotation.JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
	private Date emailConfirmedExpired;

	/**
	 * 邮箱验证时间
	 */
	@com.fasterxml.jackson.annotation.JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
	private Date emailConfirmedDate;

	/**
	 * 会员手机号
	 */
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String phoneNumber;

	/**
	 * 手机号是否验证
	 */
	private boolean phoneNumberConfirmed;

	/**
	 * 是否锁定
	 */
	private boolean lockoutEnabled = false;

	private boolean twoFactorEnabled = false;

	/**
	 * 登陆错误次数
	 */
	private int accessFailedCount;

	/**
	 * 创建日期
	 */
	@com.fasterxml.jackson.annotation.JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
	private Date createDate;

	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	public String contactQQ;

	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String telephone;

	private boolean isDefaultMobile = false;

	/**
	 * 微信公众号
	 */
	private String openId;

	private boolean isSystemAdmin = false;

	/**
	 * 外部编号1
	 */
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String referenceId1;

	/**
	 * 外部编号2
	 */
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String referenceId2;
	/**
	 * 外部编号3
	 */
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String referenceId3;

	/**
	 * 用户状态 kc.framework.enums.WorkflowBusStatus
	 */
	private int status;

	private String password;

	private boolean isModifyPassword = false;

	private List<String> roleIds = new ArrayList<String>();

	private List<String> roleNames = new ArrayList<String>();

	private List<Integer> organizationIds = new ArrayList<Integer>();

	private String OrganizationNames;
}
