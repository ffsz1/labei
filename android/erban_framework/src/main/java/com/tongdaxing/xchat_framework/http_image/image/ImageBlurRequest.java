package com.tongdaxing.xchat_framework.http_image.image;

import android.content.Context;
import android.widget.ImageView;

import com.tongdaxing.xchat_framework.http_image.http.Cache;
import com.tongdaxing.xchat_framework.http_image.http.ResponseErrorListener;
import com.tongdaxing.xchat_framework.http_image.http.ResponseListener;


/**
 * Created by lijun on 2015/4/24.
 */
public class ImageBlurRequest extends ImageRequest {

    public ImageBlurRequest(Cache cache, String url, ResponseListener successListener, ResponseErrorListener errorListener, ImageConfig imageConfig, ImageView imageView, Context context, ImageCache memCache) {
        super(cache, url, successListener, errorListener, imageConfig, imageView, context, memCache);
    }

    @Override
    public String getKey() {
        return getCacheKey(mUrl, mImageWidth, mImageHeight, true);
    }
}
