package com.tongdaxing.xchat_core.bills.bean;

import java.io.Serializable;

/**
 * Created by ${Seven} on 2017/9/13.
 */

public class TimeInfo implements Serializable {
    /*public TimeInfo(long time) {
        this.time = time;
    }*/

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    private long time;
}
