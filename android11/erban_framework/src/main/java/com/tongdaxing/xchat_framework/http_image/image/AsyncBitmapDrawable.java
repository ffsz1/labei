package com.tongdaxing.xchat_framework.http_image.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.tongdaxing.xchat_framework.http_image.http.Request;

import java.lang.ref.WeakReference;

/**
 * 绑定request的drawable
 *
 * @author zhongyongsheng on 14-6-18.
 */
public class AsyncBitmapDrawable extends BitmapDrawable {

    private WeakReference<Request> bindedRequestRef;

    public AsyncBitmapDrawable(Resources res, Bitmap bitmap, Request bitmapWorkerTask) {
        super(res, bitmap);
        bindedRequestRef =  new WeakReference<Request>(bitmapWorkerTask);
    }

    public Request getBindedRequest() {
        return bindedRequestRef.get();
    }

}
