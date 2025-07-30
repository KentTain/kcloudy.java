package kc.dto.app;

import kc.dto.PropertyAttributeBaseDTO;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppSettingPropertyDTO extends PropertyAttributeBaseDTO {

	private int appSettingId;

	/**
	 * 设置系统编号
	 */
	@Length(min = 1, max = 20, message = "应用设置编码不能超过20个字符")
	private String appSettingCode;

	private String appSettingName;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		AppSettingPropertyDTO node = (AppSettingPropertyDTO) o;

		if (!Objects.equals(appSettingCode, node.appSettingCode))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (appSettingCode != null ? appSettingCode.hashCode() : 0);
		return result;
	}
}
