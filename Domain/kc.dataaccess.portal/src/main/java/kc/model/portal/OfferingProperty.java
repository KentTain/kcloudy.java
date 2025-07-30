package kc.model.portal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Tables.OfferingProperty)
@Inheritance(strategy= InheritanceType.JOINED)
public class OfferingProperty extends kc.framework.base.EntityBase implements java.io.Serializable, Comparable<OfferingProperty>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    /**
     * 类型：kc.enums.offering.OfferingPropertyType
     */
    @Column(name = "OfferingPropertyType")
    private int offeringPropertyType = 1;

    /**
     * 名称
     */
    @Column(name = "Name", length = 256)
    private String name;

    /**
     * 属性值
     */
    @Column(name = "Value")
    private String value;

    /**
     * 属性值2
     */
    @Column(name = "Value1")
    private String value1;

    /**
     * 属性值3
     */
    @Column(name = "Value2")
    private String value2;

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

    /**
     * 排序
     */
    @Column(name = "Index")
    private int index;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "OfferingId", nullable = false, foreignKey = @ForeignKey(name="FK_ptl_OfferingProperty_ptl_Offering_OfferingId"))
    private Offering offering;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        OfferingProperty node = (OfferingProperty) o;

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
    public int compareTo(OfferingProperty o) {
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
