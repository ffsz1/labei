package com.juxiao.xchat.base.utils;

import java.net.URLEncoder;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


@SuppressWarnings("restriction")
public class DESUtils {
	private static BASE64Decoder decoder = new BASE64Decoder();
	private static BASE64Encoder encoder = new BASE64Encoder();
    private static String DES = "DES";
    private static String ENCODE = "UTF-8";//保持平台兼容统一使用utf-8

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

    //客户端加密
    public static String DESAndBase64Encrypt(String dataToEncypt, String pwd) throws Exception {
        byte[] encryptData = encryptByteDES(dataToEncypt.getBytes(ENCODE), pwd);
        String dataBase64 = encoder.encode(encryptData);
        return dataBase64;
    }

    //服务端解密
    public static String DESAndBase64Decrypt(String dataBase64, String pwd) throws Exception {
        byte[] encryptedData = decoder.decodeBuffer(dataBase64);
        byte[] decryptedData = decryptByteDES(encryptedData, pwd);
        String textDecrypt = new String(decryptedData, ENCODE);
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

    public static void main(String[] args) {
        try {
            String en = DESAndBase64Encrypt("app=xchat&appVersion=1.1.6.1&os=android&ticket=eyJhbGciOiJIUzI1NiJ9.eyJ0aWNrZXRfdHlwZSI6bnVsbCwidWlkIjoxMDA5NTIsInRpY2tldF9pZCI6ImRkNDhjNmY2LWQ0NjAtNGJkNy1iZDgzLWRiNTc3YjNiNTBhZSIsImV4cCI6MzYwMCwiY2xpZW50X2lkIjoiZXJiYW4tY2xpZW50In0.Uu8TRq84Fi8d3C05sJrm3jPY5V7gSHYnUlDGJfi7yXM&netType=1&channel=mm&appCode=17&deviceId=dd792fa4-1c30-3e3b-870b-cd36b24f7628&uid=100952&osVersion=7.1.1&model=SM-C7100&ispType=3&queryUid=100433", "E#R$T%Y^");
            System.out.println(URLEncoder.encode(en, "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}