package com.juxiao.xchat.manager.external.weixin.bo;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * 微信客服接收对象信息
 *
 * @class: WeixinCustomReceiverBO.java
 * @author: chenjunsheng
 * @date 2018/5/8
 */
@Getter
@Setter
public class WeixinCustomReceiverBO {

    private String ToUserName;

    private String FromUserName;

    private long CreateTime;

    private String Content;

    private String MsgId;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
