package kc.model.account;

import kc.framework.base.ConfigEntity;
import kc.framework.base.PropertyBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="sys_UserSetting")
@NamedEntityGraph(name = "Graph.UserSetting.PropertyAttributeList",
		attributeNodes = {@NamedAttributeNode("propertyAttributeList")})
public class UserSetting extends PropertyBase<UserSettingProperty> {
	private static final long serialVersionUID = -3428273179520510974L;

    /**
     * 设置系统编号（SequenceName--SystemSetting：SSC2018120100001）
	 */
	@Column(name = "Code", length = 20)
	private String code;

	@Column(name = "IsSystemSetting")
	private boolean isSystemSetting;

	@Column(name = "SystemSettingId")
	private Integer systemSettingId;

	@Column(name = "SystemSettingCode", length = 20)
	private String systemSettingCode;

	@Column(name = "ApplicationId")
	private String applicationId;

	@Column(name = "ApplicationName")
	private String applicationName;

	@ManyToOne
	@JoinColumn(name = "UserId", nullable = false, foreignKey = @ForeignKey(name="FK_sys_UserSetting_sys_User_UserId"))
	private User user;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userSetting", cascade = CascadeType.ALL)
	private Set<UserSettingProperty> propertyAttributeList = new HashSet<>();
}
