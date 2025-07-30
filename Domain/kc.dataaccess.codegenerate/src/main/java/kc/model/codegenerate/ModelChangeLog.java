package kc.model.codegenerate;

import kc.framework.base.ProcessLogBase;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name=Tables.ModelChangeLog)
public class ModelChangeLog extends ProcessLogBase {
	@Column(name = "ModelType")
	private int modelType;

	@Column(name = "ReferenceId", length = 128)
	private String referenceId;

	@Column(name = "ReferenceName", length = 256)
	private String referenceName;

	@Column(name = "RefObjectJson")
	public String refObjectJson ;
}
