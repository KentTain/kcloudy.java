package kc.framework.exceptions;

/**
 * 数据访问层异常类，用于封装数据访问层引发的异常，以供 业务逻辑层 抓取
 * 
 * @author 田长军
 *
 */
public class DataAccessException extends RuntimeException {

	private static final long serialVersionUID = -7708392553282560500L;

	public DataAccessException() {
		super();
	}

	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataAccessException(Throwable cause) {
		super(cause);
	}
}
