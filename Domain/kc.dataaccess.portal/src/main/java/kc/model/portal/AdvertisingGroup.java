package kc.model.portal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Tables.AdvertisingGroup)
@Inheritance(strategy = InheritanceType.JOINED)
public class AdvertisingGroup extends kc.framework.base.EntityBase implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;
    /**
     * 广告页名称
     */
    @Column(name = "Name", length = 128)
    private String name;

    /**
     * 展示页面图片
     */
    @Column(name = "ImageBlob", length = 256)
    private String imageBlob;

    /**
     * 展示页面链接地址
     */
    @Column(name = "Link", length = 512)
    private String link;

    /**
     * 是否启用
     */
    @Column(name = "IsEnable")
    private boolean isEnable;

    /**
     * 描述(显示时，填充html description)
     */
    @Column(name = "Description", length = 512)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "advertisingGroup")
    private List<Advertising> advertisings = new ArrayList<Advertising>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        AdvertisingGroup node = (AdvertisingGroup) o;

        if (id != node.id)
            return false;
        if (!Objects.equals(name, node.name))
            return false;
        if (!Objects.equals(link, node.link))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        return result;
    }
}
