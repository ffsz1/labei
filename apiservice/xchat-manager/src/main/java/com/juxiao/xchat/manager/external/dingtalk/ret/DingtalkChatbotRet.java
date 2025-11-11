package com.juxiao.xchat.manager.external.dingtalk.ret;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author chris
 * @Title:
 * @date 2018/10/8
 * @time 10:00
 */
@Getter
@Setter
@AllArgsConstructor
public class DingtalkChatbotRet {

    private boolean isSuccess;
    private Integer errorCode;
    private String errorMsg;

    public DingtalkChatbotRet(){
        this.isSuccess=false;
        this.errorCode=0;
    }

}
