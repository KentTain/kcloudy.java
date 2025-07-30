package kc.model.app;

import kc.framework.base.Entity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@javax.persistence.Entity
@Table(name = Tables.Application)
@Inheritance(strategy = InheritanceType.JOINED)
public class Application extends Entity implements java.io.Serializable {

    private static final long serialVersionUID = 3396289557966673057L;

    /**
     * 应用程序Id
     */
    @Id
    @GeneratedValue(generator="system_uuid")
    @GenericGenerator(name="system_uuid",strategy="uuid")
    //@GeneratedValue(generator = "uuid2")
    //@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ApplicationId")
    private String applicationId;
    /**
     * 应用程序名称
     */
    @Column(name = "ApplicationName", length = 128)
    private String applicationName;
    /**
     * 版本
     */
    @Column(name = "Version")
    private int version;
    /**
     * 应用程序编码
     */
    @Column(name = "ApplicationCode", length = 128)
    private String applicationCode;
    /**
     * 域名
     */
    @Column(name = "DomainName", length = 256)
    private String domainName;
    /**
     * 描述
     */
    @Column(name = "Description", length = 512)
    private String description;
    /**
     * 小图标
     */
    @Column(name = "SmallIcon", length = 256)
    private String smallIcon;
    /**
     * 大图标
     */
    @Column(name = "BigIcon", length = 256)
    private String bigIcon;
    /**
     * 站点名称
     */
    @Column(name = "WebSiteName", length = 128)
    private String webSiteName;
    /**
     * 排序
     */
    @Column(name = "Index")
    private int index;
    /**
     * 是否开通工作流
     */
    @Column(name = "IsEnabledWorkFlow")
    private Boolean isEnabledWorkFlow;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "application")
    private List<AppGit> appGits = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "application")
    private List<AppSetting> appSettings = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "AppTemplateId", nullable = false, foreignKey = @ForeignKey(name="FK_app_Application_app_DevTemplate_AppTemplateId"))
    private DevTemplate appTemplate;
}
