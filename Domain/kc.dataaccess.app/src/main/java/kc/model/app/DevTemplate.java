package kc.model.app;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name=Tables.DevTemplate)
public class DevTemplate extends kc.framework.base.Entity {
	/**
	 * Id
	 */
	@Id
	@GeneratedValue(generator="system_uuid")
	@GenericGenerator(name="system_uuid",strategy="uuid")
	//@GeneratedValue(generator = "uuid2")
	//@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "Id")
	private String id;

	/**
	 * 模板类型：kc.enums.TemplateType
	 * 			1：BackEnd；2：FrontEnd；4：Mobile；8：H5
	 */
	@Column(name = "Type")
	private int type;
	/**
     * 模板名称
	 */
	@Column(name = "Name", length = 128)
	private String name;
	/**
	 * 模板版本号
	 */
	@Column(name = "Version", length = 32)
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
	@Column(name = "GitAccount", length = 64)
	private String gitAccount;
	/**
	 * Git密码
	 */
	@Column(name = "GitPassword", length = 256)
	private String gitPassword;
	/**
	 * 仓库Token
	 */
	@Column(name = "GitToken", length = 512)
	private String gitToken;
	/**
	 * Git上TagId
	 */
	@Column(name = "GitTagId", length = 128)
	private String gitTagId;
	/**
	 * Git上Tag的哈希Id
	 */
	@Column(name = "GitShaId", length = 128)
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
