package kc.model.portal;

import kc.framework.base.PropertyAttributeBase;
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
@Table(name = Tables.CompanyExtInfo)
public class CompanyExtInfo extends PropertyAttributeBase {

	/**
	 * 扩展字段类型：
	 * 		0：常用信息；1：关于我们；2：企业文化；3：加入我们
	 */
	@Column(name = "CompanyExtInfoType")
	private int companyExtInfoType;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "CompanyId", nullable = false, foreignKey = @ForeignKey(name="FK_ptl_CompanyExtInfo_ptl_CompanyInfo_CompanyId"))
	private CompanyInfo companyInfo;
}
