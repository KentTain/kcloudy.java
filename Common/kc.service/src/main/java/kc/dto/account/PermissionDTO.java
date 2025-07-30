package kc.dto.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import kc.dto.TreeNodeDTO;
import kc.framework.enums.BusinessType;
import kc.framework.enums.ResultType;
import kc.framework.extension.StringExtensions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.MappedSuperclass;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class PermissionDTO extends TreeNodeDTO<PermissionDTO> implements java.io.Serializable {

	private static final long serialVersionUID = 6362006093819856408L;

	@com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
	private boolean isEditMode;

	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String areaName;
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String controllerName;
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String actionName;
	@Length(min = 0, max = 512, message = "属性名称不能超过512个字符")
	private String parameters ;

	/**
	 * 业务类型
	 * kc.framework.enums.BusinessType
	 */
	private BusinessType resultType = BusinessType.None;
	private String resultTypeString = resultType.getDesc();

	@Length(min = 0, max = 4000, message = "属性名称不能超过4000个字符")
	private String description;
	@Length(min = 0, max = 128, message = "属性名称不能超过128个字符")
	private String defaultRoleId ;

	private UUID applicationId;

	private String applicationName;

	private String authorityId;
	
	private Integer parentId;
	/**
	 * 父节点的名称
	 */
	private String parentName;

	/**
	 * 访问地址
	 */
	private String url = getPermissionURL();
    
	private List<RoleDTO> roles = new ArrayList<RoleDTO>();

	public String getPermissionURL()
	{
		if (StringExtensions.isNullOrEmpty(controllerName) && StringExtensions.isNullOrEmpty(actionName))
		{
			return "";
		}

		if (StringExtensions.isNullOrEmpty(areaName))
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

		PermissionDTO node = (PermissionDTO) o;

		if (!Objects.equals(areaName, node.areaName))
			return false;
		if (!Objects.equals(controllerName, node.controllerName))
			return false;
		if (!Objects.equals(actionName, node.actionName))
			return false;
		if (applicationId != null ? !applicationId.equals(node.applicationId) : node.applicationId != null)
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
