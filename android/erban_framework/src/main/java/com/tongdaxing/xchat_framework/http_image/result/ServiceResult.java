package com.tongdaxing.xchat_framework.http_image.result;

import java.io.Serializable;

/**
 * @author houzhenjing
 */
public class ServiceResult<T> implements Serializable {

    private static final long serialVersionUID = -1954065564856833013L;

    public static final int SC_SUCCESS = 200;

    private int code = -1;

    private String message = "";

    private T data;

    public ServiceResult() {
    }

    public boolean isSuccess() {
        return this.code == SC_SUCCESS;
    }

    public static <T> ServiceResult<T> success(T data) {
        return success(data, "");
    }

    public static <T> ServiceResult<T> success(T data, String message) {
        ServiceResult<T> result = new ServiceResult<T>();
        result.setCodeSuccess().setMessage(message).setData(data);
        return result;
    }

    public int getCode() {
        return code;
    }

    public ServiceResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public ServiceResult<T> setCodeSuccess() {
        this.code = SC_SUCCESS;
        return this;
    }


    public String getMessage() {
        return message;
    }

    public ServiceResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public ServiceResult<T> setData(T data) {
        this.data = data;
        return this;
    }


    public static final int SUCCESS = 200;//成功
    public static final int INVALID_SERVICE = 199;//服务不可用
    public static final int SERVEXCEPTION = 5000;//服务端异常
    public static final int UNKNOWN = 999;//未知错误
    public static final int BUSIERROR = 4000;//服务繁忙
    public static final int PARAMETERILLEGAL = 1444;//该用户不存在
    public static final int ROOMRUNNING = 1500;//房间正在运行中...
    public static final int AUCTCURDOING = 2100;//当前被竞拍者正在被竞拍中，还未结束
    public static final int AUCTCURLESSTHANMAXMONEY = 2101;//竞拍价格小于当前最高价
    public static final int ORDERNOTEXISTS = 3404;//订单不存在
    public static final int SMSSENDERROR = 4001;//发送短信出错
    public static final int PHONEINVALID = 4002;//手机格式不正确
    public static final int DIAMONDNUMNOTENOUGH = 2104;//钻石数量不够
    public static final int SMSCODEERROR = 4003;//短信验证码错误
    public static final int WEEKNOTWITHCASHTOWNUMS = 1600;//每周提现俩次


    /** 没有网络 */
    public static final int NOT_NET = 50010;
    public static final int OTHER = 50011;


    public String getErrorMessage() {
        String errorStr = "服务器正在维护";
        switch (code) {
            case DIAMONDNUMNOTENOUGH:
                errorStr = "钻石数量不够";
                break;

            case SMSCODEERROR:
                errorStr = "短信验证码错误";
                break;
            case PARAMETERILLEGAL:
                errorStr = "该用户不存在";
                break;
            case ROOMRUNNING:
                errorStr = "房间正在运行中...";
                break;
            case AUCTCURDOING:
                errorStr = "当前被竞拍者正在被竞拍中，还未结束";
                break;
            case AUCTCURLESSTHANMAXMONEY:
                errorStr = "竞拍价格小于当前最高价";
                break;
            case ORDERNOTEXISTS:
                errorStr = "订单不存在";
                break;
            case PHONEINVALID:
                errorStr = "手机格式不正确";
                break;
            case NOT_NET:
                errorStr = "网络异常";
                break;
            case UNKNOWN:
                errorStr = "未知错误";
                break;
            default:
                errorStr = message;
        }
        return errorStr;
    }

    public String getError() {
        String errorStr = "服务器正在维护";
        switch (code) {
            case DIAMONDNUMNOTENOUGH:
                errorStr = "钻石数量不够";
                break;
            case SMSCODEERROR:
                errorStr = "短信验证码错误";
                break;
            case INVALID_SERVICE:
                errorStr = "服务不可用";
                break;
            case SERVEXCEPTION:
                errorStr = "服务端异常";
                break;
            case UNKNOWN:
                errorStr = "未知错误";
                break;
            case BUSIERROR:
                errorStr = "服务繁忙";
                break;
            case PARAMETERILLEGAL:
                errorStr = "该用户不存在";
                break;
            case ROOMRUNNING:
                errorStr = "房间正在运行中...";
                break;
            case AUCTCURDOING:
                errorStr = "当前被竞拍者正在被竞拍中，还未结束";
                break;
            case AUCTCURLESSTHANMAXMONEY:
                errorStr = "竞拍价格小于当前最高价";
                break;
            case ORDERNOTEXISTS:
                errorStr = "订单不存在";
                break;
            case SMSSENDERROR:
                errorStr = "发送短信出错";
                break;
            case PHONEINVALID:
                errorStr = "手机格式不正确";
                break;
            case WEEKNOTWITHCASHTOWNUMS:
                errorStr = "每周只能提现2次";
                break;
            default:

        }
        return errorStr;
    }

    @Override
    public String toString() {
        return "ServiceResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
