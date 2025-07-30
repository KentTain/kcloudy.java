package kc.dto.portal;

import kc.dto.ProcessLogBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfferingOperationLogDTO extends ProcessLogBaseDTO {
	private static final long serialVersionUID = -3428273179520510974L;

	/**
	 * 商品编码
	 */
	private String offeringCode;

	/**
	 * 商品名称
	 */
	private String offeringName;

}
