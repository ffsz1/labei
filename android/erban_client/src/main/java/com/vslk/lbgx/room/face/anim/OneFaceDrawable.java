package com.vslk.lbgx.room.face.anim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * 用来显示一张图片
 *
 * @author xiaoyu
 * @date 2017/12/1
 */

public class OneFaceDrawable extends Drawable {

    private static final String TAG = "OneFaceDrawable";
    private float ratio;

    private class Pair {
        private Bitmap bitmap;
        private Rect rect;

        Pair(Bitmap bitmap, Rect rect) {
            this.bitmap = bitmap;
            this.rect = rect;
        }
    }

    private String image;
    private Pair pair;
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private int mSpaceX;
    private int mSpaceY;
    private Context mContext;

    OneFaceDrawable(Context context, String image, int width, int height) {
        this.image = image;
        this.mWidth = width;
        this.mHeight = height;
        this.mContext = context;
        if (!TextUtils.isEmpty(image)) {
            init();
        }
    }

    private static final float RATIO_MAX = 1F;

    private void init() {
        // 确定一张图片的位置
        Bitmap originalBitmap = BitmapFactory.decodeFile(image);
        float ratioX = (mWidth + 0.F) / originalBitmap.getWidth();
        float ratioY = (mHeight + 0.F) / originalBitmap.getHeight();
        ratio = ratioX > ratioY ? ratioY : ratioX;
        Bitmap bitmap = Bitmap.createScaledBitmap(originalBitmap, (int) (originalBitmap.getWidth() * ratio), (int) (originalBitmap.getHeight() * ratio), true);
        pair = new Pair(bitmap, calcRect(bitmap));
        // 留白
        mSpaceX = (mWidth - bitmap.getWidth()) / 2;
        mSpaceY = (mHeight - bitmap.getHeight()) / 2;
        // 笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private Rect calcRect(Bitmap bitmap) {
        Rect rect = new Rect();
        rect.left = 0;
        rect.top = 0;
        rect.right = rect.left + bitmap.getWidth();
        rect.bottom = bitmap.getHeight();
        return rect;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawBitmap(pair.bitmap, mSpaceX + pair.rect.left, mSpaceY + pair.rect.top, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public int getIntrinsicHeight() {
        return mHeight;
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
