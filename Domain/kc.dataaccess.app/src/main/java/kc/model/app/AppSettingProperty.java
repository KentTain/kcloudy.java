package kc.model.app;

import kc.framework.base.PropertyAttributeBase;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name=Tables.AppSettingProperty)
public class AppSettingProperty extends PropertyAttributeBase {

	/**
	 * 设置系统编号
	 */
	@Column(name = "AppSettingCode", length = 20)
	private String appSettingCode;

	@ManyToOne
	@JoinColumn(name = "AppSettingId", nullable = false, foreignKey = @ForeignKey(name="FK_app_AppSettingProperty_app_AppSetting_AppSettingId"))
	private AppSetting appSetting;

}
