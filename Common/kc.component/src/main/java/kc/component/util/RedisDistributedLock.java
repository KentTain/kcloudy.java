package kc.component.util;

import kc.framework.GlobalConfig;
import kc.framework.TimeSpan;
import kc.framework.enums.DatabaseType;
import kc.framework.extension.DateExtensions;
import kc.framework.extension.StringExtensions;
import kc.framework.lock.DistributedLockAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.Date;
import java.util.function.Consumer;

/**
 * redis的分布式锁，使用redis setnx
 * 
 * @author 田长军
 * 
 */
@Component("RedisDistributedLock")
public class RedisDistributedLock extends DistributedLockAbstract<Boolean> {
	private Logger logger = LoggerFactory.getLogger(RedisDistributedLock.class);
	@Autowired
	private IRedisHelper redisHelper;
	
	/**
	 * Redis的分布式锁
	 */
	@Override
	public void DoDistributedLock(String key, TimeSpan acquireTimeout, TimeSpan lockTimeOut, Consumer<Boolean> action) {
		key = DistributedLockPro + key;
		final String value = java.util.UUID.randomUUID().toString();
		final String connString = GlobalConfig.GetDecryptRedisConnectionString();

		long nowTicks = DateExtensions.getTicks(new Date());
		long end = nowTicks + acquireTimeout.Ticks();
		while (nowTicks < end) {
			nowTicks = DateExtensions.getTicks(new Date());
			long lLockTimeOut = lockTimeOut.TotalMilliseconds();
			boolean result = redisHelper.setNx(key, value, lLockTimeOut);
			//logger.debug(String.format("----cmdText: %s, result: %s", cmdText, result));
			if (result) {
				try {
					action.accept(result);
				} finally {
					//logger.debug(String.format("---begin-Release-nowTicks: %s, end: %s, timeoutmilliseconds: %s, result: %s", nowTicks, end, acquireTimeout.TotalMilliseconds(), result));
					redisHelper.del(key);
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
	}

}