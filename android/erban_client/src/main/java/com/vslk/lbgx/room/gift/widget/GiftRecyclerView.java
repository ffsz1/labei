package com.vslk.lbgx.room.gift.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.netease.nim.uikit.common.util.log.LogUtil;

/**
 * @author xiaoyu
 * @date 2017/12/12
 */

public class GiftRecyclerView extends RecyclerView {
    private static final String TAG = "gift";

    public GiftRecyclerView(Context context) {
        super(context);
    }

    public GiftRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GiftRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private float mLastY;
    private float mStartY;
    private boolean allowIntercept = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int pos = ((GridLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // down事件首先阻断父控件的拦截
                getParent().requestDisallowInterceptTouchEvent(true);
                // 但是允许父控件后续的拦截
                allowIntercept = true;
                mLastY = ev.getY();
                mStartY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.e(TAG, "ACTION_MOVE");
                float dy = ev.getY() - mLastY;
                float totalY = ev.getY() - mStartY;
                if (pos == 0 && getChildAt(0).getTop() == 0) {
                    if (getChildAt(0).getTop() + dy < 0) {
                        // 只要内部滑动一次,则往后都不允许父控件拦截
                        getParent().requestDisallowInterceptTouchEvent(true);
                        allowIntercept = false;
                    }
                    if (allowIntercept && ViewConfiguration.get(getContext()).getScaledTouchSlop() < totalY) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                } else {
                    // 如果当前第一个完整显示的位置不是0,则允许内部滑动
                    allowIntercept = false;
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.e(TAG, "ACTION_UP");
                break;
            default:
        }
        return super.dispatchTouchEvent(ev);
    }
}
