package kc.framework.enums;


import java.util.List;

/**
 * 业务类型
 */
public enum BusinessType implements EnumBase {

	/**
	 * 未知
	 */
	None("未知", 0),

	/**
	 * 信息化
	 */
	IT("信息化", 1),

	/**
	 * 人力资源
	 */
	HR("人力资源", 2),

	/**
	 * 销售
	 */
	Sale("销售", 4),

	/**
	 * 采购
	 */
	Purchase("采购", 8),

	/**
	 * 仓储
	 */
	WareHouse("仓储", 16),

	/**
	 * 生产
	 */
	Manufacturing("生产", 32),

	/**
	 * 质检
	 */
	QA("质检", 64),

	/**
	 * 市场
	 */
	Marketing("市场", 128),

	/**
	 * 财务
	 */
	Accounting("财务", 256);

	// 定义一个 private 修饰的实例变量
	private String desc;
	private Integer index;

	// 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
	private BusinessType(String desc, Integer index) {
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
	public static BusinessType valueOf(Integer index) {
		for (BusinessType type : values()) {
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
		for (BusinessType c : values()) {
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
	public static java.util.Map<Integer, String> getList(List<BusinessType> exceptEnums) {
		java.util.Map<Integer, String> result = new java.util.HashMap<Integer, String>();
		for (BusinessType c : values()) {
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
