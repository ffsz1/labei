package com.tongdaxing.xchat_framework.util.util;

/**
 * /**
 *
 * @author liuguofu
 */

import android.util.Base64;

import java.nio.charset.Charset;
import java.security.Key;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;


public class DESUtils {

//    static BASE64Decoder decoder = new BASE64Decoder();
//    static BASE64Encoder encoder = new BASE64Encoder();

    static String DES = "DES";
    static String ENCODE = "UTF-8";//保持平台兼容统一使用utf-8
    public static String giftCarSecret = "MIIBIjANBgkqhkiG9w0B";

    //des 加密
    private static byte[] encryptByteDES(byte[] byteD, String strKey) throws Exception {
        return doEncrypt(byteD, getKey(strKey), DES);
    }

    //des 解密
    private static byte[] decryptByteDES(byte[] byteD, String strKey) throws Exception {
        return doDecrypt(byteD, getKey(strKey), DES);
    }

    public static SecretKey getKey(String strKey) throws Exception {
        DESKeySpec desKeySpec = new DESKeySpec(strKey.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey sk = keyFactory.generateSecret(desKeySpec);
        return sk;
    }


    public static void main(String[] args) throws Exception {

        String text = "admin";
//        String pwd = "NJD783iSDK0d9fjd98KKDf9O";
        String pwd = "1ea53d260ecf11e7b56e00163e046a26";
        //客户端加密
//        String data = DESAndBase64Encrypt(text, pwd);
//        System.out.println("text DES加密后base64：" + URLEncoder.encode(data, "UTF-8"));

        //服务端解密
//        String textDecrypt = DESAndBase64Decrypt(data, pwd);
//        System.out.println("未处理原文：" + text);
//        System.out.println("解密后数据：" + textDecrypt);


//        byte[] bytes = text.getBytes();
//        byte[] encode = Base64.encode(bytes,Base64.DEFAULT);

    }

    //客户端加密
    public static String DESAndBase64Encrypt(String dataToEncypt) throws Exception {
        byte[] encryptData = encryptByteDES(replaceBlank(dataToEncypt).getBytes(ENCODE), "1ea53d260ecf11e7b56e00163e046a26");
        byte[] encode = Base64.encode(encryptData, Base64.DEFAULT);
        String dataBase64 = StringUtils.toEncodedString(encode, Charset.defaultCharset());
        return replaceBlank(dataBase64);
    }

    public static String DESAndBase64Encrypt(String dataToEncypt, String key) throws Exception {
        byte[] encryptData = encryptByteDES(replaceBlank(dataToEncypt).getBytes(ENCODE), key);
        byte[] encode = Base64.encode(encryptData, Base64.DEFAULT);
        String dataBase64 = StringUtils.toEncodedString(encode, Charset.defaultCharset());
        return replaceBlank(dataBase64);
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    //服务端解密
    public static String DESAndBase64Decrypt(String dataBase64) throws Exception {
//        Log.d("DESAndBase64Decrypt", dataBase64);

        return DESAndBase64Decrypt(dataBase64, "VvXh6qc55O292wdw68cvgQ");
    }

    //服务端解密
    public static String DESAndBase64Decrypt(String dataBase64, String key) throws Exception {
//        Log.d("DESAndBase64Decrypt", dataBase64);
        if (StringUtils.isEmpty(dataBase64)) return null;
        byte[] encryptedData = Base64.decode(dataBase64, Base64.DEFAULT);
        byte[] decryptedData = decryptByteDES(encryptedData, key);
        String textDecrypt = new String(decryptedData, ENCODE);
//        LogUtil.d("DESAndBase64Decrypt", textDecrypt);
        return textDecrypt;
    }

    /**
     * 执行加密操作
     *
     * @param data 待操作数据
     * @param key  Key
     * @param type 算法 RSA or DES
     * @return
     * @throws Exception
     */
    private static byte[] doEncrypt(byte[] data, Key key, String type) throws Exception {
        Cipher cipher = Cipher.getInstance(type);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * 执行解密操作
     *
     * @param data 待操作数据
     * @param key  Key
     * @param type 算法 RSA or DES
     * @return
     * @throws Exception
     */
    private static byte[] doDecrypt(byte[] data, Key key, String type) throws Exception {
        Cipher cipher = Cipher.getInstance(type);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }
}