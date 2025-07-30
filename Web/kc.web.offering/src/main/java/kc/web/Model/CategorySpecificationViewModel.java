package kc.web.Model;

import kc.dto.account.UserSimpleDTO;
import kc.dto.offering.OfferingSpecificationDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class CategorySpecificationViewModel implements java.io.Serializable{
    private int categoryId;

    private List<OfferingSpecificationDTO> specifications = new ArrayList<>();
}
