package kc.framework.extension;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class DateExtensionsTest {

	private final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		//Date date = formatter.parse("2019/03/02 12:20:00");

		//ZoneId defaultZoneId = ZoneId.systemDefault();
		//System.out.print("\nDefault Zone IDs: " + defaultZoneId.toString());

		//System.out.print("\nDate format: " + formatter.format(date));

		//LocalDateTime now = LocalDateTime.now();
		//DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		//System.out.println("\nLocal DateTime format: " + dtFormatter.format(now));
	}

	@Test
	void test_AddYear() throws ParseException {
		Date date = formatter.parse("2019/03/02 12:20:00");
		Date except = formatter.parse("2021/03/02 12:20:00");
		Date result = DateExtensions.addYear(date, 2);

		assertEquals(except, result);
	}

	@Test
	void test_AddMonth() throws ParseException {
		Date date = formatter.parse("2019/03/02 12:20:00");
		Date except = formatter.parse("2019/05/02 12:20:00");
		Date result = DateExtensions.addMonth(date, 2);

		assertEquals(0, except.compareTo(result));
	}

	@Test
	void test_AddDay() throws ParseException {
		Date date = formatter.parse("2019/03/02 12:20:00");
		Date except = formatter.parse("2019/03/12 12:20:00");
		Date result = DateExtensions.addDay(date, 10);

		assertEquals(0, except.compareTo(result));
	}

	@Test
	void test_GetDateString() throws ParseException {
		String except = "2019/03/12 12:20:00";
		Date date = formatter.parse(except);
		String result = DateExtensions.getDateString(date, DateExtensions.FMT_yMdHms2);

		assertEquals(except, result);
	}

	@Test
	void test_NowDateString() throws ParseException {
		String result = DateExtensions.getNowDateString(DateExtensions.FMT_yMdHms2);
		Date date = formatter.parse(result);
		String except = DateExtensions.getDateString(date, DateExtensions.FMT_yMdHms2);

		assertEquals(except, result);
	}

	/*
	 * @Test public void testGetDays() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testCountHours() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testCountDays() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetDateStringString() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetDateString() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetDateDateString() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetDateDateSimpleDateFormat() {
	 * fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetNowDateString() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetNowDateSimpleDateFormat() {
	 * fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetLastMonthFirstDay() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetLastMonthLastDay() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetFirstDay() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetLastDay() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetNthWeekendOfMonth() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetTheLastWeekendOfMonth() {
	 * fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetNthWeekdayOfMonth() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetTheLastWeekdayOfMonth() {
	 * fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetDayOfWeek() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetNthWeekOfMonth() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetTheLastWeekOfMonth() { fail("Not yet implemented");
	 * }
	 * 
	 * @Test public void testGetNthDayOfMonth() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetTheFirstDayOfWeek() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetTheFirstDayOfMonth() { fail("Not yet implemented");
	 * }
	 * 
	 * @Test public void testGetTheLastDayOfMonth() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetTheFirstDayOfYear() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetZeroHourOfDay() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetZeroHourOfTheNextDay() {
	 * fail("Not yet implemented"); }
	 * 
	 * @Test public void testIsWeeklyCycleWeekdays() { fail("Not yet implemented");
	 * }
	 * 
	 * @Test public void testIsBusinessDay() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetTicks() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testDateToLocalDateTime() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testToRelativeDateString() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testToRelativeDateStringUtc() {
	 * fail("Not yet implemented"); }
	 * 
	 * @Test public void testToLocalDate() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testToLocalDateStr() { fail("Not yet implemented"); }
	 */

}
