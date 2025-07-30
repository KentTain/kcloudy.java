package kc.framework.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Inheritance;
import javax.persistence.MappedSuperclass;

import kc.framework.enums.ProcessLogType;
import kc.framework.extension.DateExtensions;
import lombok.*;

/**
 * @author tianc
 * 日志基类
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
//@Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
public abstract class ProcessLogBase extends EntityBase implements java.io.Serializable{
	private static final long serialVersionUID = 2702674487192612828L;
	
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ProcessLogId", unique=true, nullable=false)
	private int processLogId;
	 /**
     * 当前操作类型
     */
	@Column(name = "Type")
	private int type = ProcessLogType.Success.getIndex();
    /**
     * 当前操作对象的UserId
     */
	@Column(name = "OperatorId", length = 128)
	private String operatorId ;

    /**
     * 当前操作对象的DisplayName
     */
	@Column(name = "Operator", length = 50)
	private String operator;
    /**
     * 当前操作时间
     */
	@Column(name = "OperateDate")
	private Date operateDate = DateExtensions.getDateTimeNow();
    /**
     * 日志内容
     */
	@Column(name = "Remark", length = 4000)
	private String remark;

}
