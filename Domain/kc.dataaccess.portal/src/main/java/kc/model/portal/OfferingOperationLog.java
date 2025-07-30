package kc.model.portal;

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
@Entity
@Table(name = Tables.OfferingOperationLog)
public class OfferingOperationLog extends ProcessLogBase {
	private static final long serialVersionUID = -3428273179520510974L;

	/**
	 * 商品编码
	 */
	@Column(name = "OfferingCode", length = 16)
	private String offeringCode;

	/**
	 * 商品名称
	 */
	@Column(name = "OfferingName", length = 256)
	private String offeringName;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "OfferingId", nullable = false, foreignKey = @ForeignKey(name="FK_ptl_OfferingOperationLog_ptl_Offering_OfferingId"))
	private Offering offering;
}
