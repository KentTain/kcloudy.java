package kc.model.portal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Tables.Offering)
@Inheritance(strategy = InheritanceType.JOINED)
public class Offering extends kc.framework.base.Entity implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OfferingId")
    private int offeringId;
    /**
     * 商品编码
     */
    @Column(name = "OfferingCode", length = 16)
    private String offeringCode;

    /**
     * 商品名称
     */
    @Column(name = "OfferingName", length = 256)
    private String offeringName;

    /**
     * 商品类型：kc.enums.OfferingType
     */
    @Column(name = "OfferingType")
    private int offeringType = 0;

    /**
     * 商品审批状态：kc.enums.OfferingStatus
     */
    @Column(name = "Status")
    private int status = 0;

    /**
     * 是否生效
     */
    @Column(name = "IsEnabled")
    private boolean isEnabled = true;

    /**
     * 商品图片
     */
    @Column(name = "OfferingImageBlob", length = 512)
    private String offeringImageBlob;

    /**
     * 商品文档
     */
    @Column(name = "OfferingFileBlob", length = 512)
    private String offeringFileBlob;

    /**
     * 商品单价
     */
    @Column(name = "OfferingPrice")
    private java.math.BigDecimal offeringPrice;

    /**
     * 商品折扣
     */
    @Column(name = "OfferingDiscount")
    private java.math.BigDecimal offeringDiscount;

    /**
     * 商品税率
     */
    @Column(name = "OfferingRate")
    private java.math.BigDecimal offeringRate;

    /**
     * 描述(显示时，填充html description)
     */
    @Column(name = "Description", length = 4000)
    private String description;

    /**
     * 商品二维码BlobId
     */
    @Column(name = "Barcode", length = 512)
    private String barcode;

    /**
     * 排序
     */
    @Column(name = "Index")
    private int index;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryId", nullable = false, foreignKey = @ForeignKey(name = "FK_ptl_Offering_ptl_OfferingCategory_CategoryId"))
    private OfferingCategory category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "offering", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @OrderBy("index ASC")
    private List<OfferingProperty> offeringProperties = new ArrayList<OfferingProperty>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "offering", cascade = {CascadeType.PERSIST})
    private List<OfferingOperationLog> offeringOperationLogs = new ArrayList<OfferingOperationLog>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        Offering node = (Offering) o;

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
