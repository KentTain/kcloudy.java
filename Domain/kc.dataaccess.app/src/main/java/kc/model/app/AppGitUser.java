package kc.model.app;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name=Tables.AppGitUser)
public class AppGitUser extends kc.framework.base.Entity {
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
	 * 用户Id
	 */
	@Column(name = "UserId", length = 64)
	private String userId;

	/**
	 * 是否使用Token访问
	 */
	@Column(name = "IsUseToken")
	private Boolean isUseToken = false;

	/**
	 * Git用户账号
	 */
	@Column(name = "UserAccount", length = 64)
	private String userAccount;

	/**
	 * Git密码
	 */
	@Column(name = "UserPassword", length = 256)
	private String userPassword;

	/**
	 * GitToken
	 */
	@Column(name = "UserToken", length = 512)
	private String userToken;

	/**
	 * 是否管理员账号
	 */
	@Column(name = "IsAdmin")
	private Boolean isAdmin;

	@ManyToOne
	@JoinColumn(name = "AppGitId", nullable = false, foreignKey = @ForeignKey(name="FK_app_AppGitUser_app_AppGit_AppGitId"))
	private AppGit appGit;

}
