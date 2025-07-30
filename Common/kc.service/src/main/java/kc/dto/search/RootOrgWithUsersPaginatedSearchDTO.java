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
public class RootOrgWithUsersPaginatedSearchDTO extends EntityDTO implements java.io.Serializable{

    private List<String> roleIds = new ArrayList<String>();

    private UUID appid;

    private String departName;

    private String departCode;

    private String userName;

    private String userCode;

    private String searchKey;

    private int pageIndex;

    private int pageSize;
}
