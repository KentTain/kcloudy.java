package kc.framework.exceptions;

/**
 * 数据访问层异常类，用于封装业务逻辑层引发的异常，直接返回到View层错误类
 * 
 * @author 田长军
 *
 */
public class BusinessPromptException extends RuntimeException {

	private static final long serialVersionUID = 6001500454189600583L;

	public BusinessPromptException() {
		super();
	}

	public BusinessPromptException(String message) {
		super(message);
	}

	public BusinessPromptException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessPromptException(Throwable cause) {
		super(cause);
	}
}
