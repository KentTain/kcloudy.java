package kc.framework.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

public enum ExecutorSetting implements EnumBase {
	/**
	 * 设置组织角色及用户
	 */
	Executor("设置组织角色及用户", 0),

	/**
	 * 流程发起人的主管
	 */
	CreatorManager("流程发起人的主管", 10),

	/**
	 * 流程发起人所在组织
	 */
	CreatorOrganization("流程发起人所在组织", 11),

	/**
	 * 流程发起人所属角色
	 */
	CreatorRole("流程发起人所属角色", 12),

	/**
	 * 流程发起人的上级主管
	 */
	CreatorSuperior("流程发起人的上级主管", 13),

	/**
	 * 提交审批人的主管
	 */
	SubmitterManager("提交审批人的主管", 20),

	/**
	 * 提交审批人所在组织
	 */
	SubmitterOrganization("提交审批人所在组织", 21),

	/**
	 * 提交审批人所属角色
	 */
	SubmitterRole("提交审批人所属角色", 22),

	/**
	 * 提交审批人的上级主管
	 */
	SubmitterSuperior("提交审批人的上级主管", 23),

	/**
	 * 表单设置的审批人字段
	 */
	FormAuditor("表单设置的审批人字段", 30),

	/**
	 * 表单设置的审批组织字段
	 */
	FormAuditOrg("表单设置的审批组织字段", 31),

	/**
	 * 表单设置的审批角色字段
	 */
	FormAuditRole("表单设置的审批角色字段", 32);

	// 定义一个 private 修饰的实例变量
	private String desc;
	private Integer index;

	// 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
	private ExecutorSetting(String desc, Integer index) {
		this.desc = desc;
		this.index = index;
	}

	/**
	 * 根据索引值：int的值获取Enum对象
	 * 
	 * @param index
	 * @return ConfigStatus Enum对象
	 */
	@com.fasterxml.jackson.annotation.JsonCreator
	public static ExecutorSetting valueOf(Integer index) {
		for (ExecutorSetting type : values()) {
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
		for (ExecutorSetting c : values()) {
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
	public static java.util.Map<Integer, String> getList(List<ExecutorSetting> exceptEnums) {
		java.util.Map<Integer, String> result = new java.util.HashMap<Integer, String>();
		for (ExecutorSetting c : values()) {
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
	@JsonValue
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
}
