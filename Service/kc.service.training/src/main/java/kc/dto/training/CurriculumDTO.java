package kc.dto.training;

import java.util.Date;

import kc.dto.EntityBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurriculumDTO extends EntityBaseDTO implements java.io.Serializable {

	private static final long serialVersionUID = 1773381422396572074L;

	private boolean editMode;
	
	private int curriculumId;

	/**
	 * 上课时间
	 */
	private Date courceDate;

	/**
	 * 上课时长
	 */
	private int courceDuration;

	/**
	 * 二维码
	 */
	private String qrCode;

	private ClassRoomDTO classRoom;

	private CourseDTO course;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		CurriculumDTO node = (CurriculumDTO) o;

		if (curriculumId != node.curriculumId)
			return false;
		if (courceDate != null ? !courceDate.equals(node.courceDate) : node.courceDate != null)
			return false;
		if (courceDuration != node.courceDuration)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + curriculumId;
		result = 31 * result + (courceDate != null ? courceDate.hashCode() : 0);
		result = 31 * result + courceDuration;
		return result;
	}
}
