package kc.dto.workflow;

import kc.dto.DataPermitEntityDTO;
import kc.dto.RuleEntityBaseDTO;
import kc.enums.WorkflowTaskStatus;
import kc.framework.enums.ExecutorSetting;
import kc.framework.enums.WorkflowFormType;
import kc.framework.enums.WorkflowNodeType;
import kc.framework.enums.WorkflowType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class WorkflowProTaskDTO extends DefNodeBaseDTO<WorkflowProTaskRuleDTO> implements Serializable {

	private WorkflowTaskStatus taskStatus;
	private String taskStatusString;
	public String getTaskStatusString() { return taskStatus.getDesc(); }

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
	 * 同意人Ids
	 */
	private String agreeUserIds;
	/**
	 * 同意人姓名
	 */
	private String agreeUserNames;
	/**
	 * 不同意人Ids
	 */
	private String disagreeUserIds;
	/**
	 * 不同意人姓名
	 */
	private String disagreeUserNames;

	/**
	 * 未处理人Ids
	 */
	private String unProcessUserIds;
	/**
	 * 未处理人姓名
	 */
	private String unProcessUserNames;

	/**
	 * 下一节点的所有审核人Ids
	 */
	private String allUserIds;
	/**
	 * 下一节点的所有审核人姓名列表
	 */
	private String allUserNames;

	/**
	 * 前一任务节点
	 */
	private UUID prevNodeId;
	/**
	 * 前一任务节点对象
	 */
	private WorkflowProTaskDTO prevNode;

	/**
	 * 下一任务节点
	 */
	private UUID nextNodeId;
	/**
	 * 下一任务节点对象
	 */
	private WorkflowProTaskDTO nextNode;

	/**
	 * 回退任务节点
	 */
	private UUID returnNodeId;
	/**
	 * 回退任务节点对象
	 */
	private WorkflowProTaskDTO returnNode;

	/**
	 * 流程处理对象Id
	 */
	private UUID processId;

	/**
	 * 流程处理对象编码
	 */
	private String processCode;

	/**
	 * 流程处理对象版本
	 */
	private String processVersion;

	/**
	 * 流程处理对象名称
	 */
	private String processName;

	/**
	 * 任务执行详情
	 */
	private List<WorkflowProTaskExecuteDTO> taskExecutes = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof WorkflowProTaskDTO))
			return false;
		if (!super.equals(o))
			return false;

		WorkflowProTaskDTO entity = (WorkflowProTaskDTO) o;

		if (!Objects.equals(taskStatus, entity.taskStatus))
			return false;
		if (!Objects.equals(processId, entity.processId))
			return false;
		if (!Objects.equals(processCode, entity.processCode))
			return false;
		if (!Objects.equals(processName, entity.processName))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (taskStatus != null ? taskStatus.hashCode() : 0 );
		result = 31 * result + (processId != null ? processId.hashCode() : 0 );
		result = 31 * result + (processCode != null ? processCode.hashCode() : 0 );
		result = 31 * result + (processName != null ? processName.hashCode() : 0 );
		return result;
	}
}
