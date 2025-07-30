package kc.framework.exceptions;

public class BusinessApiException extends RuntimeException {

	private static final long serialVersionUID = 7401171907147707116L;

	public BusinessApiException() {
        super();
    }
	
	public BusinessApiException(String message) {
        super(message);
    }
	
	public BusinessApiException(String message, Throwable cause) {
        super(message, cause);
    }
	
	public BusinessApiException(Throwable cause) {
        super(cause);
    }
}
