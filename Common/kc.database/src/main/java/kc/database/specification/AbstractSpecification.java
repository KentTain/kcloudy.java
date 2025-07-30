package kc.database.specification;

import org.springframework.data.jpa.domain.Specification;

/**
 * 抽象动态查询条件
 *
 */
public abstract class AbstractSpecification<T, ATTR> implements Specification<T>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -254420073566863829L;
	/**
	 * 逻辑运算：等于
	 */
	public static final int LOGICAL_OPERATOR_EQUAL = 1;
	/**
	 * 逻辑运算：小于
	 */
	public static final int LOGICAL_OPERATOR_LESS = 2;
	/**
	 * 逻辑运算：大于
	 */
	public static final int LOGICAL_OPERATOR_GREATER = 3;
	/**
	 * 逻辑运算：小于等于
	 */
	public static final int LOGICAL_OPERATOR_LESS_EQUAL = 4;
	/**
	 * 逻辑运算：大于等于
	 */
	public static final int LOGICAL_OPERATOR_GREATER_EQUAL = 5;
	/**
	 * 逻辑运算：不等于
	 */
	public static final int LOGICAL_OPERATOR_NOT_EQUAL = 6;
	/**
	 * 逻辑运算：包含
	 */
	public static final int LOGICAL_OPERATOR_LIKE = 7;
	/**
	 * 逻辑运算：左包含
	 */
	public static final int LOGICAL_OPERATOR_STARTWITH = 8;
	/**
	 * 逻辑运算：右包含
	 */
	public static final int LOGICAL_OPERATOR_ENDWITH = 9;

	/**
	 * 逻辑运算：非空
	 */
	public static final int LOGICAL_OPERATOR_NOT_NULL = 10;
	/**
	 * 逻辑运算：In
	 */
	public static final int LOGICAL_OPERATOR_IN = 11;
    /**
     * 逻辑运算：包含（自定义）
     */
	public static final int LOGICAL_OPERATOR_LIKE_CUSTOM = 99;
	public static final int LOGICAL_OPERATOR_CUSTOM = 100;
	protected String attrName;
	protected ATTR attrValue;
	protected ATTR[] attrValues;
	protected int logicalOperator;
	/**
	 * 构造方法
	 * @param attrName			属性名称
	 * @param attrValue			属性值
	 * @param logicalOperator	逻辑运算符
	 */
	public AbstractSpecification(String attrName, ATTR attrValue, int logicalOperator) {
		super();
		this.attrName = attrName;
		this.attrValue = attrValue;
		this.logicalOperator = logicalOperator;
	}

	@SafeVarargs
	public AbstractSpecification(String attrName, int logicalOperator, ATTR ... attrValues) {
		super();
		this.attrName = attrName;
		this.attrValues = attrValues;
		if (this.attrValues.length > 0) {
			this.attrValue = this.attrValues[0];
		}
		this.logicalOperator = logicalOperator;
	}
}
