package com.juxiao.xchat.base.utils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

import javax.sound.midi.Soundbank;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 日期处理工具类
 *
 * @class: DateUtils
 * @author: chenjunsheng
 * @date 2018年4月27日
 */
public final class DateUtils {


    /**
     * 获取当前星期一的日期，时间部分为0时0分0秒
     *
     * @return
     * @author: chenjunsheng
     * @date 2018/6/5
     */
    public static Date getCurrentMonday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        // 获得当前日期是一个星期的第几天
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        if (1 == weekday) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当前星期天的日期
     *
     * @param hour
     * @param minute
     * @param second
     * @return
     * @author: chenjunsheng
     * @date 2018/6/5
     */
    public static Date getCurrentSundayTime(int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        int weekday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (weekday == 0) {
            weekday = 7;
        }
        calendar.add(Calendar.DATE, 7 - weekday);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTime();
    }

    public static Date getLastMonthStart() {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getLastMonthEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 判断是否是今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        if (date == null) {
            return false;
        }

        String today = DateFormatUtils.YYYYMMDD.date2Str(new Date());
        String thisDate = DateFormatUtils.YYYYMMDD.date2Str(date);
        return today.equals(thisDate);
    }

    public static Date parser(String date, String format) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat(format);
            return parser.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String dateToStr(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    public static Date getLastDay(Date date, int day) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.DATE, - day);
        return cl.getTime();
    }

    /**
     * 转换字符串成日期对象
     *
     * @param dateStr 日期字符串
     * @param format  格式，如：yy-MM-dd HH:mm:ss
     * @return
     */
    public static Date convertStrToDate(String dateStr, String format) {
        if (!StringUtils.isBlank(dateStr) && !StringUtils.isBlank(format)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return sdf.parse(dateStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean inOneMonth(Date date) {
        LocalDateTime lastTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().isAfter(lastTime.plusMonths(1));
    }

    public static boolean betweenDataTime(String start, String end) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = LocalDateTime.parse(start, dateTimeFormatter);
        LocalDateTime endDateTime = LocalDateTime.parse(end, dateTimeFormatter);
        return startDateTime.isBefore(now) && now.isBefore(endDateTime);
    }

    public static int checkPeriodContinue(String start,String end) {
        DateTime submitDate = new DateTime(DateUtils.convertStrToDate(start,"yyyy-MM-dd"));
        DateTime nowDate = new DateTime(DateUtils.convertStrToDate(end,"yyyy-MM-dd"));
        return  Days.daysBetween(submitDate, nowDate).getDays();
    }

    public static int getDutyDays(String strStartDate,String strEndDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate=null;
        Date endDate = null;

        try {
            startDate=df.parse(strStartDate);
            endDate = df.parse(strEndDate);
        } catch (ParseException e) {
            System.out.println("非法的日期格式,无法进行转换");
            e.printStackTrace();
        }
        int result = 0;
        while (startDate.compareTo(endDate) <= 0) {
            if (startDate.getDay() != 6 && startDate.getDay() != 0)
                result++;
            startDate.setDate(startDate.getDate() + 1);
        }

        return result;
    }

    /**
     * 判断是否在某个时间段内(24小时制)
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static boolean between(String startTime, String endTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime start = LocalTime.parse(startTime, dateTimeFormatter);
        LocalTime end = LocalTime.parse(endTime, dateTimeFormatter);
        LocalTime now = LocalTime.now();
        return start.isBefore(now) && end.isAfter(now);
    }

    /**
     * 判断是否在某个时间段内(年月日时分秒)
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return boolean
     */
    public static boolean betweenStrToDate(String startTime, String endTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startTime, dateTimeFormatter);
        LocalDateTime end = LocalDateTime.parse(endTime, dateTimeFormatter);
        LocalDateTime now = LocalDateTime.now();
        return start.isBefore(now) && end.isAfter(now);
    }

    /**
     * 将localDateTime 按照一定的格式转换成String
     * @param localDateTime
     * @param pattern
     * @return
     */
    public static String localDateTimeFormat(LocalDateTime localDateTime,String pattern){
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 将 Date 转换成LocalDateTime
     * atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }



    /**
     * 将date转换成String
     * @param date
     * @param pattern
     * @return
     */
    public static String dateFormat(Date date,String pattern){
        return localDateTimeFormat(dateToLocalDateTime(date),pattern);
    }
    /**
     * 将date 加减天数
     * @param date 日期时间
     * @param num 天数
     * @return
     */
    public static Date duDate(Date date,int num){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE,num);
        return c.getTime();
    }
    /**
     * 将date 加减月数
     * @param date
     * @param num
     * @return
     */
    public static Date duMonth(Date date,int num){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH,num);
        return c.getTime();
    }

    public static Date removeTime(Date date){
        String s = dateFormat(date, "yyyy-MM-dd");
        Date date1 = convertStrToDate(s, "yyy-MM-dd");
/*        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");
        String stringDate = sdf.format(date);
        sdf.parse(stringDate);*/
        return date1;
    }

    public static void main(String[] args) {
        String string = "2019-05-10 23:44:22";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = DateTimeUtils.convertStrToDate(string);
        System.out.println(dateFormat(date,"HH:mm"));
        String str = simpleDateFormat.format(date);
        System.out.println(str);
    }
}
