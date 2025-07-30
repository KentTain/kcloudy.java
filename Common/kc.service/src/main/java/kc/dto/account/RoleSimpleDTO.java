package kc.dto.account;

import java.util.Date;
import java.util.List;

import javax.persistence.MappedSuperclass;

import kc.dto.EntityBaseDTO;
import kc.framework.enums.BusinessType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class RoleSimpleDTO extends EntityBaseDTO implements java.io.Serializable {

	private static final long serialVersionUID = -5382217821353495415L;

	private boolean isEditMode;

	private String roleId;

	private String roleName;

	private String displayName;

	private String description;

	private boolean checked;

	private boolean isSystemRole;

	private BusinessType businessType;

	private String businessTypeString = businessType.getDesc();

	private List<String> userIds;

	//private List<MenuNodeSimpleDTO> menuNodes;

	//private List<PermissionSimpleDTO> permissions;

	private boolean isDeleted;

	private String createdBy;

	private Date createdDate;
}
