package kc.framework.extension;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public final class DateExtensions {
	public static DateTimeFormatter milliSecondFmt1 = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
	public static DateTimeFormatter milliSecondFmt2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
	public static DateTimeFormatter FMT_yMd1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static DateTimeFormatter FMT_yMdHms1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static DateTimeFormatter FMT_yMd2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	public static DateTimeFormatter FMT_yMdHms2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	public static DateTimeFormatter FMT_yMd3 = DateTimeFormatter.ofPattern("yyyyMMdd");
	public static DateTimeFormatter FMT_yMdHms3 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	public static DateTimeFormatter FMT_yMdHm1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	public static DateTimeFormatter FMT_yMdHm2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
	public static DateTimeFormatter FMT_yMdHm3 = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
	public static DateTimeFormatter FMT_yM = DateTimeFormatter.ofPattern("yyyy/MM");
	public static DateTimeFormatter FMT_y = DateTimeFormatter.ofPattern("yyyy");
	public static DateTimeFormatter FMT_yMd4 = DateTimeFormatter.ofPattern("yyyy/M/dd");
	public static DateTimeFormatter FMT_yMd5 = DateTimeFormatter.ofPattern("yyyy/M/d");
	public static DateTimeFormatter[] FMTS = new DateTimeFormatter[] { FMT_yMd1, FMT_yMdHms1, FMT_yMd2, FMT_yMdHms2,
			FMT_yMd3, FMT_yMdHms3, FMT_yMdHm1, FMT_yMdHm2, FMT_yMdHm3, FMT_yM, FMT_y, FMT_yMd4, FMT_yMd5 };
	// 默认使用系统当前时区
	public static ZoneId ZONE_LOCAL = ZoneId.systemDefault();
	public static ZoneId ZONE_UTC = ZoneId.of("UTC");
	
	// Number of 100ns ticks per time unit
    private static long TicksPerMillisecond = 10000;
    private static long TicksPerSecond = TicksPerMillisecond * 1000;
    private static long TicksPerMinute = TicksPerSecond * 60;
    private static long TicksPerHour = TicksPerMinute * 60;
    private static long TicksPerDay = TicksPerHour * 24;

    // Number of milliseconds per time unit
    //private static int MillisPerSecond = 1000;
    //private static int MillisPerMinute = MillisPerSecond * 60;
    //private static int MillisPerHour = MillisPerMinute * 60;
    //private static int MillisPerDay = MillisPerHour * 24;

	// Number of days in a non-leap year
    private static int DaysPerYear = 365;
    // Number of days in 4 years
    private static int DaysPer4Years = DaysPerYear * 4 + 1;       // 1461
    // Number of days in 100 years
    private static int DaysPer100Years = DaysPer4Years * 25 - 1;  // 36524
    // Number of days in 400 years
    private static int DaysPer400Years = DaysPer100Years * 4 + 1; // 146097

    // Number of days from 1/1/0001 to 12/31/1600
    //private static int DaysTo1601 = DaysPer400Years * 4;          // 584388
    // Number of days from 1/1/0001 to 12/30/1899
    //private static int DaysTo1899 = DaysPer400Years * 4 + DaysPer100Years * 3 - 367;
    // Number of days from 1/1/0001 to 12/31/1969
    //private static int DaysTo1970 = DaysPer400Years * 4 + DaysPer100Years * 3 + DaysPer4Years * 17 + DaysPerYear; // 719,162
    // Number of days from 1/1/0001 to 12/31/9999
    private static int DaysTo10000 = DaysPer400Years * 25 - 366;  // 3652059

	public static long MinTicks = 0;
	public static long MaxTicks = DaysTo10000 * TicksPerDay - 1;
    
	/**
	 * 时期是否相等
	 * 
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return boolean
	 * @author tianc
	 */
	public static boolean isEquals(Date date1, Date date2) {
		return date1.equals(date2);
	}

	/** 是否为闰年
	 * @param date
	 * @return boolean
	 */
	public static boolean isLeapYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);// 设置时间
		
		int year = calendar.get(Calendar.YEAR);
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }
	
	public static LocalDateTime toLocalDateTime(Date date)
	{
		Instant instant = date.toInstant();
		return LocalDateTime.ofInstant(instant, ZONE_LOCAL);
	}
	
	/**
	 * 将Date转换成LocalDate
	 * 
	 * @param date
	 * @return LocalDate
	 * @author tianc
	 */
	public static LocalDate toLocalDate(Date date) {
		Instant instant = date.toInstant();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE_LOCAL);
		return localDateTime.toLocalDate();
	}

	/**
	 * 将Date转换成LocalTime
	 * 
	 * @param date
	 * @return LocalTime
	 * @author tianc
	 */
	public static LocalTime toLocalTime(Date date) {
		Instant instant = date.toInstant();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE_LOCAL);
		return localDateTime.toLocalTime();
	}

	/**
	 * 获取Utc的本地时间
	 * @return Date
	 */
	public static Date toDateTimeFromLocalDateTime(LocalDateTime localDateTime){
		ZonedDateTime zdt = localDateTime.atZone(ZONE_LOCAL);
		return Date.from(zdt.toInstant());
	}

	/**
	 * 获取Utc的本地时间
	 * @return Date
	 */
	public static Date toDateTimeUtcFromLocalDateTime(LocalDateTime localDateTime){
		ZonedDateTime zdt = localDateTime.atZone(ZONE_UTC);
		return Date.from(zdt.toInstant());
	}

	/**
	 * 在日期上增加相应年份
	 *
	 * @param years 日期
	 * @param years 要增加的年份数 (整数)
	 * @return Date
	 * @author tianc
	 */
	public static Date addYear(Date date, int years) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, years);// 把日期往后增加years年.正数往后推,负数往前移动
		return cal.getTime();
	}

	/**
	 * 在日期上增加相应月份
	 *
	 * @param date   月份
	 * @param months 要增加的月份数 (整数)
	 * @return Date
	 * @author tianc
	 */
	public static Date addMonth(Date date, int months) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months);// 把日期往后增加months月.正数往后推,负数往前移动
		return cal.getTime();
	}

	/**
	 * 在日期上增加相应天数
	 *
	 * @param date 日期
	 * @param days 要增加的天数 (整数)
	 * @return Date
	 * @author tianc
	 */
	public static Date addDay(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);// 把日期往后增加days天.正数往后推,负数往前移动
		return cal.getTime();
	}

	/**
	 * 在当前给定时间 如果是 23:59:59就加一秒</br>
	 *
	 * @param date Date日期类型
	 * @return Date
	 * @author tianc
	 */
	public static Date addOneSecond(Date date) {
		Date ret = null;
		// 格式化为时/分/秒样式
		String dateStr = getDateString(date, "yyyyMMddHHmmss");
		if (dateStr.endsWith("235959")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.SECOND, 1);// 加1秒
			ret = cal.getTime();
		}
		return ret;
	}

	/**
	 * 统计两个日期相差天数 <br/>
	 * 例:相差3700秒,那么3700/3600 = 1 返回 例:24小时是一天,如果相差23小时,本方法返回0,如果相差25小时,本方法返回1,,即舍去余数
	 *
	 * @param date1 第一个日期
	 * @param date2 第二个日期
	 * @return int
	 * @author tianc
	 */
	public static long countDays(Date date1, Date date2) {
		long hours = countHours(date1, date2);
		return  hours / 24;
	}

	/**
	 * 统计两个日期相差小时数 <br/>
	 * 例:3600秒是一小时,那么如果相差3500秒,本方法返回0,如果相差3700秒,本方法返回1,即舍去余数
	 *
	 * @param date1 第一个日期
	 * @param date2 第二个日期
	 * @return int
	 * @author tianc
	 */
	public static long countHours(Date date1, Date date2) {
		long l = countMinutes(date1, date2);
		return l / 60;
	}
	/**
	 * 统计两个日期相差分钟数 <br/>
	 *
	 * @param date1 第一个日期
	 * @param date2 第二个日期
	 * @return int
	 * @author tianc
	 */
	public static long countMinutes(Date date1, Date date2)
	{
		long l = countSeconds(date1, date2);
		return l / 60;
	}
	/**
	 * 统计两个日期相差秒数  <br/>
	 *
	 * @param date1 第一个日期
	 * @param date2 第二个日期
	 * @return int
	 * @author tianc
	 */
	public static long countSeconds(Date date1, Date date2)
	{
		long l = countMilliSeconds(date1, date2);
		return l / 1000;
	}
	/**
	 * 统计两个日期相差毫秒数 <br/>
	 *
	 * @param date1 第一个日期
	 * @param date2 第二个日期
	 * @return int
	 * @author tianc
	 */
	public static long countMilliSeconds(Date date1, Date date2)
	{
		long l = date2.getTime() - date1.getTime();
		return l;
	}
	/**
	 * 统计两个日期相差毫秒数 <br/>
	 *
	 * @param date1 第一个日期
	 * @param date2 第二个日期
	 * @return int
	 * @author tianc
	 */
	public static long countMilliSeconds(LocalDateTime date1, LocalDateTime date2)
	{
		long l = getMilliByTime(date1) - getMilliByTime(date2);
		return l;
	}

	/**
	 * 得到两个日期间的间隔天数 </br>
	 *
	 * @param firstDay String格式 "2015-06-08"
	 * @param lastDay  String格式 "2015-06-07"
	 * @author tianc
	 */
	public static long countDays(String firstDay, String lastDay) {
		if (firstDay == null || "".equals(firstDay)) {
			return 0L;
		}
		if (lastDay == null || "".equals(lastDay)) {
			return 0L;
		}
		LocalDateTime beginDate = LocalDateTime.parse(firstDay, FMT_yMd1);
		LocalDateTime endDate = LocalDateTime.parse(lastDay, FMT_yMd1);
		return ChronoUnit.DAYS.between(beginDate, endDate);
	}

	/**
	 * 获取本地时间
	 * @return LocalDateTime
	 */
	public static LocalDateTime getLocalDateTimeNow()
	{
		return LocalDateTime.now(ZONE_LOCAL);
	}
	
	/**
	 * 获取Utc的本地时间
	 * @return LocalDateTime
	 */
	public static LocalDateTime getLocalDateTimeUtcNow()
	{
		return LocalDateTime.now(ZONE_UTC);
	}
	
	/**
	 * 获取本地时间
	 * @return Date
	 */
	public static Date getDateTimeNow()
	{
		LocalDateTime localDateTime = LocalDateTime.now(ZONE_LOCAL);
		return Date.from(localDateTime.atZone(ZONE_LOCAL).toInstant());
	}
	
	/**
	 * 获取Utc的本地时间
	 * @return Date
	 */
	public static Date getDateTimeUtcNow()
	{
		LocalDateTime localDateTime = LocalDateTime.now(ZONE_UTC);
		return Date.from(localDateTime.atZone(ZONE_UTC).toInstant());
	}

	/**
	 * 以指定样式格式化字符串成日期
	 *
	 * @param specifiedFmt 指定格式化样式的格式化类
	 * @return String
	 * @author tianc
	 */
	public static String getNowDateString(String specifiedFmt) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(specifiedFmt);
		LocalDateTime now = LocalDateTime.now();

		return now .format(formatter);
	}

	/**
	 * 以指定样式格式化字符串成日期
	 *
	 * @param formatter 指定格式化样式的格式化类
	 * @return String
	 * @author tianc
	 */
	public static String getNowDateString(DateTimeFormatter formatter) {
		LocalDateTime now = LocalDateTime.now();

		return now.format(formatter);
	}

	/**
	 * 以指定样式格式化字符串成日期时间
	 *
	 * @param specifiedFmt 指定格式化样式的格式化类
	 * @return String
	 * @author tianc
	 */
	public static String getUTCNowDateString(String specifiedFmt) {
		LocalDateTime current = LocalDateTime.now(ZONE_UTC);
		DateTimeFormatter format = DateTimeFormatter.ofPattern(specifiedFmt);

		return current.format(format);
	}

	/**
	 * 以指定样式格式化字符串成日期时间
	 *
	 * @param formatter 指定格式化样式的格式化类
	 * @return String
	 * @author tianc
	 */
	public static String getUTCNowDateString(DateTimeFormatter formatter) {
		LocalDateTime current = LocalDateTime.now(ZONE_UTC);

		return current.format(formatter);
	}
	
    /**
     * 获取指定日期的毫秒
     * @param time
     * @return
     */
    public static long getMilliByTime(LocalDateTime time) {
        return time.atZone(ZONE_LOCAL).toInstant().toEpochMilli();
    }

	
	/**日期最小值
	 * @return
	 */
	public static Date getMinValue() {
		return new Date(MinTicks);
	}

	/**
	 * 日期最大值
	 * @return
	 */
	public static Date getMaxValue() {
		return new Date(MaxTicks);
	}
	
	/**
	 * 将毫秒时间戳转换成Date
	 * 
	 * @param time 毫秒时间
	 * @return Date
	 * @author tianc
	 */
	public static Date timeMilliToDate(String time) {
		return Date.from(Instant.ofEpochMilli(Long.parseLong(time)));
	}

	/**
	 * 把String转换成Date</br>
	 * 1.先用指定的specifiedFmt去转换 2.指定的specifiedFmt转换失败,则用默认的时间格式化样式数组去转换
	 * 
	 * @param sourceStr    将要被转换成Date类型的String
	 * @param specifiedFmt 指定格式化样式的String
	 * @return Date
	 * @author tianc
	 */
	public static Date getDate(String sourceStr, String specifiedFmt) {
		Date ret = null;
		boolean hasException = true;
		if (StringExtensions.isNullOrEmpty(specifiedFmt)) {
			DateTimeFormatter sdf1 = DateTimeFormatter.ofPattern(specifiedFmt);
			LocalDateTime localDateTime = LocalDateTime.parse(sourceStr, sdf1);

			ret = toDateTimeFromLocalDateTime(localDateTime);
		}
		if (ret != null)// 没有异常表示正常
			return ret;
		ret = getDate(sourceStr);// 有异常就偿试着用默认的格式化
		return ret;
	}

	/**
	 * 把String转换成Date</br>
	 * 用默认的时间格式化样式数组去转换
	 * 
	 * @param sourceStr 将要被转换成Date类型的String
	 * @return Date
	 * @author tianc
	 */
	public static Date getDate(String sourceStr) {
		sourceStr = sourceStr.replace("-", "/");
		LocalDateTime d = null;
		boolean hasException = false;
		for (DateTimeFormatter sdf : FMTS) {
			if (hasException) {// 如果有异常,那么继续格式化转换日期
				try {
					d = LocalDateTime.parse(sourceStr, sdf);
					break;
				} catch (Exception e) {
					hasException = true;
				}
			} else {// 如果没异常了,那么马上退出循环
				break;
			}
		}
		return toDateTimeFromLocalDateTime(d);
	}

	/**
	 * 以指定样式格式化字符串成日期
	 * 
	 * @param date         将要被转换成String类型的Date
	 * @param specifiedFmt 指定格式化样式的String
	 * @return String
	 * @author tianc
	 */
	public static String getDateString(Date date, String specifiedFmt) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(specifiedFmt);
		LocalDateTime localDt = toLocalDateTime(date);
		return formatter.format(localDt);
	}

	/**
	 * 以指定样式格式化字符串成日期
	 * 
	 * @param date      将要被转换成String类型的Date
	 * @param formatter 指定格式化样式的格式化类
	 * @return String
	 * @author tianc
	 */
	public static String getDateString(Date date, DateTimeFormatter formatter) {
		LocalDateTime localDt = toLocalDateTime(date);
		return formatter.format(localDt);
	}

	/**
	 * 获得指定日期上个月第一天的日期</br>
	 * 
	 * @param date Date日期类型
	 * @return String
	 * @author tianc
	 */
	public static String getLastMonthFirstDay(Date date) {
		DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String str = "";
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);// 设置时间
		calendar.add(Calendar.MONTH, -1);// 加/减一个月,正数往后推,负数往前移动
		calendar.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		str = sdf.format(toLocalDateTime(calendar.getTime()));


		return str;
	}

	/**
	 * 取本月第1天,返回字符串</br>
	 * 
	 * @param today 字符串型日期
	 * @return String
	 * @author tianc
	 */
	public static String getFirstDayString(String today) {
		DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(today, sdf);
		// 取本月第1天
		LocalDate firstDayOfThisMonth = date.with(TemporalAdjusters.firstDayOfMonth()); // 2017-03-01
		return sdf.format(firstDayOfThisMonth);
	}

	/**
	 *  取本月最后一天，返回字符串</br>
	 * 
	 * @param today 字符串型日期
	 * @return String
	 * @author tianc
	 */
	public static String getLastDayString(String today) {
		DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(today, sdf);
		//  取本月最后一天
		LocalDate firstDayOfThisMonth = date.with(TemporalAdjusters.lastDayOfMonth()); // 2017-03-01
		return sdf.format(firstDayOfThisMonth);
	}

	/**
	 * 获取本月第几周的周六</br>
	 * 
	 * @param firstDayOfMonth   Date日期类型
	 * @param nthWeekendInMonth 第几周
	 * @return Date
	 * @author tianc
	 */
	public static Date getNthWeekendOfMonth(Date firstDayOfMonth, int nthWeekendInMonth) {
		int iCount = 0;
		while (iCount < nthWeekendInMonth) {
			if (!isBusinessDay(firstDayOfMonth)) {
				iCount++;
			}
			firstDayOfMonth = addDay(firstDayOfMonth, 1);
		}

		return addDay(firstDayOfMonth, -1);
	}

	/**
	 * 获取本月最后一周的周六</br>
	 * 
	 * @param firstDayOfMonth Date日期类型
	 * @return Date
	 * @author tianc
	 */
	public static Date getTheLastWeekendOfMonth(Date firstDayOfMonth) {
		Date result = new Date();

		Date lastDayOfMonth = getTheLastDayOfMonth(firstDayOfMonth);
		for (int i = 1; i <= 7; i++) {
			if (!isBusinessDay(lastDayOfMonth)) {
				result = lastDayOfMonth;
				break;
			}

			lastDayOfMonth = addDay(lastDayOfMonth, -1);
		}

		return result;
	}

	/**
	 * 获取本月第几周的周一</br>
	 * 
	 * @param firstDayOfMonth   Date日期类型
	 * @param nthWeekdayInMonth 第几周
	 * @return Date
	 * @author tianc
	 */
	public static Date getNthWeekdayOfMonth(Date firstDayOfMonth, int nthWeekdayInMonth) {
		int iCount = 0;
		while (iCount < nthWeekdayInMonth) {
			if (isBusinessDay(firstDayOfMonth)) {
				iCount++;
			}
			firstDayOfMonth = addDay(firstDayOfMonth, 1);
		}

		return addDay(firstDayOfMonth, -1);
	}

	/**
	 * 获取本月最后一周的周一</br>
	 * 
	 * @param firstDayOfMonth Date日期类型
	 * @return Date
	 * @author tianc
	 */
	public static Date getTheLastWeekdayOfMonth(Date firstDayOfMonth) {
		Date result = new Date();

		Date lastDayOfMonth = getTheLastDayOfMonth(firstDayOfMonth);
		for (int i = 1; i <= 7; i++) {
			if (isBusinessDay(lastDayOfMonth)) {
				result = lastDayOfMonth;
				break;
			}

			lastDayOfMonth = addDay(lastDayOfMonth, -1);
		}

		return result;
	}

	/**
	 * 获取星期几</br>
	 * 
	 * @param date Date日期类型
	 * @return int
	 * @author tianc
	 */
	public static int getDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取本月第几周的第几个工作日</br>
	 * 
	 * @param firstDayOfMonth Date日期类型
	 * @param nthWeekInMonth  第几周
	 * @param weekday         第几个工作日
	 * @return Date
	 * @author tianc
	 */
	public static Date getNthWeekOfMonth(Date firstDayOfMonth, int nthWeekInMonth, int weekday) {
		int iCount = 0;
		while (iCount < nthWeekInMonth) {
			if (getDayOfWeek(firstDayOfMonth) == weekday) {
				iCount++;
			}
			firstDayOfMonth = addDay(firstDayOfMonth, 1);
		}

		return addDay(firstDayOfMonth, -1);
	}

	/**
	 * 获取本月最后一周的第几个工作日</br>
	 * 
	 * @param firstDayOfMonth Date日期类型
	 * @param weekday         第几个工作日
	 * @return Date
	 * @author tianc
	 */
	public static Date getTheLastWeekOfMonth(Date firstDayOfMonth, int weekday) {
		Date result = new Date();
		Date lastDayOfMonth = getTheLastDayOfMonth(firstDayOfMonth);

		for (int i = 1; i <= 7; i++) {
			if (getDayOfWeek(lastDayOfMonth) == weekday) {
				result = lastDayOfMonth;
				break;
			}

			lastDayOfMonth = addDay(lastDayOfMonth, -1);
		}

		return result;
	}

	/**
	 * 获取本月的第几天</br>
	 * 
	 * @param firstDayOfMonth Date日期类型
	 * @param nthDayInMonth   第几天
	 * @return Date
	 * @author tianc
	 */
	public static Date getNthDayOfMonth(Date firstDayOfMonth, int nthDayInMonth) {
		Date result = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(firstDayOfMonth);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
		calendar.add(Calendar.DATE, -1);
		int lastDay = calendar.get(Calendar.DAY_OF_MONTH);

		if (nthDayInMonth <= lastDay) {
			result = calendar.getTime();
		}

		return result;
	}

	/**
	 * 获取本周的第1天</br>
	 * 
	 * @param date Date日期类型
	 * @return Date
	 * @author tianc
	 */
	public static Date getTheFirstDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int weekday = calendar.get(Calendar.DAY_OF_WEEK);
		return addDay(date, weekday * -1);
	}

	/**
	 * 获取本月的第一天</br>
	 * 
	 * @param date Date日期类型
	 * @return Date
	 * @author tianc
	 */
	public static Date getTheFirstDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
		return calendar.getTime();
	}

	/**
	 * 获取本月的最后一天</br>
	 * 
	 * @param date Date日期类型
	 * @return Date
	 * @author tianc
	 */
	public static Date getTheLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	/**
	 * 获取本年的第一天</br>
	 * 
	 * @param date Date日期类型
	 * @return Date
	 * @author tianc
	 */
	public static Date getTheFirstDayOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), 1, 1);
		return calendar.getTime();
	}

	/**
	 * 获取本天的零点时刻</br>
	 * 
	 * @param date Date日期类型
	 * @return Date
	 * @author tianc
	 */
	public static Date getZeroHourOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
		return calendar.getTime();
	}

	/**
	 * 获得指定日期上个月最后一天的日期</br>
	 * 
	 * @param date Date日期类型
	 * @return Date
	 * @author tianc
	 */
	public static Date getLastMonthLastDayString(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);// 加/减一个月,正数往后推,负数往前移动
		cal.setTime(cal.getTime());
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);// 取得该月份最后一天
		cal.set(Calendar.DAY_OF_MONTH, lastDay);// 设置成该月份的最后一天
		return cal.getTime();
	}

	/**
	 * 获取下一天的零点时刻</br>
	 * 
	 * @param date Date日期类型
	 * @return Date
	 * @author tianc
	 */
	public static Date getZeroHourOfTheNextDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE) + 1, 0, 0,
				0);
		return calendar.getTime();
	}

	/**
	 * 是否为哪几周</br>
	 * 
	 * @param startDate           Date日期类型
	 * @param weeklyCycleWeekdays 哪几周
	 * @return boolean
	 * @author tianc
	 */
	public static boolean isWeeklyCycleWeekdays(Date startDate, List<Integer> weeklyCycleWeekdays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return weeklyCycleWeekdays.contains(dayOfWeek);
	}

	/**
	 * 是否为工作日</br>
	 * 
	 * @param startDate Date日期类型
	 * @return boolean
	 * @author tianc
	 */
	public static boolean isBusinessDay(Date startDate) {
		Instant instant = startDate.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDate localDate = instant.atZone(zoneId).toLocalDate();
		return localDate.getDayOfWeek() != DayOfWeek.SATURDAY || localDate.getDayOfWeek() != DayOfWeek.SUNDAY;
	}

	/**
	 * 获取基于0001/1/1至今的纳秒数（与Net时间函数的Ticks相对应）</br>
	 * 
	 * @param date Date日期类型
	 * @return long
	 * @author tianc
	 */
	public static long getTicks(Date date) {
		TimeZone utc = TimeZone.getTimeZone("UTC");
		Calendar calendar = Calendar.getInstance(utc);
		calendar.clear();
		calendar.setTime(date);
		long milli = calendar.getTimeInMillis();

		long ticks = (milli * 10000) + 621355968000000000L;
		return ticks;
	}
	
	public static long getUtcNowTicks() {
		Date date = new Date();
		return getTicks(date);
	}

	/**
	 * 基于系统服务器时区的LocalDateTime</br>
	 * 
	 * @param date Date日期类型
	 * @return LocalDateTime
	 * @author tianc
	 */
	public static LocalDateTime dateToLocalDateTime(Date date) {
		Instant instant = date.toInstant();
		return instant.atZone(ZONE_LOCAL).toLocalDateTime();
	}

	/**
	 * 获取已经过去的时间的描述（与本地时间为基准）</br>
	 * 
	 * @param date Date日期类型
	 * @return boolean
	 * @author tianc
	 */
	public static String toRelativeDateString(Date date) {
		LocalDate localNow = LocalDate.now();
		Date now = Date.from(localNow.atStartOfDay(ZONE_LOCAL).toInstant());
		return getRelativeDateValue(date, now);
	}

	/**
	 * 获取已经过去的时间的描述（与UTC时间为基准）</br>
	 * 例如：on 2010，2 days ago，yesterday，15 hours ago，a few minutes ago
	 * 
	 * @param date Date日期类型
	 * @return boolean
	 * @author tianc
	 */
	public static String toRelativeDateStringUtc(Date date) {
		LocalDate localNow = LocalDate.now();
		Date now = Date.from(localNow.atStartOfDay(ZONE_UTC).toInstant());
		return getRelativeDateValue(date, now);
	}

	private static String getRelativeDateValue(Date date, Date comparedTo) {
		LocalDateTime startDate = dateToLocalDateTime(date);
		LocalDateTime endDate = dateToLocalDateTime(comparedTo);

		long diffInDays = ChronoUnit.DAYS.between(startDate, endDate);
		long diffInHours = ChronoUnit.HOURS.between(startDate, endDate);
		long diffInMinutes = ChronoUnit.MINUTES.between(startDate, endDate);

		if (diffInDays >= 365) {
			DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM d, yyyy");
			return "on " + startDate.format(format);
		}
		if (diffInDays >= 7) {
			DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM d");
			return "on " + startDate.format(format);
		} else if (diffInDays > 1)
			return String.format("{0:N0} days ago", diffInDays);
		else if (diffInDays == 1)
			return "yesterday";
		else if (diffInHours >= 2)
			return String.format("{0:N0} hours ago", diffInHours);
		else if (diffInMinutes >= 60)
			return "more than an hour ago";
		else if (diffInMinutes >= 5)
			return String.format("{0:N0} minutes ago", diffInMinutes);
		if (diffInMinutes >= 1)
			return "a few minutes ago";
		else
			return "less than a minute ago";
	}

	/**
	 * 先转换为北京时间(UTC+08:00)，再ToString</br>
	 * 
	 * @param date   Date日期类型
	 * @param format Date日期格式
	 * @return boolean
	 * @author tianc
	 */
	public static String toLocalDateStr(Date date, String format) {
		ZoneId defaultZoneId = ZoneId.of("Asia/Chita");
		Instant instant = date.toInstant();
		LocalDateTime localNow = instant.atZone(defaultZoneId).toLocalDateTime();

		DateTimeFormatter f = DateTimeFormatter.ofPattern(format);
		return f.format(localNow);
	}

	/**
	 * 获取两个时间之间的所有日期（yyyy-MM-dd格式的字符串列表）
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public static List<Date> findAllDaysInDateDiff(Date startDate, Date endDate){
		List<Date> betweenTime = new ArrayList<Date>();
		try
		{
			Calendar sCalendar = Calendar.getInstance();
			sCalendar.setTime(startDate);
			int year = sCalendar.get(Calendar.YEAR);
			int month = sCalendar.get(Calendar.MONTH);
			int day = sCalendar.get(Calendar.DATE);
			sCalendar.set(year, month, day, 0, 0, 0);

			Calendar eCalendar = Calendar.getInstance();
			eCalendar.setTime(endDate);
			year = eCalendar.get(Calendar.YEAR);
			month = eCalendar.get(Calendar.MONTH);
			day = eCalendar.get(Calendar.DATE);
			eCalendar.set(year, month, day, 0, 0, 0);

			while (sCalendar.before(eCalendar))
			{
				betweenTime.add(sCalendar.getTime());
				sCalendar.add(Calendar.DAY_OF_YEAR, 1);
			}
			betweenTime.add(eCalendar.getTime());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return betweenTime;
	}

	/**
	 * 获取两个时间之间的所有日期（yyyy-MM-dd格式的字符串列表）
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public static List<String> findAllDayStringsInDateDiff(Date startDate, Date endDate){
		List<String> betweenTime = new ArrayList<String>();
		if (startDate == null || endDate == null) return betweenTime;

		try
		{
			SimpleDateFormat outformat = new SimpleDateFormat("yyyy-MM-dd");

			Calendar sCalendar = Calendar.getInstance();
			sCalendar.setTime(startDate);
			int year = sCalendar.get(Calendar.YEAR);
			int month = sCalendar.get(Calendar.MONTH);
			int day = sCalendar.get(Calendar.DATE);
			sCalendar.set(year, month, day, 0, 0, 0);

			Calendar eCalendar = Calendar.getInstance();
			eCalendar.setTime(endDate);
			year = eCalendar.get(Calendar.YEAR);
			month = eCalendar.get(Calendar.MONTH);
			day = eCalendar.get(Calendar.DATE);
			eCalendar.set(year, month, day, 0, 0, 0);

			while (sCalendar.before(eCalendar))
			{
				betweenTime.add(outformat.format(sCalendar.getTime()));
				sCalendar.add(Calendar.DAY_OF_YEAR, 1);
			}
			betweenTime.add(outformat.format(eCalendar.getTime()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return betweenTime;
	}

}
