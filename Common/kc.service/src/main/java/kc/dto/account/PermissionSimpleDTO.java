package kc.dto.account;

import java.util.Objects;
import java.util.UUID;

import kc.dto.TreeNodeSimpleDTO;
import kc.framework.enums.ResultType;
import kc.framework.extension.StringExtensions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class PermissionSimpleDTO extends TreeNodeSimpleDTO<PermissionSimpleDTO> implements java.io.Serializable {

	private static final long serialVersionUID = -6962834721717966919L;

	private boolean isEditMode;

	private String areaName;
    
	private String controllerName;
    
	private String actionName;
    
	private String parameters ;
	/**
	 * 业务类型
	 * kc.framework.enums.BusinessType
	 */
	private ResultType resultType;

	private String resultTypeString = resultType.getDesc();

	private String description;
	
	/**
	 * 父节点的名称
	 */
	private String parentName;
	
	private String defaultRoleId ;

	private UUID applicationId;

	private String applicationName;

	private String authorityId;

	/**
	 * 访问地址
	 */
	private String url = getPermissionURL();

	public String getPermissionURL()
	{
		if (StringExtensions.isNullOrEmpty(controllerName) && StringExtensions.isNullOrEmpty(actionName))
		{
			return "";
		}

		if(StringExtensions.isNullOrEmpty(areaName))
			return String.format("/%s/%s", controllerName.trim(), actionName.trim());

		return String.format("/%s/%s/%s", areaName.trim(), controllerName.trim(), actionName.trim());
	}

    @Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		PermissionSimpleDTO node = (PermissionSimpleDTO) o;

		if (!Objects.equals(areaName, node.areaName))
			return false;
		if (!Objects.equals(controllerName, node.controllerName))
			return false;
		if (!Objects.equals(actionName, node.actionName))
			return false;
		if (!Objects.equals(applicationId, node.applicationId))
			return false;
		if (!Objects.equals(authorityId, node.authorityId))
			return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (areaName != null ? areaName.hashCode() : 0 );
		result = 31 * result + (controllerName != null ? controllerName.hashCode() : 0 );
		result = 31 * result + (actionName != null ? actionName.hashCode() : 0 );
		result = 31 * result + (applicationId != null ? applicationId.hashCode() : 0 );
		result = 31 * result + (authorityId != null ? authorityId.hashCode() : 0 );
		return result;
	}
}
