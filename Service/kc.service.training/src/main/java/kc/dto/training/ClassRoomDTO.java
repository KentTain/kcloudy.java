package kc.dto.training;

import java.util.HashSet;
import java.util.Set;

import kc.dto.EntityDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomDTO extends EntityDTO implements java.io.Serializable {

	private static final long serialVersionUID = -366104319681473240L;

	private boolean editMode;
	
	private int classRoomId;
	/**
	 * 教室名
	 */
	private String name;
	/**
	 * 描述
	 */
	private String description;

	@Builder.Default
	private Set<CurriculumDTO> curriculums = new HashSet<CurriculumDTO>();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		ClassRoomDTO node = (ClassRoomDTO) o;

		if (classRoomId != node.classRoomId)
			return false;
		if (name != null ? !name.equals(node.name) : node.name != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + classRoomId;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}
}
