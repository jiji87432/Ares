package com.pay.risk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class DateUtils {
	private static final Logger logger = Logger.getLogger(DateUtils.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");

	/**
	 *
	 */
	public static String getyyyyMMdd(Date d) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		return f.format(d);
	}

	public static String getyyyyMM(Date d) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		return f.format(d);
	}

	public static String getDateTime(Date d) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return f.format(d);
	}

	public static String get24DateTime(Date d) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return f.format(d);
	}

	/**
	 * 将日期的时分秒部分变为"0:0:0.000"。通常用于查询条件中开始日期的初始化
	 * @param date
	 *            日期
	 * @return 时分秒部分为"0:0:0.000"的日期
	 */
	public static Date getBeginDate(Date date) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	/**
	 * 将日期的时分秒部分变为"23:59:59.999"。通常用于查询条件中结束日期的初始化
	 * @param date
	 *            日期
	 * @return 时分秒部分为"23:59:59.999"的日期
	 */
	public static Date getEndDate(Date date) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		return calendar.getTime();
	}

	/**
	 * 返回流逝时间的中文描述(比如：x天h小时m分s秒SSS毫秒)
	 * @param elapsedTime
	 *            从开始到结束流逝时间的毫秒数
	 * @return 时间差的中文描述(比如：d天h小时m分s秒SSS毫秒)
	 */
	public static String getChineseTimeConsuming(long elapsedTime) {
		int day = (int) (elapsedTime / 1000 / 60 / 60 / 24);
		int hour = (int) ((elapsedTime % (1000 * 60 * 60 * 24)) / 1000 / 60 / 60);
		int minute = (int) ((elapsedTime % (1000 * 60 * 60 * 24) % (1000 * 60 * 60)) / 1000 / 60);
		int second = (int) ((elapsedTime / 1000) % 60);
		int millisecond = (int) (elapsedTime % 1000);

		StringBuilder sb = new StringBuilder();
		if (elapsedTime > 0) {
			if (day > 0) {
				sb.append(day).append("d ");
			}
			if (hour > 0) {
				sb.append(hour).append("h ");
			}
			if (minute > 0) {
				sb.append(minute).append("mi ");
			}
			if (second > 0) {
				sb.append(second).append("se ");
			}
			if (millisecond > 0) {
				sb.append(millisecond).append("ms");
			}
		} else {
			sb.append("0ms");
		}

		return sb.toString();
	}

	/**
	 * 距日期dateStrFull K天的日期列表
	 * 返回日期列表 t 代表是够包含 dateStrFull 这个日期，Y代表包含
	 */

	private static SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public static Map<String, String> retrunDateStrList(Date date, int k) {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 1; i <= k; i++) {
			Calendar now = Calendar.getInstance();
			try {
				now.setTime(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
			now.set(Calendar.DATE, now.get(Calendar.DATE) - i);
			String _s = sdfFull.format(now.getTime());
			map.put(String.valueOf(i), _s.substring(0, 10).replaceAll("-", ""));
		}
		return map;
	}

	public static boolean compareDate(String dateStr, int num) {
		try {

			try {
				Integer.parseInt(dateStr);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("该日期dateStr有非数字：" + dateStr);
				return false;
			}

			if (dateStr != null) {
				// 校验日期
				Date keyDate = sdf.parse(dateStr);

				Date date = new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				// 将当前时间的时分秒设置成0，防止过滤掉今天的在这之前的用户
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				// 计算几天之前的日期
				cal.add(Calendar.DATE, -num);
				Date date2 = cal.getTime();
				if (keyDate.getTime() < date2.getTime() || keyDate.getTime() > date.getTime()) {
					return true;
				}
			} else {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		return false;
	}

	public static boolean compareDateMonth(String dateStr, int num) {
		logger.info("compareDateMonth 方法。。。");
		try {
			try {
				Integer.parseInt(dateStr);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("该月份有非数字" + dateStr);
				return false;
			}

			if (dateStr != null) {
				// 判断时间区间
				Date fieldDate = sdf2.parse(dateStr);
				Date date = new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				// 将当前时间的时分秒设置成0，防止过滤掉今天的在这之前的用户
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				// 计算几个月之前的日期
				cal.add(Calendar.MONTH, -num);
				Date date2 = cal.getTime();
				if (fieldDate.getTime() < date2.getTime() || fieldDate.getTime() > date.getTime()) {
					return true;
				}

			} else {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		return false;
	}

	/**
	 * @Description 判断参数timeStr日期是不是今天
	 * @param timeStr
	 * @return 不是今天 true 是今天 false
	 * @see 需要参考的类或方法
	 */
	public static boolean compareNoToday(String dateStr) {
		try {

			try {
				Integer.parseInt(dateStr);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("该日期有非数字" + dateStr);
				return false;
			}

			if (dateStr != null) {
				Date fieldDate = sdf.parse(dateStr);

				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				// 将当前日期的时分秒设置成0，防止过滤掉今天的在这之前的用户
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MILLISECOND, 0);
				Date dateNow = c.getTime();

				if (fieldDate.getTime() != dateNow.getTime()) {
					return true;
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static int getExpireSeconds(int s) {
		Date startTime = new Date();
		Integer dseconds = (s - (Integer.valueOf(new SimpleDateFormat("HH").format(startTime)))) * 3600;
		return dseconds;
	}

	/**
	 * 获取今天yyyyMMdd 形式的string
	 * @Description 一句话描述方法用法
	 * @return
	 * @see 需要参考的类或方法
	 */
	public static String getTodayStr() {
		return sdf.format(new Date());
	}

	/**
	 * @Description 获取当天的剩余时间
	 * @return
	 * @see 需要参考的类或方法
	 */
	public static int getSeconds() {
		Calendar curDate = Calendar.getInstance();
		Calendar tommorowDate = new GregorianCalendar(curDate.get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate.get(Calendar.DATE) + 1, 0, 0, 0);
		return (int) (tommorowDate.getTimeInMillis() - curDate.getTimeInMillis()) / 1000;
	}
}
