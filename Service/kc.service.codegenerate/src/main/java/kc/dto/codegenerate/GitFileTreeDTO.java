package kc.dto.codegenerate;

import lombok.*;

import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public class GitFileTreeDTO implements java.io.Serializable {
    /**
     * 文件Id
     */
    private String id;
    /**
     * 父节点Id
     */
    private String parentId;
    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 是否目录
     */
    private boolean isDirectory;

    /**
     * 是否叶节点
     */
    private boolean leaf = false;

    /**
     * 节点深度
     */
    private int level;

    /**
     * 排序
     */
    private int index;

    /**
     * 子节点数据
     */
    private List<GitFileTreeDTO> children = new ArrayList<>();
}
