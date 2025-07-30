package kc.dto.app;

import kc.dto.ProcessLogBaseDTO;
import kc.enums.app.AppLogType;
import lombok.*;

import javax.validation.constraints.Max;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApplicationLogDTO extends ProcessLogBaseDTO {

	private AppLogType appLogType;

	@Max(128)
	private String referenceId;

	@Max(200)
	private String referenceName;

	public String refObjectJson ;
}
