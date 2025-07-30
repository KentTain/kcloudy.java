package kc.model.training;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import kc.framework.base.Entity;
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
@Table(name = Tables.Course)
@Inheritance(strategy = InheritanceType.JOINED)
@NamedEntityGraph(name = "Graph.Course.Books", attributeNodes = { @NamedAttributeNode("books") })
@NamedEntityGraph(name = "Graph.Course.Students", attributeNodes = { @NamedAttributeNode("students") })
public class Course extends Entity implements java.io.Serializable {

	private static final long serialVersionUID = -4256104201658485986L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CourseId", unique = true, nullable = false)
	private int courseId;
	/**
	 * 课程名
	 */
	@Column(name = "Name")
	private String name;

	/**
	 * 课程状态： kc.enums.training.CourseStatus
	 */
	@Column(name = "Status")
	private int status;
	/**
	 * 开始时间
	 */
	@Column(name = "StartDate")
	private Date startDate;

	/**
	 * 结束时间
	 */
	@Column(name = "EndDate")
	private Date endDate;

	/**
	 * 人数限制
	 */
	@Column(name = "StudentLimit")
	private int studentLimit;

	/**
	 * 描述
	 */
	@Column(name = "Description")
	private String description;

	@Builder.Default
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
	private Set<Curriculum> curriculums = new HashSet<Curriculum>();

	@Builder.Default
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "courses")
	private Set<Book> books = new HashSet<Book>();

	@Builder.Default
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "courses")
	private Set<Student> students = new HashSet<Student>();

	@Builder.Default
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "courses")
	private Set<Teacher> teachers = new HashSet<Teacher>();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		Course node = (Course) o;

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
