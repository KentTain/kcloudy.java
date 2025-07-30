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
@Table(name = Tables.Article)
@Inheritance(strategy = InheritanceType.JOINED)
public class Article extends kc.framework.base.Entity implements java.io.Serializable {
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
     * 文章内容
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "Content", columnDefinition = "TEXT")
    private String content;

    /**
     * 作者
     */
    @Column(name = "Author", length = 128)
    private String author;

    /**
     * 作者邮箱
     */
    @Column(name = "AuthorEmail", length = 128)
    private String authorEmail;

    /**
     * 关键字
     */
    @Column(name = "Keywords", length = 512)
    private String keywords;

    /**
     * 描述(显示时，填充html description)
     */
    @Column(name = "ArticleDesc", length = 512)
    private String articleDesc;

    /**
     * 文章类型：jumoon.portal.enums.ArticleType
     */
    @Column(name = "ArticleType")
    private int articleType = 0;

    /**
     * 文章配图
     */
    @Column(name = "ImageBlob")
    private String imageBlob;

    /**
     * 是否能评论
     */
    @Column(name = "CanComment")
    private boolean canComment;

    /**
     * 匿名可评论
     */
    @Column(name = "CanAnonymousComment")
    private boolean canAnonymousComment;

    /**
     * 附件链接
     */
    @Column(name = "FileBlob", length = 512)
    private String fileBlob;

    /**
     * 外链
     */
    @Column(name = "Link", length = 512)
    private String link;

    /**
     * 是否显示
     */
    @Column(name = "IsShow")
    private boolean isShow;

    /**
     * 发布状态
     */
    @Column(name = "Status")
    private int status = 0;

    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false, foreignKey = @ForeignKey(name = "FK_ptl_Article_ptl_ArticleCategory_CategoryId"))
    private ArticleCategory articleCategory;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        Article node = (Article) o;

        if (id != node.id)
            return false;
        if (!Objects.equals(title, node.title))
            return false;
        if (!Objects.equals(author, node.author))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }
}
