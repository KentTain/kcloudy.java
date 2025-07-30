package kc.database.specification;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

/**
 * 相等动态查询条件
 *
 */
public class EqualSpecification<T, ATTR> implements Specification<T>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4489965636179071957L;
	private String fieldName;
	private ATTR fieldValue;

	public EqualSpecification(String fieldName, ATTR fieldValue) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		Path<ATTR> path = SpecificationHelper.getPath(root, fieldName);
		return cb.equal(path, fieldValue);
	}

}