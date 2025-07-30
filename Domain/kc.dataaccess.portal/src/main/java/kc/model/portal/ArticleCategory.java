package kc.model.portal;

import kc.framework.base.TreeNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Tables.ArticleCategory)
@Inheritance(strategy = InheritanceType.JOINED)
public class ArticleCategory extends TreeNode<ArticleCategory> implements java.io.Serializable {

    /**
     * 文章类型：jumoon.portal.enums.ArticleType
     */
    @Column(name = "ArticleType")
    private int articleType = 0;

    /**
     * 关键字
     */
    @Column(name = "Keywords", length = 512)
    private String keywords;

    /**
     * 描述(显示时，填充html description)
     */
    @Column(name = "Description", length = 512)
    private String description;

    /**
     * 是否显示
     */
    @Column(name = "IsShow")
    private boolean isShow;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "articleCategory")
    private List<Article> articles = new ArrayList<Article>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        ArticleCategory node = (ArticleCategory) o;

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
