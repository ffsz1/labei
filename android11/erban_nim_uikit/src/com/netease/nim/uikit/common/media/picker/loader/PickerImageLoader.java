package com.netease.nim.uikit.common.media.picker.loader;

import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.glide.GlideApp;

public class PickerImageLoader {
    public static void initCache() {
        //:HUANGJUN
    }

    public static void clearCache() {
        //:HUANGJUN
    }

    public static void display(final String thumbPath, final String originalPath, final ImageView imageView, final int defResource) {
        GlideApp.with(NimUIKit.getContext())
                .load(thumbPath)
                .centerCrop()
                .placeholder(defResource)
                .error(defResource)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(new RotateTransformation(NimUIKit.getContext(), originalPath))
                .into(imageView);
    }
}
