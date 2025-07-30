package kc.service.base;

import kc.service.enums.ServiceResultType;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpStatus;

@Getter
@Setter
public class ServiceResult<T> {
	public ServiceResult() {
		this.resultType = ServiceResultType.Success;
		this.httpStatusCode = org.apache.http.HttpStatus.SC_OK;
		this.success = true;
	}

	/**
	 * 初始化一个 定义返回消息、日志消息与附加数据的业务操作结果信息类 的新实例
	 * 
	 * @param resultType 业务操作结果类型
	 */
	public ServiceResult(ServiceResultType resultType) {
		this.resultType = resultType;
		this.httpStatusCode = resultType == ServiceResultType.Success
				? org.apache.http.HttpStatus.SC_OK
				: HttpStatus.SC_INTERNAL_SERVER_ERROR;
		this.success = resultType == ServiceResultType.Success;
	}
	
	/**
	 * 初始化一个 定义返回消息、日志消息与附加数据的业务操作结果信息类 的新实例
	 * 
	 * @param resultType 业务操作结果类型
	 * @param httpStatus 响应状态码
	 */
	public ServiceResult(ServiceResultType resultType, int httpStatus) {
		this(resultType);
		this.httpStatusCode = httpStatus;
		this.success = resultType == ServiceResultType.Success;
	}

	/**
	 * 初始化一个 定义返回消息、日志消息与附加数据的业务操作结果信息类 的新实例
	 * 
	 * @param resultType 业务操作结果类型
	 * @param message    业务返回消息
	 */
	public ServiceResult(ServiceResultType resultType, String message) {
		this(resultType);
		this.message = message;
	}
	
	/**
	 * 初始化一个 定义返回消息、日志消息与附加数据的业务操作结果信息类 的新实例
	 * 
	 * @param resultType 业务操作结果类型
	 * @param httpStatus 响应状态码
	 * @param message    业务返回消息
	 */
	public ServiceResult(ServiceResultType resultType, int httpStatus, String message) {
		this(resultType, httpStatus);
		this.message = message;
	}

	/**
	 * 初始化一个 定义返回消息、日志消息与附加数据的业务操作结果信息类 的新实例
	 * 
	 * @param resultType 业务操作结果类型
	 * @param message    业务返回消息
	 * @param result     业务返回数据
	 */
	public ServiceResult(ServiceResultType resultType, String message, T result) {
		this(resultType, message);
		this.result = result;
	}
	
	/**
	 * 初始化一个 定义返回消息、日志消息与附加数据的业务操作结果信息类 的新实例
	 * 
	 * @param resultType 业务操作结果类型
	 * @param httpStatus 响应状态码
	 * @param message    业务返回消息
	 * @param result     业务返回数据
	 */
	public ServiceResult(ServiceResultType resultType, int httpStatus, String message, T result) {
		this(resultType, httpStatus, message);
		this.result = result;
	}

	/**
	 * 初始化一个 定义返回消息、日志消息与附加数据的业务操作结果信息类 的新实例
	 * 
	 * @param resultType 业务操作结果类型
	 * @param message    业务返回消息
	 * @param logMessage 业务日志记录消息
	 */
	public ServiceResult(ServiceResultType resultType, String message, String logMessage) {
		this(resultType, message);
		this.logMessage = logMessage;
	}
	
	/**
	 * 初始化一个 定义返回消息、日志消息与附加数据的业务操作结果信息类 的新实例
	 * 
	 * @param resultType 业务操作结果类型
	 * @param httpStatus 响应状态码
	 * @param message    业务返回消息
	 * @param logMessage 业务日志记录消息
	 */
	public ServiceResult(ServiceResultType resultType, int httpStatus, String message, String logMessage) {
		this(resultType, httpStatus, message);
		this.logMessage = logMessage;
	}

	/**
	 * 初始化一个 定义返回消息、日志消息与附加数据的业务操作结果信息类 的新实例
	 * 
	 * @param resultType 业务操作结果类型
	 * @param message    业务返回消息
	 * @param logMessage 业务日志记录消息
	 * @param result     业务返回数据
	 */
	public ServiceResult(ServiceResultType resultType, String message, String logMessage, T result) {
		this(resultType, message, logMessage);
		this.result = result;
	}
	
	/**
	 * 初始化一个 定义返回消息、日志消息与附加数据的业务操作结果信息类 的新实例
	 * 
	 * @param resultType 业务操作结果类型
	 * @param httpStatus 响应状态码
	 * @param message    业务返回消息
	 * @param logMessage 业务日志记录消息
	 * @param result     业务返回数据
	 */
	public ServiceResult(ServiceResultType resultType, int httpStatus, String message, String logMessage, T result) {
		this(resultType, httpStatus, message, logMessage);
		this.result = result;
	}

	private int httpStatusCode;

	/**
	 * 获取或设置 操作结果类型
	 */
	private ServiceResultType resultType;

	/**
	 * 获取或设置 操作返回信息
	 */
	private String message;

	/**
	 * 是否成功
	 */
	private boolean success;

	/**
	 * 获取或设置 操作返回的日志消息，用于记录日志
	 */
	private String logMessage;

	/**
	 * 获取或设置 操作结果附加信息
	 */
	private T result;
}
