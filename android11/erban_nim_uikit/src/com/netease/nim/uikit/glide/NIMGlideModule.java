package com.netease.nim.uikit.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.uikit.NimUIKit;

import java.io.File;

/**
 * glide 配置
 * Created by huangjun on 2017/4/1.
 */
@GlideModule
public class NIMGlideModule extends AppGlideModule {
    private static final String TAG = "NIMGlideModule";

    private static final int M = 1024 * 1024;
    private static final int MAX_DISK_CACHE_SIZE = 256 * M;
    private static DiskCache diskCache = null;

    /**
     * ************************ Disk Cache ************************
     */
    private static synchronized DiskCache getDiskCache() {
        if (diskCache == null) {
            diskCache = createDiskCache();
        }
        return diskCache;
    }

    private static synchronized DiskCache createDiskCache() {
        File cacheDir = getCacheDir();
        return DiskLruCacheWrapper.get(cacheDir, MAX_DISK_CACHE_SIZE);
    }

    @NonNull
    private static File getCacheDir() {
        final Context context = NimUIKit.getContext();
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, context.getPackageName() + "/cache/image/");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    /**
     * ************************ Memory Cache ************************
     */

    static void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * ************************ GlideModule override ************************
     */
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取系统分配给应用的总内存大小
        int memoryCacheSize = maxMemory / 8;//设置图片内存缓存占用八分之一
        //设置内存缓存大小
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        // 设置Glide磁盘缓存大小
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, MAX_DISK_CACHE_SIZE));
        //设置默认图片解码格式
        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565).disallowHardwareConfig());//disallowHardwareConfig()方法用于解决8.0以上系统抛出Software rendering doesn't support hardware bitmaps异常的问题
        //设置BitmapPool缓存内存大小
        builder.setBitmapPool(new LruBitmapPool(memoryCacheSize));
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
