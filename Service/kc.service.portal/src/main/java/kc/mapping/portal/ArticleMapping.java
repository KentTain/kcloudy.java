package kc.mapping.portal;

import kc.dto.portal.ArticleDTO;
import kc.enums.portal.*;
import kc.model.portal.Article;
import kc.model.portal.ArticleCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface ArticleMapping {
    ArticleMapping INSTANCE = Mappers.getMapper(ArticleMapping.class);

    @Mappings({
            @Mapping(source = "articleType", target = "articleType"),
            @Mapping(source = "status", target = "status"),
            @Mapping(target = "articleCategory", ignore = true)
    })
    Article to(ArticleDTO source);

    List<Article> to(List<ArticleDTO> source);

    @Mappings({
            @Mapping(source = "articleCategory.id", target = "articleCategoryId"),
            @Mapping(source = "articleCategory.name", target = "articleCategoryName"),
            @Mapping(source = "articleType", target = "articleType"),
            @Mapping(source = "status", target = "status"),
            @Mapping(target = "editMode", ignore = true),
            @Mapping(target = "downloadUrl", ignore = true),
            @Mapping(target = "statusString", ignore = true),
            @Mapping(target = "articleTypeString", ignore = true),
            @Mapping(target = "articleCategory", ignore = true),
    })
    ArticleDTO from(Article source);

    List<ArticleDTO> from(List<Article> source);

    default ArticleType ConvertArticleType(int type) {
        return ArticleType.valueOf(type);
    }
    default int ConvertArticleType(ArticleType type) { return type.getIndex(); }

    default ArticleStatus ConvertArticleStatus(int type) {
        return ArticleStatus.valueOf(type);
    }
    default int ConvertArticleStatus(ArticleStatus type) { return type.getIndex(); }
}
