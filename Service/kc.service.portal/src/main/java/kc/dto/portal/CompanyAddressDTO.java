package kc.dto.portal;

import kc.dto.EntityDTO;
import kc.enums.portal.AddressType;
import kc.model.portal.CompanyInfo;
import kc.model.portal.Tables;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class CompanyAddressDTO extends EntityDTO implements java.io.Serializable {

    private int id;

    /**
     * 公司地址类型：
     *      0: 公司注册地址，1: 公司收货地址，2: 公司发货地址
     */
    private AddressType addressType;
    private String addressTypeString;
    public String getAddressTypeString(){
        if(addressType == null)
            return AddressType.CompanyAddress.getDesc();
        return addressType.getDesc();
    }

    /**
     * 省份Id
     */
    private int provinceId;
    /**
     * 省份名
     */
    private String provinceName;

    /**
     * 城市Id
     */
    private int cityId;
    /**
     * 城市名
     */
    private String cityName;

    /**
     * 区域Id
     */
    private int districtId;
    /**
     * 区域名
     */
    private String districtName;

    /**
     * 街道Id
     */
    private int streetId;
    /**
     * 街道名
     */
    private String streetName;

    /**
     * 详细地址
     */
    @Length(min = 0, max = 256, message = "详细地址不能超过256个字符")
    private String address;

    /**
     * 地图经度坐标
     */
    @Column(name = "LongitudeX")
    public BigDecimal longitudeX;

    /**
     * 地图纬度坐标
     */
    @Column(name = "LatitudeY")
    public BigDecimal latitudeY;

    /**
     * 邮编
     */
    @Length(min = 0, max = 20, message = "邮编不能超过20个字符")
    private String zipCode;

    /**
     * 联系人UserId
     */
    private String contactId;

    /**
     * 联系人名称
     */
    private String contactName;

    /**
     * 是否为默认地址
     */
    @com.fasterxml.jackson.annotation.JsonProperty("isDefault")
    private boolean isDefault;

    /**
     * 备注
     */
    @Column(name = "Remark", length = 1000)
    @Length(min = 0, max = 1000, message = "备注不能超过1000个字符")
    private String remark;

    private CompanyInfoDTO companyInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        CompanyAddressDTO node = (CompanyAddressDTO) o;

        if (id != node.id)
            return false;
        if (addressType != node.addressType)
            return false;

        if (!Objects.equals(provinceName, node.provinceName))
            return false;
        if (!Objects.equals(cityName, node.cityName))
            return false;
        if (!Objects.equals(districtName, node.districtName))
            return false;
        if (!Objects.equals(streetName, node.streetName))
            return false;
        if (!Objects.equals(address, node.address))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        result = 31 * result + addressType.getIndex();
        result = 31 * result + (provinceName != null ? provinceName.hashCode() : 0);
        result = 31 * result + (cityName != null ? cityName.hashCode() : 0);
        result = 31 * result + (districtName != null ? districtName.hashCode() : 0);
        result = 31 * result + (streetName != null ? streetName.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
}
