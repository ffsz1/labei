package com.xchat.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理工具类
 *
 * @class: DateTimeUtils.java
 * @author: chenjunsheng
 * @date 2018/6/5
 */
public final class DateTimeUtils {

    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_PATTERN2 = "yyyy-MM-dd";

    /**
     * 设置Date对象时间
     *
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     * @author: chenjunsheng
     * @date 2018/6/5
     */
    public static Date setTime(Date date, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date setTime(Date date, int hour, int minute, int second, int millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        return calendar.getTime();
    }

    /**
     * 在某个日期的基础上增加天
     *
     * @param date
     * @param day
     * @return
     */
    public static Date addDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 在某个日期基础上增加小时
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date addHours(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }

    public static Date addMinute(Date date, int minute) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.MINUTE, minute);
        return cl.getTime();
    }

    /**
     * 在某个日期基础上增加秒数
     *
     * @param date
     * @param second
     * @return
     */
    public static Date addSecond(Date date, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    public static Date getLastDay(Date date, int day) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.DATE, -day);
        return cl.getTime();
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

    public static Date convertStrToDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date convertStrToDate(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置当天开始时间
     *
     * @param dateStr
     * @return
     */
    public static Date formatBeginDate(String dateStr) {
        Date date = convertStrToDate(dateStr, DEFAULT_DATE_PATTERN2);
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
     * 设置当天结束时间
     *
     * @param dateStr
     */
    public static Date formatEndDate(String dateStr) {
        Date date = convertStrToDate(dateStr, DEFAULT_DATE_PATTERN2);
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

    public static String convertDate(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
            return sdf.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String convertDate(Date date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (Exception e) {
            return null;
        }
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

    /**
     * 根据当前时间判断是否是上周
     *
     * @param time
     * @return
     */
    public static boolean isLastWeek(Date time) {
        //得到日历
        Calendar calendar = Calendar.getInstance();
        //把当前时间赋值给日历
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date beforeDays = calendar.getTime();
        if (beforeDays.getTime() >= time.getTime()) {
            return true;
        } else {
            return false;
        }
    }

}
