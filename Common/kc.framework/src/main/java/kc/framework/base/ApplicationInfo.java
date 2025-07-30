package kc.framework.base;

import java.util.UUID;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ApplicationInfo implements java.io.Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 6595406167112625267L;

	public ApplicationInfo(String id, String code, String name, String domain, String scope, int index)
     {
         this.appId = id;
         this.appCode = code;
         this.appName = name;
         this.appDomain = domain;
         this.appScope = scope;
         this.index = index;
     }
     private String appId;

    private String appCode;

     private String appName;

     private String appDomain;

     private String appScope;

    private int index;
}
