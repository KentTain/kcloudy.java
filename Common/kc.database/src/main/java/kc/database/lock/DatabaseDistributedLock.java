package kc.database.lock;

import kc.framework.GlobalConfig;
import kc.framework.TimeSpan;
import kc.framework.enums.DatabaseType;
import kc.framework.lock.DistributedLockAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.function.Consumer;

/**
 * 数据库的分布式锁，使用数据库的行级锁--表：dbo.sys_DatabaseDistributedLock
 * 
 * @author 田长军
 * 
 */
public abstract class DatabaseDistributedLock extends DistributedLockAbstract<Boolean> {
	private Logger logger = LoggerFactory.getLogger(DatabaseDistributedLock.class);
	private static final String DefaultDbo = "dbo";
	private static final String LockTableName = "sys_DatabaseDistributedLock";

	protected Connection getConnection(DatabaseType dbType, String connectionString) {
		try {
			String userName = "sa"; // 默认用户名
			String userPwd = "P@ssw0rd"; // 密码

			switch (dbType) {
				case MySql:
					userName = "root";
					userPwd = "P@ssw0rd";
					Class.forName("com.mysql.cj.jdbc.Driver");
					// DriverManager.registerDriver(new com.mysql.jdbc.Driver());
					break;
				case SqlServer:
					userName = "sa";
					userPwd = "P@ssw0rd";
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
					// DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
					break;
				default:
					break;
			}
			return DriverManager.getConnection(connectionString, userName, userPwd);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return null;
		}
	}

	protected static String GetCreateLockTableSql(DatabaseType dbType) {
		String sql = "";
		switch (dbType) {
		case MySql:
			sql = String.format("CREATE TABLE IF NOT EXISTS `%s.%s` (\r\n"
					+ "`Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',\r\n"
					+ "`LockKey` varchar(64) NOT NULL DEFAULT '' COMMENT '锁定的方法名',\r\n"
					+ "`Desc` varchar(1024) NOT NULL DEFAULT '备注信息',\r\n"
					+ "`UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '保存数据时自动生成',\r\n"
					+ "PRIMARY KEY (`id`),\r\n" + "UNIQUE KEY `IX_%s_LockKey` (`LockKey`) USING BTREE\r\n"
					+ "              ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='锁定中的方法';",
					DefaultDbo, LockTableName, LockTableName);
			break;
		default:
			sql = String.format("IF (NOT EXISTS (SELECT * \r\n" + "FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '%s' AND  TABLE_NAME = '%s'))\r\n"
					+ "   BEGIN\r\n"
					+ "        CREATE TABLE [%s].[%s](\r\n"
					+ "           [Id] [int] IDENTITY(1,1) NOT NULL,\r\n"
					+ "           [LockKey] [nvarchar](64) NULL,\r\n"
					+ "           [Desc] [nvarchar](1024) NULL,\r\n"
					+ "           [UpdateTime] [datetime] NULL,\r\n"
					+ "        CONSTRAINT [IX_%s_LockKey] UNIQUE ([LockKey]),\r\n"
					+ "        CONSTRAINT [PK_%s] PRIMARY KEY CLUSTERED,\r\n"
					+ "       (\r\n"
					+ "           [Id] ASC\r\n"
					+ "       )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\r\n"
					+ "       ) ON [PRIMARY]\r\n"
					+ "   END", DefaultDbo, LockTableName, DefaultDbo, LockTableName, LockTableName, LockTableName);
			break;
		}

		return sql;
	}

	protected String InsertLockKey(DatabaseType dbType, String locakKey, String value) {
		String sql = "";
		switch (dbType) {
		case MySql:
			sql = String.format(
					"INSERT INTO `%s.%s` (`LockKey`,`Desc`,`UpdateTime`)"
							+ "VALUES ('%s','%s', UTC_TIMESTAMP())ON DUPLICATE KEY UPDATE  LockKey=VALUES(LockKey)",
					DefaultDbo, LockTableName, locakKey, value);
			break;
		default:
			sql = String.format(
					"IF NOT EXISTS (SELECT * FROM [%s].[%s] WHERE [LockKey] = '%s')\r\n" + "      BEGIN\r\n"
							+ "        INSERT INTO [%s].[%s] ([LockKey],[Desc],[UpdateTime])\r\n"
							+ "        VALUES ('%s','%s', getutcdate())\r\n" + "      END",
					DefaultDbo, LockTableName, locakKey, value);
			break;
		}

		return sql;
	}

	protected String GetLockRowSql(DatabaseType dbType, String locakKey) {
		String sql = "";
		switch (dbType) {
		case MySql:
			sql = String.format("select `Id` from `%s.%s` where `LockKey`='%s' for update", DefaultDbo, LockTableName,
					locakKey);
			break;
		default:
			sql = String.format("select [Id] from [%s].[%s] WITH (ROWLOCK,XLOCK) where [LockKey]=N'%s'", DefaultDbo,
					LockTableName, locakKey);
			break;
		}

		return sql;
	}

	protected boolean executeCommand(Connection conn, String cmdText) {
		if (conn == null) {
			return false;
		}

		Statement statement = null;
		try {
			statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			if (statement == null) {
				return false;
			}

			int i = statement.executeUpdate(cmdText);
			return i > 0;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();

			return false;
		} finally {
			close(statement);
		}
	}

	protected void close(Object obj) {
		if (obj == null) {
			return;
		}
		try {
			if (obj instanceof Statement) {
				((Statement) obj).close();
			} else if (obj instanceof PreparedStatement) {
				((PreparedStatement) obj).close();
			} else if (obj instanceof ResultSet) {
				((ResultSet) obj).close();
			} else if (obj instanceof Connection) {
				((Connection) obj).close();
			}
		} catch (SQLException ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
}