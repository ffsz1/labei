package com.tongdaxing.xchat_framework.http_image.image;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.util.LruCache;

import com.tongdaxing.xchat_framework.http_image.http.HttpLog;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 图片缓存,包括memcache和diskcache
 */
public class ImageCache {

    private Set<SoftReference<Bitmap>> mReusableBitmaps;
    private LruCache<String, BitmapDrawable> mMemoryCache;
    private int memCacheSize;

    public ImageCache(Context context, AjaxImageCacheOption imageCacheOption) {
        memCacheSize = imageCacheOption.memCacheSize;
        init();
    }

    public ImageCache(Context context) {
        this(context, new AjaxImageCacheOption());
    }

    private void init() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            mReusableBitmaps = Collections.synchronizedSet(new HashSet<SoftReference<Bitmap>>());
        }

        HttpLog.d("Image memory cache size = " + memCacheSize);

        mMemoryCache = new LruCache<String, BitmapDrawable>(memCacheSize) {

            /**
             * 删除不用的cache
             */
            @Override
            protected void entryRemoved(boolean evicted, String key,
                                        BitmapDrawable oldValue, BitmapDrawable newValue) {

                if (RecycleBitmapDrawable.class.isInstance(oldValue)) {
                    // 如果是可重用的drawble则设置为不可用缓存
                    ((RecycleBitmapDrawable) oldValue).setIsCached(false);
                } else {

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                        mReusableBitmaps.add(new SoftReference<Bitmap>(oldValue.getBitmap()));
                    }
                }
            }

            /**
             * 计算bitmap占用的cache容量
             */
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                final int bitmapSize = getBitmapSize(value) / 1024;
                return bitmapSize == 0 ? 1 : bitmapSize;
            }
        };
    }

    public void addBitmapToCache(String data, BitmapDrawable value) {
        if (data == null || value == null) {
            return;
        }

        if (mMemoryCache != null) {
            if (RecycleBitmapDrawable.class.isInstance(value)) {
                ((RecycleBitmapDrawable) value).setIsCached(true);
            }
            mMemoryCache.put(data, value);
        }

    }

    public BitmapDrawable getBitmapFromMemCache(String data) {
        BitmapDrawable memValue = null;

        if (mMemoryCache != null) {
            memValue = mMemoryCache.get(data);
        }

        if (null != memValue && null != memValue.getBitmap() && memValue.getBitmap().isRecycled()) {
            mMemoryCache.remove(data);
            memValue = null;
            HttpLog.d("cache bitmap is recycled, so remove it from memory cache.");
        }

        //if (memValue != null)
        //	YLog.verbose(this, "Image loaded from memcache...");

        return memValue;
    }

    public BitmapDrawable removeBitmapFromMemCache(String key) {
        BitmapDrawable previousValue = null;

        if (null != mMemoryCache) {
            previousValue = mMemoryCache.remove(key);
        }

        return previousValue;
    }

    /**
     *
     */
    public void cleanMemCache() {
        mMemoryCache.evictAll();
    }

    @TargetApi(12)
    public static int getBitmapSize(BitmapDrawable value) {
        Bitmap bitmap = value.getBitmap();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    protected Bitmap getBitmapFromReusableSet(BitmapFactory.Options options) {
        //使用inBitmap必须是inSampleSize = 1时才可使用,可认为这是系统bug
        //https://code.google.com/p/android/issues/detail?id=58338
        if (options.inSampleSize != 1) {
            mReusableBitmaps.clear();
            return null;
        }
        Bitmap bitmap = null;

        if (mReusableBitmaps != null && !mReusableBitmaps.isEmpty()) {
            final Iterator<SoftReference<Bitmap>> iterator = mReusableBitmaps.iterator();
            Bitmap item;

            while (iterator.hasNext()) {
                item = iterator.next().get();

                if (null != item && item.isMutable()) {
                    if (canUseForInBitmap(item, options)) {
                        bitmap = item;

                        iterator.remove();
                        break;
                    }
                } else {
                    iterator.remove();
                }
            }
        }

        return bitmap;
    }

    private static boolean canUseForInBitmap(Bitmap candidate, BitmapFactory.Options targetOptions) {
        int width = targetOptions.outWidth / targetOptions.inSampleSize;
        int height = targetOptions.outHeight / targetOptions.inSampleSize;

        return candidate.getWidth() == width && candidate.getHeight() == height;
    }

    public static class AjaxImageCacheOption {
        public static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
        public static final int DEFAULT_MEM_CACHE_SIZE = 5 * 1024; // 5MB
        public static final int DEFAULT_MAX_MEM_CACHE_SIZE = 32 * 1024; // 32MB

        private int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
        private boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;
        private int maxMemCacheSize = DEFAULT_MAX_MEM_CACHE_SIZE;

        public AjaxImageCacheOption() {
            setMemCacheSizePercent(0.1f);
        }

        public void setMemoryCacheEnabled(boolean memoryCacheEnabled) {
            this.memoryCacheEnabled = memoryCacheEnabled;
        }

        public void setMaxMemorySize(int size) {
            maxMemCacheSize = size;
        }

        /**
         * 根据app可用vm的大小设置百分比的内存容量,要求在0.05至0.8之间
         * http://developer.android.com/training/displaying-bitmaps/
         *
         * @param percent
         */
        public void setMemCacheSizePercent(float percent) {
            if (percent < 0.05f || percent > 0.8f) {
                throw new IllegalArgumentException(
                        "setMemCacheSizePercent - percent must be "
                                + "between 0.05 and 0.8 (inclusive)");
            }
            memCacheSize = Math.min(Math.round(percent * Runtime.getRuntime().maxMemory()
                    / 1024), maxMemCacheSize);
        }
    }
}
