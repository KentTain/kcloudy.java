package kc.dto.offering;

import kc.dto.BlobInfoDTO;
import kc.dto.TreeNodeDTO;
import kc.enums.offering.OfferingPriceType;
import kc.enums.offering.OfferingPropertyType;
import kc.framework.util.SerializeHelper;
import kc.framework.extension.StringExtensions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy= InheritanceType.JOINED)
public class CategoryDTO extends TreeNodeDTO<CategoryDTO> implements java.io.Serializable{

    @com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
    private boolean isEditMode;

    private int parentId;

    private String parentName;

    /**
     * 商品首页图片对象Json字符串
     */
    @Length(min = 0, max = 100, message = "属性名称不能超过1000个字符")
    private String categoryImage;

    /**
     * 商品首页图片Id
     */
    private BlobInfoDTO categoryImageBlob;
    public BlobInfoDTO getCategoryImageBlob() {
        if (StringExtensions.isNullOrEmpty(categoryImage))
            return null;
        return SerializeHelper.FromJson(categoryImage, BlobInfoDTO.class);
    }

    /**
     * 商品文档对象Json字符串
     */
    @Length(min = 0, max = 1000, message = "属性名称不能超过1000个字符")
    private String categoryFile;

    /**
     * 商品文档对象Json字符串
     */
    private BlobInfoDTO categoryFileBlob;
    public BlobInfoDTO getCategoryFileBlob() {
        if (StringExtensions.isNullOrEmpty(categoryFile))
            return null;
        return SerializeHelper.FromJson(categoryFile, BlobInfoDTO.class);
    }

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
    @Length(min = 0, max = 4000, message = "属性名称不能超过4000个字符")
    private String description;

    /**
     * 是否显示
     */
    @com.fasterxml.jackson.annotation.JsonProperty("isShow")
    private boolean isShow;

    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<OfferingDTO> offerings = new ArrayList<>();

    private List<CategoryManagerDTO> categoryManagers = new ArrayList<CategoryManagerDTO>();

    private List<PropertyProviderDTO> propertyProviders = new ArrayList<PropertyProviderDTO>();


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        CategoryDTO node = (CategoryDTO) o;

        if (parentId != node.parentId)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + parentId;
        result = 31 * result + (categoryImage != null ? categoryImage.hashCode() : 0);
        return result;
    }
}
