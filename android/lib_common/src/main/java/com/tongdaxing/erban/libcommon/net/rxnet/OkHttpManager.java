package com.tongdaxing.erban.libcommon.net.rxnet;


import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.tongdaxing.erban.libcommon.net.rxnet.callback.RetrofitCallback;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.LogUtil;
import com.tongdaxing.xchat_framework.util.util.SignUtils;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.yingtao.ndklib.JniUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


/**
 * 创建者     polo
 * 创建时间   2017/8/19 17:13
 * 描述	      ${}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${}
 */
public class OkHttpManager implements RetrofitCallback.RetrofitResponseCallback {

    private static OkHttpManager mInstance;
    private String kp = "";
    private Context context;
    private Gson mGson = new Gson();
    private IHostListener mHostListener;

    public interface IHostListener {
        void upHost();
    }

    private OkHttpManager() {
    }

    public void init(Context context, boolean isDebug, IHostListener mHostListener) {
        this.context = context;
        this.mHostListener = mHostListener;
        kp = JniUtils.getDk(context, isDebug ? 1 : 0);
//        Log.e("aesokhttp aes", "kp:"+kp+"ak:"+JniUtils.getAk(context) + " iv:" + JniUtils.getAkIv(context));
//        Log.e("aesokhttp sn", kp);
//        kp = "29ce0ce8c125f2c39fc35ac1a3e4fb51";
    }

    public static OkHttpManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 最新的请求方式 - 签名头
     *
     * @param url
     * @param params
     * @return
     */
    private Map<String, String> getSignHeader(String url, Map<String, String> headers, Map<String, String> params) {
        LogUtil.d("request_info", "url-->>\n" + url);
        //打印日志
        if (headers != null) {
            StringBuilder headersBuilder = new StringBuilder();
            for (Map.Entry<String, String> p : headers.entrySet()) {
                headersBuilder.append(p.getKey()).append("=").append(p.getValue()).append("  ");
            }
            LogUtil.d("request_info", "header-->>\n" + headersBuilder.toString());
        }
        if (params != null) {
            StringBuilder paramsBuilder = new StringBuilder();
            for (Map.Entry<String, String> p : params.entrySet()) {
                paramsBuilder.append(p.getKey()).append("=").append(p.getValue()).append("&");
            }
            LogUtil.d("request_info", "body-->>\n" + paramsBuilder.substring(0, paramsBuilder.length() - 1));
        }
        if (headers == null) {
            headers = new HashMap<>();
        }
        String time = System.currentTimeMillis() + "";
        //加上签名
        headers.put("t", time);
        String sign = SignUtils.getSign(url, params, kp, time);
        headers.put("sn", sign);
        return headers;
    }


    /**
     * GET请求
     *
     * @param url
     * @param params
     * @param myCallBack
     */
    public void getRequest(String url, Map<String, String> params, final MyCallBack myCallBack) {
        getRequest(url, null, params, myCallBack);
    }

    /**
     * GET请求 - 带有请求头的
     *
     * @param url
     * @param params
     * @param myCallBack
     */
    public void getRequest(String url, Map<String, String> headers, Map<String, String> params, final MyCallBack myCallBack) {
        dealParamEmptyEx(params);
        Map<String, String> sgHeader = getSignHeader(url, headers, params);
        try {
            encryptParams(params);
        } catch (Exception e) {
            e.printStackTrace();
            myCallBack.onError(new Exception("请求失败"));
        }
        RxNet.create(RxNetService.class).getCallAddHeader(url, sgHeader, params).enqueue(new RetrofitCallback<ResponseBody>(myCallBack, this));
    }

    /**
     * post请求
     *
     * @param url
     * @param params
     * @param myCallBack
     */
    public void doPostRequest(String url, Map<String, String> params, final MyCallBack myCallBack) {
        doPostRequest(url, null, params, myCallBack);
    }


    /**
     * post请求 - 带有请求头的
     *
     * @param url
     * @param params
     * @param myCallBack
     */
    public void doPostRequest(String url, Map<String, String> headers, Map<String, String> params, final MyCallBack myCallBack) {
        dealParamEmptyEx(params);
        Map<String, String> sgHeader = getSignHeader(url, headers, params);
        try {
            encryptParams(params);
        } catch (Exception e) {
            e.printStackTrace();
            myCallBack.onError(new Exception("请求失败"));
        }
        RxNet.create(RxNetService.class).postCallAddHeader(url, sgHeader, params).enqueue(new RetrofitCallback<ResponseBody>(myCallBack, this));
    }


    /**
     * 处理返回结果
     *
     * @param myCallBack
     */
    private void dealResponseResult(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response, MyCallBack myCallBack) {
        if (myCallBack == null) {
            return;
        }
        if (call.request() != null && call.request().url() != null) {
            LogUtil.d("request_info", "code-->>" + String.valueOf(response.code()) + "\nurl-->>" + call.request().url().toString());
        }
        String string = "";
        if (response.body() != null) {
            try {
                string = decryptParams(response.body());
//                string = response.body().string();
                LogUtil.d("request_info", "response_body-->>\n" + string);
            } catch (Exception e) {
                myCallBack.onError(e);
            }
            Object o = null;
            if (myCallBack.mType.toString().equals(Json.class.toString())) {
                o = Json.parse(string);
            } else {
                try {
                    o = mGson.fromJson(string, myCallBack.mType);
                } catch (Exception e) {
                    e.printStackTrace();
                    myCallBack.onError(new Exception("数据异常，请稍后重试！"));
                }
            }
            if (o != null) {
                try {
                    myCallBack.onResponse(o);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                myCallBack.onError(new Exception("数据异常，请稍后重试！"));
            }
        } else {
            if (response.errorBody() != null) {
                try {
                    string = response.errorBody().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    myCallBack.onError(new Exception("数据异常，请稍后重试！"));
                }
                myCallBack.onError(new Exception(string));
            } else {
                myCallBack.onError(new Exception("数据异常，请稍后重试！"));
            }
        }
    }

    /**
     * 处理错误
     *
     * @param call
     * @param t
     * @param myCallBack
     */
    private void dealErrorResult(retrofit2.Call<ResponseBody> call, Throwable t, MyCallBack myCallBack) {
        //替换域名
//        Log.e("---------------", t.getMessage().contains("Unable to resolve host") + ",t:" + t.getMessage() + ",\nt2:" + t.getLocalizedMessage());
//        String str = t.getMessage().replace(" ", "");
//        if (str.contains("Unable to resolve host")) {
//            mHostListener.upHost();
//        }
        if (call.request() != null && call.request().url() != null) {
            LogUtil.d("request_info_url", call.request().url().toString());
        }
        try {
            myCallBack.onError(new Exception(t.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call call, Response response, MyCallBack myCallBack) {
        dealResponseResult(call, response, myCallBack);
    }

    @Override
    public void onFailure(Call call, Throwable t, MyCallBack myCallBack) {
        dealErrorResult(call, t, myCallBack);
    }

    public static abstract class MyCallBack<T> {
        Type mType;

        public MyCallBack() {
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

        public abstract void onError(Exception e);

        public abstract void onResponse(T response);
    }

    /**
     * 处理参数为null的时候导致的retrofit的异常崩溃
     *
     * @param params
     */
    private void dealParamEmptyEx(Map<String, String> params) {
        if (params != null) {
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                if (StringUtils.isEmpty(params.get(key))) {
                    params.put(key, "");
                }
            }
        }
    }

    private void encryptParams(Map<String, String> params) throws Exception {
        if (AppUtils.isAppDebug()) {
            return;
        }

        if (params != null && !params.isEmpty()) {
            StringBuilder paramsBuilder = new StringBuilder();
            for (Map.Entry<String, String> param : params.entrySet()) {
                paramsBuilder.append(param.getKey()).append("=").append(URLEncoder.encode(param.getValue(), "utf-8")).append("&");
            }
            //去掉最后一个&（最后一个必然会是&）
            String paramsStr = paramsBuilder.substring(0, paramsBuilder.length() - 1);
            LogUtil.d("request_info", "pre_encrypt_body-->>\n" + paramsStr);
            params.clear();
            //只传加密后参数ed
            Log.d("aesokhttp aes", JniUtils.getAk(context) + " iv:" + JniUtils.getAkIv(context));
            ;
            params.put("ed", JniUtils.encryptAes(context, paramsStr));
        }
    }

    private String decryptParams(ResponseBody data) throws Exception {
        String bodyStr;
        Json bodyJson;
        try {
            bodyStr = data.string();
            LogUtil.d("request_info", "pre_decrypt_body-->>\n" + bodyStr);
            bodyJson = Json.parse(bodyStr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("数据异常，请稍后重试！");
        }
        String string;
        if (bodyJson == null || !bodyJson.has("ed")) {
            string = bodyStr;
        } else {
            try {
                string = JniUtils.decryptAes(context, bodyJson.getString("ed"));
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("数据异常，请稍后重试！");
            }
        }
        return string;
    }

}