package com.xchat.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by liuguofu on 2017/10/2.
 */
public class DateUtil {

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

    public static Date addDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    public static Date setTime(Date date, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 设置时间
     *
     * @param date   时间
     * @param hour   时 - 24小时制
     * @param minute 分
     * @param second 秒
     * @return
     */
    public static Date setTimeHourOfDay(Date date, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date str2Date(String str, DateFormat format) throws ParseException {
        if (format == null) {
            throw new NullPointerException("format is null");
        }
        return format.getFormat().parse(str);
    }

    public static String date2Str(Date date, DateFormat format) {
        return format.getFormat().format(date);
    }

    public enum DateFormat {
        YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),

        YYYY_MM_DD("yyyy-MM-dd");

        private SimpleDateFormat format;

        DateFormat(String format) {
            this.format = new SimpleDateFormat(format);
            this.format.setLenient(false);
        }

        public SimpleDateFormat getFormat() {
            return format;
        }
    }


    /**
     * 判断是否在某个时间段内(年月日时分秒)
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static boolean betweenStrToDate(String startTime, String endTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startTime, dateTimeFormatter);
        LocalDateTime end = LocalDateTime.parse(endTime, dateTimeFormatter);
        LocalDateTime now = LocalDateTime.now();
        System.out.println(start);
        System.out.println(end);
        return start.isBefore(now) && end.isAfter(now);
    }

    /**
     * 判断是否在某个时间段内(24小时制)
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static boolean between(String startTime, String endTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalTime start = LocalTime.parse(startTime, dateTimeFormatter);
        LocalTime end = LocalTime.parse(endTime, dateTimeFormatter);
        LocalTime now = LocalTime.now();
        return start.isBefore(now) && end.isAfter(now);
    }



    /**
          * 判断时间是否在时间段内
          * @param nowTime
          * @param beginTime
          * @param endTime
          * @return
          */
    public static boolean belongCalendar(Date nowTime,Date beginTime, Date endTime) {
        //设置当前时间
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        //设置开始时间
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        //设置结束时间
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        //处于开始时间之后，和结束时间之前的判断
        return date.after(begin) && date.before(end);
    }

    public static boolean betweenDate(String startTime,String endTime)throws Exception{
        return belongCalendar(new Date(),str2Date(startTime,DateFormat.YYYY_MM_DD_HH_MM_SS),str2Date(endTime,DateFormat.YYYY_MM_DD_HH_MM_SS));
    }

    public static void main(String[] args) throws Exception{
        String startTime = "2019-05-12 10:50:33";
        String endTime = "2019-05-13 10:59:33";
//        System.out.println(between(startTime,endTime));
//        System.out.println(betweenDate(startTime,endTime));
        System.out.println(betweenStrToDate(startTime,endTime));


    }

}
