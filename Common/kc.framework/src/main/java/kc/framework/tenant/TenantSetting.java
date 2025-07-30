package kc.framework.tenant;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kc.framework.base.PropertyAttributeBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity
@Table(name = "tenant_TenantUserSetting")
public class TenantSetting extends PropertyAttributeBase implements java.io.Serializable{

	private static final long serialVersionUID = -832571602696237643L;
	
	@ManyToOne
	@JoinColumn(name = "TenantId", nullable = false)
	private Tenant tenant;
}
