package kc.database.lock;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kc.framework.GlobalConfig;
import kc.framework.TestBase;
import kc.framework.enums.SystemType;

@org.junit.Ignore
//@org.junit.jupiter.api.Disabled
public class DatabaseDistributedLockTest extends TestBase{
	private Logger logger = LoggerFactory.getLogger(DatabaseDistributedLockTest.class);
	private int n = 100;// 库存：500
	private int iCount = 100; // 线程数

	//@org.junit.jupiter.api.BeforeAll
	@org.junit.BeforeClass
	public static void setUpBeforeClass() throws ParseException {
		GlobalConfig.EncryptKey = "dev-cfwin-EncryptKey";
		GlobalConfig.SystemType = SystemType.Dev;
		GlobalConfig.DatabaseConnectionString = "jdbc:sqlserver://127.0.0.1;databaseName=sm_project";
		GlobalConfig.MySqlConnectionString = "jdbc:mysql://localhost:3306/sm_project?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true";
		
		intilize();
	}

	@org.junit.AfterClass
	//@org.junit.jupiter.api.AfterAll
	public static void setDownAfterClass() throws ParseException {
		GlobalConfig.DatabaseConnectionString = "";
		GlobalConfig.MySqlConnectionString = "";
		
		tearDown();
	}

	private class NoDistributedLock_SecKill extends net.sourceforge.groboutils.junit.v1.TestRunnable {
		@Override
		public void runTest() throws Throwable {
			SecKill("NoDistributedLock_SecKill_Test", SecList.NoKillSet);
		}
	}
	@org.junit.Test
	//@org.junit.jupiter.api.Test
	public void test_NoDistributedLock_SecKill() throws Throwable {
		n = 100;// 库存：100
		String except = java.util.stream.IntStream.range(0, n).boxed().map(i -> (n - i - 1))
				.map(Object::toString).collect(Collectors.joining(","));

		// Runner数组，想当于并发多少个。
		net.sourceforge.groboutils.junit.v1.TestRunnable[] trs = new net.sourceforge.groboutils.junit.v1.TestRunnable[iCount];
		for (int i = 0; i < iCount; i++) {
			trs[i] = new NoDistributedLock_SecKill();
		}

		// 用于执行多线程测试用例的Runner，将前面定义的单个Runner组成的数组传入
		net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner mttr = new net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner(trs);
		// 开发并发执行数组里定义的内容
		mttr.runTestRunnables();

		String result = toCommaSeparatedInt(SecList.NoKillSet);

		logger.debug("--NoDistributedLock-except: " + except);
		logger.debug("--NoDistributedLock-result: " + result);

		//org.junit.Assert.assertNotEquals(except, result);
	}

	private class SqlServerDistributedLock_SecKill extends net.sourceforge.groboutils.junit.v1.TestRunnable {
		@Override
		public void runTest() throws Throwable {
			SqlServerDistributedLock.getInstance().DoDistributedLock("SqlServerDistributedLock_SecKill", (success) -> {
				SecKill("SqlServerDistributedLock_SecKill", SecList.SqlServerSecKillSet);
			});
		}
	}
	@org.junit.Test
	//@org.junit.jupiter.api.Test
	public void test_SqlServerDistributedLock_SecKill() throws Throwable {
		n = 100;// 库存：500
		String except = java.util.stream.IntStream.range(0, n).boxed().map(i -> (n - i - 1))
				.map(Object::toString).collect(Collectors.joining(","));

		// Runner数组，想当于并发多少个。
		net.sourceforge.groboutils.junit.v1.TestRunnable[] trs = new net.sourceforge.groboutils.junit.v1.TestRunnable[iCount];
		for (int i = 0; i < iCount; i++) {
			trs[i] = new SqlServerDistributedLock_SecKill();
		}

		// 用于执行多线程测试用例的Runner，将前面定义的单个Runner组成的数组传入
		net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner mttr = new net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner(trs);
		// 开发并发执行数组里定义的内容
		mttr.runTestRunnables();

		String result = toCommaSeparatedInt(SecList.SqlServerSecKillSet);

		logger.debug("--SqlServerDistributedLock-except: " + except);
		logger.debug("--SqlServerDistributedLock-result: " + result);

		org.junit.Assert.assertEquals(except, result);
	}

	private class MySqlDistributedLock_SecKill extends net.sourceforge.groboutils.junit.v1.TestRunnable {
		@Override
		public void runTest() throws Throwable {
			MySqlDistributedLock.getInstance().DoDistributedLock("MySqlDistributedLock_SecKill", (success) -> {
				SecKill("MySqlDistributedLock_SecKill", SecList.MySqlSecKillSet);
			});
		}
	}
	@org.junit.Test()
	//@org.junit.jupiter.api.Test
	public void test_MySqlDistributedLock_SecKill() throws Throwable {
		n = 100;// 库存：500
		String except = java.util.stream.IntStream.range(0, n).boxed().map(i -> (n - i - 1))
				.map(Object::toString).collect(Collectors.joining(","));

		// Runner数组，想当于并发多少个。
		net.sourceforge.groboutils.junit.v1.TestRunnable[] trs = new net.sourceforge.groboutils.junit.v1.TestRunnable[iCount];
		for (int i = 0; i < iCount; i++) {
			trs[i] = new MySqlDistributedLock_SecKill();
		}

		// 用于执行多线程测试用例的Runner，将前面定义的单个Runner组成的数组传入
		net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner mttr = new net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner(trs);
		// 开发并发执行数组里定义的内容
		mttr.runTestRunnables();

		String result = toCommaSeparatedInt(SecList.MySqlSecKillSet);

		logger.debug("--MySqlDistributedLock-except: " + except);
		logger.debug("--MySqlDistributedLock-result: " + result);

		org.junit.Assert.assertEquals(except, result);
	}
	
	/**
	 * 保存多线程秒杀后的剩余商品数
	 * @author tianc
	 *
	 */
	private static class SecList {
		public static final List<Integer> NoKillSet = new ArrayList<Integer>();

		//public static final List<Integer> RedisSecKillSet = new ArrayList<Integer>();

		public static final List<Integer> SqlServerSecKillSet = new ArrayList<Integer>();

		public static final List<Integer> MySqlSecKillSet = new ArrayList<Integer>();

	}

	/**
	 * 秒杀
	 * 
	 * @param name 秒杀类型
	 * @param left 秒杀剩余库存数列表
	 */
	private void SecKill(String name, List<Integer> left) {
		try {
			if(n == 0) return;

			--n;
			//System.out.println(String.format("Thread[%s--%s]获得了锁，剩余库存：%s", name, Thread.currentThread().getId(), n));

			left.add(n);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * 将一个int列表，转换成：1, 2, 3
	 * 
	 * @param enumeration 需要转换的列表
	 * @return String
	 */
	private static String toCommaSeparatedInt(List<Integer> enumeration) {
		if (enumeration == null || enumeration.size() == 0)
			return "";

		Function<Integer, String> selector = s -> s != null ? s.toString() : "";
		return enumeration.stream().map(selector).collect(Collectors.joining(","));
	}

}
