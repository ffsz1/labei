package com.erban.main.util;

import com.alibaba.fastjson.JSONObject;
import com.erban.main.config.WxConfig;
import com.erban.main.service.wx.WxService;
import com.google.gson.Gson;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class OpenIdUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxService.class);

    public static JSONObject oauth2GetOpenid(String code, String classify) {
        String appid = "";
        String appsecret = "";
        switch (classify) {
            case "1":
                //自己的配置appid
                appid = "wx534aae2afa0bcc0d";
                //自己的配置APPSECRET;
                appsecret = "d941a1b705d4611001a4d991de176066";
                break;
            case "2":
                appid = "wxf3394ba97e0b550d";
                appsecret = "d6c9dde5b15702153de8e80b323bc61d";
                break;
            case "3":
                appid = "**********";
                appsecret = "************";
                break;
            case "4":
                appid = "**********";
                appsecret = "************";
                break;
            case "5":
                appid = "**********";
                appsecret = "************";
        }
        //授权（必填）
        String grant_type = "authorization_code";
        //URL
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        //请求参数
        String params = "appid=" + appid + "&secret=" + appsecret + "&js_code=" + code + "&grant_type=" + grant_type;
        //发送请求
        String data = HttpUtil.get(requestUrl, params);
        LOGGER.info("[ getSnsapiBaseinfo ] 请求:{}?{},返回:>{}", requestUrl, params, data);
        Map<String, Object> map = new HashMap<>();
        //解析相应内容（转换成json对象
        Gson gson = new Gson();
        JSONObject json = new JSONObject(gson.fromJson(data, map.getClass()));
        return json;
    }

    /**
     * 获取信息
     */
    public static JSONObject getUserInfo(String encryptedData, String sessionKey, String iv){
        // 被加密的数据
        byte[] dataByte = Base64.getDecoder().decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.getDecoder().decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.getDecoder().decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                Map<String, Object> map = new HashMap<>();
                //解析相应内容（转换成json对象
                Gson gson = new Gson();
                return new JSONObject(gson.fromJson(result, map.getClass()));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

}
