package kc.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PaginatedBaseDTO<T> extends EntityBaseDTO  implements java.io.Serializable{

	public PaginatedBaseDTO(int pageIndex, int pageSize, long total, List<T> rows) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.total = total;
        this.rows = rows;
    }

	private Integer pageIndex;

	private Integer pageSize;

	private Long total;

	private List<T> rows = new ArrayList<T>();
    
	private Integer records;
}
