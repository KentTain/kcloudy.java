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
@Table(name = Tables.Book)
@Inheritance(strategy = InheritanceType.JOINED)
public class Book extends Entity implements java.io.Serializable {

	private static final long serialVersionUID = 855593664084350410L;
	/**
	 * 序列名称
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BookId", unique = true, nullable = false)
	private int bookId;
	/**
	 * 书名
	 */
	@Column(name = "Name")
	private String name;
	/**
	 * 地址
	 */
	@Column(name = "Url")
	private String url;
	/**
	 * 描述
	 */
	@Column(name = "Description")
	private String description;

	@Builder.Default
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = Tables.BooksInCourses, joinColumns = { @JoinColumn(name = "BookId") }, inverseJoinColumns = {
			@JoinColumn(name = "CourseId") })
	private Set<Course> courses = new HashSet<Course>();
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		Book node = (Book) o;

		if (bookId != node.bookId)
			return false;
		if (name != null ? !name.equals(node.name) : node.name != null)
			return false;
		if (url != null ? !url.equals(node.url) : node.url != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + bookId;
		result = 31 * result + (name != null ? name.hashCode() : 0 );
		result = 31 * result + (url != null ? url.hashCode() : 0);
		return result;
	}
}
