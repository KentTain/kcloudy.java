package kc.enums.portal;

import kc.framework.enums.EnumBase;

import java.util.List;

/**
 * 商品属性类型
 */
public enum OfferingPropertyType implements EnumBase {
    /**
     * 商品详情
     */
    Detail("商品详情", 1),

    /**
     * 商品图片
     */
    Image("商品图片", 2),

    /**
     * 商品文件
     */
    File("商品文件", 4),

    /**
     * 商品视频
     */
    Video("商品视频", 8),

    /**
     * 商品音频
     */
    Audio("商品音频", 16),

    /**
     * 服务区域
     */
    Area("服务区域", 32),

    /**
     * 服务提供商
     */
    ServiceProvider("服务提供商", 64),

    /**
     * 品牌
     */
    Brand("品牌", 128),

    /**
     * 付款方式
     */
    PaymentInfo("付款方式", 256);

    // 定义一个 private 修饰的实例变量
    private String desc;
    private Integer index;

    // 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
    private OfferingPropertyType(String desc, Integer index) {
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
    public static OfferingPropertyType valueOf(Integer index) {
        for (OfferingPropertyType type : values()) {
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
        for (OfferingPropertyType c : values()) {
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
    public static java.util.Map<Integer, String> getList(List<OfferingPropertyType> exceptEnums) {
        java.util.Map<Integer, String> result = new java.util.HashMap<Integer, String>();
        for (OfferingPropertyType c : values()) {
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
