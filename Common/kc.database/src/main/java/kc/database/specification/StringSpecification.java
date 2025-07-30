package kc.database.specification;

import javax.persistence.criteria.*;

/**
 * 字符串属性查询条件
 */
public class StringSpecification<T> extends AbstractSpecification<T, String> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -704998218320642872L;

    private int start, len;

    /**
     * 构造方法
     *
     * @param attrName        属性名称
     * @param attrValue       属性值
     * @param logicalOperator 逻辑运算符
     */
    public StringSpecification(String attrName, String attrValue, int logicalOperator) {
        this(attrName, logicalOperator, attrValue);
    }

    public StringSpecification(String attrName, int logicalOperator, String... attrValues) {
        super(attrName, logicalOperator, attrValues);
        this.start = 0;
        this.len = 0;
    }

    public StringSpecification(String attrName, int start, int length, int logicalOperator, String... attrValues) {
        super(attrName, logicalOperator, attrValues);
        this.start = start;
        this.len = length;
    }

    /* (non-Javadoc)
     * @see org.springframework.data.jpa.domain.Specification#toPredicate(javax.persistence.criteria.Root, javax.persistence.criteria.CriteriaQuery, javax.persistence.criteria.CriteriaBuilder)
     */
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Path<String> path = SpecificationHelper.getPath(root, attrName);
        Expression<String> expr = path;
        if (0 < start && 0 < len) {
            expr = cb.substring(path, start, len);
        } else {

        }
        switch (logicalOperator) {
            case StringSpecification.LOGICAL_OPERATOR_EQUAL: {
                return cb.equal(expr, attrValue);
            }
            case StringSpecification.LOGICAL_OPERATOR_GREATER: {
                return cb.greaterThan(expr, attrValue);
            }
            case StringSpecification.LOGICAL_OPERATOR_GREATER_EQUAL: {
                return cb.greaterThanOrEqualTo(expr, attrValue);
            }
            case StringSpecification.LOGICAL_OPERATOR_LESS: {
                return cb.lessThan(expr, attrValue);
            }
            case StringSpecification.LOGICAL_OPERATOR_LESS_EQUAL: {
                return cb.lessThanOrEqualTo(expr, attrValue);
            }
            case StringSpecification.LOGICAL_OPERATOR_NOT_EQUAL: {
                return cb.notEqual(expr, attrValue);
            }
            case StringSpecification.LOGICAL_OPERATOR_LIKE: {
                return cb.like(expr, "%" + attrValue + "%");
            }
            case StringSpecification.LOGICAL_OPERATOR_STARTWITH: {
                return cb.like(expr, attrValue + "%");
            }
            case StringSpecification.LOGICAL_OPERATOR_ENDWITH: {
                return cb.like(expr, "%" + attrValue);
            }
            case StringSpecification.LOGICAL_OPERATOR_NOT_NULL: {
                return cb.isNotNull(path);
            }
            case StringSpecification.LOGICAL_OPERATOR_LIKE_CUSTOM: {
                return cb.like(expr, attrValue);
            }
            case StringSpecification.LOGICAL_OPERATOR_IN: {
                CriteriaBuilder.In<String> predicate = cb.in(expr);
                for (String item : attrValues) {
                    predicate.value(item);
                }
                return predicate;
            }
            default:
                return null;
        }
    }
}

