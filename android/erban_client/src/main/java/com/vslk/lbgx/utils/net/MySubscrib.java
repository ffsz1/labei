package com.vslk.lbgx.utils.net;

import android.support.annotation.NonNull;

import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;


public abstract class MySubscrib<T> implements Observer<T> {

    private boolean showtoast;

    public MySubscrib() {
    }

    public MySubscrib(boolean showtoast) {
        this.showtoast = showtoast;
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
      //  LogUtil.loge("请求报错:"+e.getMessage());
        String msg="";
        if (e instanceof AlreadyOpenExeption){
            onAlreadyOpenRoom();
        }else if (e instanceof ServerException){
            if (e.getMessage()!=null){
                msg = e.getMessage();
            }
        }else if (e instanceof HttpException){
            msg="服务器内部错误"+((HttpException) e).code();
        }else {
            msg="网络错误,请稍后再试...";
        }
        onFailure(msg);
    }

    public void onAlreadyOpenRoom() {
    }


    @Override
    public void onNext(T t) {
        onSuccess(t);
    }


    public void onNullData() {
    }


    public void onFailure(String msg) {
        if (showtoast){
            SingleToastUtil.showToast(BasicConfig.INSTANCE.getAppContext(), msg);
        }
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onSubscribe(Disposable disposable) {
    }

    public abstract void onSuccess(@NonNull T data);


}