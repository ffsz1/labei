package com.tongdaxing.xchat_framework.http_image.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.widget.ImageView;

import com.tongdaxing.xchat_framework.http_image.http.BytesQueryRequest;
import com.tongdaxing.xchat_framework.http_image.http.Cache;
import com.tongdaxing.xchat_framework.http_image.http.CacheCleanRequest;
import com.tongdaxing.xchat_framework.http_image.http.CacheShrinkRequest;
import com.tongdaxing.xchat_framework.http_image.http.DefaultRequestProcessor;
import com.tongdaxing.xchat_framework.http_image.http.DiskCache;
import com.tongdaxing.xchat_framework.http_image.http.HttpLog;
import com.tongdaxing.xchat_framework.http_image.http.ProgressListener;
import com.tongdaxing.xchat_framework.http_image.http.RequestError;
import com.tongdaxing.xchat_framework.http_image.http.RequestParam;
import com.tongdaxing.xchat_framework.http_image.http.RequestProcessor;
import com.tongdaxing.xchat_framework.http_image.http.ResponseErrorListener;
import com.tongdaxing.xchat_framework.http_image.http.ResponseListener;

/**
 * 图片显示管理器
 *
 * @author zhongyongsheng on 14-6-18.
 */
public class ImageManager {

    /**
     * 默认渐变动画时长
     */
    protected static final int FADE_IN_TIME = 200;
    protected static ImageManager mInstance;

    protected RequestProcessor mImageProcessor;
    protected RequestProcessor mBlurProcessor;

    protected Cache mCache;
    protected ImageCache mImageCache;
    protected Context mContext;
    protected boolean mFadeInBitmap = false;

    protected ImageManager() {
    }

    public static synchronized ImageManager instance() {
        if (mInstance == null) {
            mInstance = new ImageManager();
        }
        return mInstance;
    }

    public synchronized void init(Context context, String cacheDir) {
        mContext = context;

        mCache = new DiskCache(DiskCache.getCacheDir(context, cacheDir),
                60 * 1024 * 1024, 0.15f);
        mCache.initialize();

        mImageProcessor = new DefaultRequestProcessor(3, "Image_");
        mImageProcessor.start();

        mBlurProcessor = new ImageBlurProcessor();
        mBlurProcessor.start();

        mImageCache = new ImageCache(context);
    }

    Context getContext() {
        return mContext;
    }

    public RequestProcessor getProcessor() {
        return mImageProcessor;
    }

    public RequestProcessor getBlurProcessor() {
        return mBlurProcessor;
    }

    public Cache getCache() {
        return mCache;
    }

    public ImageCache getImageCache() {
        return mImageCache;
    }

    /**
     * 按ImageConfig的设置,从网络获取图片(有缓存则从缓存取),显示在RecycleImageView
     *
     * @param url
     * @param recycleImageView
     * @param imageConfig
     * @param loadingBitmapResource 加载中默认图
     */
    public void loadImage(String url,
                          RecycleImageView recycleImageView,
                          ImageConfig imageConfig,
                          int loadingBitmapResource) {
        loadImage(url, recycleImageView, imageConfig, loadingBitmapResource, 0);
    }

    public void loadImage(String url,
                          RecycleImageView recycleImageView,
                          ImageConfig imageConfig,
                          int loadingBitmapResource,
                          int blankBitmapResource) {
        loadImage(url, recycleImageView, imageConfig, loadingBitmapResource, blankBitmapResource, false);
    }

    public void loadBlurImage(String url,
                              RecycleImageView recycleImageView,
                              int loadingBitmapResource,
                              int blankBitmapResource) {

        ImageConfig config = new ImageConfig(ImageConfig.ImagePrecision.MIDDLE, ImageConfig.ImageTransparency.ARGB_8888);
        loadImage(url, recycleImageView, config, loadingBitmapResource, blankBitmapResource, true);
    }

    /**
     * 按ImageConfig的设置,从网络获取图片(有缓存则从缓存取),显示在RecycleImageView
     *
     * @param url
     * @param recycleImageView
     * @param imageConfig
     * @param loadingBitmapResource 加载中默认图
     * @param blankBitmapResource   图片为空时显示的图片
     * @param isBlur                是否加载模糊图片
     */
    public void loadImage(String url,
                          RecycleImageView recycleImageView,
                          ImageConfig imageConfig,
                          int loadingBitmapResource,
                          int blankBitmapResource,
                          boolean isBlur) {
        if (url == null || url.length() == 0 || isGif(url)) {
            /*BitmapDrawable blankDrawable = getBitmapDrawableFromResource(
                    imageConfig.getImagePrecision().getWidth(), imageConfig.getImagePrecision().getHeight()
                    , blankBitmapResource);
            recycleImageView.setImageDrawable(blankDrawable);*/
            recycleImageView.setImageResource(blankBitmapResource);
            return;
        }

        if (mImageCache != null && url.length() > 0) {
            String cacheKey = ImageRequest.getCacheKey(url, imageConfig.getImagePrecision().getWidth(),
                    imageConfig.getImagePrecision().getHeight(), isBlur);
            BitmapDrawable bd = mImageCache.getBitmapFromMemCache(cacheKey);
            if (bd != null) {
                recycleImageView.setImageDrawable(bd);
                return;
            }
        }

        BitmapDrawable loadingDrawable = getBitmapDrawableFromResource(
                imageConfig.getImagePrecision().getWidth(),
                imageConfig.getImagePrecision().getHeight(), loadingBitmapResource);

        Bitmap loadingBitmap = loadingDrawable == null ? null : loadingDrawable.getBitmap();
        final ImageRequest request;
        if (isBlur) {
            request = newBlurRequest(url, imageConfig, recycleImageView, loadingBitmap);
        } else {
            request = newRequest(url, imageConfig, recycleImageView, loadingBitmap);
        }

        final AsyncBitmapDrawable asyncDrawable = new AsyncBitmapDrawable(mContext.getResources(), loadingBitmap, request);
        recycleImageView.setImageDrawable(asyncDrawable);

        if (isBlur) {
            mBlurProcessor.add(request);
        } else {
            mImageProcessor.add(request);
        }
    }

    /**
     * 从网络获取图片(有缓存则从缓存取),显示在RecycleImageView
     *
     * @param url
     * @param recycleImageView
     * @param imageWidth            目标图片想显示的宽度
     * @param imageHeight           目标图片想显示的高度
     * @param loadingBitmapResource 加载中默认图
     */
    public void loadImage(String url,
                          RecycleImageView recycleImageView,
                          int imageWidth,
                          int imageHeight,
                          int loadingBitmapResource) {
        loadImage(url, recycleImageView, imageWidth, imageHeight, loadingBitmapResource, 0);
    }

    /**
     * 从网络获取图片(有缓存则从缓存取),显示在RecycleImageView
     *
     * @param url
     * @param recycleImageView
     * @param imageWidth            目标图片想显示的宽度
     * @param imageHeight           目标图片想显示的高度
     * @param loadingBitmapResource 加载中默认图
     * @param blankBitmapResource   图片为空时显示的图片
     */
    public void loadImage(String url,
                          RecycleImageView recycleImageView,
                          int imageWidth,
                          int imageHeight,
                          int loadingBitmapResource,
                          int blankBitmapResource) {
        loadImage(url, recycleImageView, new ImageConfig(imageWidth, imageHeight),
                loadingBitmapResource, blankBitmapResource);
    }

    /**
     * @param path
     * @param recycleImageView
     * @param imageConfig
     * @param loadingBitmapResource
     */
    public void loadImageFile(String path,
                              RecycleImageView recycleImageView,
                              ImageConfig imageConfig,
                              int loadingBitmapResource) {
        loadImageFile(path, recycleImageView, imageConfig.getImagePrecision().getWidth(),
                imageConfig.getImagePrecision().getHeight(), loadingBitmapResource);
    }

    /**
     * @param path
     * @param recycleImageView
     * @param imageWidth
     * @param imageHeight
     * @param loadingBitmapResource
     */
    public void loadImageFile(String path, RecycleImageView recycleImageView,
                              int imageWidth,
                              int imageHeight,
                              int loadingBitmapResource) {
        if (mImageCache != null && path != null && path.length() > 0) {
            BitmapDrawable bd = mImageCache.getBitmapFromMemCache(
                    ImageRequest.getCacheKey(path, imageWidth, imageHeight));
            if (bd != null) {
                recycleImageView.setImageDrawable(bd);
                return;
            }
        }

        BitmapDrawable loadingBitmapDrawable =
                getBitmapDrawableFromResource(imageWidth, imageHeight, loadingBitmapResource);
        recycleImageView.setImageDrawable(loadingBitmapDrawable);

        if (path == null || path.length() == 0) {
            return;
        }
        BitmapDrawable bitmapDrawable = new BitmapDrawable(mContext.getResources(),
                ImageRequest.decodeSampledBitmapFile(path, imageWidth, imageHeight, false));
        recycleImageView.setImageDrawable(bitmapDrawable);
    }

    public void loadImageResource(int resource, RecycleImageView recycleImageView,
                                  ImageConfig imageConfig) {
        BitmapDrawable resourceDrawable = getBitmapDrawableFromResource(imageConfig.getImagePrecision().getWidth(),
                imageConfig.getImagePrecision().getHeight(), resource);
        recycleImageView.setImageDrawable(resourceDrawable);
    }

    public void loadImageResource(String resource, RecycleImageView recycleImageView,
                                  ImageConfig imageConfig, int blankBitmapResource) {
        int resourceId = mContext.getResources().getIdentifier(resource, "drawable", mContext.getPackageName());
        if (resourceId == 0) {
            resourceId = blankBitmapResource;
        }
        BitmapDrawable resourceDrawable = getBitmapDrawableFromResource(imageConfig.getImagePrecision().getWidth(),
                imageConfig.getImagePrecision().getHeight(), resourceId);
        recycleImageView.setImageDrawable(resourceDrawable);
    }

    public BitmapDrawable getBitmapDrawableFromResource(int imageWidth,
                                                        int imageHeight,
                                                        int loadingBitmapResource) {
        BitmapDrawable loadingBitmapDrawable = mImageCache.getBitmapFromMemCache(
                String.valueOf(loadingBitmapResource));
        if (loadingBitmapDrawable == null) {
            if (loadingBitmapResource > 0) {
                Bitmap loadingBitmap = ImageRequest.decodeSampledBitmapFromResourceId(
                        mContext, loadingBitmapResource, imageWidth, imageHeight, null);
                HttpLog.v("GetBitmapDrawableFromResource Decode");
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                    loadingBitmapDrawable = new BitmapDrawable(mContext.getResources(), loadingBitmap);
                } else {
                    loadingBitmapDrawable = new RecycleBitmapDrawable(mContext.getResources(), loadingBitmap);
                }
                if (loadingBitmap != null)
                    mImageCache.addBitmapToCache(String.valueOf(loadingBitmapResource), loadingBitmapDrawable);
            }
        }
        return loadingBitmapDrawable;
    }

    public ImageRequest newRequest(final String url,
                                   final ImageConfig imageConfig,
                                   final Context context,
                                   final boolean shouldMemCache,
                                   ResponseListener<ImageResponse> successListener,
                                   ResponseErrorListener errorListener) {
        ImageRequest imageRequest = new ImageRequest(mCache,
                url,
                successListener,
                errorListener,
                imageConfig,
                null,
                context,
                shouldMemCache ? mImageCache : null
        );
        return imageRequest;
    }

    public ImageRequest newRequest(final String url,
                                      final ImageConfig imageConfig,
                                      final ImageView imageView,
                                      final Bitmap loadingBitmap) {
        ImageRequest imageRequest = new ImageRequest(mCache,
                url,
                new ResponseListener<ImageResponse>() {

                    @Override
                    public void onResponse(ImageResponse response) {
                        //HttpLog.d("Image response url = " + url);
                        final ImageView imageView = response.imageRequest.getAttachedImageView();
                        if (response != null && imageView != null) {
                            setImageDrawable(imageView, response.bitmapDrawable, loadingBitmap);
                        }
                    }

                },
                new ResponseErrorListener() {

                    @Override
                    public void onErrorResponse(RequestError error) {
                        HttpLog.e(error, "Image load error url " + url);
                    }
                },

                imageConfig,
                imageView,
                imageView.getContext(),
                mImageCache
        );
        return imageRequest;
    }

    public ImageBlurRequest newBlurRequest(final String url,
                                           final ImageConfig imageConfig,
                                           final ImageView imageView,
                                           final ResponseListener listener,
                                           final ResponseErrorListener errorListener) {
        ImageBlurRequest imageRequest = new ImageBlurRequest(mCache,
                url,
                listener,
                errorListener,
                imageConfig,
                imageView,
                imageView.getContext(),
                mImageCache
        );
        return imageRequest;
    }


    public ImageBlurRequest newBlurRequest(final String url,
                                   final ImageConfig imageConfig,
                                   final ImageView imageView,
                                   final Bitmap loadingBitmap) {
        ImageBlurRequest imageRequest = new ImageBlurRequest(mCache,
                url,
                new ResponseListener<ImageResponse>() {

                    @Override
                    public void onResponse(ImageResponse response) {
                        final ImageView imageView = response.imageRequest.getAttachedImageView();
                        if (response != null && imageView != null) {
                            setImageDrawable(imageView, response.bitmapDrawable, loadingBitmap);
                        }
                    }

                },
                new ResponseErrorListener() {

                    @Override
                    public void onErrorResponse(RequestError error) {
                        HttpLog.e(error, "Image load error url " + url);
                    }
                },

                imageConfig,
                imageView,
                imageView.getContext(),
                mImageCache
        );
        return imageRequest;
    }


    protected void setImageDrawable(ImageView imageView, Drawable drawable, Bitmap loadingBitmap) {
        if (mFadeInBitmap) {
            //实现alpha渐变切换效果
            final TransitionDrawable td =
                    new TransitionDrawable(new Drawable[]{
                            new BitmapDrawable(mContext.getResources(), loadingBitmap),
                            drawable
                    });

            imageView.setImageDrawable(td);
            td.startTransition(FADE_IN_TIME);
        } else {
            imageView.setImageDrawable(drawable);
        }
    }

    public void addCachedImage(String path, ImageConfig imageConfig, BitmapDrawable bitmapDrawable) {
        if (mImageCache != null && path != null && path.length() > 0) {
            mImageCache.addBitmapToCache(ImageRequest.getCacheKey(path,
                    imageConfig.getImagePrecision().getWidth(),
                    imageConfig.getImagePrecision().getHeight()), bitmapDrawable);
        }
    }

    public BitmapDrawable getCachedImage(String path, ImageConfig imageConfig) {
        if (mImageCache != null && path != null && path.length() > 0) {
            return mImageCache.getBitmapFromMemCache(
                    ImageRequest.getCacheKey(path,
                            imageConfig.getImagePrecision().getWidth(),
                            imageConfig.getImagePrecision().getHeight()));
        }
        return null;
    }

    /**
     * 清除缓存
     *
     * @param successListener
     * @param errorListener
     * @return
     */
    public CacheCleanRequest submitCacheCleanRequest(ResponseListener<Object> successListener,
                                                     ResponseErrorListener errorListener) {
        CacheCleanRequest req = new CacheCleanRequest(mCache, successListener, errorListener);
        mImageProcessor.add(req);
        return req;
    }

    public static boolean isGif(String url) {
        return (url != null && url.endsWith(".gif"));
    }

    public static boolean isPng(String url) {
        return (url != null && url.endsWith(".png"));
    }

    public static boolean isJpg(String url) {
        return (url != null && url.endsWith(".jpg"));
    }

    public void loadDrawable(final String url,
                             final RecycleImageView recycleImageView,
                             final ImageConfig imageConfig,
                             final int loadingBitmapResource,
                             final int blankBitmapResource,
                             final DrawableParser drawableParser) {
        loadDrawable(url, recycleImageView, imageConfig,
                getBitmapDrawableFromResource(imageConfig.getImagePrecision().getWidth(), imageConfig.getImagePrecision().getHeight(), loadingBitmapResource),
                getBitmapDrawableFromResource(imageConfig.getImagePrecision().getWidth(), imageConfig.getImagePrecision().getHeight(), blankBitmapResource),
                drawableParser);
    }

    public void loadDrawable(final String url,
                             final RecycleImageView recycleImageView,
                             final ImageConfig imageConfig,
                             final BitmapDrawable loadingDrawable,
                             final BitmapDrawable blankDrawable,
                             final DrawableParser drawableParser) {
        final int imageWidth = imageConfig.getImagePrecision().getWidth();
        final int imageHeight = imageConfig.getImagePrecision().getHeight();
        if (url == null || url.length() == 0) {
            recycleImageView.setImageDrawable(blankDrawable);
            return;
        }
        /*if (mImageCache != null && url != null && url.length() > 0) {
            BitmapDrawable bd = mImageCache.getBitmapFromMemCache(
                    ImageRequest.getCacheKey(url, imageWidth, imageHeight));
            if (bd != null)
            {
                recycleImageView.setImageDrawable(bd);
                return;
            }
        }*/

        Bitmap loadingBitmap = loadingDrawable == null ? null : loadingDrawable.getBitmap();
        final DrawableRequest request = newDrawableRequest(url, imageWidth, imageHeight, recycleImageView,
                loadingBitmap, drawableParser);
        final AsyncBitmapDrawable asyncDrawable =
                new AsyncBitmapDrawable(mContext.getResources(), loadingBitmap, request);
        recycleImageView.setImageDrawable(asyncDrawable);
        mImageProcessor.add(request);
    }

    protected DrawableRequest newDrawableRequest(final String url,
                                                 int width,
                                                 int height,
                                                 final ImageView imageView,
                                                 final Bitmap loadingBitmap,
                                                 final DrawableParser drawableParser) {
        DrawableRequest request = new DrawableRequest(mCache,
                url,
                new ResponseListener<DrawableResponse>() {

                    @Override
                    public void onResponse(DrawableResponse response) {
                        HttpLog.d("Drawable Request response url = " + url);
                        final ImageView imageView = response.request.getAttachedImageView();
                        if (response != null && imageView != null) {
                            imageView.setImageDrawable(response.drawable);
                        }
                    }

                },
                new ResponseErrorListener() {

                    @Override
                    public void onErrorResponse(RequestError error) {
                        HttpLog.e(error, "Drawable load error url " + url);
                    }
                },
                drawableParser,
                imageView
        );
        return request;
    }

    /**
     * 压缩缓存空间
     *
     * @param successListener
     * @param errorListener
     * @return
     */
    public CacheShrinkRequest submitCacheShrinkRequest(ResponseListener<Object> successListener,
                                                       ResponseErrorListener errorListener) {
        CacheShrinkRequest req = new CacheShrinkRequest(mCache, successListener, errorListener);
        mImageProcessor.add(req);
        return req;
    }

    public BitmapDrawable getBitmapFromMemCache(String path) {
        return mImageCache.getBitmapFromMemCache(path);
    }

    public void addBitmapToCache(String path, BitmapDrawable drawable) {
        mImageCache.addBitmapToCache(path, drawable);
    }

    public BytesQueryRequest submitBytesQueryRequest(String url,
                                                     RequestParam param,
                                                     ResponseListener<BytesQueryRequest.BytesWrapper> successListener,
                                                     ResponseErrorListener errorListener,
                                                     ProgressListener progressListener) {
        if (url == null
                || successListener == null
                || errorListener == null) return null;
        BytesQueryRequest req = new BytesQueryRequest(mCache, url, successListener,
                errorListener, progressListener);
        req.setNoExpire(param.getNoExpire());
        mImageProcessor.add(req);
        return req;
    }
}
