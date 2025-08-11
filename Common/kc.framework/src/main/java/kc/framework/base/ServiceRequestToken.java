package kc.framework.base;

import java.util.Dictionary;

import kc.framework.extension.StringExtensions;
import kc.framework.security.DesProvider;
import kc.framework.security.EncryptPasswordUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@lombok.extern.slf4j.Slf4j
public class ServiceRequestToken implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7061037037197778332L;
	private String _key = EncryptPasswordUtil.DEFAULT_Key;

	public ServiceRequestToken() {
	}

	public ServiceRequestToken(int userId, String memberId, String key) {
		if (!StringExtensions.isNullOrEmpty(key))
			this._key = key;
		this.UserId = userId;
		this.MemberId = memberId;
		this.RandomId = key;
		// this.ExpiredDateTime = expiredDateTime;
	}

	public ServiceRequestToken(String signatureJson, String key) {
		if (StringExtensions.isNullOrEmpty(signatureJson))
			return;
		try {
			if (!StringExtensions.isNullOrEmpty(key))
				this._key = key;
			String encrypt = DesProvider.DecryptString(signatureJson, _key);

			InitObject(encrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int UserId;
	public String MemberId;
	public String RandomId;

	// public DateTime? ExpiredDateTime;

	public String GetEncrptSignature() {
		try {
			String objStr = ObjToString();
			return DesProvider.EncryptString(objStr, _key);
		} catch (Exception e) {
			log.error("------GetEncrptSignature ", e);
			return "";
		}
	}

	public String IsValid(String memberId) {
		// if (HttpContext.Current != null)
		// {
		// var url = HttpContext.Current.Request.Url.Host;
		// var referrerurl = HttpContext.Current.Request.Headers["Origin"];
		// var userHostName = HttpContext.Current.Request.UserHostName;
		// var clientIp = HttpContext.Current.Request.UserHostAddress;
		// LogUtil.LogInfo("ServiceRequestToken--IsValid--memberId：" + memberId +
		// Environment.NewLine
		// + "userHostName：" + userHostName + Environment.NewLine
		// + "clientIp：" + clientIp + Environment.NewLine
		// + "url：" + url + Environment.NewLine
		// + "referrerurl：" + referrerurl + Environment.NewLine
		// + "ExpiredDateTime：" + ExpiredDateTime);
		// }

		String result = "";
		// if (ExpiredDateTime.HasValue && ExpiredDateTime < DateTime.UtcNow)
		// return String.Format("抱歉，服务账号（%s）已经超期了(过期时间：%s)，请重新申请。", memberId,
		// ExpiredDateTime.Value.ToLocalTime());

		boolean isValid = MemberId.equals(memberId);
		if (!isValid)
			return String.format("抱歉，服务账号(%s)未通过认证，请提供正确的服务账号。", memberId);

		// isValid = ApplicationDomain.Equals(url, StringComparison.OrdinalIgnoreCase);
		// if (!isValid)
		// return "抱歉，该域名(" + url + ")未被授权访问服务。";

		return result;
	}

	private String ObjToString() {
		return String.format("key=%s;UserId=%s;MemberId=%s;RandomId=%s", _key, UserId, MemberId, RandomId);
	}

	private boolean InitObject(String objStr) {
		try {
			Dictionary<String, String> dictObj = StringExtensions.keyValuePairFromConnectionString(objStr);
			_key = dictObj.get("key");
			UserId = Integer.parseInt(dictObj.get("userid"));
			MemberId = dictObj.get("memberid");
			RandomId = dictObj.get("randomid");
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
