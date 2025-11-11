package com.tongdaxing.xchat_framework.http_image.http;

/**
 * http 处理进度监听器
 *
 * @author zhongyongsheng
 */
public interface ProgressListener {


    /**
     * 返回进度信息
     *
     * @param info 进度信息
     */
    public void onProgress(ProgressInfo info);
}
