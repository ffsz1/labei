/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tongdaxing.xchat_framework.http_image.http;

/**
 * 返回给回调方法的对象封装
 *
 * @author zhongyongsheng
 */
public class Response<T> {

    public final T result;
    public final Cache.Entry cacheEntry;
    public final RequestError error;
    public boolean intermediate = false;
    public boolean notModified = false;

    private Response(T result, Cache.Entry cacheEntry) {
        this.result = result;
        this.cacheEntry = cacheEntry;
        this.error = null;
    }

    private Response(RequestError error) {
        this.result = null;
        this.cacheEntry = null;
        this.error = error;
    }

    public static <T> Response<T> success(T result, Cache.Entry cacheEntry) {
        return new Response<T>(result, cacheEntry);
    }

    public static <T> Response<T> error(RequestError error) {
        return new Response<T>(error);
    }

    public boolean isSuccess() {
        return error == null;
    }
}
