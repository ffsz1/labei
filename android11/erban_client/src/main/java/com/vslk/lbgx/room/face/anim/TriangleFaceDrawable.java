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

import com.vslk.lbgx.ui.widget.marqueeview.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来显示最后一张图片
 * 实现了连贯排列的效果
 *
 * @author xiaoyu
 * @date 2017/12/1
 */

public class TriangleFaceDrawable extends Drawable {

    private static final String TAG = "drawable";

    private class Pair {
        private Bitmap bitmap;
        private Rect rect;

        Pair(Bitmap bitmap, Rect rect) {
            this.bitmap = bitmap;
            this.rect = rect;
        }
    }

    private int[] images;
    private List<Pair> data;
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private int mSpaceX;
    private int mSpaceY;
    private Context mContext;

    TriangleFaceDrawable(Context context, int[] images, int width, int height) {
        this.images = images;
        this.mWidth = width;
        this.mHeight = height;
        this.mContext = context;
        if (images.length > 0) {
            init();
        }
    }

    private static final int RAW_MAX_COUNT = 3;
    private static final int COLUMN_MAX_COUNT = 3;

    private void init() {
        // 确定每一张图片的位置
        data = new ArrayList<>();
        int size = images.length;
        // 行数
        int rows = size < RAW_MAX_COUNT ? 1 : size < 6 ? 2 : 3;
        // 列数
        int columns = size > COLUMN_MAX_COUNT ? COLUMN_MAX_COUNT : size == 1 ? 1 : 2;
        int max = Math.max(rows, columns);
        float ratio = 1;

        Bitmap originalBitmap = null;
        for (int i = 0; i < size; i++) {
            originalBitmap = BitmapFactory.decodeResource(mContext.getResources(),images[i]);
            ratio = (max == rows) ? (originalBitmap.getWidth() / (mWidth + 0.F)) * max : (originalBitmap.getHeight() / (mHeight + 0.F)) * max;
            int width = (int) (originalBitmap.getWidth() / ratio);
            int maxWidth = Utils.dip2px(mContext, 28);
            if (width > maxWidth) {
                ratio = ratio * (width / (maxWidth + 0.F));
            }

            Bitmap bitmap = Bitmap.createScaledBitmap(originalBitmap, width > maxWidth ? maxWidth : width, (int) (originalBitmap.getHeight() / ratio), true);
            Pair pair = new Pair(bitmap, calcRect(bitmap, i));
            data.add(pair);
        }
        // 留白
        mSpaceX = (mWidth - columns * data.get(0).bitmap.getWidth()) / 2;
        mSpaceY = (mHeight - rows * data.get(0).bitmap.getHeight()) / 2;
        // 笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private Rect calcRect(Bitmap bitmap, int pos) {
        int size = images.length;
        Rect rect = new Rect();
        switch (size) {
            case 1:
                rect.left = 0;
                rect.top = 0;
                rect.right = bitmap.getWidth();
                rect.bottom = bitmap.getHeight();
                break;
            case 2:
                rect.left = pos * bitmap.getWidth();
                rect.top = 0;
                rect.right = rect.left + bitmap.getWidth();
                rect.bottom = bitmap.getHeight();
                break;
            case 3:
                if (pos == 0) {
                    rect.left = bitmap.getWidth() / 2;
                    rect.top = 0;
                    rect.right = rect.left + bitmap.getWidth();
                    rect.bottom = bitmap.getHeight();
                } else {
                    rect.left = (pos - 1) * bitmap.getWidth();
                    rect.top = bitmap.getHeight();
                    rect.right = rect.left + bitmap.getWidth();
                    rect.bottom = rect.top + bitmap.getHeight();
                }
                break;
            default:
        }
        return rect;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        for (int i = 0; i < data.size(); i++) {
            canvas.drawBitmap(data.get(i).bitmap, mSpaceX + data.get(i).rect.left, mSpaceY + data.get(i).rect.top, mPaint);
        }
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
