package kc.dto.message;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.MappedSuperclass;

import kc.dto.EntityDTO;
import kc.enums.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class MemberRemindMessageDTO extends EntityDTO  implements Serializable {

	private int id;
	/**
	 * 标题
	 */
	private String messageTitle;
	/**
	 * 内容
	 */
	private String messageContent;

	/**
	 * 消息模板Id
	 */
	private String typeId;
	/**
	 * 消息模板名称
	 */
	private String typeName;

	/**
	 * 阅读状态
	 */
	private MessageStatus status;
	private String statusString;
	public String getStatusString() { return status.getDesc(); }

	/**
	 * 阅读时间
	 */
	private Date readDate;

	/**
	 * 阅读人Id
	 */
	private String userId;

	/**
	 * 阅读人
	 */
	private String userName;

	private UUID applicationId;

	private String applicationName;
}
