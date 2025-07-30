package kc.dto.portal;

import kc.dto.TreeNodeDTO;
import kc.enums.portal.OfferingPriceType;
import kc.enums.portal.OfferingPropertyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy= InheritanceType.JOINED)
public class OfferingCategoryDTO extends TreeNodeDTO<OfferingCategoryDTO> implements java.io.Serializable{

    @com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
    private boolean isEditMode;

    private int parentId;

    private String parentName;

    /**
     * 商品首页图片对象Json字符串
     */
    private String imageBlob;
    /**
     * 商品首页图片Id
     */
    private String imageBlobId;

    /**
     * 商品文档对象Json字符串
     */
    private String fileBlob;
    /**
     * 商品文档对象Json字符串
     */
    private String fileBlobId;

    /**
     * 商品价格定义：kc.enums.OfferingPriceType
     */
    private OfferingPriceType offeringPriceType = OfferingPriceType.NegotiablePrice;
    private String offeringPriceTypeString;
    public String getOfferingTypeString() {
        if (offeringPriceType == null)
            return OfferingPriceType.NegotiablePrice.getDesc();
        return offeringPriceType.getDesc();
    }

    /**
     * 商品属性定义：kc.enums.OfferingVersion
     */
    private int offeringPropertyType = OfferingPropertyType.Detail.getIndex();
    private EnumSet<OfferingPropertyType> offeringPropertyTypes = EnumSet.of(OfferingPropertyType.Detail);
    private String offeringPropertyTypeString;
    public String getOfferingVersionString() {
        if (offeringPropertyTypes == null)
            return OfferingPropertyType.Detail.getDesc();

        StringBuilder sbResult = new StringBuilder();
        for (OfferingPropertyType version : offeringPropertyTypes) {
            sbResult.append(version.getDesc()).append(",");
        }

        if (sbResult.length() > 0)
            sbResult.deleteCharAt(sbResult.length() - 1);

        return sbResult.toString();
    }

    /**
     * 描述(显示时，填充html description)
     */
    private String description;

    /**
     * 是否显示
     */
    @com.fasterxml.jackson.annotation.JsonProperty("isShow")
    private boolean isShow;

    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<OfferingDTO> offerings = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        OfferingCategoryDTO node = (OfferingCategoryDTO) o;

        if (parentId != node.parentId)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + parentId;
        result = 31 * result + (imageBlobId != null ? imageBlobId.hashCode() : 0);
        return result;
    }
}
