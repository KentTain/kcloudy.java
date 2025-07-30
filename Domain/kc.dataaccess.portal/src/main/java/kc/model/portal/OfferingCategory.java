package kc.model.portal;

import kc.framework.base.TreeNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Tables.OfferingCategory)
@Inheritance(strategy= InheritanceType.JOINED)
public class OfferingCategory extends TreeNode<OfferingCategory> implements java.io.Serializable{

    /**
     * 商品首页图片
     */
    @Column(name = "ImageBlob", length = 512)
    private String imageBlob;

    /**
     * 商品文档
     */
    @Column(name = "FileBlob", length = 512)
    private String fileBlob;

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        OfferingCategory node = (OfferingCategory) o;

        if (imageBlob != node.imageBlob)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (imageBlob != null ? imageBlob.hashCode() : 0);
        return result;
    }
}
