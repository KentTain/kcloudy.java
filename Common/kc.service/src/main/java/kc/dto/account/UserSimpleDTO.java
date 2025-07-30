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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class UserSimpleDTO extends EntityBaseDTO implements java.io.Serializable {

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
	private String userName;

	/**
	 * 会员编号
	 */
	private String memberId;
	/**
	 * 会员显示名
	 */
	private String displayName;
	/**
	 * 会员Email
	 */
	private String email;

	/**
	 * 会员手机号
	 */
	private String phoneNumber;
	/**
	 * 登陆错误次数
	 */
	private int accessFailedCount;

	/**
	 * 创建日期
	 */
	@com.fasterxml.jackson.annotation.JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
	private Date createDate;

	public String contactQQ;

	private String telephone;

	/**
	 * 是否为客户
	 */
	private boolean client;

	/**
	 * 微信公众号
	 */
	private String openId;

	private String referenceId1;

	private String referenceId2;

	private String referenceId3;

	/**
	 * 用户状态 kc.framework.enums.WorkflowBusStatus
	 */
	private int status;

	private boolean isModifyPassword = false;

	private List<String> userRoleIds = new ArrayList<String>();

	private List<String> userRoleNames = new ArrayList<String>();

	private List<Integer> userOrgIds = new ArrayList<Integer>();

	private List<String> userOrgNames = new ArrayList<String>();

}
