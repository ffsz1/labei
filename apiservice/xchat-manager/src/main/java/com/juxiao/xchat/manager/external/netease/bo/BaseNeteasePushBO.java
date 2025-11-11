package com.juxiao.xchat.manager.external.netease.bo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseNeteasePushBO {
    /**
     * 自定义通知内容，第三方组装的字符串，建议是JSON串，最大长度4096字符
     */
    protected Attach attach;//	String	是
    /**
     * iOS推送内容，第三方自己组装的推送内容,不超过150字符
     */
    protected String pushcontent;//	String	否
    /**
     * iOS推送对应的payload,必须是JSON,不能超过2k字符
     */
    protected Payload payload;//	String	否
    /**
     * 如果有指定推送，此属性指定为客户端本地的声音文件名，长度不要超过30个字符，如果不指定，会使用默认声音
     */
    protected String sound;//	String	否
    /**
     * 1表示只发在线，2表示会存离线，其他会报414错误。默认会存离线
     */
    protected int save;//	int	否
    /**
     * 发消息时特殊指定的行为选项,Json格式，可用于指定消息计数等特殊行为;option中字段不填时表示默认值。
     */
    protected Option option;//	String	否
    /**  */
    private String body;

}
