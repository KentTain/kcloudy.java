package kc.dto.workflow;

import kc.dto.BlobInfoDTO;
import kc.dto.EntityBaseDTO;
import kc.enums.WorkflowExecuteStatus;
import kc.framework.enums.WorkflowNodeType;
import kc.framework.extension.StringExtensions;
import kc.framework.util.SerializeHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.*;


/**
 * 流程发起提交模型
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class WorkflowExecuteData extends EntityBaseDTO implements Serializable {

	private UUID id = UUID.randomUUID();

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
	 * 任务GUID
	 */
	private UUID taskId;
	/**
	 * 任务编码
	 */
	private String taskCode;
	/**
	 * 任务名称
	 */
	private String taskName;

	/**
	 * 任务节点类型：0：Start-开始；1：Task-任务、2：Condition-条件、3：SubFlow-子流程、4：End-结束
	 */
	private WorkflowNodeType taskType = WorkflowNodeType.Task;
	private String taskTypeString;
	public String getTaskTypeString() { return taskType.getDesc(); }

	/**
	 * 通知人Ids
	 */
	private String notifyUserIds;

	/**
	 * 通知人姓名
	 */
	private String notifyUserNames;

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
	 * 执行人Id
	 */
	private String executeUserId;
	/**
	 * 执行人姓名
	 */
	private String executeUserName;

	/**
	 * 执行时间
	 */
	private Date executeDateTime;

	/**
	 * 流程任务执行状态
	 */
	private WorkflowExecuteStatus executeStatus;
	private String executeStatusString;
	public String getExecuteStatusString() { return executeStatus.getDesc(); }

	/**
	 * 执行附件
	 */
	private String executeFileBlob;

	/**
	 * 附件对象
	 */
	private BlobInfoDTO executeFile;
	public BlobInfoDTO getExecuteFile()
	{
		if (StringExtensions.isNullOrEmpty(executeFileBlob))
			return null;

		return SerializeHelper.FromJson(executeFileBlob, BlobInfoDTO.class);
	}

	/**
	 * 执行描述
	 */
	private String executeRemark;

	/**
	 * 表单数据
	 */
	private List<WorkflowProFieldDTO> formData = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof WorkflowExecuteData))
			return false;
		if (!super.equals(o))
			return false;

		WorkflowExecuteData entity = (WorkflowExecuteData) o;

		if (!Objects.equals(id, entity.id))
			return false;
		if (!Objects.equals(workflowDefId, entity.workflowDefId))
			return false;
		if (!Objects.equals(workflowDefCode, entity.workflowDefCode))
			return false;
		if (!Objects.equals(workflowDefVersion, entity.workflowDefVersion))
			return false;
		if (!Objects.equals(taskId, entity.taskId))
			return false;
		if (!Objects.equals(taskCode, entity.taskCode))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (id != null ? id.hashCode() : 0 );
		result = 31 * result + (workflowDefId != null ? workflowDefId.hashCode() : 0 );
		result = 31 * result + (workflowDefCode != null ? workflowDefCode.hashCode() : 0 );
		result = 31 * result + (workflowDefVersion != null ? workflowDefVersion.hashCode() : 0 );
		result = 31 * result + (taskId != null ? taskId.hashCode() : 0 );
		result = 31 * result + (taskCode != null ? taskCode.hashCode() : 0 );
		return result;
	}
}
