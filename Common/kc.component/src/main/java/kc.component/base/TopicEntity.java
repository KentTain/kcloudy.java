package kc.component.base;

import kc.framework.base.EntityBase;
import kc.framework.enums.QueueType;
import kc.framework.enums.ServiceBusType;
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
public class TopicEntity<T> extends EntityBase implements Serializable {

	@Column(name = "Id", length = 128)
	private String id = UUID.randomUUID().toString();

	@Column(name = "TopicName", length = 128)
	private String topicName;

	@Column(name = "TopicType")
	private ServiceBusType topicType = ServiceBusType.Redis;

	@Column(name = "CreatedDate")
	private Date createdDate = DateExtensions.getDateTimeUtcNow();

	@Column(name = "Tenant", length = 128)
	private String tenant;

	@Column(name = "CallBackUrl")
	private String callBackUrl;

    private T topicContext;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof TopicEntity))
			return false;
		if (!super.equals(o))
			return false;

		TopicEntity entity = (TopicEntity) o;

		if (!Objects.equals(tenant, entity.tenant))
			return false;
		if (!Objects.equals(id, entity.id))
			return false;
		if (!Objects.equals(topicName, entity.topicName))
			return false;
		if (!Objects.equals(topicType, entity.topicType))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (tenant != null ? tenant.hashCode() : 0 );
		result = 31 * result + (id != null ? id.hashCode() : 0 );
		result = 31 * result + (topicName != null ? topicName.hashCode() : 0 );
		result = 31 * result + topicType.hashCode();
		return result;
	}
}
