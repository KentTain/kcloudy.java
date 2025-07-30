package kc.dto.offering;

import kc.dto.ProcessLogBaseDTO;
import kc.framework.base.ProcessLogBase;
import kc.model.offering.Category;
import kc.model.offering.Tables;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryOperationLogDTO extends ProcessLogBaseDTO {

	/**
	 * 商品名称
	 */
	private String categoryName;

}
