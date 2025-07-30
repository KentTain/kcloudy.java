package kc.database.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

/**
 *
 */
public class InSpecification<T, ATTR> implements Specification<T>
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8206809082507367216L;
	private String attrName;
    private ATTR[] values;
    public InSpecification(String attrName, ATTR[] values) {
        this.attrName = attrName;
        this.values = values;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Path<ATTR> path = SpecificationHelper.getPath(root, attrName);
        CriteriaBuilder.In<ATTR> predicate = cb.in(path);
        for(ATTR item : values) {
            predicate.value(item);
        }
        return predicate;
    }
}