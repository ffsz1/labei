package com.vslk.lbgx.ui.me.bills.event;

/**
 * <p> 事件日期 </p>
 * Created by Administrator on 2017/11/8.
 */
public class DateInfoEvent {
    public long millSeconds;
    public int position;

    public DateInfoEvent(long millSeconds, int position) {
        this.millSeconds = millSeconds;
        this.position = position;
    }
}
