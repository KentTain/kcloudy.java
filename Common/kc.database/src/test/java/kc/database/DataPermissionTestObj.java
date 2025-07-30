package kc.database;

import kc.framework.base.DataPermitEntity;
import kc.framework.extension.StringListConverter;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="test_datePermission")
@Inheritance(strategy= InheritanceType.JOINED)
public class DataPermissionTestObj extends kc.framework.base.Entity {

    public DataPermissionTestObj(String userName, List<String> orgIds, List<String> roleIds, List<String> userIds) {
        super();
        this.name = userName;
        this.setOrgIds(orgIds);
        this.setRoleIds(roleIds);
        this.setUserIds(userIds);
    }

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", unique = true, nullable = false)
    private int id;

    //JPA（hibernate）一对多根据多的一方某属性进行过滤查询：https://cloud.tencent.com/developer/article/1384036
    //https://blog.csdn.net/pushme_pli/article/details/86760476
    //@ElementCollection
    @Column(name = "OrgIds", length = 4000)
    @Convert(converter = StringListConverter.class)
    private List<String> orgIds = new ArrayList<>();
    @Column(name = "OrgIds", insertable = false, updatable = false)
    private String orgIdsString; //No need for setter

    //JPA（hibernate）一对多根据多的一方某属性进行过滤查询：https://cloud.tencent.com/developer/article/1384036
    //@ElementCollection
    @Column(name = "RoleIds", length = 4000)
    @Convert(converter = StringListConverter.class)
    private List<String> roleIds = new ArrayList<>();
    @Column(name = "RoleIds", insertable = false, updatable = false)
    private String roleIdsString; //No need for setter

    //JPA（hibernate）一对多根据多的一方某属性进行过滤查询：https://cloud.tencent.com/developer/article/1384036
    //@ElementCollection
    @Column(name = "UserIds", length = 4000)
    @Convert(converter = StringListConverter.class)
    private List<String> userIds = new ArrayList<>();
    @Column(name = "UserIds", insertable = false, updatable = false)
    private String userIdsString; //No need for setter

    @Column(name = "Name", length = 256)
    private String name;
}
