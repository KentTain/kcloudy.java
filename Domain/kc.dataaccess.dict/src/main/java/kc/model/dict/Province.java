package kc.model.dict;

import kc.framework.base.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@javax.persistence.Entity
@Table(name = Tables.Province)
@Inheritance(strategy = InheritanceType.JOINED)
@NamedEntityGraph(name = "Graph.Province.Cities",
		attributeNodes = { @NamedAttributeNode("cities") })
public class Province extends Entity implements Serializable {

	@Id
	@Column(name = "ProvinceId")
	private int provinceId;

	@Column(name = "Name", length = 512)
	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "province")
	private List<City> cities = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Province))
			return false;
		if (!super.equals(o))
			return false;

		Province entity = (Province) o;

		if (!Objects.equals(provinceId, entity.provinceId))
			return false;
		if (!Objects.equals(name, entity.name))
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
