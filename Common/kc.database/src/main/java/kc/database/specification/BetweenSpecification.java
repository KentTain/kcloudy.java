package kc.database.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

/**
 * Between动态查询条件
 */
public class BetweenSpecification<T, ATTR extends Comparable<ATTR>> implements Specification<T>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5479998205601485115L;
	private String attrName;
	private ATTR lowerBound, upperBound;

	/**
	 * 构造方法
	 * @param attrName		属性名称
	 * @param lowerBound	属性值下界
	 * @param upperBound	属性值上界
	 */
	public BetweenSpecification(String attrName, ATTR lowerBound, ATTR upperBound) {
		super();
		this.attrName = attrName;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	/* (non-Javadoc)
	 * @see org.springframework.data.jpa.domain.Specification#toPredicate(javax.persistence.criteria.Root, javax.persistence.criteria.CriteriaQuery, javax.persistence.criteria.CriteriaBuilder)
	 */
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		Path<ATTR> path = SpecificationHelper.getPath(root, attrName);
		return cb.between(path, lowerBound, upperBound);
	}

}

