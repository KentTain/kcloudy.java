package kc.framework.tenant;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import kc.framework.GlobalConfig;
import kc.framework.enums.ServiceBusType;
import kc.framework.extension.StringExtensions;
import kc.framework.security.EncryptPasswordUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity
@Table(name = "tenant_Tenant")
public class Tenant extends TenantApiAccessInfo implements java.io.Serializable {

	private static final long serialVersionUID = 6241468341179372793L;

	/**
	 * 数据库类型
	 */
	@Column(name = "DatabaseType")
	private kc.framework.enums.DatabaseType databaseType;

	/**
	 * 租户的使用的数据库服务器名称
	 */
	@Column(name = "Server")
	private String server;

	/**
	 * 租户的使用的数据库实例名称
	 */
	@Column(name = "Database")
	private String database;

	/**
	 * 租户的使用的数据库服务器密码
	 */
	@Column(name = "DatabasePasswordHash")
	private String databasePasswordHash;

	/**
	 * 租户使用的数据库连接字符串
	 * 
	 * @return
	 */
	public String ConnectionString() {
		String result = "";
		if (StringExtensions.isNullOrEmpty(server) || StringExtensions.isNullOrEmpty(database))
			return result;

		try {
			switch (databaseType) {
			case MySql:
				String mysqlConn = "jdbc:mysql://%s:3306/%s?user=%s&password=%s&serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true";
				result = String.format(mysqlConn, server, database, getTenantName(),
						EncryptPasswordUtil.DecryptPassword(databasePasswordHash, getPrivateEncryptKey()));
			case SqlServer:
				String javaServer = server.replace(",3433", ":3433").replace(", 3433", ":3433");
				String sqlConn = "jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s";
				result = String.format(sqlConn, javaServer, database, getTenantName(),
						EncryptPasswordUtil.DecryptPassword(databasePasswordHash, getPrivateEncryptKey()));
			default:
				break;
			}

			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			return result;
		}
	}

	public String DatabasePassword() {
		String result = "";
		if (StringExtensions.isNullOrEmpty(databasePasswordHash))
			return result;
		try {
			return EncryptPasswordUtil.DecryptPassword(databasePasswordHash, getPrivateEncryptKey());
		} catch (Exception ex) {
			ex.printStackTrace();
			return result;
		}
	}

	@Column(name = "StorageType")
	private kc.framework.enums.StorageType storageType;

	/**
	 * 租户的使用的存储连接地址
	 */
	@Column(name = "StorageEndpoint")
	private String storageEndpoint;

	/**
	 * 租户的使用的存储连接名称
	 */
	@Column(name = "StorageAccessName")
	private String storageAccessName;

	/**
	 * 租户的使用的存储连接秘钥
	 */
	@Column(name = "StorageAccessKeyPasswordHash")
	private String storageAccessKeyPasswordHash;

	public String GetDecryptStorageConnectionString() {
		if (StringExtensions.isNullOrEmpty(storageEndpoint) || StringExtensions.isNullOrEmpty(storageAccessName)
				|| StringExtensions.isNullOrEmpty(storageAccessKeyPasswordHash))
			return GlobalConfig.StorageConnectionString.toLowerCase();

		String azureConn = "BlobEndpoint=%s;DefaultEndpointsProtocol=https;AccountName=%s;AccountKey=%s";

		try {
			return String.format(azureConn, storageEndpoint, storageAccessName,
					EncryptPasswordUtil.DecryptPassword(storageAccessKeyPasswordHash, getPrivateEncryptKey()));
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	@Column(name = "QueueType")
	private kc.framework.enums.QueueType queueType;

	/**
	 * 租户的使用的队列连接地址
	 */
	@Column(name = "QueueEndpoint")
	private String queueEndpoint;

	/**
	 * 租户的使用的队列连接名称
	 */
	@Column(name = "QueueAccessName")
	private String queueAccessName;

	/**
	 * 租户的使用的队列连接秘钥
	 */
	@Column(name = "QueueAccessKeyPasswordHash")
	private String queueAccessKeyPasswordHash;

	public String GetDecryptQueueConnectionString() {
		if (StringExtensions.isNullOrEmpty(queueEndpoint) || StringExtensions.isNullOrEmpty(queueAccessName)
				|| StringExtensions.isNullOrEmpty(queueAccessKeyPasswordHash))
			return GlobalConfig.QueueConnectionString.toLowerCase();

		String azureConn = "QueueEndpoint=%s;DefaultEndpointsProtocol=https;AccountName=%s;AccountKey=%s";

		try {
			return String.format(azureConn, queueEndpoint, queueAccessName,
					EncryptPasswordUtil.DecryptPassword(queueAccessKeyPasswordHash, getPrivateEncryptKey()));
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	@Column(name = "NoSqlType")
	private kc.framework.enums.NoSqlType noSqlType;

	/**
	 * 租户的使用的非结构化数据库连接地址
	 */
	@Column(name = "NoSqlEndpoint")
	private String noSqlEndpoint;

	/**
	 * 租户的使用的非结构化数据库连接名称
	 */
	@Column(name = "NoSqlAccessName")
	private String noSqlAccessName;

	/**
	 * 租户的使用的非结构化数据库连接秘钥
	 */
	@Column(name = "NoSqlAccessKeyPasswordHash")
	private String noSqlAccessKeyPasswordHash;

	public String GetDecryptNoSqlConnectionString() {
		if (StringExtensions.isNullOrEmpty(noSqlEndpoint) || StringExtensions.isNullOrEmpty(noSqlAccessName)
				|| StringExtensions.isNullOrEmpty(noSqlAccessKeyPasswordHash))
			return GlobalConfig.NoSqlConnectionString.toLowerCase();

		String azureConn = "TableEndpoint=%s;DefaultEndpointsProtocol=https;AccountName=%s;AccountKey=%s";

		try {
			return String.format(azureConn, noSqlEndpoint, noSqlAccessName,
					EncryptPasswordUtil.DecryptPassword(noSqlAccessKeyPasswordHash, getPrivateEncryptKey()));
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	/**
	 * 租户的使用的分布式类型
	 */
	@Column(name = "ServiceBusType")
	private ServiceBusType serviceBusType;

	/**
	 * 租户的使用的分布式连接地址
	 */
	@Column(name = "ServiceBusEndpoint")
	private String serviceBusEndpoint;
	/**
	 * 租户的使用的分布式连接名称
	 */
	@Column(name = "ServiceBusAccessName")
	private String serviceBusAccessName;

	/**
	 * 租户的使用的分布式连接秘钥
	 */
	@Column(name = "ServiceBusAccessKeyPasswordHash")
	private String serviceBusAccessKeyPasswordHash;

	public String GetDecryptServiceBusConnectionString() {
		if (StringExtensions.isNullOrEmpty(serviceBusEndpoint) || StringExtensions.isNullOrEmpty(serviceBusAccessName)
				|| StringExtensions.isNullOrEmpty(serviceBusAccessKeyPasswordHash))
			return GlobalConfig.ServiceBusConnectionString.toLowerCase();

		String azureConn = "Endpoint=%s;SharedAccessKeyName=%s;SharedAccessKey=%s";

		try {
			return String.format(azureConn, serviceBusEndpoint, serviceBusAccessName,
					EncryptPasswordUtil.DecryptPassword(serviceBusAccessKeyPasswordHash, getPrivateEncryptKey()));
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}


	/**
	 * 租户的使用的视频存储类型
	 */
	@Column(name = "VodType")
	private kc.framework.enums.VodType vodType;

	/**
	 * 租户的使用的视频存储连接地址
	 */
	@Column(name = "VodEndpoint")
	private String vodEndpoint;
	/**
	 * 租户的使用的视频存储连接名称
	 */
	@Column(name = "VodAccessName")
	private String vodAccessName;

	/**
	 * 租户的使用的视频存储连接秘钥
	 */
	@Column(name = "VodAccessKeyPasswordHash")
	private String vodAccessKeyPasswordHash;

	public String GetDecryptVodConnectionString() {
		if (StringExtensions.isNullOrEmpty(vodEndpoint) || StringExtensions.isNullOrEmpty(vodAccessName)
				|| StringExtensions.isNullOrEmpty(vodAccessKeyPasswordHash))
			return GlobalConfig.VodConnectionString.toLowerCase();

		String azureConn = "VodEndpoint=%s;AccountName=%s;AccountKey=%s";

		try {
			return String.format(azureConn, vodEndpoint, vodAccessName,
					EncryptPasswordUtil.DecryptPassword(vodAccessKeyPasswordHash, getPrivateEncryptKey()));
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}


	/**
	 * 租户的使用的视频存储类型
	 */
	@Column(name = "CodeType")
	private kc.framework.enums.CodeType codeType;

	/**
	 * 租户的使用的视频存储连接地址
	 */
	@Column(name = "CodeEndpoint")
	private String codeEndpoint;
	/**
	 * 租户的使用的视频存储连接名称
	 */
	@Column(name = "CodeAccessName")
	private String codeAccessName;

	/**
	 * 租户的使用的视频存储连接秘钥
	 */
	@Column(name = "CodeAccessKeyPasswordHash")
	private String codeAccessKeyPasswordHash;

	public String GetDecryptCodeConnectionString() {
		if (StringExtensions.isNullOrEmpty(codeEndpoint) || StringExtensions.isNullOrEmpty(codeAccessName)
				|| StringExtensions.isNullOrEmpty(codeAccessKeyPasswordHash))
			return GlobalConfig.CodeConnectionString.toLowerCase();

		String azureConn = "CodeEndpoint=%s;AccountName=%s;AccountKey=%s";

		try {
			return String.format(azureConn, codeEndpoint, codeAccessName,
					EncryptPasswordUtil.DecryptPassword(codeAccessKeyPasswordHash, getPrivateEncryptKey()));
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 租户的联系人姓名
	 */
	@Column(name = "ContactName")
	private String contactName;

	/**
	 * 租户的联系人邮箱
	 */
	@Column(name = "ContactEmail")
	private String contactEmail;

	/**
	 * 租户的联系人电话
	 */
	@Column(name = "ContactPhone")
	private String contactPhone;

	/**
	 * 租户别名（别名/独立域名）
	 */
	@Column(name = "NickName")
	private String nickName;

	/**
	 * 租户别名最后的修改时间
	 */
	@Column(name = "NickNameLastModifyDate")
	private Date nickNameLastModifyDate;

	/**
	 * 密码过期时间
	 */
	@Column(name = "PasswordExpiredTime")
	private Date passwordExpiredTime;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy ="tenant")
	private Set<TenantSetting> tenantSettings = new HashSet<TenantSetting>();
	
	public String GetOwnDomain()
    {
		Optional<TenantSetting> optSetting = tenantSettings.stream()
        		.filter(t -> t.getTenant().getTenantId() == getTenantId() 
    			&& t.getName().equals(TenantConstant.PropertyName_OwnDomainSetting))
    		.findFirst();
    	if(optSetting.isPresent())
    		return optSetting.get().getValue();
		
        return "";
    }
}
