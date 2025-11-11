package com.tongdaxing.erban.libcommon.swipeactivity;

/**
 * @author hm
 */
public interface SwipeBackActivityBase {
    /**
     * 得到SwipeBackLayout对象
     * @return
     */
    public abstract SwipeBackLayout getSwipeBackLayout();

    /**
     * 设置是否可以滑动
     * @param enable
     */
    public abstract void setSwipeBackEnable(boolean enable);

    /**
     * 自动滑动返回并关闭Activity
     */
    public abstract void scrollToFinishActivity();

}
