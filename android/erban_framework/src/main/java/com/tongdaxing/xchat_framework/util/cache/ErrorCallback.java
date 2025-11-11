package com.tongdaxing.xchat_framework.util.cache;

public interface ErrorCallback {

    /**
     * 错误回调处理
     *
     * @param e IOExcepiton,
     * @throws Exception
     */
    public void onError(CacheException e) throws Exception;
}
