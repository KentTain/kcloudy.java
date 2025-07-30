package kc.model.account;

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
@Table(name="sys_SystemSetting")
@NamedEntityGraph(name = "Graph.SystemSetting.PropertyAttributeList", attributeNodes = {@NamedAttributeNode("propertyAttributeList")})
public class SystemSetting extends PropertyBase<SystemSettingProperty> {
	private static final long serialVersionUID = -3428273179520510974L;

	/**
	 * 设置系统编号（SequenceName--SystemSetting：SSC2018120100001）
	 */
	@Column(name = "Code", length = 20)
	private String code;

	@Column(name = "ApplicationId")
	private String applicationId;

	@Column(name = "ApplicationName")
	private String applicationName;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "systemSetting", cascade = CascadeType.ALL)
	private Set<SystemSettingProperty> propertyAttributeList = new HashSet<>();
}
