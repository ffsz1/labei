package com.tongdaxing.erban.libcommon.net.exception;

import android.support.annotation.StringDef;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2018/1/6
 */
public class ErrorThrowable extends Throwable {
    /** 房间信息为空报错 */
    public static final String ROOM_INFO_NULL_ERROR = "50012";

    @StringDef(ROOM_INFO_NULL_ERROR)
    public @interface ErrorStr {
    }


    public ErrorThrowable(@ErrorStr String message) {
        super(message);
    }

    public ErrorThrowable(@ErrorStr String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorThrowable(Throwable cause) {
        super(cause);
    }
}
