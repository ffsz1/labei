package com.tongdaxing.xchat_framework.http_image.image;

import android.graphics.Bitmap;

/**
 * 图片读取完后拦截处理
 *
 * @author zhongyongsheng on 14-10-13.
 */
public interface ImageIntercepter {

    /**
     * 请求完bitmap此拦截触发,用于对bitmap进行处理
     *
     * @param imageRequest 图片请求
     * @param bitmap       处理前的bitmap
     * @return 处理后的bitmap, 返回null则抛出RequestError, 终止此次请求, 回调RequestErrorListener
     */
    Bitmap onIntercept(ImageRequest imageRequest, Bitmap bitmap);

}
