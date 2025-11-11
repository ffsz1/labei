package com.tongdaxing.xchat_core.utils;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: StringUtils
 * @Description: 字符串操作工具包
 * @Author zengweijie
 * @date 2013-8-6 下午1:51:14
 ************************************************************************** 
 */
public class StringUtils {
	private final static Pattern emailer = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	/**
	 * 将字符串转位日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 以友好的方式显示时间
	 * 
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}

	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input) || "null".equals(input)
				|| "-1".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	public static boolean isEmpty(Object arg0) {
		if (arg0 == null)
			return true;
		return false;
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 字符串转布尔值
	 * 
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception e) {
		}
		return false;
	}

	public static String getURLEncoder(String eStr) {
		String str = "";
		try {
			str = URLEncoder.encode(eStr, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * is null or its length is 0 or it is made by space
	 * 
	 * <pre>
	 * isBlank(null) = true;
	 * isBlank(&quot;&quot;) = true;
	 * isBlank(&quot;  &quot;) = true;
	 * isBlank(&quot;a&quot;) = false;
	 * isBlank(&quot;a &quot;) = false;
	 * isBlank(&quot; a&quot;) = false;
	 * isBlank(&quot;a b&quot;) = false;
	 * </pre>
	 * 
	 * @param str
	 * @return if string is null or its size is 0 or it is made by space, return
	 *         true, else return false.
	 */
	public static boolean isBlank(String str) {
		return (str == null || str.trim().length() == 0);
	}

	/**
	 * 技能人周期转换 描述这个方法的作用
	 * 
	 * @param arg0
	 * @return
	 * @Exception 异常对象
	 */
	public static String formapOrderDateForDay(String arg0) {
		String temp = "";

		if (arg0 == null) {
			return temp;
		}

		String[] tempArr = arg0.split(",");

		if (tempArr.length == 0) {
			return temp;
		}

		for (String string : tempArr) {
			String _tempName = "";
			int tempIndex = Integer.parseInt(string);
			switch (tempIndex) {
			case 1:
				_tempName = "周一";
				break;
			case 2:
				_tempName = "周二";
				break;
			case 3:
				_tempName = "周三";
				break;
			case 4:
				_tempName = "周四";
				break;
			case 5:
				_tempName = "周五";
				break;
			case 6:
				_tempName = "周六";
				break;
			case 7:
				_tempName = "周日";
				break;

			default:
				break;
			}
			temp = temp + _tempName + "、";
		}

		return temp.substring(0, temp.lastIndexOf("、"));
	}

	public static String formapOrderDateForDayTag(String arg0) {
		String temp = "";

		if (arg0 == null) {
			return temp;
		}

		String[] tempArr = arg0.split(",");

		if (tempArr.length == 0) {
			return temp;
		}

		for (String string : tempArr) {
			temp = temp + string + ",";
		}

		return temp.substring(0, temp.lastIndexOf(","));
	}

	/**
	 * 
	 * @Title: splicing
	 * @Description: 字符串拼接
	 * @param @param str
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String splic(String... str) {
		String sc = "";
		if (str.length > 0) {
			StringBuffer bf = new StringBuffer();
			for (String item : str) {
				bf.append(item);
			}
			sc = bf.toString();
		}
		return sc;
	}

	/**
	 * 删除最后字符
	 * 
	 * @param @param str
	 * @return String 返回类型
	 */
	public static String delLastStr(String str) {
		if (StringUtils.isBlank(str)) {
			return "";
		}
		String tempStr = str.substring(0, str.length() - 1);
		return tempStr;
	}

	// /** * 判断字符串是否是整数 */
	// public static boolean isInteger(String value) {
	// try {
	// Integer.parseInt(value);
	// return true;
	// } catch (NumberFormatException e) {
	// return false;
	// }
	// }
	//
	// /** * 判断字符串是否是浮点数 */
	// public static boolean isDouble(String value) {
	// try {
	// Double.parseDouble(value);
	// if (value.contains("."))
	// return true;
	// return false;
	// } catch (NumberFormatException e) {
	// return false;
	// }
	// }
	//
	// /** * 判断字符串是否是数字 */
	// public static boolean isNumber(String value) {
	// return isInteger(value) || isDouble(value);
	// }

	// 验证字符串中包含数字，很简单。
	public static boolean isNumber(String str) {
		boolean isNumber = true;
		char[] ch = str.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			isNumber = Character.isDigit(ch[i]);
			if (!isNumber)
				return false;
		}
		return true;
	}
}
