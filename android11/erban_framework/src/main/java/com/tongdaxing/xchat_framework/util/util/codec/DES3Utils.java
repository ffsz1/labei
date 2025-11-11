package com.tongdaxing.xchat_framework.util.util.codec;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by Zhanghuiping on 14/6/5.
 */

public class DES3Utils {
    private final static String SECRET_KEY = "asfje87sj08$%^ewj937#@#4jsn";
    private final static String IV = "01234567";
    private final static String ENCODING = "utf-8";

    public static String encrypt(String plainText) throws Exception {
        return encrypt(plainText, SECRET_KEY);
    }

    /**
     * @param plainText plainText
     * @param secretKey secretKey , min length is 24(DESedeKeySpec.DES_EDE_KEY_LEN)
     * @return encryptText
     * @throws Exception
     */
    public static String encrypt(String plainText, String secretKey) throws Exception {
        if (plainText == null || plainText.length() == 0 || secretKey == null || secretKey.length() == 0) {
            return null;
        }
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
        deskey = keyFactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(ENCODING));
        return Base64Utils.encodeToString(encryptData, Base64Utils.NO_WRAP);
    }

    public static String decrypt(String encryptText) throws Exception {
        return decrypt(encryptText, SECRET_KEY);
    }

    public static String decrypt(String encryptText, String secretKey) throws Exception {
        if (encryptText == null || encryptText.length() == 0 || secretKey == null || secretKey.length() == 0) {
            return null;
        }
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
        deskey = keyFactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        byte[] decryptData = cipher.doFinal(Base64Utils.decode(encryptText, Base64Utils.NO_WRAP));

        return new String(decryptData, ENCODING);
    }

}
