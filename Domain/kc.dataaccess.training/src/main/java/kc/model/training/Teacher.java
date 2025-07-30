package kc.model.training;

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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = Tables.Teacher)
@Inheritance(strategy = InheritanceType.JOINED)
public class Teacher extends Entity implements java.io.Serializable{

	private static final long serialVersionUID = -4183012300888869132L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "TeacherId", unique = true, nullable = false)
	private int teacherId;
	/**
	 * 登录用户ID
	 */
	@Column(name = "UserId")
	private String userId;

	/**
	 * 姓名
	 */
	@Column(name = "Name")
	private String name;

	/**
	 * 性别
	 */
	@Column(name = "Sex")
	private String sex;

	/**
	 * 手机
	 */
	@Column(name = "Mobile")
	private String mobile;

	/**
	 * 邮箱
	 */
	@Column(name = "Email")
	private String email;

	/**
	 * 身份证
	 */
	@Column(name = "IdentityCard")
	private String identityCard;
	
	/**
	 * 状态：kc.enums.training.AccountStatus
	 */
	@Column(name = "Status")
	private int status ;
	
	@Builder.Default
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
			name = Tables.TeachersInCourses,
			joinColumns = { @JoinColumn(name = "TeacherId") },
			inverseJoinColumns = { @JoinColumn(name = "CourseId") }
		)
	private Set<Course> courses = new HashSet<Course>();
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		Teacher node = (Teacher) o;

		if (teacherId != node.teacherId)
			return false;
		if (name != null ? !name.equals(node.name) : node.name != null)
			return false;
		if (userId != null ? !userId.equals(node.userId) : node.userId != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + teacherId;
		result = 31 * result + (name != null ? name.hashCode() : 0 );
		result = 31 * result + (userId != null ? userId.hashCode() : 0);
		return result;
	}
}
