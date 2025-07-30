package kc.framework.enums;

import java.util.List;

/**
 * 商品类型
 */
public enum PropertyDataType implements EnumBase {
    /**
     * 文本框列表
     */
    TextList("文本框列表", 0),

    /**
     * 单选列表
     */
    RadioList("单选列表", 1),

    /**
     * 下拉列表
     */
    DropdownList("下拉列表", 2),

    /**
     * 复选框
     */
    CheckBoxList("复选框", 3),

    /**
     * 最大最小值范围
     */
    Range("最大最小值范围", 4),

    /**
     * 图片上传
     */
    ImageUpload("图片上传", 5),

    /**
     * 文件上传
     */
    FileUpload("文件上传", 6),

    /**
     * 文本编辑
     */
    TextArea("文本编辑", 7),

    /**
     * 区域范围
     */
    CityList("区域范围", 8),

    /**
     * 服务提供商
     */
    ServiceProviderList("服务提供商", 9),

    /**
     * 商品报价区间
     */
    PriceRange("商品报价区间", 10),

    /**
     * 行业筛选
     */
    IndustryClassfication("行业筛选", 11),

    /**
     * 付款方式
     */
    PaymentInfo("付款方式", 12);

    // 定义一个 private 修饰的实例变量
    private String desc;
    private Integer index;

    // 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
    private PropertyDataType(String desc, Integer index) {
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
    public static PropertyDataType valueOf(Integer index) {
        for (PropertyDataType type : values()) {
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
        for (PropertyDataType c : values()) {
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
    public static java.util.Map<Integer, String> getList(List<PropertyDataType> exceptEnums) {
        java.util.Map<Integer, String> result = new java.util.HashMap<Integer, String>();
        for (PropertyDataType c : values()) {
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
