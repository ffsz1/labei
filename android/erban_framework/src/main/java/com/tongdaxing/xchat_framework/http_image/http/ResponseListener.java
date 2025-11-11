package com.tongdaxing.xchat_framework.http_image.http;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Http 请求成功的监听器
 *
 * @author zhongyongsheng
 */
public abstract class ResponseListener<T> {

   public Type mType;

    public ResponseListener() {
        mType = getSuperclassTypeParameter(getClass());
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    /**
     * 返回成功结果
     *
     * @param response 数据结果
     */
    public abstract void onResponse(T response);
}
