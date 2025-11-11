package com.tongdaxing.erban.libcommon.net.rxnet.http;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by 11876 on 2017/11/26.
 */
public interface HttpRequstListener {
    void onSuccessfulCallback(int httpWhat, Call<ResponseBody> call, boolean isError, String response);
    void onFailureCallback(int httpWhat, Call<ResponseBody> call, Throwable t);
}
