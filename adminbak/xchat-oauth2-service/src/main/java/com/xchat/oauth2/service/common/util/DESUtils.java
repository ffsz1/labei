package com.xchat.oauth2.service.common.util;

/**
 * @author liuguofu
 */

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.net.URLEncoder;
import java.security.Key;


public class DESUtils {

    static BASE64Decoder decoder = new BASE64Decoder();
    static BASE64Encoder encoder = new BASE64Encoder();

    static String DES = "DES";
    static String ENCODE = "UTF-8";//保持平台兼容统一使用utf-8

    //des 加密
    private static byte [] encryptByteDES(byte[] byteD,String strKey) throws Exception {
        return doEncrypt(byteD, getKey(strKey), DES);
    }
    //des 解密
    private static byte [] decryptByteDES(byte[] byteD,String strKey) throws Exception {
        return doDecrypt(byteD, getKey(strKey), DES);
    }

    public static SecretKey getKey(String strKey)  throws Exception {
        DESKeySpec desKeySpec = new DESKeySpec(strKey.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey sk = keyFactory.generateSecret(desKeySpec);
        return sk;
    }

    public static void main(String[] args) throws Exception {

        String text = "liuguofuadmin";
        text = "1234567890";
        String pwd="NJD783iSDK0d9fjd98KKDf9O";
        pwd="1ea53d260ecf11e7b56e00163e046a26";
        //客户端加密
        String data = DESAndBase64Encrypt(text, pwd);
        System.out.println("text DES加密后base64："+ URLEncoder.encode(data,"UTF-8"));

//        data="e9k2ODx2%2BmQ%3D%0A";
        //服务端解密
        String textDecrypt = DESAndBase64Decrypt(data, pwd);
        System.out.println("未处理原文："+text);
        System.out.println("解密后数据："+textDecrypt);
    }

    //客户端加密
    public static String DESAndBase64Encrypt(String dataToEncypt,String pwd) throws Exception{

        byte[] encryptData = encryptByteDES(dataToEncypt.getBytes(ENCODE), pwd);
        String dataBase64 = encoder.encode(encryptData);

        return dataBase64;
    }
    //服务端解密
    public static String DESAndBase64Decrypt(String dataBase64,String pwd) throws Exception {

        byte[] encryptedData = decoder.decodeBuffer(dataBase64);
        byte[] decryptedData  = decryptByteDES(encryptedData, pwd);
        String textDecrypt = new String(decryptedData,ENCODE);
        return textDecrypt;
    }


    /**
     * 执行加密操作
     * @param data 待操作数据
     * @param key Key
     * @param type 算法 RSA or DES
     * @return
     * @throws Exception
     */
    private static byte[] doEncrypt(byte[] data,Key key,String type) throws Exception{
        Cipher cipher = Cipher.getInstance(type);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * 执行解密操作
     * @param data 待操作数据
     * @param key Key
     * @param type 算法 RSA or DES
     * @return
     * @throws Exception
     */
    private static byte[] doDecrypt(byte[] data,Key key,String type) throws Exception{
        Cipher cipher = Cipher.getInstance(type);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }
}
