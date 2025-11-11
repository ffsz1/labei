package com.juxiao.xchat.dao.sysconf.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

@Getter
@Setter
public class NetEaseConversationDO {
    private Integer id;
    private String convType;
    private String toStr;
    private String fromAccount;
    private String fromClientType;
    private String fromDeviceId;
    private String fromNick;
    private String msgTimestamp;
    private String msgType;
    private String body;
    private String attach;
    private String msgidClient;
    private String msgidServer;
    private String resendFlag;
    private String customSafeFlag;
    private String customApnsText;
    private String tMembers;
    private String ext;
    private String antispam;
    private String yidunRes;
    private Date createTime;

    public String getBody() {
        if (StringUtils.isNotBlank(body) && body.length() > 500) {
            body = body.substring(0, 500);
        }
        return body;
    }
}