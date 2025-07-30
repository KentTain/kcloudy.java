package kc.dto.portal;

import kc.dto.BlobInfoDTO;
import kc.dto.EntityDTO;
import kc.enums.portal.OfferingStatus;
import kc.enums.portal.OfferingType;
import kc.framework.extension.StringExtensions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.Date;
import java.util.Objects;

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
    private BlobInfoDTO offeringImageBlob;

    /**
     * 商品图片查看路径
     */
    private String showImageUrl;
    public String getShowImageUrl(){
        if(offeringImageBlob == null || StringExtensions.isNullOrEmpty(offeringImageBlob.getBlobId()))
            return "";
        return "/Home/ShowTempImg?id=" + offeringImageBlob.getBlobId();
    }

    /**
     * 商品文档对象
     */
    private BlobInfoDTO offeringFileBlob;

    /**
     * 商品文档下载路径
     */
    private String downloadFileUrl;
    public String getDownloadFileUrl(){
        if(offeringFileBlob == null || StringExtensions.isNullOrEmpty(offeringFileBlob.getBlobId()))
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

    //private OfferingCategoryDTO category;

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
