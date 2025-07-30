package kc.dto.app;

import kc.dto.PropertyBaseDTO;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppSettingDTO extends PropertyBaseDTO<AppSettingPropertyDTO> {

	@com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
	private boolean isEditMode;

    /**
     * 设置系统编号（SequenceName--AppSetting：ASC2018120100001）
	 */
	@Length(min = 1, max = 20, message = "应用设置编码不能超过20个字符")
	private String code;

	private String applicationId;

	private String applicationName;

	private List<AppSettingPropertyDTO> propertyAttributeList = new ArrayList<>();

}
