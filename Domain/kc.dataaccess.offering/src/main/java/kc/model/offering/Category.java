package kc.model.offering;

import kc.framework.base.TreeNode;
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
@Table(name = Tables.Category)
@Inheritance(strategy= InheritanceType.JOINED)
public class Category extends TreeNode<Category> implements java.io.Serializable{

    /**
     * 商品首页图片
     */
    @Column(name = "CategoryImage", length = 1000)
    private String categoryImage;

    /**
     * 商品文档
     */
    @Column(name = "CategoryFile", length = 1000)
    private String categoryFile;

    /**
     * 商品属性定义：KC.Enums.Offering.OfferingPropertyType
     */
    @Column(name = "OfferingPropertyType")
    public int offeringPropertyType ;

    /**
     * 商品价格定义：KC.Enums.Offering.OfferingPriceType
     */
    @Column(name = "OfferingPriceType")
    public int offeringPriceType;

    /**
     * 描述(显示时，填充html description)
     */
    @Column(name = "Description", length = 4000)
    private String description;

    /**
     * 是否显示
     */
    @Column(name = "IsShow")
    private boolean isShow;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @OrderBy("index ASC")
    private List<Offering> offerings = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @OrderBy("userName ASC")
    private List<CategoryManager> categoryManagers = new ArrayList<CategoryManager>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @OrderBy("index ASC")
    private List<PropertyProvider> propertyProviders = new ArrayList<PropertyProvider>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private Set<CategoryOperationLog> categoryOperationLogs = new HashSet<CategoryOperationLog>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        Category node = (Category) o;

        if (categoryImage != node.categoryImage)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (categoryImage != null ? categoryImage.hashCode() : 0);
        return result;
    }
}
