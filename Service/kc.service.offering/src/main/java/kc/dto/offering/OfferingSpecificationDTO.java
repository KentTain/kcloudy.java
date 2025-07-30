package kc.dto.offering;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class OfferingSpecificationDTO {

    /**
     * 规格Id：对应PropertyId
     */
    private Integer specId;

    /**
     * 规格名称：对应PropertyName
     */
    private String specName;

    /**
     * 属性1Id
     */
    private Integer attrId1;
    /**
     * 属性1名称
     */
    private String attrName1;
    /**
     * 属性2Id
     */
    private Integer attrId2;
    /**
     * 属性2名称
     */
    private String attrName2;
    /**
     * 属性3Id
     */
    private Integer attrId3;
    /**
     * 属性3名称
     */
    private String attrName3;
    /**
     * 属性4Id
     */
    private Integer attrId4;
    /**
     * 属性4名称
     */
    private String attrName4;
    /**
     * 属性5Id
     */
    private Integer attrId5;
    /**
     * 属性5名称
     */
    private String attrName5;
    /**
     * 属性6Id
     */
    private Integer attrId6;
    /**
     * 属性6名称
     */
    private String attrName6;
    /**
     * 属性7Id
     */
    private Integer attrId7;
    /**
     * 属性7名称
     */
    private String attrName7;
    /**
     * 属性8Id
     */
    private Integer attrId8;
    /**
     * 属性8名称
     */
    private String attrName8;
    /**
     * 属性9Id
     */
    private Integer attrId9;
    /**
     * 属性9名称
     */
    private String attrName9;
    /**
     * 属性10Id
     */
    private Integer attrId10;
    /**
     * 属性10名称
     */
    private String attrName10;

    /**
     * 是否能编辑
     */
    @com.fasterxml.jackson.annotation.JsonProperty("canEdit")
    private Boolean canEdit;

    /**
     * 是否为必填
     */
    @com.fasterxml.jackson.annotation.JsonProperty("isRequire")
    private Boolean isRequire;

    private Integer index;
}
