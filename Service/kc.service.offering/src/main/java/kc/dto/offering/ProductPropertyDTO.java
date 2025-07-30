package kc.dto.offering;

import kc.dto.EntityBaseDTO;
import kc.enums.offering.ProductPropertyType;
import kc.enums.offering.ServiceDataType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy= InheritanceType.JOINED)
public class ProductPropertyDTO extends EntityBaseDTO implements java.io.Serializable, Comparable<ProductPropertyDTO>{

    private int id;

    /**
     * 类型：kc.enums.offering.ProductPropertyType
     */
    private ProductPropertyType productPropertyType = ProductPropertyType.Specification;
    private String productPropertyTypeString;
    public String getProductPropertyTypeString() {
        if (productPropertyType == null)
            return ProductPropertyType.Specification.getDesc();
        return productPropertyType.getDesc();
    }

    /**
     * 名称
     */
    private String name;

    /**
     * 属性值
     */
    private String value;

    /**
     * 属性值2
     */
    private String value1;

    /**
     * 属性值3
     */
    private String value2;

    /**
     * 引用商品的属性Id
     */
    private Integer refProviderId;
    /**
     * 引用商品的属性值Id
     */
    private Integer refProviderAttrId;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否能编辑
     */
    private boolean canEdit;

    /**
     * 是否为必填
     */
    private boolean isRequire;

    private boolean isProvider;

    private int index;

    private ProductDTO product;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        ProductPropertyDTO node = (ProductPropertyDTO) o;

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
    public int compareTo(ProductPropertyDTO o) {
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
