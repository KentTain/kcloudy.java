package kc.model.portal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Tables.CompanyInfo)
@Inheritance(strategy = InheritanceType.JOINED)
public class CompanyInfo extends kc.framework.base.EntityBase implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CompanyId")
    private int companyId;
    /**
     * 公司代码
     */
    @Column(name = "CompanyCode", length = 128)
    private String companyCode;
    /**
     * 公司名
     */
    @Column(name = "CompanyName", length = 1024)
    private String companyName;

    /**
     * 行业类型Id
     */
    @Column(name = "industryId")
    private int industryId;
    /**
     * 行业类型
     */
    @Column(name = "IndustryName", length = 1024)
    private String industryName;
    /**
     * 企业经营模式
     */
    @Column(name = "BusinessModel")
    private int businessModel;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "companyInfo")
    private List<CompanyContact> companyContacts = new ArrayList<CompanyContact>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "companyInfo")
    private List<CompanyAddress> companyAddresses = new ArrayList<CompanyAddress>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "companyInfo")
    private List<CompanyAccount> companyAccounts = new ArrayList<CompanyAccount>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "companyInfo")
    private List<CompanyExtInfo> companyExtInfos = new ArrayList<CompanyExtInfo>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "companyInfo")
    private List<CompanyProcessLog> companyProcessLogs = new ArrayList<CompanyProcessLog>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        CompanyInfo node = (CompanyInfo) o;

        if (companyId != node.companyId)
            return false;
        if (!Objects.equals(companyCode, node.companyCode))
            return false;
        if (!Objects.equals(companyName, node.companyName))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + companyId;
        result = 31 * result + (companyCode != null ? companyCode.hashCode() : 0);
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        return result;
    }
}
