package kc.dto.training;

import java.util.HashSet;
import java.util.Set;

import kc.dto.EntityDTO;

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
public class BookDTO extends EntityDTO implements java.io.Serializable {

	private static final long serialVersionUID = -7506835842345126579L;
	
	private boolean editMode;
	
	/**
	 * 序列名称
	 */
	private int bookId;
	/**
	 * 书名
	 */
	private String name;
	/**
	 * 地址
	 */
	private String url;
	/**
	 * 描述
	 */
	private String description;

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

		BookDTO node = (BookDTO) o;

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
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (url != null ? url.hashCode() : 0);
		return result;
	}
}
