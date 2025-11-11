package com.xchat.oauth2.service.common.status;

/**
 * @author liuguofu
 */
public enum OAuthStatus {
    ACCESS_DENIED(100, "access denied"), //拒绝访问

    INVALID_REQUEST(101, "invalid request"), //请求不合法

    INVALID_REQUEST_SCHEME(102, "invalid request scheme"), //错误的请求协议

    INVALID_REQUEST_METHOD(103, "invalid request method"), //错误的请求方法

    INVALID_CLIENT_ID(104, "invalid client id"), //client id不存在或已删除

    CLIENT_ID_IS_BLOCKED(105, "client id is blocked"), //client id已被禁用

    UNAUTHORIZED_CLIENT_ID(106, "unauthorized client id"), //client id未授权

    USERNAME_PASSWORD_MISMATCH(107, "用户名与密码不匹配哦,请重新输入"), //用户名密码不匹配

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

    ACCESS_TOKEN_HAS_EXPIRED_SINCE_PASSWORD_CHANGED(125, "access token has expired since password changed"),
    //因用户修改密码而导致access_token过期

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

    INVALID_IDENTIFYING_CODE(150, "验证码错误"), //不可用的验证码

    INVALID_USERNAME(151, "invalid username"), //用户名不合法

    USER_HAS_SIGNED_UP(152, "user has signed up"), //用户名已被注册

    INVALID_RESET_CODE(153, "invalid reset code"), //重置码无效

    INVALID_NICK(161, "invalid nick"),   //昵称不合法

    INVALID_THIRD_TOKEN(162, "invalid third token"), //第三方token不合法

    THIRD_ACCOUNT_HAVE_BIND(163, "the third account have bind"), //第三方账户已经绑定或之前已使用该账户登陆过系统

    UNBIND_OPENID_NOT_MATCH(164, "unbind openId not match error"), //账户解绑失败

    UNBIND_MAIN_ACCOUNT(165, "unbind main account error"), //解绑主账户错误

    PARAMETERILLEGAL(166, "parameter illegal"),

    SUCCESS(200, "success"), //成功

    INVALID_SERVICE(199, "invalid service"),//服务不可用

    SIGN_IP_TO_OFTEN(301, "注册过于频繁"),//注册过于频繁

    SMS_IP_TO_OFTEN(302, "获取短信过于频繁"),//获取短信过于频繁

    SMS_V_TO_OFTEN(303, "短信码验证过于频繁"),//获取短信过于频繁

    ACCOUNT_ERROR(407, "用户账号异常,请联系官方客服"),//账号异常-账号被封

    DECEIVE_ERROR(408, "用户账号异常，请联系官方客服"),//账号异常-设备被封

    VERSION_ERROR(409, "版本过低，请升级版本"),

    MULTI_ACCOUNT(410, "该手机号有多个绑定账号，请使用ID登录"), //多个绑定账号

    UNKNOWN(999, "unknown"); //未知错误

    private final int value;

    private final String reasonPhrase;

    private OAuthStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    /**
     * Return the integer value of this status code.
     */
    public int value() {
        return this.value;
    }

    /**
     * Return the reason phrase of this status code.
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    /**
     * Return a string representation of this status code.
     */
    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
