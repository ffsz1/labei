package com.tongdaxing.xchat_framework.http_image.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.tongdaxing.xchat_framework.http_image.http.BaseRequest;
import com.tongdaxing.xchat_framework.http_image.http.Cache;
import com.tongdaxing.xchat_framework.http_image.http.DefaultRetryPolicy;
import com.tongdaxing.xchat_framework.http_image.http.HttpHeaderParser;
import com.tongdaxing.xchat_framework.http_image.http.HttpLog;
import com.tongdaxing.xchat_framework.http_image.http.Request;
import com.tongdaxing.xchat_framework.http_image.http.Response;
import com.tongdaxing.xchat_framework.http_image.http.ResponseData;
import com.tongdaxing.xchat_framework.http_image.http.ResponseErrorListener;
import com.tongdaxing.xchat_framework.http_image.http.ResponseListener;

import java.lang.ref.WeakReference;

/**
 * Drawable显示请求
 *
 * @author zhongyongsheng on 14-9-11.
 */
public class DrawableRequest<DrawableResponse> extends BaseRequest {

    private static final int IMAGE_TIMEOUT_MS = 3000;
    private static final int IMAGE_MAX_RETRIES = 2;
    private static final float IMAGE_BACKOFF_MULT = 0.7f;

    private Context mContext;
    private WeakReference<ImageView> mImageViewReference;
    private ImageCache mMemCache;
    protected DrawableParser mDrawableParser;

    public DrawableRequest(Cache cache, String url, ResponseListener successListener,
                           ResponseErrorListener errorListener, DrawableParser drawableParser,
                           ImageView imageView) {
        super(cache, url, successListener, errorListener);
        mDrawableParser = drawableParser;
        mImageViewReference = new WeakReference<ImageView>(imageView);
        mContext = imageView.getContext();
        setRetryPolicy(new DefaultRetryPolicy(IMAGE_TIMEOUT_MS, IMAGE_MAX_RETRIES, IMAGE_BACKOFF_MULT));
    }

    @Override
    public void parseDataToResponse(ResponseData responseData) {
        HttpLog.v("GifRequest parse data");
        Drawable drawable = null;
        String filePath = null;

        try {
            if (mDrawableParser != null) {
                drawable = mDrawableParser.parse(responseData.data);
            }
        } catch (Exception e) {
            HttpLog.e(e, "Drawable parser error file path + " + filePath);
        }

        com.tongdaxing.xchat_framework.http_image.image.DrawableResponse drawableResponse = new com.tongdaxing.xchat_framework.http_image.image.DrawableResponse();
        drawableResponse.request = this;
        drawableResponse.drawable = drawable;
        mResponse = Response.success(drawableResponse, HttpHeaderParser.parseCacheHeaders(responseData));
    }

    public ImageView getAttachedImageView() {
        final ImageView imageView = mImageViewReference.get();
        final DrawableRequest bindedRequest = getBindedRequest(imageView);

        if (this == bindedRequest) {
            return imageView;
        }

        return null;
    }

    public static DrawableRequest getBindedRequest(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncBitmapDrawable) {
                final AsyncBitmapDrawable asyncDrawable = (AsyncBitmapDrawable) drawable;
                Request request = asyncDrawable.getBindedRequest();
                if (request instanceof DrawableRequest) {
                    return (DrawableRequest) request;
                }
            }
        }
        return null;
    }
}
