package kc.dto.portal;

import kc.dto.ProcessLogBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProcessLogDTO extends ProcessLogBaseDTO {

	/**
	 * 公司名称
	 */
	private String CompanyName;

	private int CompanyId;
}
