package kc.dataaccess.portal;

import kc.model.portal.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IArticleRepository extends JpaRepository<Article, Integer>, JpaSpecificationExecutor<Article> {

    @Query("SELECT CASE WHEN COUNT(c.title) > 0 THEN true ELSE false END FROM Article c WHERE c.title = :name")
    boolean existsByName(@Param("name") String name);

    @Query("SELECT CASE WHEN COUNT(c.title) > 0 THEN true ELSE false END FROM Article c WHERE c.title = :name and c.id != :id")
    boolean existsByIdAndName(@Param("id") int id, @Param("name") String name);
}
