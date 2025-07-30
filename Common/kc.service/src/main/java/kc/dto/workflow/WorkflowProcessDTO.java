package kc.dto.workflow;

import kc.dto.EntityDTO;
import kc.enums.WorkflowProcessStatus;
import kc.framework.enums.SecurityType;
import kc.framework.enums.WorkflowBusStatus;
import kc.framework.enums.WorkflowFormType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.*;

/**
 * 流程执行处理过程
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class WorkflowProcessDTO extends DefinitionBaseDTO implements Serializable {

	/**
	 * 发起人Id
	 */
	private String submitUserId;

	/**
	 * 发起人
	 */
	private String submitUserName;

	/**
	 * 流程状态
	 */
	private WorkflowProcessStatus processStatus;

	/**
	 * 开始时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
	//@com.fasterxml.jackson.annotation.JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@com.fasterxml.jackson.annotation.JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
	private Date startDateTime = new Date();

	/**
	 * 结束时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
	//@com.fasterxml.jackson.annotation.JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@com.fasterxml.jackson.annotation.JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
	private Date endDateTime = new Date();

	/**
	 * 规定结束时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
	//@com.fasterxml.jackson.annotation.JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@com.fasterxml.jackson.annotation.JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
	private Date deadlineDate = new Date();

	/**
	 * 任务Id
	 */
	private UUID currentTaskId;

	/**
	 * 流程定义GUID
	 */
	private UUID workflowDefId;

	/**
	 * 流程定义编码
	 */
	private String workflowDefCode;

	/**
	 * 流程定义版本
	 */
	private String workflowDefVersion;

	/**
	 * 流程定义名称
	 */
	private String workflowDefName;

	/**
	 * 流程任务数据
	 */
	private List<WorkflowProTaskDTO> tasks = new ArrayList<>();

	/**
	 * 流程表单数据
	 */
	private List<WorkflowProFieldDTO> context = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof WorkflowProcessDTO))
			return false;
		if (!super.equals(o))
			return false;

		WorkflowProcessDTO entity = (WorkflowProcessDTO) o;

		if (!Objects.equals(submitUserId, entity.submitUserId))
			return false;
		if (!Objects.equals(processStatus, entity.processStatus))
			return false;
		if (!Objects.equals(currentTaskId, entity.currentTaskId))
			return false;
		if (!Objects.equals(workflowDefId, entity.workflowDefId))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (submitUserId != null ? submitUserId.hashCode() : 0 );
		result = 31 * result + (processStatus != null ? processStatus.hashCode() : 0 );
		result = 31 * result + (currentTaskId != null ? currentTaskId.hashCode() : 0 );
		result = 31 * result + (workflowDefId != null ? workflowDefId.hashCode() : 0 );
		return result;
	}
}
