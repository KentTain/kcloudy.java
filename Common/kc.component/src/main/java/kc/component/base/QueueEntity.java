package kc.component.base;

import kc.framework.base.EntityBase;
import kc.framework.enums.QueueType;
import kc.framework.extension.DateExtensions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
//@AttributeOverride(name = "created_by", column = @Column(name = "CreatedBy"))
//@AttributeOverride(name = "created_date", column = @Column(name = "CreatedDate"))
//@AttributeOverride(name = "modified_by", column = @Column(name = "ModifiedBy"))
//@AttributeOverride(name = "modified_date", column = @Column(name = "ModifiedDate"))
//@AttributeOverride(name = "is_deleted", column = @Column(name = "IsDeleted"))
public class QueueEntity extends EntityBase implements Serializable {
	private static final long serialVersionUID = 3862416351900991824L;

	@Column(name = "Id", length = 128)
	private String id = UUID.randomUUID().toString();

	@Column(name = "ErrorCount")
	private int errorCount = 0;

	@Column(name = "MaxProcessErrorCount")
	private int maxProcessErrorCount = 3;

	@Column(name = "QueueName", length = 128)
	private String queueName;

	@Column(name = "QueueType")
	private QueueType queueType = QueueType.Redis;

	@Column(name = "CreatedDate")
	private Date createdDate = DateExtensions.getDateTimeUtcNow();

	@Column(name = "ErrorMessage")
	private String errorMessage;

	@Column(name = "Tenant", length = 128)
	private String tenant;
	/**
	 * 队列消息成功执行后，是否要删除的字段（NotDeletedWhenSuccess），
	 * 以控制队列的是否一致存在，直到调用者进行删除，默认为：false
	 */
	@Column(name = "IsManuallyDelete")
	private boolean isManuallyDelete;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof QueueEntity))
			return false;
		if (!super.equals(o))
			return false;

		QueueEntity entity = (QueueEntity) o;

		if (!Objects.equals(tenant, entity.tenant))
			return false;
		if (!Objects.equals(id, entity.id))
			return false;
		if (!Objects.equals(queueName, entity.queueName))
			return false;
		if (!Objects.equals(queueType, entity.queueType))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (tenant != null ? tenant.hashCode() : 0 );
		result = 31 * result + (id != null ? id.hashCode() : 0 );
		result = 31 * result + (queueName != null ? queueName.hashCode() : 0 );
		result = 31 * result + queueType.hashCode();
		return result;
	}
}
