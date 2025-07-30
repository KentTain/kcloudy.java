package kc.enums.codegenerate;

import kc.framework.enums.EnumBase;

import java.util.List;

public enum ModelBaseType implements EnumBase {
	/**
	 * 基础类型
	 */
	EntityBase("基础类型", 0),

	/**
	 * 实体类型
	 */
	Entity("实体类型", 1),

	/**
	 * 树型结构
	 */
	TreeNode("树型结构", 2),

	/**
	 * 属性设置
	 */
	Property("属性设置", 3),

	/**
	 * 属性值设置
	 */
	PropertyAttribute("属性值设置", 4);

	// 定义一个 private 修饰的实例变量
	private String desc;
	private Integer index;

	// 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
	private ModelBaseType(String desc, Integer index) {
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
	public static ModelBaseType valueOf(Integer index) {
		for (ModelBaseType type : values()) {
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
		for (ModelBaseType c : values()) {
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
	public static java.util.Map<Integer, String> getList(List<ModelBaseType> exceptEnums) {
		java.util.Map<Integer, String> result = new java.util.HashMap<Integer, String>();
		for (ModelBaseType c : values()) {
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
