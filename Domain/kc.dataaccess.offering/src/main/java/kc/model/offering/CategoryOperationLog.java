package kc.model.offering;

import kc.framework.base.ProcessLogBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Tables.CategoryOperationLog)
public class CategoryOperationLog extends ProcessLogBase {

	/**
	 * 商品名称
	 */
	@Column(name = "CategoryName", length = 256)
	private String categoryName;

	@ManyToOne(fetch= FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "CategoryId", nullable = false, foreignKey = @ForeignKey(name="FK_prd_CategoryOperationLog_prd_Category_CategoryId"))
	private Category category;
}
