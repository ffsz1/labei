package com.juxiao.xchat.manager.external.joinpay.ret;

import lombok.Data;

@Data
public class JoinpayRet {
    /** 版本号 */
    private String r0_Version;
    /** 商户编号 */
    private String r1_MerchantNo;
    /** 商户订单号 */
    private String r2_OrderNo;
    /**支付金额*/
    private String r3_Amount;
    /**交易币种*/
    private String r4_Cur;
    /**公用回传参数 */
    private String r5_Mp;
    /** 交易类型 */
    private String r6_FrpCode;
    /** 交易流水号 */
    private String r7_TrxNo;
    /** 银行商户编码*/
    private String r8_MerchantBankCode;
    /** 子商户号*/
    private String r9_SubMerchantNo;
    /** 响应码*/
    private String ra_Code;
    /** 响应码描述*/
    private String rb_CodeMsg;
    /** 结果*/
    private String rc_Result;
    /** 二维码图片码*/
    private String rd_Pic;
    /** 签名数据*/
    private String hmac;
}
