package kc.framework.exceptions;

/**
 * 数据访问层异常类，用于封装业务逻辑层引发的异常，以供 UI 层抓取
 * 
 * @author 田长军
 *
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 6071462880189102408L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}
}
