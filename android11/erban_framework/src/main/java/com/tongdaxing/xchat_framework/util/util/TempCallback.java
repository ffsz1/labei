package com.tongdaxing.xchat_framework.util.util;

/**
 * Function:
 * Author: Edward on 2019/5/30
 */
public class TempCallback {
    public interface ITempCallback {
        boolean canSendPic();
    }

    private static ITempCallback iTempCallback;

    public static ITempCallback getiTempCallback() {
        return iTempCallback;
    }

    public static void setiTempCallback(ITempCallback iTempCallback1) {
        iTempCallback = iTempCallback1;
    }
}
