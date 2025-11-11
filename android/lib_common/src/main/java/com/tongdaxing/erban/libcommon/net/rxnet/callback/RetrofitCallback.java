package com.tongdaxing.erban.libcommon.net.rxnet.callback;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 根据返回结果处理回调
 * @param <T>
 */
public class RetrofitCallback<T> implements Callback<T> {
    private OkHttpManager.MyCallBack myCallBack;
    private RetrofitResponseCallback retrofitResponseCallback;

    public RetrofitCallback(OkHttpManager.MyCallBack myCallBack,RetrofitResponseCallback retrofitResponseCallback) {
        this.myCallBack = myCallBack;
        this.retrofitResponseCallback = retrofitResponseCallback;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (retrofitResponseCallback != null){
            retrofitResponseCallback.onResponse(call,response,myCallBack);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (retrofitResponseCallback != null){
            retrofitResponseCallback.onFailure(call,t,myCallBack);
        }
    }

    public interface RetrofitResponseCallback<T>{
        void onResponse(Call<T> call, Response<T> response, OkHttpManager.MyCallBack myCallBack);
        void onFailure(Call<T> call, Throwable t, OkHttpManager.MyCallBack myCallBack);
    }
}
