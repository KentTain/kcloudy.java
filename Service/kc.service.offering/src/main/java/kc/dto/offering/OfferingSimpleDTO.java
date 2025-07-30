package kc.dto.offering;

import kc.dto.BlobInfoDTO;
import kc.dto.EntityDTO;
import kc.enums.offering.OfferingStatus;
import kc.enums.offering.OfferingType;
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
@Inheritance(strategy = InheritanceType.JOINED)
public class OfferingSimpleDTO extends EntityDTO implements java.io.Serializable {

    @com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
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
     * 商品图片对象
     */
    private String offeringImage;
    private BlobInfoDTO offeringImageBlob;
    public BlobInfoDTO getOfferingImageBlob() {
        if (StringExtensions.isNullOrEmpty(offeringImage))
            return null;
        return SerializeHelper.FromJson(offeringImage, BlobInfoDTO.class);
    }

    /**
     * 商品文档下载路径
     */
    private String showImageUrl;
    public String getShowImageUrl(){
        if(offeringImageBlob == null || StringExtensions.isNullOrEmpty(offeringImageBlob.getBlobId()))
            return "";
        return offeringImageBlob.getShowImageUrl();
    }

    /**
     * 商品文档对象
     */
    private String offeringFile;
    private BlobInfoDTO offeringFileBlob;
    public BlobInfoDTO getOfferingFileBlob() {
        if (StringExtensions.isNullOrEmpty(offeringFile))
            return null;
        return SerializeHelper.FromJson(offeringFile, BlobInfoDTO.class);
    }

    /**
     * 商品文档下载路径
     */
    private String downloadFileUrl;
    public String getDownloadFileUrl(){
        if(offeringFileBlob == null || StringExtensions.isNullOrEmpty(offeringFileBlob.getBlobId()))
            return "";
        return offeringFileBlob.getDownloadFileUrl();
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
     * 商品类型：kc.enums.OfferingType
     */
    @Length(min = 0, max = 128, message = "属性名称不能超过128个字符")
    private String offeringTypeCode;
    @Length(min = 0, max = 512, message = "属性名称不能超过512个字符")
    private String offeringTypeName;

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
     * 上架时间
     */
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 下架时间
     */
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 商品二维码BlobId
     */
    private String barcode;

    /**
     * 是否推荐
     */
    private boolean recommended;

    /**
     * 排序
     */
    private int index;

    //private CategoryDTO category;

    private int categoryId;
    private String categoryName;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        OfferingSimpleDTO node = (OfferingSimpleDTO) o;

        if (offeringId != node.offeringId)
            return false;
        if (!Objects.equals(offeringCode, node.offeringCode))
            return false;
        if (!Objects.equals(offeringName, node.offeringName))
            return false;

        return true;
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
