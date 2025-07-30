package kc.component.util;

import kc.framework.GlobalConfig;
import kc.framework.TestBase;
import kc.framework.enums.SystemType;

import net.sourceforge.groboutils.junit.v1.TestRunnable;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.*;
//import org.springframework.test.context.junit.jupiter.*;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

//@org.junit.jupiter.api.DisplayName("Redis分布式锁单元测试")
//@org.junit.jupiter.api.Disabled("RedisDistributedLockTest")
//@org.junit.jupiter.api.ExtendWith(SpringExtension.class)
@org.junit.Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisDistributedLockTest extends TestBase{
	private Logger logger = LoggerFactory.getLogger(RedisDistributedLockTest.class);
	private int n = 100;// 库存：500
	private int iCount = 100; // 线程数

	@Autowired
	private RedisDistributedLock redisDistributedLock;

	@org.junit.BeforeClass
	//@org.junit.jupiter.api.BeforeAll
	public static void setUpBeforeClass() {
		intilize();
	}
	
	@org.junit.AfterClass
	//@org.junit.jupiter.api.AfterAll
	public static void setDownAfterClass() {
		tearDown();
	}

	/* ------JUnit4使用groboutils进行多线程测试
	 */
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
		TestRunnable[] trs = new TestRunnable[iCount];
		for (int i = 0; i < iCount; i++) {
			trs[i] = new NoDistributedLock_SecKill();
		}

		// 用于执行多线程测试用例的Runner，将前面定义的单个Runner组成的数组传入
		net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner mttr = new net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner(trs);
		// 开发并发执行数组里定义的内容
		mttr.runTestRunnables();

		String result = toCommaSeparatedInt(SecList.NoKillSet);

		logger.info("--NoDistributedLock-except: " + except);
		logger.info("--NoDistributedLock-result: " + result);

		org.junit.Assert.assertNotEquals(except, result);
	}

	private class RedisDistributedLockTest_SecKill extends net.sourceforge.groboutils.junit.v1.TestRunnable {
		@Override
		public void runTest() throws Throwable {
			redisDistributedLock.DoDistributedLock("RedisDistributedLockTest_SecKill", (success) -> {
				SecKill("RedisDistributedLockTest_SecKill", SecList.RedisSecKillSet);
			});
		}
	}
	@org.junit.Test
	//@org.junit.jupiter.api.Test
	public void test_RedisDistributedLockTest_SecKill() throws Throwable {
		n = 100;// 库存：500
		String except = java.util.stream.IntStream.range(0, n).boxed().map(i -> (n - i - 1))
				.map(Object::toString).collect(Collectors.joining(","));

		// Runner数组，想当于并发多少个。
		TestRunnable[] trs = new TestRunnable[iCount];
		for (int i = 0; i < iCount; i++) {
			trs[i] = new RedisDistributedLockTest_SecKill();
		}

		// 用于执行多线程测试用例的Runner，将前面定义的单个Runner组成的数组传入
		net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner mttr = new net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner(trs);
		// 开发并发执行数组里定义的内容
		mttr.runTestRunnables();

		String result = toCommaSeparatedInt(SecList.RedisSecKillSet);


		logger.info("--RedisDistributedLockTest-except: " + except);
		logger.info("--RedisDistributedLockTest-result: " + result);

		org.junit.Assert.assertEquals(except, result);
	}

	/**
	 * 保存多线程秒杀后的剩余商品数
	 * @author tianc
	 *
	 */
	private static class SecList {
		public static final List<Integer> NoKillSet = new ArrayList<Integer>();

		public static final List<Integer> RedisSecKillSet = new ArrayList<Integer>();

		public static final List<Integer> SqlServerSecKillSet = new ArrayList<Integer>();

		public static final List<Integer> MySqlSecKillSet = new ArrayList<Integer>();

	}

	/**
	 * 秒杀
	 * 
	 * @param name 测试方法名称
	 * @param left 剩余商品数列表
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
