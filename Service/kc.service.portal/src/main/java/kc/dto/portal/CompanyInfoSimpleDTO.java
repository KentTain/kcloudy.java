package kc.dto.portal;

import kc.dto.EntityBaseDTO;
import kc.enums.portal.BusinessModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class CompanyInfoSimpleDTO extends EntityBaseDTO implements java.io.Serializable {

    private int companyId;
    /**
     * 公司代码
     */
    @Length(min = 0, max = 128, message = "公司代码不能超过128个字符")
    private String companyCode;
    /**
     * 公司名
     */
    @Length(min = 0, max = 1024, message = "公司名不能超过1000个字符")
    @NotBlank(message = "公司名不能为空")
    private String companyName;

    /**
     * 行业类型Id
     */
    private int industryId;
    /**
     * 行业类型
     */
    @Length(min = 0, max = 1024, message = "公司名不能超过1000个字符")
    private String industryName;
    /**
     * 企业经营模式
     */
    private BusinessModel businessModel;
    private String businessModelString;
    public String getBusinessModelString(){
        if(businessModel == null)
            return BusinessModel.ProductionFoundry.getDesc();
        return businessModel.getDesc();
    }

    /**
     * 联系人UserId
     */
    private String contactId;

    /**
     * 联系人名称
     */
    @Length(min = 0, max = 50, message = "公司名不能超过50个字符")
    private String contactName;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        CompanyInfoSimpleDTO node = (CompanyInfoSimpleDTO) o;

        if (companyId != node.companyId)
            return false;
        if (!Objects.equals(companyCode, node.companyCode))
            return false;
        if (!Objects.equals(companyName, node.companyName))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + companyId;
        result = 31 * result + (companyCode != null ? companyCode.hashCode() : 0);
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        return result;
    }
}
