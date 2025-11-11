package com.xchat.common.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateTimeUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateTimeUtil.class);
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_PATTERN2 = "yyyy-MM-dd";
    private static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

    public static Date getLastDay(Date date, int day) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.DATE, -day);
        return cl.getTime();
    }

    public static Date getNextDay(Date date, int day) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.DATE, day);
        cl.set(Calendar.HOUR_OF_DAY, 0);
        cl.set(Calendar.MINUTE, 0);
        cl.set(Calendar.SECOND, 0);
        return cl.getTime();
    }

    public static Date getNextHour(Date date, int hour) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.set(Calendar.HOUR, cl.get(Calendar.HOUR) + hour);
        return cl.getTime();
    }

    public static Date getDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        return convertStrToDate(dateStr, "yyyy-MM-dd");
    }

    /**
     * 获取当天的字符串
     *
     * @return
     */
    public static String getTodayStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public static String getTodayStr2() {
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        return sdf.format(new Date());
    }

    public static String getCurrentTime() {
        return getCurrentTime(DEFAULT_TIME_PATTERN);
    }


    /**
     * 获取当前时间的字符串
     *
     * @return
     */
    public static String getCurrentDateTime() {
        return getCurrentTime(DEFAULT_DATE_PATTERN);
    }

    /**
     * 获取当前时间的字符串
     *
     * @param format 字符串格式，如：yy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return sdf.format(timestamp);
    }

    /**
     * 获取当前的月份
     *
     * @return
     */
    public static String getCurrentMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(new Date());
    }

    /**
     * 比较两个时间，如果返回大于0，time1大于time2,如果返回-1，time1小于time2，返回0则相等
     *
     * @param time1
     * @param time2
     * @return
     * @throws ParseException
     */
    public static int compareTime(String time1, String time2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        Date date1 = sdf.parse(time1);
        Date date2 = sdf.parse(time2);
        long result = date1.getTime() - date2.getTime();
        if (result > 0) {
            return 1;
        } else if (result == 0) {
            return 0;
        } else {
            return -1;
        }
    }

    public static int compareTime(Date date1, Date date2) {
        long result = date1.getTime() - date2.getTime();
        if (result > 0) {
            return 1;
        } else if (result == 0) {
            return 0;
        } else {
            return -1;
        }
    }

    public static Date convertStrToDate(String dateStr) {
        if (!BlankUtil.isBlank(dateStr) && !BlankUtil.isBlank(DEFAULT_DATE_PATTERN)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
                return sdf.parse(dateStr);
            } catch (Exception e) {
                logger.warn("convertDate fail, date is " + dateStr, e);
            }
        }
        return null;
    }

    /**
     * 转换字符串成日期对象
     *
     * @param dateStr 日期字符串
     * @param format  格式，如：yy-MM-dd HH:mm:ss
     * @return
     */
    public static Date convertStrToDate(String dateStr, String format) {
        if (!BlankUtil.isBlank(dateStr) && !BlankUtil.isBlank(format)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return sdf.parse(dateStr);
            } catch (Exception e) {
                logger.warn("convertDate fail, date is " + dateStr, e);
            }
        }
        return null;
    }

    /**
     * 把日期转换成另一种格式
     *
     * @param date 日期
     * @return
     */
    public static String convertDate(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
            return sdf.format(date);
        } catch (Exception e) {
            logger.warn("convertDate fail, date is " + date, e);
        }
        return null;
    }

    /**
     * 把日期转换成另一种格式
     *
     * @param date   日期
     * @param format 转换日期格式
     * @return
     */
    public static String convertDate(Date date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (Exception e) {
            logger.warn("convertDate fail, date is " + date, e);
        }
        return null;
    }

    /**
     * 把字符串日期转换成另一种格式
     *
     * @param dateStr     字符串日期
     * @param format      转换日期格式
     * @param otherFormat 转换日期格式
     * @return
     */
    public static String convertDate(String dateStr, String format, String otherFormat) {
        try {
            Date date = convertStrToDate(dateStr, format);
            SimpleDateFormat sdf = new SimpleDateFormat(otherFormat);
            return sdf.format(date);
        } catch (Exception e) {
            logger.warn("convertDate fail, date is " + dateStr, e);
        }
        return null;
    }

    /**
     * 把字符串日期转换成另一种格式
     *
     * @param dateStr 字符串日期
     * @param format  转换格式
     * @return
     */
    public static String convertDate(String dateStr, String format) {
        return convertDate(dateStr, DEFAULT_DATE_PATTERN, format);
    }

    public static Date getCurrentMonday(int hour, int minute, int second) {
        Date InputDate = new Date();
        Calendar cDate = Calendar.getInstance();
        cDate.setFirstDayOfWeek(Calendar.MONDAY);
        cDate.setTime(InputDate);
        Calendar firstDate = Calendar.getInstance();
        firstDate.setFirstDayOfWeek(Calendar.MONDAY);
        firstDate.setTime(InputDate);
        if (cDate.get(Calendar.WEEK_OF_YEAR) == 1 && cDate.get(Calendar.MONTH) == 11) {
            firstDate.set(Calendar.YEAR, cDate.get(Calendar.YEAR) + 1);
        }
        int typeNum = cDate.get(Calendar.WEEK_OF_YEAR);

        firstDate.set(Calendar.WEEK_OF_YEAR, typeNum);
        firstDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        //所在周开始日期
        firstDate.set(Calendar.HOUR_OF_DAY, hour);
        firstDate.set(Calendar.MINUTE, minute);
        firstDate.set(Calendar.SECOND, second);
        return firstDate.getTime();

    }

    public static Date getCurrentSunday(int hour, int minute, int second) {
//		Date InputDate = new Date();
//		Calendar cDate = Calendar.getInstance();
//		cDate.setTime(InputDate);
//		Calendar lastDate = Calendar.getInstance();
//		lastDate.setTime(InputDate);
//		if(cDate.get(Calendar.WEEK_OF_YEAR)==1&&cDate.get(Calendar.MONTH)==11){
//			lastDate.set(Calendar.YEAR, cDate.get(Calendar.YEAR)+1);
//		}
//		int typeNum  = cDate.get(Calendar.WEEK_OF_YEAR);
//		//所在周开始日期
//		lastDate.set(Calendar.WEEK_OF_YEAR, typeNum);
//		lastDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
//		lastDate.set(Calendar.HOUR_OF_DAY,hour);
//		lastDate.set(Calendar.MINUTE,minute);
//		lastDate.set(Calendar.SECOND,second);
        Calendar lastDate = Calendar.getInstance();
        int day_of_week = lastDate.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        lastDate.add(Calendar.DATE, -day_of_week + 7);
        lastDate.set(Calendar.HOUR_OF_DAY, hour);
        lastDate.set(Calendar.MINUTE, minute);
        lastDate.set(Calendar.SECOND, second);
        return lastDate.getTime();

    }

    public static boolean checkIsToday(Date date) {
        if (date == null) {
            return false;
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        if (fmt.format(date).toString().equals(fmt.format(new Date()).toString())) {//格式化为相同格式
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断某个时间是否在指定时间区间内
     *
     * @param curTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean isBetweenDate(Date curTime, Date beginTime, Date endTime) {
        if (curTime == null || beginTime == null || endTime == null) {
            return false;
        }
        if (curTime.after(beginTime) && curTime.before(endTime)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        Date lastDate = getLastDay(new Date(), -1);
        System.out.println(lastDate);
//		System.out.println(convertDate("2015-11-10 20:33", "yy年MM月dd日 hh时"));
//		System.out.println(getCurrentMonth());
//		dateTest();
        System.out.println(getNextHour(new Date(), 2));
        System.out.println(getNextHour(new Date(), 13));
        System.out.println(getNextHour(new Date(), 16));
    }

    /**
     * 设置当天结束时间
     *
     * @param dateStr
     */
    public static Date formatEndDate(String dateStr) {
        Date date = DateTimeUtil.convertStrToDate(dateStr, DateTimeUtil.DEFAULT_DATE_PATTERN2);
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            return cal.getTime();
        }
        return null;
    }

    /**
     * 设置当天开始时间
     *
     * @param dateStr
     * @return
     */
    public static Date formatBeginDate(String dateStr) {
        Date date = DateTimeUtil.convertStrToDate(dateStr, DateTimeUtil.DEFAULT_DATE_PATTERN2);
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            return cal.getTime();
        }
        return null;
    }

    /**
     * 获取时间的小时
     *
     * @param date
     * @return
     */
    public static int getHour(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);//获取年份
        int month = cal.get(Calendar.MONTH);//获取月份
        int day = cal.get(Calendar.DATE);//获取日
        int hour = cal.get(Calendar.HOUR_OF_DAY);//小时
        int minute = cal.get(Calendar.MINUTE);//分
        int second = cal.get(Calendar.SECOND);//秒
        int WeekOfYear = cal.get(Calendar.DAY_OF_WEEK);//一周的第几天
        return hour;
    }

    public static long getDateMillis(Date date, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        return cal.getTimeInMillis();
    }

    public static Date formatBeginDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static Date formatEndDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    public static Date getLastMonthStart() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static Date getLastMonthEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    public static Date addSecond(Date date, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    public static String date2Str(Date date, DateUtil.DateFormat format) {
        return format.getFormat().format(date);
    }
}
