package kc.enums;

import kc.framework.enums.EnumBase;

import java.util.HashMap;
import java.util.Map;

public enum NewsBulletinType implements EnumBase {

	/**
	 * 内部通知
	 */
	Internal("内部通知", 0),

	/**
	 * 外部公告
	 */
	External("外部公告", 1),

	/**
	 * 新闻
	 */
	News("新闻", 2),

	/**
	 * 帮助
	 */
	Help("帮助", 3),

	/**
	 * 活动
	 */
	Activity("活动", 4);

	// 定义一个 private 修饰的实例变量
	private String desc;
	private int index;
	private static Map<Integer, NewsBulletinType> map = new HashMap<Integer, NewsBulletinType>();
	static {
		for (NewsBulletinType type : NewsBulletinType.values()) {
			map.put(type.getIndex(), type);
		}
	}

	public static NewsBulletinType valueOf(int index) {
		return map.get(index);
	}

	// 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
	private NewsBulletinType(String desc, int index) {
		this.desc = desc;
		this.index = index;
	}

	// 普通方法
	public static String getDesc(int index) {
		for (NewsBulletinType c : NewsBulletinType.values()) {
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

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
}
