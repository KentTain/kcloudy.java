package kc.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import kc.framework.base.TreeNode;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="test_menu")
@Inheritance(strategy=InheritanceType.JOINED)
public class MenuNode extends TreeNode<MenuNode> {
	private static final long serialVersionUID = 3862416351900991824L;
	
	@Column(name = "Desc")
	private String Desc;

}
