package com.xchat.common.utils;

import java.util.Calendar;
import java.util.Date;

public class GetTimeUtils {
    /*获得指定天数的0点和24点*/
    public static Date getTimesnights(Date time, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.HOUR_OF_DAY, i);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获得当天24点时间
    public static Date getTimesnight(int i) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, i);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    //获得本周一0点时间
    public static Date getTimesWeekmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    // 获得本周日0点时间
    public static Date getTimesWeeknight() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTimesWeekmorning());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        return cal.getTime();
    }

    //获得本周日的0点
    public static Date getTimesPreSunday() {
        Calendar preWeekSundayCal = Calendar.getInstance();
        preWeekSundayCal.set(Calendar.DAY_OF_WEEK, 1);
        preWeekSundayCal.set(Calendar.MINUTE, 0);
        preWeekSundayCal.set(Calendar.SECOND, 0);
        preWeekSundayCal.set(Calendar.HOUR_OF_DAY, 0);
        return preWeekSundayCal.getTime();
    }

    //获得本周一的0点
    public static Date getTimesPreMonday() {
        Calendar preWeekMondayCal = Calendar.getInstance();
        preWeekMondayCal.set(Calendar.DAY_OF_WEEK, 1);
        preWeekMondayCal.set(Calendar.MINUTE, 0);
        preWeekMondayCal.set(Calendar.SECOND, 0);
        preWeekMondayCal.set(Calendar.HOUR_OF_DAY, 0);
        preWeekMondayCal.add(Calendar.DATE, -6);
        return preWeekMondayCal.getTime();
    }

    public static void main(String args[]){
        System.out.println(getTimesWeekmorning());
        System.out.println(getTimesnights(getTimesWeeknight(),24));

        System.out.println(getTimesPreMonday());
        System.out.print(getTimesPreSunday());

    }
}
