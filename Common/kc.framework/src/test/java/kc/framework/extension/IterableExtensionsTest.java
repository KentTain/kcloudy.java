package kc.framework.extension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import kc.framework.base.Apple;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class IterableExtensionsTest {

	private static List<Apple> appleList; // 苹果2, 苹果1, 苹果1, 香蕉, 荔枝, 苹果3

	@BeforeAll
	static void setUpBeforeClass() {
	}

	@BeforeEach
	void setUp() {
		appleList = new ArrayList<>();// 存放apple对象集合

		Apple apple1 = new Apple(1, "苹果1", new BigDecimal("3.25"), 10);
		Apple apple12 = new Apple(1, "苹果2", new BigDecimal("1.35"), 20);
		Apple apple13 = new Apple(1, "苹果3", new BigDecimal("1.35"), 20);
		Apple apple2 = new Apple(2, "香蕉", new BigDecimal("2.89"), 30);
		Apple apple3 = new Apple(3, "荔枝", new BigDecimal("9.99"), 40);

		appleList.add(apple12);
		appleList.add(apple1);
		appleList.add(apple1);
		appleList.add(apple2);
		appleList.add(apple3);
		appleList.add(apple13);

		//String result = ListExtensions.toCommaSeparatedStringByFilter(appleList, s -> s.getName());
		//System.out.println("\nappleList: " + result);
	}

	@Test
	void testWhere() {
		Iterable<Apple> appleResult = IterableExtensions.where(appleList, s -> s.getName().startsWith("苹果"));
		String except = "苹果2,苹果1,苹果1,苹果3";
		String result = ListExtensions.toCommaSeparatedStringByFilter(IterableExtensions.toList(appleResult), s -> s.getName());
		//System.out.println("\ntestDistinct appleResult: " + result);

		assertEquals(except, result);
	}

	@Test
	void testSelect() {
		Iterable<String> appleResult = IterableExtensions.select(appleList, s -> s.getName());
		String except = "苹果2,苹果1,苹果1,香蕉,荔枝,苹果3";
		String result = ListExtensions.toCommaSeparatedStringByFilter(IterableExtensions.toList(appleResult), s -> s);
		//System.out.println("\ntestDistinct appleResult: " + result);

		assertEquals(except, result);
	}
	
	@Test
	void testSelectByFilter() {
		Iterable<String> appleResult = IterableExtensions.select(appleList, s -> s.getName(), s -> s.getName().startsWith("苹果"));
		String except = "苹果2,苹果1,苹果1,苹果3";
		String result = ListExtensions.toCommaSeparatedStringByFilter(IterableExtensions.toList(appleResult), s -> s);
		//System.out.println("\ntestDistinct appleResult: " + result);

		assertEquals(except, result);
	}
}
