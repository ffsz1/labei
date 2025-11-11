package com.erban.main.service.api;


import com.erban.main.model.Users;
import com.erban.main.service.base.BaseService;
import com.xchat.common.wx.MD5;
import com.xchat.oauth2.service.domain.user.User;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 微信支付
 */
@Service
public class WxpayService extends BaseService {
    // 统一下单接口
    private static final String API_UNIFIED_ORDER ="https://api.mch.weixin.qq.com/pay/unifiedorder";

    private static final String API_KEY = "6475328bc85411e793637cd30abdabb2";
    private static final String MCH_ID = "1484534822";  // 商户ID
    private static final String APPID = "wx9a8220001aa6c5c9";   // 应用ID


    /**
     * 统一下单接口
     *
     * @param body
     * @param outTradeNo
     * @param totalFee
     * @param spbillCreateIp
     */
    public void createPreOrder(String body, String outTradeNo,String totalFee,String spbillCreateIp){

    }

    /**
     * 生成一个随机字符串
     *
     * @return
     */
    private String generRandomStr() {
        Random random = new Random();
        return random.nextInt(999999999) + "";
    }

    /**
     *  生成签名
     *
     * @param characterEncoding
     * @param parameters
     * @return
     */
    public static String createSign(String characterEncoding,SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + API_KEY);
        String sign = MD5.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    /**
     * 将请求参数转换成xml的字符串
     *
     * @param parameters
     * @return
     */
    public String getRequestXml(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)|| "sign".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    public static void main(String[] args) {

        Users users = new Users();

        WxpayService wxpayService = new WxpayService();

        for (int i = 0; i < 20; i++) {
            System.out.println(wxpayService.generRandomStr());
        }
    }
}
