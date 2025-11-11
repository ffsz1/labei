
package com.tongdaxing.erban.libcommon.net.rxnet.interceptor;


import com.tongdaxing.erban.libcommon.net.rxnet.model.HttpHeaders;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p> 添加头部参数 </p>
 *
 * @author jiahui
 *         date 2017/12/7
 */
public class HeaderInterceptor implements Interceptor {

    private HttpHeaders mHttpHeaders;

    public HeaderInterceptor(HttpHeaders httpHeaders) {
        mHttpHeaders = httpHeaders;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (mHttpHeaders == null || mHttpHeaders.isEmpty()) {
            return chain.proceed(builder.build());//FIXME 这里有内存泄漏，需要优化
        }
        for (Map.Entry<String, String> entry : mHttpHeaders.headersMap.entrySet()) {
            builder.header(entry.getKey(), entry.getValue());
        }
        return chain.proceed(builder.build());
    }
}
