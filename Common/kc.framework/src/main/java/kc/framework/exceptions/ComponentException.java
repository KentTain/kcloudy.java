package kc.framework.exceptions;

/**
 * 组件异常类
 * 
 * @author 田长军
 *
 */
public class ComponentException extends RuntimeException {

	private static final long serialVersionUID = 8654276929420558176L;

	public ComponentException() {
		super();
	}

	public ComponentException(String message) {
		super(message);
	}

	public ComponentException(String message, Throwable cause) {
		super(message, cause);
	}

	public ComponentException(Throwable cause) {
		super(cause);
	}
}
