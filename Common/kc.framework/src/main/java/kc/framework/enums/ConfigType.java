package kc.framework.enums;

import java.util.List;

/**
 * @author tianc 配置类型
 */
public enum ConfigType implements EnumBase{
	/**
	 * 默认无
	 */
	Default("无", 0),
	/**
	 * 电子邮件
	 */
	EmailConfig("电子邮件", 1),
	/**
	 * 短信平台
	 */
	SmsConfig("短信平台", 2),
	/**
	 * 支付方式
	 */
	PaymentMethod("支付方式", 3),
	/**
	 * 身份认证
	 */
	ID5("身份认证", 4),
	/**
	 * 呼叫平台
	 */
	CallConfig("呼叫平台", 5),
	/**
	 * 物流平台
	 */
	LogisticsPlatform("物流平台", 6),
	/**
	 * 微信公众号
	 */
	WeixinConfig("微信公众号", 7),
	/**
	 * 电子签章
	 */
	ContractConfig("电子签章", 8),
	/**
	 * 独立域名
	 */
	OwnDomain("独立域名", 9),
	/**
	 * 未知
	 */
	UNKNOWN("未知", 99);

	// 定义一个 private 修饰的实例变量
	private String desc;
	private Integer index;

	// 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
	private ConfigType(String desc, Integer index) {
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
	public static ConfigType valueOf(Integer index) {
		for (ConfigType type : values()) {
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
		for (ConfigType c : values()) {
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
	public static java.util.Map<Integer, String> getList(List<ConfigType> exceptEnums) {
		java.util.Map<Integer, String> result = new java.util.HashMap<Integer, String>();
		for (ConfigType c : values()) {
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
