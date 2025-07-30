package kc.model.account;

import kc.framework.base.ConfigEntity;
import kc.framework.base.ProcessLogBase;
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
@Table(name="sys_SystemSettingProperty")
public class SystemSettingProperty extends PropertyAttributeBase {
	private static final long serialVersionUID = -3428273179520510974L;

	/**
	 * 设置系统编号（SequenceName--SystemSetting：SSC2018120100001）
	 */
	@Column(name = "SystemSettingCode", length = 20)
	private String systemSettingCode;

	@ManyToOne
	@JoinColumn(name = "SystemSettingId", nullable = false, foreignKey = @ForeignKey(name="FK_sys_SystemSettingProperty_sys_SystemSetting_SystemSettingId"))
	private SystemSetting systemSetting;
}
