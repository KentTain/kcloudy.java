package kc.dto.account;

import javax.persistence.MappedSuperclass;

import kc.dto.TreeNodeDTO;
import kc.enums.OrganizationType;
import kc.framework.enums.BusinessType;
import kc.framework.enums.WorkflowBusStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class OrganizationDTO extends TreeNodeDTO<OrganizationDTO> implements java.io.Serializable {
	private static final long serialVersionUID = 4746312471509131747L;

	@com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
	private boolean isEditMode;
	
	/**
	 * 组织编号（SequenceName--Organization：ORG2018120100001）
	 */
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String organizationCode;

	/**
	 * 组织类型
	 * kc.enums.admin.OrganizationType
	 */
	private OrganizationType organizationType = OrganizationType.InternalCompany;
	
	private String organizationTypeString;
	public String getOrganizationTypeString(){
		if(organizationType == null)
			return OrganizationType.InternalCompany.getDesc();
		return organizationType.getDesc();
	}
	/**
	 * 业务类型
	 * kc.framework.enums.BusinessType
	 */
	private BusinessType businessType = BusinessType.None;

	private String businessTypeString;
	public String getBusinessTypeString(){
		if(businessType == null)
			return BusinessType.None.getDesc();
		return businessType.getDesc();
	}
	/**
	 * 状态
	 * kc.framework.enums.WorkflowBusStatus
	 */
	private WorkflowBusStatus status = WorkflowBusStatus.Approved;

	private String statusString;
	public String getStatusString(){
		if(status == null)
			return WorkflowBusStatus.Approved.getDesc();
		return status.getDesc();
	}

	/**
	 * 外部编号1
	 */
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String referenceId1;

	/**
	 * 外部编号2
	 */
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String referenceId2;

	/**
	 * 外部编号3
	 */
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String referenceId3;

	private Integer parentId;
	
	private String parentName;

	private String userNamesInfo;

	private String createdByAndName;

	private Set<UserDTO> users = new HashSet<>();
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		OrganizationDTO node = (OrganizationDTO) o;

		if (!organizationCode.equals(node.organizationCode))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (organizationCode != null ? organizationCode.hashCode() : 0);
		return result;
	}
}
