package kc.dto.portal;

import kc.enums.portal.LinkType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Objects;
import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class WebSiteLinkDTO extends kc.framework.base.Entity implements java.io.Serializable {
    private int id;
    /**
     * 链接名称
     */
    @Length(min = 0, max = 128, message = "链接名称不能超过128个字符")
    @NotBlank(message = "链接名称不能为空")
    private String title;

    /**
     * 链接类型
     */
    private LinkType linkType;
    private String linkTypeString;
    public String getLinkTypeString(){
        if(linkType == null)
            return LinkType.Links.getDesc();
        return linkType.getDesc();
    }

    /**
     * 外部链接
     */
    @Length(min = 0, max = 512, message = "外部链接不能超过512个字符")
    private String links;

    /**
     * 内容
     */
    @Length(min = 0, max = 4000, message = "内容不能超过4000个字符")
    private String content;

    /**
     * SEO关键字
     */
    @Length(min = 0, max = 1000, message = "SEO关键字不能超过1000个字符")
    private String metaKeywords;
    /**
     * SEO描述
     */
    @Length(min = 0, max = 2000, message = "SEO描述不能超过2000个字符")
    private String metaDescription;
    /**
     * SEO标题
     */
    @Length(min = 0, max = 1000, message = "SEO标题不能超过1000个字符")
    private String metaTitle;

    /**
     * 是否可用
     */
    @com.fasterxml.jackson.annotation.JsonProperty("isEnable")
    private boolean isEnable;

    /**
     * 是否显示
     */
    @com.fasterxml.jackson.annotation.JsonProperty("isNav")
    private boolean isNav;


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        WebSiteLinkDTO node = (WebSiteLinkDTO) o;

        if (id != node.id)
            return false;
        if (!Objects.equals(title, node.title))
            return false;
        if (!Objects.equals(links, node.links))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (links != null ? links.hashCode() : 0);
        return result;
    }
}
