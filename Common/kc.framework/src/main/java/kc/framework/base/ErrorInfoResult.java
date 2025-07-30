package kc.framework.base;

public class ErrorInfoResult implements java.io.Serializable {
	private static final long serialVersionUID = -7644965943068191814L;

	private int errcode;
	private String errmsg;

	public ErrorInfoResult(int code, String msg) {
		errcode = code;
		errmsg = msg;
	}

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	
	public String toString()
	{
		return "errcode: " + this.errcode + ", errmsg: " + this.errmsg;
	}
}
