package kc.dto.dict;

import kc.dto.EntityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class DictTypeDTO extends EntityDTO implements Serializable {

	private int id;

	private String name;

	private String code;

	@com.fasterxml.jackson.annotation.JsonProperty("isSys")
	private boolean isSys;

	private List<DictValueDTO> dictValues = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DictTypeDTO))
			return false;
		if (!super.equals(o))
			return false;

		DictTypeDTO entity = (DictTypeDTO) o;

		if (!Objects.equals(id, entity.id))
			return false;
		if (!Objects.equals(name, entity.name))
			return false;
		if (!Objects.equals(code, entity.code))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + id;
		result = 31 * result + (name != null ? name.hashCode() : 0 );
		result = 31 * result + (code != null ? code.hashCode() : 0 );
		return result;
	}
}
