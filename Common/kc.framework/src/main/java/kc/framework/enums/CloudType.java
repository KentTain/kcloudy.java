package kc.framework.enums;

import java.util.List;

public enum CloudType implements EnumBase{

	/**
	 * 本地系统
	 */
	FileSystem("本地系统", 0),

	/**
	 * 微软云
	 */
	Azure("微软云", 1),

	/**
	 * 腾讯云
	 */
	TencentCloud("腾讯云", 2),

	/**
	 * 阿里云
	 */
	Ali("阿里云", 3),

	/**
	 * 亚马逊云
	 */
	AWS(" 亚马逊云", 4),

	/**
	 * 华为云
	 */
	HuaweiCloud(" 华为云", 5);
	
	// 定义一个 private 修饰的实例变量
	private String desc;
	private Integer index;
	
	// 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
	private CloudType(String desc, Integer index) {
		this.desc = desc;
		this.index = index;
	}

	/**
	 * 根据索引值：Integer的值获取Enum对象
	 * 
	 * @param index 索引值
	 * @return ConfigType Enum对象
	 */
	@com.fasterxml.jackson.annotation.JsonCreator
	public static CloudType valueOf(Integer index) {
		for (CloudType type : values()) {
			if (type.getIndex() == index) {
				return type;
			}
		}
		return null;
	}

	/**
	 * 根据Index的值，获取Enum类型的描述
	 * 
	 * @param index 索引值
	 * @return String Enum类型的描述
	 */
	public static String getDesc(Integer index) {
		for (CloudType c : values()) {
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
	public static java.util.Map<Integer, String> getList(List<CloudType> exceptEnums) {
		java.util.Map<Integer, String> result = new java.util.HashMap<Integer, String>();
		for (CloudType c : values()) {
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
