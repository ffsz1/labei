package com.xchat.common.netease.neteaseacc.constant;

/**
 * Created by liuguofu on 2017/4/29.
 */
public enum NetEaseCode {
    SUCCESS(200,"success"),//成功
    CLIENT_ERROR(201,"客户端版本不对，需升级sdk"),
    BOLOCK(301,"被封禁"),
    USER_PWD_ERROR(302,"用户名或密码错误"),
    IP_ERROR(315,"IP限制"),
    OAUTHILLEGA_ERROR(403,"非法操作或没有权限"),
    OBJ_NOTFOUND(404,"对象不存在"),
    TO_LONG_ERROR(405,"参数长度过长"),
    READ_ONLY(406,"对象只读"),
    REQ_TIMEOUT(408,"客户端请求超时"),
    SMS_OAUTH_ERROR(413,"验证失败(短信服务)"),
    PARAM_ERROR(414,"参数错误"),
    NET_ERROR(415,"客户端网络问题"),
    TO_OFTEN(416,"频率控制"),
    DUP_HANDEL(417,"重复操作"),
    CHANNEL_ERROR(418,"通道不可用(短信服务)"),
    OVER_NUM(419,"数量超过上限"),
    ACC_BLOCK(422,"账号被禁用"),
    DUP_HTTP_REQ(431,"HTTP重复请求"),
    SERV_ERROR(500,"服务器内部错误"),
    SERV_BUSY(503,"服务器繁忙"),
    MSG_TIMEOUT(508,"消息撤回时间超限"),
    INVALID_PROT(509,"无效协议"),
    SERV_INVALID(514,"服务不可用"),
    UNPACK_ERROR(998,"解包错误"),
    PACK_ERROR(999,"打包错误"),
    //群相关错误码
    NUM_LIMIT(801,"群人数达到上限"),
    OAUTH_ERROR(802,"没有权限"),
    GROUP_NOTEXISTS(803,"群不存在"),
    USER_NOTIN_GROUP(804,"用户不在群"),
    GROUPTYPE_MISMATCH(805,"群类型不匹配"),
    GROUPNUM_LIMIT(806,"创建群数量达到限制"),
    GROUPMEM_ERROR(807,"群成员状态错误"),
    APP_SUC(808,"申请成功"),
    ALREADY_INGROUP(809,"已经在群内"),
    INVITE_SUC(810,"邀请成功"),
//  音视频通话/白板相关错误码
    CHAN_INVALID(9102,"通道失效"),
    RESPONED_ERROR(9103,"已经在他端对这个呼叫响应过了"),
    UNREACH_ERROR(11001,"通话不可达，对方离线状态"),
// 聊天室相关错误码
    IMCONNECT_ERROR(13001,"IM主连接状态异常"),
    IMROOM_ERROR(13002,"聊天室状态异常"),
    BLACKLISTACC(13003,"账号在黑名单中,不允许进入聊天室"),
    CHATFORBIN(13004,"在禁言列表中,不允许发言"),
//  特定业务相关错误码
    EMAIL_ERROR(10431,"输入email不是邮箱"),
    PHONE_ERROR(10432,"输入mobile不是手机号码"),
    PWD_DUPERROR(10433,"注册输入的两次密码不相同"),
    COMPANY_NOTEXISTS(10434,"企业不存在"),
    ACCPWD_ERROR(10435,"登陆密码或账号不对"),
    APP_NOTEXISTS(10436,"app不存在"),
    EMAIL_EXISTS(10437,"email已注册"),
    PHONE_EXISTS(10438,"手机号已注册"),
    APP_NAME_EXISTS(10441,"app名字已经存在");

    private final int value;

    private final String msg;

    private NetEaseCode(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public int value() {
        return this.value;
    }

    public String getMsg() {
        return msg;
    }
    @Override
    public String toString() {
        return Integer.toString(value);
    }



}
