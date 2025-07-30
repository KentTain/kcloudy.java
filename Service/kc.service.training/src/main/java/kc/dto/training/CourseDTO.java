package kc.dto.training;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import kc.dto.EntityDTO;
import kc.enums.training.CourseStatus;
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
public class CourseDTO extends EntityDTO implements java.io.Serializable {

	private static final long serialVersionUID = 8131584204002667359L;

	private boolean editMode;
	
	private int courseId;
	/**
	 * 课程名
	 */
	private String name;
	/**
     * 课程状态： kc.enums.training.CourseStatus
     */
	private CourseStatus status;
	/**
	 * 开始时间
	 */
	private Date startDate;

	/**
	 * 结束时间
	 */
	private Date endDate;

	/**
	 * 人数限制
	 */
	private int studentLimit;

	/**
	 * 描述
	 */
	private String description;

	@Builder.Default
	private Set<CurriculumDTO> curriculums = new HashSet<CurriculumDTO>();

	@Builder.Default
	private Set<BookDTO> books = new HashSet<BookDTO>();

	@Builder.Default
	private Set<StudentDTO> students = new HashSet<StudentDTO>();

	@Builder.Default
	private Set<TeacherDTO> teachers = new HashSet<TeacherDTO>();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		CourseDTO node = (CourseDTO) o;

		if (courseId != node.courseId)
			return false;
		if (name != null ? !name.equals(node.name) : node.name != null)
			return false;
		if (startDate != null ? !startDate.equals(node.startDate) : node.startDate != null)
			return false;
		if (endDate != null ? !endDate.equals(node.endDate) : node.endDate != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + courseId;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
		result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
		return result;
	}
}
