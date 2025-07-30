package kc.database.specification;

import javax.persistence.criteria.*;

/**
 * 
 */
public class IsNullSpecification<T> extends AbstractSpecification<T,Object> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2194782673180999790L;

	public IsNullSpecification(String attrName) {
        super(attrName, null, AbstractSpecification.LOGICAL_OPERATOR_CUSTOM);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Path<Object> path = SpecificationHelper.getPath(root, attrName);

        return cb.isNull(path);
    }
}
