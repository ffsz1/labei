package com.xchat.oauth2.service.infrastructure;

import java.util.Date;

/**
 * @author liuguofu
 */
public abstract class DateUtils {


    /**
     * Private constructor
     */
    private DateUtils() {
    }

    public static Date now() {
        return new Date();
    }

}
