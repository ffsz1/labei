package com.xchat.oauth2.service.core.exception;

/**
 * @author liuguofu
 *         on 4/10/15.
 */
public enum StatusCode {

    ACCESS_DENIED(100,"access denied"), //拒绝访问

    INVALID_REQUEST(101,"invalid request"), //请求不合法

    INVALID_REQUEST_SCHEME(102,"invalid request scheme"), //错误的请求协议

    INVALID_REQUEST_METHOD(103,"invalid request method"), //错误的请求方法

    UNSUPPORTED_CLIENT_OS(104,"unsupported client os"), //客户端操作系统不支持

    UNSUPPORTED_CLIENT_VERSION(105,"unsupported client version"), //客户端版本不支持

    RATE_LIMIT_EXCEEDED(106,"rate limit exceeded"), //用户访问速度限制

    UNAUTHENTICATION(107,"authentication error"), //权限验证错误

    ILLEGAL_PARAMETER(108,"illegal parameter"), //非法参数

    TIMEOUT_ERROR(109,"timeout error"), //超时错误

    BUSINESS_ERROR(110,"business error"), //业务处理异常

    DUPLICATE_EXCEPTION(111,"duplicate error"), //存在重复的记录

    FILE_UPLOAD_ERROR(112,"file upload error"), //文件上传失败

    DATA_NOT_EXIST_ERROR(113,"data not exist error"), //数据不存在

    USER_FORBIDDEN_ERROR(160,"the user is not allowed to operate,because the user have been put in blacklist!"),

    ILLEGAL_CONTENT_ERROR(161,"content contains illegal info!"),

    SERVER_MAINTENANCE(998,"server maintenance"), //服务器维护

    UNKNOWN(999,"unknown error"); //未知错误

    private final int value;

    private final String reasonPhrase;

    private StatusCode(int value, String reasonPhrase) {
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
