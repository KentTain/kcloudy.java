package kc.model.portal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Tables.CompanyAddress)
@Inheritance(strategy = InheritanceType.JOINED)
public class CompanyAddress extends kc.framework.base.Entity implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    /**
     * 公司地址类型：
     *      0: 公司注册地址，1: 公司收货地址，2: 公司发货地址
     */
    @Column(name = "AddressType")
    private int addressType;

    /**
     * 省份Id
     */
    @Column(name = "ProvinceId")
    private int provinceId;
    /**
     * 省份名
     */
    @Column(name = "ProvinceName", length = 128)
    private String provinceName;

    /**
     * 城市Id
     */
    @Column(name = "CityId")
    private int cityId;
    /**
     * 城市名
     */
    @Column(name = "CityName", length = 128)
    private String cityName;

    /**
     * 区域Id
     */
    @Column(name = "DistrictId")
    private int districtId;
    /**
     * 区域名
     */
    @Column(name = "DistrictName", length = 128)
    private String districtName;

    /**
     * 街道Id
     */
    @Column(name = "StreetId")
    private int streetId;
    /**
     * 街道名
     */
    @Column(name = "StreetName", length = 256)
    private String streetName;

    /**
     * 详细地址
     */
    @Column(name = "Address", length = 1024)
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
    @Column(name = "ZipCode", length = 20)
    private String zipCode;

    /**
     * 联系人UserId
     */
    @Column(name = "ContactId", length = 128)
    private String contactId;

    /**
     * 联系人名称
     */
    @Column(name = "ContactName", length = 50)
    private String contactName;

    /**
     * 是否为默认地址
     */
    @Column(name = "IsDefault")
    private boolean isDefault;

    /**
     * 备注
     */
    @Column(name = "Remark", length = 1000)
    private String remark;

    @ManyToOne
    @JoinColumn(name = "CompanyId", nullable = false, foreignKey = @ForeignKey(name = "FK_ptl_CompanyAddress_ptl_CompanyInfo_CompanyId"))
    private CompanyInfo companyInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        if (!super.equals(o))
            return false;

        CompanyAddress node = (CompanyAddress) o;

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
        result = 31 * result + addressType;
        result = 31 * result + (provinceName != null ? provinceName.hashCode() : 0);
        result = 31 * result + (cityName != null ? cityName.hashCode() : 0);
        result = 31 * result + (districtName != null ? districtName.hashCode() : 0);
        result = 31 * result + (streetName != null ? streetName.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
}
