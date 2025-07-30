package kc.model.dict;


import kc.framework.base.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@javax.persistence.Entity
@Table(name = Tables.City)
@Inheritance(strategy = InheritanceType.JOINED)
public class City extends Entity implements Serializable {

	@Id
	@Column(name = "Id")
	private int id;

	@Column(name = "Name", length = 512)
	private String name;

	@ManyToOne
	@JoinColumn(name = "ProvinceId", nullable = false, foreignKey = @ForeignKey(name="FK_dic_City_dic_Province_ProvinceId"))
	private Province province;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof City))
			return false;
		if (!super.equals(o))
			return false;

		City entity = (City) o;

		if (!Objects.equals(id, entity.id))
			return false;
		if (!Objects.equals(name, entity.name))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + id;
		result = 31 * result + (name != null ? name.hashCode() : 0 );
		return result;
	}
}
