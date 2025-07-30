package kc.model.dict;


import kc.framework.base.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@javax.persistence.Entity
@Table(name = Tables.MobileLocation)
@Inheritance(strategy = InheritanceType.JOINED)
public class MobileLocation extends EntityBase implements Serializable {

	@Id
	@Column(name = "Id")
	private int id;

	@Column(name = "Mobile")
	private String mobile;

	@Column(name = "Province")
	private String province;

	@Column(name = "City")
	private String city;

	@Column(name = "Corp")
	private String corp;

	@Column(name = "AreaCode")
	private String areaCode;

	@Column(name = "PostCode")
	private String postCode;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof MobileLocation))
			return false;
		if (!super.equals(o))
			return false;

		MobileLocation entity = (MobileLocation) o;

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
