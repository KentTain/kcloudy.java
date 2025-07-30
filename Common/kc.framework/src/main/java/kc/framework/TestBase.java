package kc.framework;

import java.util.List;

import kc.framework.base.TreeNode;
import kc.framework.enums.SystemType;
import kc.framework.extension.StringExtensions;
import kc.framework.tenant.Tenant;
import kc.framework.tenant.TenantConstant;
import kc.framework.tenant.TenantContext;

public abstract class TestBase {

    /**
     * Local Storage（FTP）、Institution（供应链金融）
     */
    public static Tenant DbaTenant;
    /**
     * Local Storage（Local Disk）、Bank（商业保理）
     */
    public static Tenant TestTenant;
    /**
     * Azure Storage、Enterprise（店铺赊销）
     */
    public static Tenant BuyTenant;
    /**
     * Azure Storage、Enterprise（店铺赊销）
     */
    public static Tenant SaleTenant;

    private static boolean sqlServerConnectStringIsEmpty = false;
    private static boolean mySqlConnectStringIsEmpty = false;

    /**
     * 初始化测试租户：DbaTenant、TestTenant、BuyTenant、 SaleTenant <br/>
     * 并设置TenantContext中的初始租户未：TestTenant
     */
    public static void intilize() {
        GlobalConfig.SystemType = SystemType.Dev;
        GlobalConfig.EncryptKey = "dev-cfwin-EncryptKey";

        if (StringExtensions.isNullOrEmpty(GlobalConfig.DatabaseConnectionString)) {
            sqlServerConnectStringIsEmpty = true;
            GlobalConfig.DatabaseConnectionString = "Server=localhost;Database=MSSqlKCContext;User ID=sa;Password=0QVw0yFoX2GuwkMSQyz1tg==;MultipleActiveResultSets=true;";
        }
        if (StringExtensions.isNullOrEmpty(GlobalConfig.MySqlConnectionString)) {
            mySqlConnectStringIsEmpty = true;
            GlobalConfig.MySqlConnectionString = "Server=localhost;Database=MySqlKCContext;User ID=root;Password=0QVw0yFoX2GuwkMSQyz1tg==;MultipleActiveResultSets=true;";
        }

        GlobalConfig.UploadConfig = new kc.framework.UploadConfig();
        GlobalConfig.UploadConfig.setFileExt("txt,doc,docx,xls,xlsx,ppt,pptx,pdf");
        GlobalConfig.UploadConfig.setFileMaxSize(10);
        GlobalConfig.UploadConfig.setImageExt("jpg,jpeg,png,gif,bmp");
        GlobalConfig.UploadConfig.setImageMaxSize(10);


        initTestTenant();
    }

    /**
     * 初始化测试租户：DbaTenant、TestTenant、BuyTenant、 SaleTenant <br/>
     * 并设置TenantContext中的初始租户未：TestTenant
     */
    public static void intilize(GlobalConfigData configData) {
        if (null == configData) {
            intilize();
            return;
        }

        GlobalConfig.InitGlobalConfigWithApiData(null, configData);

        initTestTenant();
    }

    private static void initTestTenant(){
        TenantConstant.InitTestTenant();

        DbaTenant = TenantConstant.DbaTenantApiAccessInfo;
        TestTenant = TenantConstant.TestTenantApiAccessInfo;
        BuyTenant = TenantConstant.BuyTenantApiAccessInfo;
        SaleTenant = TenantConstant.SaleTenantApiAccessInfo;

        TenantContext.setCurrentTenant(TestTenant);
    }

    /**
     * 设置TenantContext中的租户，用于不同租户间的测试
     *
     * @param tenant 需要设置的租户
     */
    protected void InjectTenant(Tenant tenant) {
        TenantContext.setCurrentTenant(tenant);
    }

    /**
     * 将GlobalConfig中的值设置为空
     */
    public static void tearDown() {
        if (sqlServerConnectStringIsEmpty)
            GlobalConfig.DatabaseConnectionString = "";
        if (mySqlConnectStringIsEmpty)
            GlobalConfig.MySqlConnectionString = "";
    }

    /**
     * 打印树结构
     *
     * @param nodes
     */
    public static <T extends TreeNode<T>> void printTreeNodes(List<T> nodes) {
        for (T node : nodes) {
            printTreeNode(node, 1);
        }
    }

    /**
     * 打印树结构
     *
     * @param node
     * @level 打印层级
     */
    public static <T extends TreeNode<T>> void printTreeNode(T node, int level) {
        StringBuilder preStr = new StringBuilder();
        for (int i = 0; i < level; i++) {
            preStr.append("|----");
        }
        System.out.println(preStr.toString() + node.getLevel() + ": " + node.getName());
        for (T children : node.getChildNodes()) {
            printTreeNode(children, level + 1);
        }
    }
}
