package com.vslk.lbgx.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.tongdaxing.xchat_framework.util.util.log.MLog;


/**
 * Created by wa on 14-9-28.
 */
public class FixedTouchViewPager extends ViewPager {
    private boolean isCanScroll = true;

    public FixedTouchViewPager(Context context) {
        super(context);
    }

    public FixedTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            if(!isCanScroll){
                return false;
            }
            return super.onTouchEvent(ev);
        } catch (Throwable ex) {
            MLog.error(this, "xuwakao, onTouchEvent fix touch viewpager error happens, ev = " + ev);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            if(!isCanScroll){
                return false;
            }
            return super.onInterceptTouchEvent(ev);
        } catch (Exception ex) {
            MLog.error(this, "xuwakao, onInterceptTouchEvent fix touch viewpager error happens, ev= " + ev);
        }
        return false;
    }

    public void setCanScroll(boolean isCanScroll){
        this.isCanScroll = isCanScroll;
    }

    @Override
    public void scrollTo(int x, int y){
        if (isCanScroll){
            super.scrollTo(x, y);
        }
    }
}