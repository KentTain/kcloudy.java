package kc.framework.tenant;

import kc.framework.GlobalConfig;
import kc.framework.base.ApplicationInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ApplicationConstant {

    /**
     * 空UUID：00000000-0000-0000-0000-000000000000
     */
    public static final UUID UUID_NIL = java.util.UUID.fromString("00000000-0000-0000-0000-000000000000");

    public static final String SsoAppName = "统一认证系统";
    public static final String SsoCode = "sso";
    public static final String SsoScope = "ssoapi";
    public static final String SsoAppSId = "e98d7c1c-f6d8-4370-822a-c0a4377e0a59";
    public static final UUID SsoAppId = java.util.UUID.fromString(SsoAppSId);

    public static final String AdminAppName = "租户管理";
    public static final String AdminCode = "admin";
    public static final String AdminScope = "adminapi";
    public static final String AdminAppSId = "98E6825F-7702-4A83-B194-A25442A25D7A";
    public static final UUID AdminAppId = java.util.UUID.fromString(AdminAppSId);

    public static final String BlogAppName = "博客管理";
    public static final String BlogCode = "blog";
    public static final String BlogScope = "blogapi";
    public static final String BlogAppSId = "D0E0D0F1-9A7E-4869-8295-DA9BBC072BAB";
    public static final UUID BlogAppId = java.util.UUID.fromString(BlogAppSId);

    public static final String CodeAppName = "代码生成管理";
    public static final String CodeCode = "code";
    public static final String CodeScope = "codeapi";
    public static final String CodeAppSId = "8473A5DD-6CED-4D18-81E0-A64988B72A05";
    public static final UUID CodeAppId = java.util.UUID.fromString(CodeAppSId);

    public static final String AppAppName = "应用管理";
    public static final String AppCode = "app";
    public static final String AppScope = "appapi";
    public static final String AppAppSId = "1F301943-268F-4940-8BFB-900E0E6E0D35";
    public static final UUID AppAppId = java.util.UUID.fromString(AppAppSId);

    public static final String ConfigAppName = "配置管理";
    public static final String CfgCode = "cfgapi";
    public static final String CfgScope = "cfgapi";
    public static final String ConfigAppSId = "9158A492-C6AE-4C50-87A4-7ADB8BB8D36D";
    public static final UUID ConfigAppId = java.util.UUID.fromString(ConfigAppSId);

    public static final String DictAppName = "字典管理";
    public static final String DicCode = "dic";
    public static final String DicScope = "dicapi";
    public static final String DictAppSId = "21E87C50-B014-40BD-ADD7-01C64513FD3A";
    public static final UUID DictAppId = java.util.UUID.fromString(DictAppSId);

    public static final String MsgAppName = "消息管理";
    public static final String MsgCode = "msg";
    public static final String MsgScope = "msgapi";
    public static final String MsgAppSId = "8600D55B-5F73-41F8-9D06-0F65710D3D80";
    public static final UUID MsgAppId = java.util.UUID.fromString(MsgAppSId);

    public static final String AccAppName = "账户管理";
    public static final String AccCode = "acc";
    public static final String AccScope = "accapi";
    public static final String AccAppSId = "45672506-DDB7-4D57-AD44-BD0AB136B556";
    public static final UUID AccAppId = java.util.UUID.fromString(AccAppSId);

    public static final String EconAppName = "合同管理";
    public static final String EconCode = "econ";
    public static final String EconScope = "econapi";
    public static final String EconAppSId = "18CC3E21-1B74-4BCD-BC5F-FF5E1E4E6C57";
    public static final UUID EconAppId = java.util.UUID.fromString(EconAppSId);

    public static final String DocAppName = "文档管理";
    public static final String DocCode = "doc";
    public static final String DocScope = "docapi";
    public static final String DocAppSId = "7B3A90EC-8311-4113-B010-88E046D4D036";
    public static final UUID DocAppId = java.util.UUID.fromString(DocAppSId);

    public static final String PrjAppName = "项目管理";
    public static final String PrjCode = "prj";
    public static final String PrjScope = "prjapi";
    public static final String PrjAppSId = "6DEFB2F1-AF54-4E3B-B38F-B80DF10AEBD0";
    public static final UUID PrjAppId = java.util.UUID.fromString(PrjAppSId);

    public static final String MbrAppName = "会员管理";
    public static final String MbrCode = "mbr";
    public static final String MbrScope = "mbrapi";
    public static final String MbrAppSId = "0868A6DC-B7B6-4EA9-A40E-CB3617C92512";
    public static final UUID MbrAppId = java.util.UUID.fromString(MbrAppSId);

    public static final String HrAppName = "人事管理";
    public static final String HrCode = "hr";
    public static final String HrScope = "hrapi";
    public static final String HrAppSId = "D96AB61D-7556-4552-98D2-B3507FBF0E55";
    public static final UUID HrAppId = java.util.UUID.fromString(HrAppSId);

    public static final String CrmAppName = "客户管理";
    public static final String CrmCode = "crm";
    public static final String CrmScope = "crmapi";
    public static final String CrmAppSId = "95e9e18f-0316-4c04-bedc-a8e321431c0a";
    public static final UUID CrmAppId = java.util.UUID.fromString(CrmAppSId);

    public static final String SrmAppName = "供应商管理";
    public static final String SrmCode = "srm";
    public static final String SrmScope = "srmapi";
    public static final String SrmAppSId = "379DD8C7-B603-4A45-BD23-72BAABEE374A";
    public static final UUID SrmAppId = java.util.UUID.fromString(SrmAppSId);

    public static final String PrdAppName = "商品管理";
    public static final String PrdCode = "prd";
    public static final String PrdScope = "prdapi";
    public static final String PrdAppSId = "211D74B3-A43F-4167-8BAD-924AF898D570";
    public static final UUID PrdAppId = java.util.UUID.fromString(PrdAppSId);

    public static final String PmcAppName = "物料管理";
    public static final String PmcCode = "pmc";
    public static final String PmcScope = "pmcapi";
    public static final String PmcAppSId = "82D4AEBE-45E8-46DA-83CA-DA3DF071D96E";
    public static final UUID PmcAppId = java.util.UUID.fromString(PmcAppSId);

    public static final String PortalAppName = "门户网站";
    public static final String PortalCode = "portal";
    public static final String PortalScope = "portalapi";
    public static final String PortalAppSId = "AD401D87-0F1C-46DE-AE3E-BE4EC1C57D2C";
    public static final UUID PortalAppId = java.util.UUID.fromString(PortalAppSId);

    public static final String SomAppName = "销售管理";
    public static final String SomCode = "som";
    public static final String SomScope = "somapi";
    public static final String SomAppSId = "A328591D-A64D-4379-B70E-3BDD152848D1";
    public static final UUID SomAppId = java.util.UUID.fromString(SomAppSId);

    public static final String PomAppName = "采购管理";
    public static final String PomCode = "pom";
    public static final String PomScope = "pomapi";
    public static final String PomAppSId = "8A6CD786-582E-44BD-8F5A-E45266E0ABF7";
    public static final UUID PomAppId = java.util.UUID.fromString(PomAppSId);

    public static final String WmsAppName = "仓储管理";
    public static final String WmsCode = "wms";
    public static final String WmsScope = "wmsapi";
    public static final String WmsAppSId = "418841DB-3A2A-4C75-9F7A-C0A14F6EE756";
    public static final UUID WmsAppId = java.util.UUID.fromString(WmsAppSId);

    public static final String JrAppName = "融资管理";
    public static final String JrCode = "jr";
    public static final String JrScope = "jrapi";
    public static final String JrAppSId = "5A79FD6A-7DD9-45E6-B35B-AB24D32E89BB";
    public static final UUID JrAppId = java.util.UUID.fromString(JrAppSId);

    public static final String TrainAppName = "培训管理";
    public static final String TrainCode = "train";
    public static final String TrainScope = "trainapi";
    public static final String TrainAppSId = "6F0B2682-CCE6-4D7A-AF12-A609DE44EBA6";
    public static final UUID TrainAppId = java.util.UUID.fromString(TrainAppSId);

    public static final String ExamAppName = "考试管理";
    public static final String ExamCode = "exam";
    public static final String ExamScope = "examapi";
    public static final String ExamAppSId = "E9E3A02D-B28C-4B53-A26B-69DD807C89C8";
    public static final UUID ExamAppId = java.util.UUID.fromString(ExamAppSId);

    public static final String WorkflowAppName = "工作流管理";
    public static final String WorkflowCode = "flow";
    public static final String WorkflowScope = "flowapi";
    public static final String WorkflowAppSId = "D6036537-1BEB-4818-AF7B-3AF493E0C930";
    public static final UUID WorkflowAppId = java.util.UUID.fromString(WorkflowAppSId);

    public static final String PayAppName = "支付管理";
    public static final String PayCode = "pay";
    public static final String PayScope = "payapi";
    public static final String PayAppSId = "9376560F-FFDE-416F-B9E8-966B7A79012A";
    public static final UUID PayAppId = java.util.UUID.fromString(PayAppSId);

    public static final String WXAppName = "微信管理";
    public static final String WXCode = "wx";
    public static final String WXScope = "wxapi";
    public static final String WXAppSId = "26755248-3986-4C7C-9980-E353BAA85AC4";
    public static final UUID WXAppId = java.util.UUID.fromString(WXAppSId);

    public static final String ResapiAppName = "资源服务";
    public static final String ResCode = "res";
    public static final String ResScope = "resapi";
    public static final String ResapiAppSId = "3C2ADF22-0979-4392-873F-33DBEA51234D";
    public static final UUID ResapiAppId = java.util.UUID.fromString(ResapiAppSId);

    public static final String WebapiAppName = "接口管理";
    public static final String WebapiCode = "api";
    public static final String WebapiScope = "webapi";
    public static final String WebapiAppSId = "740b4c83-aa39-410f-a35d-6d427ac33311";
    public static final UUID WebapiAppId = java.util.UUID.fromString(WebapiAppSId);

    public static final String OpenIdScope = "openid";
    public static final String ProfileScope = "profile";

    public static final List<ApplicationInfo> GetAllApplications() {
        List<ApplicationInfo> result = new ArrayList<>();
        {
            result.add(new ApplicationInfo(SsoAppSId, SsoCode, SsoAppName, GlobalConfig.SSOWebDomain, SsoScope, 0));
            result.add(new ApplicationInfo(AdminAppSId, AdminCode, AdminAppName, GlobalConfig.AdminWebDomain, AdminScope, 28));
            result.add(new ApplicationInfo(BlogAppSId, BlogCode, BlogAppName, GlobalConfig.BlogWebDomain, BlogScope, 13));
            result.add(new ApplicationInfo(CodeAppSId, CodeCode, CodeAppName, GlobalConfig.CodeWebDomain, CodeScope, 27));

            result.add(new ApplicationInfo(AppAppSId, AppCode, AppAppName, GlobalConfig.AppWebDomain, AppScope, 26));
            result.add(new ApplicationInfo(ConfigAppSId, CfgCode, ConfigAppName, GlobalConfig.CfgWebDomain, CfgScope, 24));
            result.add(new ApplicationInfo(DictAppSId, DicCode, DictAppName, GlobalConfig.DicWebDomain, DicScope, 25));
            result.add(new ApplicationInfo(MsgAppSId, MsgCode, MsgAppName, GlobalConfig.MsgWebDomain, MsgScope, 23));

            result.add(new ApplicationInfo(AccAppSId, AccCode, AccAppName, GlobalConfig.AccWebDomain, AccScope, 28));
            result.add(new ApplicationInfo(EconAppSId, EconCode, EconAppName, GlobalConfig.EconWebDomain, EconScope, 18));
            result.add(new ApplicationInfo(DocAppSId, DocCode, DocAppName, GlobalConfig.DocWebDomain, DocScope, 20));
            result.add(new ApplicationInfo(HrAppSId, HrCode, HrAppName, GlobalConfig.HrWebDomain, HrScope, 11));

            result.add(new ApplicationInfo(CrmAppSId, CrmCode, CrmAppName, GlobalConfig.CrmWebDomain, CrmScope, 2));
            result.add(new ApplicationInfo(SrmAppSId, SrmCode, SrmAppName, GlobalConfig.SrmWebDomain, SrmScope, 3));
            result.add(new ApplicationInfo(PrdAppSId, PrdCode, PrdAppName, GlobalConfig.PrdWebDomain, PrdScope, 4));
            result.add(new ApplicationInfo(PmcAppSId, PmcCode, PmcAppName, GlobalConfig.PmcWebDomain, PmcScope, 5));

            result.add(new ApplicationInfo(PortalAppSId, PortalCode, PortalAppName, GlobalConfig.PortalWebDomain, PortalScope, 1));
            result.add(new ApplicationInfo(SomAppSId, SomCode, SomAppName, GlobalConfig.SomWebDomain, SomScope, 6));
            result.add(new ApplicationInfo(PomAppSId, PomCode, PomAppName, GlobalConfig.PomWebDomain, PomScope, 7));
            result.add(new ApplicationInfo(WmsAppSId, WmsCode, WmsAppName, GlobalConfig.WmsWebDomain, WmsScope, 9));

            result.add(new ApplicationInfo(JrAppSId, JrCode, JrAppName, GlobalConfig.JRWebDomain, JrScope, 10));
            result.add(new ApplicationInfo(PrjAppSId, PrjCode, PrjAppName, GlobalConfig.PrjWebDomain, PrjScope, 14));
            result.add(new ApplicationInfo(MbrAppSId, MbrCode, MbrAppName, GlobalConfig.MbrWebDomain, MbrScope, 15));

            result.add(new ApplicationInfo(TrainAppSId, TrainCode, TrainAppName, GlobalConfig.TrainWebDomain, TrainScope, 12));
            result.add(new ApplicationInfo(ExamAppSId, ExamCode, ExamAppName, GlobalConfig.ExamWebDomain, ExamScope, 16));

            result.add(new ApplicationInfo(WorkflowAppSId, WorkflowCode, WorkflowAppName, GlobalConfig.WorkflowWebDomain, WorkflowScope, 21));
            result.add(new ApplicationInfo(PayAppSId, PayCode, PayAppName, GlobalConfig.PayWebDomain, PayScope, 22));

            result.add(new ApplicationInfo(WXAppSId, WXCode, WXAppName, GlobalConfig.WXWebDomain, WXScope, 19));
            result.add(new ApplicationInfo(ResapiAppSId, ResCode, ResapiAppName, GlobalConfig.ResWebDomain, ResScope, 29));
            result.add(new ApplicationInfo(WebapiAppSId, WebapiCode, WebapiAppName, GlobalConfig.ApiWebDomain, WebapiScope, 30));
        }

        return result;
    }

    public static final String ClientAuthorityId = "5DE763F5-3E85-4A2A-8203-31D11FE9599D";
    public static final String DefaultAuthorityId = "126AC4CF-84CF-410B-8989-A4EB8397EC3F";

    public static final char DefaultAuthoritySplitChar = ':';

    /// <summary>
    /// 租户存储限制：1000
    /// </summary>
    public static final int StorageLimit = 1000;

    /// <summary>
    /// 数据库实例限制：20
    /// </summary>
    public static final int DatabaseLimit = 20;

}
