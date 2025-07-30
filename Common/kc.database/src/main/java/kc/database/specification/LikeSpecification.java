package kc.database.specification;

/**
 * Like动态查询条件
 *
 */
public class LikeSpecification<T> extends StringSpecification<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1157745458817733461L;

	public LikeSpecification(String attrName, String attrValue) {
		super(attrName, attrValue, StringSpecification.LOGICAL_OPERATOR_LIKE);
	}

}
