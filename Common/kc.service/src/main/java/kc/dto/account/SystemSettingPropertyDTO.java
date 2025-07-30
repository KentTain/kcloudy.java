package kc.dto.account;

import kc.dto.PropertyAttributeBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SystemSettingPropertyDTO extends PropertyAttributeBaseDTO implements java.io.Serializable{

	/**
	 * 设置系统编号（SequenceName--SystemSetting：SSC2018120100001）
	 */
	private String systemSettingCode;

	private SystemSettingDTO systemSetting;
}
