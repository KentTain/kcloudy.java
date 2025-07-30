package kc.dto.portal;

import kc.dto.EntityDTO;
import kc.enums.portal.BankAccountType;
import kc.enums.portal.LinkType;
import kc.model.portal.CompanyInfo;
import kc.model.portal.Tables;
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
public class CompanyAccountDTO extends EntityDTO implements java.io.Serializable {

    private int id;

    /**
     * 银行账户类型：
     *      0：一般存款账户；1：基本存款账户；2：临时存款账户；3：专用存款账户；4：个人账户；99：其他
     */
    private BankAccountType bankType;
    private String bankTypeString;
    public String getBankTypeString(){
        if(bankType == null)
            return BankAccountType.NormalAccount.getDesc();
        return bankType.getDesc();
    }

    /**
     * 开户名
     */
    @Length(min = 0, max = 1024, message = "开户名不能超过1024个字符")
    @NotBlank(message = "开户名不能为空")
    private String accountName;

    /**
     * 开户银行
     */
    @Column(name = "BankName", length = 512)
    @Length(min = 0, max = 512, message = "开户银行不能超过512个字符")
    @NotBlank(message = "开户银行不能为空")
    private String bankName;
    /**
     * 开户银行帐号
     */
    @Column(name = "BankNumber", length = 128)
    @Length(min = 0, max = 128, message = "开户银行帐号不能超过128个字符")
    @NotBlank(message = "开户银行帐号不能为空")
    private String bankNumber;
    /**
     * 省份Id
     */
    private int provinceId;
    /**
     * 省份名
     */
    private String provinceName;

    /**
     * 城市Id
     */
    private int cityId;
    /**
     * 城市名
     */
    private String cityName;

    /**
     * 区域Id
     */
    private int districtId;
    /**
     * 区域名
     */
    private String districtName;

    /**
     * 街道Id
     */
    @Column(name = "StreetId")
    private int streetId;
    /**
     * 街道名
     */
    private String streetName;

    /**
     * 详细地址
     */
    @Length(min = 0, max = 256, message = "详细地址不能超过256个字符")
    private String bankAddress;

    /**
     * 联系人UserId
     */
    private String contactId;

    /**
     * 联系人名称
     */
    private String contactName;

    /**
     * 备注
     */
    @Length(min = 0, max = 1000, message = "备注不能超过1000个字符")
    private String remark;

    private CompanyInfoDTO companyInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        CompanyAccountDTO node = (CompanyAccountDTO) o;

        if (id != node.id)
            return false;
        if (bankType != node.bankType)
            return false;

        if (!Objects.equals(accountName, node.accountName))
            return false;
        if (!Objects.equals(bankNumber, node.bankNumber))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        result = 31 * result + bankType.getIndex();
        result = 31 * result + (accountName != null ? accountName.hashCode() : 0);
        result = 31 * result + (bankNumber != null ? bankNumber.hashCode() : 0);
        return result;
    }
}
