package com.xchat.oauth2.service.service.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.HttpUtils;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.service.wx.bean.AuthorizationCodeResBean;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;

@Service
public class WeixinAuthService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 登录凭证校验接口
     */
    private final String authUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    private final String appId = "wxc5e7b2a205845e4b";

    private final String secret = "ebc8a00460aa53e9290e002272548bc4";

    public BusiResult<JSONObject> auth(String code, String iv, String signature, String encryptedData) throws Exception {
        String url = String.format(authUrl, appId, secret, code);
        String json = HttpUtils.executeGet(url);
        AuthorizationCodeResBean resBean = JSON.parseObject(json, AuthorizationCodeResBean.class);
        if (StringUtils.isNotBlank(resBean.getErrcode())) {
            logger.warn("[ 小程序登录 ]登录异常，返回:>{}", json);
            return new BusiResult<>(BusiStatus.NOAUTHORITY, resBean.getErrcode() + ":" + resBean.getErrmsg(), null);
        }

        logger.info("[ 小程序登录 ]登录成功，返回:>{}", json);
        // 被加密的数据
        byte[] dataByte = Base64.decodeBase64(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decodeBase64(resBean.getSession_key());
        // 偏移量
        byte[] ivByte = Base64.decodeBase64(iv);
        // 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
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
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
        AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
        parameters.init(new IvParameterSpec(ivByte));
        cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
        byte[] resultByte = cipher.doFinal(dataByte);
        if (null != resultByte && resultByte.length > 0) {
            String result = new String(resultByte, "UTF-8");
            return new BusiResult<>(BusiStatus.SUCCESS, JSON.parseObject(result));
        }
        return new BusiResult<>(BusiStatus.SERVERERROR);
    }
}
