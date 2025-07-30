package kc.database.lock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kc.framework.GlobalConfig;
import kc.framework.TimeSpan;
import kc.framework.enums.DatabaseType;
import kc.framework.extension.DateExtensions;
import kc.framework.lock.DistributedLockAbstract;

/**
 * MySql的分布式锁，使用数据库的行级锁--表：dbo.sys_DatabaseDistributedLock
 * 
 * @author 田长军
 * 
 */
public class MySqlDistributedLock extends DatabaseDistributedLock {
	private Logger logger = LoggerFactory.getLogger(MySqlDistributedLock.class);
	public static final String DefaultDbo = "dbo";
	public static final String LockTableName = "sys_DatabaseDistributedLock";

	private static final DatabaseType dbType = DatabaseType.MySql;

	private static class SingletonHolder{
        private static MySqlDistributedLock instance=new MySqlDistributedLock();
    }
    private MySqlDistributedLock(){
    	Connection connection = null;
		try {
			final String connectionString = GlobalConfig.GetDecryptMySqlConnectionString();
			System.out.println("MySql Distributed Lock connect String: " + connectionString);
			connection = getConnection(dbType, connectionString);
			// 创建锁表及插入行
			String createTableSql = GetCreateLockTableSql(dbType);

			//logger.debug(createTableSql);
			executeCommand(connection, createTableSql);
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			logger.error(e.getMessage(), e);
		} finally {
			close(connection);
		}
    }
    public static MySqlDistributedLock getInstance(){
    	return SingletonHolder.instance;
    }
	
	/**
	 * MySql的分布式锁
	 */
	@Override
	public void DoDistributedLock(String key, TimeSpan acquireTimeout, TimeSpan lockTimeOut, Consumer<Boolean> action) {
		// TODO Auto-generated method stub
		key = DistributedLockPro + key;
		final String value = java.util.UUID.randomUUID().toString();

		final String connectionString = GlobalConfig.GetDecryptMySqlConnectionString();
		Connection connection = getConnection(dbType, connectionString);

		// 创建锁表及插入行
		String insertDataSql = InsertLockKey(dbType, key, value);
		//logger.debug(String.format("---begin-Insert-lock-sql: %s", insertDataSql));
		executeCommand(connection, insertDataSql);
		//logger.debug(String.format("---end-Insert-lock-sql: %s", insertDataSql));
		
		Statement statement = null;
		try {
			connection.setAutoCommit(false);
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			String cmdText = GetLockRowSql(dbType, key);
			
			long nowTicks = DateExtensions.getTicks(new Date());
			long end = nowTicks + acquireTimeout.Ticks();
			while (nowTicks < end) {
				nowTicks = DateExtensions.getTicks(new Date());
				boolean result = statement.execute(cmdText);
				//logger.debug(String.format("----cmdText: %s, result: %s", cmdText, result));
				if (result) {
					try {
						action.accept(result);
					} finally {
						//logger.debug(String.format("---begin-Release-nowTicks: %s, end: %s, timeoutmilliseconds: %s, result: %s", nowTicks, end, acquireTimeout.TotalMilliseconds(), result));
						connection.commit();
						//logger.debug(String.format("---end-Release-nowTicks: %s, end: %s, timeoutmilliseconds: %s, result: %s", nowTicks, end, acquireTimeout.TotalMilliseconds(), result));
					}

					return;
				}

				try {
					Thread.sleep(10);
				} catch (Exception e) {
					Thread.currentThread().interrupt();
				}
			}

		} catch (SQLException e1) {
			logger.error(e1.getMessage(), e1);
		} finally {
			try {
				close(statement);
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

}