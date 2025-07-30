package kc.service.portal;

import kc.dataaccess.portal.*;
import kc.dto.PaginatedBaseDTO;
import kc.dto.portal.*;
import kc.enums.portal.*;
import kc.framework.extension.*;
import kc.mapping.CycleAvoidingMappingContext;
import kc.mapping.portal.*;
import kc.model.portal.*;
import kc.service.base.ServiceBase;
import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@lombok.extern.slf4j.Slf4j
public class ArticleService extends ServiceBase implements IArticleService {
    private final IArticleCategoryRepository _articleCategoryRepository;
    private final ArticleCategoryMapping _articleCategoryMapping;
    private final IArticleRepository _articleRepository;
    private final ArticleMapping _articleMapping;
    @Autowired
    public ArticleService(IGlobalConfigApiService globalConfigApiService,
                          IArticleCategoryRepository _articleCategoryRepository,
                          ArticleCategoryMapping _articleCategoryMapping,
                          IArticleRepository _articleRepository,
                          ArticleMapping _articleMapping) {
        super(globalConfigApiService);
        this._articleCategoryRepository = _articleCategoryRepository;
        this._articleCategoryMapping = _articleCategoryMapping;
        this._articleRepository = _articleRepository;
        this._articleMapping = _articleMapping;
    }

    @Override
    public List<ArticleCategoryDTO> GetRootArticleCategorysByName(String name, ArticleType type) {
        List<ArticleCategory> filterData = _articleCategoryRepository.findAll(new Specification<ArticleCategory>() {
            private static final long serialVersionUID = 924729964744553669L;

            @Override
            public Predicate toPredicate(Root<ArticleCategory> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));
                if (!StringExtensions.isNullOrEmpty(name)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + name + "%")));
                }

                if (type != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("articleType"), type.getIndex())));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });

        List<Integer> ids = filterData.stream().map(m -> m.getId()).collect(Collectors.toList());
        if (ids.size() == 0)
            return new ArrayList<>();

        List<ArticleCategory> data = _articleCategoryRepository.findAllTreeNodesWithNestParentAndChildByIds(ArticleCategory.class, ids);
        return _articleCategoryMapping.from(data, new CycleAvoidingMappingContext());
    }

    @Override
    public ArticleCategoryDTO GetArticleCategoryById(int id) {
        ArticleCategory data = _articleCategoryRepository.findWithParentById(id);
        return _articleCategoryMapping.from(data, new CycleAvoidingMappingContext());
    }

    @Override
    public boolean SaveArticleCategory(ArticleCategoryDTO data) {
        int id = data.getId();
        String name = data.getText();
        Integer parentId = data.getParentId();

        ArticleCategory model = _articleCategoryMapping.to(data, new CycleAvoidingMappingContext());
        ArticleCategory parent = null;
        if (parentId != null) {
            parent = _articleCategoryRepository.findWithParentById(parentId);
            if (parent != null) model.setParentNode(parent);
        }

        boolean exist = ExistArticleCategoryName(id, name);
        if (exist)
            throw new IllegalArgumentException("名称【" + name + "】已存在,请重新输入！");
        try {
            if (model.getId() != 0) {
                if (parent != null)
                    model.setTreeCode(parent.getTreeCode() + _articleCategoryRepository.getTreeCodeSplitIdWithChar() + model.getId());

                _articleCategoryRepository.save(model);
                _articleCategoryRepository.flush();
            } else {
                _articleCategoryRepository.save(model);
                if (parent != null) {
                    model.setLevel(parent.getLevel() + 1);
                    model.setTreeCode(parent.getTreeCode() + _articleCategoryRepository.getTreeCodeSplitIdWithChar() + model.getId());
                } else {
                    model.setLevel(1);
                    model.setTreeCode(String.valueOf(model.getId()));
                }

                model.setLeaf(true);

                _articleCategoryRepository.save(model);
                _articleCategoryRepository.flush();
            }

            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public boolean RemoveArticleCategory(int id) {
        ArticleCategory model = _articleCategoryRepository.findWithParentById(id);
        model.setDeleted(true);
        _articleCategoryRepository.saveAndFlush(model);
        return true;
    }

    @Override
    public boolean ExistArticleCategoryName(int id, String name) {
        if (id == 0) {
            return _articleCategoryRepository.existsByName(name);
        } else {
            return _articleCategoryRepository.existsByIdAndName(id, name);
        }
    }

    /*------------------------------------------新闻动态-----------------------------------------------*/
    @Override
    public PaginatedBaseDTO<ArticleDTO> findPaginatedArticleByType(int pageIndex, int pageSize, ArticleType type) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Article> data = _articleRepository.findAll(new Specification<Article>() {
            private static final long serialVersionUID = 2931517314948070210L;

            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.and(criteriaBuilder.isFalse(root.get("isDeleted"))));
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), ArticleStatus.Approved.getIndex())));
                if (type != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("articleType"), type.getIndex())));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);

        long total = data.getTotalElements();
        List<ArticleDTO> rows = _articleMapping.from(data.getContent());
        return new PaginatedBaseDTO<>(pageIndex, pageSize, total, rows);
    }

    @Override
    public PaginatedBaseDTO<ArticleDTO> findPaginatedArticleByFilter(int pageIndex, int pageSize, String title, String content, String author, String authorEmail, ArticleStatus status, ArticleType type) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Article> data = _articleRepository.findAll(new Specification<Article>() {
            private static final long serialVersionUID = 2931517314948070210L;

            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringExtensions.isNullOrEmpty(title)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("title"), "%" + title + "%")));
                }
                if (type != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("articleType"), type.getIndex())));
                }
                if (status != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), status.getIndex())));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);

        long total = data.getTotalElements();
        List<ArticleDTO> rows = _articleMapping.from(data.getContent());
        return new PaginatedBaseDTO<>(pageIndex, pageSize, total, rows);
    }

    @Override
    public ArticleDTO GetArticleById(int id) {
        Optional<Article> data = _articleRepository.findById(id);
        return data.map(_articleMapping::from).orElse(null);
    }

    @Override
    public boolean PublishArticle(int id, Boolean isAggree, String content) {
        Optional<Article> optModel = _articleRepository.findById(id);
        if (!optModel.isPresent())
            throw new IllegalArgumentException("与Id【" + id + "】相匹配的文章不存在,请重新输入！");

        Article model = optModel.get();

        if (model.getStatus() == ArticleStatus.Draft.getIndex()
                || model.getStatus() == ArticleStatus.Disagree.getIndex()) {
            model.setStatus(ArticleStatus.AuditPending.getIndex());
        } else if (model.getStatus() == ArticleStatus.AuditPending.getIndex()) {
            if (isAggree != null && isAggree) {
                model.setStatus(ArticleStatus.Approved.getIndex());
            } else {
                model.setStatus(ArticleStatus.Disagree.getIndex());
            }
        } else if (model.getStatus() == ArticleStatus.Approved.getIndex()
                || model.getStatus() == ArticleStatus.Disagree.getIndex()) {
            model.setStatus(ArticleStatus.Trash.getIndex());
        }

        _articleRepository.saveAndFlush(model);
        return true;
    }

    @Override
    public boolean SaveArticle(ArticleDTO data) {
        int id = data.getId();
        String name = data.getTitle();

        Article model = _articleMapping.to(data);
        boolean exist = ExistArticleName(id, name);
        if (exist)
            throw new IllegalArgumentException("标题【" + name + "】已存在,请重新输入！");

        int categoryId = data.getArticleCategoryId();
        Optional<ArticleCategory> categoryOpt = _articleCategoryRepository.findById(categoryId);
        if (!categoryOpt.isPresent())
            throw new IllegalArgumentException("未找到Id【" + categoryId + "】的文章分类,请重新输入！");

        ArticleCategory category = categoryOpt.get();
        if (model.getId() == 0) {
            category.getArticles().add(model);
            model.setArticleCategory(category);
        } else {
            //编辑
            model.setArticleCategory(category);
        }

        _articleRepository.saveAndFlush(model);
        return true;
    }

    @Override
    public boolean RemoveArticle(int id) {
        Optional<Article> optModel = _articleRepository.findById(id);
        if (!optModel.isPresent())
            throw new IllegalArgumentException("与Id【" + id + "】相匹配的文章不存在,请重新输入！");

        Article model = optModel.get();
        model.setDeleted(true);
        _articleRepository.saveAndFlush(model);
        return true;
    }

    @Override
    public boolean ExistArticleName(int id, String name) {
        if (id == 0) {
            return _articleRepository.existsByName(name);
        } else {
            return _articleRepository.existsByIdAndName(id, name);
        }
    }
}
