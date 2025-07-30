package kc.dto.search;

import kc.dto.EntityBaseDTO;
import lombok.*;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class WorkflowTaskSearchDTO extends EntityBaseDTO implements Serializable {

    private String userId;

    private List<String> orgCodes = new ArrayList<>();

    private List<String> roleIds = new ArrayList<>();
}
