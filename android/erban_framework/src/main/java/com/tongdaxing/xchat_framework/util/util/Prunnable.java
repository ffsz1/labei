package com.tongdaxing.xchat_framework.util.util;

/**
 * 创建者     polo
 * 创建时间   2017/8/18 10:36
 * 描述
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述
 */

public class Prunnable implements Runnable {
    protected Object[] params = null;

    public Prunnable() {
    }

    public Prunnable(Object... ps) {
        this.params = ps;
    }

    public void run() {
    }
}