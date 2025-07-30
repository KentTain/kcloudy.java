package kc.dto.message;

import java.util.Date;

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
public class MemberMessageStatusDTO extends EntityDTO {

	private static final long serialVersionUID = -7737259680401285781L;

	public int Id;

	/**
	 * 状态
	 */
	public MessageStatus Status;

	/**
	 * 阅读时间
	 */
	public Date ReadDate;

	/**
	 * UserId
	 */
	public String UserId;

	/**
	 * 消息ID
	 */
	public int MessageId;

	/**
	 * 标题
	 */
	public String Title;

	/**
	 * 类型名称
	 */
	public String TypeName;
	/**
	 * 消息
	 */
	public String MemberRemindMessage;
}
