package kc.enums.codegenerate;

import kc.framework.enums.EnumBase;

import java.util.List;

public enum ApiMethodType implements EnumBase {

	/**
	 * 新增接口
	 */
	Create("新增接口", 0),

	/**
	 * 删除接口
	 */
	Remove("删除接口", 1),

	/**
	 * 修改接口
	 */
	Update("修改接口", 2),

	/**
	 * 删除接口
	 */
	Delete("删除接口", 3),

	/**
	 * 查询接口
	 */
	Search("查询接口", 4),

	/**
	 * 导出接口
	 */
	Export("导出接口", 5),

	/**
	 * 导入接口
	 */
	Import("导入接口", 6),

	/**
	 * 其他
	 */
	Other("其他", 99);

	// 定义一个 private 修饰的实例变量
	private String desc;
	private Integer index;

	// 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
	private ApiMethodType(String desc, Integer index) {
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
	public static ApiMethodType valueOf(Integer index) {
		for (ApiMethodType type : values()) {
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
		for (ApiMethodType c : values()) {
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
	public static java.util.Map<Integer, String> getList(List<ApiMethodType> exceptEnums) {
		java.util.Map<Integer, String> result = new java.util.HashMap<Integer, String>();
		for (ApiMethodType c : values()) {
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
