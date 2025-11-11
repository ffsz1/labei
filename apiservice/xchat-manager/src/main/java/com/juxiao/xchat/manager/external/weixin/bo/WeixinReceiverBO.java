package com.juxiao.xchat.manager.external.weixin.bo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * @class: WeixinReceiverBO
 * @author: chenjunsheng
 * @date 2018年4月26日
 */
@Getter
@Setter
@XStreamAlias("xml")
public class WeixinReceiverBO {
    /**
     * 微信分配的公众账号ID（企业号corpid即为此appId）,不为空
     */
    @XStreamAlias("appid")
    private String appid;
    /**
     * 微信支付分配的商户号
     */
    @XStreamAlias("mch_id")
    private String mch_id;
    /**
     * 微信支付分配的终端设备号
     */
    @XStreamAlias("device_info")
    private String device_info;
    /**
     * 随机字符串，不长于32位
     */
    @XStreamAlias("nonce_str")
    private String nonce_str;
    /**
     * 签名，详见签名算法
     */
    @XStreamAlias("sign")
    private String sign;
    /**
     * 签名类型，HMAC-SHA256 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
     */
    @XStreamAlias("sign_type")
    private String sign_type;
    /**
     * 业务结果，SUCCESS SUCCESS/FAIL
     */
    @XStreamAlias("result_code")
    private String result_code;
    /**
     * 结果
     */
    @XStreamAlias("return_code")
    private String return_code;
    /**
     * 错误代码，SYSTEMERROR 错误返回的信息描述
     */
    @XStreamAlias("err_code")
    private String err_code;
    /**
     * 错误代码描述，系统错误，错误返回的信息描述
     */
    @XStreamAlias("err_code_des")
    private String err_code_des;
    /**
     * 用户标识，用户在商户appid下的唯一标识
     */
    @XStreamAlias("openid")
    private String openid;
    /**
     * 是否关注公众账号：Y 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
     */
    @XStreamAlias("is_subscribe")
    private String is_subscribe;
    /**
     * 交易类型：JSAPI、NATIVE、APP
     */
    @XStreamAlias("trade_type")
    private String trade_type;
    /**
     * 付款银行：CMC 银行类型，采用字符串类型的银行标识，银行类型见银行列表
     */
    @XStreamAlias("bank_type")
    private String bank_type;
    /**
     * 订单金额，订单总金额，单位为分，类型：int
     */
    @XStreamAlias("total_fee")
    private Integer total_fee;
    /**
     * 应结订单金额，应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
     */
    @XStreamAlias("settlement_total_fee")
    private String settlement_total_fee;
    /**
     * 货币种类，货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    @XStreamAlias("fee_type")
    private String fee_type;
    /**
     * 现金支付金额，现金支付金额订单现金支付金额，详见支付金额
     */
    @XStreamAlias("cash_fee")
    private String cash_fee;
    /**
     * 现金支付货币类型，货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    @XStreamAlias("cash_fee_type")
    private String cash_fee_type;
    /**
     * 代金券金额，代金券金额<=订单金额，订单金额-代金券金额=现金支付金额，详见支付金额，类型：int
     */
    @XStreamAlias("coupon_fee")
    private String coupon_fee;
    /**
     * 代金券使用数量，代金券使用数量，类型：int
     */
    @XStreamAlias("coupon_count")
    private String coupon_count;
    /**
     * 微信支付订单号，微信支付订单号
     */
    @XStreamAlias("transaction_id")
    private String transaction_id;
    /**
     * 商户订单号，商户系统的订单号，与请求一致。
     */
    @XStreamAlias("out_trade_no")
    private String out_trade_no;
    /**
     * 商家数据包，原样返回
     */
    @XStreamAlias("attach")
    private String attach;
    /**
     * 支付完成时间
     */
    @XStreamAlias("time_end")
    private String time_end;

    @XStreamAlias("coupon_fee_0")
    private String coupon_fee_0;

    @XStreamAlias("coupon_id_0")
    private String coupon_id_0;
}
