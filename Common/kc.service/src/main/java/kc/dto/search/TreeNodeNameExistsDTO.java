package kc.dto.search;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TreeNodeNameExistsDTO extends kc.dto.EntityBaseDTO implements java.io.Serializable {
	private Integer id;

	private Integer pId;

	private String name;

}
