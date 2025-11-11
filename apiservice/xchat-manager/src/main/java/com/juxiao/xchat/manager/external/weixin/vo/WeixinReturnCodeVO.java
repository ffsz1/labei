package com.juxiao.xchat.manager.external.weixin.vo;

import com.juxiao.xchat.base.utils.XmlUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * @class: WeixinReturnCodeResponse
 * @author: chenjunsheng
 * @date 2018年4月26日
 */
@Getter
@Setter
@XStreamAlias("xml")
public class WeixinReturnCodeVO {
    @XStreamAlias("return_code")
    private String returnCode;
    @XStreamAlias("return_msg")
    private String returnMsg;

    public WeixinReturnCodeVO(String returnCode, String returnMsg) {
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
    }

    @Override
    public String toString() {
        return XmlUtils.toXml(this, true);
    }
}
