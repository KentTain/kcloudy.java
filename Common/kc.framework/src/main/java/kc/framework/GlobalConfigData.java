package kc.framework;

import java.util.ArrayList;
import java.util.List;

import kc.framework.base.ApplicationInfo;
import lombok.Data;

@Data
public class GlobalConfigData implements java.io.Serializable {

	private static final long serialVersionUID = 2785196581085926546L;

	private String EncryptKey;
	private String BlobStorage;
	private String AdminEmails;
	private String TempFilePath;

	private String DatabaseConnectionString;
	private String MySqlConnectionString;
	private String StorageConnectionString;
	private String QueueConnectionString;
	private String NoSqlConnectionString;
	private String RedisConnectionString;
	private String ServiceBusConnectionString;
	private String VodConnectionString;
	private String CodeConnectionString;

	private List<ApplicationInfo> Applications = new ArrayList<ApplicationInfo>();
	private ApplicationInfo CurrentApplication;

	/**
	 * subdomain的sso地址：http://sso.kcloudy.com/
	 * 本地测试接口地址：http://localhost:1001/
	 */
	private String SsoWebDomain;

	/**
	 * subdomain的admin地址：http://admin.kcloudy.com/
	 * 本地测试接口地址：http://localhost:1003
	 */
	private String AdminWebDomain;
	/**
	 * subdomain的Blog地址：http://blog.kcloudy.com/
	 * 本地测试接口地址：http://localhost:1005
	 */
	private String BlogWebDomain;
	/**
	 * subdomain的Blog地址：http://code.kcloudy.com/
	 * 本地测试接口地址：http://localhost:1007
	 */
	private String CodeWebDomain;
	/**
	 * subdomain的config地址：http://subdomain.cfg.kcloudy.com/
	 * 本地测试接口地址：http://subdomain.localhost:1101/
	 */
	private String CfgWebDomain;

	/**
	 * subdomain的dictionary地址：http://subdomain.dic.kcloudy.com/
	 * 本地测试接口地址：http://subdomain.localhost:1103/
	 */
	private String DicWebDomain;

	/**
	 * subdomain的app地址：http://subdomain.app.kcloudy.com/
	 * 本地测试接口地址：http://subdomain.localhost:1105
	 */
	private String AppWebDomain;

	/**
	 * subdomain的dictionary地址：http://subdomain.msg.kcloudy.com/
            *     本地测试接口地址：http://subdomain.localhost:1109/
	 */
	private String MsgWebDomain;
	
	/**
	 * subdomain的account地址：http://subdomain.acc.kcloudy.com/
	 * 本地测试接口地址：http://subdomain.localhost:2001/
	 */
	private String AccWebDomain;
	/**
	 * subdomain的electronic contract地址：http://subdomain.econ.kcloudy.com/
	   *    本地测试接口地址：http://subdomain.localhost:2003/
	 */
	private String EconWebDomain;
	/**
	 * subdomain的document地址：http://subdomain.doc.kcloudy.com/
	   *    本地测试接口地址：http://subdomain.localhost:2005/
	 */
	private String DocWebDomain;
	/**
	 * subdomain的Human Resource地址：http://subdomain.hr.kcloudy.com/
	   *    本地测试接口地址：http://subdomain.localhost:2007/
	 */
	private String HrWebDomain;

	/**
	 * subdomain的crm地址：http://subdomain.crm.kcloudy.com/
	 * 本地测试接口地址：http://subdomain.localhost:3001/
	 */
	private String CrmWebDomain;

	/**
	 * subdomain的crm地址：http://subdomain.srm.kcloudy.com/
	 *     本地测试接口地址：http://subdomain.localhost:3003/
	 */
	private String SrmWebDomain;

	/**
	 * subdomain的prd地址：http://subdomain.prd.kcloudy.com/
	 *     本地测试接口地址：http://subdomain.localhost:3005/
	 */
	private String PrdWebDomain;

	/**
	 * subdomain的pmc地址：http://subdomain.pmc.kcloudy.com/
	 *      本地测试接口地址：http://subdomain.localhost:3007/
	 */
	private String PmcWebDomain;
	
	/**
	 * subdomain的电商地址：http://subdomain.shop.kcloudy.com/
	 * 本地测试接口地址：http://subdomain.localhost:4001/
	 */
	private String PortalWebDomain;
	/**
	 * subdomain的som地址：http://subdomain.som.kcloudy.com/
	 *     本地测试接口地址：http://subdomain.localhost:3003/
	 */
	private String SomWebDomain;

	/**
	 * subdomain的Purchase Order地址：http://subdomain.pom.kcloudy.com/
	 *     本地测试接口地址：http://subdomain.localhost:3005/
	 */
	private String PomWebDomain;

	/**
	 * subdomain的WMS地址：http://subdomain.wms.kcloudy.com/
	 *      本地测试接口地址：http://subdomain.localhost:4007/
	 */
	private String WmsWebDomain;
	
	/**
	 * subdomain的融资地址：http://subdomain.market.kcloudy.com/
	 * 本地测试接口地址：http://subdomain.localhost:5001/
	 */
	private String JrWebDomain;
	/**
	 * subdomain的项目管理地址：http://subdomain.prj.kcloudy.com/ </br>
	 * 本地测试接口地址：http://subdomain.localhost:5001/
	 */
	private String PrjWebDomain;

	/**
	 * subdomain的会员管理地址：http://subdomain.mbr.kcloudy.com/ </br>
	 *      本地测试接口地址：http://subdomain.localhost:5005/
	 */
	private String MbrWebDomain;
	/**
	 * subdomain的培训管理地址：http://subdomain.trn.kcloudy.com/
	 * 	本地测试接口地址：http://subdomain.localhost:6001/
	 */
	private String TrainWebDomain;
	/**
	 * subdomain的exam地址：http://subdomain.exam.kcloudy.com/ <br/>
	 * 本地测试接口地址：http://subdomain.localhost:6003/
	 */
	private String ExamWebDomain;
	/**
	 * subdomain的工作流地址：http://subdomain.flow.kcloudy.com/
	 * 本地测试接口地址：http://subdomain.localhost:7001/
	 */
	private String FlowWebDomain;

	/**
	 * subdomain的sso地址：http://subdomain.pay.kcloudy.com/
	 * 本地测试接口地址：http://subdomain.localhost:8001/
	 */
	private String PayWebDomain;

	/**
	 * subdomain的微信地址：http://subdomain.wx.kcloudy.com/
	 * 本地测试接口地址：http://subdomain.localhost:9001/
	 */
	private String WxWebDomain;
	/**
	 * subdomain的资源地址：http://subdomain.resource.kcloudy.com/
	 * 本地测试接口地址：http://subdomain.localhost:9999/
	 */
	private String ResWebDomain;

	/**
	 * subdomain的接口地址，无api/后缀：http://subdomain.api.kcloudy.com/
	 */
	private String ApiWebDomain;
	/**
	 * 上传配置
	 */
	private UploadConfig UploadConfig;
}
