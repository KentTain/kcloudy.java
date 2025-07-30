package kc.service.base;

public class ErrorInfoResult implements java.io.Serializable {
	private static final long serialVersionUID = -7644965943068191814L;

	private int errCode;
	private String errMsg;

	public ErrorInfoResult(int code, String msg) {
		errCode = code;
		errMsg = msg;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	public String toString()
	{
		return "errCode: " + this.errCode + ", errMsg: " + this.errMsg;
	}
}
