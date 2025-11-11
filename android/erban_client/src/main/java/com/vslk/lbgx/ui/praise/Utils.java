package com.vslk.lbgx.ui.praise;

import java.util.Random;

public class Utils {
    private static final Random RANDOM = new Random();

    private Utils() {
    }

    public static int rondomRange(int max, int min) {
        return RANDOM.nextInt(max)
                % (max - min + 1) + min;
    }
}
