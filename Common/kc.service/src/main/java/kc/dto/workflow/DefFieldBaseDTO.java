package kc.dto.workflow;

import kc.dto.EntityBaseDTO;
import kc.dto.TreeNodeDTO;
import kc.framework.enums.AttributeDataType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class DefFieldBaseDTO<T> extends TreeNodeDTO<T> implements Serializable {

	private String displayName;

	private String description;

	private AttributeDataType dataType;
	private String dataTypeString;
	public String getDataTypeString() { return dataType.getDesc(); }

	private String value;

	private String value1;

	private String value2;

	@com.fasterxml.jackson.annotation.JsonProperty("canEdit")
	private boolean canEdit = false;
	@com.fasterxml.jackson.annotation.JsonProperty("isPrimaryKey")
	private boolean isPrimaryKey = false;
	@com.fasterxml.jackson.annotation.JsonProperty("isExecutor")
	private boolean isExecutor = false;
	@com.fasterxml.jackson.annotation.JsonProperty("isCondition")
	private boolean isCondition = false;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DefFieldBaseDTO))
			return false;
		if (!super.equals(o))
			return false;

		DefFieldBaseDTO entity = (DefFieldBaseDTO) o;

		if (!Objects.equals(displayName, entity.displayName))
			return false;
		if (!Objects.equals(dataType, entity.dataType))
			return false;
		if (!Objects.equals(value, entity.value))
			return false;
		if (canEdit && !entity.canEdit)
			return false;
		if (!isPrimaryKey && entity.isPrimaryKey)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (displayName != null ? displayName.hashCode() : 0 );
		result = 31 * result + (dataType != null ? dataType.hashCode() : 0 );
		result = 31 * result + (value != null ? value.hashCode() : 0 );
		result = 31 * result + Boolean.valueOf(canEdit).hashCode();
		result = 31 * result + Boolean.valueOf(isPrimaryKey).hashCode();
		return result;
	}
}
