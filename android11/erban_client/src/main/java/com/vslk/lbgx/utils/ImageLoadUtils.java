package com.vslk.lbgx.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.vslk.lbgx.utils.blur.BlurTransformation;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.glide.GlideApp;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.utils.SystemUtils;

import java.io.File;

/**
 * 图片加载处理
 * Created by chenran on 2017/11/9.
 */
public class ImageLoadUtils {
    private static final String PIC_PROCESSING = "?imageslim";
    // 七牛accessUrl
    private static final String ACCESS_URL = "pic.haijiaoxingqiu.cn";

    public static void loadAvatar(Context context, String avatar, ImageView imageView, boolean isCircle) {
        if (StringUtil.isEmpty(avatar)) {
            return;
        }

        StringBuilder sb = new StringBuilder(avatar);
        if (avatar.contains(ACCESS_URL)) {
            if (!avatar.contains("?")) {
                sb.append(PIC_PROCESSING);
            }
            sb.append("|imageView2/1/w/100/h/100");
        }
        if (isCircle) {
            loadCircleImage(context, sb.toString(), imageView, R.drawable.ic_default_avatar);
        } else {
            loadImage(context, sb.toString(), imageView, R.drawable.ic_default_avatar);
        }
    }

    public static void loadBigAvatar(Context context, String avatar, ImageView imageView) {
        if (StringUtil.isEmpty(avatar)) {
            return;
        }
        StringBuffer sb = new StringBuffer(avatar);
        if (avatar.contains(ACCESS_URL)) {
            if (!avatar.contains("?")) {
                sb.append(PIC_PROCESSING);
            }
            sb.append("|imageView2/1/w/400/h/200");
        }
        loadImage(context, sb.toString(), imageView, R.drawable.ic_default_avatar);
    }

    public static void loadAvatar(Context context, String avatar, ImageView imageView) {
        loadAvatar(context, avatar, imageView, false);
    }

    public static void loadSmallRoundBackground(Context context, String url, ImageView imageView) {
        if (StringUtil.isEmpty(url)) {
            return;
        }
        StringBuilder sb = new StringBuilder(url);
        if (url.contains(ACCESS_URL)) {
            if (!url.contains("?")) {
                sb.append(PIC_PROCESSING);
            }
            sb.append("|imageView2/1/w/220/h/220");
        }
        GlideApp.with(context)
                .load(sb.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transforms(new CenterCrop(),
                        new RoundedCorners(
                                context.getResources().getDimensionPixelOffset(R.dimen.common_cover_round_size)))
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.ic_default_avatar)
                .into(imageView);
    }

    public static void loadSmallRoundBackground(Context context, String url, ImageView imageView, int radius) {
        if (StringUtil.isEmpty(url)) {
            return;
        }
        StringBuilder sb = new StringBuilder(url);
        if (url.contains(ACCESS_URL)) {
            if (!url.contains("?")) {
                sb.append(PIC_PROCESSING);
            }
            sb.append("|imageView2/1/w/220/h/220");
        }
        GlideApp.with(context)
                .load(sb.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transforms(new CenterCrop(),
                        new RoundedCorners(radius))
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.ic_default_avatar)
                .into(imageView);
    }

    public static void loadRoomBgBackground(Context context, String url, ImageView imageView) {
        if (StringUtil.isEmpty(url)) {
            return;
        }
        StringBuilder sb = new StringBuilder(url);
        if (url.contains(ACCESS_URL)) {
            if (!url.contains("?")) {
                sb.append(PIC_PROCESSING);
            }
            sb.append("|imageView2/1/w/720/h/1280");
        }
        GlideApp.with(context)
                .load(sb.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadUserInfoBackground(Context context, String url, ImageView imageView) {
        if (StringUtil.isEmpty(url)) {
            return;
        }
        StringBuffer sb = new StringBuffer(url);
        if (url.contains(ACCESS_URL)) {
            if (!url.contains("?")) {
                sb.append(PIC_PROCESSING);
            }
            sb.append("|imageView2/1/w/660/h/300");
        }

        GlideApp.with(context)
                .load(sb.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transforms(new CenterCrop())
                .placeholder(R.drawable.ic_default_avatar)
                .into(imageView);
    }

    public static void loadBannerRoundBackground(Context context, String url, ImageView imageView, int roundSize) {
        if (StringUtil.isEmpty(url)) {
            return;
        }
        StringBuffer sb = new StringBuffer(url);
        if (url.contains(ACCESS_URL)) {
            if (!url.contains("?")) {
                sb.append(PIC_PROCESSING);
            }
            sb.append("|imageView2/1/w/660/h/220");
        }

        GlideApp.with(context)
                .load(sb.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transforms(new CenterCrop(),
                        new RoundedCorners(context.getResources().getDimensionPixelOffset(roundSize)))
                .placeholder(R.drawable.ic_default_avatar)
                .into(imageView);
    }

    public static void loadBannerRoundBackground(Context context, String url, ImageView imageView, int roundSize,
                                                 @DrawableRes int placeHolder) {
        if (StringUtil.isEmpty(url)) {
            return;
        }
        StringBuffer sb = new StringBuffer(url);
        if (url.contains(ACCESS_URL)) {
            if (!url.contains("?")) {
                sb.append(PIC_PROCESSING);
            }
            sb.append("|imageView2/1/w/660/h/220");
        }

        GlideApp.with(context)
                .load(sb.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transforms(new CenterCrop(),
                        new RoundedCorners(context.getResources().getDimensionPixelOffset(roundSize)))
                .placeholder(placeHolder)
                .into(imageView);
    }

    public static void loadPhotoThumbnail(Context context, String url, ImageView imageView) {
        if (StringUtil.isEmpty(url)) {
            return;
        }
        StringBuffer sb = new StringBuffer(url);
        if (url.contains(ACCESS_URL)) {
            if (!url.contains("?")) {
                sb.append(PIC_PROCESSING);
            }
            sb.append("|imageView2/1/w/150/h/150");
        }

        loadImage(context, sb.toString(), imageView, R.drawable.ic_default_avatar);
    }

    public static void loadImageWithBlurTransformation(Context context, String url, final ImageView imageView) {
        if (StringUtil.isEmpty(url)) {
            return;
        }
        StringBuffer sb = new StringBuffer(url);
        if (url.contains(ACCESS_URL)) {
            if (!url.contains("?")) {
                sb.append(PIC_PROCESSING);
            }
            sb.append("|imageView2/1/w/75/h/75");
        }
        GlideApp.with(context)
                .load(sb.toString())
                .dontTransform()
                .dontAnimate()
                .override(75, 75)
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o,
                                                Target<Drawable> target, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o,
                                                   Target<Drawable> target, DataSource dataSource, boolean b) {
                        imageView.setImageDrawable(drawable);
                        if (!b) {
                            imageView.setAlpha(0.F);
                            imageView.animate().alpha(1F).setDuration(500).start();
                        }
                        return true;
                    }
                })
                // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .transforms(new BlurTransformation(context, 10, 1))
                .into(imageView);
    }

    public static void loadImageWithBlurTransformation(Context context, Bitmap bitmap, final ImageView imageView) {
        GlideApp.with(context)
                .load(bitmap)
                .dontTransform()
                .dontAnimate()
                .override(75, 75)
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .listener(new RequestListener<Drawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o,
                                                Target<Drawable> target, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o,
                                                   Target<Drawable> target, DataSource dataSource, boolean b) {
                        imageView.setImageDrawable(drawable);
                        if (!b) {
                            imageView.setAlpha(0.F);
                            imageView.animate().alpha(1F).setDuration(500).start();
                        }
                        return true;
                    }
                })
                // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .transforms(new BlurTransformation(context, 25, 2))
                .into(imageView);
    }

    public static void loadImageWithBlurTransformationAndCorner(Context context, String url, ImageView imageView) {
        if (StringUtil.isEmpty(url)) {
            return;
        }
        StringBuffer sb = new StringBuffer(url);
        if (url.contains(ACCESS_URL)) {
            if (!url.contains("?")) {
                sb.append(PIC_PROCESSING);
            }
            sb.append("|imageView2/1/w/75/h/75");
        }
        GlideApp.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transforms(new CenterCrop(),
                        new BlurTransformation(context, 25, 3),
                        new RoundedCorners(context.getResources().getDimensionPixelOffset(R.dimen.common_cover_round_size))
                )
                .into(imageView);

    }

    public static void loadImageWithBlurTransformationAll(Context context, String url, final ImageView imageView) {
        if (StringUtil.isEmpty(url)) {
            return;
        }
        StringBuffer sb = new StringBuffer(url);
        if (url.contains(ACCESS_URL)) {
            if (!url.contains("?")) {
                sb.append(PIC_PROCESSING);
            }
            sb.append("|imageView2/1/w/660/h/220");
        }
        GlideApp.with(context)
                .load(sb.toString())
                .dontTransform()
                .dontAnimate()
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o,
                                                Target<Drawable> target, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o,
                                                   Target<Drawable> target, DataSource dataSource, boolean b) {
                        imageView.setImageDrawable(drawable);
                        if (!b) {
                            imageView.setAlpha(0.F);
                            imageView.animate().alpha(1F).setDuration(500).start();
                        }
                        return true;
                    }
                })
                // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .transforms(new BlurTransformation(context, 5, 1))
                .into(imageView);
    }

    public static void loadCircleImage(Context context, String url, ImageView imageView, int defaultRes) {
        GlideApp.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .centerCrop()
                .transform(new CircleCrop())
                .error(defaultRes)
                .placeholder(defaultRes)
                .into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView, int defaultRes) {
        GlideApp.with(context).load(url)
                .dontAnimate()
                .centerCrop()
                .placeholder(defaultRes)
                .error(defaultRes)
                .into(imageView);
    }

    public static void loadImage(Context context, File file, ImageView imageView, int defaultRes) {
        GlideApp.with(context).load(file).dontAnimate().placeholder(defaultRes).into(imageView);
    }

    public static void loadImage(Context context, @DrawableRes int id, ImageView imageView) {
        GlideApp.with(context).asGif().load(id).into(imageView);
    }

    /**
     * 加载app资源图片方法
     */
    public static void loadImageRes(Context context, @DrawableRes int id, ImageView imageView, @DrawableRes int holderRes) {
        GlideApp.with(context).load(id).placeholder(holderRes).centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }

    /**
     * 加载app资源图片方法
     */
    public static void loadImageRes(Context context, @DrawableRes int id, ImageView imageView) {
        GlideApp.with(context).load(id).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }

    public static void loadImage(Context context, File file, ImageView imageView) {
        GlideApp.with(context).load(file).dontAnimate().into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        GlideApp.with(context).load(url).dontAnimate().into(imageView);
    }

    public static void loadImageTag(Context context, String url, ImageView imageView) {
        GlideApp.with(context).load(url).dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }

    public static void loadCarImage(Context context, String url, ImageView imageView) {
        GlideApp.with(context).asBitmap().load(url).dontAnimate().into(imageView);
    }

    /**
     * 可以设置错误图和占位图方法
     */
    public static void loadImage(Context context, String url, ImageView imageView, int holder, int error) {
        GlideApp.with(context).load(url).placeholder(holder).error(error).into(imageView);
    }

    /**
     * 可以设置错误图和占位图方法
     */
    public static void loadImages(Context context, String url, ImageView imageView, int holder, int error) {
        GlideApp.with(context).load(url).placeholder(holder).error(error).centerCrop().into(imageView);
    }

    /**
     * 加载本地gif图
     */
    public static void loadGifImage(Context context, @DrawableRes int id, ImageView imageView) {
        GlideApp.with(context).asGif().load(id).centerCrop().into(imageView);
    }

    public static void clearMemory(Context context) {
        if (SystemUtils.isMainThread()) {
            Glide.get(context).clearMemory();
        }
    }
}
