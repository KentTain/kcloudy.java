package kc.dto.account;

import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import kc.dto.TreeNodeSimpleDTO;
import kc.framework.extension.StringExtensions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class MenuNodeSimpleDTO extends TreeNodeSimpleDTO<MenuNodeSimpleDTO> implements java.io.Serializable {

	private static final long serialVersionUID = -3233830287859309119L;

	/**
	 * 请求地址的Area
	 */
	private String areaName;

	/**
	 * 请求地址的Action
	 */
	private String actionName;

	/**
	 * 请求地址的Controller
	 */
	private String controllerName;

	/**
	 * 菜单参数
	 */
	private String parameters;

	/**
	 * 小图标
	 */
	private String smallIcon;

	/**
	 * 大图标
	 */
	private String bigIcon;

	/**
	 * 访问地址
	 */
	private String url = getMenuURL();

	/**
	 * 是否为扩展页
	 */
	@com.fasterxml.jackson.annotation.JsonProperty("isExtPage")
	private boolean isExtPage;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 菜单所属的应用Id
	 */
	private UUID applicationId;

	/**
	 * 菜单所属的应用名称
	 */
	private String applicationName;
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
	/**
	 * 菜单所属的默认角色Id
	 */
	private String defaultRoleId;

	/**
	 * 菜单的权限控制Id
	 */
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

		MenuNodeSimpleDTO node = (MenuNodeSimpleDTO) o;

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
