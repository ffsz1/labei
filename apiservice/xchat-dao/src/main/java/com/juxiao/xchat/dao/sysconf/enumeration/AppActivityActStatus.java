package com.juxiao.xchat.dao.sysconf.enumeration;

/**
 * 活动页面
 *
 * @class: AppActivityActStatus.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
public enum AppActivityActStatus {
    /**
     * 正在活动中
     */
    using(1),
    /**
     * 该活动被删除
     */
    deleted(2);

    private int value;

    AppActivityActStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}