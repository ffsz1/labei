package com.tongdaxing.xchat_framework.http_image.result;

/**
 * Created by lijun on 2015/6/3.
 * <p>
 * 业务错误代码
 */
public class BusinessErrorCodes {

//    public static final int ACCESS_DENIED = 100; //  "access denied"), //拒绝访问
//
//    public static final int INVALID_REQUEST = 101; //"invalid request"), //请求不合法
//
//    public static final int INVALID_REQUEST_SCHEME = 102; //"invalid request scheme"), //错误的请求协议
//
//    public static final int INVALID_REQUEST_METHOD = 103; //"invalid request method"), //错误的请求方法
//
//    public static final int UNSUPPORTED_CLIENT_OS = 104; //"unsupported client os"), //客户端操作系统不支持
//
//    public static final int UNSUPPORTED_CLIENT_VERSION = 105; //"unsupported client version"), //客户端版本不支持
//
//    public static final int RATE_LIMIT_EXCEEDED = 106; //"rate limit exceeded"), //用户访问速度限制
//
//    public static final int UNAUTHENTICATION = 107;//"authentication error"), //权限验证错误
//
//    public static final int ILLEGAL_PARAMETER = 108; //"illegal parameter"), //非法参数
//
//    public static final int TIMEOUT_ERROR = 109;//"timeout error"), //超时错误
//
//    public static final int BUSINESS_ERROR = 110; //"business error"), //业务处理异常
//
//    public static final int DUPLICATE_ERROR = 111;//"duplicate error"), //存在重复的记录
//
//    public static final int FILE_UPLOAD_ERROR = 112;//"file upload error"), //文件上传失败
//
//    public static final int DATA_NOT_EXIST_ERROR = 113;//"data not exist error"), //数据不存在
//
//    public static final int USER_FORBIDDEN_ERROR = 160; //"the user is not allowed to operate,because the user have been put in blacklist!"), //黑名单用户
//
//    public static final int ILLEGAL_CONTENT_ERROR = 161; //"content contains illegal info!"), //内容不合法
//
//    public static final int SERVER_MAINTENANCE = 998;//"server maintenance"), //服务器维护

//    public static final int UNKNOWN = 999;//"unknown error"); //未知错误


    public enum OAuthStatus {

        ACCESS_DENIED(100, "access denied"), //拒绝访问

        INVALID_REQUEST(101, "invalid request"), //请求不合法

        INVALID_REQUEST_SCHEME(102, "invalid request scheme"), //错误的请求协议

        INVALID_REQUEST_METHOD(103, "invalid request method"), //错误的请求方法

        INVALID_CLIENT_ID(104, "invalid client id"), //client id不存在或已删除

        CLIENT_ID_IS_BLOCKED(105, "client id is blocked"), //client id已被禁用

        UNAUTHORIZED_CLIENT_ID(106, "unauthorized client id"), //client id未授权

        USERNAME_PASSWORD_MISMATCH(107, "username password mismatch"), //用户名密码不匹配

        INVALID_REQUEST_SCOPE(108, "invalid request scope"), //访问的scope不合法，开发者不用太关注，一般不会出现该错误

        INVALID_USER(109, "invalid user"), //用户不存在或已删除

        USER_HAS_BLOCKED(110, "user has blocked"), //用户已被屏蔽

        INVALID_TOKEN(111, "invalid token"), //token不存在或已被用户删除，或者用户修改了密码

        ACCESS_TOKEN_IS_MISSING(112, "access token is missing"), //未找到access_token

        ACCESS_TOKEN_HAS_EXPIRED(113, "access token has expired"), //access_token已过期

        INVALID_REQUEST_URI(114, "invalid request uri"), //请求地址未注册

        INVALID_CREDENTIAL_1(115, "invalid credential 1"), //用户未授权访问此数据

        INVALID_CREDENTIAL_2(116, "invalid credential 2"), //client id未申请此权限

        NOT_TRIAL_USER(117, "not trial user"), //未注册的测试用户

        REQUIRED_PARAMETER_IS_MISSING(118, "required parameter is missing"), //缺少参数

        INVALID_GRANT(119, "invalid grant type"),

        UNSUPPORTED_GRANT_TYPE(120, "unsupported grant type"), //错误的grant_type

        UNSUPPORTED_RESPONSE_TYPE(121, "unsupported response type"), //错误的response_type

        CLIENT_SECRET_MISMATCH(122, "client secret mismatch"), //client_secret不匹配

        REDIRECT_URI_MISMATCH(123, "redirect uri mismatch"), //redirect_uri不匹配

        INVALID_AUTHORIZATION_CODE(124, "invalid authorization code"), //authorization_code不存在或已过期

        ACCESS_TOKEN_HAS_EXPIRED_SINCE_PASSWORD_CHANGED(125, "access token has expired since password changed"), //因用户修改密码而导致access_token过期

        ACCESS_TOKEN_HAS_NOT_EXPIRED(126, "access token has not expired"), //access_token未过期;

        UNSUPPORTED_TICKET_ISSUE_TYPE(127, "unsupported ticket issue type"),

        INVALID_TICKET(128, "invalid ticket"),//ticket不存在或已过期

        TICKET_IS_MISSING(129, "ticket is missing"), //未找到ticket

        TICKET_HAS_EXPIRED(130, "ticket has expired"), //ticket过期

        TICKET_HAS_NOT_EXPIRED(131, "ticket has not expired"), //ticket未过期

        TICKET_HAS_EXPIRED_SINCE_PASSWORD_CHANGED(132, "ticket has expired since password changed"), //因为用户修改密码而ticket过期

        INVALID_SCOPE(133, "invalid scope"),

        RATE_LIMIT_EXCEEDED1(134, "rate limit exceeded 1"), //用户访问速度限制

        RATE_LIMIT_EXCEEDED2(135, "rate limit exceeded 2"), //IP访问速度限制

        INVALID_IDENTIFYING_CODE(150, "invalid identifying code"), //不可用的验证码

        INVALID_USERNAME(151, "invalid username"), //用户名不合法

        USER_HAS_SIGNED_UP(152, "user has signed up"), //用户名已被注册

        INVALID_RESET_CODE(153, "invalid reset code"), //重置码无效

        INVALID_NICK(161, "invalid nick"),   //昵称不合法

        INVALID_THIRD_TOKEN(162, "invalid third token"), //第三方token不合法

        THIRD_ACCOUNT_HAVE_BIND(163, "the third account have bind"), //第三方账户已经绑定或之前已使用该账户登陆过系统

        UNBIND_OPENID_NOT_MATCH(164, "unbind openId not match error"), //账户解绑失败

        UNBIND_MAIN_ACCOUNT(165, "unbind main account error"), //解绑主账户错误

        SUCCESS(200, "success"), //成功

        INVALID_SERVICE(199, "invalid service"),//服务不可用

        UNKNOWN(999, "unknown"); //未知错误

        OAuthStatus(int code, String value) {

        }
    }

    public static final int SUCCESS = 200;//(200,"success"), public static final int成功
    public static final int INVALID_SERVICE = 199;//(199,"invalid service"),public static final int服务不可用
    public static final int UNKNOWN = 999;//(999,"unknown"),public static final int未知错误
    public static final int BUSIERROR = 4000;//(4000,"BusiError"),
    public static final int PARAMETERILLEGAL = 1444;//(1444,"parameter illegal"),
    public static final int USERNOTEXISTS = 1404;//(1404,"user not exists");

}
