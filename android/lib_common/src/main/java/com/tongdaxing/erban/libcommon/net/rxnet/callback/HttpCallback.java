package com.tongdaxing.erban.libcommon.net.rxnet.callback;

import com.tongdaxing.xchat_framework.util.util.LogUtil;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class HttpCallback<T> implements Callback<ResponseBody> {

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            log(call,"http_url");
            dealWithResult(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        log(call,"http_url");
        LogUtil.d("http_response",t == null?"接口异常":t.getMessage());
    }

    private void dealWithResult(Response<ResponseBody> response) throws IOException {
        if (response != null && response.body() != null && response.isSuccessful()){
            LogUtil.d("http_response",response.body().string());
        }else {
            if (response == null) {
                dealWithError(10000,"数据异常");
                LogUtil.d("http_response","response is null");
            }else {
                if (response.body() != null) {
                    LogUtil.d("http_response",response.body().string());
                }else {
                    LogUtil.d("http_response", "response body is null");
                }
                dealWithError(response.code(), response.errorBody() == null?"数据异常":response.errorBody().string());
            }
        }
    }

    private void dealWithError(int code,String error){
        onFailure(error);
    }


    private void log(Call<ResponseBody> call,String tag){
        if (call != null && call.request() != null && call.request().url() != null)
            LogUtil.d(tag,call.request().url().toString());
    }

    public abstract void onFailure(String error);

    public abstract void onSuccess(T t);

}
