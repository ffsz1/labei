package com.tongdaxing.xchat_framework.okhttp;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 创建者     polo
 * 创建时间   2017/7/19 17:29
 * 描述
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述
 */

public class CookiesManager implements CookieJar {


    private final PersistentCookieStore mCookieStore;

    public CookiesManager(Context context) {

        mCookieStore = new PersistentCookieStore(context);
    }


    String mCookieStr = "";

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                //                String cookieStr = item.name() + "=" + item.value();
                //                                LogUtils.d("saveFromResponse", "name:" + item.name() + "   " + "value:" + item.value());
                mCookieStr = item.name() + "=" + item.value() + "; Domain=" + item.domain();
                //                MyApp.cookie_text = cookieStr;
                //                MyApp.has_cookie_text = true;

                mCookieStore.add(url, item);

            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = mCookieStore.get(url);

        return cookies;
    }


}


