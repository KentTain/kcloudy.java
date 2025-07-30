package kc.dto.workflow;

import kc.dto.RuleEntityBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class WorkflowProTaskRuleDTO extends RuleEntityBaseDTO implements java.io.Serializable {

	/**
	 * 任务GUID
	 */
	private UUID taskId;
	/**
	 * 任务编码
	 */
	private String taskCode;

	/**
	 * 任务对象
	 */
	private WorkflowProTaskDTO task;

}
