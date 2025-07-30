package kc.database;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kc.framework.GlobalConfig;
import kc.framework.enums.DatabaseType;
import kc.framework.enums.TenantVersion;
import kc.framework.extension.StringExtensions;
import kc.framework.security.EncryptPasswordUtil;
import kc.framework.tenant.Tenant;
import kc.framework.tenant.TenantConstant;

public class DbLoginUserUtil {
	private final static Logger logger = LoggerFactory.getLogger(DbLoginUserUtil.class.getName());
	private final static String UserPreString = "U_";

	public final static String DefaultTenantRole = "TenantRole";

	/**
	 * 注意，只能用于所有的Tenant都是在该Sa登录用户下的connectString去生成的数据库的
	 * @param tennats
	 * @param connectString master库的sa用户
	 * @return
	 */
	public static boolean CreateTenantDbloginUserList(List<Tenant> tennats, String connectString) {
		boolean isSuccess = true;

		for (Tenant tenant : tennats) {
			boolean result = CreateTenantDbLoginUser(tenant, connectString);
			if (!result)
				isSuccess = false;
		}

		return isSuccess;
	}

	/**创建tenant的数据库登录用户及赋予相应的权限
	 * @param tenant 租户的TenantName、Database、Version、TenantPasswordHash、PrivateEncryptKey
	 * @param connectString SA用户的连接字符串
	 * @return
	 */
	public static boolean CreateTenantDbLoginUser(Tenant tenant, String connectString) {
		Boolean isProfessional = (tenant.getVersion() == TenantVersion.Professional.getIndex());

		String tenantName = tenant.getTenantName();
		String tenantDatabase = tenant.getDatabase();
		String userName = UserPreString + tenantName;
		String tenantPwd = EncryptPasswordUtil.DecryptPassword(tenant.getDatabasePasswordHash(),
				!StringExtensions.isNullOrEmpty(tenant.getPrivateEncryptKey()) ? tenant.getPrivateEncryptKey()
						: TenantConstant.DefaultPrivateEncryptKey);

		if (StringExtensions.isNullOrEmpty(connectString))
			connectString = GlobalConfig.GetDecryptDatabaseConnectionString();

		connectString = TenantConnection.convertConnection(tenant.getDatabaseType(), connectString);
		
		// 创建登录名，在AzureSql下，必须在master下创建
		String masterDbConStr = ChangeDatabaseConnectionString(connectString,
				tenant.getDatabaseType() == DatabaseType.MySql ? "Sys" : "master");
		String tenantDbConStr = ChangeDatabaseConnectionString(connectString, tenantDatabase);
		System.out.println("----masterDbConStr: " + masterDbConStr);
		System.out.println("----tenantDbConStr: "+ tenantDbConStr);
		
		TenantConnection masterTenantConnection = new TenantConnection();
		{
			masterTenantConnection.setTenantName(tenant.getTenantName());
			masterTenantConnection.setDatabaseType(tenant.getDatabaseType());
			masterTenantConnection.setConnectionString(masterDbConStr);
		}

		TenantConnection tenantOwnConnection = new TenantConnection();
		{
			masterTenantConnection.setTenantName(tenant.getTenantName());
			masterTenantConnection.setDatabaseType(tenant.getDatabaseType());
			masterTenantConnection.setConnectionString(tenantDbConStr);
		}
		// logger.debug(String.Format("%s：创建登录账户\r\n master dbcon=[%s] /r/n tenant
		// dbcon=[%s] /r/n tenant conn=[3]", tenantName, masterDbConStr,
		// tenantDbConStr));

		// 直接使用以下语句创建数据库（其他：CREATE USER、CREATE Login）：
		// Sql Azure
		// 安全限制，见：https://azure.microsoft.com/zh-cn/documentation/articles/sql-database-security-guidelines/
		// IF EXISTS (SELECT [name]
		// FROM [sys].[databases]
		// WHERE [name] = N'database_name')
		// CREATE DATABASE [database_name];
		// sql Azure 会报错：The CREATE DATABASE statement must be the only statement in the
		// batch

		// 创建数据库
		StringBuilder sbSql = new StringBuilder();
		try {
			// 创建数据库，在AzureSql下，必须在master下创建
			if (!IsExistDatabase(masterDbConStr, tenantDatabase, tenant.getDatabaseType())) {
				sbSql = new StringBuilder();
				// sbSql.AppendLine(" GRANT VIEW any DATABASE TO PUBLIC ");
				sbSql.append(String.format("   CREATE database %s", tenantDatabase));
				logger.debug(String.format("%s：创建数据库（Database）--Sql：\r\n%s", tenantName, sbSql));
				new DBHelper(masterTenantConnection);
				DBHelper.ExecSql(sbSql.toString());
			}
		} catch (Exception ex) {
			logger.error(String.format("[Tenant: %s]创建数据库出错，错误消息：%s, 脚本：%s", tenantName, ex.getMessage(), sbSql));
			return false;
		}

		// 创建登录账户
		try {
			// 创建登录账户，在AzureSql下，必须在master下创建
			if (!IsExistLoginUser(masterDbConStr, tenantName, tenant.getDatabaseType())) {
				sbSql = new StringBuilder();
				switch (tenant.getDatabaseType()) {
				case MySql:// MySql无数据库登录用户
					break;
				default:
					sbSql.append(String.format("CREATE LOGIN [%s] with password=N'%s'", tenantName, tenantPwd));
					// logger.debug(String.Format("%s：创建登录账户（Login）--Sql：\r\n%s", tenantName,
					// sbSql));
					new DBHelper(masterTenantConnection);
					DBHelper.ExecSql(sbSql.toString());
					break;
				}
			}
			// 租户的数据库登录账户无法登录时，需重新修改登录用户的密码：现有只支持Sql Server
			else if (tenant.getDatabaseType() == DatabaseType.SqlServer
					&& !StringExtensions.isNullOrEmpty(tenant.ConnectionString())
					&& !CanConnectDatabase(tenant.ConnectionString(), tenant.getDatabaseType())) {
				boolean success1 = DeleteTenantDbLoginUser(tenant, connectString);
				if (success1) {
					sbSql = new StringBuilder();
					switch (tenant.getDatabaseType()) {
					case MySql:// MySql无数据库登录用户
						break;
					default:
						sbSql.append(String.format("CREATE LOGIN [%s] with password=N'%s'", tenantName, tenantPwd));
						// logger.debug(String.Format("%s：创建登录账户（Login）--Sql：\r\n%s", tenantName,
						// sbSql));
						new DBHelper(masterTenantConnection);
						DBHelper.ExecSql(sbSql.toString());
						break;
					}
				}
			}
		} catch (Exception ex) {
			logger.error(String.format("创建登录账户(Login)--[Tenant: %s]的登录用户出错，错误消息：%s, 脚本：%s", tenantName,
					ex.getMessage(), sbSql));
			return false;
		}

		try {
			// 创建Schema
			sbSql = new StringBuilder();
			switch (tenant.getDatabaseType()) {
			case MySql:// MySql无Schema定义（Database即为Schema）
				break;
			default:
				sbSql.append("IF NOT EXISTS (");
				sbSql.append("  SELECT name FROM sys.schemas ");
				sbSql.append(String.format("    WHERE name = N'%s' ) ", tenantName));
				sbSql.append("BEGIN ");
				// sbSql.AppendLine(String.Format(" EXEC N'CREATE SCHEMA [%s] AUTHORIZATION
				// [dbo]'", tenantName));
				sbSql.append(String.format("  EXEC (N'CREATE SCHEMA [%s]')", tenantName));
				sbSql.append("END");

				// logger.debug(String.Format("%s：创建Schema--Sql：\r\n%s", tenantName, sbSql));
				new DBHelper(tenantOwnConnection);
				DBHelper.ExecSql(sbSql.toString());
				break;
			}

			if (!IsExistUser(tenantDbConStr, userName, tenant.getDatabaseType())) {
				sbSql = new StringBuilder();
				switch (tenant.getDatabaseType()) {
				case MySql:
					sbSql.append(String.format("CREATE USER '%s''%' IDENTIFIED BY '%s';", userName, tenantPwd));
					sbSql.append(String.format("GRANT all privileges on `%s`.* to '%s''%' IDENTIFIED BY '%s';",
							tenantDatabase, userName, tenantPwd));
					// logger.debug(String.Format("%s：用户授权（User:%s）--Sql：\r\n%s", tenantName,
					// userName,sbSql));
					new DBHelper(masterTenantConnection);
					DBHelper.ExecSql(sbSql.toString());

					sbSql = new StringBuilder();
					sbSql.append(String.format("FLUSH privileges"));
					DBHelper.ExecSql(sbSql.toString());
					break;
				default:
					sbSql.append(String.format("CREATE USER [%s] FOR LOGIN [%s] WITH DEFAULT_SCHEMA=[%s]", userName,
							tenantName));
					// logger.debug(String.Format("%s：创建数据库用户--Sql：\r\n%s", tenantName, sbSql));
					new DBHelper(tenantOwnConnection);
					DBHelper.ExecSql(sbSql.toString());
					
					sbSql = new StringBuilder();
					sbSql.append(String.format(
							"GRANT ALTER, CREATE SEQUENCE, DELETE, EXECUTE, INSERT, REFERENCES, SELECT, TAKE OWNERSHIP, UPDATE, VIEW CHANGE TRACKING, VIEW DEFINITION  ON SCHEMA::[%s] TO [%s]",
							tenantName, userName));
					sbSql.append(String.format("EXEC sp_addrolemember N'db_ddladmin', N'%s'", userName));
					// logger.debug(String.Format("%s：用户授权（User）--Sql：\r\n%s", tenantName, sbSql));
					new DBHelper(tenantOwnConnection);
					DBHelper.ExecSql(sbSql.toString());
					break;
				}
			}
		} catch (Exception ex) {
			logger.error(String.format("创建用户角色及给角色赋权限--[Tenant: %s]的登录用户出错，错误消息：%s, 脚本：%s", tenantName,
					ex.getMessage(), sbSql));
			return false;
		}

		try {
			if (isProfessional && IsExistLoginUser(masterDbConStr, tenantName, tenant.getDatabaseType())) {
				// sbSql.Clear();
				// sbSql.AppendLine(String.Format(" DENY VIEW any DATABASE TO PUBLIC "));
				// sbSql.AppendLine(String.Format(" ALTER AUTHORIZATION ON DATABASE::[%s] TO
				// [%s] ", tenantDatabase, tenantName));
				// logger.debug(String.Format("%s：限制登录账户（Login）的访问权限--Sql：\r\n%s", tenantName,
				// sbSql));
				// SqlDatabaseHelper.ExecuteNonQuery(masterDbConStr, sbSql.ToString(), null);
			}
		} catch (Exception ex) {
			logger.error(String.format("创建登录账户(Login)--[Tenant: %s]的登录用户出错，错误消息：%s, 脚本：%s", tenantName,
					ex.getMessage(), sbSql));
			return false;
		}

		return true;
	}

	/**删除租户的数据库登录用户
	 * @param tenant
	 * @param connectString SA用户的连接字符串
	 * @return
	 */
	public static boolean DeleteTenantDbLoginUser(Tenant tenant, String connectString) {
		String tenantName = tenant.getTenantName();
		String tenantDatabase = tenant.getDatabase();
		String userName = UserPreString + tenantName;

		if (StringExtensions.isNullOrEmpty(connectString))
			connectString = GlobalConfig.GetDecryptDatabaseConnectionString();

		connectString = TenantConnection.convertConnection(tenant.getDatabaseType(), connectString);
		
		// 创建登录名，在AzureSql下，必须在master下创建
		String masterDbConStr = ChangeDatabaseConnectionString(connectString,
				tenant.getDatabaseType() == DatabaseType.MySql ? "Sys" : "master");
		String tenantDbConStr = ChangeDatabaseConnectionString(connectString, tenantDatabase);
		System.out.println("----masterDbConStr: " + masterDbConStr);
		System.out.println("----tenantDbConStr: "+ tenantDbConStr);
		
		TenantConnection masterTenantConnection = new TenantConnection();
		{
			masterTenantConnection.setTenantName(tenant.getTenantName());
			masterTenantConnection.setDatabaseType(tenant.getDatabaseType());
			masterTenantConnection.setConnectionString(masterDbConStr);
		}

		TenantConnection tenantOwnConnection = new TenantConnection();
		{
			masterTenantConnection.setTenantName(tenant.getTenantName());
			masterTenantConnection.setDatabaseType(tenant.getDatabaseType());
			masterTenantConnection.setConnectionString(tenantDbConStr);
		}

		try {
			StringBuilder sbSql = new StringBuilder();
			// 删除数据库Schema的用户
			if (IsExistUser(tenantDbConStr, userName, tenant.getDatabaseType())) {
				sbSql = new StringBuilder();
				switch (tenant.getDatabaseType()) {
				case MySql:
					sbSql.append(String.format("DROP USER '%s''%'", tenantName));
					// logger.debug(String.Format("%s：删除数据库用户（User）--Sql：\r\n%s", tenantName, sbSql));
					new DBHelper(masterTenantConnection);
					DBHelper.ExecSql(sbSql.toString());
					break;
				default:
					sbSql.append(String.format("   ALTER AUTHORIZATION ON SCHEMA::[%s] TO [dbo]", tenantName));
					// sbSql.AppendLine(String.Format(" EXEC sp_droprolemember N'%s', N'%s' ", DefaultTenantRole, userName));
					sbSql.append(String.format("   DROP USER %s", userName));
					// logger.debug(String.Format("%s：删除数据库用户（User）--Sql：\r\n%s", tenantName, sbSql));
					new DBHelper(tenantOwnConnection);
					 DBHelper.ExecSql(sbSql.toString());
					break;
				}

			}

			// 删除Tenant角色
			// sbSql.Clear();
			// sbSql.AppendLine("IF NOT EXISTS (");
			// sbSql.AppendLine(" SELECT members.[name] ");
			// sbSql.AppendLine(" FROM sys.database_role_members AS rolemembers ");
			// sbSql.AppendLine(" JOIN sys.database_principals AS roles ON
			// roles.[principal_id] = rolemembers.[role_principal_id] ");
			// sbSql.AppendLine(" JOIN sys.database_principals AS members ON
			// members.[principal_id] = rolemembers.[member_principal_id] ");
			// sbSql.AppendLine(String.Format(" WHERE roles.[Name]=N'%s' ) ",
			// DefaultTenantRole));
			// sbSql.AppendLine("BEGIN");
			// sbSql.AppendLine(String.Format(" DROP ROLE [%s] ", DefaultTenantRole));
			// sbSql.AppendLine("END");
			// logger.debug(String.Format("%s：删除Tenant角色（Role）--Sql：\r\n%s", tenantName,
			// sbSql));
			// SqlDatabaseHelper.TranExecuteNonQuery(tenantDbConStr, sbSql.ToString(),
			// null);

			// 删除登录用户(Login)
			if (tenant.getDatabaseType() == DatabaseType.SqlServer
					&& IsExistLoginUser(masterDbConStr, tenantName, tenant.getDatabaseType())) {
				sbSql = new StringBuilder();
				sbSql.append(String.format("   DROP LOGIN [%s]", tenantName));
				new DBHelper(masterTenantConnection);
				// logger.debug(String.Format("%s：删除登录账户（Login）--Sql：\r\n%s", tenantName, sbSql));
				DBHelper.ExecSql(sbSql.toString());
			}

			return true;
		} catch (Exception ex) {
			logger.error(String.format("删除Login及User--[Tenant: %s]的登录用户出错，错误消息：%s", tenantName, ex.getMessage()));
			return false;
		}
	}

	private static boolean IsExistDatabase(String masterDbConStr, String tenantDatabase, DatabaseType type) {
		TenantConnection masterTenantConnection = new TenantConnection();
		{
			masterTenantConnection.setDatabaseType(type);
			masterTenantConnection.setConnectionString(masterDbConStr);
		}
		StringBuilder sbSql = new StringBuilder();
		switch (type) {
		case MySql:
			sbSql.append(String.format(
					"SELECT (CASE WHEN count(*)> 0 THEN 1 ELSE 0 END) as Num FROM information_schema.SCHEMATA where SCHEMA_NAME=N'%s'",
					tenantDatabase));
			break;
		default:
			sbSql.append(String.format(
					"IF db_id(N'%s') IS NOT NULL SELECT 1 ELSE SELECT Count(*) FROM sys.databases WHERE [name]=N'%s'",
					tenantDatabase, tenantDatabase));
			break;
		}
		new DBHelper(masterTenantConnection);
		Object result = DBHelper.ExecScalar(sbSql.toString());
		boolean isExistDatabase = Integer.parseInt(result.toString()) > 0;
		return isExistDatabase;
	}

	private static boolean IsExistLoginUser(String masterDbConStr, String tenantName, DatabaseType type) {
		TenantConnection masterTenantConnection = new TenantConnection();
		{
			masterTenantConnection.setDatabaseType(type);
			masterTenantConnection.setConnectionString(masterDbConStr);
		}

		StringBuilder sbSql = new StringBuilder();
		switch (type) {
		case MySql:// MySql无数据库登录用户
			return true;
		default:
			sbSql.append(String.format(
					"IF NOT EXISTS (SELECT [Name] FROM sys.sql_logins WHERE [Name]=N'%s') SELECT 0 ELSE SELECT 1",
					tenantName));
			break;
		}
		new DBHelper(masterTenantConnection);
		Object result = DBHelper.ExecScalar(sbSql.toString());
		boolean isExistUser = Integer.parseInt(result.toString()) > 0;
		return isExistUser;
	}

	private static boolean IsExistUser(String masterDbConStr, String userName, DatabaseType type) {
		TenantConnection masterTenantConnection = new TenantConnection();
		{
			masterTenantConnection.setDatabaseType(type);
			masterTenantConnection.setConnectionString(masterDbConStr);
		}

		StringBuilder sbSql = new StringBuilder();
		switch (type) {
		case MySql:
			sbSql.append(String.format(
					"SELECT (CASE WHEN count(*)> 0 THEN 1 ELSE 0 END) as Num FROM mysql.user where User=N'%s'",
					userName));
			break;
		default:
			sbSql.append(String.format(
					"IF NOT EXISTS (SELECT [Name] FROM sys.database_principals WHERE [Name]=N'%s') SELECT 0 ELSE SELECT 1",
					userName));
			break;
		}
		new DBHelper(masterTenantConnection);
		Object result = DBHelper.ExecScalar(sbSql.toString());
		boolean isExistUser = Integer.parseInt(result.toString()) > 0;
		return isExistUser;
	}

	private static boolean CanConnectDatabase(String connectionStr, DatabaseType type) {
		try {
			TenantConnection masterTenantConnection = new TenantConnection();
			{
				masterTenantConnection.setDatabaseType(type);
				masterTenantConnection.setConnectionString(connectionStr);
			}

			return new DBHelper(masterTenantConnection).CanConnectDatabase();
		} catch (Exception ex) {
			logger.error(String.format("连接数据库[Connection String：%s]出错; 错误消息：%s", connectionStr, ex.getMessage()));
			return false;
		}
	}

	private static String ChangeDatabaseConnectionString(String connectString, String replaceDatabseName) {
		return connectString.replaceAll("(?<=databaseName=)(.*?)(?=;user)", replaceDatabseName);
	}
}
