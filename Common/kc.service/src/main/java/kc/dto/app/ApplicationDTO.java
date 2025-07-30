package kc.dto.app;

import kc.dto.EntityDTO;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class ApplicationDTO extends EntityDTO implements java.io.Serializable {

	private static final long serialVersionUID = 3396289557966673057L;

	@com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
	private boolean isEditMode = false;
	/**
	 * 应用程序Id
	 */
	private String applicationId;
	/**
	 * 应用程序编码
	 */
	@Length(min = 1, max = 128, message = "应用程序编码不能超过128个字符")
	private String applicationCode;
	/**
	 * 应用程序名称
	 */
	@Length(min = 1, max = 200, message = "应用程序名称不能超过200个字符")
	private String applicationName;
	/**
	 * 站点名称
	 */
	@Length(min = 0, max = 200, message = "站点名称不能超过200个字符")
	private String webSiteName;
	/**
	 * 版本
	 */
	private int version;

	/**
	 * 域名
	 */
	@Length(min = 0, max = 200, message = "域名不能超过200个字符")
	private String domainName;
	/**
	 * 描述
	 */
	@Length(min = 0, max = 4000, message = "描述不能超过4000个字符")
	private String description;
	/**
	 * 小图标
	 */
	@Length(min = 0, max = 200, message = "小图标不能超过200个字符")
	private String smallIcon;
	/**
	 * 大图标
	 */
	@Length(min = 0, max = 200, message = "大图标不能超过200个字符")
	private String bigIcon;
	/**
	 * 排序
	 */
	private int index;
	/**
	 * 是否开通工作流
	 */
	@com.fasterxml.jackson.annotation.JsonProperty("isEnabledWorkFlow")
	private Boolean isEnabledWorkFlow = false;

	private AppTemplateDTO appTemplate;

	private List<AppSettingDTO> appSettings = new ArrayList<>();
}
