package kc.model.app;

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
@Table(name=Tables.ApplicationLog)
public class ApplicationLog extends ProcessLogBase {
	@Column(name = "AppLogType")
	private int appLogType;

	@Column(name = "ReferenceId", length = 128)
	private String referenceId;

	@Column(name = "ReferenceName", length = 256)
	private String referenceName;

	@Column(name = "RefObjectJson")
	public String refObjectJson ;
}
