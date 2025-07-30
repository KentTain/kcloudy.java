package kc.dto.portal;

import kc.dto.EntityDTO;
import kc.framework.enums.BusinessType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class CompanyContactDTO extends EntityDTO implements java.io.Serializable {

    private int id;

    /**
     * 业务类型
     */
    private BusinessType businessType;
    private String businessTypeString;
    public String getBusinessTypeString(){
        if(businessType == null)
            return BusinessType.None.getDesc();
        return businessType.getDesc();
    }

    /**
     * 联系人UserId
     */
    @Length(min = 0, max = 128, message = "详细地址不能超过128个字符")
    private String contactId;

    /**
     * 联系人姓名
     */
    @Length(min = 0, max = 50, message = "联系人姓名不能超过50个字符")
    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;

    /**
     * 联系人QQ
     */
    @Length(min = 0, max = 20, message = "联系人QQ不能超过20个字符")
    private String contactQQ;
    /**
     * 联系人微信
     */
    @Length(min = 0, max = 128, message = "联系人微信不能超过128个字符")
    private String contactWeixin;
    /**
     * 联系人邮件
     */
    @Length(min = 0, max = 128, message = "联系人邮件不能超过128个字符")
    private String contactEmail;

    /**
     * 联系人手机
     */
    @Length(min = 0, max = 20, message = "联系人手机不能超过20个字符")
    private String contactPhoneNumber;

    /**
     * 联系人座机
     */
    @Length(min = 0, max = 20, message = "联系人座机不能超过20个字符")
    private String contactTelephone;

    /**
     * 联系人职位
     */
    @Length(min = 0, max = 20, message = "联系人职位不能超过20个字符")
    private String positionName;

    /**
     * 是否默认联系人
     */
    @com.fasterxml.jackson.annotation.JsonProperty("isDefault")
    private boolean isDefault;

    private CompanyInfoDTO companyInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        CompanyContactDTO node = (CompanyContactDTO) o;

        if (id != node.id)
            return false;
        if (businessType != node.businessType)
            return false;

        if (!Objects.equals(contactName, node.contactName))
            return false;
        if (!Objects.equals(contactEmail, node.contactEmail))
            return false;
        if (!Objects.equals(contactPhoneNumber, node.contactPhoneNumber))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        result = 31 * result + businessType.getIndex();
        result = 31 * result + (contactName != null ? contactName.hashCode() : 0);
        result = 31 * result + (contactEmail != null ? contactEmail.hashCode() : 0);
        result = 31 * result + (contactPhoneNumber != null ? contactPhoneNumber.hashCode() : 0);
        return result;
    }
}
