package com.xchat.common.utils;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * @class: RandomUtils.java
 * @author: chenjunsheng
 * @date 2018/8/9
 */
public class RandomUtils {

    private static final Random RAND = new Random();

    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("0.000000");

    public static int nextInt(int min, int max) {
        return RAND.nextInt(max - min + 1) + min;
    }

    public static double nextDoule(DecimalFormat format) {
        double number = RAND.nextDouble();
        if (format == null) {
            return number;
        }
        return Double.parseDouble(format.format(number));
    }

    public static double nextDoule() {
        return nextDoule(DOUBLE_FORMAT);
    }


}
