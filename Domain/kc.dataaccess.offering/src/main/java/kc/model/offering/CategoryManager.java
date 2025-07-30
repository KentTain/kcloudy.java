package kc.model.offering;

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
@Table(name = Tables.CategoryManager)
@Inheritance(strategy= InheritanceType.JOINED)
public class CategoryManager extends kc.framework.base.Entity implements java.io.Serializable, Comparable<CategoryManager>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;
    /**
     * 商品编码
     */
    @Column(name = "UserId", length = 128)
    private String userId;
    /**
     * 用户系统编号
     */
    @Column(name = "MemberId", length = 20)
    private String memberId;

    /**
     * 用户名
     */
    @Column(name = "UserName", length = 256)
    private String userName;

    /**
     * 用户显示名
     */
    @Column(name = "DisplayName", length = 512)
    private String displayName;

    /**
     * 用户邮箱
     */
    @Column(name = "Email")
    private String email;

    /**
     * 用户手机号
     */
    @Column(name = "PhoneNumber")
    private String phoneNumber;

    /**
     * 用户座机号
     */
    @Column(name = "Telephone")
    private String telephone;

    /**
     * 用户QQ号
     */
    @Column(name = "ContactQQ")
    private String contactQQ;

    /**
     * 用户微信号
     */
    @Column(name = "OpenId")
    private String openId;


    /**
     * 是否为默认联系人
     */
    @Column(name = "IsDefault")
    private boolean isDefault;

    /**
     * 是否生效
     */
    @Column(name = "IsValid")
    private boolean isValid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryId", nullable = false, foreignKey = @ForeignKey(name="FK_prd_CategoryManager_prd_Category_CategoryId"))
    private Category category;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        CategoryManager node = (CategoryManager) o;

        if (id != node.id)
            return false;
        if (!Objects.equals(userId, node.userId))
            return false;
        return Objects.equals(userName, node.userName);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(CategoryManager o) {
        //return -1; //-1表示放在红黑树的左边,即逆序输出
        //return 1;  //1表示放在红黑树的右边，即顺序输出
        //return o;  //表示元素相同，仅存放第一个元素
        //主要条件 姓名的长度,如果姓名长度小的就放在左子树，否则放在右子树
        int num = this.userName.length() - o.userName.length();
        //姓名的长度相同，不代表内容相同,如果按字典顺序此 String 对象位于参数字符串之前，则比较结果为一个负整数。
        //如果按字典顺序此 String 对象位于参数字符串之后，则比较结果为一个正整数。
        //如果这两个字符串相等，则结果为 0
        int num1 =  num == 0 ? this.userName.compareTo(o.userName) : num;

        return  num1 == 0 ? this.displayName.compareTo(o.displayName) : num1;
    }
}
