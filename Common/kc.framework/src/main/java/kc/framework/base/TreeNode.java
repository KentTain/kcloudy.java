package kc.framework.base;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;

import lombok.*;

/**
 * @author tianc 树结构基类
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
//@Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
//@AttributeOverride(name="parent_id",column=@Column(name="ParentId"))
//@AttributeOverride(name="tree_code",column=@Column(name="TreeCode"))
@NamedEntityGraphs({
        @NamedEntityGraph(name = "Graph.TreeNode.ChildNodes",
                attributeNodes = {@NamedAttributeNode(value = "childNodes", subgraph = "son")},                         //一级延伸
                subgraphs = {@NamedSubgraph(name = "son", attributeNodes = @NamedAttributeNode(value = "childNodes", subgraph = "grandson")),         //二级延伸
                        @NamedSubgraph(name = "grandson", attributeNodes = @NamedAttributeNode(value = "childNodes", subgraph = "greatGrandSon")),    //三级延伸
                        @NamedSubgraph(name = "greatGrandSon", attributeNodes = @NamedAttributeNode(value = "childNodes"))}
        )
})
public abstract class TreeNode<T> extends Entity implements java.io.Serializable {
    private static final long serialVersionUID = 3862416351900991824L;

    /**
     * 子节点Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", unique = true, nullable = false)
    //@Getter(onMethod_={
    //		@Id,
    //		@GeneratedValue(strategy = GenerationType.IDENTITY),
    //		@Column(name = "Id", unique = true, nullable = false)})
    private int id;

    /**
     * 名称
     */
    //@Getter(onMethod_={@Column(name = "Name", length = 128)})
    @Column(name = "Name", length = 128)
    private String name;

    /**
     * 标识树形结构的编码:
     * 一级树节点Id-二级树节点Id-三级树节点Id-四级树节点Id
     * 1-1-1-1 ~~ 999-999-999-999
     */
    //@Getter(onMethod_={@Column(name = "TreeCode", length = 128)})
    @Column(name = "TreeCode", length = 128)
    private String treeCode;

    /**
     * 是否叶节点
     */
    //@Getter(onMethod_={@Column(name = "Leaf")})
    @Column(name = "Leaf")
    private boolean leaf;

    /**
     * 节点深度
     */
    //@Getter(onMethod_={@Column(name = "Level")})
    @Column(name = "Level")
    private int level;

    /**
     * 排序
     */
    //@Getter(onMethod_={@Column(name = "Index")})
    @Column(name = "Index")
    private int index;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ParentId")
    private T parentNode;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentNode")
    private List<T> childNodes = new ArrayList<T>();
}
