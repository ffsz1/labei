package com.tongdaxing.xchat_framework.im;

/**
 * IM错误码
 */
public interface IMError {
    String IM_LOGIN_FAIL = "100100";//:"im登录鉴权失败"
    String IM_GET_USER_INFO_FAIL = "100101";//获取用户信息失败
    String IM_GET_ROOM_INFO_FAIL = "100200";//获取房间信息失败
    String IM_ROOM_SOCKET_ID_NOT_EXIST = "100201";//用户房间socketId不存在
    String IM_USER_IN_ROOM_BLACK_LIST = "100202";//用户在房间黑名单中
    String IM_ROOM_NOT_EXIST = "100203";//房间不存在
    String IM_QUEUE_POSITION_LIMIT = "100300";//队列位置权限控制
    String IM_OP_LIMIT_LOWE = "100301";//只能操作比自己低权限的用户
    String IM_USER_IN_BLACK = "100400";//用户已在黑名单
    String IM_USER_NOT_IN_THE_BLACK = "100401";//用户不在黑名单
    String IM_USER_IS_MANAGER = "100402";//用户已是管理员
    String IM_USER_NOT_MANAGER = "100403";//用户不是管理员
    String IM_MS_SEND_ERROR = "im消息发送失败";



    int USER_REAL_NAME_NEED_VERIFIED = 2507;//"该功能需要进行实名验证"
    int USER_REAL_NAME_AUDITING = 2508;// "您的实名认证信息正在审核中……"
    int USER_REAL_NAME_NEED_PHONE = 2511;//"该功能需要手机绑定"

    int IM_ERROR_LOGIN_AUTH_FAIL = 100100;//:"im登录鉴权失败"
    int IM_ERROR_GET_USER_INFO_FAIL = 100101;//获取用户信息失败
}
