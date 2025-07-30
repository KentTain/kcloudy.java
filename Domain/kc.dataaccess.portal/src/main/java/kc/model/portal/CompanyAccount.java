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
@Table(name = Tables.CompanyAccount)
@Inheritance(strategy = InheritanceType.JOINED)
public class CompanyAccount extends kc.framework.base.Entity implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    /**
     * 银行账户类型：
     *      0：一般存款账户；1：基本存款账户；2：临时存款账户；3：专用存款账户；4：个人账户；99：其他
     */
    @Column(name = "BankType")
    private int bankType;

    /**
     * 开户名
     */
    @Column(name = "AccountName", length = 1024)
    private String accountName;

    /**
     * 开户银行
     */
    @Column(name = "BankName", length = 512)
    private String bankName;
    /**
     * 开户银行帐号
     */
    @Column(name = "BankNumber", length = 128)
    private String bankNumber;
    /**
     * 省份Id
     */
    @Column(name = "ProvinceId")
    private int provinceId;
    /**
     * 省份名
     */
    @Column(name = "ProvinceName", length = 128)
    private String provinceName;

    /**
     * 城市Id
     */
    @Column(name = "CityId")
    private int cityId;
    /**
     * 城市名
     */
    @Column(name = "CityName", length = 128)
    private String cityName;

    /**
     * 区域Id
     */
    @Column(name = "DistrictId")
    private int districtId;
    /**
     * 区域名
     */
    @Column(name = "DistrictName", length = 128)
    private String districtName;

    /**
     * 街道Id
     */
    @Column(name = "StreetId")
    private int streetId;
    /**
     * 街道名
     */
    @Column(name = "StreetName", length = 256)
    private String streetName;

    /**
     * 详细地址
     */
    @Column(name = "BankAddress", length = 1024)
    private String bankAddress;

    /**
     * 联系人UserId
     */
    @Column(name = "ContactId", length = 128)
    private String contactId;

    /**
     * 联系人名称
     */
    @Column(name = "ContactName", length = 50)
    private String contactName;

    /**
     * 备注
     */
    @Column(name = "Remark", length = 1000)
    private String remark;

    @ManyToOne
    @JoinColumn(name = "CompanyId", nullable = false, foreignKey = @ForeignKey(name = "FK_ptl_CompanyAccount_ptl_CompanyInfo_CompanyId"))
    private CompanyInfo companyInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        CompanyAccount node = (CompanyAccount) o;

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
        result = 31 * result + bankType;
        result = 31 * result + (accountName != null ? accountName.hashCode() : 0);
        result = 31 * result + (bankNumber != null ? bankNumber.hashCode() : 0);
        return result;
    }
}
