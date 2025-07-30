package kc.dto.search;

import kc.dto.EntityBaseDTO;
import kc.dto.EntityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrgTreesWithUsersSearchDTO extends EntityBaseDTO implements java.io.Serializable{

    private Integer type;

    private List<Integer> orgIds = new ArrayList<>();

    private List<Integer> exceptOrgIds = new ArrayList<>();
}
