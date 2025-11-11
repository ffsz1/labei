package com.vslk.lbgx.room.avroom.other;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * @author chenran
 * @date 2017/9/24
 */

public class ScrollSpeedLinearLayoutManger extends LinearLayoutManager {
    private float MILLISECONDS_PER_INCH = 0.2F;
    private Context context;
    private LinearSmoothScroller linearSmoothScroller;
    private int lastPosition = -1;

    public ScrollSpeedLinearLayoutManger(Context context) {
        super(context);
        this.context = context;
        linearSmoothScroller = new LinearSmoothScroller(context) {
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                PointF pointF = ScrollSpeedLinearLayoutManger.this
                        .computeScrollVectorForPosition(targetPosition);
                Log.e("Point", pointF.y + "");
                return pointF;
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                //返回滑动一个pixel需要多少毫秒
                return MILLISECONDS_PER_INCH;
            }

        };
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        if (linearSmoothScroller.isRunning() && lastPosition != -1) {
            scrollToPosition(lastPosition);
        }
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
        lastPosition = position;
    }
}