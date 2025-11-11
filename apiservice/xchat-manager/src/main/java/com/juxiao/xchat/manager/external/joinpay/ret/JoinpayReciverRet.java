package com.juxiao.xchat.manager.external.joinpay.ret;

import lombok.Data;

@Data
public class JoinpayReciverRet {
    /** 商户编号 */
    private String r1_MerchantNo;
    /** 商户订单号 */
    private String r2_OrderNo;
    /**支付金额*/
    private String r3_Amount;
    /**项目*/
    private String r4_ProductName;
    /**单号 */
    private String r5_TrxNo;
    /** 响应码 */
    private String ra_Status;
    /** 响应码 */
    private String rb_Code;
    /** 响应信息*/
    private String rc_CodeMsg;
    /** 签名数据*/
    private String hmac;
}
