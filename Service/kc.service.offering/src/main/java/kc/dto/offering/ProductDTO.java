package kc.dto.offering;

import com.fasterxml.jackson.annotation.JsonProperty;
import kc.dto.BlobInfoDTO;
import kc.dto.EntityDTO;
import kc.enums.offering.ProductPropertyType;
import kc.framework.util.SerializeHelper;
import kc.framework.extension.StringExtensions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class ProductDTO extends EntityDTO implements java.io.Serializable {

    @com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
    private boolean isEditMode;

    private int productId;

    /**
     * 产品编码
     */
    @Length(min = 0, max = 128, message = "属性名称不能超过128个字符")
    private String productCode;

    /**
     * 产品名称
     */
    @Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
    private String productName;

    /**
     * 是否生效
     */
    @com.fasterxml.jackson.annotation.JsonProperty("isEnabled")
    private boolean isEnabled = true;
    /**
     * 产品图片对象Json字符串
     */
    @Length(min = 0, max = 1000, message = "属性名称不能超过1000个字符")
    private String productImage;
    private BlobInfoDTO productImageBlob;
    public BlobInfoDTO getProductImageBlob() {
        if (StringExtensions.isNullOrEmpty(productImage))
            return null;
        return SerializeHelper.FromJson(productImage, BlobInfoDTO.class);
    }

    /**
     * 数据来自于productProperties，类型为图片的产品属性
     */
    private List<BlobInfoDTO> productImageBlobs = new ArrayList<>();
    public List<BlobInfoDTO> getProductImageBlobs() {
        productImageBlobs = new ArrayList<BlobInfoDTO>();
        if (productProperties == null || productProperties.size() <= 0)
            return productImageBlobs;

        List<ProductPropertyDTO> blobProperties = productProperties.stream()
                .filter(m -> m.getProductPropertyType() == ProductPropertyType.Image)
                .sorted(Comparator.comparingInt(ProductPropertyDTO::getIndex))
                .collect(Collectors.toList());
        for (ProductPropertyDTO blobProperty : blobProperties) {
            BlobInfoDTO blob = SerializeHelper.FromJson(blobProperty.getValue(), BlobInfoDTO.class);
            if (blob != null) {
                //第一次保存时（新创建的），自增列：属性Id为序列化成Json字符串中，故反序列化需要将属性Id进行重新赋值
                blob.setPropertyId(blobProperty.getId());
                productImageBlobs.add(blob);
            }
        }
        return productImageBlobs;
    }

    /**
     * 产品图片对象Json字符串
     */
    @Length(min = 0, max = 1000, message = "属性名称不能超过1000个字符")
    private String productFile;
    private BlobInfoDTO productFileBlob;
    public BlobInfoDTO getProductFileBlob() {
        if (StringExtensions.isNullOrEmpty(productFile))
            return null;
        return SerializeHelper.FromJson(productFile, BlobInfoDTO.class);
    }

    /**
     * 产品单价
     */
    private java.math.BigDecimal productPrice;

    /**
     * 产品折扣
     */
    private java.math.BigDecimal productDiscount;
    /**
     * 产品税率
     */
    private java.math.BigDecimal productRate;

    /**
     * 产品二维码BlobId
     */
    private String barcode;

    /**
     * 排序
     */
    private int index;

    private int offeringId;

    private String offeringName;

    //private Offering offering;

    /**
     * 产品规格属性
     */
    private List<ProductPropertyDTO> specificationProperties = new ArrayList<>();
    public List<ProductPropertyDTO> getSpecificationProperties() {
        if (productProperties == null || productProperties.size() <= 0)
            return specificationProperties;

        List<ProductPropertyDTO> blobProperties = productProperties.stream()
                .filter(m -> m.getProductPropertyType() == ProductPropertyType.Specification)
                .sorted(Comparator.comparingInt(ProductPropertyDTO::getIndex))
                .collect(Collectors.toList());
        specificationProperties.addAll(blobProperties);
        return specificationProperties;
    }

    /**
     * 其值转化为以下对象：specificationProperties（类型：Specification)、productImageBlobs（类型：Image），提供给前端使用
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ProductPropertyDTO> productProperties = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        ProductDTO node = (ProductDTO) o;

        if (offeringId != node.offeringId)
            return false;

        if (productId != node.productId)
            return false;

        if (!Objects.equals(productName, node.productName))
            return false;

        return Objects.equals(productCode, node.productCode);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + productId;
        result = 31 * result + (productCode != null ? productCode.hashCode() : 0);
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        return result;
    }
}
