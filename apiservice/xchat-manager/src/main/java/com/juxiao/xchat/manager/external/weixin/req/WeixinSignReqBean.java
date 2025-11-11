package com.juxiao.xchat.manager.external.weixin.req;

import com.alibaba.fastjson.JSON;
import com.juxiao.xchat.base.utils.MD5Utils;
import com.juxiao.xchat.base.utils.PojoUtils;
import com.juxiao.xchat.base.utils.XmlUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * @copyright: 草耕公司源代码, 版权归草耕公司所有
 * @class: WeixinSignReqBean
 * @description: WeixinSignReqBean往渠道提交的对象
 * @author: chenjunsheng
 * @date 2018年4月8日
 */
@Getter
@Setter
@XStreamAlias(value = "xml")
public class WeixinSignReqBean {
    /**
     * 微信分配的公众账号ID（企业号corpid即为此appId），必填：是
     */
    @XStreamAlias(value = "appid")
    private String appid;

    /**
     * 微信支付分配的商户号，必填：是
     */
    @XStreamAlias(value = "mch_id")
    private String mch_id;

    /**
     * 设备号，终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"，必填：否
     */
    @XStreamAlias(value = "device_info")
    private String device_info;

    /**
     * 随机字符串，不长于32位。推荐随机数生成算法，必填：是
     */
    @XStreamAlias(value = "nonce_str")
    private String nonce_str;

    /**
     * 签名，详见签名生成算法，必填：是
     */
    @XStreamAlias(value = "sign")
    private String sign;

    /**
     * 商品描述，腾讯充值中心-QQ会员充值，必填：是
     */
    @XStreamAlias(value = "body")
    private String body;

    /**
     * <p>
     * 商品详细列表，使用Json格式，传输签名前请务必使用CDATA标签将JSON文本串保护起来，必填：否
     * </p>
     * <p>
     * goods_detail []：
     * </p>
     * <p>
     * └ goods_id String 必填 32 商品的编号
     * </p>
     * <p>
     * └ wxpay_goods_id String 可选 32 微信支付定义的统一商品编号
     * </p>
     * <p>
     * └ goods_name String 必填 256 商品名称
     * </p>
     * <p>
     * └ quantity Int 必填 商品数量
     * </p>
     * <p>
     * └ price Int 必填 商品单价，单位为分
     * </p>
     * <p>
     * └ goods_category String 可选 32 商品类目ID
     * </p>
     * <p>
     * └ body String 可选 1000 商品描述信息
     * </p>
     */
    @XStreamAlias(value = "detail")
    private String detail;

    /**
     * 附加数据，附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据，必填：否
     */
    @XStreamAlias(value = "attach")
    private String attach;

    /**
     * 商户订单号，商户系统内部的订单号，32个字符内、可包含字母，其他说明见商户订单号，必填：是
     */
    @XStreamAlias(value = "out_trade_no")
    private String out_trade_no;

    /**
     * 总金额，订单总金额，单位为分，详见支付金额，必填：是
     */
    @XStreamAlias(value = "total_fee")
    private int total_fee;

    /**
     * 终端IP，APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP，必填：是
     */
    @XStreamAlias(value = "spbill_create_ip")
    private String spbill_create_ip;

    /**
     * 商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠，必填：否
     */
    @XStreamAlias(value = "goods_tag")
    private String goods_tag;

    /**
     * 通知地址，接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数，必填：是
     */
    @XStreamAlias(value = "notify_url")
    private String notify_url;

    /**
     * 交易类型，取值如下：JSAPI，NATIVE，APP。详细说明见参数规定，必填：是
     */
    @XStreamAlias(value = "trade_type")
    private String trade_type;

    /**
     * 商品ID，trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义，必填：否
     */
    @XStreamAlias(value = "product_id")
    private String product_id;

    /**
     * 指定支付方式：no_credit，指定不能使用信用卡支付，必填：否
     */
    @XStreamAlias(value = "limit_pay")
    private String limit_pay;

    /**
     * 用户标识，trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。
     * 企业号请使用【企业号OAuth2.0接口】获取企业号内成员userid，再调用【企业号userid转openid接口】进行转换，必填：否
     */
    @XStreamAlias(value = "openid")
    private String openid;

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    /**
     * @param key
     * @author: chenjunsheng
     * @date 2018年4月28日
     */
    public void sign(String key) {
        String preSign = PojoUtils.keyValuePair(this, "sign") + "&key=" + key;
        this.sign = MD5Utils.encode(preSign).toUpperCase();
    }

    /**
     * @return
     * @author: chenjunsheng
     * @date 2018年4月28日
     */
    public String toXml() {
        return XmlUtils.toXml(this, false);
    }
}
