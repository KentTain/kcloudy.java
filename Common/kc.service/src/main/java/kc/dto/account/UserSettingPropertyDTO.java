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
public class UserSettingPropertyDTO extends PropertyAttributeBaseDTO implements java.io.Serializable{

	/**
	 * 系统设置属性Id
	 */
	private Integer systemSettingPropertyAttrId;

	/**
	 * 设置系统编号
	 */
	private Integer userSettingCode;

	private UserSettingDTO userSetting;
}
