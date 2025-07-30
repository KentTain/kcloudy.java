package kc.dto.dict;

import kc.dto.TreeNodeDTO;
import kc.dto.message.NewsBulletinDTO;
import kc.enums.NewsBulletinType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class IndustryClassficationDTO extends TreeNodeDTO<IndustryClassficationDTO> implements java.io.Serializable {

	@com.fasterxml.jackson.annotation.JsonProperty("isValid")
	private boolean isValid;

	/**
	 * 描述
	 */
	private int parentId;

	/**
	 * 描述
	 */
	private String parentName;

	/**
	 * 是否显示
	 */
	@com.fasterxml.jackson.annotation.JsonProperty("isSelect")
	private boolean isSelect;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		IndustryClassficationDTO node = (IndustryClassficationDTO) o;

		if (!Objects.equals(parentName, node.parentName))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (parentName != null ? parentName.hashCode() : 0);
		return result;
	}
}
