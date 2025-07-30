package kc.dto.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.MappedSuperclass;

import kc.dto.EntityBaseDTO;
import kc.framework.enums.BusinessType;
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
public class RoleDTO extends EntityBaseDTO implements java.io.Serializable {

	private static final long serialVersionUID = -5382217821353495415L;

	@com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
	private boolean isEditMode;

	private String roleId;

	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String roleName;

	@Length(min = 0, max = 256, message = "属性名称不能超过256个字符")
	private String displayName;

	@Length(min = 0, max = 4000, message = "属性名称不能超过4000个字符")
	private String description;

	private boolean checked;

	private boolean isSystemRole;

	private BusinessType businessType = BusinessType.None;

	private String businessTypeString = businessType.getDesc();

	private List<String> userIds = new ArrayList<String>();

	private List<UserDTO> users = new ArrayList<UserDTO>();

	private List<MenuNodeDTO> menuNodes = new ArrayList<MenuNodeDTO>();

	private List<PermissionDTO> permissions = new ArrayList<PermissionDTO>();

	private boolean isDeleted;

	private String createdBy;

	private String createdName;

	private Date createdDate;

	private String modifiedBy;

	private String modifiedName;

	private Date modifiedDate;
}
