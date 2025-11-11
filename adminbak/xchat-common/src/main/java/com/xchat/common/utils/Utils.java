package com.xchat.common.utils;

import java.util.Calendar;

/**
 * 整个系统通用工具类
 *
 * @class: Utils.java
 * @author: chenjunsheng
 * @date 2018/7/23
 */
public class Utils {

    /**
     * 把版本号转化成可对比的Long类型
     *
     * @param version
     * @return
     */
    public static long version2long(String version) {
        String[] varr = version.split("\\.");
        long vlong = 0L;
        for (int i = 0; i < varr.length; ++i) {
        	if(i==varr.length-1) {
				Integer num=Integer.parseInt(varr[i]);
				if(num<10) {
					varr[i]=rightPad(varr[i], 2, '0');
				}
			}
            vlong = vlong << 8 | Integer.parseInt(varr[i]);
        }
        return vlong;
    }

    public static void main(String[] args) {
        System.out.println(version2long("1.0.6"));
    }
    
    /**
     * 将字符串转换成Int
     * @param num
     * @return
     */
    public static Integer formatInt(String num) {
    	if(StringUtils.isBlank(num)) {
    		return 0;
    	}else {
    		return Integer.parseInt(num);
    	}
    }
    /**
     * 获取当天剩余 秒数
     */
    public static Long getLastSecond() {
        Calendar ca = Calendar.getInstance();
        //失效的时间
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 0);
        long fl = ca.getTimeInMillis();
        long second=(fl - System.currentTimeMillis())/1000;
        return second;
    }
    
    /**
     * 字符串不足多少位，向右补
     * @param src 字符串
     * @param len 长度
     * @param ch 补元素
     * @return
     */
    public static String rightPad(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, 0, src.length());
        for (int i = src.length(); i < len; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }
    
    /**
     * 字符串不足多少位，向左补
     * @param src 字符串
     * @param len 长度
     * @param ch 补元素
     * @return
     */
    public static String leftPad(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, diff, src.length());
        for (int i = 0; i < diff; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }
    
}
