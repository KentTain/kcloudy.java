package kc.model.portal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Tables.Advertising)
@Inheritance(strategy = InheritanceType.JOINED)
public class Advertising extends kc.framework.base.Entity implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;
    /**
     * 文章标题
     */
    @Column(name = "Name", length = 128)
    private String name;

    /**
     * 作者
     */
    @Column(name = "AdvertisingWidth")
    private int advertisingWidth;

    /**
     * 作者邮箱
     */
    @Column(name = "AdvertisingHeight")
    private int advertisingHeight;

    /**
     * 关键字
     */
    @Column(name = "ImageBlob", length = 512)
    private String imageBlob;

    /**
     * 描述(显示时，填充html description)
     */
    @Column(name = "Link", length = 512)
    private String link;

    /**
     * 是否显示
     */
    @Column(name = "IsShow")
    private boolean isShow;

    /**
     * 发布状态
     */
    @Column(name = "Status")
    private int status = 0;

    /**
     * 广告版本
     */
    @Column(name = "AdVersion", length = 128)
    private String adVersion;

    @ManyToOne
    @JoinColumn(name = "AdvertisingGroupId", nullable = false, foreignKey = @ForeignKey(name = "FK_ptl_Advertising_ptl_AdvertisingGroup_AdvertisingGroupId"))
    private AdvertisingGroup advertisingGroup;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        Advertising node = (Advertising) o;

        if (id != node.id)
            return false;
        if (!Objects.equals(name, node.name))
            return false;
        if (!Objects.equals(link, node.link))
            return false;
        if (!Objects.equals(adVersion, node.adVersion))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (adVersion != null ? adVersion.hashCode() : 0);
        return result;
    }
}
