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
@Table(name = Tables.DictValue)
@Inheritance(strategy = InheritanceType.JOINED)
public class DictValue extends Entity implements Serializable {

	@javax.persistence.Id
	@Column(name = "Id")
	private int id;

	@Column(name = "Code", length = 128)
	private String code;

	@Column(name = "Name", length = 512)
	private String name;

	@Column(name = "Description", length=4000)
	private String description;

	@Column(name = "DictTypeCode", length = 128)
	private String dictTypeCode;

	@ManyToOne
	@JoinColumn(name = "DictTypeId", nullable = false, foreignKey = @ForeignKey(name="FK_dic_DictValue_dic_DictType_DictTypeId"))
	private DictType dictType;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DictValue))
			return false;
		if (!super.equals(o))
			return false;

		DictValue entity = (DictValue) o;

		if (!Objects.equals(id, entity.id))
			return false;
		if (!Objects.equals(code, entity.code))
			return false;
		if (!Objects.equals(name, entity.name))
			return false;
		if (!Objects.equals(dictTypeCode, entity.dictTypeCode))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + id;
		result = 31 * result + (name != null ? name.hashCode() : 0 );
		result = 31 * result + (code != null ? code.hashCode() : 0 );
		result = 31 * result + (dictTypeCode != null ? dictTypeCode.hashCode() : 0 );;
		return result;
	}
}
