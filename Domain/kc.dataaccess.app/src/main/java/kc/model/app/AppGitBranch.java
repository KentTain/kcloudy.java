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
@Table(name=Tables.AppGitBranch)
public class AppGitBranch extends kc.framework.base.Entity {
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
	 * 分支类型：BranchType
	 * 			0：Team；1：Private
	 */
	@Column(name = "Type")
	private int type;

	/**
	 * 分支名称
	 */
	@Column(name = "Name", length = 64)
	private String name;

	@ManyToOne
	@JoinColumn(name = "AppGitId", nullable = false, foreignKey = @ForeignKey(name="FK_app_AppGitBranch_app_AppGit_AppGitId"))
	private AppGit appGit;

}
