package kc.dto.dict;

import kc.dto.EntityBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class MobileLocationDTO extends EntityBaseDTO implements Serializable {

	private int id;

	private String mobile;

	private String province;

	private String city;

	private String corp;

	private String areaCode;

	private String postCode;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof MobileLocationDTO))
			return false;
		if (!super.equals(o))
			return false;

		MobileLocationDTO entity = (MobileLocationDTO) o;

		if (!Objects.equals(id, entity.id))
			return false;
		if (!Objects.equals(mobile, entity.mobile))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + id;
		result = 31 * result + (mobile != null ? mobile.hashCode() : 0 );
		return result;
	}
}
