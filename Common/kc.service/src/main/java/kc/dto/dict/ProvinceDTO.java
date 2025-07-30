package kc.dto.dict;

import kc.dto.EntityBaseDTO;
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
public class ProvinceDTO extends EntityBaseDTO implements Serializable {

	private int provinceId;

	private String name;

	@com.fasterxml.jackson.annotation.JsonProperty("isSelect")
	private boolean isSelect;

	private List<CityDTO> cities = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ProvinceDTO))
			return false;
		if (!super.equals(o))
			return false;

		ProvinceDTO entity = (ProvinceDTO) o;

		if (!Objects.equals(provinceId, entity.provinceId))
			return false;
		if (!Objects.equals(name, entity.name))
			return false;
		if (!Objects.equals(isSelect, entity.isSelect))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + provinceId;
		result = 31 * result + (name != null ? name.hashCode() : 0 );
		return result;
	}
}
