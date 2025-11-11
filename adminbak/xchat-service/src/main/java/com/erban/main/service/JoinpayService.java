package com.erban.main.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.erban.main.vo.admin.JoinpayRet;
import com.erban.main.vo.admin.JoinpayTransfer;
import com.erban.main.vo.admin.JoinpayTransferCallback;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.utils.HttpClientUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.ws.WebServiceException;
import java.util.HashMap;
import java.util.Map;

@Service
public class JoinpayService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    final String userNo = "888106600008428";
    final String key = "303014c77c50497b881f0dca5882ff18";
    final String singlePay = "https://www.joinpay.com/payment/pay/singlePay";
    final String singlePayQuery = "https://www.joinpay.com/payment/pay/singlePayQuery";
    private Gson gson = new Gson();

    /**
     * 企业转账(汇聚)
     *
     */
    public JoinpayRet transfer(JoinpayTransfer joinpayTransfer) throws Exception {
        // 发起汇聚转账
        Map<String, Object> map = Maps.newHashMap();
        map.put("userNo", userNo);/** 商户编号 */
        map.put("productCode", "BANK_PAY_DAILY_ORDER");/** 产品类型 */
        map.put("requestTime", joinpayTransfer.getRequestTime()); /**交易请求时间*/
        map.put("merchantOrderNo", getShort(joinpayTransfer.getMerchantOrderNo()));/**商户订单号*/
        map.put("receiverAccountNoEnc", joinpayTransfer.getReceiverAccountNoEnc()); /**收款账户号 */
        map.put("receiverNameEnc", joinpayTransfer.getReceiverNameEnc()); /** 收款人 */
        map.put("receiverAccountType", 201); /** 账户类型 */
        map.put("receiverBankChannelNo", ""); /** 收款账户联行号 */
        map.put("paidAmount", joinpayTransfer.getPaidAmount()); /** 交易金额*/
        map.put("currency", "201"); /** 币种*/
        map.put("isChecked", "202"); /** 是否复核 */
        map.put("paidDesc", "拉贝钻石提现" + joinpayTransfer.getPaidAmount() + "元"); /** 代付说明 */
        map.put("paidUse", "205"); /** 代付用途 */
        if ("prod".equalsIgnoreCase(GlobalConfig.sysEnv)) {
            map.put("callbackUrl", "http://47.94.6.83:8083/yingtao/withdraw/joinpay/transferCallback"); /** 商户通知地址*/
        } else {
            map.put("callbackUrl", "http://127.0.0.1:8083/yingtao/withdraw/joinpay/transferCallback"); /** 商户通知地址*/
        }
        map.put("firstProductCode", ""); /** 优先使用产品*/
        map.put("hmac", getSign(getRequestSign(map)));/** 签名数据 */
        logger.info("请求汇聚参数:" + gson.toJson(map));
        String reqBodyJson = JSON.toJSONString(map);
        String httpResponseJson = HttpClientUtil.sendHttpPost(singlePay, reqBodyJson);
        logger.info("url:{},接收汇聚返回参数：{}", singlePay, httpResponseJson);
        // 响应信息map集合
        Map<String, Object> httpResponseMap = (Map<String, Object>) JSONObject.parse(httpResponseJson);
        // 业务数据map集合
        Map<String, Object> dataMap = (Map<String, Object>) httpResponseMap.get("data");
        dataMap.put("statusCode", httpResponseMap.get("statusCode"));
        dataMap.put("message", httpResponseMap.get("message"));

        // 响应签名串
        String respSign = getResponseSign(dataMap);
        // 请求数据的加密签名
        String reqHmac = getSign(respSign);
        // 请求数据的加密签名
        String respHmac = (String) dataMap.get("hmac");

        reqHmac=reqHmac.toUpperCase();
        respHmac=respHmac.toUpperCase();
        boolean isMatch = reqHmac.equals(respHmac);
        if (isMatch) {
            JoinpayRet joinpayRet = gson.fromJson(httpResponseJson, JoinpayRet.class);
            if (!"2001".equals(joinpayRet.getStatusCode())) {
                throw new WebServiceException("创建订单失败："+joinpayRet.getData().getErrorDesc());
            }
            return joinpayRet;
        } else {
            throw new WebServiceException("创建订单失败：验签失败");
        }
    }

    public JoinpayTransferCallback transferQuery(String merchantOrderNo) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userNo", userNo);// 商户编号
        map.put("merchantOrderNo", merchantOrderNo);//商户订单号

        // 签名
        String hmac = getSign(getQueryRequestSign(map));
        map.put("hmac", hmac);/** 签名数据 */

        // Map转json字符串
        String reqBodyJson = JSON.toJSONString(map);
        String httpResponseJson = HttpClientUtil.sendHttpPost(singlePayQuery, reqBodyJson);
        // 响应信息map集合
        Map<String, Object> httpResponseMap = (Map<String, Object>) JSONObject.parse(httpResponseJson);
        // 业务数据map集合
        Map<String, Object> dataMap = (Map<String, Object>) httpResponseMap.get("data");
        dataMap.put("statusCode", httpResponseMap.get("statusCode"));
        dataMap.put("message", httpResponseMap.get("message"));

        // 请求数据的加密签名
        String reqHmac = getSign(getQueryResponseSign(dataMap));
        // 请求数据的加密签名
        String respHmac = (String) dataMap.get("hmac");

        reqHmac=reqHmac.toUpperCase();
        respHmac=respHmac.toUpperCase();
        boolean isMatch = reqHmac.equals(respHmac);
        if (isMatch) {
            return gson.fromJson(httpResponseJson, JoinpayTransferCallback.class);
        } else {
            throw new WebServiceException("查询订单失败：验签失败");
        }
    }

    public static String getRequestSign(Map<String, Object> params) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(params.get("userNo")).append(params.get("productCode")).append(params.get("requestTime"))
                .append(params.get("merchantOrderNo")).append(params.get("receiverAccountNoEnc"))
                .append(params.get("receiverNameEnc")).append(params.get("receiverAccountType"))
                .append(params.get("receiverBankChannelNo")).append(params.get("paidAmount"))
                .append(params.get("currency")).append(params.get("isChecked")).append(params.get("paidDesc"))
                .append(params.get("paidUse")).append(params.get("callbackUrl")).append(params.get("firstProductCode"));
        return stringBuilder.toString();
    }

    public static String getResponseSign(Map<String, Object> params) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(params.get("statusCode")).append(params.get("message")).append(params.get("errorCode"))
                .append(params.get("errorDesc")).append(params.get("userNo")).append(params.get("merchantOrderNo"));
        return stringBuilder.toString();
    }

    public static String getQueryRequestSign(Map<String, Object> params) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(params.get("userNo")).append(params.get("merchantOrderNo"));
        return stringBuilder.toString();
    }

    public static String getQueryResponseSign(Map<String, Object> params) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(params.get("statusCode")).append(params.get("message"))
                .append(params.get("status"))
                .append(params.get("errorCode"))
                .append(params.get("errorDesc")).append(params.get("userNo"))
                .append(params.get("merchantOrderNo"));
        if(params.get("platformSerialNo") !=null) {
            stringBuilder.append(params.get("platformSerialNo"));
        };
        if(params.get("receiverAccountNoEnc") !=null) {
            stringBuilder.append(params.get("receiverAccountNoEnc"));
        };
        if(params.get("receiverNameEnc") !=null) {
            stringBuilder.append(params.get("receiverNameEnc"));
        };
        if(params.get("paidAmount") !=null) {
            stringBuilder.append(params.get("paidAmount"));
        };
        if(params.get("fee") !=null) {
            stringBuilder.append(params.get("fee"));
        };
        return stringBuilder.toString();
    }

    private String getSign(String str) {
        return signByMD5(str, key);
    }

    private String signByMD5(String requestSign, String merchantKey) {
        return DigestUtils.md5Hex(requestSign + merchantKey).toUpperCase();
    }

    private String getShort(String uuid) {
        if (uuid.length() < 25) {
            return uuid;
        }
        int hashCodeV = uuid.hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        return 1 + String.format("%015d", hashCodeV);
    }


    /**
     * 企业转账(汇聚)
     *
     */
    public JoinpayRet transferRedPackage(JoinpayTransfer joinpayTransfer) throws Exception {
        // 发起汇聚转账
        Map<String, Object> map = Maps.newHashMap();
        map.put("userNo", userNo);/** 商户编号 */
        map.put("productCode", "BANK_PAY_DAILY_ORDER");/** 产品类型 */
        map.put("requestTime", joinpayTransfer.getRequestTime()); /**交易请求时间*/
        map.put("merchantOrderNo", getShort(joinpayTransfer.getMerchantOrderNo()));/**商户订单号*/
        map.put("receiverAccountNoEnc", joinpayTransfer.getReceiverAccountNoEnc()); /**收款账户号 */
        map.put("receiverNameEnc", joinpayTransfer.getReceiverNameEnc()); /** 收款人 */
        map.put("receiverAccountType", 201); /** 账户类型 */
        map.put("receiverBankChannelNo", ""); /** 收款账户联行号 */
        map.put("paidAmount", joinpayTransfer.getPaidAmount()); /** 交易金额*/
        map.put("currency", "201"); /** 币种*/
        map.put("isChecked", "202"); /** 是否复核 */
        map.put("paidDesc", "拉贝红包提现" + joinpayTransfer.getPaidAmount() + "元"); /** 代付说明 */
        map.put("paidUse", "205"); /** 代付用途 */
        if ("prod".equalsIgnoreCase(GlobalConfig.sysEnv)) {
            map.put("callbackUrl", "http://47.94.6.83:8083/yingtao/withdraw/joinpay/transferRedPackageCallback"); /** 商户通知地址*/
        } else {
            map.put("callbackUrl", "http://127.0.0.1:8083/yingtao/withdraw/joinpay/transferRedPackageCallback"); /** 商户通知地址*/
        }
        map.put("firstProductCode", ""); /** 优先使用产品*/
        map.put("hmac", getSign(getRequestSign(map)));/** 签名数据 */
        logger.info("请求汇聚参数:" + gson.toJson(map));
        String reqBodyJson = JSON.toJSONString(map);
        String httpResponseJson = HttpClientUtil.sendHttpPost(singlePay, reqBodyJson);
        logger.info("url:{},接收汇聚返回参数：{}", singlePay, httpResponseJson);
        // 响应信息map集合
        Map<String, Object> httpResponseMap = (Map<String, Object>) JSONObject.parse(httpResponseJson);
        // 业务数据map集合
        Map<String, Object> dataMap = (Map<String, Object>) httpResponseMap.get("data");
        dataMap.put("statusCode", httpResponseMap.get("statusCode"));
        dataMap.put("message", httpResponseMap.get("message"));

        // 响应签名串
        String respSign = getResponseSign(dataMap);
        // 请求数据的加密签名
        String reqHmac = getSign(respSign);
        // 请求数据的加密签名
        String respHmac = (String) dataMap.get("hmac");

        reqHmac=reqHmac.toUpperCase();
        respHmac=respHmac.toUpperCase();
        boolean isMatch = reqHmac.equals(respHmac);
        if (isMatch) {
            JoinpayRet joinpayRet = gson.fromJson(httpResponseJson, JoinpayRet.class);
            if (!"2001".equals(joinpayRet.getStatusCode())) {
                throw new WebServiceException("创建订单失败："+joinpayRet.getData().getErrorDesc());
            }
            return joinpayRet;
        } else {
            throw new WebServiceException("创建订单失败：验签失败");
        }
    }
}
