package com.tongdaxing.xchat_framework.http_image.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.tongdaxing.xchat_framework.http_image.http.HttpLog;

/**
 * 可设置重用的BitmapDrawable
 *
 * @author zhongyongsheng
 */
public class RecycleBitmapDrawable extends BitmapDrawable {

    private static final String LOG_TAG = RecycleBitmapDrawable.class.getSimpleName();

    private int mCacheRefCount = 0;
    private int mDisplayRefCount = 0;

    private boolean mHasBeenDisplayed;

    public RecycleBitmapDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
        HttpLog.d(LOG_TAG);
    }

    public void setIsDisplayed(boolean isDisplayed) {
        synchronized (this) {
            if (isDisplayed) {
                mDisplayRefCount++;
                mHasBeenDisplayed = true;
            } else {
                mDisplayRefCount--;
            }
            // Check to see if recycle() can be called
            checkState();
        }

    }

    public void setIsCached(boolean isCached) {
        synchronized (this) {
            if (isCached) {
                mCacheRefCount++;
            } else {
                mCacheRefCount--;
            }
            checkState();
        }
    }

    private synchronized void checkState() {
        if (mCacheRefCount <= 0 && mDisplayRefCount <= 0 && mHasBeenDisplayed
                && hasValidBitmap()) {
            HttpLog.v("No longer being used or cached so recycling. ");

            getBitmap().recycle();
        }
    }

    private synchronized boolean hasValidBitmap() {
        Bitmap bitmap = getBitmap();
        return bitmap != null && !bitmap.isRecycled();
    }

}
