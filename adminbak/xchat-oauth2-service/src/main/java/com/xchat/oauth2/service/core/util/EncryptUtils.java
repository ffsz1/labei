package com.xchat.oauth2.service.core.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author liuguofu
 */
public final class EncryptUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptUtils.class);

    private static final String HMAC_SHA1 = "HmacSHA1";

    private EncryptUtils() {
        // empty
    }

    public static boolean checkMd5sum(String data, String sign) {
        String md5sum = md5Sum(data);
        LOGGER.info("checkeMd5sum data:{} mySign:{} sign:{}", new Object[]{data, md5sum, sign});
        return md5sum.equalsIgnoreCase(sign);
    }

    public static String md5Sum(String data) {
        return getMD5(data);
    }

    public static boolean checkHmacSHA1(String encryptKey, String data, String sign) {
        try {
            String signData = hmacSHA1(data, encryptKey);
            LOGGER.info("checkHmacSHA1 data:{} mySign:{} sign:{}", new Object[]{data, signData, sign});
            return signData.equalsIgnoreCase(sign);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 生成签名数据
     *
     * @param data 待加密的数据
     * @param key  加密使用的key
     * @return 加密后的字符串，纯小写
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    public static String hmacSHA1(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data);
        return byte2hex(rawHmac);
    }

    /**
     * 加密
     *
     * @param str - 待加密的数据
     * @param key - 加密使用的key
     * @return 正常情况下返回加密后的字符串，纯小写；如果出错，返回null
     */
    public static String hmacSHA1(String str, String key) {
        try {
            byte[] data = str.getBytes("UTF-8");
            byte[] keyData = key.getBytes("UTF-8");
            return hmacSHA1(data, keyData);
        } catch (Exception e) {
        }
        return StringUtils.EMPTY;
    }

    public static String getMD5(String str) {
        return encode(str, "MD5");
    }

    public static String getSHA1(String str) {
        return encode(str, "SHA-1");
    }

    public static String getLittleMD5(String str) {
        String estr = encode(str, "MD5");
        return estr.substring(0, 20);
    }

    public static String getLittleSHA1(String str) {
        String estr = encode(str, "SHA-1");
        return estr.substring(0, 20);
    }

    private static String encode(String str, String type) {
        try {
            MessageDigest alga = MessageDigest.getInstance(type);
            alga.update(str.getBytes("UTF-8"));
            byte[] digesta = alga.digest();
            return byte2hex(digesta);
        } catch (Exception e) {
        }
        return StringUtils.EMPTY;
    }

    public static String uuid(String[] strs) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            for (int i = 0; i < strs.length; i++) {
                if (!StringUtils.isEmpty(strs[i])) {
                    md.update(strs[i].getBytes());
                }
            }
            byte[] bs = md.digest();
            return byte2hex(bs);
        } catch (Exception e) {
        }
        return StringUtils.EMPTY;
    }

    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        for (int n = 0; n < b.length; n++) {
            String stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString();
    }

    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * 异或加密
     *
     * @param str
     * @param key
     * @return
     */
    public static String xorEncrypt(String str, String key) {
        BigInteger strbi = new BigInteger(str.getBytes());
        BigInteger keybi = new BigInteger(key.getBytes());
        BigInteger encryptbi = strbi.xor(keybi);

        return new String(encryptbi.toByteArray());
    }

    /**
     * 异或解密
     *
     * @param encryptStr
     * @param key
     * @return
     */
    public static String xorDecrypt(String encryptStr, String key) {
        BigInteger encryptbi = new BigInteger(encryptStr.getBytes());
        BigInteger keybi = new BigInteger(key.getBytes());
        BigInteger decryptbi = encryptbi.xor(keybi);
        return new String(decryptbi.toByteArray());
    }

    public static void main(String[] args) {
        System.out.println(getSHA1("ni hao a "));
    }

}
