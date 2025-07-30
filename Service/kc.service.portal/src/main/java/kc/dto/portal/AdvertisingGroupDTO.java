package kc.dto.portal;

import kc.dto.EntityBaseDTO;
import kc.model.portal.Tables;
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
@Inheritance(strategy = InheritanceType.JOINED)
public class AdvertisingGroupDTO extends EntityBaseDTO implements java.io.Serializable {

    private int id;
    /**
     * 广告页名称
     */
    private String name;

    /**
     * 展示页面图片
     */
    private String imageBlob;

    /**
     * 展示页面链接地址
     */
    private String link;

    /**
     * 是否启用
     */
    private boolean isEnable;

    /**
     * 描述(显示时，填充html description)
     */
    private String description;

    private List<AdvertisingDTO> advertisings = new ArrayList<AdvertisingDTO>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        AdvertisingGroupDTO node = (AdvertisingGroupDTO) o;

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
