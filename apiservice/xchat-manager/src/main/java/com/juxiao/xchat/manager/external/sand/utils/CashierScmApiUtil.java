package com.juxiao.xchat.manager.external.sand.utils;

//import cn.com.sand.pay.online.sdk.util.DynamicPropertyHelper;
import com.alibaba.fastjson.JSONObject;

import java.text.DateFormat;

/**
 * （仅供参考）
 *
 * @author sandpay
 */
public class CashierScmApiUtil {

//    // 商户号
//    public static final String mid = DynamicPropertyHelper.getStringProperty("sandpay.merch.mid").get();
//    public static final String plmid = DynamicPropertyHelper.getStringProperty("sandpay.merch.plmid").get();
//    public static final String productCode = DynamicPropertyHelper.getStringProperty("sandpay.merch.productcode").get();
//
//    // 平台商户号
//    public static final String plMid = "";
//
//    // WEB统一下单接口地址
//    public static final String GATEWAY_CREATEORDER_URL = "web/order/create";
//
//    // 订单查询接口地址
//    public static final String GATEWAY_QUERYORDER_URL = "api/order/query";
//
//    // 对账单申请接口地址
//    public static final String GATEWAY_CLEARFILE_URL = "api/clearfile/download";
//
//    // 退款申请接口地址
//    public static final String GATEWAY_REFUND_URL = "api/order/refund";
//
//
//    /**
//     * 日期格式转换
//     *
//     * @return String
//     * @Title: getCurTimestampStr
//     */
//    public static String getCurTimestampStr() {
//        DateFormat fmt = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
//        return fmt.format(System.currentTimeMillis());
//    }
//
//    /**
//     * 获取支付报文头
//     *
//     * @return JSONObject
//     * @Title: getGwHeadJson
//     */
//    public static JSONObject getGwHeadJson(String method) {
//        JSONObject headJson = new JSONObject();
//        headJson.put("version", "1.0");
//        headJson.put("method", method);
//        headJson.put("productId", productCode);
//        headJson.put("accessType", "3");//商户接入类型
//        headJson.put("mid", mid);
//        headJson.put("plMid", plmid);//对应核心企业ID
//        headJson.put("channelType", "07");
//        headJson.put("reqTime", cn.com.sandpay.util.CashierScmApiUtil.getCurTimestampStr());
//        return headJson;
//    }

}
