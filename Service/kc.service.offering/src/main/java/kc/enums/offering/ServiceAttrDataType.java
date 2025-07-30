package kc.enums.offering;

import kc.framework.enums.EnumBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tianc 数据类型
 */
public enum ServiceAttrDataType implements EnumBase{

	/**
	 * 字符串
	 */
	String("字符串", 0),

	/**
	 * 布尔型
	 */
	Bool("布尔型", 1),

	/**
	 * 整型
	 */
	Integer("整型", 2),

	/**
	 * 数值型
	 */
	Decimal("数值型", 3),

	/**
	 * 金额
	 */
	Currancy("金额", 4),

	/**
	 * 日期型
	 */
	Datetime("日期型", 5),

	/**
	 * 文本型
	 */
	Text("文本型", 6),

	/**
	 * 其他
	 */
	Other("其他", 99);

	// 定义一个 private 修饰的实例变量
	private String desc;
	private Integer index;

	// 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
	private ServiceAttrDataType(String desc, Integer index) {
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
	public static ServiceAttrDataType valueOf(Integer index) {
		for (ServiceAttrDataType type : values()) {
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
		for (ServiceAttrDataType c : values()) {
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
	public static Map<Integer, String> getList(List<ServiceAttrDataType> exceptEnums) {
		Map<Integer, String> result = new HashMap<Integer, String>();
		for (ServiceAttrDataType c : values()) {
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
