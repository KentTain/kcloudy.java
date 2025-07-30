package kc.web.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordViewModel implements java.io.Serializable {

	private static final long serialVersionUID = -3705675475028634127L;

	/**
	 * 当前密码: 必填
	 */
	private String OldPassword;

	/**
	 * 新密码: 必填--密码长度必须在6位与16位之间
	 */
	private String NewPassword;

	/**
	 * 确认密码: 必填--密码长度必须在6位与16位之间、新密码和确认密码不匹配
	 */
	private String ConfirmPassword;
}
