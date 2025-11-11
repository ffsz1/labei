package com.juxiao.xchat.base.utils;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机生成工具类
 *
 * @class: RandomUtils.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
public class RandomUtils {
    private static final Random RANDOM = new Random();
    private static final DecimalFormat DOUBLE_2_FORMAT = new DecimalFormat("0.00");
    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("0.000000000");

    /**
     * 随机生成Double类型数字
     *
     * @return
     * @author: chenjunsheng
     * @date 2018/6/8
     */
    public static double randomDouble() {
        double rdouble = RANDOM.nextDouble();
        return new Double(DOUBLE_FORMAT.format(rdouble));
    }

//    public static double threadLocalRandomDouble() {
//        return new Double(DOUBLE_FORMAT.format(ThreadLocalRandom.current().nextDouble()));
//    }

    public static double threadLocalRandomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    /**
     * 随机生成{min}-{max}范围内的Double类型数字
     *
     * @param min
     * @param max
     * @return
     * @author: chenjunsheng
     * @date 2018/6/14
     */
    public static double randomRegionDouble(double min, double max) {
        double rdouble = min + RANDOM.nextDouble() * (max - min);
        return new Double(DOUBLE_2_FORMAT.format(rdouble));
    }

    /**
     * 随机生成{min}-{max}范围内的Int类型数字
     *
     * @param min
     * @param max
     * @return
     * @author: chenjunsheng
     * @date 2018/6/14
     */
    public static int randomRegionInteger(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;

    }

    public static String getRandomNumStr(int strLength) {
        // 获得随机数
        long pross = Math.abs(RANDOM.nextLong());
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);
        // 返回固定的长度的随机数
        return fixLenthString.substring(0, strLength);
    }

    /**
     * @param sum        要拆分的总数
     * @param splitCount 个数
     * @param min        最小拆分值
     * @param max        最大拆分值
     */
    public static int[] randomSplit(int sum, int splitCount, int min, int max) {
        float avg = (float) sum / splitCount;
        if (avg < min) {
            throw new IllegalArgumentException("The parameter of min shold be less than " + avg);
        }

        if (avg > max) {
            throw new IllegalArgumentException("The parameter of max shold be less than " + avg);
        }

        if (avg == min) {
            int[] array = new int[splitCount];
            for (int i = 0; i < splitCount; i++) {
                array[i] = (int) avg;
            }
            return array;
        }

        int[] array = new int[splitCount];
        int difference = sum - min * splitCount;
        for (int i = 0; i < splitCount - 1; i++) {
            if (difference <= 0) {
                array[i] = min;
                continue;
            }

            int augend = RANDOM.nextInt(difference);
            array[i] = min + augend;
            if (array[i] > max) {
                i--;
                continue;
            }

            difference = difference - augend;

//            if (difference / (splitCount - i) > max - avg) {
//                i--;
//            }
//            array[i] = random;
//            difference = difference - augend;
        }

        array[splitCount - 1] = min + difference;
        return array;
    }

    public static void main(String[] args) {
        for(int i = 0; i<=100; i++){
            double randomNumber = RandomUtils.threadLocalRandomDouble();
            System.out.println(randomNumber);
        }
    }

}
