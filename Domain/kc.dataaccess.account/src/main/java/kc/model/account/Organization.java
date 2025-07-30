package kc.model.account;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import kc.framework.base.TreeNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity
@Table(name="sys_Organization")
@NamedEntityGraph(name = "Graph.Organization.Users", 
	attributeNodes = {@NamedAttributeNode("users")})
public class Organization extends TreeNode<Organization> implements java.io.Serializable {

	private static final long serialVersionUID = 4746312471509131747L;

	/**
	 * 组织编号（SequenceName--Organization：ORG2018120100001）
	 */
	@Column(name = "OrganizationCode", columnDefinition = "nvarchar(20)")
	private String organizationCode;

	/**
	 * 组织类型
	 * kc.enums.admin.OrganizationType
	 */
	//@Enumerated(EnumType.ORDINAL)
	@Column(name = "OrganizationType")
	private int organizationType;

	/**
	 * 业务类型
	 * kc.framework.enums.BusinessType
	 */
	//@Enumerated(EnumType.ORDINAL)
	@Column(name = "BusinessType")
	private int businessType;
	/**
	 * 状态
	 * kc.framework.enums.WorkflowBusStatus
	 */
	@Column(name = "Status")
	private int status;

	/**
	 * 外部编号1
	 */
	@Column(name = "ReferenceId1", columnDefinition = "nvarchar(50)")
	private String referenceId1;

	/**
	 * 外部编号2
	 */
	@Column(name = "ReferenceId2", columnDefinition = "nvarchar(50)")
	private String referenceId2;

	/**
	 * 外部编号3
	 */
	@Column(name = "ReferenceId3", columnDefinition = "nvarchar(50)")
	private String referenceId3;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy ="organizations")
	private Set<User> users = new HashSet<User>();
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (!super.equals(o))
			return false;

		Organization node = (Organization) o;

		return organizationCode.equals(node.organizationCode);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (organizationCode != null ? organizationCode.hashCode() : 0);
		return result;
	}
}
