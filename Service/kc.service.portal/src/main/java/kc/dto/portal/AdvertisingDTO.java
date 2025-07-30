package kc.dto.portal;

import kc.dto.EntityDTO;
import kc.framework.enums.WorkflowBusStatus;
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
@Inheritance(strategy = InheritanceType.JOINED)
public class AdvertisingDTO extends EntityDTO implements java.io.Serializable {

    private int id;
    /**
     * 文章标题
     */
    private String name;

    /**
     * 广告图宽
     */
    private int advertisingWidth;

    /**
     * 广告图高
     */
    private int advertisingHeight;

    /**
     * 广告图
     */
    private String imageBlob;

    /**
     * 描述(显示时，填充html description)
     */
    private String link;

    /**
     * 广告图的外链
     */
    private boolean isShow;

    /**
     * 发布状态
     */
    private WorkflowBusStatus status = WorkflowBusStatus.Draft;

    /**
     * 广告版本
     */
    private String adVersion;

    private AdvertisingGroupDTO advertisingGroup;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        AdvertisingDTO node = (AdvertisingDTO) o;

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
