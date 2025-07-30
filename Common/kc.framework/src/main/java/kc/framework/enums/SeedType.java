package kc.framework.enums;

import java.util.List;

public enum SeedType implements EnumBase{

	/**
	 * 组织ID
	 */
	Organization("组织ID", 1),

	/**
	 * 用户ID
	 */
	Member("用户ID", 2),

	/**
	 * 商品ID
	 */
	Offering("商品ID", 3),

	/**
	 * 订单ID
	 */
	OrderOffering("订单ID", 4),

	/**
	 * 采购订单ID
	 */
	PurchaseOrder("采购订单ID", 5),

	/**
	 * 销售订单ID
	 */
	SalesOrder("销售订单ID", 6),

	/**
	 * 发货单ID
	 */
	DeliveryNote("发货单ID", 7),

	/**
	 * 应收发票单ID
	 */
	ARInvoice("应收发票单ID", 8),

	/**
	 * 入库单ID
	 */
	GoodsReceipt("入库单ID", 9),

	/**
	 * 应付发票ID
	 */
	APInvoice("应付发票ID", 10),

	/**
	 * 融资申请编号
	 */
	FinancingDemand("融资申请编号", 11),

	/**
	 * 投资类标准商品
	 */
	Investment("投资类标准商品", 12),

	/**
	 * 融资类标准商品
	 */
	Financing("融资类标准商品", 13),

	/**
	 * 销售类标准商品
	 */
	Supplying("销售类标准商品", 14),

	/**
	 * 采购类标准商品
	 */
	Purchasing("采购类标准商品", 15),

	/**
	 * 商品模板编号
	 */
	OfferingProviderService("商品模板编号", 16),

	/**
	 * 采购需求
	 */
	OrderPurchase("采购需求", 17),

	/**
	 * 询价单
	 */
	OrderInquire("询价单", 18),

	/**
	 * 企业信息服务
	 */
	SoftService("企业信息服务", 19),

	/**
	 * 消息模板
	 */
	MessageTemplate("消息模板", 20),

	/**
	 * 客户ID
	 */
	Customer(" 客户ID", 21),

	/**
	 * 财务报表
	 */
	FinacingStatement("财务报表", 22),

	/**
	 * 调查
	 */
	InvestigationInfo("调查", 23),

	/**
	 * 额度使用记录
	 */
	CreditUsage("额度使用记录", 24),
	/**
	 * 配置
	 */
	Config("配置", 25),
	/**
	 * 业务类型
	 */
	BusinessType("业务类型", 26);

	// 定义一个 private 修饰的实例变量
	private String desc;
	private Integer index;

	// 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
	private SeedType(String desc, Integer index) {
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
	public static SeedType valueOf(Integer index) {
		for (SeedType type : values()) {
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
		for (SeedType c : values()) {
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
	public static java.util.Map<Integer, String> getList(List<SeedType> exceptEnums) {
		java.util.Map<Integer, String> result = new java.util.HashMap<Integer, String>();
		for (SeedType c : values()) {
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
