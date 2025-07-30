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
@Table(name = Tables.CompanyContact)
@Inheritance(strategy = InheritanceType.JOINED)
public class CompanyContact extends kc.framework.base.Entity implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    /**
     * 业务类型
     */
    @Column(name = "BusinessType")
    private int businessType;

    /**
     * 联系人UserId
     */
    @Column(name = "ContactId", length = 128)
    private String contactId;

    /**
     * 联系人姓名
     */
    @Column(name = "ContactName", length = 50)
    private String contactName;

    /**
     * 联系人QQ
     */
    @Column(name = "ContactQQ", length = 20)
    private String contactQQ;
    /**
     * 联系人微信
     */
    @Column(name = "ContactWeixin", length = 128)
    private String contactWeixin;
    /**
     * 联系人邮件
     */
    @Column(name = "ContactEmail", length = 128)
    private String contactEmail;

    /**
     * 联系人手机
     */
    @Column(name = "ContactPhoneNumber", length = 20)
    private String contactPhoneNumber;

    /**
     * 联系人座机
     */
    @Column(name = "ContactTelephone", length = 20)
    private String contactTelephone;

    /**
     * 联系人职位
     */
    @Column(name = "PositionName", length = 20)
    private String positionName;

    /**
     * 是否默认联系人
     */
    @Column(name = "IsDefault")
    private boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "CompanyId", nullable = false, foreignKey = @ForeignKey(name = "FK_ptl_CompanyContact_ptl_CompanyInfo_CompanyId"))
    private CompanyInfo companyInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        CompanyContact node = (CompanyContact) o;

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
        result = 31 * result + businessType;
        result = 31 * result + (contactName != null ? contactName.hashCode() : 0);
        result = 31 * result + (contactEmail != null ? contactEmail.hashCode() : 0);
        result = 31 * result + (contactPhoneNumber != null ? contactPhoneNumber.hashCode() : 0);
        return result;
    }
}
