package kc.dto.workflow;

import kc.dto.EntityDTO;
import kc.framework.enums.SecurityType;
import kc.framework.enums.WorkflowBusStatus;
import kc.framework.enums.WorkflowFormType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * 发起流程的数据模型
 */
@Getter
@Setter
@AllArgsConstructor
@MappedSuperclass
public class WorkflowStartExecuteData extends WorkflowExecuteData implements Serializable {

	/**
	 * 表单接入方式
	 * 	0 ：ModelDefinition--表单数据接入
	 * 	1 ：FormAddress--表单地址接入
	 * 	99：None--无
	 */
	private WorkflowFormType workflowFormType = WorkflowFormType.None;

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


	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof WorkflowStartExecuteData))
			return false;
		if (!super.equals(o))
			return false;

		WorkflowStartExecuteData entity = (WorkflowStartExecuteData) o;

		if (!Objects.equals(workflowFormType, entity.workflowFormType))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (workflowFormType != null ? workflowFormType.hashCode() : 0 );
		return result;
	}
}
