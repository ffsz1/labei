package com.tongdaxing.erban.libcommon.net.rxnet;

import android.database.Observable;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


/**
 * Retrofit 封装使用的接口请求服务类
 */
public interface RxNetService {
    /**
     *  app 内部封装使用的监听方式
     *  Observable
     *
     * @param url
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<ResponseBody> post(@Url String url, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST
    Observable<ResponseBody> postAddHeader(@Url String url, @HeaderMap Map<String, String> headers, @FieldMap Map<String, String> params);


    @GET
    Observable<ResponseBody> get(@Url String url);

    @GET
    Observable<ResponseBody> get(@Url String url, @QueryMap Map<String, String> params);

    @GET
    Observable<ResponseBody> getAddHeader(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> params);


    /**
     *  GET
     * 可以使用retrofit默认OkHttp返回值方式的请求
     * .enqueue(new retrofit2.Callback<Response>()
     * @param url
     * @param headers
     * @param params
     * @return
     */
    @GET
    Call<ResponseBody> getCallAddHeader(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> params);


    /**
     *  POST
     * 可以使用retrofit默认OkHttp返回值方式的请求
     * .enqueue(new retrofit2.Callback<Response>()
     * @param url
     * @param headers
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST
    Call<ResponseBody> postCallAddHeader(@Url String url, @HeaderMap Map<String, String> headers, @FieldMap Map<String, String> params);

}
