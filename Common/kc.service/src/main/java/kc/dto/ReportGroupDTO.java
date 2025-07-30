package kc.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public class ReportGroupDTO extends EntityBaseDTO implements Serializable {

	private String key1;
	private String keyName1;

	private String key2;
	private String keyName2;

	private String key3;
	private String keyName3;

	private String key4;
	private String keyName4;

	private String key5;
	private String keyName5;

	private String key6;
	private String keyName6;

	private BigDecimal value;

}
