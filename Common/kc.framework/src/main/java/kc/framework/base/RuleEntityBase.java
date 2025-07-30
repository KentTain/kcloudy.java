package kc.framework.base;

import kc.framework.extension.DateExtensions;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public abstract class RuleEntityBase extends EntityBase implements Serializable {

	@Column(name = "Id")
	private int id;

	/**
	 * 规则类型：kc.framework.enums.RuleType
	 */
	@Column(name = "RuleType")
	private int ruleType;

	/**
	 * 字段名
	 */
	@Column(name = "FieldName", length = 128)
	private String fieldName;


	/**
	 * 操作类型：kc.framework.enums.RuleOperatorType
	 */
	@Column(name = "OperatorType")
	private int operatorType;

	/**
	 * 字段值
	 */
	@Column(name = "FieldValue", length = 256)
	private String fieldValue;

}
