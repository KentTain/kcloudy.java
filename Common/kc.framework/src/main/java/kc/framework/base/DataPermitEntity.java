package kc.framework.base;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 数据权限基类：
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public abstract class DataPermitEntity extends Entity implements Serializable {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", unique = true, nullable = false)
    private int id;

    @Column(name = "OrgIds", length = 4000)
    //@Convert(converter = StringListConverter.class)
    private String orgIds;

    @Column(name = "RoleIds", length = 4000)
    //@Convert(converter = StringListConverter.class)
    private String roleIds;

    @Column(name = "UserIds", length = 4000)
    //@Convert(converter = StringListConverter.class)
    private String userIds;
}
