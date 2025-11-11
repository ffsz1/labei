package com.tongdaxing.erban.libcommon.net.rxnet.http;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by 11876 on 2017/11/25.
 */
public interface RetrofitService {

    @GET
    Call<ResponseBody> get(@Url String url,@QueryMap Map<String, Object> params);

    @GET
    Call<ResponseBody> getWithHeader(@Url String url, @HeaderMap Map<String, Object> headers, @QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> post(@Url String url, @FieldMap Map<String,Object> params);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> postWithHeader(@Url String url, @HeaderMap Map<String, Object> headers, @FieldMap Map<String,Object> params);



    @GET
    Call<ResponseBody> getDefault(@Url String url);

    /**
     *  get请求通用方法
     *  http://....?param1=...&param2=... 的多参数形式
     * @param params
     * @return
     */
    @GET
    Call<ResponseBody> getDefault(@Url String url, @QueryMap Map<String, Object> params);

    /**
     * post请求通用方法
     * Map的key作为表单的键
     */
    @POST
    @FormUrlEncoded
    Call<ResponseBody> postDefault(@Url String url, @FieldMap Map<String, Object> map);

    /**
     * Map的key作为表单的键
     */
    @POST
    @FormUrlEncoded
    Call<ResponseBody> postString(@Url String url, @FieldMap Map<String, String> map);

    @PATCH
    @FormUrlEncoded
    Call<ResponseBody> patDefault(@Url String url, @FieldMap Map<String, Object> map);

    @PATCH
    @FormUrlEncoded
    Call<ResponseBody> changePW(@Url String url, @FieldMap Map<String, Object> map);

    /**
     * delete 请求方式
     * @param url
     * @return
     */
    @DELETE
    Call<ResponseBody> delete(@Url String url);

    /**
     * Map的key作为表单的键
     */
    @PUT
    @FormUrlEncoded
    Call<ResponseBody> putDefault(@Url String url, @FieldMap Map<String, Object> map);


    /**
     *
     * @param url
     * @param file
     * @return
     */
    @Multipart
    @POST
    Call<ResponseBody> uploadFile(@Url String url, @Header("authorization") String contentRange, @Part MultipartBody.Part file);


    /**
     * 下载最新模板
     *
     * @return
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadImage(@Url String fileUrl);
}
