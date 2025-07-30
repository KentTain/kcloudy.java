package kc.dto.portal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kc.dto.TreeNodeDTO;
import kc.enums.portal.ArticleType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import javax.persistence.MappedSuperclass;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class ArticleCategoryDTO extends TreeNodeDTO<ArticleCategoryDTO> implements java.io.Serializable {

    private static final long serialVersionUID = -8034918023643621429L;

    @com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
    private boolean isEditMode;

    /**
     * 组织类型
     */
    private ArticleType articleType = ArticleType.News;
    private String articleTypeString;
    public String getArticleTypeString(){
        if(articleTypeString == null)
            return ArticleType.News.getDesc();
        return articleType.getDesc();
    }

    /**
     * 描述
     */
    private String description;

    /**
     * 关键字
     */
    private String keywords;

    /**
     * 是否显示
     */
    @com.fasterxml.jackson.annotation.JsonProperty("isShow")
    private boolean isShow;

    @JsonIgnore
    private Set<ArticleDTO> articles = new HashSet<ArticleDTO>();

    private Integer parentId;

    private String parentName;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        ArticleCategoryDTO node = (ArticleCategoryDTO) o;

        if (keywords != node.keywords)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (keywords != null ? keywords.hashCode() : 0);
        return result;
    }
}
