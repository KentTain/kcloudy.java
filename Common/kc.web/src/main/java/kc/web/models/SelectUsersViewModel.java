package kc.web.models;

import kc.dto.EntityDTO;
import kc.dto.account.OrganizationDTO;
import kc.dto.account.OrganizationSimpleDTO;
import kc.dto.account.RoleDTO;
import kc.dto.account.RoleSimpleDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SelectUsersViewModel extends EntityDTO implements java.io.Serializable{

    public SelectUsersViewModel(List<OrganizationSimpleDTO> deptInfos){
        this.deptInfos.addAll(deptInfos);
    }

    public SelectUsersViewModel(List<OrganizationSimpleDTO> deptInfos, List<RoleSimpleDTO> roleInfos){
        this.deptInfos.addAll(deptInfos);
        this.roleInfos.addAll(roleInfos);
    }

    private List<OrganizationSimpleDTO> deptInfos = new ArrayList<OrganizationSimpleDTO>();

    private List<RoleSimpleDTO> roleInfos = new ArrayList<RoleSimpleDTO>();
}
