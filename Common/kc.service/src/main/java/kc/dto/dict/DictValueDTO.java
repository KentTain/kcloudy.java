package kc.dto.dict;

import kc.dto.EntityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class DictValueDTO extends EntityDTO implements Serializable {

	private int id;

	private String name;

	private String code;

	@com.fasterxml.jackson.annotation.JsonProperty("isSelect")
	private boolean isSelect;

	private String description;

	private int dictTypeId;

	private String dictTypeCode;

	private String dictTypeName;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DictValueDTO))
			return false;
		if (!super.equals(o))
			return false;

		DictValueDTO entity = (DictValueDTO) o;

		if (!Objects.equals(id, entity.id))
			return false;
		if (!Objects.equals(code, entity.code))
			return false;
		if (!Objects.equals(name, entity.name))
			return false;
		if (!Objects.equals(dictTypeId, entity.dictTypeId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + id;
		result = 31 * result + (name != null ? name.hashCode() : 0 );
		result = 31 * result + (code != null ? code.hashCode() : 0 );
		result = 31 * result + dictTypeId;
		return result;
	}
}
