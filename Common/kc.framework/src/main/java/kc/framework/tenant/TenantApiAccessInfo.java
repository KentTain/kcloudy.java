package kc.framework.tenant;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import kc.framework.base.Entity;
import kc.framework.base.ServiceRequestToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@MappedSuperclass
public class TenantApiAccessInfo extends Entity implements java.io.Serializable{
	private static final long serialVersionUID = 7910316812466702986L;

	/**
	 * 租户Id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TenantId", unique = true, nullable = false)
	private int tenantId;

	/**
	 * 租户Code：Client's MemberId（如：2014073100001）
	 */
	@Column(name = "TenantName", unique = true, nullable = false, length=20)
	private String tenantName;

	/**
	 * 租户名称
	 */
	@Column(name = "TenantDisplayName", nullable = false, length=512)
	private String tenantDisplayName;

	/**
	 * 租户的共用（访问WebApi时使用）
	 */
	@Column(name = "TenantSignature", nullable = false, length=2000)
	private String tenantSignature;

	/**
	 * 租户类型：KC.Framework.Tenant.TenantType
	 */
	@Column(name = "TenantType")
	private Integer tenantType;

	/**
	 * 租户版本：KC.Framework.Tenant.TenantVersion
	 */
	@Column(name = "Version")
	private Integer version;

	/**
	 * 云类型：KC.Framework.Tenant.CloudType
	 */
	@Column(name = "CloudType")
	private kc.framework.enums.CloudType cloudType;

	@Column(name = "TenantLogo", length=2000)
	private String tenantLogo;

	@Column(name = "TenantIntroduction", length=4000)
	private String tenantIntroduction;

	/**
	 * 租户私钥，用于加密租户敏感数据用
	 */
	@Column(name = "PrivateEncryptKey", nullable = false, length=1000)
	private String privateEncryptKey;

	/**
	 * 租户别名最后的修改时间
	 */
	public String[] Hostnames;

	public String GenerateSignature() throws Exception {
		ServiceRequestToken s = new ServiceRequestToken(tenantId, tenantName, privateEncryptKey);
		return s.GetEncrptSignature();
	}

	public ServiceRequestToken GetServiceToken() {
		return new ServiceRequestToken(tenantSignature, privateEncryptKey);
	}
}
