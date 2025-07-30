package kc.dto.portal;

import kc.dto.PropertyAttributeBaseDTO;
import kc.enums.portal.CompanyExtInfoType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyExtInfoDTO extends PropertyAttributeBaseDTO {

	/**
	 * 扩展字段类型：
	 * 		0：常用信息；1：关于我们；2：企业文化；3：加入我们
	 */
	private CompanyExtInfoType companyExtInfoType;
	private String companyExtInfoTypeString;
	public String getCompanyExtInfoTypeString(){
		if(companyExtInfoType == null)
			return CompanyExtInfoType.BasicInfo.getDesc();
		return companyExtInfoType.getDesc();
	}

	private CompanyInfoDTO companyInfo;
}
