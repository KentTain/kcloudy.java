package kc.model.app;

import kc.framework.base.PropertyBase;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name=Tables.AppSetting)
@NamedEntityGraph(name = "Graph.AppSetting.PropertyAttributeList",
		attributeNodes = {@NamedAttributeNode("propertyAttributeList")})
public class AppSetting extends PropertyBase<AppSettingProperty> {
    /**
     * 设置系统编号（SequenceName--AppSetting：ASC2018120100001）
	 */
	@Column(name = "Code", length = 20)
	private String code;

	@ManyToOne
	@JoinColumn(name = "ApplicationId", nullable = false, foreignKey = @ForeignKey(name="FK_app_AppSetting_app_Application_ApplicationId"))
	private Application application;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "appSetting")
	private List<AppSettingProperty> propertyAttributeList = new ArrayList<>();

}
