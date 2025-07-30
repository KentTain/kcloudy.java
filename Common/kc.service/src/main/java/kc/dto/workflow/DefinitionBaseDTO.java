package kc.dto.workflow;

import kc.dto.EntityDTO;
import kc.dto.TreeNodeDTO;
import kc.framework.enums.AttributeDataType;
import kc.framework.enums.SecurityType;
import kc.framework.enums.WorkflowBusStatus;
import kc.framework.enums.WorkflowFormType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class DefinitionBaseDTO extends EntityDTO implements Serializable {

	private UUID id = UUID.randomUUID();

	private String code;

	private String version;

	private String name;

	private WorkflowBusStatus status;
	private String statusTypeString;
	public String getStatusString() { return status.getDesc(); }
	/**
	 * 设置的执行结束间隔天数
	 */
	private Integer defDeadlineInterval;

	/**
	 * 消息模板编码
	 */
	private String defMessageTemplateCode;

	/**
	 * 接口安全类型
	 * 	SecurityKey = 0,
	 * 	HeaderKey = 1,
	 * 	OAuth = 2,
	 * 	None = 99,
	 */
	private kc.framework.enums.SecurityType securityType = SecurityType.None;
	private String securityTypeeString;
	public String getSecurityTypeString() { return securityType.getDesc(); }

	/**
	 * 访问外部接口的认证地址
	 */
	private String authAddress;

	/**
	 * 访问外部接口的认证地址附加参数
	 */
	private String authAddressParams;

	/**
	 * 访问外部接口的认证Scope
	 */
	private String authScope;

	/**
	 * 访问外部接口的Key
	 */
	private String authKey;

	/**
	 * 访问外部接口的Secret
	 */
	private String authSecret;

	/**
	 * 表单接入方式
	 * 	0 ：ModelDefinition--表单数据接入
	 * 	1 ：FormAddress--表单地址接入
	 * 	99：None--无
	 */
	private kc.framework.enums.WorkflowFormType workflowFormType = WorkflowFormType.None;

	/**
	 * FormType=FormAddree时：应用表单可访问地址
	 */
	private String appFormDetailApiUrl;

	/**
	 * FormType=FormAddree时：应用表单可访问的QueryString
	 */
	private String appFormDetailQueryString;

	/**
	 * 业务审批通过后，应用回调地址
	 */
	private String appAuditSuccessApiUrl;

	/**
	 * 业务审批退回后，应用回调地址
	 */
	private String appAuditReturnApiUrl;

	/**
	 * 业务审批需要传出的QueryString
	 */
	private String appAuditQueryString;


	private String description;

	private Integer categoryId;

	private String categoryName;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DefinitionBaseDTO))
			return false;
		if (!super.equals(o))
			return false;

		DefinitionBaseDTO entity = (DefinitionBaseDTO) o;

		if (!Objects.equals(id, entity.id))
			return false;
		if (!Objects.equals(code, entity.code))
			return false;
		if (!Objects.equals(version, entity.version))
			return false;
		if (!Objects.equals(name, entity.name))
			return false;
		if (!Objects.equals(status, entity.status))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (id != null ? id.hashCode() : 0 );
		result = 31 * result + (code != null ? code.hashCode() : 0 );
		result = 31 * result + (version != null ? version.hashCode() : 0 );
		result = 31 * result + (name != null ? name.hashCode() : 0 );
		result = 31 * result + (status != null ? status.hashCode() : 0 );
		return result;
	}
}
