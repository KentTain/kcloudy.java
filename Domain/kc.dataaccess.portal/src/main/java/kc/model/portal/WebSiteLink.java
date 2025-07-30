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
@Table(name = Tables.WebSiteLink)
@Inheritance(strategy = InheritanceType.JOINED)
public class WebSiteLink extends kc.framework.base.Entity implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;
    /**
     * 文章标题
     */
    @Column(name = "Title", length = 128)
    private String title;

    /**
     * 作者邮箱
     */
    @Column(name = "LinkType")
    private int linkType;

    /**
     * 描述(显示时，填充html description)
     */
    @Column(name = "Links", length = 512)
    private String links;

    /**
     * 内容
     */
    @Column(name = "Content", length = 4000)
    private String content;

    /**
     * SEO关键字
     */
    @Column(name = "MetaKeywords", length = 1000)
    private String metaKeywords;
    /**
     * SEO描述
     */
    @Column(name = "MetaDescription", length = 2000)
    private String metaDescription;
    /**
     * SEO关键字
     */
    @Column(name = "MetaTitle", length = 1000)
    private String metaTitle;
    /**
     * 是否显示
     */
    @Column(name = "IsEnable")
    private boolean isEnable;

    /**
     * 是否显示
     */
    @Column(name = "IsNav")
    private boolean isNav;


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        WebSiteLink node = (WebSiteLink) o;

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
