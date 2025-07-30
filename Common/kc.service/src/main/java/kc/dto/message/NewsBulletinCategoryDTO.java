package kc.dto.message;

import kc.dto.TreeNodeDTO;
import kc.enums.NewsBulletinType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class NewsBulletinCategoryDTO extends TreeNodeDTO<NewsBulletinCategoryDTO> implements java.io.Serializable {

	@com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
	private boolean isEditMode;

	private int id;

	/**
	 * 文章类型
	 */
	private NewsBulletinType type;
	private String typeString;
	public String getTypeString() { return type.getDesc(); }

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 是否显示
	 */
	@com.fasterxml.jackson.annotation.JsonProperty("isShow")
	private boolean isShow;

	private List<NewsBulletinDTO> newsBulletins = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		NewsBulletinCategoryDTO node = (NewsBulletinCategoryDTO) o;

		if (!Objects.equals(id, node.id))
			return false;
		if (!Objects.equals(type, node.type))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + id;
		result = 31 * result + (type != null ? type.hashCode() : 0);
		return result;
	}
}
