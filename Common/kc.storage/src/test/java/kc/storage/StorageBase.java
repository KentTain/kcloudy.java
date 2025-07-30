package kc.storage;

import kc.framework.enums.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import kc.framework.enums.ServiceBusType;
import kc.framework.tenant.TenantConstant;

public class StorageBase extends kc.framework.TestBase{

	@BeforeAll
	public static void setUpBeforeClass() {
		intilize();
		// Init Dba's Tenant     SqlServer、FTP、MSMQ、AzureTable、Kafka
        //DbaTenant = TenantConstant.DbaTenantApiAccessInfo;
        {
        	DbaTenant.setTenantId(TenantConstant.DbaTenantId);
        	DbaTenant.setCloudType(CloudType.FileSystem);
        	DbaTenant.setVersion(TenantVersion.Standard.getIndex());
        	DbaTenant.setTenantType(TenantType.Institution.getIndex());

        	DbaTenant.setTenantName(TenantConstant.DbaTenantName);
        	DbaTenant.setTenantDisplayName(TenantConstant.DbaTenantDisplayName);
        	DbaTenant.setTenantSignature(TenantConstant.DbaTenantSignature);
        	DbaTenant.setPrivateEncryptKey("N4d8775824n");
        	DbaTenant.setContactEmail("tianchangjun@cfwin.com");
            //DataBase
        	DbaTenant.setDatabaseType(DatabaseType.SqlServer);
        	DbaTenant.setServer("localhost");
        	DbaTenant.setDatabase("MSSqlKCContext");
        	DbaTenant.setDatabasePasswordHash("T5TpUcgaTDVBLwN8EQS9UA==");
            //Storage
        	DbaTenant.setStorageType(StorageType.FTP);
        	DbaTenant.setStorageAccessName("ftpuser");
        	DbaTenant.setStorageEndpoint("ftp://192.168.2.141");
        	DbaTenant.setStorageAccessKeyPasswordHash("TlmPz46KPI7zt/TLnjd6fmmNaSY6qNSn1MbKP7gpOr0=");
            //Queue
        	DbaTenant.setQueueType(QueueType.MSMQ);
        	DbaTenant.setQueueAccessName("localMsmqQueue");
        	DbaTenant.setQueueEndpoint(".");
        	DbaTenant.setQueueAccessKeyPasswordHash("oeWnC1Gb/Po=");
            //NoSql
        	DbaTenant.setNoSqlType(NoSqlType.AzureTable);
        	DbaTenant.setNoSqlAccessName("cfwinstorage");
        	DbaTenant.setNoSqlEndpoint("https://cfwinstorage.table.core.chinacloudapi.cn/");
        	DbaTenant.setNoSqlAccessKeyPasswordHash("hEwbGcfmKPgpOz+kq0B7G3bfKBpkwjYgU05eZ96kgyJqmeCBbqvYgwVuf3uhUsxzDhs6dYTS+e+DSfD3nMcDtPsBqlb/Are3zZqE2R2aDlDYS/F+R1XRRUI+TD46Spd2");
            //ServiceBus
        	DbaTenant.setServiceBusType(ServiceBusType.Kafka);
        	DbaTenant.setServiceBusAccessName("localKafkaQueue");
        	DbaTenant.setServiceBusEndpoint("192.168.2.80:9092);192.168.2.80:9093);192.168.2.80:9094");
        	DbaTenant.setServiceBusAccessKeyPasswordHash("Zc0i/S7QPjo=");

        }

        // Init Test's Tenant   SqlServer、File、MSMQ、AzureTable、Kafka
        //TestTenant = TenantConstant.TestTenantApiAccessInfo;
        {
        	TestTenant.setTenantId(TenantConstant.TestTenantId);
        	TestTenant.setCloudType(CloudType.FileSystem);
        	TestTenant.setVersion(TenantVersion.Standard.getIndex());
        	TestTenant.setTenantType(TenantType.Bank.getIndex());//商业保理

        	TestTenant.setTenantName(TenantConstant.TestTenantName);
        	TestTenant.setTenantDisplayName(TenantConstant.TestTenantDisplayName);
        	TestTenant.setTenantSignature(TenantConstant.TestTenantSignature);
            TestTenant.setPrivateEncryptKey("N4d8775824n");
            TestTenant.setContactEmail("tianchangjun@cfwin.com");
            //DataBase
            TestTenant.setDatabaseType(DatabaseType.SqlServer);
            TestTenant.setServer("localhost");
            TestTenant.setDatabase("MSSqlKCContext");
            TestTenant.setDatabasePasswordHash("T5TpUcgaTDVBLwN8EQS9UA==");
            //Storage
            TestTenant.setStorageType(StorageType.File);
            TestTenant.setStorageAccessName("localStorage");
            TestTenant.setStorageEndpoint("D:\\Business\\blobstorage");
            TestTenant.setStorageAccessKeyPasswordHash("TlmPz46KPI7zt/TLnjd6fmmNaSY6qNSn1MbKP7gpOr0=");
            //Queue
            TestTenant.setQueueType(QueueType.MSMQ);
            TestTenant.setQueueAccessName("localMsmqQueue");
            TestTenant.setQueueEndpoint(".");
            TestTenant.setQueueAccessKeyPasswordHash("oeWnC1Gb/Po=");
            //NoSql
            TestTenant.setNoSqlType(NoSqlType.AzureTable);
            TestTenant.setNoSqlAccessName("cfwinstorage");
            TestTenant.setNoSqlEndpoint("https://cfwinstorage.table.core.chinacloudapi.cn/");
            TestTenant.setNoSqlAccessKeyPasswordHash("hEwbGcfmKPgpOz+kq0B7G3bfKBpkwjYgU05eZ96kgyJqmeCBbqvYgwVuf3uhUsxzDhs6dYTS+e+DSfD3nMcDtPsBqlb/Are3zZqE2R2aDlDYS/F+R1XRRUI+TD46Spd2");
            //ServiceBus
            TestTenant.setServiceBusType(ServiceBusType.Kafka);
            TestTenant.setServiceBusAccessName("localKafkaQueue");
            TestTenant.setServiceBusEndpoint("192.168.2.80:9092);192.168.2.80:9093);192.168.2.80:9094");
            TestTenant.setServiceBusAccessKeyPasswordHash("Zc0i/S7QPjo=");

        }

        // Init Buy's Tenant    MySql、Blob、AzureQueue、AzureTable、ServiceBus
        //BuyTenant = TenantConstant.BuyTenantApiAccessInfo;
        {
        	BuyTenant.setTenantId(TenantConstant.BuyTenantId);
        	BuyTenant.setCloudType(CloudType.Azure);
        	BuyTenant.setVersion(TenantVersion.Standard.getIndex());
        	BuyTenant.setTenantType(TenantType.Enterprise.getIndex());//店铺赊销

        	BuyTenant.setTenantName(TenantConstant.BuyTenantName);
        	BuyTenant.setTenantDisplayName(TenantConstant.BuyTenantDisplayName);
        	BuyTenant.setTenantSignature(TenantConstant.BuyTenantSignature);
        	BuyTenant.setPrivateEncryptKey("K7ef0139cbk");
        	BuyTenant.setContactEmail("tianchangjun@cfwin.com");
            //DataBase
        	BuyTenant.setDatabaseType(DatabaseType.MySql);
        	BuyTenant.setServer("localhost");
        	BuyTenant.setDatabase("NetTest");
        	BuyTenant.setDatabasePasswordHash("R+YUBQCJ3CqLVWSYsQEWlg==");
            //Storage
        	BuyTenant.setStorageType(StorageType.Blob);
        	BuyTenant.setStorageAccessName("cfwinstorage");
        	BuyTenant.setStorageEndpoint("https://cfwinstorage.blob.core.chinacloudapi.cn/");
        	BuyTenant.setStorageAccessKeyPasswordHash("msPXQJhZYAXqMVdqtqH0c32ORWBZJ0IKB55WOL2SsPPbPQPiLqliWdUDLVAIHR+F/hxOz+Z7POzv6yVP+/82uzzDwIfe3owjJGhvU179nu9ADw3NNWjZumHY6zpOg7f0");
            //Queue
        	BuyTenant.setQueueType(QueueType.AzureQueue);
        	BuyTenant.setQueueAccessName("cfwinstorage");
        	BuyTenant.setQueueEndpoint("https://cfwinstorage.queue.core.chinacloudapi.cn/");
        	BuyTenant.setQueueAccessKeyPasswordHash("msPXQJhZYAXqMVdqtqH0c32ORWBZJ0IKB55WOL2SsPPbPQPiLqliWdUDLVAIHR+F/hxOz+Z7POzv6yVP+/82uzzDwIfe3owjJGhvU179nu9ADw3NNWjZumHY6zpOg7f0");
            //NoSql
        	BuyTenant.setNoSqlType(NoSqlType.AzureTable);
        	BuyTenant.setNoSqlAccessName("cfwinstorage");
        	BuyTenant.setNoSqlEndpoint("https://cfwinstorage.table.core.chinacloudapi.cn/");
        	BuyTenant.setNoSqlAccessKeyPasswordHash("msPXQJhZYAXqMVdqtqH0c32ORWBZJ0IKB55WOL2SsPPbPQPiLqliWdUDLVAIHR+F/hxOz+Z7POzv6yVP+/82uzzDwIfe3owjJGhvU179nu9ADw3NNWjZumHY6zpOg7f0");
            //ServiceBus
        	BuyTenant.setServiceBusType(ServiceBusType.ServiceBus);
        	BuyTenant.setServiceBusAccessName("RootManageSharedAccessKey");
        	BuyTenant.setServiceBusEndpoint("sb://devcfwin.servicebus.chinacloudapi.cn/");
        	BuyTenant.setServiceBusAccessKeyPasswordHash("rhIVOASJncr/HciOebECPxa7FgcoRo85fUV5inBVKnlAPmlUDKSHY3MAszNCO4py");

        }

        // Init Sale's Tenant   SqlServer、Aliyun OSS、AzureQueue、AzureTable、Redis
        //SaleTenant = TenantConstant.SaleTenantApiAccessInfo;
        {
        	SaleTenant.setTenantId(TenantConstant.SaleTenantId);
        	SaleTenant.setCloudType(CloudType.Ali);
        	SaleTenant.setVersion(TenantVersion.Standard.getIndex());
        	SaleTenant.setTenantType(TenantType.Enterprise.getIndex());//店铺赊销

        	SaleTenant.setTenantName(TenantConstant.SaleTenantName);
        	SaleTenant.setTenantDisplayName(TenantConstant.SaleTenantDisplayName);
        	SaleTenant.setTenantSignature(TenantConstant.SaleTenantSignature);
        	SaleTenant.setPrivateEncryptKey("K7ef0139cbk");
        	SaleTenant.setContactEmail("tianchangjun@cfwin.com");
            //DataBase
        	SaleTenant.setDatabaseType(DatabaseType.SqlServer);
        	SaleTenant.setServer("localhost");
        	SaleTenant.setDatabase("MSSqlKCContext");
        	SaleTenant.setDatabasePasswordHash("R+YUBQCJ3CqLVWSYsQEWlg==");
            //Storage
        	SaleTenant.setStorageType(StorageType.AliOSS);
        	SaleTenant.setStorageEndpoint("http://oss-cn-shenzhen.aliyuncs.com");
			SaleTenant.setStorageAccessName("LTAI4G3LCfpphDx3pLBgbHmY");
			SaleTenant.setStorageAccessKeyPasswordHash("dZUhvLovTUjBfqhF/hsxtwwR869kaCw83DAGIcny1cI=");
            //Queue
        	SaleTenant.setQueueType(QueueType.AzureQueue);
        	SaleTenant.setQueueAccessName("cfwinstorage");
        	SaleTenant.setQueueEndpoint("https://cfwinstorage.queue.core.chinacloudapi.cn/");
        	SaleTenant.setQueueAccessKeyPasswordHash("msPXQJhZYAXqMVdqtqH0c32ORWBZJ0IKB55WOL2SsPPbPQPiLqliWdUDLVAIHR+F/hxOz+Z7POzv6yVP+/82uzzDwIfe3owjJGhvU179nu9ADw3NNWjZumHY6zpOg7f0");
            //NoSql
        	SaleTenant.setNoSqlType(NoSqlType.AzureTable);
        	SaleTenant.setNoSqlAccessName("cfwinstorage");
        	SaleTenant.setNoSqlEndpoint("https://cfwinstorage.table.core.chinacloudapi.cn/");
        	SaleTenant.setNoSqlAccessKeyPasswordHash("msPXQJhZYAXqMVdqtqH0c32ORWBZJ0IKB55WOL2SsPPbPQPiLqliWdUDLVAIHR+F/hxOz+Z7POzv6yVP+/82uzzDwIfe3owjJGhvU179nu9ADw3NNWjZumHY6zpOg7f0");
            //ServiceBus
        	SaleTenant.setServiceBusType(ServiceBusType.ServiceBus);
        	SaleTenant.setServiceBusAccessName("RootManageSharedAccessKey");
        	SaleTenant.setServiceBusEndpoint("sb://devcfwin.servicebus.chinacloudapi.cn/");
        	SaleTenant.setServiceBusAccessKeyPasswordHash("rhIVOASJncr/HciOebECPxa7FgcoRo85fUV5inBVKnlAPmlUDKSHY3MAszNCO4py");
        }
	}
	@AfterAll
	public static void setDownAfterClass() {
	}

}
