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
@Table(name = Tables.WebSiteInfo)
@Inheritance(strategy = InheritanceType.JOINED)
public class WebSiteInfo extends kc.framework.base.Entity implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;
    /**
     * 文章标题
     */
    @Column(name = "Name", length = 512)
    private String name;

    /**
     * 时间 周一至周五
     */
    @Column(name = "ServiceDate", length = 50)
    private String serviceDate;

    /**
     * 时间
     */
    @Column(name = "ServiceTime", length = 50)
    private String serviceTime;
    /**
     * logo
     */
    @Column(name = "LogoImageBlob", length = 512)
    private String logoImageBlob;

    /**
     * 二维码
     */
    @Column(name = "QRCode", length = 128)
    private String qrCode;
    /**
     * 首页轮播图
     */
    @Column(name = "HomePageSlide", length = 2046)
    private String homePageSlide;

    /**
     * 主图
     */
    @Column(name = "MainImageBlob", length = 512)
    private String mainImageBlob;

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
     * SEO关键字
     */
    @Column(name = "KeyWord", length = 1000)
    private String keyWord;
    /**
     * SEO描述
     */
    @Column(name = "KeyDescription", length = 2000)
    private String keyDescription;

    /**
     * 二维码
     */
    @Column(name = "SkinUrl", length = 2000)
    private String skinUrl;

    /**
     * 公司简介
     */
    @Column(name = "CompanyInfo", length = 8000)
    private String companyInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        WebSiteInfo node = (WebSiteInfo) o;

        if (id != node.id)
            return false;
        if (!Objects.equals(name, node.name))
            return false;
        if (!Objects.equals(keyWord, node.keyWord))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (keyWord != null ? keyWord.hashCode() : 0);
        return result;
    }
}
