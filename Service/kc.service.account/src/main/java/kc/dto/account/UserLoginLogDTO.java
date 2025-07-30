package kc.dto.account;

import kc.dto.ProcessLogBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginLogDTO extends ProcessLogBaseDTO {
	private static final long serialVersionUID = -3428273179520510974L;

	private String ipAddress;

	private String browserInfo;
}
