package kc.model.dict;

import kc.framework.base.TreeNode;
import kc.framework.extension.StringExtensions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity
@Table(name=Tables.IndustryClassfication)
public class IndustryClassfication extends TreeNode<IndustryClassfication> implements java.io.Serializable {

	@com.fasterxml.jackson.annotation.JsonProperty("isValid")
	@Column(name = "IsValid")
	private boolean isValid;

    @Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		IndustryClassfication node = (IndustryClassfication) o;

		if (!Objects.equals(isValid, node.isValid))
			return false;
		return true;
	}

}
