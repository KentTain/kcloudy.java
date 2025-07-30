package kc.dto.search;

import kc.dto.EntityBaseDTO;
import kc.dto.EntityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class DataPermissionSearchDTO extends EntityBaseDTO implements Serializable {

    private Set<String> orgCodes = new HashSet<>();

    private Set<String> roleIds = new HashSet<>();

    private Set<String> userIds = new HashSet<>();
}
