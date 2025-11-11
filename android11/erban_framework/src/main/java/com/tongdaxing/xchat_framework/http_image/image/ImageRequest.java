package com.tongdaxing.xchat_framework.http_image.image;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.widget.ImageView;

import com.tongdaxing.xchat_framework.http_image.http.BaseHttpClient;
import com.tongdaxing.xchat_framework.http_image.http.BaseRequest;
import com.tongdaxing.xchat_framework.http_image.http.Cache;
import com.tongdaxing.xchat_framework.http_image.http.DefaultRetryPolicy;
import com.tongdaxing.xchat_framework.http_image.http.HttpHeaderParser;
import com.tongdaxing.xchat_framework.http_image.http.HttpLog;
import com.tongdaxing.xchat_framework.http_image.http.Request;
import com.tongdaxing.xchat_framework.http_image.http.RequestError;
import com.tongdaxing.xchat_framework.http_image.http.Response;
import com.tongdaxing.xchat_framework.http_image.http.ResponseData;
import com.tongdaxing.xchat_framework.http_image.http.ResponseErrorListener;
import com.tongdaxing.xchat_framework.http_image.http.ResponseListener;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * 图片显示请求
 *
 * @author zhongyongsheng on 14-6-15.
 */
public class ImageRequest<ImageResponse> extends BaseRequest {

    private static final String TAG = ImageRequest.class.getSimpleName();

    private static final int IMAGE_TIMEOUT_MS = BaseHttpClient.SOCKET_TIMEOUT;
    private static final int IMAGE_MAX_RETRIES = 2;
    private static final float IMAGE_BACKOFF_MULT = 1.0f;

    protected Context mContext;
    protected int mImageWidth;
    protected int mImageHeight;
    protected WeakReference<ImageView> mImageViewReference;
    protected ImageCache mMemCache;
    protected ImageConfig mImageConfig;

    public ImageRequest(Cache cache,
                        String url,
                        ResponseListener successListener,
                        ResponseErrorListener errorListener,
                        ImageConfig imageConfig,
                        ImageView imageView,
                        Context context,
                        ImageCache memCache) {
        super(cache, url, successListener, errorListener);
        mImageWidth = imageConfig.getImagePrecision().getWidth();
        mImageHeight = imageConfig.getImagePrecision().getHeight();
        mImageConfig = imageConfig;
        mImageViewReference = new WeakReference<ImageView>(imageView);
        mContext = context;
        mMemCache = memCache;
        setRetryPolicy(
                new DefaultRetryPolicy(IMAGE_TIMEOUT_MS, IMAGE_MAX_RETRIES, IMAGE_BACKOFF_MULT));
    }

    @Override
    public void parseDataToResponse(ResponseData responseData) {
        Bitmap bitmap = null;
        byte[] data = responseData.data;
        if (data != null && data.length > 0) {
            try {
                bitmap = decodeSampledBitmapFromByteArray(
                        data, mImageWidth, mImageHeight, mMemCache, mImageConfig);

                if (mImageConfig != null && mImageConfig.getImageIntercepter() != null) {
                    if ((bitmap = mImageConfig.getImageIntercepter().onIntercept(this, bitmap)) == null) {
                        throw new RequestError("ImageIntercepter return false, Image request cancel.");
                    }
                }
            } catch (Exception e) {
                HttpLog.e(e, "Decode bitmap error.");
            } catch (OutOfMemoryError oom) {
                HttpLog.e(oom, "Decode bitmap oom.");
                System.gc();
            }
        }

        BitmapDrawable drawable = null;
        if (bitmap != null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                drawable = new BitmapDrawable(mContext.getResources(), bitmap);
            } else {
                drawable = new RecycleBitmapDrawable(mContext.getResources(), bitmap);
            }

            if (mMemCache != null) {
                mMemCache.addBitmapToCache(getKey(), drawable);
            }
        }

        com.tongdaxing.xchat_framework.http_image.image.ImageResponse response = new com.tongdaxing.xchat_framework.http_image.image.ImageResponse();
        response.bitmapDrawable = drawable;
        response.imageRequest = this;

        mResponse = Response.success(response, HttpHeaderParser.parseCacheHeaders(responseData));
    }



    @Override
    public String getKey() {
        return getCacheKey(mUrl, mImageWidth, mImageHeight);
    }

    public static String getCacheKey(String url, int width, int height) {
        return getCacheKey(url, width, height, false);
    }

    public static String getCacheKey(String url, int width, int height, boolean isBlur) {
        String key = new StringBuilder(url.length() + 12).append("#W").append(width)
                .append("#H").append(height).append(url).toString();
        if (isBlur) {
            key = key.concat("#BLUR");
        }

        return key;
    }

    public static Bitmap decodeSampledBitmapFromByteArray(
            byte[] bytes, int reqWidth, int reqHeight, ImageCache cache, ImageConfig imageConfig) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = imageConfig != null ?
                imageConfig.getImageTransparency().getBitmapConfig() :
                Bitmap.Config.RGB_565;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;

//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
//            addInBitmapOptions(options, cache);
//        }

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    public static Bitmap decodeSampledBitmapFromByteArray(
            byte[] bytes, ImageConfig imageConfig) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = imageConfig != null ?
                imageConfig.getImageTransparency().getBitmapConfig() :
                Bitmap.Config.RGB_565;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        options.inSampleSize = imageConfig != null ? calculateInSampleSize(options, imageConfig.getImagePrecision().getWidth(),
                imageConfig.getImagePrecision().getHeight()) : 1;

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    /**
     * decode resourceId到Bitmap
     *
     * @param context
     * @param resourceId
     * @param config
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResourceId(Context context, int resourceId, ImageConfig config) {
        return decodeSampledBitmapFromResourceId(context, resourceId, config.getImagePrecision().getWidth(),
                config.getImagePrecision().getHeight(), config);
    }

    /**
     * decode resourceId到Bitmap
     *
     * @param context
     * @param resourceId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResourceId(
            Context context, int resourceId, int reqWidth, int reqHeight, ImageConfig imageConfig) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = imageConfig != null ?
                imageConfig.getImageTransparency().getBitmapConfig() :
                Bitmap.Config.RGB_565;
        BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;

        try {
            return BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        } catch (OutOfMemoryError e) {
            HttpLog.e(e, "decodeSampledBitmapFromResourceId oom.");
            System.gc();
            return null;
        }
    }

    /**
     * decode path到Bitmap
     *
     * @param path
     * @param config
     * @return
     */
    public static Bitmap decodeSampledBitmapFile(String path, ImageConfig config) {
        return decodeSampledBitmapFile(path, config, false);
    }

    /**
     * decode path到Bitmap
     *
     * @param path
     * @param config
     * @param rorate
     * @return
     */
    public static Bitmap decodeSampledBitmapFile(String path, ImageConfig config, boolean rorate) {
        return decodeSampledBitmapFile(path, config.getImagePrecision().getWidth(),
                config.getImagePrecision().getHeight(), false);
    }

    /**
     * decode path到Bitmap
     *
     * @param path
     * @param reqWidth
     * @param reqHeight
     * @param rorate
     * @return
     */
    public static Bitmap decodeSampledBitmapFile(String path, int reqWidth, int reqHeight, boolean rorate) {
        if (path == null || path.length() == 0) {
            MLog.warn("ImageRequest", "DecodeSampledBitmapFile path is empty");
            return null;
        }

        File file = new File(path);
        if (!file.exists()) {
            MLog.warn("ImageRequest", "DecodeSampledBitmapFile file not exists");
            return null;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(file.getPath(), options);
        } catch (OutOfMemoryError e) {
            MLog.error("ImageRequest", "Decode file oom.", e);
            System.gc();
        }
        if (rorate)
            bitmap = rorateBitmap(path, bitmap);
        return bitmap;
    }

    /**
     * 旋转bitmap
     *
     * @param filePath
     * @param bitmap
     * @return
     */
    public static Bitmap rorateBitmap(String filePath, Bitmap bitmap) {
        Bitmap resizedBitmap = null;
        try {
            ExifInterface exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix matrix = new Matrix();
            int rotate = 0;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    rotate = 45;
                    break;
                default:
                    break;
            }

            matrix.postRotate(rotate);

            /**
             * recreate the new Bitmap
             */
            resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        } catch (Exception e) {
            MLog.error("xuwakao", "xuwakao, rorateBitmap e  = " + e);
            return bitmap;
        }
        return resizedBitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            final float totalPixels = width * height;

            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        //HttpLog.v("Sample size is %d", inSampleSize);
        return inSampleSize;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void addInBitmapOptions(BitmapFactory.Options options, ImageCache cache) {
        options.inMutable = true;
        if (cache != null) {
            Bitmap inBitmap = cache.getBitmapFromReusableSet(options);

            if (inBitmap != null) {
                MLog.verbose(TAG, "Found bitmap to use for inBitmap");
                options.inBitmap = inBitmap;
            }
        }
    }

    public ImageView getAttachedImageView() {
        final ImageView imageView = mImageViewReference.get();
        final ImageRequest bindedRequest = getBindedRequest(imageView);

        if (this == bindedRequest) {
            return imageView;
        }

        return null;
    }

    public static ImageRequest getBindedRequest(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncBitmapDrawable) {
                final AsyncBitmapDrawable asyncDrawable = (AsyncBitmapDrawable) drawable;
                Request request = asyncDrawable.getBindedRequest();
                if (request instanceof ImageRequest) {
                    return (ImageRequest) request;
                }
            }
        }
        return null;
    }

}
