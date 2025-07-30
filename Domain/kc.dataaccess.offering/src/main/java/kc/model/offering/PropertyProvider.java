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
@Table(name = Tables.PropertyProvider)
@Inheritance(strategy= InheritanceType.JOINED)
public class PropertyProvider extends kc.framework.base.EntityBase implements java.io.Serializable, Comparable<PropertyProvider>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    /**
     * 类型：kc.enums.offering.ServiceDataType
     */
    @Column(name = "ServiceDataType")
    private int serviceDataType = 0;

    /**
     * 名称
     */
    @Column(name = "Name", length = 256)
    private String name;

    /**
     * 描述
     */
    @Column(name = "Description", length=4000)
    private String description;

    /**
     * 是否能编辑
     */
    @Column(name = "CanEdit")
    private boolean canEdit;

    /**
     * 是否为必填
     */
    @Column(name = "IsRequire")
    private boolean isRequire;

    @Column(name = "Index")
    private int index;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryId", nullable = false, foreignKey = @ForeignKey(name="FK_prd_PropertyProvider_prd_Category_CategoryId"))
    private Category category;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "serviceProvider")
    @OrderBy("index ASC")
    private List<PropertyProviderAttr> serviceProviderAttrs = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        PropertyProvider node = (PropertyProvider) o;

        if (id != node.id)
            return false;
        return Objects.equals(name, node.name);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(PropertyProvider o) {
        //return -1; //-1表示放在红黑树的左边,即逆序输出
        //return 1;  //1表示放在红黑树的右边，即顺序输出
        //return o;  //表示元素相同，仅存放第一个元素
        //主要条件 姓名的长度,如果姓名长度小的就放在左子树，否则放在右子树
        int num = this.index - o.index;
        //姓名的长度相同，不代表内容相同,如果按字典顺序此 String 对象位于参数字符串之前，则比较结果为一个负整数。
        //如果按字典顺序此 String 对象位于参数字符串之后，则比较结果为一个正整数。
        //如果这两个字符串相等，则结果为 0
        return num == 0 ? this.name.compareTo(o.name) : num;
    }
}
