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
@Table(name = Tables.ClassRoom)
@Inheritance(strategy = InheritanceType.JOINED)
public class ClassRoom extends Entity implements java.io.Serializable{

	private static final long serialVersionUID = 8492338147933765072L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ClassRoomId", unique = true, nullable = false)
	private int classRoomId ;
	/**
	 * 教室名
	 */
	@Column(name = "Name")
	private String name ;
	/**
	 * 描述
	 */
	@Column(name = "Description")
	private String description ;
	
	@Builder.Default
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "classRoom")
	private Set<Curriculum> curriculums = new HashSet<Curriculum>();
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		ClassRoom node = (ClassRoom) o;

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
		result = 31 * result + (name != null ? name.hashCode() : 0 );
		return result;
	}
}
