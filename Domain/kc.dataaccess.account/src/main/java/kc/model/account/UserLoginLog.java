package kc.model.account;

import kc.framework.base.ProcessLogBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity
@Table(name="sys_UserLoginLog")
public class UserLoginLog extends ProcessLogBase {
	private static final long serialVersionUID = -3428273179520510974L;

	@Column(name = "IPAddress", length = 50)
	private String ipAddress;

	@Column(name = "BrowserInfo", length = 100)
	private String browserInfo;
}
