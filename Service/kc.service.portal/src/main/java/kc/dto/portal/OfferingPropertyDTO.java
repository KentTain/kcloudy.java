package kc.dto.portal;

import kc.dto.EntityDTO;
import kc.enums.portal.OfferingPropertyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy= InheritanceType.JOINED)
public class OfferingPropertyDTO extends EntityDTO implements java.io.Serializable{

    @com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
    private boolean isEditMode;

    private int id;

    /**
     * 类型：kc.enums.offering.OfferingPropertyType
     */
    private OfferingPropertyType offeringPropertyType = OfferingPropertyType.Detail;
    private String offeringPropertyTypeString;
    public String getOfferingPropertyTypeString() {
        if (offeringPropertyType == null)
            return OfferingPropertyType.Detail.getDesc();
        return offeringPropertyType.getDesc();
    }

    /**
     * 名称
     */
    private String name;

    /**
     * 属性值
     */
    private String value;

    /**
     * 属性值2
     */
    private String value1;

    /**
     * 属性值3
     */
    private String value2;

    /**
     * 是否能编辑
     */
    @com.fasterxml.jackson.annotation.JsonProperty("canEdit")
    private boolean canEdit;

    /**
     * 是否为必填
     */
    @com.fasterxml.jackson.annotation.JsonProperty("isRequire")
    private boolean isRequire;

    private int index;

    //private OfferingDTO offering;

    private int offeringId;
    private String offeringName;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        OfferingPropertyDTO node = (OfferingPropertyDTO) o;

        if (id != node.id)
            return false;
        return Objects.equals(name, node.name);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
