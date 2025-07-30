package kc.dataaccess.portal;


import kc.database.repository.ITreeNodeRepository;
import kc.model.portal.ArticleCategory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IArticleCategoryRepository extends ITreeNodeRepository<ArticleCategory, Integer>, JpaSpecificationExecutor<ArticleCategory> {

    @Query("SELECT d FROM ArticleCategory d LEFT OUTER JOIN FETCH d.parentNode WHERE d.id = :id")
    ArticleCategory findWithParentById(int id);

    @Query("FROM ArticleCategory d JOIN d.articles u WHERE u.id = :articleId")
    List<ArticleCategory> findWithArticlesByArticleId(String articleId);

    @Query("SELECT CASE WHEN COUNT(c.name) > 0 THEN true ELSE false END FROM ArticleCategory c WHERE c.name = :name")
    boolean existsByName(@Param("name") String name);

    @Query("SELECT CASE WHEN COUNT(c.name) > 0 THEN true ELSE false END FROM ArticleCategory c WHERE c.name = :name and c.id != :id")
    boolean existsByIdAndName(@Param("id") int id, @Param("name") String name);

}
