package com.hncxco.library_ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.support.annotation.ColorInt;
import android.view.ViewGroup;

import com.hncxco.library_ui.R;

/**
 * 波纹view
 * Created by zeda on 2019/6/13.
 */

public class WaveView extends View {

    //每次绘制间隔
    protected int drawInterval = 65;

    //每start一次，最少绘制多久
    protected int startMinDrawTime = 600;

    //颜色
    protected @ColorInt
    int color = 0xFFFF9999;

    //最小圈半径
    protected int minRadius = 60;
    //最小透明度
    protected int minAlpha = 50;

    //最大圈半径
    protected int maxRadius = 0;
    //最大透明度
    protected int maxAlpha = 180;

    //中间圈半径
    protected float innerRadius = 68f;

    //每次递增的圈半径
    protected float onceRadius = 2f;
    //每次递减的透明度
    protected int onceAlpha = 6;

    //绘制周期的总次数（start一次，绘制多少次）
    private int totalDrawNumber;

    private Paint paint;
    private boolean isStarting = false;
    private int drawNumber;

    private float radius;
    private int alpha;

    private Handler handler;

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WaveView(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, com.hncxco.library_ui.R.styleable.WaveView);
        if (typedArray != null) {
            minRadius = typedArray.getDimensionPixelOffset(R.styleable.WaveView_w_radius, minRadius);
            maxRadius = typedArray.getDimensionPixelOffset(R.styleable.WaveView_w_max_radius, maxRadius);
            startMinDrawTime = typedArray.getInt(R.styleable.WaveView_w_draw_time, startMinDrawTime);
            color = typedArray.getColor(com.hncxco.library_ui.R.styleable.WaveView_w_color, color);
            typedArray.recycle();
        }
        if (maxRadius == 0) {
            maxRadius = minRadius + (int) (8 * context.getResources().getDisplayMetrics().density + 0.5f);
        }
        totalDrawNumber = startMinDrawTime / drawInterval + 1;
        innerRadius = minRadius + (maxRadius - minRadius) / 2f;
        paint = new Paint();
        paint.setColor(color);
        radius = minRadius;
        alpha = maxAlpha;
        handler = new Handler();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //这里在不恰当设置宽高（宽高设置了WRAP_CONTENT/MATCH_PARENT或者宽高比直径还小）的情况下纠正
        int diameter = maxRadius * 2;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT || layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT || layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT || layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT
                    || layoutParams.width < diameter || layoutParams.height < diameter) {
                layoutParams.width = diameter;
                layoutParams.height = diameter;
            }
        } else {
            layoutParams = new ViewGroup.LayoutParams(diameter, diameter);
        }
        setLayoutParams(layoutParams);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isStarting) return;
        paint.setAlpha(alpha);
        paint.setAntiAlias(true);
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, paint);
        if (radius == maxRadius) {
            //已经达到最外圈，开始新的一轮绘制
            radius = minRadius;
            alpha = maxAlpha;
        } else {
            if (radius + onceRadius <= maxRadius) {
                //圈范围递增
                radius += onceRadius;
            } else {
                //超出最外圈一点则等于最外圈
                radius = maxRadius;
            }
            if (radius == innerRadius) {
                //达到中间圈，重置透明度（两次透明度递减制造变化两次的效果）
                alpha = maxAlpha;
            } else {
                if (alpha - onceAlpha >= minAlpha) {
                    //透明度递减
                    alpha -= onceAlpha;
                } else {
                    //超出最小透明度则等于最小透明度
                    alpha = minAlpha;
                }
            }
        }

        if (drawNumber >= totalDrawNumber) {
            //绘制次数达到波纹绘制周期的总次数 则停止绘制
            stop();
            invalidate();
        } else {
            //定时开始下一次的绘制
            handler.postDelayed(this::invalidate, drawInterval);
            drawNumber++;
        }
    }

    public void start() {
        drawNumber = 0;
        if (!isStarting) {
            isStarting = true;
            handler.removeCallbacksAndMessages(null);
            invalidate();
        }
    }

    public void stop() {
        drawNumber = 0;
        radius = minRadius;
        alpha = maxAlpha;
        isStarting = false;
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }

    public int getDrawInterval() {
        return drawInterval;
    }

    public void setDrawInterval(int drawInterval) {
        this.drawInterval = drawInterval;
    }

    public int getStartMinDrawTime() {
        return startMinDrawTime;
    }

    public void setStartMinDrawTime(int startMinDrawTime) {
        this.startMinDrawTime = startMinDrawTime;
    }

    public int getMinRadius() {
        return minRadius;
    }

    public void setMinRadius(int minRadius) {
        this.minRadius = minRadius;
    }

    public int getMinAlpha() {
        return minAlpha;
    }

    public void setMinAlpha(int minAlpha) {
        this.minAlpha = minAlpha;
    }

    public int getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }

    public int getMaxAlpha() {
        return maxAlpha;
    }

    public void setMaxAlpha(int maxAlpha) {
        this.maxAlpha = maxAlpha;
    }

    public float getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(float innerRadius) {
        this.innerRadius = innerRadius;
    }

    public float getOnceRadius() {
        return onceRadius;
    }

    public void setOnceRadius(float onceRadius) {
        this.onceRadius = onceRadius;
    }

    public int getOnceAlpha() {
        return onceAlpha;
    }

    public void setOnceAlpha(int onceAlpha) {
        this.onceAlpha = onceAlpha;
    }
}
