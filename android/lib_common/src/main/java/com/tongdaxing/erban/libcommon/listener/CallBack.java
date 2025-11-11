package com.tongdaxing.erban.libcommon.listener;

/**
 * <p> 网络回调接口</p>
 *
 * @author jiahui
 * @date 2017/12/7
 */
public interface CallBack<T> {
    /**
     * 获取数据成功回调方法
     *
     * @param data 要返回的数据类型
     */
    void onSuccess(T data);

    /**
     * 获取数据失败回调方法
     *
     * @param code  错误码
     * @param error 失败的信息
     */
    void onFail(int code, String error);
}