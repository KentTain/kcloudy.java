package kc.service.portal;


import kc.dto.PaginatedBaseDTO;
import kc.dto.portal.*;
import kc.enums.portal.*;

import java.util.List;

public interface IArticleService {

    List<ArticleCategoryDTO> GetRootArticleCategorysByName(String name, ArticleType type);

    ArticleCategoryDTO GetArticleCategoryById(int id);

    boolean SaveArticleCategory(ArticleCategoryDTO data);
    boolean RemoveArticleCategory(int id);
    boolean ExistArticleCategoryName(int id, String name);

    /*------------------------------------------新闻动态-----------------------------------------------*/
    PaginatedBaseDTO<ArticleDTO> findPaginatedArticleByType(int pageIndex, int pageSize, ArticleType type);

    PaginatedBaseDTO<ArticleDTO> findPaginatedArticleByFilter(
            int pageIndex, int pageSize, String title, String content, String author, String authorEmail, ArticleStatus status, ArticleType type);

    ArticleDTO GetArticleById(int id);

    boolean PublishArticle(int id, Boolean isAggree, String content);
    boolean SaveArticle(ArticleDTO data);
    boolean RemoveArticle(int id);
    boolean ExistArticleName(int id, String name);
}
