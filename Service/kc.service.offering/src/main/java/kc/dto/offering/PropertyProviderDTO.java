package kc.dto.offering;

import kc.dto.EntityBaseDTO;
import kc.dto.EntityDTO;
import kc.enums.offering.ServiceDataType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy= InheritanceType.JOINED)
public class PropertyProviderDTO extends EntityBaseDTO implements java.io.Serializable{

    @com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
    private boolean isEditMode;

    private int id;

    /**
     * 类型：kc.enums.offering.ServiceDataType
     */
    private ServiceDataType serviceDataType = ServiceDataType.TextList;
    private String serviceDataTypeString;
    public String getServiceDataTypeString() {
        if (serviceDataType == null)
            return ServiceDataType.TextList.getDesc();
        return serviceDataType.getDesc();
    }

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

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

    private CategoryDTO category;

    private List<PropertyProviderAttrDTO> serviceProviderAttrs = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        PropertyProviderDTO node = (PropertyProviderDTO) o;

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
