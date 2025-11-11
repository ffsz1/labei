package com.juxiao.xchat.manager.external.ecpss.impl;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.juxiao.xchat.base.utils.DateFormatUtils;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.external.ecpss.EcpssManager;
import com.juxiao.xchat.manager.external.ecpss.bo.OrderCallBackBO;
import com.juxiao.xchat.manager.external.ecpss.bo.QueryOrderBO;
import com.juxiao.xchat.manager.external.ecpss.utils.EcpssUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: EcpssManagerImpl
 * @Description: 汇潮支付Impl
 * @Author: alwyn
 * @Date: 2019/5/27 20:12
 * @Version: 1.0
 */
@Service
public class EcpssManagerImpl implements EcpssManager {

    @Autowired
    private SystemConf systemConf;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 商户订单号
     */
    private String merid = "197041S190614003";
    /**
     * 商户秘钥
     */
    private String key = "n0ORbXSUW4azupgiQqGv5as6GmWltYo6";
    /**
     * 支付成功回调通知--测试环境
     */
//    private static final String TEST_NOTIFY_URL = "http:// .com/charge/ecpss/callback";
    private static final String TEST_NOTIFY_URL = "http://域名/charge/ecpss/callback";
    /**
     * 支付成功回调通知--生产环境
     */
    private static final String PROD_NOTIFY_URL = "http://域名/charge/ecpss/callback";
    /**
     * 支付宝创建订单-URL
     */
    private static final String ALIPAY_CREATE_ORDER_URL = "https://alipay.3c-buy.com/api/createOrder";
    /**
     * 订单状态查询-URL
     */
    private static final String QUERY_ORDER_URL = "http://jh.chinambpc.com/api/queryOrder";

    @Autowired
    private Gson gson;

    @Override
    public String alipayOrder(String orderNo, int amount, Long uid, String mchItem) {
        Map<String, String> paraMap = new HashMap<String, String>(16);
        double moeny = amount;
        paraMap.put("merid", merid);
        paraMap.put("noncestr", UUIDUtils.get());
        paraMap.put("orderTime", DateFormatUtils.YYYYMMDDHHMMSS.date2Str(new Date()));
        if ("prod".equalsIgnoreCase(systemConf.getEnv())) {
            // 正式环境
            paraMap.put("notifyUrl", PROD_NOTIFY_URL);
        } else {
            paraMap.put("notifyUrl", TEST_NOTIFY_URL);
        }
        paraMap.put("merchantOutOrderNo", orderNo);
        paraMap.put("orderMoney", String.valueOf(moeny / 100));
        String stringA = EcpssUtils.formatUrlMap(paraMap, false, false);
        String stringsignTemp = stringA + "&key=" + key;
        // 不参与加密的参数
        if (StringUtils.isNotBlank(mchItem)) {
            paraMap.put("mchItem", mchItem);
        }
        if (uid != null) {
            paraMap.put("id", uid.toString());
        }
        String param = EcpssUtils.formatUrlMap(paraMap, true, false);
        String url = ALIPAY_CREATE_ORDER_URL + "?" + param + "&sign=" + EcpssUtils.getMD5(stringsignTemp);
        logger.info("[汇潮支付]:下单请求{}", url);
        return url;

    }

    @Override
    public QueryOrderBO queryOrder(String orderNo) throws WebServiceException {
        //拼装参数
        Map<String, String> param = Maps.newHashMap();
        param.put("merchantOutOrderNo", orderNo);
        param.put("merid", merid);
        param.put("noncestr", UUIDUtils.get());
        //将参数转换为key=value形式
        String paramStr = EcpssUtils.formatUrlMap(param, false, false);
        //在最后拼接上密钥
        String signStr = paramStr + "&key=" + key;
        //MD5签名
        String sign = EcpssUtils.getMD5(signStr);
        //拼接签名
        paramStr = paramStr + "&sign=" + sign;
        QueryOrderBO orderBO;
        // 查询结果
        String order = EcpssUtils.sendPost(QUERY_ORDER_URL, paramStr, "UTF-8");
        logger.info("[汇潮支付订单查询]订单号,orderNo:{},订单信息:{}", orderNo, order);
        if (StringUtils.isNotBlank(order)) {
            orderBO = gson.fromJson(order, QueryOrderBO.class);
        } else {
            orderBO = new QueryOrderBO();
        }
        return orderBO;
    }

    @Override
    public OrderCallBackBO verifySignature(OrderCallBackBO callBackBO) throws WebServiceException {
        //拼接签名参数
        Map<String, String> signParamMap = Maps.newHashMap();
        signParamMap.put("merchantOutOrderNo", callBackBO.getMerchantOutOrderNo());
        signParamMap.put("merid", merid);
        signParamMap.put("msg", callBackBO.getMsg());
        signParamMap.put("noncestr", callBackBO.getNoncestr());
        signParamMap.put("orderNo", callBackBO.getOrderNo());
        signParamMap.put("payResult", callBackBO.getPayResult() + "");
        //转换为key=value模式
        String signParam = EcpssUtils.formatUrlMap(signParamMap, false, false);
        //生成签名
        String signLocal = EcpssUtils.getMD5(signParam + "&key=" + key);
        //对比签名
        if (signLocal.equals(callBackBO.getSign())) {
            logger.info("[汇潮支付回调]验签成功,callBack:{}", callBackBO);
            return callBackBO;
        }
        logger.error("[汇潮支付回调]验签失败,callBack:{}", callBackBO);
        throw new WebServiceException(WebServiceCode.PARAM_ERROR);
    }
}
