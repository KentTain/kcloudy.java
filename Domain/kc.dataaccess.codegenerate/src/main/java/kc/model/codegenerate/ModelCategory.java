package kc.model.codegenerate;

import kc.framework.base.TreeNode;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name=Tables.ModelCategory)
public class ModelCategory extends TreeNode<ModelCategory> {
    /**
     * 模型类型
	 */
	@Column(name = "ModelType")
	private int modelType;

	@Column(name = "ApplicationId")
	private String applicationId;

}
