package kc.dto.config;

import java.util.ArrayList;
import java.util.List;

import kc.dto.EntityDTO;
import kc.framework.enums.ConfigStatus;
import kc.framework.enums.ConfigType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigEntityDTO extends EntityDTO implements java.io.Serializable{

	private static final long serialVersionUID = -4052400408764598999L;

	private boolean isEditMode;
	
	/**
	 * 配置Id
	 */
	private int configId;

	/**
	 * 配置类型：KC.Enums.Core.ConfigType
	 */
	private ConfigType configType;

	/**
	 * 配置标记
	 */
	private int configSign;

	/**
	 * 配置名称
	 */
	private String configName;

	/**
	 * 配置描述
	 */
	private String configDescription;

	/**
	 * 配置生成的XML
	 */
	private String configXml;

	/**
	 * 配置图片链接
	 */
	private String configImgUrl;

	/**
	 * 配置状态：KC.Enums.Core.ConfigStatus
	 */
	private ConfigStatus state;
	
	/**
	 * 配置代码
	 */
	private String configCode;

	private List<ConfigAttributeDTO> configAttributes = new ArrayList<ConfigAttributeDTO>();
}
