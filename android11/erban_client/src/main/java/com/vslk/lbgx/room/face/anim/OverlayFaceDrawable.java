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
import com.netease.nim.uikit.common.util.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来显示最后一张图片
 * 实现了连贯排列的效果
 *
 * @author xiaoyu
 * @date 2017/12/1
 */

public class OverlayFaceDrawable extends Drawable {

    private static final String TAG = "drawable";
    private float ratio;

    private class Pair {
        private Bitmap bitmap;
        private Rect rect;

        Pair(Bitmap bitmap, Rect rect) {
            this.bitmap = bitmap;
            this.rect = rect;
        }
    }

    private List<String> images;
    private List<Pair> data;
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private int mSpaceX;
    private int mSpaceY;
    private Context mContext;

    OverlayFaceDrawable(Context context, List<String> images, int width, int height) {
        this.images = images;
        this.mWidth = width;
        this.mHeight = height;
        this.mContext = context;
        if (images.size() > 1) {
            init();
        }
    }

    private static final float RATIO_MAX = 0.5F;

    private void init() {
        // 确定每一张图片的位置
        data = new ArrayList<>();
        int size = images.size();
        Bitmap originalBitmap = BitmapFactory.decodeFile(images.get(0));
        // 高度最大不超过35dp
        int maxHeight = Utils.dip2px(mContext, 35);
        int targetHeight = maxHeight > originalBitmap.getHeight() ? originalBitmap.getHeight() : maxHeight;
        float scale = targetHeight / (originalBitmap.getHeight() + 0.F);
        int targetWidth = (int) (scale * originalBitmap.getWidth());
        ratio = RATIO_MAX;
        // 算出overlay为0.5F的时候宽度相当于几张图片的大小
        int count = (int) (size / 2.F + 0.5F);
        if (count * targetWidth > mWidth) {
            // 如果overlay为0.5F的时候显示的总宽度大于mWidth,则需要计算出他可以显示的overlay的比例
            int canShownWidth = (mWidth - targetWidth) / (size - 1);
            ratio = (canShownWidth + 0.F) / targetWidth;
        }
        for (int i = 0; i < size; i++) {
            originalBitmap = BitmapFactory.decodeFile(images.get(i));
            Bitmap bitmap = Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, true);
            Pair pair = new Pair(bitmap, calcRect(bitmap, i));
            data.add(pair);
        }
        // 留白
        mSpaceX = (int) ((mWidth - (targetWidth * ((size - 1) * ratio + 1))) / 2);
        mSpaceY = (mHeight - targetHeight) / 2;
        // 笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        LogUtil.e(TAG, mWidth + ", mHeight: " + mHeight + ", count: " + count + ", ratio: " + ratio +
                ", mSpaceX: " + mSpaceX + ", mSpaceY: " + mSpaceY + ", bitmap.getWidth(): " +
                data.get(0).bitmap.getWidth() + ", bitmap.getHeight(): " + data.get(0).bitmap.getHeight() +
                ", originalBitmap.getWidth(): " + originalBitmap.getWidth() + ", originalBitmap.getHeight(): " + originalBitmap.getHeight());
    }

    private Rect calcRect(Bitmap bitmap, int pos) {
        Rect rect = new Rect();
        rect.left = (int) (pos * ratio * bitmap.getWidth());
        rect.top = 0;
        rect.right = rect.left + bitmap.getWidth();
        rect.bottom = bitmap.getHeight();
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
