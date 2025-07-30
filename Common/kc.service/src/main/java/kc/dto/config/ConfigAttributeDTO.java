package kc.dto.config;

import kc.dto.PropertyAttributeBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigAttributeDTO extends PropertyAttributeBaseDTO implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4826723150311364934L;

	private String DisplayName;

	private String Description;

	private int ConfigId;

	private boolean IsFileAttr;

	private ConfigEntityDTO ConfigEntity;
}
