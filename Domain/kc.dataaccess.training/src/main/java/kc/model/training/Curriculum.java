package kc.model.training;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = Tables.Curriculum)
@Inheritance(strategy = InheritanceType.JOINED)
public class Curriculum extends EntityBase implements java.io.Serializable{

	private static final long serialVersionUID = 6638209041240731362L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CurriculumId", unique = true, nullable = false)
	private int curriculumId;

	/**
	 * 上课时间
	 */
	@Column(name = "CourceDate")
	private Date courceDate;
	
	/**
	 * 上课时长
	 */
	@Column(name = "CourceDuration")
	private int courceDuration;

	/**
	 * 二维码
	 */
	@Column(name = "QrCode")
	private String qrCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ClassRoomId", nullable = false, updatable = false, insertable = false)
	private ClassRoom classRoom;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CourseId", nullable = false, updatable = false, insertable = false)
	private Course course;
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		Curriculum node = (Curriculum) o;

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
		result = 31 * result + (courceDate != null ? courceDate.hashCode() : 0 );
		result = 31 * result + courceDuration;
		return result;
	}
}
