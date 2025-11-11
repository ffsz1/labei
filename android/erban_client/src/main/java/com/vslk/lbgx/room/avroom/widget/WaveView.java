package com.vslk.lbgx.room.avroom.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.manager.RtcEngineManager;

/**
 * Created by huangmeng1 on 2018/1/13.
 */

public class WaveView extends View {
    private static final int DRAW_INTERVAL = 65;//波纹绘制间隔
    private static final int TOTAL_CYCLE_NUMBER = RtcEngineManager.AUDIO_UPDATE_INTERVAL / DRAW_INTERVAL + 1;//波纹绘制周期的总次数（start一次，绘制多少次）


    //    private List<Integer> alphaList = new ArrayList<>();
    private boolean isStarting = false;
    private int drawNumber;

    private float minRadius = 56f;
    private static final int MIN_ALPHA = 10;

    private float maxRadius = 80f;
    private float innerRadius = 72;
    private static final int MAX_ALPHA = 180;

    private static final float ONCE_RADIUS = 2f;
    private static final int ONCE_ALPHA = 15;

    private float radius;
    private int alpha;

    private Handler handler;

    private Paint paint;
    private int mWidth;
    private int mHeight;


    //小点
    private Path mPath = new Path();
    private Bitmap mBitmap;
    //保持圆上xy的实际坐标
    private float[] pos = new float[2];
    //保持单位圆上xy的坐标
    private float[] tan = new float[2];
    private Matrix mMatrix = new Matrix();
    private float mFloat = 0;

    private int gender = 1;


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

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

    private void initSpot() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;//缩小4倍
        if (gender == 1) {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_wave_male, options);
            paint.setColor(0x00D7F5);
        } else {
            paint.setColor(0xFF7EF5);
            mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_wave_female, options);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        initPaint();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        if (typedArray != null) {
            float distance = ConvertUtils.dp2px(4);
            minRadius = typedArray.getDimension(R.styleable.WaveView_w_radius, 56);
            maxRadius = minRadius + distance * 2;
            innerRadius = minRadius + distance * 1;
            typedArray.recycle();
        }
        radius = minRadius;
        alpha = MAX_ALPHA;
        handler = new Handler();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setStrokeWidth(4f);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isStarting) return;
        initSpot();
        paint.setAlpha(alpha);
        paint.setAntiAlias(true);
        canvas.drawCircle(mWidth / 2f, mHeight / 2f, radius, paint);
        mPath.reset();
        paint.setAlpha(180);
        mPath.addCircle(mWidth / 2f, mHeight / 2f, radius - 8, Path.Direction.CW);
        canvas.drawPath(mPath, paint);
        PathMeasure pathMeasure = new PathMeasure(mPath, false);
        //圆周长
        float distance = pathMeasure.getLength();
        mFloat += 0.01;
        if (mFloat >= 1) {
            mFloat = 0;
        }
        pathMeasure.getPosTan(distance * mFloat, pos, tan);
        float px = pos[0];
        float py = pos[1];
        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180 / Math.PI);
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        mMatrix.reset();
        //必须选postRotate，再调用 postTranslate
        mMatrix.postRotate(degrees, (float) width / 2, (float) height / 2);
        mMatrix.postTranslate(px - (float) width / 2, py - (float) height / 2);
        canvas.drawBitmap(mBitmap, mMatrix, paint);


        if (radius == maxRadius) {
            //已经达到最外层，开始新的一圈绘制
            radius = minRadius;
            alpha = MAX_ALPHA;
        } else {
            if (radius + ONCE_RADIUS <= maxRadius) {
                radius += ONCE_RADIUS;
            } else {
                radius = maxRadius;
            }
            if (radius == innerRadius) {
                alpha = MAX_ALPHA;
            } else {
                if (alpha - ONCE_ALPHA >= MIN_ALPHA) {
                    alpha -= ONCE_ALPHA;
                } else {
                    alpha = MIN_ALPHA;
                }
            }
        }
        if (drawNumber >= TOTAL_CYCLE_NUMBER) {
            stop();
            invalidate();
        } else {
            handler.postDelayed(this::invalidate, DRAW_INTERVAL);
            drawNumber++;
        }
    }

    public void start() {
        drawNumber = 0;
        isStarting = true;
        handler.removeCallbacksAndMessages(null);
        invalidate();
    }

    public void stop() {
        drawNumber = 0;
        radius = minRadius;
        alpha = MAX_ALPHA;
        isStarting = false;
    }
}
