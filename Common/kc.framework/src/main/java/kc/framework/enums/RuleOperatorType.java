package kc.framework.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

public enum RuleOperatorType implements EnumBase {
	/**
	 * 等于
	 */
	Equal("等于", 0),

	/**
	 * 不等于
	 */
	NotEqual("不等于", 1),

	/**
	 * 包含
	 */
	Contains("包含", 2),
	/**
	 * 大于
	 */
	Greater("大于", 10),

	/**
	 * 小于
	 */
	Less("小于", 11),

	/**
	 * 大于等于
	 */
	GreaterThanAndEqual("大于等于", 12),

	/**
	 * 小于等于
	 */
	LessThanAndEqual("小于等于", 13);

	// 定义一个 private 修饰的实例变量
	private String desc;
	private Integer index;

	// 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
	private RuleOperatorType(String desc, Integer index) {
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
	public static RuleOperatorType valueOf(Integer index) {
		for (RuleOperatorType type : values()) {
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
		for (RuleOperatorType c : values()) {
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
	public static java.util.Map<Integer, String> getList(List<RuleOperatorType> exceptEnums) {
		java.util.Map<Integer, String> result = new java.util.HashMap<Integer, String>();
		for (RuleOperatorType c : values()) {
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
