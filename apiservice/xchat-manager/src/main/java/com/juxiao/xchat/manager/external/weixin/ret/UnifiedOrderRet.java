package com.juxiao.xchat.manager.external.weixin.ret;

import com.juxiao.xchat.base.utils.MD5Utils;
import com.juxiao.xchat.base.utils.PojoUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@XStreamAlias(value = "xml")
public class UnifiedOrderRet {
    /**
     * 返回状态码：此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断：SUCCESS/FAIL
     */
    @XStreamAlias(value = "return_code")
    private String return_code;

    /**
     * 返回信息：返回信息，如非空，为错误原因，例如：签名失败、参数格式校验错误
     */
    @XStreamAlias(value = "return_msg")
    private String return_msg;

    /**
     * 公众账号ID：调用接口提交的公众账号ID
     */
    @XStreamAlias(value = "appid")
    private String appid;

    /**
     * 商户号：调用接口提交的商户号
     */
    @XStreamAlias(value = "mch_id")
    private String mch_id;

    /**
     * 设备号：自定义参数，可以为请求支付的终端设备号等
     */
    @XStreamAlias(value = "device_info")
    private String device_info;

    /**
     * 随机字符串：微信返回的随机字符串
     */
    @XStreamAlias(value = "nonce_str")
    private String nonce_str;

    /**
     * 签名：微信返回的签名值，详见签名算法
     */
    @XStreamAlias(value = "sign")
    private String sign;

    /**
     * 业务结果：SUCCESS/FAIL
     */
    @XStreamAlias(value = "result_code")
    private String result_code;

    /**
     * 错误代码：详细参见下文错误列表
     */
    @XStreamAlias(value = "err_code")
    private String err_code;

    /**
     * 错误代码描述：错误信息描述
     */
    @XStreamAlias(value = "err_code_des")
    private String err_code_des;

    /**
     * 交易类型：交易类型，取值为：JSAPI，NATIVE，APP等，说明详见参数规定
     */
    @XStreamAlias(value = "trade_type")
    private String trade_type;

    /**
     * 预支付交易会话标识：微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
     */
    @XStreamAlias(value = "prepay_id")
    private String prepay_id;

    /**
     * 二维码链接：trade_type为NATIVE时有返回，用于生成二维码，展示给用户进行扫码支付
     */
    @XStreamAlias(value = "code_url")
    private String code_url;

    /**
     * @return
     * @class: WeixinUnifiedResBean.java
     * @description: 验证签名
     * @author: chenjunsheng
     * @date 2018年4月8日
     */
    public boolean validateSign(String paykey) {
        if (StringUtils.isBlank(this.sign)) {
            return false;
        }

        String preSign = PojoUtils.keyValuePair(this, "sign") + "&key=" + paykey;
        String sign = MD5Utils.encode(preSign);
        return this.sign.equalsIgnoreCase(sign);
    }
}
