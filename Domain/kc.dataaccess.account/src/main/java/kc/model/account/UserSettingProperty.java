package kc.model.account;

import kc.framework.base.PropertyAttributeBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="sys_UserSettingProperty")
public class UserSettingProperty extends PropertyAttributeBase {
	private static final long serialVersionUID = -3428273179520510974L;

	/**
	 * 系统设置属性Id
	 */
	@Column(name = "SystemSettingPropertyAttrId")
	private Integer systemSettingPropertyAttrId;

	/**
	 * 设置系统编号
	 */
	@Column(name = "UserSettingCode")
	private Integer userSettingCode;

	@ManyToOne
	@JoinColumn(name = "UserSettingId", nullable = false, foreignKey = @ForeignKey(name="FK_sys_UserSettingProperty_sys_UserSetting_UserSettingId"))
	private UserSetting userSetting;
}
