package com.tongdaxing.xchat_framework.util.util;

import android.content.Context;

import com.tongdaxing.xchat_framework.R;
import com.tongdaxing.xchat_framework.util.util.log.MLog;
import com.tongdaxing.xchat_framework.util.util.valid.BlankUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.IllegalFormatException;
import java.util.Locale;

/**
 * Created by Zhanghuiping on 14/6/5.
 */
public class TimeUtils {

    public static final String RULE_1 = "yyyy-MM-dd";
    public static final String RULE_2 = "yyyyMMdd";

    public static final long MONTHS_OF_YEAR = 12;
    public static final long DAYS_OF_YEAR = 365;
    public static final long DAYS_OF_MONTH = 30;
    public static final long HOURS_OF_DAY = 24;
    public static final long MINUTES_OF_HOUR = 60;
    public static final long SECONDS_OF_MINUTE = 60;
    public static final long MILLIS_OF_SECOND = 1000;

    public static boolean isSameDay(long millis1, long millis2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(millis1);

        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(millis2);

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
    }

    public static int isYesterday(String date) {
        int day = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = new Date();//当前时间
            Date d2 = sdf.parse(date);//传进的时间
            long cha = d2.getTime() - d1.getTime();
            day = Long.valueOf(cha / (1000 * 60 * 60 * 24)).intValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return day;

    }

    public static int getYear(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return c.get(Calendar.YEAR);
    }

    public static int getMonth(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return c.get(Calendar.MONTH) + 1;
    }

    public static int getDayOfMonth(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDayOfWeek(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * @param millis time in millis
     * @return hour of 12-hour clock
     */
    public static int getHourOf12HClock(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return c.get(Calendar.HOUR);
    }

    /**
     * @param millis time in millis
     * @return hour of 24-hour clock
     */
    public static int getHourOf24HClock(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return c.get(Calendar.MINUTE);
    }

    public static int getSecond(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return c.get(Calendar.SECOND);
    }

    public static long nextDay(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.add(Calendar.DATE, 1);  // number of days to add
        return c.getTimeInMillis();
    }

    public static long previousDay(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.add(Calendar.DATE, -1);  // number of days to add
        return c.getTimeInMillis();
    }

    public static final long MILLIS_OF_SEC = 1000;

    /**
     * Get current time in secs.
     *
     * @return Current time in seconds.
     */
    public static int curSec() {
        long l = System.currentTimeMillis();
        return (int) (l / MILLIS_OF_SEC);
    }

    /**
     * Get expired dead time, in millis.
     *
     * @param millisToExpire millis from now to the dead time.
     * @return Expired dead time, in millis, it is in UTC(GMT).
     */
    public static long getExpireDeadTime(long millisToExpire) {
        return System.currentTimeMillis() + millisToExpire;
    }

    /**
     * @param timeMillis time in millis.
     * @return format time string , like "2014-06-06 16:19:15"
     */
    public static String getTimeStringFromMillis(long timeMillis) {
        String format = "%04d-%02d-%02d %02d:%02d:%2d";
        return getTimeStringFromMillis(timeMillis, format);
    }

    /**
     * @param timeMillis time in millis
     * @param format     format string, must contain 6 args: year, month, day, hour, minute, second
     * @return format time string
     */
    public static String getTimeStringFromMillis(long timeMillis, String format) {
        if (format == null || format.length() == 0) {
            return null;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeMillis);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        String timeString = null;
        try {
            timeString = String.format(format, year, month, day, hour, min, sec);
        } catch (IllegalFormatException e) {
            MLog.error("TimeUtils", "getTimeStringFromMillis error! " + e.toString());
        }
        return timeString;
    }

    //毫秒值转换成年月日
    public static String getDateTimeString(long milliseconds, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new Date(milliseconds);
        return formatter.format(date);
    }


    public static String stampToDate(long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        long lt = s;
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将毫秒时间转换为指定的字符串格式,例如传入format“year-mon-day”,返回类似“2014-08-29”
     *
     * @param timeMillis
     * @param format     year,mon,day,hour,min,sec
     * @return 指定时间格式
     */
    public static String getFormatTimeString(long timeMillis, String format) {
        if (format == null || format.length() == 0) {
            return null;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeMillis);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        String timeString = null;
        try {
//            timeString = String.format(format, year, month, day, hour, min, sec);
            timeString = format.replaceAll("year", String.valueOf(year))
                    .replaceAll("mon", month < 10 ? "0" + month : "" + month)
                    .replaceAll("day", day < 10 ? "0" + day : "" + day)
                    .replaceAll("hour", hour < 10 ? "0" + hour : "" + hour)
                    .replaceAll("min", min < 10 ? "0" + min : "" + min)
                    .replaceAll("sec", sec < 10 ? "0" + sec : "" + sec);
        } catch (Exception e) {
            MLog.error("TimeUtils", "getFormatTimeString error! " + e.toString());
        }
        return timeString;
    }

    public static String getPostTimeString(Context context, long timeMillis, boolean showToday, boolean showSecond) {
        if (context == null) {
            return null;
        }
        String sSpace = " ";
        String sToday = context.getString(R.string.str_today);
        String sRightNow = context.getString(R.string.str_right_now);
        String sYesterday = context.getString(R.string.str_yesterday);
        String sBeforeYesterday = context.getString(R.string.str_day_before_yesterday);
        String sShortDateFormat = context.getString(R.string.str_short_date_format);
        String sDateFormat = context.getString(R.string.str_date_format);
        String sHourAgo = context.getString(R.string.str_hours_ago_format);
        String sMinutAgo = context.getString(R.string.str_minutes_ago_format);

        Calendar current = Calendar.getInstance();
        Calendar post = Calendar.getInstance();

        current.setTimeInMillis(System.currentTimeMillis());
        post.setTimeInMillis(timeMillis);

        int diffSecs = (int) ((current.getTimeInMillis() - post.getTimeInMillis()) / MILLIS_OF_SECOND);
        if (diffSecs <= 0) {
            return sRightNow;
        }
        int diffHours = (int) (diffSecs / SECONDS_OF_MINUTE / MINUTES_OF_HOUR);
        int diffDays = current.get(Calendar.DAY_OF_YEAR) - post.get(Calendar.DAY_OF_YEAR);

        boolean isSameYear = post.get(Calendar.YEAR) == current.get(Calendar.YEAR);
        boolean isSameDay = diffHours <= HOURS_OF_DAY;

        StringBuilder builder = new StringBuilder();
        if (!isSameDay || !isSameYear) {
            if (diffDays > 0 && diffDays <= 2) {
                builder.append(diffDays == 1 ? sYesterday : sBeforeYesterday);
                builder.append(sSpace);
            } else {
                if (isSameYear) {
                    builder.append(String.format(sShortDateFormat,
                            post.get(Calendar.MONTH) + 1,
                            post.get(Calendar.DAY_OF_MONTH)));
                    builder.append(sSpace);
                } else {
                    builder.append(String.format(sDateFormat,
                            post.get(Calendar.YEAR),
                            post.get(Calendar.MONTH) + 1,
                            post.get(Calendar.DAY_OF_MONTH)));
                    builder.append(sSpace);
                }
            }
        } else if (showToday) {

            if (diffHours > 0) {
                builder.append(String.format(sHourAgo, diffHours));
            } else {
                int diffMinuts = diffSecs / 60;
                if (diffMinuts > 0) {
                    builder.append(String.format(sMinutAgo, diffMinuts));
                } else {
                    builder.append(String.format(sMinutAgo, diffMinuts + 1));
                }
            }
//            builder.append(sToday);
//            builder.append(sSpace);
        }

//        if (showSecond) {
//            builder.append(String.format(context.getString(R.string.str_time_format),
//                    post.get(Calendar.HOUR_OF_DAY), post.get(Calendar.MINUTE), post.get(Calendar.SECOND)));
//        } else {
//            builder.append(String.format(context.getString(R.string.str_short_time_format),
//                    post.get(Calendar.HOUR_OF_DAY), post.get(Calendar.MINUTE)));
//        }
        return builder.toString();
    }

    public static String getProgresstime(long progress) {
        if (progress < 0) progress = 0;
        Date date = new Date(progress);
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        return format.format(date);
    }

    public static String getChineseMonth(Context context, long millis) {
        int month = getMonth(millis);
        String[] monthStrs = context.getResources().getStringArray(R.array.time_month_strs);
        if (!BlankUtil.isBlank(monthStrs)) {
            for (int i = 0; i < monthStrs.length; i++) {
                if ((month - 1) == i) {
                    return monthStrs[i];
                }
            }
        }

        return "";
    }

    /**
     * @param datetime
     * @param rule     "yyyyMMdd"
     * @return
     */
    public static long dateString2Time(String datetime, String rule) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(rule);
        try {
            Date date = dateFormat.parse(datetime);
            MLog.debug(TimeUtils.class, "" + date.getTime());
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            MLog.error(TimeUtils.class, e);
        }

        return 0;
    }

    /**
     * t multiply scale, check overflow
     *
     * @param t     t
     * @param scale scale
     * @return t * scale if not overflow, else return Long.MAX_VALUE or Long.MIN_VALUE
     */
    private static long checkOverflow(long t, long scale) {
        if (t > Long.MAX_VALUE / scale) {
            return Long.MAX_VALUE;
        }
        if (t < Long.MIN_VALUE / scale) {
            return Long.MIN_VALUE;
        }
        return t * scale;
    }

    public static class YEARS {
        public static long toMillis(long years) {
            return checkOverflow(years, DAYS_OF_YEAR * HOURS_OF_DAY * MINUTES_OF_HOUR * SECONDS_OF_MINUTE * MILLIS_OF_SECOND);
        }

        public static long toSeconds(long years) {
            return checkOverflow(years, DAYS_OF_YEAR * HOURS_OF_DAY * MINUTES_OF_HOUR * SECONDS_OF_MINUTE);
        }

        public static long toMinutes(long years) {
            return checkOverflow(years, DAYS_OF_YEAR * HOURS_OF_DAY * MINUTES_OF_HOUR);
        }

        public static long toHours(long years) {
            return checkOverflow(years, DAYS_OF_YEAR * HOURS_OF_DAY);
        }

        public static long toDays(long years) {
            return checkOverflow(years, DAYS_OF_YEAR);
        }

        public static long toMonths(long years) {
            return checkOverflow(years, MONTHS_OF_YEAR);
        }
    }

    public static class MONTHS {
        public static long toMillis(long months) {
            return checkOverflow(months, DAYS_OF_MONTH * HOURS_OF_DAY * MINUTES_OF_HOUR * SECONDS_OF_MINUTE * MILLIS_OF_SECOND);
        }

        public static long toSeconds(long months) {
            return checkOverflow(months, DAYS_OF_MONTH * HOURS_OF_DAY * MINUTES_OF_HOUR * SECONDS_OF_MINUTE);
        }

        public static long toMinutes(long months) {
            return checkOverflow(months, DAYS_OF_MONTH * HOURS_OF_DAY * MINUTES_OF_HOUR);
        }

        public static long toHours(long months) {
            return checkOverflow(months, DAYS_OF_MONTH * HOURS_OF_DAY);
        }

        public static long toDays(long months) {
            return checkOverflow(months, DAYS_OF_MONTH);
        }

        public static long toYears(long months) {
            return months / MONTHS_OF_YEAR;
        }
    }

    public static class DAYS {
        public static long toMillis(long days) {
            return checkOverflow(days, HOURS_OF_DAY * MINUTES_OF_HOUR * SECONDS_OF_MINUTE * MILLIS_OF_SECOND);
        }

        public static long toSeconds(long days) {
            return checkOverflow(days, HOURS_OF_DAY * MINUTES_OF_HOUR * SECONDS_OF_MINUTE);
        }

        public static long toMinutes(long days) {
            return checkOverflow(days, HOURS_OF_DAY * MINUTES_OF_HOUR);
        }

        public static long toHours(long days) {
            return checkOverflow(days, HOURS_OF_DAY);
        }

        public static long toMonths(long days) {
            return days / DAYS_OF_MONTH;
        }

        public static long toYears(long days) {
            return days / DAYS_OF_YEAR;
        }
    }

    public static class HOURS {
        public static long toMillis(long hours) {
            return checkOverflow(hours, MINUTES_OF_HOUR * SECONDS_OF_MINUTE * MILLIS_OF_SECOND);
        }

        public static long toSeconds(long hours) {
            return checkOverflow(hours, MINUTES_OF_HOUR * SECONDS_OF_MINUTE);
        }

        public static long toMinutes(long hours) {
            return checkOverflow(hours, MINUTES_OF_HOUR);
        }

        public static long toDays(long hours) {
            return hours / HOURS_OF_DAY;
        }

        public static long toMonths(long hours) {
            return toDays(hours) / DAYS_OF_MONTH;
        }

        public static long toYears(long hours) {
            return toDays(hours) / DAYS_OF_YEAR;
        }
    }

    public static class MINUTES {
        public static long toMillis(long minutes) {
            return checkOverflow(minutes, SECONDS_OF_MINUTE * MILLIS_OF_SECOND);
        }

        public static long toSeconds(long minutes) {
            return checkOverflow(minutes, SECONDS_OF_MINUTE);
        }

        public static long toHours(long minutes) {
            return minutes / MINUTES_OF_HOUR;
        }

        public static long toDays(long minutes) {
            return toHours(minutes) / HOURS_OF_DAY;
        }

        public static long toMonths(long minutes) {
            return toDays(minutes) / DAYS_OF_MONTH;
        }

        public static long toYears(long minutes) {
            return toDays(minutes) / DAYS_OF_YEAR;
        }
    }

    public static class SECONDS {
        public static long toMillis(long seconds) {
            return checkOverflow(seconds, MILLIS_OF_SECOND);
        }

        public static long toMinutes(long seconds) {
            return seconds / SECONDS_OF_MINUTE;
        }

        public static long toHours(long seconds) {
            return toMinutes(seconds) / MINUTES_OF_HOUR;
        }

        public static long toDays(long seconds) {
            return toHours(seconds) / HOURS_OF_DAY;
        }

        public static long toMonths(long seconds) {
            return toDays(seconds) / DAYS_OF_MONTH;
        }

        public static long toYears(long seconds) {
            return toDays(seconds) / DAYS_OF_YEAR;
        }
    }

    public static class MILLIS {
        public static long toSeconds(long millis) {
            return millis / MILLIS_OF_SECOND;
        }

        public static long toMinutes(long millis) {
            return toSeconds(millis) / SECONDS_OF_MINUTE;
        }

        public static long toHours(long millis) {
            return toMinutes(millis) / MINUTES_OF_HOUR;
        }

        public static long toDays(long millis) {
            return toHours(millis) / HOURS_OF_DAY;
        }

        public static long toMonths(long millis) {
            return toDays(millis) / DAYS_OF_MONTH;
        }

        public static long toYears(long millis) {
            return toDays(millis) / DAYS_OF_YEAR;
        }
    }

    /**
     * 将毫秒转换为年月日时分秒 ,只拿时分秒
     *
     * @author GaoHuanjie
     */
    public static String getYearMonthDayHourMinuteSecond(long timeMillis) {
        int timezone = 8; // 时区
        long totalSeconds = timeMillis / 1000;
        totalSeconds += 60 * 60 * timezone;
        int second = (int) (totalSeconds % 60);// 秒
        long totalMinutes = totalSeconds / 60;
        int minute = (int) (totalMinutes % 60);// 分
        long totalHours = totalMinutes / 60;
        int hour = (int) (totalHours % 24);// 时
        int totalDays = (int) (totalHours / 24);
        int _year = 1970;
        int year = _year + totalDays / 366;
        int month = 1;
        int day = 1;
        int diffDays;
        boolean leapYear;
        while (true) {
            int diff = (year - _year) * 365;
            diff += (year - 1) / 4 - (_year - 1) / 4;
            diff -= ((year - 1) / 100 - (_year - 1) / 100);
            diff += (year - 1) / 400 - (_year - 1) / 400;
            diffDays = totalDays - diff;
            leapYear = (year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0);
            if (!leapYear && diffDays < 365 || leapYear && diffDays < 366) {
                break;
            } else {
                year++;
            }
        }

        int[] monthDays;
        if (diffDays >= 59 && leapYear) {
            monthDays = new int[]{-1, 0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335};
        } else {
            monthDays = new int[]{-1, 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
        }
        for (int i = monthDays.length - 1; i >= 1; i--) {
            if (diffDays >= monthDays[i]) {
                month = i;
                day = diffDays - monthDays[i] + 1;
                break;
            }
        }
        String hours;
        String minutes;
        String seconds;
        if (hour < 10) {
            hours = "0" + hour;
        } else {
            hours = "" + hour;
        }
        if (minute < 10) {
            minutes = "0" + minute;
        } else {
            minutes = "" + minute;
        }
        if (second < 10) {
            seconds = "0" + second;
        } else {
            seconds = "" + second;
        }

//        return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
        return hours + ":" + minutes + ":" + seconds;
    }

    public static Date getTimesnight(int i) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, i);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /*获得指定天数的0点和24点*/
    public static Date getTimesnights(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    //由出生日期获得年龄
    public static int getAge(long birthDay) {
        if (birthDay <=  0)
            return 0;
        int age = 0;
        try {
            Date data = new Date(birthDay);
            Calendar cal = Calendar.getInstance();
            if (data == null) {
                return age;
            }
            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH);
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
            cal.setTime(data);
            int yearBirth = cal.get(Calendar.YEAR);
            int monthBirth = cal.get(Calendar.MONTH);
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
            age = yearNow - yearBirth;
            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    if (dayOfMonthNow < dayOfMonthBirth) age--;
                } else {
                    age--;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return age;
    }
}
