package kc.model.app;

import kc.framework.base.PropertyBase;
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
@Table(name=Tables.AppGit)
@NamedEntityGraph(name = "Graph.AppGit.AppBranches",
		attributeNodes = {@NamedAttributeNode("gitBranches")})
@NamedEntityGraph(name = "Graph.AppGit.AppUsers",
		attributeNodes = {@NamedAttributeNode("gitUsers")})
public class AppGit extends kc.framework.base.Entity {
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
	 * Git仓库地址
	 */
	@Column(name = "GitAddress", length = 512)
	private String gitAddress;

	/**
	 * 主开发分支
	 */
	@Column(name = "GitMainBranch", length = 64)
	private String gitMainBranch;
	/**
	 * Git仓库Token
	 */
	@Column(name = "GitToken", length = 512)
	private String gitToken;

	/**
	 * 是否使用
	 */
	@Column(name = "IsActived")
	private Boolean isActived = false;
	/**
	 * 是否系统分配
	 */
	@Column(name = "IsSystem")
	private Boolean isSystem = false;
	/**
	 * 描述
	 */
	@Column(name = "Description", length = 512)
	private String description;

	@ManyToOne
	@JoinColumn(name = "ApplicationId", nullable = false, foreignKey = @ForeignKey(name="FK_app_AppGit_app_Application_ApplicationId"))
	private Application application;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "appGit")
	private List<AppGitBranch> gitBranches = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "appGit")
	private List<AppGitUser> gitUsers = new ArrayList<>();

}
