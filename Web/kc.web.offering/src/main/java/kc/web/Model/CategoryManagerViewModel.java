package kc.web.Model;

import kc.dto.account.UserSimpleDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class CategoryManagerViewModel implements java.io.Serializable{
    private int categoryId;

    private List<UserSimpleDTO> users = new ArrayList<>();
}
