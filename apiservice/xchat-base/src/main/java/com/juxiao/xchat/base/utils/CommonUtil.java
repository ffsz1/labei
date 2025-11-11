package com.juxiao.xchat.base.utils;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CommonUtil {
    public static String phoneRegx = "1(3|4|5|6|7|8|9)[0-9]{9}";
    public static String numberOnly = "^[0-9]*$";

    public static String getRandomNumStr(int strLength) {
        Random rm = new Random();

        // 获得随机数
        long pross = Math.abs(rm.nextLong());

        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);

        // 返回固定的长度的随机数
        return fixLenthString.substring(0, strLength);
    }

    public static boolean checkNumberOnly(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        if (str.matches(numberOnly)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查数字包含不同数字是否大于指定个数
     *
     * @param number
     * @param condition
     * @return
     */
    public static boolean checkNumber(String number, int condition) {
        boolean flag = false;
        if (StringUtils.isEmpty(number)) {
            return flag;
        }
        Map map = new HashMap();
        for (int i = 0; i < number.length(); i++) {
            map.put(number.charAt(i), number.charAt(i));
        }
        if (map.size() > condition) {
            flag = true;
        }
        return flag;
    }

    /**
     * 检查数字中，同样并且出现最多的数字，是否没有超过了condition次
     * 没有超过返回 true，超过了返回false
     *
     * @param number
     * @param condition
     * @return
     */
    public static boolean checkMaxDumpNumber(String number, int condition) {
        if (StringUtils.isEmpty(number)) {
            return true;
        }
        int numberLegth = number.length();
        Map map = new HashMap();
        for (int i = 0; i < numberLegth; i++) {
            map.put(number.charAt(i), number.charAt(i));
        }
        if (condition > map.size()) {
            return true;
        } else {
            return false;
        }
    }

    public static int getNumberDumpMaxCount(String number) {
        int maxDumpCount = 0;
        int numberAt = 0;
        for (int i = 0; i < number.length(); i++) {
            if (maxDumpCount == 0) {
                numberAt = Integer.valueOf(number.charAt(i));
                maxDumpCount++;
            } else {
                if (numberAt == Integer.valueOf(number.charAt(i)))
                    maxDumpCount++;
                else
                    maxDumpCount--;
            }
        }
        System.out.println(numberAt);
        return numberAt;
    }

    /**
     * 生成ID
     *
     * @param digit     生成位数
     * @param condition 条件, 填3表示生成的id包含3个以上不同数字
     * @return
     * @throws Exception
     */
    public static String generalId(int digit, int condition) throws Exception {
        String generalId = CommonUtil.getRandomNumStr(digit);
        boolean flag = false;
        do {
            flag = CommonUtil.checkMaxDumpNumber(generalId, condition);
            if (!flag) {
                generalId = CommonUtil.getRandomNumStr(digit);
            }
        } while (!flag);
        return generalId;
    }

    /**
     *
     *
     * @param phone
     * @return
     */
    public static boolean checkValidPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        if (phone.matches(phoneRegx)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 通过通话秒数获取分钟数，不足60秒按照一分钟计
     *
     * @param second
     * @return
     */
    public static int getPhoneCallMinuteBySecond(Integer second) {
        Integer minute = second / 60;
        if (second % 60 > 0) {
            minute++;
        }
        return minute;
    }
}
