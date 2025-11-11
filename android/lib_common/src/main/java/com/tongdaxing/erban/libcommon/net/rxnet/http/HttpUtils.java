package com.tongdaxing.erban.libcommon.net.rxnet.http;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 网络请求工具类
 * Created by ZWK on 2017/11/24.
 */
public class HttpUtils {
    public static final String BASE_URL = "https://api.yooyuu.com.cn/";
    public static final String TEST_URL = "http://192.168.35.200:4000/";
    private final String TAG = "http";
    private static HttpUtils instance;
    private boolean isTest = false;
    private OkHttpClient okHttpClient;
    private final int TIME_OUT = 10;

    private HttpUtils() {
    }

    public boolean isTest() {
        return true;
    }

    public static HttpUtils getInstance() {
        if (null == instance) {
            synchronized (HttpUtils.class) {
                instance = new HttpUtils();
            }
        }
        return instance;
    }

    private void init() {
        if (okHttpClient == null)
            //初始化OkHttp
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .build();
    }

    private RetrofitService getRetrofitService(){
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(isTest?TEST_URL:BASE_URL)
                .build();
        return retrofit.create(RetrofitService.class);
    }

    /**
     * get 拼接参数？params1=...&params2=...
     *
     * @param url
     * @param httpWhat
     * @param params
     * @param httpRequstListener
     */
    public void get(String url, final int httpWhat, Map<String, Object> params, final HttpRequstListener httpRequstListener) {
        init();
        getRetrofitService().getDefault(url, params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (httpRequstListener != null)
                    dealWithResponseCallback(httpWhat, call, response, httpRequstListener);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (httpRequstListener != null)
                    httpRequstListener.onFailureCallback(httpWhat, call, t);
            }
        });
    }

    /**
     * post 提交参数
     *
     * @param url
     * @param httpWhat
     * @param params
     * @param httpRequstListener
     */
    public void post(String url, final int httpWhat, Map<String, Object> params, final HttpRequstListener httpRequstListener) {
        init();
        getRetrofitService().postDefault(url, params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (httpRequstListener != null)
                    dealWithResponseCallback(httpWhat, call, response, httpRequstListener);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (httpRequstListener != null)
                    httpRequstListener.onFailureCallback(httpWhat, call, t);
            }
        });
    }

    /**
     * post 提交参数
     *
     * @param url
     * @param httpWhat
     * @param params
     * @param httpRequstListener
     */
    public void postString(String url, final int httpWhat, Map<String, String> params, final HttpRequstListener httpRequstListener) {
        init();
        getRetrofitService().postString(url, params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (httpRequstListener != null)
                    dealWithResponseCallback(httpWhat, call, response, httpRequstListener);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (httpRequstListener != null)
                    httpRequstListener.onFailureCallback(httpWhat, call, t);
            }
        });
    }


    /**
     * pat 修改密码特殊请求.
     *
     * @param url
     * @param httpWhat
     * @param params
     * @param httpRequstListener
     */
    public void patPw(String url, final int httpWhat, Map<String, Object> params, final HttpRequstListener httpRequstListener) {
        init();
        getRetrofitService().changePW(url, params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (httpRequstListener != null)
                    dealWithResponseCallback(httpWhat, call, response, httpRequstListener);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (httpRequstListener != null)
                    httpRequstListener.onFailureCallback(httpWhat, call, t);
            }
        });
    }

    /**
     * get 拼接参数？params1=...&params2=...
     *
     * @param url
     * @param httpWhat
     * @param params
     * @param httpRequstListener
     */
    public void put(String url, final int httpWhat, Map<String, Object> params, final HttpRequstListener httpRequstListener) {
        init();
        getRetrofitService().putDefault(url, params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (httpRequstListener != null)
                    dealWithResponseCallback(httpWhat, call, response, httpRequstListener);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (httpRequstListener != null)
                    httpRequstListener.onFailureCallback(httpWhat, call, t);
            }
        });
    }

    /**
     * 上传图片
     * @param url
     * @param imgPath
     * @param httpWhat
     * @param header
     * @param httpRequstListener
     */
    public void uploadImage(String url,String imgPath,final int httpWhat,String header,final HttpRequstListener httpRequstListener){
        //Create Upload Server Client
//        ApiService service = RetroClient.getApiService();
        //File creating from selected URL
        init();
        File file = new File(imgPath);
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        getRetrofitService().uploadFile(url,header,body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (httpRequstListener != null)
                    dealWithResponseCallback(httpWhat, call, response, httpRequstListener);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (httpRequstListener != null)
                    httpRequstListener.onFailureCallback(httpWhat, call, t);
            }
        });;
    }

    /**
     * 下载图片
     * @param url
     * @param httpWhat
     * @param callback
     */
    public void downloadImage(String url,final int httpWhat,Callback<ResponseBody> callback){
        getRetrofitService().downloadImage(url).enqueue(callback);;
    }

    /**
     * pat 修改密码特殊请求.
     *
     * @param url
     * @param httpWhat
//     * @param params
     * @param httpRequstListener
     */
    public void deletePraise(String url, final int httpWhat,final HttpRequstListener httpRequstListener) {
        init();
        getRetrofitService().delete(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (httpRequstListener != null)
                    dealWithResponseCallback(httpWhat, call, response, httpRequstListener);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (httpRequstListener != null)
                    httpRequstListener.onFailureCallback(httpWhat, call, t);
            }
        });
    }

    private void dealWithResponseCallback(int httpWhat, Call<ResponseBody> call, Response<ResponseBody> response, HttpRequstListener httpRequstListener) {
        try {
            if (response != null) {
                if (response.body() != null) {//返回结果正常
                    httpRequstListener.onSuccessfulCallback(httpWhat, call, false, response.body().string());
                } else {
                    if (response.errorBody() != null) {//返回异常结果
                        httpRequstListener.onSuccessfulCallback(httpWhat, call, true, response.errorBody().string());
                    }
                }
            } else {//请求失败
                httpRequstListener.onFailureCallback(httpWhat, call, new Throwable("返回结果为空"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpRequstListener.onFailureCallback(httpWhat, call, new Throwable("返回结果异常"));
        }
    }

    public String getUrlHost(){
        return isTest?TEST_URL:BASE_URL;
    }



    private void http_get(String url, HashMap<String,Object> params){
        getRetrofitService().get(url,params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (call != null && call.request() !)
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
