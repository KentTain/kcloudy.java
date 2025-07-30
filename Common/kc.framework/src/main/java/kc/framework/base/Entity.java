package kc.framework.base;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import kc.framework.extension.DateExtensions;
import kc.framework.extension.StringExtensions;
import lombok.*;

/**
 * 实体模型基类：
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EqualsAndHashCode(callSuper = false)
//@AttributeOverride(name = "created_by", column = @Column(name = "CreatedBy"))
//@AttributeOverride(name = "created_date", column = @Column(name = "CreatedDate"))
//@AttributeOverride(name = "modified_by", column = @Column(name = "ModifiedBy"))
//@AttributeOverride(name = "modified_date", column = @Column(name = "ModifiedDate"))
//@AttributeOverride(name = "is_deleted", column = @Column(name = "IsDeleted"))
public abstract class Entity extends EntityBase implements Serializable {
	private static final long serialVersionUID = 3862416351900991824L;

	/**
	 * 是否删除：
	 */
	@Column(name = "IsDeleted")
	private boolean isDeleted;

	/**
	 * 创建人Id：
	 */
	@Column(name = "CreatedBy", length = 128)
	private String createdBy;

	/**
	 * 创建人：
	 */
	@Column(name = "CreatedName", length = 128)
	private String createdName;

	/**
	 * 创建时间：
	 */
	@Column(name = "CreatedDate")
	private Date createdDate = DateExtensions.getDateTimeUtcNow();

	/**
	 * 修改人Id：
	 */
	@Column(name = "ModifiedBy", length = 128)
	private String modifiedBy;

	/**
	 * 修改人：
	 */
	@Column(name = "ModifiedName", length = 128)
	private String modifiedName;

	/**
	 * 修改时间：
	 */
	@Column(name = "ModifiedDate")
	private Date modifiedDate = DateExtensions.getDateTimeUtcNow();
}
