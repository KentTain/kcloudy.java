package kc.mapping.portal;

import kc.dto.portal.ArticleCategoryDTO;
import kc.enums.portal.*;
import kc.mapping.CycleAvoidingMappingContext;
import kc.model.portal.Article;
import kc.model.portal.ArticleCategory;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ArticleCategoryMapping {
    ArticleCategoryMapping INSTANCE = Mappers.getMapper(ArticleCategoryMapping.class);

    @Mappings({
            @Mapping(source = "text", target = "name" ),
            @Mapping(source = "children", target = "childNodes" ),
            //@Mapping(source = "parentId", target = "parentNode.id" ),
            @Mapping(target = "parentNode", ignore = true),
            @Mapping(target = "articles", ignore = true)
    })
    ArticleCategory to(ArticleCategoryDTO source, @Context CycleAvoidingMappingContext context);

    List<ArticleCategory> to(List<ArticleCategory> source, @Context CycleAvoidingMappingContext context);

    @Mappings({
            @Mapping(source = "name", target = "text" ),
            @Mapping(source = "childNodes", target = "children" ),
            @Mapping(source = "parentNode.id", target = "parentId" ),
            @Mapping(source = "parentNode.name", target = "parentName" ),
            @Mapping(source = "articleType", target = "articleType"),
            @Mapping(target = "editMode", ignore = true),
            @Mapping(target = "articleTypeString", ignore = true),
            @Mapping(target = "parentNode", ignore = true),
            @Mapping(target = "articles", ignore = true)
    })
    @InheritInverseConfiguration
    ArticleCategoryDTO from(ArticleCategory source, @Context CycleAvoidingMappingContext context);

    List<ArticleCategoryDTO> from(List<ArticleCategory> source, @Context CycleAvoidingMappingContext context);

    default ArticleType ConvertArticleType(int type) {
        return ArticleType.valueOf(type);
    }
    default int ConvertArticleType(ArticleType type) { return type.getIndex(); }

    default String ConvertArticles(java.util.Set<Article> articles) {
        return articles.stream().map(m -> m.getAuthor()).collect(Collectors.joining(","));
    }

}
