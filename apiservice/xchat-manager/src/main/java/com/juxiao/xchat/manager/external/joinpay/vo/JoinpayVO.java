package com.juxiao.xchat.manager.external.joinpay.vo;

import lombok.Data;

@Data
public class JoinpayVO {
    /** 版本号 */
    private String p0_Version;
    /** 商户编号 */
    private String p1_MerchantNo;
    /**商户订单号*/
    private String p2_OrderNo;
    /**订单金额*/
    private String p3_Amount;
    /**交易币种 */
    private String p4_Cur;
    /**商品名称 */
    private String p5_ProductName;
    /**商品描述 */
    private String p6_ProductDesc;
    /**公用回传参数 */
    private String p7_Mp;
    /**商户页面通知地址 */
    private String p8_ReturnUrl;
    /**服务器异步通知地址 */
    private String p9_NotifyUrl;
    /**交易类型 */
    private String q1_FrpCode;
    /**银行商户编码 */
    private String q2_MerchantBankCode;
    /**子商户号 */
    private String q3_SubMerchantNo;
    /**是否展示图片 */
    private String q4_IsShowPic;
    /**微信 Openid */
    private String q5_OpenId;
    /**付款码数字 */
    private String q6_AuthCode;
    /**APPID */
    private String q7_AppId;
    /**终端号 */
    private String q8_TerminalNo;
    /**微信/支付宝 H5 模式 */
    private String q9_TransactionModel;
    /**交易商户号 */
    private String qa_TradeMerchantNo;
    /**买家的支付宝 */
    private String qb_buyerId;
    /**签名数据 */
    private String hmac;
}
