package kc.model.offering;

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
@Entity
@Table(name = Tables.Product)
@Inheritance(strategy= InheritanceType.JOINED)
public class Product extends kc.framework.base.Entity implements java.io.Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductId")
    private int productId;

    /**
     * 产品编码
     */
    @Column(name = "ProductCode")
    private String productCode;

    /**
     * 产品名称
     */
    @Column(name = "ProductName")
    private String productName;

    /**
     * 是否生效
     */
    @Column(name = "IsEnabled")
    private boolean isEnabled = true;

    /**
     * 产品图片
     */
    @Column(name = "ProductImage", length = 1000)
    private String productImage;

    /**
     * 产品文件
     */
    @Column(name = "productFile", length = 1000)
    private String productFile;

    /**
     * 产品单价
     */
    @Column(name = "ProductPrice")
    private java.math.BigDecimal productPrice;

    /**
     * 产品折扣
     */
    @Column(name = "ProductDiscount")
    private java.math.BigDecimal productDiscount;
    /**
     * 产品税率
     */
    @Column(name = "ProductRate")
    private java.math.BigDecimal productRate;

    /**
     * 产品二维码BlobId
     */
    @Column(name = "Barcode", length = 512)
    private String barcode;

    /**
     * 排序
     */
    @Column(name = "Index")
    private int index;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "OfferingId", nullable = false, foreignKey = @ForeignKey(name="FK_prd_Product_prd_Offering_OfferingId"))
    private Offering offering;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.DETACH})
    @OrderBy("index ASC")
    private List<ProductProperty> productProperties = new ArrayList<ProductProperty>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        Product node = (Product) o;

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
