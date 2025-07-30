package kc.framework.tenant;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Dictionary;
import java.util.Enumeration;

import kc.framework.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kc.framework.base.TupleThree;
import kc.framework.GlobalConfig;
import kc.framework.base.ServiceRequestToken;
import kc.framework.extension.ListExtensions;
import kc.framework.extension.StringExtensions;
import kc.framework.security.Base64Provider;
import kc.framework.security.EncryptPasswordUtil;
import kc.framework.security.MD5Provider;

public class TenantConstant {
	private final static Logger logger = LoggerFactory.getLogger(TenantConstant.class);
	public static final CloudType DefaultCloudType = CloudType.Azure;
	public static final int DefaultVersion = 7; //TenantVersion.Standard.getIndex() | TenantVersion.Professional.getIndex() | TenantVersion.Customized.getIndex();
	public static final int DefaultTenantType = 63; //TenantType.Enterprise.getIndex() | TenantType.Institution.getIndex() | TenantType.Bank.getIndex() | TenantType.FinanceLease.getIndex() | TenantType.SmallLoan.getIndex() | TenantType.Bank.getIndex();
	public static final DatabaseType DefaultDatabaseType = DatabaseType.SqlServer;
	public static String DefaultPrivateEncryptKey;
	public static String DefaultDatabaseConnectionString;

    /**
     * 是否开通邮件服务：KC.Enums.Core.ConfigType.EmailConfig = 1
     */
    public static final String PropertyName_EmailSetting = "EnableEmail";
    /**
     * 是否开通短信服务：KC.Enums.Core.ConfigType.SmsConfig = 2
     */
    public static final String PropertyName_SmsSetting = "EnableSms";
    /**
     * 是否开通支付服务：KC.Enums.Core.ConfigType.PaymentMethod = 3
     */
    public static final String PropertyName_PaymentSetting = "EnablePayment";
    /**
     * 是否开通身份认证服务：KC.Enums.Core.ConfigType.ID5 = 4
     */
    public static final String PropertyName_IDSetting = "EnableId5";
    /**
     * 是否开通呼叫中心服务：KC.Enums.Core.ConfigType.CallConfig = 5
     */
    public static final String PropertyName_CallSetting = "EnableCallCenter";
    /**
     * 是否开通物流服务：KC.Enums.Core.ConfigType.LogisticsPlatform = 6
     */
    public static final String PropertyName_LogisticsSetting = "EnableLogistics";
    /**
     * 是否开通微信服务：KC.Enums.Core.ConfigType.WeixinConfig = 7
     */
    public static final String PropertyName_WeixinSetting = "EnableWeixin";
    /**
     * 是否开通邮件服务：KC.Enums.Core.ConfigType.ContractConfig = 8
     */
    public static final String PropertyName_ContractSetting = "EnableContract";
    /**
     * 是否开通独立域名服务：KC.Enums.Core.ConfigType.OwnDomain = 9
     */
    public static final String PropertyName_OwnDomainSetting = "EnableOwnDomainName";
    
    public static final String Default_EmailLimit = "10-200-500";
    public static final String Default_SmsLimit = "5-50-500";
    public static final String Default_IDLimit = "5-10-100";
    public static final String Default_LogisticsLimit = "10-100-500";
    public static final String Default_ContractLimit = "5-20-100";
    

	/**
	 *  域名替换字符串：subdomain
	 */
	public static final String SubDomain = "subdomain";
	
	public static final String ClaimTypes_TenantName = "tenantname";

	public static final int DbaTenantId = 1;
	/// <summary>
	/// cDba: 后台管理
	/// </summary>
	public static final String DbaTenantName = "cDba";
	public static final String DbaTenantDisplayName = "后台管理";
	public static String DbaTenantSignature;
	/// <summary>
	/// Dba's signature to access Webapi
	/// </summary>
	public static Tenant DbaTenantApiAccessInfo;

	public static final int TestTenantId = 2;
	/// <summary>
	/// cTest: 测试用户
	/// </summary>
	public static final String TestTenantName = "cTest";
	public static final String TestTenantDisplayName = "测试租户-核心企业";
	public static String TestTenantSignature;
	/// <summary>
	/// cTest's signature to access Webapi
	/// </summary>
	public static Tenant TestTenantApiAccessInfo;

	public static int BuyTenantId = 3;
	/// <summary>
	/// cBuy: 测试租户-客户
	/// </summary>
	public static final String BuyTenantName = "cBuy";
	public static final String BuyTenantDisplayName = "测试租户-客户";
	public static String BuyTenantSignature;
	/// <summary>
	/// Buy's signature to access Webapi
	/// </summary>
	public static Tenant BuyTenantApiAccessInfo;

	public static final int SaleTenantId = 4;
	/// <summary>
	/// cSale: 测试租户-供应商
	/// </summary>
	public static final String SaleTenantName = "cSale";
	public static final String SaleTenantDisplayName = "测试租户-供应商";
	public static String SaleTenantSignature;
	/// <summary>
	/// Buy's signature to access Webapi
	/// </summary>
	public static Tenant SaleTenantApiAccessInfo;

	public static String[] GetTenantHosts(String tenantName) {
		tenantName = tenantName.toLowerCase();
		switch(GlobalConfig.SystemType)
        {
            case Dev:
            	String[] devhosts =  {
                //--------------本地开发环境：调试--------------
                "http://" + tenantName + ".localhost:1003",//com.web.admin
                "https://" + tenantName + ".localhost:1013",//com.web.admin
                "http://" + tenantName + ".localhost:1004",//com.webapi.admin
                "https://" + tenantName + ".localhost:1014",//com.webapi.admin
                //博客管理
                "http://" + tenantName + ".localhost:1005",//com.web.blog
                "https://" + tenantName + ".localhost:1015",//com.web.blog
                "http://" + tenantName + ".localhost:1006",//com.webapi.blog
                "https://" + tenantName + ".localhost:1016",//com.webapi.blog
                //配置管理
                "http://" + tenantName + ".localhost:1101",//com.web.config
                "https://" + tenantName + ".localhost:1111",//com.web.config
                "http://" + tenantName + ".localhost:1102",//com.webapi.config
                "https://" + tenantName + ".localhost:1112",//com.webapi.config
                //字典管理
                "http://" + tenantName + ".localhost:1103",//com.web.dict
                "https://" + tenantName + ".localhost:1113",//com.web.dict
                "http://" + tenantName + ".localhost:1104",//com.webapi.dict
                "https://" + tenantName + ".localhost:1114",//com.webapi.dict
                //应用管理
                "http://" + tenantName + ".localhost:1105",//com.web.app
                "https://" + tenantName + ".localhost:1115",//com.web.app
                "http://" + tenantName + ".localhost:1106",//com.webapi.app
                "https://" + tenantName + ".localhost:1116",//com.webapi.app
                //消息管理
                "http://" + tenantName + ".localhost:1107",//com.web.message
                "https://" + tenantName + ".localhost:1117",//com.web.message
                "http://" + tenantName + ".localhost:1108",//com.webapi.message
                "https://" + tenantName + ".localhost:1118",//com.webapi.message
                
                //账户管理
                "http://" + tenantName + ".localhost:2001",//com.web.account
                "https://" + tenantName + ".localhost:2011",//com.web.account
                "http://" + tenantName + ".localhost:2002",//com.webapi.account
                "https://" + tenantName + ".localhost:2012",//com.webapi.account
                //合同管理
                "http://" + tenantName + ".localhost:2003",//com.web.contract
                "https://" + tenantName + ".localhost:2013",//com.web.contract
                "http://" + tenantName + ".localhost:2004",//com.webapi.contract
                "https://" + tenantName + ".localhost:2014",//com.webapi.contract
                //文档管理
                "http://" + tenantName + ".localhost:2005",//com.web.doc
                "https://" + tenantName + ".localhost:2015",//com.web.doc
                "http://" + tenantName + ".localhost:2006",//com.webapi.doc
                "https://" + tenantName + ".localhost:2016",//com.webapi.doc
                //人事管理
                "http://" + tenantName + ".localhost:2007",//com.web.hr
                "https://" + tenantName + ".localhost:2017",//com.web.hr
                "http://" + tenantName + ".localhost:2008",//com.webapi.hr
                "https://" + tenantName + ".localhost:2018",//com.webapi.hr
                
                //客户管理
                "http://" + tenantName + ".localhost:3001",//com.web.customer
                "https://" + tenantName + ".localhost:3011",//com.web.customer
                "http://" + tenantName + ".localhost:3002",//com.webapi.customer
                "https://" + tenantName + ".localhost:3012",//com.webapi.customer
                //供应商管理
                "http://" + tenantName + ".localhost:3003",//com.web.supply
                "https://" + tenantName + ".localhost:3013",//com.web.supply
                "http://" + tenantName + ".localhost:3004",//com.webapi.supply
                "https://" + tenantName + ".localhost:3014",//com.webapi.supply
                //商品管理
                "http://" + tenantName + ".localhost:3005",//com.web.product
                "https://" + tenantName + ".localhost:3015",//com.web.product
                "http://" + tenantName + ".localhost:3006",//com.webapi.product
                "https://" + tenantName + ".localhost:3016",//com.webapi.product
                //物料管理
                "http://" + tenantName + ".localhost:3007",//com.web.material
                "https://" + tenantName + ".localhost:3017",//com.web.material
                "http://" + tenantName + ".localhost:3008",//com.webapi.material
                "https://" + tenantName + ".localhost:3018",//com.webapi.material

                //门户管理
                "http://" + tenantName + ".localhost:4001",//com.web.portal
                "https://" + tenantName + ".localhost:4011",//com.web.portal
                "http://" + tenantName + ".localhost:4002",//com.webapi.portal
                "https://" + tenantName + ".localhost:4012",//com.webapi.portal

                //流程管理
                "http://" + tenantName + ".localhost:7001",//com.web.workflow
                "https://" + tenantName + ".localhost:7011",//com.web.workflow
                "http://" + tenantName + ".localhost:7002",//com.webapi.workflow
                "https://" + tenantName + ".localhost:7012",//com.webapi.workflow
                //支付管理
                "http://" + tenantName + ".localhost:8001",//com.web.payment
                "https://" + tenantName + ".localhost:8011",//com.web.payment
                "http://" + tenantName + ".localhost:8002",//com.webapi.payment
                "https://" + tenantName + ".localhost:8012",//com.webapi.payment

                //微信管理
                "http://" + tenantName + ".localhost:9001",//com.web.wechat
                "https://" + tenantName + ".localhost:9011",//com.web.wechat
                "http://" + tenantName + ".localhost:9002",//com.webapi.wechat
                "https://" + tenantName + ".localhost:9012",//com.webapi.wechat

                //--------------本地开发发布环境：linux/IIS--------------
                //配置管理
                "http://" + tenantName + ".localcfg.kcloudy.com",
                "http://" + tenantName + ".localcfgapi.kcloudy.com",
                "https://" + tenantName + ".localcfg.kcloudy.com",
                "https://" + tenantName + ".localcfgapi.kcloudy.com",
                //字典管理
                "http://" + tenantName + ".localdic.kcloudy.com",
                "http://" + tenantName + ".localdicapi.kcloudy.com",
                "https://" + tenantName + ".localdic.kcloudy.com",
                "https://" + tenantName + ".localdicapi.kcloudy.com",
                //应用管理
                "http://" + tenantName + ".localapp.kcloudy.com",
                "http://" + tenantName + ".localappapi.kcloudy.com",
                "https://" + tenantName + ".localapp.kcloudy.com",
                "https://" + tenantName + ".localappapi.kcloudy.com",
                //消息管理
                "http://" + tenantName + ".localmsg.kcloudy.com",
                "http://" + tenantName + ".localmsgapi.kcloudy.com",
                "https://" + tenantName + ".localmsg.kcloudy.com",
                "https://" + tenantName + ".localmsgapi.kcloudy.com",

                //账户管理
                "http://" + tenantName + ".localacc.kcloudy.com",
                "http://" + tenantName + ".localaccapi.kcloudy.com",
                "https://" + tenantName + ".localacc.kcloudy.com",
                "https://" + tenantName + ".localaccapi.kcloudy.com",
                //合同管理
                "http://" + tenantName + ".localecon.kcloudy.com",
                "https://" + tenantName + ".localeconapi.kcloudy.com",
                "http://" + tenantName + ".localecon.kcloudy.com",
                "https://" + tenantName + ".localeconapi.kcloudy.com",
                //文档管理
                "http://" + tenantName + ".localdoc.kcloudy.com",
                "http://" + tenantName + ".localdocapi.kcloudy.com",
                "https://" + tenantName + ".localdoc.kcloudy.com",
                "https://" + tenantName + ".localdocapi.kcloudy.com",
                //人事管理
                "http://" + tenantName + ".localhr.kcloudy.com",
                "http://" + tenantName + ".localhrapi.kcloudy.com",
                "https://" + tenantName + ".localhr.kcloudy.com",
                "https://" + tenantName + ".localhrapi.kcloudy.com",
                
                //客户管理
                "http://" + tenantName + ".localcrm.kcloudy.com",
                "http://" + tenantName + ".localcrmapi.kcloudy.com",
                "https://" + tenantName + ".localcrm.kcloudy.com",
                "https://" + tenantName + ".localcrmapi.kcloudy.com",
                //供应商管理
                "http://" + tenantName + ".localsrm.kcloudy.com",
                "http://" + tenantName + ".localsrmapi.kcloudy.com",
                "https://" + tenantName + ".localsrm.kcloudy.com",
                "https://" + tenantName + ".localsrmapi.kcloudy.com",
                //商品管理
                "http://" + tenantName + ".localprd.kcloudy.com",
                "http://" + tenantName + ".localprdapi.kcloudy.com",
                "https://" + tenantName + ".localprd.kcloudy.com",
                "https://" + tenantName + ".localprdapi.kcloudy.com",
                //物料管理
                "http://" + tenantName + ".localpmc.kcloudy.com",
                "http://" + tenantName + ".localpmcapi.kcloudy.com",
                "https://" + tenantName + ".localpmc.kcloudy.com",
                "https://" + tenantName + ".localpmcapi.kcloudy.com",
                
                //门户管理
                "http://" + tenantName + ".local.kcloudy.com",//com.web.portal
                "https://" + tenantName + ".local.kcloudy.com",//com.web.portal
                "http://" + tenantName + ".localapi.kcloudy.com",//com.webapi.portal
                "https://" + tenantName + ".localapi.kcloudy.com",//com.webapi.portal

                //流程管理
                "http://" + tenantName + ".localflow.kcloudy.com",//com.web.workflow
                "https://" + tenantName + ".localflow.kcloudy.com",//com.web.workflow
                "http://" + tenantName + ".localflowapi.kcloudy.com",//com.webapi.workflow
                "https://" + tenantName + ".localflowapi.kcloudy.com",//com.webapi.workflow
                //支付管理
                "http://" + tenantName + ".localpay.kcloudy.com",//com.web.payment
                "https://" + tenantName + ".localpay.kcloudy.com",//com.web.payment
                "http://" + tenantName + ".localpayapi.kcloudy.com",//com.webapi.payment
                "https://" + tenantName + ".localpayapi.kcloudy.com",//com.webapi.payment

                //微信管理
                "http://" + tenantName + ".localwx.kcloudy.com",//com.web.wechat
                "https://" + tenantName + ".localwx.kcloudy.com",//com.web.wechat
                "http://" + tenantName + ".localwxapi.kcloudy.com",//com.webapi.wechat
                "https://" + tenantName + ".localwxapi.kcloudy.com",//com.webapi.wechat
            };
            	return devhosts;
            case Test:
            	String[] testhosts =  {
                //--------------测试环境--------------
                //配置管理
                "http://" + tenantName + ".testcfg.kcloudy.com",
                "http://" + tenantName + ".testcfgapi.kcloudy.com",
                "https://" + tenantName + ".testcfg.kcloudy.com",
                "https://" + tenantName + ".testcfgapi.kcloudy.com",
                //字典管理
                "http://" + tenantName + ".testdic.kcloudy.com",
                "http://" + tenantName + ".testdicapi.kcloudy.com",
                "https://" + tenantName + ".testdic.kcloudy.com",
                "https://" + tenantName + ".testdicapi.kcloudy.com",
                //应用管理
                "http://" + tenantName + ".testapp.kcloudy.com",
                "http://" + tenantName + ".testappapi.kcloudy.com",
                "https://" + tenantName + ".testapp.kcloudy.com",
                "https://" + tenantName + ".testappapi.kcloudy.com",
                //消息管理
                "http://" + tenantName + ".testmsg.kcloudy.com",
                "http://" + tenantName + ".testmsgapi.kcloudy.com",
                "https://" + tenantName + ".testmsg.kcloudy.com",
                "https://" + tenantName + ".testmsgapi.kcloudy.com",
                
                //账户管理
                "http://" + tenantName + ".testacc.kcloudy.com",
                "http://" + tenantName + ".testaccapi.kcloudy.com",
                "https://" + tenantName + ".testacc.kcloudy.com",
                "https://" + tenantName + ".testaccapi.kcloudy.com",
                //合同管理
                "http://" + tenantName + ".testecon.kcloudy.com",//com.web.contract
                "https://" + tenantName + ".testeconapi.kcloudy.com",//com.web.contract
                "http://" + tenantName + ".testecon.kcloudy.com",//com.webapi.contract
                "https://" + tenantName + ".testeconapi.kcloudy.com",//com.webapi.contract
                //文档管理
                "http://" + tenantName + ".testdoc.kcloudy.com",
                "http://" + tenantName + ".testdocapi.kcloudy.com",
                "https://" + tenantName + ".testdoc.kcloudy.com",
                "https://" + tenantName + ".testdocapi.kcloudy.com",
                //人事管理
                "http://" + tenantName + ".testhr.kcloudy.com",
                "http://" + tenantName + ".testhrapi.kcloudy.com",
                "https://" + tenantName + ".testhr.kcloudy.com",
                "https://" + tenantName + ".testhrapi.kcloudy.com",
                
                //客户管理
                "http://" + tenantName + ".testcrm.kcloudy.com",
                "http://" + tenantName + ".testcrmapi.kcloudy.com",
                "https://" + tenantName + ".testcrm.kcloudy.com",
                "https://" + tenantName + ".testcrmapi.kcloudy.com",
                //供应商管理
                "http://" + tenantName + ".testsrm.kcloudy.com",
                "http://" + tenantName + ".testsrmapi.kcloudy.com",
                "https://" + tenantName + ".testsrm.kcloudy.com",
                "https://" + tenantName + ".testsrmapi.kcloudy.com",
                //商品管理
                "http://" + tenantName + ".testprd.kcloudy.com",
                "http://" + tenantName + ".testprdapi.kcloudy.com",
                "https://" + tenantName + ".testprd.kcloudy.com",
                "https://" + tenantName + ".testprdapi.kcloudy.com",
                //物料管理
                "http://" + tenantName + ".testpmc.kcloudy.com",
                "http://" + tenantName + ".testpmcapi.kcloudy.com",
                "https://" + tenantName + ".testpmc.kcloudy.com",
                "https://" + tenantName + ".testpmcapi.kcloudy.com",

                //门户管理
                "http://" + tenantName + ".test.kcloudy.com",//com.web.portal
                "https://" + tenantName + ".test.kcloudy.com",//com.web.portal
                "http://" + tenantName + ".test.kcloudy.com",//com.webapi.portal
                "https://" + tenantName + ".test.kcloudy.com",//com.webapi.portal
                
                //流程管理
                "http://" + tenantName + ".testflow.kcloudy.com",//com.web.workflow
                "https://" + tenantName + ".testflow.kcloudy.com",//com.web.workflow
                "http://" + tenantName + ".testflowapi.kcloudy.com",//com.webapi.workflow
                "https://" + tenantName + ".testflowapi.kcloudy.com",//com.webapi.workflow
                //支付管理
                "http://" + tenantName + ".testpay.kcloudy.com",//com.web.payment
                "https://" + tenantName + ".testpay.kcloudy.com",//com.web.payment
                "http://" + tenantName + ".testpayapi.kcloudy.com",//com.webapi.payment
                "https://" + tenantName + ".testpayapi.kcloudy.com",//com.webapi.payment

                //微信管理
                "http://" + tenantName + ".testwx.kcloudy.com",//com.web.wechat
                "https://" + tenantName + ".testwx.kcloudy.com",//com.web.wechat
                "http://" + tenantName + ".testwxapi.kcloudy.com",//com.webapi.wechat
                "https://" + tenantName + ".testwxapi.kcloudy.com",//com.webapi.wechat
            };
            	return testhosts;
            case Beta:
                String[] betahosts = {
                //--------------灰度发布环境--------------
                //配置管理
                "http://" + tenantName + ".betacfg.kcloudy.com",
                "http://" + tenantName + ".betacfgapi.kcloudy.com",
                "https://" + tenantName + ".betacfg.kcloudy.com",
                "https://" + tenantName + ".betacfgapi.kcloudy.com",
                //字典管理
                "http://" + tenantName + ".betadic.kcloudy.com",
                "http://" + tenantName + ".betadicapi.kcloudy.com",
                "https://" + tenantName + ".betadic.kcloudy.com",
                "https://" + tenantName + ".betadicapi.kcloudy.com",
                //应用管理
                "http://" + tenantName + ".betaapp.kcloudy.com",
                "http://" + tenantName + ".betaappapi.kcloudy.com",
                "https://" + tenantName + ".betaapp.kcloudy.com",
                "https://" + tenantName + ".betaappapi.kcloudy.com",
                //消息管理
                "http://" + tenantName + ".betamsg.kcloudy.com",
                "http://" + tenantName + ".betamsgapi.kcloudy.com",
                "https://" + tenantName + ".betamsg.kcloudy.com",
                "https://" + tenantName + ".betamsgapi.kcloudy.com",

                //账户管理
                "http://" + tenantName + ".betaacc.kcloudy.com",
                "http://" + tenantName + ".betaaccapi.kcloudy.com",
                "https://" + tenantName + ".betaacc.kcloudy.com",
                "https://" + tenantName + ".betaaccapi.kcloudy.com",
                //合同管理
                "http://" + tenantName + ".betaecon.kcloudy.com",
                "https://" + tenantName + ".betaeconapi.kcloudy.com",
                "http://" + tenantName + ".betaecon.kcloudy.com",
                "https://" + tenantName + ".betaeconapi.kcloudy.com",
                //文档管理
                "http://" + tenantName + ".betadoc.kcloudy.com",
                "http://" + tenantName + ".betadocapi.kcloudy.com",
                "https://" + tenantName + ".betadoc.kcloudy.com",
                "https://" + tenantName + ".betadocapi.kcloudy.com",
                //人事管理
                "http://" + tenantName + ".betahr.kcloudy.com",
                "http://" + tenantName + ".betahrapi.kcloudy.com",
                "https://" + tenantName + ".betahr.kcloudy.com",
                "https://" + tenantName + ".betahrapi.kcloudy.com",
                
                //客户管理
                "http://" + tenantName + ".betacrm.kcloudy.com",
                "http://" + tenantName + ".betacrmapi.kcloudy.com",
                "https://" + tenantName + ".betacrm.kcloudy.com",
                "https://" + tenantName + ".betacrmapi.kcloudy.com",
                //供应商管理
                "http://" + tenantName + ".betasrm.kcloudy.com",
                "http://" + tenantName + ".betasrmapi.kcloudy.com",
                "https://" + tenantName + ".betasrm.kcloudy.com",
                "https://" + tenantName + ".betasrmapi.kcloudy.com",
                //商品管理
                "http://" + tenantName + ".betaprd.kcloudy.com",
                "http://" + tenantName + ".betaprdapi.kcloudy.com",
                "https://" + tenantName + ".betaprd.kcloudy.com",
                "https://" + tenantName + ".betaprdapi.kcloudy.com",
                //物料管理
                "http://" + tenantName + ".betapmc.kcloudy.com",
                "http://" + tenantName + ".betapmcapi.kcloudy.com",
                "https://" + tenantName + ".betapmc.kcloudy.com",
                "https://" + tenantName + ".betapmcapi.kcloudy.com",
                
                //门户管理
                "http://" + tenantName + ".beta.kcloudy.com",//com.web.portal
                "https://" + tenantName + ".beta.kcloudy.com",//com.web.portal
                "http://" + tenantName + ".beta.kcloudy.com",//com.webapi.portal
                "https://" + tenantName + ".beta.kcloudy.com",//com.webapi.portal

                //流程管理
                "http://" + tenantName + ".betaflow.kcloudy.com",//com.web.workflow
                "https://" + tenantName + ".betaflow.kcloudy.com",//com.web.workflow
                "http://" + tenantName + ".betaflowapi.kcloudy.com",//com.webapi.workflow
                "https://" + tenantName + ".betaflowapi.kcloudy.com",//com.webapi.workflow
                //支付管理
                "http://" + tenantName + ".betapay.kcloudy.com",//com.web.payment
                "https://" + tenantName + ".betapay.kcloudy.com",//com.web.payment
                "http://" + tenantName + ".betapayapi.kcloudy.com",//com.webapi.payment
                "https://" + tenantName + ".betapayapi.kcloudy.com",//com.webapi.payment

                //微信管理
                "http://" + tenantName + ".betawx.kcloudy.com",//com.web.wechat
                "https://" + tenantName + ".betawx.kcloudy.com",//com.web.wechat
                "http://" + tenantName + ".betawxapi.kcloudy.com",//com.webapi.wechat
                "https://" + tenantName + ".betawxapi.kcloudy.com",//com.webapi.wechat
                
            };
                return betahosts;
            default:
                String[] prdhosts = {
                //--------------正式发布环境--------------
                //配置管理
                "http://" + tenantName + ".cfg.kcloudy.com",
                "http://" + tenantName + ".cfgapi.kcloudy.com",
                "https://" + tenantName + ".cfg.kcloudy.com",
                "https://" + tenantName + ".cfgapi.kcloudy.com",
                //字典管理
                "http://" + tenantName + ".dic.kcloudy.com",
                "http://" + tenantName + ".dicapi.kcloudy.com",
                "https://" + tenantName + ".dic.kcloudy.com",
                "https://" + tenantName + ".dicapi.kcloudy.com",
                //应用管理
                "http://" + tenantName + ".app.kcloudy.com",
                "http://" + tenantName + ".appapi.kcloudy.com",
                "https://" + tenantName + ".app.kcloudy.com",
                "https://" + tenantName + ".appapi.kcloudy.com",
                //消息管理
                "http://" + tenantName + ".msg.kcloudy.com",
                "http://" + tenantName + ".msgapi.kcloudy.com",
                "https://" + tenantName + ".msg.kcloudy.com",
                "https://" + tenantName + ".msgapi.kcloudy.com",

                //账户管理
                "http://" + tenantName + ".acc.kcloudy.com",
                "http://" + tenantName + ".accapi.kcloudy.com",
                "https://" + tenantName + ".acc.kcloudy.com",
                "https://" + tenantName + ".accapi.kcloudy.com",
                //合同管理
                "http://" + tenantName + ".econ.kcloudy.com",
                "https://" + tenantName + ".econapi.kcloudy.com",
                "http://" + tenantName + ".econ.kcloudy.com",
                "https://" + tenantName + ".econapi.kcloudy.com",
                //文档管理
                "http://" + tenantName + ".doc.kcloudy.com",
                "http://" + tenantName + ".docapi.kcloudy.com",
                "https://" + tenantName + ".doc.kcloudy.com",
                "https://" + tenantName + ".docapi.kcloudy.com",
                //人事管理
                "http://" + tenantName + ".hr.kcloudy.com",
                "http://" + tenantName + ".hrapi.kcloudy.com",
                "https://" + tenantName + ".hr.kcloudy.com",
                "https://" + tenantName + ".hrapi.kcloudy.com",
                
                //客户管理
                "http://" + tenantName + ".crm.kcloudy.com",
                "http://" + tenantName + ".crmapi.kcloudy.com",
                "https://" + tenantName + ".crm.kcloudy.com",
                "https://" + tenantName + ".crmapi.kcloudy.com",
                //供应商管理
                "http://" + tenantName + ".srm.kcloudy.com",
                "http://" + tenantName + ".srmapi.kcloudy.com",
                "https://" + tenantName + ".srm.kcloudy.com",
                "https://" + tenantName + ".srmapi.kcloudy.com",
                //商品管理
                "http://" + tenantName + ".prd.kcloudy.com",
                "http://" + tenantName + ".prdapi.kcloudy.com",
                "https://" + tenantName + ".prd.kcloudy.com",
                "https://" + tenantName + ".prdapi.kcloudy.com",
                //物料管理
                "http://" + tenantName + ".pmc.kcloudy.com",
                "http://" + tenantName + ".pmcapi.kcloudy.com",
                "https://" + tenantName + ".pmc.kcloudy.com",
                "https://" + tenantName + ".pmcapi.kcloudy.com",

                //门户管理
                "http://" + tenantName + ".kcloudy.com",//com.web.portal
                "https://" + tenantName + ".kcloudy.com",//com.web.portal
                "http://" + tenantName + ".kcloudy.com",//com.webapi.portal
                "https://" + tenantName + ".kcloudy.com",//com.webapi.portal

                //流程管理
                "http://" + tenantName + ".flow.kcloudy.com",//com.web.workflow
                "https://" + tenantName + ".flow.kcloudy.com",//com.web.workflow
                "http://" + tenantName + ".flowapi.kcloudy.com",//com.webapi.workflow
                "https://" + tenantName + ".flowapi.kcloudy.com",//com.webapi.workflow
                //支付管理
                "http://" + tenantName + ".pay.kcloudy.com",//com.web.payment
                "https://" + tenantName + ".pay.kcloudy.com",//com.web.payment
                "http://" + tenantName + ".payapi.kcloudy.com",//com.webapi.payment
                "https://" + tenantName + ".payapi.kcloudy.com",//com.webapi.payment

                //微信管理
                "http://" + tenantName + ".wx.kcloudy.com",//com.web.wechat
                "https://" + tenantName + ".wx.kcloudy.com",//com.web.wechat
                "http://" + tenantName + ".wxapi.kcloudy.com",//com.webapi.wechat
                "https://" + tenantName + ".wxapi.kcloudy.com",//com.webapi.wechat
            };
                return prdhosts;
        }
	}

	/**
	 * 获取数据库服务器的连接信息：Tuple<server, database, password>
	 * @param isEncryptPassword 是否将数据库登录密码进行加密处理：秘钥为配置文件中的EncryptKey
	 * @return
	 */
	private static final TupleThree<String, String, String> GetDatabaseConnectionItems(String connectString, boolean isEncryptPassword) {
		String server = "";
		String database = "";
		String passwordHash = "";

		try {
			if(StringExtensions.isNullOrEmpty(connectString))
				connectString = DefaultDatabaseConnectionString;
			Dictionary<String, String> dicCon = StringExtensions.keyValuePairFromConnectionString(connectString);
			passwordHash = isEncryptPassword ? EncryptPasswordUtil.EncryptPassword("P@ssw0rd", DefaultPrivateEncryptKey)
					: "P@ssw0rd";
			Enumeration<String> keys = dicCon.keys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				if (key.equals("server")) {
					server = dicCon.get("server");
				} else if (key.equals("data source")) {
					server = dicCon.get("data source");
				}

				if (key.equals("server")) {
					server = dicCon.get("server");
				} else if (key.equals("data source")) {
					server = dicCon.get("data source");
				}

				if (key.equals("database")) {
					database = dicCon.get("database");
				}
			}
		} catch (Exception e) {
			logger.error("Method GetDatabaseConnectionItems throw exception: " + e.getMessage());
		}
		return new TupleThree<String, String, String>(server, database, passwordHash);
	}

	public static final StorageType DefaultStorageType = StorageType.Blob;
	public static String DefaultStorageConnectionString;

	/**
	 * 获取存储服务器的连接信息：Tuple<endpont, accessName, accessKey>
	 * @param isEncryptPassword 是否将存储服务器的访问秘钥进行加密处理：加密Key为配置文件中的EncryptKey
	 * @return
	 */
	private static final TupleThree<String, String, String> GetStorageConnectionItems(String dataConnString, boolean isEncryptPassword) {
		String endpont = "";
		String accessName = "";
		String accessKey = "";
		String encryptKey = "";

		try {
			if(StringExtensions.isNullOrEmpty(dataConnString))
				dataConnString = DefaultStorageConnectionString;
			Dictionary<String, String> keyValues = StringExtensions.keyValuePairFromConnectionString(dataConnString);
			endpont = keyValues.get(ConnectionKeyConstant.BlobEndpoint);
			accessName = keyValues.get(ConnectionKeyConstant.AccessName);
			accessKey = keyValues.get(ConnectionKeyConstant.AccessKey);
			encryptKey = isEncryptPassword ? EncryptPasswordUtil.EncryptPassword(accessKey, DefaultPrivateEncryptKey)
					: accessKey;
		} catch (Exception e) {
			logger.error("Method GetStorageConnectionItems throw exception: " + e.getMessage());
		}

		return new TupleThree<String, String, String>(endpont, accessName, encryptKey);
	}

	public static final QueueType DefaultQueueType = QueueType.AzureQueue;
	public static String DefaultQueueConnectionString;

	/**
	 * 获取队列服务器的连接信息：Tuple<endpont, accessName, accessKey>
	 * @param isEncryptPassword 是否将队列服务器的访问秘钥进行加密处理：加密Key为配置文件中的EncryptKey
	 * @return
	 */
	private static final TupleThree<String, String, String> GetQueueConnectionItems(String dataConnString, boolean isEncryptPassword) {
		String endpont = "";
		String accessName = "";
		String accessKey = "";
		String encryptKey = "";

		try {
			if(StringExtensions.isNullOrEmpty(dataConnString))
				dataConnString = DefaultQueueConnectionString;
			Dictionary<String, String> keyValues = StringExtensions.keyValuePairFromConnectionString(dataConnString);
			endpont = keyValues.get(ConnectionKeyConstant.QueueEndpoint);
			accessName = keyValues.get(ConnectionKeyConstant.AccessName);
			accessKey = keyValues.get(ConnectionKeyConstant.AccessKey);
			encryptKey = isEncryptPassword ? EncryptPasswordUtil.EncryptPassword(accessKey, DefaultPrivateEncryptKey)
					: accessKey;
		} catch (Exception e) {
			logger.error("Method GetQueueConnectionItems throw exception: " + e.getMessage());
		}

		return new TupleThree<String, String, String>(endpont, accessName, encryptKey);
	}

	public static final NoSqlType DefaultNoSqlType = NoSqlType.AzureTable;
	public static String DefaultNoSqlConnectionString;


	/**
	 * 获取NoSql服务器的连接信息：Tuple<endpont, accessName, accessKey>
	 * @param isEncryptPassword 是否将NoSql服务器的访问秘钥进行加密处理：加密Key为配置文件中的EncryptKey
	 * @return
	 */
	private static final TupleThree<String, String, String> GetNoSqlConnectionItems(String dataConnString, boolean isEncryptPassword) {
		String endpont = "";
		String accessName = "";
		String accessKey = "";
		String encryptKey = "";

		try {
			if(StringExtensions.isNullOrEmpty(dataConnString))
				dataConnString = DefaultNoSqlConnectionString;
			Dictionary<String, String> keyValues = StringExtensions.keyValuePairFromConnectionString(dataConnString);
			endpont = keyValues.get(ConnectionKeyConstant.NoSqlEndpoint);
			accessName = keyValues.get(ConnectionKeyConstant.AccessName);
			accessKey = keyValues.get(ConnectionKeyConstant.AccessKey);
			encryptKey = isEncryptPassword ? EncryptPasswordUtil.EncryptPassword(accessKey, DefaultPrivateEncryptKey)
					: accessKey;
		} catch (Exception e) {
			logger.error("Method GetNoSqlConnectionItems throw exception: " + e.getMessage());
		}

		return new TupleThree<String, String, String>(endpont, accessName, encryptKey);
	}

	public static final VodType DefaultVodType = VodType.Aliyun;
	public static String DefaultVodConnectionString;

	/**
	 * 获取Vod服务器的连接信息：Tuple<endpont, accessName, accessKey>
	 * @param isEncryptPassword 是否将分布式服务服务器的访问秘钥进行加密处理：加密Key为配置文件中的EncryptKey
	 * @return
	 */
	private static final TupleThree<String, String, String> GetVodConnectionItems(String dataConnString, boolean isEncryptPassword) {
		String endpont = "";
		String accessName = "";
		String accessKey = "";
		String encryptKey = "";
		try {
			if(StringExtensions.isNullOrEmpty(dataConnString))
				dataConnString = DefaultVodConnectionString;
			Dictionary<String, String> keyValues = StringExtensions.keyValuePairFromConnectionString(dataConnString);
			endpont = keyValues.get(ConnectionKeyConstant.VodEndpoint);
			accessName = keyValues.get(ConnectionKeyConstant.AccessName);
			accessKey = keyValues.get(ConnectionKeyConstant.AccessKey);
			encryptKey = isEncryptPassword ? EncryptPasswordUtil.EncryptPassword(accessKey, DefaultPrivateEncryptKey)
					: accessKey;
		} catch (Exception e) {
			logger.error("Method GetVodConnectionItems throw exception: " + e.getMessage());
		}
		return new TupleThree<String, String, String>(endpont, accessName, encryptKey);
	}


	public static final CodeType DefaultCodeType = CodeType.Gitlab;
	public static String DefaultCodeConnectionString;

	/**
	 * 获取代码仓库的连接信息：Tuple<endpont, accessName, accessKey>
	 * @param isEncryptPassword 是否将分布式服务服务器的访问秘钥进行加密处理：加密Key为配置文件中的EncryptKey
	 * @return
	 */
	private static final TupleThree<String, String, String> GetCodeConnectionItems(String dataConnString, boolean isEncryptPassword) {
		String endpont = "";
		String accessName = "";
		String accessKey = "";
		String encryptKey = "";
		try {
			if(StringExtensions.isNullOrEmpty(dataConnString))
				dataConnString = DefaultCodeConnectionString;
			Dictionary<String, String> keyValues = StringExtensions.keyValuePairFromConnectionString(dataConnString);
			endpont = keyValues.get(ConnectionKeyConstant.CodeEndpoint);
			accessName = keyValues.get(ConnectionKeyConstant.AccessName);
			accessKey = keyValues.get(ConnectionKeyConstant.AccessKey);
			encryptKey = isEncryptPassword ? EncryptPasswordUtil.EncryptPassword(accessKey, DefaultPrivateEncryptKey)
					: accessKey;
		} catch (Exception e) {
			logger.error("Method GetCodeConnectionItems throw exception: " + e.getMessage());
		}
		return new TupleThree<String, String, String>(endpont, accessName, encryptKey);
	}
	
	public static final ServiceBusType DefaultServiceBusType = ServiceBusType.ServiceBus;
	public static String DefaultServiceBusConnectionString;

	/**
	 * 获取分布式服务服务器的连接信息：Tuple<endpont, accessName, accessKey>
	 * @param isEncryptPassword 是否将分布式服务服务器的访问秘钥进行加密处理：加密Key为配置文件中的EncryptKey
	 * @return
	 */
	private static final TupleThree<String, String, String> GetServiceBusConnectionItems(String dataConnString, boolean isEncryptPassword) {
		String endpont = "";
		String accessName = "";
		String accessKey = "";
		String encryptKey = "";
		try {
			if(StringExtensions.isNullOrEmpty(dataConnString))
				dataConnString = DefaultServiceBusConnectionString;
			Dictionary<String, String> keyValues = StringExtensions.keyValuePairFromConnectionString(dataConnString);
			endpont = keyValues.get(ConnectionKeyConstant.ServiceBusEndpoint);
			accessName = keyValues.get(ConnectionKeyConstant.ServiceBusAccessName);
			accessKey = keyValues.get(ConnectionKeyConstant.ServiceBusAccessKey);
			encryptKey = isEncryptPassword ? EncryptPasswordUtil.EncryptPassword(accessKey, DefaultPrivateEncryptKey)
					: accessKey;
		} catch (Exception e) {
			logger.error("Method GetServiceBusConnectionItems throw exception: " + e.getMessage());
		}
		return new TupleThree<String, String, String>(endpont, accessName, encryptKey);
	}


	public static String GetClientIdByTenant(Tenant tenant) {
		if (StringExtensions.isNullOrEmpty(tenant.getTenantName()))
			return null;

		return Base64Provider.EncodeString(tenant.getTenantName());
	}

	public static String GetClientSecretByTenant(Tenant tenant) {
		if (StringExtensions.isNullOrEmpty(tenant.getTenantName()))
			return null;

		try {
			String key = tenant.getTenantName().toLowerCase() + ":" + tenant.getPrivateEncryptKey();
			String md5Key = MD5Provider.Hash(key, true);
			
			return Base64Provider.EncodeString(md5Key);
		} catch (Exception e) {
			logger.error("Method GetClientSecretByTenant throw exception: " + e.getMessage());
			return null;
		}
	}

	public static String GetEncodeClientId(String encodeClientId) {
		if (StringExtensions.isNullOrEmpty(encodeClientId))
			return null;
		return Base64Provider.EncodeString(encodeClientId);
	}

	public static String GetDecodeClientId(String encodeClientId) {
		if (StringExtensions.isNullOrEmpty(encodeClientId))
			return null;
		try {
			String clientId = Base64Provider.DecodeString(encodeClientId);
			return clientId;
		} catch (Exception e) {
			logger.error("Method GetDecodeClientId throw exception: " + e.getMessage());
			return encodeClientId;
		}
	}
	
	/**
	 * Creates a SHA256 hash of the specified input.
	 * @param input 输入字符串
	 * @return
	 */
	public static String Sha256(String input) {
		return SHA(input, "SHA-256");
	}

	/**
	 * Creates a SHA512 hash of the specified input.
	 * @param input 输入字符串
	 * @return
	 */
	public static String Sha512(String input) {
		return SHA(input, "SHA-512");
	}

	/**
	 * 字符串 SHA 加密
	 * 
	 * @param input
	 * @return
	 */
	private static String SHA(final String input, final String strType) {
		if (StringExtensions.isNullOrEmpty(input))
			return "";

		// 传入要加密的字符串
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(strType);
			messageDigest.update(input.getBytes("UTF-8"));
			// 得到 byte
			byte[] byteBuffer = messageDigest.digest();

			return Base64Provider.Encode(byteBuffer);
		} catch (UnsupportedEncodingException e) {
			logger.error("Method Sha256 throw exception: " + e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			logger.error("Method Sha256 throw exception: " + e.getMessage());
		}

		return "";
	}

	public static void InitTestTenant() {
		DefaultPrivateEncryptKey = StringExtensions.isNullOrEmpty(GlobalConfig.EncryptKey)
				? EncryptPasswordUtil.DEFAULT_Key
				: GlobalConfig.EncryptKey;

		DefaultDatabaseConnectionString = StringExtensions.isNullOrEmpty(GlobalConfig.DatabaseConnectionString)
				? "Server=localhost;Database=MSSqlKCContext;User ID=sa;Password=0QVw0yFoX2GuwkMSQyz1tg==;MultipleActiveResultSets=true;"
				: GlobalConfig.GetDecryptDatabaseConnectionString();

		DefaultStorageConnectionString = StringExtensions.isNullOrEmpty(GlobalConfig.StorageConnectionString)
				? "BlobEndpoint=https://cfwinstorage.blob.core.chinacloudapi.cn/;DefaultEndpointsProtocol=https;AccountName=cfwinstorage;AccountKey=cF0V6oCzMrat9RYDOyRfuVdUKI64x5mOfFipfUzVklho/Y2EEVyZ21Ip3zxYoHrw7U9nuF3wzNt/QvFSH1NIZQ=="
				: GlobalConfig.GetDecryptStorageConnectionString();

		DefaultQueueConnectionString = StringExtensions.isNullOrEmpty(GlobalConfig.QueueConnectionString)
				? "QueueEndpoint=https://cfwinstorage.queue.core.chinacloudapi.cn/;DefaultEndpointsProtocol=https;AccountName=cfwinstorage;AccountKey=cF0V6oCzMrat9RYDOyRfuVdUKI64x5mOfFipfUzVklho/Y2EEVyZ21Ip3zxYoHrw7U9nuF3wzNt/QvFSH1NIZQ=="
				: GlobalConfig.GetDecryptQueueConnectionString();

		DefaultNoSqlConnectionString = StringExtensions.isNullOrEmpty(GlobalConfig.NoSqlConnectionString)
				? "TableEndpoint=https://cfwinstorage.table.core.chinacloudapi.cn/;DefaultEndpointsProtocol=https;AccountName=cfwinstorage;AccountKey=cF0V6oCzMrat9RYDOyRfuVdUKI64x5mOfFipfUzVklho/Y2EEVyZ21Ip3zxYoHrw7U9nuF3wzNt/QvFSH1NIZQ=="
				: GlobalConfig.GetDecryptNoSqlConnectionString();

		DefaultVodConnectionString = StringExtensions.isNullOrEmpty(GlobalConfig.VodConnectionString)
				? "VodEndpoint=cn-shanghai;AccountName=LTAI5t7eCSkzioB4v52sGQED;AccountKey=t0O9KqQ5cUpm2oPSMWKXKanjDGgLaRl0oWld6JBDVGQ="
				: GlobalConfig.GetDecryptVodConnectionString();

		DefaultCodeConnectionString = StringExtensions.isNullOrEmpty(GlobalConfig.CodeConnectionString)
				? "TableEndpoint=CodeEndpoint=http://gitlab.kcloudy.com;AccountName=test-token;AccountKey=t0O9KqQ5cUpm2oPSMWKXKanjDGgLaRl0oWld6JBDVGQ="
				: GlobalConfig.GetDecryptCodeConnectionString();

		DefaultServiceBusConnectionString = StringExtensions.isNullOrEmpty(GlobalConfig.ServiceBusConnectionString)
				? "Endpoint=r-8vb7b64e3efvcnhjl1.redis.zhangbei.rds.aliyuncs.com:6379;SharedAccessKeyName=r-8vb7b64e3efvcnhjl1;SharedAccessKey=xn0xmtohOPBMWDiUK4LdCFdanhIE1Gbk"
				: GlobalConfig.GetDecryptServiceBusConnectionString();

		TupleThree<String, String, String> connItem = GetDatabaseConnectionItems(DefaultDatabaseConnectionString, true);
		final String server = connItem.Item1;
		final String database = connItem.Item2;
		final String password = connItem.Item3;

		TupleThree<String, String, String> storageItem = GetStorageConnectionItems(DefaultStorageConnectionString, true);
		final String endpoint = storageItem.Item1;
		final String accessName = storageItem.Item2;
		final String accessKey = storageItem.Item3;

		TupleThree<String, String, String> queueItem = GetQueueConnectionItems(DefaultQueueConnectionString, true);
		final String queueEndpoint = queueItem.Item1;
		final String queueName = queueItem.Item2;
		final String queueKey = queueItem.Item3;

		TupleThree<String, String, String> nosqlItem = GetNoSqlConnectionItems(DefaultNoSqlConnectionString, true);
		final String noSqlEndpoint = nosqlItem.Item1;
		final String noSqlName = nosqlItem.Item2;
		final String noSqlKey = nosqlItem.Item3;

		TupleThree<String, String, String>  vodItem = GetVodConnectionItems(DefaultVodConnectionString,true);
		final String vodEndpoint = vodItem.Item1;
		final String vodName = vodItem.Item2;
		final String vodKey = vodItem.Item3;

		TupleThree<String, String, String>  codeItem = GetCodeConnectionItems(DefaultCodeConnectionString,true);
		final String codeEndpoint = codeItem.Item1;
		final String codeName = codeItem.Item2;
		final String codeKey = codeItem.Item3;

		TupleThree<String, String, String> sblItem = GetServiceBusConnectionItems(DefaultServiceBusConnectionString, true);
		final String serviceBusEndpoint = sblItem.Item1;
		final String serviceBusName = sblItem.Item2;
		final String serviceBusKey = sblItem.Item3;

		String[] hosts = {
				//本地开发环境：Debug
                "http://localhost:1003",//com.web.admin
                "https://localhost:1013",//com.web.admin
                "http://localhost:1004",//com.webapi.admin
                "https://localhost:1014",//com.webapi.admin

                "http://localhost:1005",//com.web.blog
                "https://localhost:1015",//com.web.blog
                "http://localhost:1006",//com.webapi.blog
                "https://localhost:1016",//com.webapi.blog

                "http://localhost:9999",//com.web.resource
                "https://localhost:9019",//com.web.resource

                //本地测试、测试、灰度、正式环境
                "http://localadmin.kcloudy.com",
                "http://devadmin.kcloudy.com",
                "http://testadmin.kcloudy.com",
                "http://betaadmin.kcloudy.com",
                "http://admin.kcloudy.com",

                "http://localblog.kcloudy.com",
                "http://devblog.kcloudy.com",
                "http://testblog.kcloudy.com",
                "http://betablog.kcloudy.com",
                "http://blog.kcloudy.com",
                };
		String[] arrHosts = ListExtensions.mergeArray(hosts, GetTenantHosts(DbaTenantName));

		DbaTenantSignature = new ServiceRequestToken(DbaTenantId, DbaTenantName, DefaultPrivateEncryptKey)
				.GetEncrptSignature();

		DbaTenantApiAccessInfo = new Tenant();
		{
			DbaTenantApiAccessInfo.setTenantId(DbaTenantId);
			DbaTenantApiAccessInfo.setTenantName(DbaTenantName);
			DbaTenantApiAccessInfo.setTenantDisplayName(DbaTenantDisplayName);
			DbaTenantApiAccessInfo.setTenantSignature(DbaTenantSignature);
			DbaTenantApiAccessInfo.setPrivateEncryptKey(DefaultPrivateEncryptKey);
			DbaTenantApiAccessInfo.setDatabaseType(DefaultDatabaseType);
			DbaTenantApiAccessInfo.setServer(server);
			DbaTenantApiAccessInfo.setDatabase(database);
			DbaTenantApiAccessInfo.setDatabasePasswordHash(password);
			DbaTenantApiAccessInfo.setStorageType(DefaultStorageType);
			DbaTenantApiAccessInfo.setStorageEndpoint(endpoint);
			DbaTenantApiAccessInfo.setStorageAccessName(accessName);
			DbaTenantApiAccessInfo.setStorageAccessKeyPasswordHash(accessKey);
			DbaTenantApiAccessInfo.setQueueType(DefaultQueueType);
			DbaTenantApiAccessInfo.setQueueEndpoint(queueEndpoint);
			DbaTenantApiAccessInfo.setQueueAccessName(queueName);
			DbaTenantApiAccessInfo.setQueueAccessKeyPasswordHash(queueKey);
			DbaTenantApiAccessInfo.setNoSqlType(DefaultNoSqlType);
			DbaTenantApiAccessInfo.setNoSqlEndpoint(noSqlEndpoint);
			DbaTenantApiAccessInfo.setNoSqlAccessName(noSqlName);
			DbaTenantApiAccessInfo.setNoSqlAccessKeyPasswordHash(noSqlKey);
			DbaTenantApiAccessInfo.setVodType(DefaultVodType);
			DbaTenantApiAccessInfo.setVodEndpoint(vodEndpoint);
			DbaTenantApiAccessInfo.setVodAccessName(vodName);
			DbaTenantApiAccessInfo.setVodAccessKeyPasswordHash(vodKey);
			DbaTenantApiAccessInfo.setCodeType(DefaultCodeType);
			DbaTenantApiAccessInfo.setCodeEndpoint(codeEndpoint);
			DbaTenantApiAccessInfo.setCodeAccessName(codeName);
			DbaTenantApiAccessInfo.setCodeAccessKeyPasswordHash(codeKey);
			DbaTenantApiAccessInfo.setServiceBusType(DefaultServiceBusType);
			DbaTenantApiAccessInfo.setServiceBusEndpoint(serviceBusEndpoint);
			DbaTenantApiAccessInfo.setServiceBusAccessName(serviceBusName);
			DbaTenantApiAccessInfo.setServiceBusAccessKeyPasswordHash(serviceBusKey);
			DbaTenantApiAccessInfo.setContactName("田长军");
			DbaTenantApiAccessInfo.setContactEmail("tianchangjun@outlook.com");
			DbaTenantApiAccessInfo.setContactPhone("13682381319");
			DbaTenantApiAccessInfo.setCloudType(DefaultCloudType);
			DbaTenantApiAccessInfo.setTenantType(DefaultTenantType);
			DbaTenantApiAccessInfo.setVersion(DefaultVersion);
			DbaTenantApiAccessInfo.setHostnames(arrHosts);
		}

		TestTenantSignature = new ServiceRequestToken(TestTenantId, TestTenantName, DefaultPrivateEncryptKey)
				.GetEncrptSignature();
		TestTenantApiAccessInfo = new Tenant();
		{
			TestTenantApiAccessInfo.setTenantId(TestTenantId);
			TestTenantApiAccessInfo.setTenantName(TestTenantName);
			TestTenantApiAccessInfo.setTenantDisplayName(TestTenantDisplayName);
			TestTenantApiAccessInfo.setTenantSignature(TestTenantSignature);
			TestTenantApiAccessInfo.setPrivateEncryptKey(DefaultPrivateEncryptKey);
			TestTenantApiAccessInfo.setDatabaseType(DefaultDatabaseType);
			TestTenantApiAccessInfo.setServer(server);
			TestTenantApiAccessInfo.setDatabase(database);
			TestTenantApiAccessInfo.setDatabasePasswordHash(password);
			TestTenantApiAccessInfo.setStorageType(DefaultStorageType);
			TestTenantApiAccessInfo.setStorageEndpoint(endpoint);
			TestTenantApiAccessInfo.setStorageAccessName(accessName);
			TestTenantApiAccessInfo.setStorageAccessKeyPasswordHash(accessKey);
			TestTenantApiAccessInfo.setQueueType(DefaultQueueType);
			TestTenantApiAccessInfo.setQueueEndpoint(queueEndpoint);
			TestTenantApiAccessInfo.setQueueAccessName(queueName);
			TestTenantApiAccessInfo.setQueueAccessKeyPasswordHash(queueKey);
			TestTenantApiAccessInfo.setNoSqlType(DefaultNoSqlType);
			TestTenantApiAccessInfo.setNoSqlEndpoint(noSqlEndpoint);
			TestTenantApiAccessInfo.setNoSqlAccessName(noSqlName);
			TestTenantApiAccessInfo.setNoSqlAccessKeyPasswordHash(noSqlKey);
			TestTenantApiAccessInfo.setVodType(DefaultVodType);
			TestTenantApiAccessInfo.setVodEndpoint(vodEndpoint);
			TestTenantApiAccessInfo.setVodAccessName(vodName);
			TestTenantApiAccessInfo.setVodAccessKeyPasswordHash(vodKey);
			TestTenantApiAccessInfo.setCodeType(DefaultCodeType);
			TestTenantApiAccessInfo.setCodeEndpoint(codeEndpoint);
			TestTenantApiAccessInfo.setCodeAccessName(codeName);
			TestTenantApiAccessInfo.setCodeAccessKeyPasswordHash(codeKey);
			TestTenantApiAccessInfo.setServiceBusType(DefaultServiceBusType);
			TestTenantApiAccessInfo.setServiceBusEndpoint(serviceBusEndpoint);
			TestTenantApiAccessInfo.setServiceBusAccessName(serviceBusName);
			TestTenantApiAccessInfo.setServiceBusAccessKeyPasswordHash(serviceBusKey);
			TestTenantApiAccessInfo.setContactName("田长军");
			TestTenantApiAccessInfo.setContactEmail("tianchangjun@outlook.com");
			TestTenantApiAccessInfo.setContactPhone("13682381319");
			TestTenantApiAccessInfo.setCloudType(DefaultCloudType);
			TestTenantApiAccessInfo.setTenantType(DefaultTenantType);
			TestTenantApiAccessInfo.setVersion(DefaultVersion);
			TestTenantApiAccessInfo.setHostnames(GetTenantHosts(TestTenantName));
		}

		BuyTenantSignature = new ServiceRequestToken(BuyTenantId, BuyTenantName, DefaultPrivateEncryptKey)
				.GetEncrptSignature();
		BuyTenantApiAccessInfo = new Tenant();
		{
			BuyTenantApiAccessInfo.setTenantId(BuyTenantId);
			BuyTenantApiAccessInfo.setTenantName(BuyTenantName);
			BuyTenantApiAccessInfo.setTenantDisplayName(BuyTenantDisplayName);
			BuyTenantApiAccessInfo.setTenantSignature(BuyTenantSignature);
			BuyTenantApiAccessInfo.setPrivateEncryptKey(DefaultPrivateEncryptKey);
			BuyTenantApiAccessInfo.setDatabaseType(DefaultDatabaseType);
			BuyTenantApiAccessInfo.setServer(server);
			BuyTenantApiAccessInfo.setDatabase(database);
			BuyTenantApiAccessInfo.setDatabasePasswordHash(password);
			BuyTenantApiAccessInfo.setStorageType(DefaultStorageType);
			BuyTenantApiAccessInfo.setStorageEndpoint(endpoint);
			BuyTenantApiAccessInfo.setStorageAccessName(accessName);
			BuyTenantApiAccessInfo.setStorageAccessKeyPasswordHash(accessKey);
			BuyTenantApiAccessInfo.setQueueType(DefaultQueueType);
			BuyTenantApiAccessInfo.setQueueEndpoint(queueEndpoint);
			BuyTenantApiAccessInfo.setQueueAccessName(queueName);
			BuyTenantApiAccessInfo.setQueueAccessKeyPasswordHash(queueKey);
			BuyTenantApiAccessInfo.setNoSqlType(DefaultNoSqlType);
			BuyTenantApiAccessInfo.setNoSqlEndpoint(noSqlEndpoint);
			BuyTenantApiAccessInfo.setNoSqlAccessName(noSqlName);
			BuyTenantApiAccessInfo.setNoSqlAccessKeyPasswordHash(noSqlKey);
			BuyTenantApiAccessInfo.setVodType(DefaultVodType);
			BuyTenantApiAccessInfo.setVodEndpoint(vodEndpoint);
			BuyTenantApiAccessInfo.setVodAccessName(vodName);
			BuyTenantApiAccessInfo.setVodAccessKeyPasswordHash(vodKey);
			BuyTenantApiAccessInfo.setCodeType(DefaultCodeType);
			BuyTenantApiAccessInfo.setCodeEndpoint(codeEndpoint);
			BuyTenantApiAccessInfo.setCodeAccessName(codeName);
			BuyTenantApiAccessInfo.setCodeAccessKeyPasswordHash(codeKey);
			BuyTenantApiAccessInfo.setServiceBusType(DefaultServiceBusType);
			BuyTenantApiAccessInfo.setServiceBusEndpoint(serviceBusEndpoint);
			BuyTenantApiAccessInfo.setServiceBusAccessName(serviceBusName);
			BuyTenantApiAccessInfo.setServiceBusAccessKeyPasswordHash(serviceBusKey);
			BuyTenantApiAccessInfo.setContactName("田长军");
			BuyTenantApiAccessInfo.setContactEmail("tianchangjun@outlook.com");
			BuyTenantApiAccessInfo.setContactPhone("13682381319");
			BuyTenantApiAccessInfo.setCloudType(DefaultCloudType);
			BuyTenantApiAccessInfo.setTenantType(DefaultTenantType);
			BuyTenantApiAccessInfo.setVersion(DefaultVersion);
			BuyTenantApiAccessInfo.setHostnames(GetTenantHosts(BuyTenantName));
		}

		SaleTenantSignature = new ServiceRequestToken(SaleTenantId, SaleTenantName, DefaultPrivateEncryptKey)
				.GetEncrptSignature();
		SaleTenantApiAccessInfo = new Tenant();
		{
			SaleTenantApiAccessInfo.setTenantId(SaleTenantId);
			SaleTenantApiAccessInfo.setTenantName(SaleTenantName);
			SaleTenantApiAccessInfo.setTenantDisplayName(SaleTenantDisplayName);
			SaleTenantApiAccessInfo.setTenantSignature(SaleTenantSignature);
			SaleTenantApiAccessInfo.setPrivateEncryptKey(DefaultPrivateEncryptKey);
			SaleTenantApiAccessInfo.setDatabaseType(DefaultDatabaseType);
			SaleTenantApiAccessInfo.setServer(server);
			SaleTenantApiAccessInfo.setDatabase(database);
			SaleTenantApiAccessInfo.setDatabasePasswordHash(password);
			SaleTenantApiAccessInfo.setStorageType(DefaultStorageType);
			SaleTenantApiAccessInfo.setStorageEndpoint(endpoint);
			SaleTenantApiAccessInfo.setStorageAccessName(accessName);
			SaleTenantApiAccessInfo.setStorageAccessKeyPasswordHash(accessKey);
			SaleTenantApiAccessInfo.setQueueType(DefaultQueueType);
			SaleTenantApiAccessInfo.setQueueEndpoint(queueEndpoint);
			SaleTenantApiAccessInfo.setQueueAccessName(queueName);
			SaleTenantApiAccessInfo.setQueueAccessKeyPasswordHash(queueKey);
			SaleTenantApiAccessInfo.setNoSqlType(DefaultNoSqlType);
			SaleTenantApiAccessInfo.setNoSqlEndpoint(noSqlEndpoint);
			SaleTenantApiAccessInfo.setNoSqlAccessName(noSqlName);
			SaleTenantApiAccessInfo.setNoSqlAccessKeyPasswordHash(noSqlKey);
			SaleTenantApiAccessInfo.setVodType(DefaultVodType);
			SaleTenantApiAccessInfo.setVodEndpoint(vodEndpoint);
			SaleTenantApiAccessInfo.setVodAccessName(vodName);
			SaleTenantApiAccessInfo.setVodAccessKeyPasswordHash(vodKey);
			SaleTenantApiAccessInfo.setCodeType(DefaultCodeType);
			SaleTenantApiAccessInfo.setCodeEndpoint(codeEndpoint);
			SaleTenantApiAccessInfo.setCodeAccessName(codeName);
			SaleTenantApiAccessInfo.setCodeAccessKeyPasswordHash(codeKey);
			SaleTenantApiAccessInfo.setServiceBusType(DefaultServiceBusType);
			SaleTenantApiAccessInfo.setServiceBusEndpoint(serviceBusEndpoint);
			SaleTenantApiAccessInfo.setServiceBusAccessName(serviceBusName);
			SaleTenantApiAccessInfo.setServiceBusAccessKeyPasswordHash(serviceBusKey);
			SaleTenantApiAccessInfo.setContactName("田长军");
			SaleTenantApiAccessInfo.setContactEmail("tianchangjun@outlook.com");
			SaleTenantApiAccessInfo.setContactPhone("13682381319");
			SaleTenantApiAccessInfo.setCloudType(DefaultCloudType);
			SaleTenantApiAccessInfo.setTenantType(DefaultTenantType);
			SaleTenantApiAccessInfo.setVersion(DefaultVersion);
			SaleTenantApiAccessInfo.setHostnames(GetTenantHosts(SaleTenantName));
		}

	}
}