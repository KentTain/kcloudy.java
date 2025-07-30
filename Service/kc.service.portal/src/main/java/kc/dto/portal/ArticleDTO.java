package kc.dto.portal;

import kc.dto.EntityDTO;
import kc.enums.portal.ArticleStatus;
import kc.enums.portal.ArticleType;
import kc.framework.extension.StringExtensions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class ArticleDTO extends EntityDTO implements java.io.Serializable{

    @com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
    private boolean isEditMode = false;

    private int id;
    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 作者
     */
    private String author;

    /**
     * 作者邮箱
     */
    private String authorEmail;

    /**
     * 关键字
     */
    private String keywords;

    /**
     * 描述(显示时，填充html description)
     */
    private String articleDesc;

    /**
     * 文章类型：ArticleType
     */
    private ArticleType articleType;
    private String articleTypeString;
    public String getArticleTypeString(){
        if(articleType == null)
            return ArticleType.News.getDesc();
        return articleType.getDesc();
    }

    /**
     * 商品审批状态
     */
    private ArticleStatus status = ArticleStatus.Draft;
    private String statusString = status.getDesc();
    public String getStatusString(){
        if(status == null)
            return ArticleStatus.Draft.getDesc();
        return status.getDesc();
    }

    /**
     * 是否能评论
     */
    private boolean canComment;

    /**
     * 匿名可评论
     */
    private boolean canAnonymousComment;

    /**
     * 附件链接
     */
    private String fileUrl;

    private String downloadUrl;
    public String getDownloadUrl(){
        if(StringExtensions.isNullOrEmpty(fileUrl))
            return "";
        return "/Home/ShowTempImg?id=" + fileUrl;
    }

    /**
     * 外链
     */
    private String link;

    /**
     * 是否显示
     */
    @com.fasterxml.jackson.annotation.JsonProperty("isShow")
    private boolean isShow = false;

    private ArticleCategoryDTO articleCategory;

    private int articleCategoryId;
    private String articleCategoryName;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        ArticleDTO node = (ArticleDTO) o;

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
