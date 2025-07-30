package kc.dto;

import kc.framework.enums.RuleOperatorType;
import kc.framework.enums.RuleType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class RuleEntityBaseDTO extends EntityBaseDTO implements Serializable {

	private Integer id;

	private RuleType ruleType;

	private String fieldName;

	private String fieldDisplayName;

	private RuleOperatorType operatorType;

	private String fieldValue;


	@Override
	public String toString() {
		return ruleType.getDesc() + " " + fieldDisplayName
				+ "【" + fieldName + "】"
				+ operatorType.getDesc()
				+ " " + fieldValue;
	}
}
