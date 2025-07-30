package kc.framework.enums;

import java.util.ArrayList;
import java.util.List;

public enum TenantVersion implements EnumBase{
	/**
	 * 通用版
	 */
	Standard("通用版", 0x01),
	/**
	 * 专业版
	 */
	Professional("专业版", 0x02),
	/**
	 * 集团版
	 */
	Group("集团版", 0x04),
	/**
	 * 定制版
	 */
	Customized("定制版", 0x08);

	// 定义一个 private 修饰的实例变量
	private String desc;
	private Integer index;

	// 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
	private TenantVersion(String desc, Integer index) {
		this.desc = desc;
		this.index = index;
	}

	/**
	 * 根据索引值：int的值获取Enum对象
	 *
	 * @param index 索引值
	 * @return ConfigType Enum对象
	 */
	@com.fasterxml.jackson.annotation.JsonCreator
	public static TenantVersion valueOf(Integer index) {
		for (TenantVersion type : values()) {
			if (type.getIndex() == index) {
				return type;
			}
		}
		return null;
	}


	/**
	 * 获取Enum所有类型的Map<索引值, Enum类型的描述>
	 *
	 * @param index 需要排除的Enum类型列表
	 * @return Map<Integer, String>
	 */
	public static String getDesc(Integer index) {
		for (TenantVersion c : values()) {
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
	public static java.util.Map<Integer, String> getList(List<TenantVersion> exceptEnums) {
		java.util.Map<Integer, String> result = new java.util.HashMap<Integer, String>();
		for (TenantVersion c : values()) {
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
