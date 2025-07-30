package kc.dto.account;

import kc.dto.PropertyBaseDTO;
import kc.framework.base.PropertyBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SystemSettingDTO extends PropertyBaseDTO<SystemSettingPropertyDTO> implements java.io.Serializable{

    /**
     * 设置系统编号（SequenceName--SystemSetting：SSC2018120100001）
	 */
	private String code;

	private String applicationId;

	private String applicationName;
}
