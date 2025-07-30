package kc.dto.offering;

import kc.dto.EntityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.validation.constraints.Email;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy= InheritanceType.JOINED)
public class CategoryManagerDTO extends EntityDTO implements java.io.Serializable{

    private int id;
    /**
     * 商品编码
     */
    @NotNull
    @Length(min = 1, max = 128, message="长度不能超过{max}位")
    private String userId;
    /**
     * 用户系统编号
     */
    @NotNull
    @Length(min = 1, max = 20, message="长度不能超过{max}位")
    private String memberId;

    /**
     * 用户名
     */
    @NotNull
    @Length(min = 1, max = 256, message="用户名长度不能超过{max}位")
    private String userName;

    /**
     * 用户显示名
     */
    @NotNull
    @Length(min = 1, max = 512, message="用户名长度不能超过{max}位")
    private String displayName;

    /**
     * 用户邮箱
     */
    @Email
    private String email;

    /**
     * 用户手机号
     */
    private String phoneNumber;

    /**
     * 用户座机号
     */
    private String telephone;

    /**
     * 用户QQ号
     */
    private String contactQQ;

    /**
     * 用户微信号
     */
    private String openId;

    /**
     * 是否为默认联系人
     */
    @com.fasterxml.jackson.annotation.JsonProperty("isDefault")
    private boolean isDefault;

    /**
     * 是否生效
     */
    @com.fasterxml.jackson.annotation.JsonProperty("isValid")
    private boolean isValid;

    private CategoryDTO category;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        CategoryManagerDTO node = (CategoryManagerDTO) o;

        if (id != node.id)
            return false;
        if (!Objects.equals(userId, node.userId))
            return false;
        if (!Objects.equals(userName, node.userName))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        return result;
    }
}
