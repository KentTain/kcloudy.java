package kc.dto.training;

import java.util.HashSet;
import java.util.Set;

import kc.dto.EntityDTO;
import kc.enums.training.AccountStatus;
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
public class StudentDTO extends EntityDTO implements java.io.Serializable {

	private static final long serialVersionUID = -7274370366323518557L;

	private boolean editMode;
	
	private int studentId;

	/**
	 * 登录用户ID
	 */
	private String userId;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 性别
	 */
	private String sex;

	/**
	 * 手机
	 */
	private String mobile;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 身份证
	 */
	private String identityCard;
	/**
	 * 状态：kc.enums.training.AccountStatus
	 */
	private AccountStatus status;

	@Builder.Default
	private Set<CourseDTO> courses = new HashSet<CourseDTO>();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		StudentDTO node = (StudentDTO) o;

		if (studentId != node.studentId)
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
		result = 31 * result + studentId;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (userId != null ? userId.hashCode() : 0);
		return result;
	}
}
