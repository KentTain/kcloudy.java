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
@Table(name = Tables.CompanyProcessLog)
public class CompanyProcessLog extends ProcessLogBase {
	private static final long serialVersionUID = -3428273179520510974L;

	/**
	 * 公司名称
	 */
	@Column(name = "CompanyName", length = 1024)
	private String CompanyName;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "CompanyId", nullable = false, foreignKey = @ForeignKey(name="FK_ptl_CompanyProcessLog_ptl_CompanyInfo_CompanyId"))
	private CompanyInfo companyInfo;
}
