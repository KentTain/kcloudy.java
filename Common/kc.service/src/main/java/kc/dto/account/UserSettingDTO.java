package kc.dto.account;

import kc.dto.PropertyBaseDTO;
import kc.framework.base.PropertyBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSettingDTO extends PropertyBaseDTO<UserSettingPropertyDTO>  implements java.io.Serializable{

    /**
     * 设置系统编号（SequenceName--SystemSetting：SSC2018120100001）
	 */
	private String code;

	private boolean isSystemSetting;

	private Integer systemSettingId;

	private String systemSettingCode;

	private String applicationId;

	private String applicationName;

	private UserDTO user;
}
