package kc.dto.search;

import kc.dto.EntityBaseDTO;
import lombok.*;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TreeNodeSearchDTO extends EntityBaseDTO implements Serializable {

    private String name;

    private Integer excludeId;

    private Integer selectedId;

    private Boolean hasAll;

    private Boolean hasRoot;

    private Integer maxLevel;
}
