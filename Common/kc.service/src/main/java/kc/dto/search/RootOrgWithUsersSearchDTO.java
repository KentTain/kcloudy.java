package kc.dto.search;

import kc.dto.EntityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RootOrgWithUsersSearchDTO extends EntityDTO implements java.io.Serializable{

    private List<String> roleIds = new ArrayList<String>();

    private List<Integer> orgIds = new ArrayList<Integer>();
}
