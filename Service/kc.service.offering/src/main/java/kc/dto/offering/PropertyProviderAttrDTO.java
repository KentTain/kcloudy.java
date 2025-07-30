package kc.dto.offering;

import kc.dto.EntityBaseDTO;
import kc.enums.offering.ServiceAttrDataType;
import kc.model.offering.Tables;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy= InheritanceType.JOINED)
public class PropertyProviderAttrDTO extends EntityBaseDTO implements java.io.Serializable{

    private int id;

    /**
     * 属性数据类型: kc.enums.offering.ServiceAttrDataType
     */
    private ServiceAttrDataType serviceAttrDataType = ServiceAttrDataType.Text;
    private String serviceAttrDataTypeString;
    public String getServiceAttrDataTypeString() {
        if (serviceAttrDataType == null)
            return ServiceAttrDataType.Text.getDesc();
        return serviceAttrDataType.getDesc();
    }

    /**
     * 属性名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

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

    @com.fasterxml.jackson.annotation.JsonProperty("canEdit")
    private boolean canEdit;

    private int index;

    private int serviceProviderId;

    private String serviceProviderName;

    //private PropertyProviderDTO serviceProvider;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        PropertyProviderAttrDTO node = (PropertyProviderAttrDTO) o;

        if (id != node.id)
            return false;
        if (!Objects.equals(name, node.name))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
