package com.juxiao.xchat.service.api.room.bo;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @Auther: alwyn
 * @Description: 云信掉线回调参数
 * @Date: 2018/9/5 9:40
 */
@Getter
@Setter
public class LogoutMsgBO {

    /** 值为3，表示是登出事件的消息 */
    private String eventType;
    /** 发生登出事件的用户帐号，字符串类型 */
    private String accid;
    /** 登出时的ip地址 */
    private String clientIp;
    /** 客户端类型： AOS、IOS、PC、WINPHONE、WEB、REST，字符串类型 */
    private String clientType;
    /** 登出事件的返回码，可转为Integer类型数据 */
    private String code;
    /** 当前sdk的版本信息，字符串类型 */
    private String sdkVersion;
    /** 登出事件发生时的时间戳，可转为Long型数据 */
    private String timestamp;

    @Override
    public String toString() {
        return "LogoutMsgBO{" +
                "eventType='" + eventType + '\'' +
                ", accid='" + accid + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", clientType='" + clientType + '\'' +
                ", code='" + code + '\'' +
                ", sdkVersion='" + sdkVersion + '\'' +
                ", timestamp='" + DateFormatUtils.format(Long.valueOf(timestamp), DateFormatUtils.ISO_DATETIME_FORMAT.getPattern()) + '\'' +
                '}';
    }
}
