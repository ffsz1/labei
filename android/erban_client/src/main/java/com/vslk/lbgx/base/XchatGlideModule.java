package com.vslk.lbgx.base;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.module.AppGlideModule;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.glide.StorageUtils;

import java.io.File;

/**
 * <p> 应用工程的glide 配置 </p>
 * Created by Administrator on 2017/11/14.
 */
//@GlideModule
public class XchatGlideModule extends AppGlideModule {
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
        final Context context = NimUIKit.getContext();
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, context.getPackageName() + "/cache/image/");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return DiskLruCacheWrapper.get(cacheDir, MAX_DISK_CACHE_SIZE);
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
        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                return getDiskCache();
            }
        });
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
    }
}
