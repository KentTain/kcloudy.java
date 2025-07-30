package kc.database.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.*;

import org.springframework.data.jpa.domain.Specification;

public class GroupSpecification<T> implements Specification<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 616191478192989861L;

	@Override
	public Specification<T> and(Specification<T> other) {
		return null;
	}

	@Override
	public Specification<T> or(Specification<T> other) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		@SuppressWarnings("rawtypes")
		List eList = new ArrayList<>();
		eList.add(root.<String>get("detectSn"));

		query.groupBy(eList);
//        query.having(cb.gt( cb.count(root), 1));
		return query.getRestriction();
	}
}
