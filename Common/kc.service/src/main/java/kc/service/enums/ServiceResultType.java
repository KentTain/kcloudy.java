package kc.service.enums;

import java.util.HashMap;
import java.util.Map;

public enum ServiceResultType {

	/**
	 * 操作成功
	 */
	Success("操作成功", 0),

	/**
	 * 操作取消或操作没引发任何变化
	 */
	NoChanged("操作取消或操作没引发任何变化", 1),

	/**
	 * 参数错误
	 */
	ParamError("参数错误", 2),

	/**
	 * 指定参数的数据不存在
	 */
	QueryNull("指定参数的数据不存在", 3),

	/**
	 * 权限不足
	 */
	PurviewLack("权限不足", 4),

	/**
	 * 非法操作
	 */
	IllegalOperation("非法操作", 5),

	/**
	 * 警告
	 */
	Warning("警告", 6),

	/**
	 * 操作引发错误
	 */
	Error("操作引发错误", 7);

	// 定义一个 private 修饰的实例变量
	private String desc;
	private int index;
	private static Map<Integer, ServiceResultType> map = new HashMap<Integer, ServiceResultType>();
	static {
		for (ServiceResultType type : ServiceResultType.values()) {
			map.put(type.getIndex(), type);
		}
	}

	public static ServiceResultType valueOf(int index) {
		return map.get(index);
	}

	// 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
	private ServiceResultType(String desc, int index) {
		this.desc = desc;
		this.index = index;
	}

	// 普通方法
	public static String getDesc(int index) {
		for (ServiceResultType c : ServiceResultType.values()) {
			if (c.getIndex() == index) {
				return c.getDesc();
			}
		}
		return null;
	}

	// 定义 get set 方法
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
