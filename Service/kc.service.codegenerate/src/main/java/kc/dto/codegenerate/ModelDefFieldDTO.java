package kc.dto.codegenerate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import kc.dto.PropertyAttributeBaseDTO;
import kc.enums.codegenerate.ModelBaseType;
import kc.enums.codegenerate.PrimaryKeyType;
import kc.service.serializer.NumericBooleanDeserializer;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ModelDefFieldDTO extends PropertyAttributeBaseDTO {

    @com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
    private Boolean isEditMode = false;
    /**
     * 显示名
     */
    @NotNull
    @Length(min = 1, max = 200, message = "显示名不能超过200个字符")
    private String displayName;
    /**
     * 描述
     */
    @Length(min = 0, max = 4000, message = "描述不能超过4000个字符")
    private String description;
    /**
     * 是否为主键字段
     */
    @JsonDeserialize(using = NumericBooleanDeserializer.class)
    @com.fasterxml.jackson.annotation.JsonProperty("isPrimaryKey")
    private Boolean isPrimaryKey = false;
    /**
     * 主键类型
     */
    private PrimaryKeyType primaryKeyType;
    private String primaryKeyTypeString;
    public String getPrimaryKeyTypeString() {
        if (primaryKeyType == null)
            return null;
        return primaryKeyType.getDesc();
    }

    /**
     * 是否必填
     */
    @JsonDeserialize(using = NumericBooleanDeserializer.class)
    @com.fasterxml.jackson.annotation.JsonProperty("isNotNull")
    private Boolean isNotNull = false;
    /**
     * 是否唯一
     */
    @JsonDeserialize(using = NumericBooleanDeserializer.class)
    @com.fasterxml.jackson.annotation.JsonProperty("isUnique")
    private Boolean isUnique = false;
    /**
     * 是否为执行人字段
     */
    @JsonDeserialize(using = NumericBooleanDeserializer.class)
    @com.fasterxml.jackson.annotation.JsonProperty("isExecutor")
    private Boolean isExecutor = false;

    /**
     * 是否为条件判断字段
     */
    @JsonDeserialize(using = NumericBooleanDeserializer.class)
    @com.fasterxml.jackson.annotation.JsonProperty("isCondition")
    private Boolean isCondition = false;

    /**
     * 关联对象id
     */
    private String relateObjectId;

    /**
     * 关联对象表名
     */
    private String relateObject;

    private int modelDefId;

    private String modelDefName;

}
