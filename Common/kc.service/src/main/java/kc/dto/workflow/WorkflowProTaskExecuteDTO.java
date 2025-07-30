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
 * 任务执行详情
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class WorkflowProTaskExecuteDTO extends EntityBaseDTO implements Serializable {

	private int id;

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
	 * 下一节点的审核人Ids
	 */
	private String nextAuditorUserIds;
	/**
	 * 下一节点的审核人姓名列表
	 */
	private String nextAuditorUserNames;


	/**
	 * 流程任务执行状态：0：Init-初始化；1：Agree-同意、2：Return-退回、3：Revoke-撤回、4：Cancel-取消、5：Pause-中止
	 */
	private WorkflowExecuteStatus executeStatus = WorkflowExecuteStatus.Init;
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
	 * 任务对象
	 */
	private WorkflowProTaskDTO task;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof WorkflowProTaskExecuteDTO))
			return false;
		if (!super.equals(o))
			return false;

		WorkflowProTaskExecuteDTO entity = (WorkflowProTaskExecuteDTO) o;

		if (!Objects.equals(id, entity.id))
			return false;
		if (!Objects.equals(taskId, entity.taskId))
			return false;
		if (!Objects.equals(taskCode, entity.taskCode))
			return false;
		if (!Objects.equals(taskName, entity.taskName))
			return false;
		if (!Objects.equals(executeUserId, entity.executeUserId))
			return false;
		if (!Objects.equals(executeUserName, entity.executeUserName))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + id;
		result = 31 * result + (executeUserId != null ? executeUserId.hashCode() : 0 );
		result = 31 * result + (executeUserName != null ? executeUserName.hashCode() : 0 );
		result = 31 * result + (taskId != null ? taskId.hashCode() : 0 );
		result = 31 * result + (taskCode != null ? taskCode.hashCode() : 0 );
		result = 31 * result + (taskName != null ? taskName.hashCode() : 0 );
		return result;
	}
}
