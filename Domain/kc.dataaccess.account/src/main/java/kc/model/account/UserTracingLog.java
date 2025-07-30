package kc.model.account;

import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kc.framework.base.ProcessLogBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//@NoArgsConstructor
@javax.persistence.Entity
@Table(name="sys_UserTracingLog")
public class UserTracingLog  extends ProcessLogBase {
	private static final long serialVersionUID = -3428273179520510974L;

}
