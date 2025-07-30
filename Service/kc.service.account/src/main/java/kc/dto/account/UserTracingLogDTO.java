package kc.dto.account;

import kc.dto.ProcessLogBaseDTO;
import kc.framework.base.ProcessLogBase;
import kc.model.account.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
//@NoArgsConstructor
public class UserTracingLogDTO extends ProcessLogBaseDTO {
	private static final long serialVersionUID = -3428273179520510974L;

}
