package com.juxiao.xchat.base.utils;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public enum DateFormatUtils {
    /**
     * 默认时间格式
     */
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),

    YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),
    /**
     * 时间格式,精确到天
     */
    YYYY_MM_DD("yyyy-MM-dd"),
    /**
     * 没有标点符号
     */
    YYYYMMDDHHMMSS("yyyyMMddHHmmss"),
    YYYYMMDD("yyyyMMdd"),

    HH_MM("HH:mm");


    /**
     * 时间格式转换实例
     */
    private SimpleDateFormat format;

    DateFormatUtils(String formatStr) {
        format = new SimpleDateFormat(formatStr);
        format.setLenient(false);
    }

    /**
     * 时间对象转换成字符串
     *
     * @param date
     * @return
     * @author: chenjunsheng
     * @date 2018年4月27日
     */
    public String date2Str(Date date) {
        if (date == null) {
            return null;
        }
        return format.format(date);
    }

    /**
     * 字符串转换成时间对象
     *
     * @param date
     * @return
     * @throws ParseException
     * @author: chenjunsheng
     * @date 2018/6/5
     */
    public Date str2Date(String date) throws ParseException {
        if (StringUtils.isBlank(date)) {
            throw new NullPointerException("传进日期为空");
        }
        return format.parse(date);
    }

}
