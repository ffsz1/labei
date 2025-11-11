package com.xchat.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.util.Base64;


/**
 * AES加解密
 */
public class AESUtils {
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_STR = "AES/CBC/PKCS5Padding";

    private static AlgorithmParameters generateIv(byte[] iv) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }

    public static String decrypt(String content, String keys, String ivs) throws Exception {
        return decrypt(Base64.getDecoder().decode(content), keys.getBytes(), ivs.getBytes());
    }

    public static String decrypt(byte[] content, byte[] keys, byte[] ivs) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
        Key secretKey = new SecretKeySpec(keys, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, generateIv(ivs));
        byte[] result = cipher.doFinal(content);
        return new String(result);
    }

    public static String encrypt(String content, String keys, String iv) throws Exception {
        return encrypt(content.getBytes(), keys.getBytes(), iv.getBytes());
    }

    public static String encrypt(byte[] content, byte[] keys, byte[] ivs) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_STR); // 创建密码器
        Key secretKey = new SecretKeySpec(keys, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, generateIv(ivs));
        return Base64.getEncoder().encodeToString(cipher.doFinal(content));
    }


    public static void main(String[] args) throws Exception {
        String content = "app=xchat&appVersion=1.1.3&os=android&netType=2&channel=mm&issue_type=multi&appCode=14&deviceId=7cf22b77-70cf-352c-a884-ba26985a6b63&access_token=eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOlsiZXJiYW5hcHAtcmVzb3VyY2UiXSwidXNlcl9uYW1lIjoiMTc2ODE4NzY5OTUiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNTQ2MTU2NjMyLCJhdXRob3JpdGllcyI6WyJST0xFX2FjY291bnQiLCJST0xFX01PQklMRSIsIlJPTEVfVU5JVFkiXSwianRpIjoiZGMwODQ5MDgtYjgyMy00NTQyLTg5MWMtOGEwY2ViNzgwMjY4IiwiY2xpZW50X2lkIjoiZXJiYW4tY2xpZW50In0.XnKwv0mdyhvKZIP8XKuJTIecVsPBF-pTOnfMthUWRm8&osVersion=8.1.0&model=COR-AL00&ispType=4";
        String keys = "E#R$T%Y^13579000";
        String iv = "Q!W@E#R$T%Y^U&I*";
        String encryptString = encrypt(content.getBytes(), keys.getBytes(), iv.getBytes());
        System.out.println("encryptString:" + encryptString); // 加密
        System.out.println(URLEncoder.encode(encryptString, "utf-8"));
        System.out.println(decrypt(Base64.getDecoder().decode(encryptString), keys.getBytes(), iv.getBytes()));

    }
}
