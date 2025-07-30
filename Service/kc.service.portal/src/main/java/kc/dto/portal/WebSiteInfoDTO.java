package kc.dto.portal;

import kc.dto.EntityDTO;
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
public class WebSiteInfoDTO extends EntityDTO implements java.io.Serializable {

    private int id;
    /**
     * 站点名称
     */
    @Length(min = 0, max = 128, message = "名称不能超过128个字符")
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 服务日期：周一至周五
     */
    @Length(min = 0, max = 50, message = "服务日期不能超过50个字符")
    @NotBlank(message = "服务日期不能为空")
    private String serviceDate;

    /**
     * 服务时间
     */
    @Length(min = 0, max = 50, message = "服务时间不能超过50个字符")
    @NotBlank(message = "服务时间不能为空")
    private String ServiceTime;
    /**
     * logo
     */
    @Length(min = 0, max = 512, message = "Logo图片不能超过512个字符")
    private String logoImageBlob;

    /**
     * 二维码
     */
    @Length(min = 0, max = 128, message = "二维码不能超过128个字符")
    private String qrCode;
    /**
     * 首页轮播图
     */
    @Length(min = 0, max = 2046, message = "首页轮播图不能超过2046个字符")
    private String homePageSlide;

    /**
     * 主图
     */
    @Length(min = 0, max = 512, message = "主图不能超过512个字符")
    private String mainImageBlob;
    /**
     * 联系人UserId
     */
    private String contactId;
    /**
     * 联系人姓名
     */
    @Length(min = 0, max = 512, message = "联系人姓名不能超过512个字符")
    private String contactName;

    /**
     * 联系人QQ
     */
    private String contactQQ;
    /**
     * 联系人微信
     */
    private String contactWeixin;
    /**
     * 联系人邮件
     */
    private String contactEmail;

    /**
     * 联系人手机
     */
    private String contactPhoneNumber;

    /**
     * 联系人座机
     */
    private String contactTelephone;

    /**
     * SEO关键字
     */
    @Length(min = 0, max = 1000, message = "主图不能超过1000个字符")
    private String keyWord;
    /**
     * SEO描述
     */
    @Length(min = 0, max = 2000, message = "主图不能超过2000个字符")
    private String keyDescription;

    /**
     * 皮肤设置
     */
    private String skinUrl;

    /**
     * 公司简介
     */
    @Length(min = 0, max = 4000, message = "公司简介不能超过4000个字符")
    private String companyInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        WebSiteInfoDTO node = (WebSiteInfoDTO) o;

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
