package kc.dto.workflow;

import kc.dto.DataPermitEntityDTO;
import kc.dto.EntityDTO;
import kc.dto.RuleEntityBaseDTO;
import kc.framework.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class DefNodeBaseDTO<T extends RuleEntityBaseDTO> extends DataPermitEntityDTO implements Serializable {

	private UUID id = UUID.randomUUID();

	private String code;

	private String name;

	private WorkflowNodeType nodeType;
	private String nodeTypeString;
	public String getNodeTypeString() { return nodeType.getDesc(); }

	/**
     * 设置的执行结束间隔天数
	 */
	private Integer deadlineInterval;

	/**
	 * 消息模板编码
	 */
	private String messageTemplateCode;

	/**
	 * 节点离顶部距离
	 */
	private BigDecimal locTop;


	/**
	 * 节点离左边距离
	 */
	private BigDecimal locLeft;

	/**
	 * 前一节点编码
	 */
	private String prevNodeCode;

	/**
	 * 后一节点编码
	 */
	private String nextNodeCode;

	/**
	 * 回退节点编码（如果NodeType==Condition，需要填写回退节点Code）
	 */
	private String returnNodeCode;

	/**
	 * 子流程Id
	 */
	private UUID subDefinitionId;

	/**
	 * 子流程编码：wfd2012020200001
	 */
	private String subDefinitionCode;

	/**
	 * 子流程版本：wfv2012020200001
	 */
	private String subDefinitionVersion;

	/**
	 * 子流程名称
	 */
	private String subDefinitionName;

	/**
	 * 流程类型：0：SingleLine-单线审批、1：MultiLine-多线审批、2：WeightLine-权重审批
	 */
	private WorkflowType type = WorkflowType.SingleLine;
	private String typeString;
	public String getTypeString() { return type.getDesc(); }

	/**
	 * 权重值
	 */
	private BigDecimal weightValue;

	/**
	 * 执行人设置类型
	 * 	0：Executor，需要设置执行人（组织、角色、用户）
	 * 	10~13：CreateManager，流程发起人的主管
	 * 	20~23：SubmitManager，上一流程节点的提交人的主管
	 * 	30：FormAuditor，表单设置的审批人
	 */
	private ExecutorSetting executorSetting;
	private String executorSettingString;
	public String getExecutorSettingString() { return executorSetting.getDesc(); }

	/**
	 * 当ExecutorSetting=FormAuditor时，需要设置相关表单的字段名
	 */
	private String executorFormFieldName;

	/**
	 * 当ExecutorSetting=FormAuditor时，需要设置相关表单的字段显示名
	 */
	private String executorFormFieldDisplayName;

	/**
	 * 通知人Ids
	 */
	private String notifyUserIds;

	/**
	 * 通知人姓名
	 */
	private String notifyUserNames;

	/**
	 * 条件为true时的设置公式：WorkflowDefField.Name >=、=、<=、contains 值
	 */
	private List<T> rules = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DefNodeBaseDTO))
			return false;
		if (!super.equals(o))
			return false;

		DefNodeBaseDTO entity = (DefNodeBaseDTO) o;

		if (!Objects.equals(id, entity.id))
			return false;
		if (!Objects.equals(code, entity.code))
			return false;
		if (!Objects.equals(name, entity.name))
			return false;
		if (!Objects.equals(nodeType, entity.nodeType))
			return false;
		if (!Objects.equals(type, entity.type))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (id != null ? id.hashCode() : 0 );
		result = 31 * result + (code != null ? code.hashCode() : 0 );
		result = 31 * result + (name != null ? name.hashCode() : 0 );
		result = 31 * result + (nodeType != null ? nodeType.hashCode() : 0 );
		result = 31 * result + (type != null ? type.hashCode() : 0 );
		return result;
	}
}
