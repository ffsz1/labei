package com.tongdaxing.xchat_framework.http_image.image;

import android.graphics.Bitmap;

import com.tongdaxing.xchat_framework.http_image.http.HttpLog;
import com.tongdaxing.xchat_framework.util.util.ResolutionUtils;


/**
 * 图片显示设定
 *
 * @author zhongyongsheng on 14-6-18.
 */
public class ImageConfig {

    private ImagePrecision imagePrecision = ImagePrecision.MIDDLE;
    private ImageTransparency imageTransparency = ImageTransparency.RGB_565;
    private ImageIntercepter imageIntercepter = null;

    public ImageConfig(ImagePrecision imagePrecision, ImageTransparency imageTransparency) {
        this.imagePrecision = imagePrecision;
        this.imageTransparency = imageTransparency;
    }

    public ImageConfig(int imageWidth, int imageHeight) {
        this.imagePrecision = new ImagePrecision(imageWidth, imageHeight);
    }

    public ImageConfig(ImagePrecision imagePrecision,
                       ImageTransparency imageTransparency,
                       ImageIntercepter imageIntercepter) {
        this.imagePrecision = imagePrecision;
        this.imageTransparency = imageTransparency;
        this.imageIntercepter = imageIntercepter;
    }

    public ImagePrecision getImagePrecision() {
        return imagePrecision;
    }

    public ImageTransparency getImageTransparency() {
        return imageTransparency;
    }

    public ImageIntercepter getImageIntercepter() {
        return imageIntercepter;
    }

    /**
     * 显示精度
     */
    public static class ImagePrecision {
        public static final ImagePrecision FULL = new ImagePrecision(1.0f);
        public static final ImagePrecision BIG = new ImagePrecision(.5f);
        public static final ImagePrecision MIDDLE = new ImagePrecision(.3f);
        public static final ImagePrecision SMALL = new ImagePrecision(.1f);

        private float mPresisionRatio;
        private int mWidth;
        private int mHeight;

        public ImagePrecision(float presisionRatio) {
            this.mPresisionRatio = presisionRatio;
        }

        public ImagePrecision(int width, int height) {
            this.mWidth = width;
            this.mHeight = height;
        }

        public int getWidth() {
            if (mWidth > 0) return mWidth;
            try {
                mWidth = ResolutionUtils.getScreenWidth(ImageManager.instance().getContext());
                mWidth = (int) (mWidth * mPresisionRatio);
                HttpLog.v("Screen width %d", mWidth);
            } catch (Exception e) {
                mWidth = 300;
                HttpLog.e(e, "Screen width error, use default");
            }

            return mWidth;
        }

        public int getHeight() {
            if (mHeight > 0) return mHeight;
            try {
                mHeight = ResolutionUtils.getScreenHeight(ImageManager.instance().getContext());
                HttpLog.v("Screen height %d", mHeight);
                mHeight = (int) (mHeight * mPresisionRatio);
            } catch (Exception e) {
                mHeight = 300;
                HttpLog.e(e, "Screen height error, use default");
            }

            return mHeight;
        }
    }

    /**
     * 图片是否会透明
     */
    public static class ImageTransparency {
        public static final ImageTransparency RGB_565 = new ImageTransparency(Bitmap.Config.RGB_565);
        public static final ImageTransparency ARGB_8888 = new ImageTransparency(Bitmap.Config.ARGB_8888);

        private Bitmap.Config mConfig;

        public ImageTransparency(Bitmap.Config config) {
            this.mConfig = config;
        }

        public Bitmap.Config getBitmapConfig() {
            return mConfig;
        }
    }

    // ===================================  create setting  ================================================
    private static ImageConfig mDefaultImageConfig;
    private static ImageConfig mBigImageConfig;
    private static ImageConfig mSmallImageConfig;
    private static ImageConfig mFullImageConfig;

    /**
     * 一般图片大小,不透明
     *
     * @return
     */
    public static synchronized ImageConfig defaultImageConfig() {
        if (mDefaultImageConfig == null)
            mDefaultImageConfig = new ImageConfig(ImagePrecision.MIDDLE, ImageTransparency.RGB_565);
        return mDefaultImageConfig;
    }

    /**
     * 全屏大图片,不透明
     *
     * @return
     */
    public static synchronized ImageConfig bigImageConfig() {
        if (mBigImageConfig == null)
            mBigImageConfig = new ImageConfig(ImagePrecision.BIG, ImageTransparency.RGB_565);
        return mBigImageConfig;
    }

    /**
     * 小图标,头像大小,不透明
     *
     * @return
     */
    public static synchronized ImageConfig smallImageConfig() {
        if (mSmallImageConfig == null)
            mSmallImageConfig = new ImageConfig(ImagePrecision.SMALL, ImageTransparency.RGB_565);
        return mSmallImageConfig;
    }

    /**
     * 全屏幕尺寸,不透明
     *
     * @return
     */
    public static synchronized ImageConfig fullImageConfig() {
        if (mFullImageConfig == null)
            mFullImageConfig = new ImageConfig(ImagePrecision.FULL, ImageTransparency.RGB_565);
        return mFullImageConfig;
    }

}
