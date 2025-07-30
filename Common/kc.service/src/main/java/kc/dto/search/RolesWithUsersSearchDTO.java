package kc.dto.search;

import kc.dto.EntityBaseDTO;
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
public class RolesWithUsersSearchDTO extends EntityBaseDTO implements java.io.Serializable{

    private Integer type;

    private List<String> roleIds = new ArrayList<>();

    private List<String> exceptRoleIds = new ArrayList<>();
}
