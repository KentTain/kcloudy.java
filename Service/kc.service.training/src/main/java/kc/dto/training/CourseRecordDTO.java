package kc.dto.training;

import java.util.Date;

import kc.dto.EntityBaseDTO;
import kc.enums.training.CourseRecordStatus;
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
public class CourseRecordDTO extends EntityBaseDTO implements java.io.Serializable {

	private static final long serialVersionUID = 5249487745778329894L;

	private int courseRecordId;

	/**
	 * 课程安排Id
	 */
	private int curriculumId;

	/**
	 * 教室Id
	 */
	private int classRoomId;

	/**
	 * 教室名称
	 */
	private String classRoom;

	/**
	 * 课程Id
	 */
	private int courseId;

	/**
	 * 课程名称
	 */
	private String course;

	/**
	 * 老师Id
	 */
	private int teacherId;

	/**
	 * 老师名称
	 */
	private String teacherName;

	/**
	 * 学生Id
	 */
	private int studentId;

	/**
	 * 学生名称
	 */
	private String studentName;

	/**
	 * 打卡时间
	 */
	private Date createdDate;

	/**
	 * 确认时间
	 */
	private Date confirmDate;

	/**
	 * 上课时长
	 */
	private int courceDuration;

	/**
	 * 状态：kc.enums.training.CourseRecordStatus
	 */
	private CourseRecordStatus status;

	/**
	 * 备注
	 */
	private String remark;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		CourseRecordDTO node = (CourseRecordDTO) o;

		if (courseRecordId != node.courseRecordId)
			return false;
		if (curriculumId != node.curriculumId)
			return false;
		if (classRoomId != node.classRoomId)
			return false;
		if (courseId != node.courseId)
			return false;
		if (teacherId != node.teacherId)
			return false;
		if (studentId != node.studentId)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + courseRecordId;
		result = 31 * result + curriculumId;
		result = 31 * result + classRoomId;
		result = 31 * result + courseId;
		result = 31 * result + teacherId;
		result = 31 * result + studentId;
		return result;
	}
}
