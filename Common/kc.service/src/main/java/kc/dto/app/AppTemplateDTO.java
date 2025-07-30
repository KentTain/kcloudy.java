package kc.dto.app;

import kc.dto.EntityDTO;
import kc.enums.TemplateType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppTemplateDTO extends EntityDTO {

	@com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
	private boolean isEditMode = false;

	/**
	 * Id
	 */
	@Length(min = 1, max = 64, message = "Id不能超过64个字符")
	private String id;

	/**
	 * 模板类型：kc.enums.TemplateType
	 * 			1：BackEnd；2：FrontEnd；4：Mobile；8：H5
	 */
	private TemplateType type;
	/**
     * 模板名称
	 */
	@Length(min = 1, max = 100, message = "模板名称不能超过100个字符")
	private String name;
	/**
	 * 模板版本号
	 */
	@Length(min = 0, max = 32, message = "模板版本号不能超过20个字符")
	private String version;
	/**
	 * Git仓库地址
	 */
	@Column(name = "GitAddress", length = 512)
	private String gitAddress;
	/**
	 * Git是否公开
	 */
	@Column(name = "IsPublic")
	private Boolean isPublic;
	/**
	 * Git用户账号
	 */
	@Length(min = 0, max = 50, message = "Git用户账号不能超过50个字符")
	private String gitAccount;
	/**
	 * Git密码
	 */
	@Length(min = 0, max = 200, message = "Git密码不能超过200个字符")
	private String gitPassword;
	/**
	 * 仓库Token
	 */
	@Length(min = 0, max = 500, message = "Git仓库Token不能超过500个字符")
	private String gitToken;
	/**
	 * Git上TagId
	 */
	@Length(min = 0, max = 128, message = "Git上TagId不能超过128个字符")
	private String gitTagId;
	/**
	 * Git上Tag的哈希Id
	 */
	@Length(min = 0, max = 128, message = "Git上Tag的哈希Id不能超过128个字符")
	private String gitShaId;
	/**
	 * 是否平台
	 */
	@Column(name = "IsPlatform")
	private Boolean isPlatform;
	/**
	 * 描述
	 */
	@Column(name = "Description", length = 512)
	private String description;

}
