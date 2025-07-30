package kc.dto.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.MappedSuperclass;

import kc.dto.TreeNodeDTO;
import kc.framework.extension.StringExtensions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class MenuNodeDTO extends TreeNodeDTO<MenuNodeDTO> implements java.io.Serializable {

	private static final long serialVersionUID = 8837645220903070780L;

	@com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
	private boolean isEditMode = false;
	
	/**
	 * 请求地址的Area
	 */
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String areaName;

	/**
	 * 请求地址的Action
	 */
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String actionName;

	/**
	 * 请求地址的Controller
	 */
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String controllerName;

	/**
	 * 菜单参数
	 */
	@Length(min = 0, max = 512, message = "属性名称不能超过512个字符")
	private String parameters;

	/**
	 * 小图标
	 */
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String smallIcon;

	/**
	 * 大图标
	 */
	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String bigIcon;

	/**
	 * 访问地址
	 */
	private String url;

	/**
	 * 是否为扩展页
	 */
	@com.fasterxml.jackson.annotation.JsonProperty("isExtPage")
	private boolean isExtPage = false;

	/**
	 * 描述
	 */
	@Length(min = 0, max = 4000, message = "属性名称不能超过4000个字符")
	private String description;

	/**
	 * 菜单所属的应用Id
	 */
	private UUID applicationId;

	/**
	 * 菜单所属的应用名称
	 */
	private String applicationName;

	private Integer parentId;
	/**
	 * 父节点的名称
	 */
	private String parentName;

	/**
	 * 租户类型，值定义：kc.framework.enums.TenantType
	 */
	private int tenantType;
	/**
	 * 租户版本，值定义：kc.framework.enums.TenantVersion
	 */
	private int version;

	private List<RoleDTO> roles = new ArrayList<RoleDTO>();

	/**
	 * 菜单所属的默认角色Id
	 */
	@Length(min = 0, max = 128, message = "属性名称不能超过128个字符")
	private String defaultRoleId;

	/**
	 * 菜单的权限控制Id
	 */
	@Length(min = 0, max = 128, message = "属性名称不能超过128个字符")
	private String authorityId;
	
	public String getMenuURL()
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

		MenuNodeDTO node = (MenuNodeDTO) o;

		if (!Objects.equals(areaName, node.areaName))
			return false;
		if (!Objects.equals(controllerName, node.controllerName))
			return false;
		if (!Objects.equals(actionName, node.actionName))
			return false;
		if (!Objects.equals(authorityId, node.authorityId))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (areaName != null ? areaName.hashCode() : 0);
		result = 31 * result + (controllerName != null ? controllerName.hashCode() : 0);
		result = 31 * result + (actionName != null ? actionName.hashCode() : 0);
		result = 31 * result + (authorityId != null ? authorityId.hashCode() : 0);
		return result;
	}
}