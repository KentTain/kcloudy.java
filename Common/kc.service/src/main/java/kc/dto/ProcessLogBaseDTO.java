package kc.dto;

import java.util.Date;
import java.util.Objects;

import javax.persistence.MappedSuperclass;

import kc.framework.enums.ProcessLogType;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public class ProcessLogBaseDTO extends EntityBaseDTO implements java.io.Serializable {

	private int processLogId;
	/**
	 * 当前操作类型
	 */
	private ProcessLogType type = ProcessLogType.Success;
	private String typeString;
	public String getTypeString(){
		if(type == null)
			return ProcessLogType.Success.getDesc();
		return type.getDesc();
	}
	/**
	 * 当前操作对象的UserId
	 */
	@Length(min = 0, max = 128, message = "操作对象的UserId不能超过128个字符")
	private String operatorId;

	/**
	 * 当前操作对象的DisplayName
	 */
	@Length(min = 0, max = 128, message = "操作对象的姓名不能超过128个字符")
	private String operator;
	/**
	 * 当前操作时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
	@com.fasterxml.jackson.annotation.JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
	private Date operateDate;
	/**
	 * 日志内容
	 */
	@Length(min = 0, max = 4000, message = "日志内容不能超过4000个字符")
	private String remark;

}