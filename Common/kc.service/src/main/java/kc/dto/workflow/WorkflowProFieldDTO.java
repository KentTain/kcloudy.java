package kc.dto.workflow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class WorkflowProFieldDTO extends DefFieldBaseDTO<WorkflowProFieldDTO> implements java.io.Serializable {

	private UUID processId;

	private WorkflowProcessDTO process;

}
