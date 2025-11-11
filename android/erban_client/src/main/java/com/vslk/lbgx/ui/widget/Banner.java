package com.vslk.lbgx.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.jude.rollviewpager.RollPagerView;

/**
 * Created by huangmeng1 on 2018/1/6.
 */

public class Banner extends RollPagerView {
    public Banner(Context context) {
        super(context);
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Banner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // 滑动距离及坐标 归还父控件焦点
    private float xDistance, yDistance, xLast, yLast, mLeft;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                mLeft = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                if (mLeft < 100 || xDistance < yDistance) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
