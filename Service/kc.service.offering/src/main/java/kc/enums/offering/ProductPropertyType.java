package kc.enums.offering;

import kc.framework.enums.EnumBase;

import java.util.List;

/**
 * 产品属性类型
 */
public enum ProductPropertyType implements EnumBase {
    /**
     * 产品规格
     */
    Specification("产品规格", 1),

    /**
     * 产品图片
     */
    Image("产品图片", 2),

    /**
     * 产品文件
     */
    File("产品文件", 4),

    /**
     * 产品视频
     */
    Video("产品视频", 8),

    /**
     * 产品音频
     */
    Audio("产品音频", 16),

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
    private ProductPropertyType(String desc, Integer index) {
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
    public static ProductPropertyType valueOf(Integer index) {
        for (ProductPropertyType type : values()) {
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
        for (ProductPropertyType c : values()) {
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
    public static java.util.Map<Integer, String> getList(List<ProductPropertyType> exceptEnums) {
        java.util.Map<Integer, String> result = new java.util.HashMap<Integer, String>();
        for (ProductPropertyType c : values()) {
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
