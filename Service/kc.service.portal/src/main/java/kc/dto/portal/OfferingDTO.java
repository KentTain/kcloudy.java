package kc.dto.portal;

import com.fasterxml.jackson.annotation.JsonProperty;
import kc.dto.BlobInfoDTO;
import kc.dto.EntityDTO;
import kc.enums.portal.OfferingPropertyType;
import kc.enums.portal.OfferingStatus;
import kc.enums.portal.OfferingType;
import kc.framework.util.SerializeHelper;
import kc.framework.extension.StringExtensions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class OfferingDTO extends EntityDTO implements java.io.Serializable {

    @JsonProperty("isEditMode")
    private boolean isEditMode;

    private int offeringId;
    /**
     * 商品编码
     */
    private String offeringCode;

    /**
     * 商品名称
     */
    private String offeringName;

    /**
     * 是否生效
     */
    @com.fasterxml.jackson.annotation.JsonAlias({"enabled"})
    @JsonProperty("isEnabled")
    private boolean isEnabled = true;

    /**
     * 默认的商品图片对象
     */
    private BlobInfoDTO offeringImageBlob;

    /**
     * 商品图片对象列表
     */
    private List<BlobInfoDTO> offeringImageBlobs;
    public List<BlobInfoDTO> getOfferingImageBlobs() {
        offeringImageBlobs = new ArrayList<BlobInfoDTO>();
        if (offeringProperties == null || offeringProperties.size() <= 0)
            return offeringImageBlobs;

        List<OfferingPropertyDTO> blobProperties = offeringProperties.stream()
                .filter(m -> m.getOfferingPropertyType() == OfferingPropertyType.Image)
                .sorted(Comparator.comparing(OfferingPropertyDTO::getIndex))
                .collect(Collectors.toList());
        for (OfferingPropertyDTO blobProperty : blobProperties) {
            BlobInfoDTO blob = SerializeHelper.FromJson(blobProperty.getValue(), BlobInfoDTO.class);
            if (blob != null) {
                //第一次保存时（新创建的），自增列：属性Id为序列化成Json字符串中，故反序列化需要将属性Id进行重新赋值
                blob.setPropertyId(blobProperty.getId());
                offeringImageBlobs.add(blob);
            }
        }
        return offeringImageBlobs;
    }

    /**
     * 商品图片查看路径
     */
    private String showImageUrl;
    public String getShowImageUrl() {
        if (offeringImageBlob == null || StringExtensions.isNullOrEmpty(offeringImageBlob.getBlobId()))
            return "";
        return "/Home/ShowTempImg?id=" + offeringImageBlob.getBlobId();
    }

    /**
     * 商品文档对象
     */
    private BlobInfoDTO offeringFileBlob;

    /**
     * 商品文档对象列表
     */
    private List<BlobInfoDTO> offeringFileBlobs;
    public List<BlobInfoDTO> getOfferingFileBlobs() {
        offeringImageBlobs = new ArrayList<BlobInfoDTO>();
        if (offeringProperties == null || offeringProperties.size() <= 0)
            return offeringImageBlobs;

        List<OfferingPropertyDTO> blobProperties = offeringProperties.stream()
                .filter(m -> m.getOfferingPropertyType() == OfferingPropertyType.File)
                .sorted(Comparator.comparing(OfferingPropertyDTO::getIndex))
                .collect(Collectors.toList());
        for (OfferingPropertyDTO blobProperty : blobProperties) {
            BlobInfoDTO blob = SerializeHelper.FromJson(blobProperty.getValue(), BlobInfoDTO.class);
            if (blob != null) {
                //第一次保存时（新创建的），自增列：属性Id为序列化成Json字符串中，故反序列化需要将属性Id进行重新赋值
                blob.setPropertyId(blobProperty.getId());
                offeringImageBlobs.add(blob);
            }
        }
        return offeringImageBlobs;
    }

    /**
     * 商品文档下载路径
     */
    private String downloadFileUrl;
    public String getDownloadFileUrl() {
        if (offeringFileBlob == null || StringExtensions.isNullOrEmpty(offeringFileBlob.getBlobId()))
            return "";
        return "/Home/ShowTempImg?id=" + offeringFileBlob.getBlobId();
    }

    /**
     * 商品单价
     */
    private java.math.BigDecimal offeringPrice;

    /**
     * 商品折扣
     */
    private java.math.BigDecimal offeringDiscount;

    /**
     * 商品税率
     */
    private java.math.BigDecimal offeringRate;

    /**
     * 描述(显示时，填充html description)
     */
    private String description;

    /**
     * 商品类型：kc.enums.OfferingType
     */
    private OfferingType offeringType = OfferingType.Sofeware;
    private String offeringTypeString;
    public String getOfferingTypeString() {
        if (offeringType == null)
            return OfferingType.Sofeware.getDesc();
        return offeringType.getDesc();
    }

    /**
     * 商品审批状态：kc.enums.OfferingStatus
     */
    private OfferingStatus status = OfferingStatus.Draft;
    private String statusString = status.getDesc();

    public String getStatusString() {
        if (status == null)
            return OfferingStatus.Draft.getDesc();
        return status.getDesc();
    }

    /**
     * 商品二维码BlobId
     */
    private String barcode;

    /**
     * 排序
     */
    private int index;

    private String content;
    public String getContent() {
        if (offeringProperties == null || offeringProperties.size() <= 0)
            return null;

        Optional<OfferingPropertyDTO> blobProperty = offeringProperties.stream()
                .filter(m -> m.getOfferingPropertyType() == OfferingPropertyType.Detail)
                .findFirst();
        return blobProperty.map(OfferingPropertyDTO::getValue).orElse(null);
    }

    private Integer contentId;
    public Integer getContentId() {
        if (offeringProperties == null || offeringProperties.size() <= 0)
            return null;

        Optional<OfferingPropertyDTO> blobProperty = offeringProperties.stream()
                .filter(m -> m.getOfferingPropertyType() == OfferingPropertyType.Detail)
                .findFirst();
        return blobProperty.map(OfferingPropertyDTO::getId).orElse(null);
    }

    //private OfferingCategoryDTO category;

    private int categoryId;
    private String categoryName;

    /**
     * 保存前端传来的已经删除的商品图片属性Id列表，无需序列化至前端
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> deletedOfferingPropertyIds = new ArrayList<Integer>();

    /**
     * 保存前端传来的已经删除的产品规格图片属性Id列表，无需序列化至前端
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> deletedProductPropertyIds = new ArrayList<Integer>();

    /**
     * 其值转化为以下对象：content（类型：Detail)、getOfferingImageBlobs（类型：Image），提供给前端使用
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<OfferingPropertyDTO> offeringProperties = new ArrayList<OfferingPropertyDTO>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        OfferingDTO node = (OfferingDTO) o;

        if (offeringId != node.offeringId)
            return false;
        if (!Objects.equals(offeringCode, node.offeringCode))
            return false;
        return Objects.equals(offeringName, node.offeringName);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + offeringId;
        result = 31 * result + (offeringCode != null ? offeringCode.hashCode() : 0);
        result = 31 * result + (offeringName != null ? offeringName.hashCode() : 0);
        return result;
    }
}
