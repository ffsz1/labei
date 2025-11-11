package com.xchat.common.wx;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.*;

public class PayUtil {
    private static Logger logger = LoggerFactory.getLogger(PayUtil.class);

    public static String getTime_stamp() {
        /*String stamp="1472275204";
         *//*Date d=new Date();
		d.getTime();*//*
		return stamp;*/

        String stamp = String.valueOf(new Date().getTime());
        return stamp;

    }

    public static String getRandomStringByLength(int len) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String getSign(Map<String, String> map, String key) {
        List<String> list = new ArrayList<String>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }

            if ("result_code".equals(entry.getKey())) {
                logger.error(entry.getKey());
                continue;
            }

            if ("return_code".equals(entry.getKey())) {
                logger.error(entry.getKey());
                continue;
            }

            if ("return_msg".equals(entry.getKey())) {
                logger.error(entry.getKey());
                continue;
            }

            list.add(entry.getKey() + "=" + String.valueOf(entry.getValue()) + "&");
        }

        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }

        String result = sb.toString();
        //key 该方法key的需要根据你当前公众号的key进行修改
        result += "key=" + key;
        String md5 = MD5.MD5Encode(result).toUpperCase();
        logger.info("[ 微信支付 ]签名字符:{},签名:{}", result, md5);
        return md5;
    }

    public static String getPaySign(Map<String, String> map, String key) {
        List<String> list = new ArrayList<String>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if ("sign".equalsIgnoreCase(entry.getKey())) {
                continue;
            }
            list.add(entry.getKey() + "=" + String.valueOf(entry.getValue()) + "&");
        }

        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }

        String result = sb.toString();
        //key 该方法key的需要根据你当前公众号的key进行修改
        result += "key=" + key;
        String md5 = MD5.MD5Encode(result).toUpperCase();
        logger.info("[ 微信支付 ]签名字符:{},签名:{}", result, md5);
        return md5;
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        String string = "<xml>\n" +
                "  <appid><![CDATA[wx009d793f92c24eec]]></appid>\n" +
                "  <attach><![CDATA[支付测试]]></attach>\n" +
                "  <bank_type><![CDATA[CFT]]></bank_type>\n" +
                "  <fee_type><![CDATA[CNY]]></fee_type>\n" +
                "  <is_subscribe><![CDATA[Y]]></is_subscribe>\n" +
                "  <mch_id><![CDATA[1484701192]]></mch_id>\n" +
                "  <nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str>\n" +
                "  <openid><![CDATA[o5fpU1bSN0b8xcU3syC8Md8ZfrJ4]]></openid>\n" +
                "  <out_trade_no><![CDATA[1409811653]]></out_trade_no>\n" +
                /* "  <sign><![CDATA[C96E9728B45C69D9CE093A7D733EB8E2]]></sign>\n" +*/
                "  <sub_mch_id><![CDATA[10000100]]></sub_mch_id>\n" +
                "  <time_end><![CDATA[20140903131540]]></time_end>\n" +
                "  <total_fee>1</total_fee>\n" +
                "  \n" +
                "  <trade_type><![CDATA[JSAPI]]></trade_type>\n" +
                "  <transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id>\n" +
                "</xml>";

        Map map = XMLParser.getMapFromXML(string);
        String s = PayUtil.getSign(map, "");
        System.out.println(s);//"0C1D1ADD0DD75F9F90D6CF48C90E5AB0"
    }
}
