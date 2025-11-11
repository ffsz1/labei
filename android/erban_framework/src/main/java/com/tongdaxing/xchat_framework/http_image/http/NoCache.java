package com.tongdaxing.xchat_framework.http_image.http;

/**
 * 不需要缓存的实现
 *
 * @author zhongyongsheng
 */
public class NoCache implements Cache {
    @Override
    public Entry get(String key) {
        return null;
    }

    @Override
    public void put(String key, Entry entry) {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void invalidate(String key, boolean fullExpire) {

    }

    @Override
    public void remove(String key) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void shrink() {

    }
}
