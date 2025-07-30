package kc.model.training;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import kc.framework.base.EntityBase;
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
@javax.persistence.Entity
@Table(name = Tables.CourseRecord)
@Inheritance(strategy = InheritanceType.JOINED)
public class CourseRecord extends EntityBase implements java.io.Serializable{

	private static final long serialVersionUID = -2826013081358110502L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CourseRecordId", unique = true, nullable = false)
	private int courseRecordId ;
	
	/**
	 * 课程安排Id
	 */
	@Column(name = "CurriculumId")
	private int curriculumId ;

	/**
	 * 教室Id
	 */
	@Column(name = "ClassRoomId")
	private int classRoomId ;

	/**
	 * 教室名称
	 */
	@Column(name = "ClassRoom")
	private String classRoom ;

	/**
	 * 课程Id
	 */
	@Column(name = "CourseId")
	private int courseId ;

	/**
	 * 课程名称
	 */
	@Column(name = "Course")
	private String course ;

	/**
	 * 老师Id
	 */
	@Column(name = "TeacherId")
	private int teacherId ;

	/**
	 * 老师名称
	 */
	@Column(name = "TeacherName")
	private String teacherName ;

	/**
	 * 学生Id
	 */
	@Column(name = "StudentId")
	private int studentId ;

	/**
	 * 学生名称
	 */
	@Column(name = "StudentName")
	private String studentName ;
	
	/**
	 * 打卡时间
	 */
	@Column(name = "CreatedDateTime")
	private Date createdDateTime ;

	/**
	 * 确认时间
	 */
	@Column(name = "ConfirmDateTime")
	private Date confirmDateTime ;

	/**
	 * 上课时长
	 */
	@Column(name = "CourseDuration")
	private int courseDuration ;

	/**
	 * 状态：kc.enums.training.CourseRecordStatus
	 */
	@Column(name = "Status")
	private int status ;

	/**
	 * 备注
	 */
	@Column(name = "Remark")
	private String remark ;
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		CourseRecord node = (CourseRecord) o;

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
