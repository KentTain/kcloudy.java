package kc.dto.dict;

import kc.dto.EntityBaseDTO;
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
public class CityDTO extends EntityBaseDTO implements Serializable {

	private int id;

	private String name;

	@com.fasterxml.jackson.annotation.JsonProperty("isSelect")
	private boolean isSelect;

	private int provinceId;

	private String provinceName;


	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof CityDTO))
			return false;
		if (!super.equals(o))
			return false;

		CityDTO entity = (CityDTO) o;

		if (!Objects.equals(id, entity.id))
			return false;
		if (!Objects.equals(name, entity.name))
			return false;
		if (!Objects.equals(provinceId, entity.provinceId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + id;
		result = 31 * result + (name != null ? name.hashCode() : 0 );
		result = 31 * result + provinceId;
		return result;
	}
}
