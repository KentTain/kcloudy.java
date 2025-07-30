package kc.enums;

import kc.framework.enums.EnumBase;

import java.util.List;

public enum WorkflowExecuteStatus implements EnumBase {
	/**
	 * 未处理
	 */
	Init("未处理", 0),

	/**
	 * 同意：每个节点的人将流程提交至下一节点
	 */
	Approve("同意", 1),

	/**
	 * 退回：可退回到某个节点继续流转，退回到发起节点，或退回到前节点
	 */
	Return("退回", 2),

	/**
	 * 撤回：节点执行完后、下一节点执行前，可以收回进行修改然后再提交
	 */
	Revoke("撤回", 3),

	/**
	 * 取消：流程发起人执行取消流程
	 */
	Cancel("取消", 4),

	/**
	 * 中止：流程提前结束，当前节点之后的其它节点不再执行
	 */
	Pause("中止", 5);

	// 定义一个 private 修饰的实例变量
	private String desc;
	private Integer index;

	// 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
	private WorkflowExecuteStatus(String desc, Integer index) {
		this.desc = desc;
		this.index = index;
	}

	/**
	 * 根据索引值：Integer的值获取Enum对象
	 * 
	 * @param index
	 * @return ConfigStatus Enum对象
	 */
	@com.fasterxml.jackson.annotation.JsonCreator
	public static WorkflowExecuteStatus valueOf(Integer index) {
		for (WorkflowExecuteStatus type : values()) {
            if (type.getIndex() == index) {
                return type;
            }
        }
        return null;
    }
	
	/**
	 * 根据Index的值，获取Enum类型的描述
	 * 
	 * @param index
	 * @return String Enum类型的描述
	 */
	public static String getDesc(Integer index) {
		for (WorkflowExecuteStatus c : values()) {
			if (c.getIndex() == index) {
				return c.getDesc();
			}
		}
		return null;
	}
	
	/**
	 * 获取Enum所有类型的Map<索引值, Enum类型的描述>
	 * 
	 * @param exceptEnums 需要排除的Enum类型列表
	 * @return Map<Integer, String>
	 */
	public static java.util.Map<Integer, String> getList(List<WorkflowExecuteStatus> exceptEnums) {
		java.util.Map<Integer, String> result = new java.util.HashMap<Integer, String>();
		for (WorkflowExecuteStatus c : values()) {
			if (exceptEnums != null && exceptEnums.contains(c))
				continue;
			result.put(c.getIndex(), c.getDesc());
		}

		return result;
	}

	// 定义 get set 方法
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	@com.fasterxml.jackson.annotation.JsonValue
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
}
