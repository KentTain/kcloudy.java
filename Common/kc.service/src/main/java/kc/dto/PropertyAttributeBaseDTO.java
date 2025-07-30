package kc.dto;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import kc.framework.enums.AttributeDataType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public abstract class PropertyAttributeBaseDTO extends EntityDTO implements java.io.Serializable {

    private int propertyAttributeId;
    /**
     * 属性类型：kc.framework.enums.AttributeDataType
     * 0：字符串；1：布尔型；2：整型；3：数值型；4：金额；5：日期型；6：文本型；7：列表
     */
    private AttributeDataType dataType;
    private String dataTypeString;
    public String getDataTypeString() {
        if (dataType == null)
            return null;
        return dataType.getDesc();
    }

    /**
     * 属性名称
     */
    @NotNull
    @Length(min = 1, max = 256, message = "属性名称不能超过256个字符")
    private String name;
    /**
     * 显示名称：
     */
    @Length(min = 0, max = 512, message = "属性名称不能超过512个字符")
    private String displayName;
    /**
     * 属性描述
     */
    @Length(min = 0, max = 4000, message = "描述不能超过4000个字符")
    private String description;

    /**
     * 属性值
     */
    private String value;
    /**
     * 属性扩展值1
     */
    @Length(min = 0, max = 4000, message = "属性扩展值1不能超过4000个字符")
    private String ext1;
    /**
     * 属性扩展值2
     */
    @Length(min = 0, max = 4000, message = "属性扩展值2不能超过4000个字符")
    private String ext2;
    /**
     * 属性扩展值3
     */
    @Length(min = 0, max = 4000, message = "属性扩展值3不能超过4000个字符")
    private String ext3;

    private Boolean canEdit = false;

    @com.fasterxml.jackson.annotation.JsonProperty("isRequire")
    private Boolean isRequire = false;

    private int index;
}
