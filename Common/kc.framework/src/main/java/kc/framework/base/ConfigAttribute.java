package kc.framework.base;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@javax.persistence.Entity
@Table(name = "cfg_ConfigAttribute")
//@AttributeOverride(name = "config_id", column = @Column(name = "ConfigId"))
public class ConfigAttribute extends PropertyAttributeBase implements java.io.Serializable {

	private static final long serialVersionUID = -4826723150311364934L;

	@Column(name = "DisplayName", length = 256)
	private String displayName;

	@Column(name = "Description", length = 1024)
	private String description;

	//@Column(name = "ConfigId")
	//private int configId;

	@Column(name = "IsFileAttr")
	private Boolean isFileAttr;

	@ManyToOne
	@JoinColumn(name = "ConfigId", nullable = false, foreignKey = @ForeignKey(name="FK_cfg_ConfigAttribute_cfg_ConfigEntity_ConfigId"))
	private ConfigEntity configEntity;
}
