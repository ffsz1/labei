package com.tongdaxing.xchat_framework.util.util.codec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by huiping on 14/8/25.
 */
public class DESUtils {
    private final static String SECRET_KEY = "asfje87s";
    private final static String IV = "01234567";

    public static String encrypt(String plainText, String secretKey) throws Exception {
        if (plainText == null || plainText.length() == 0 || secretKey == null || secretKey.length() == 0) {
            return null;
        }
        IvParameterSpec zeroIv = new IvParameterSpec(IV.getBytes());
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(plainText.getBytes());
        return Base64Utils.encodeToString(encryptedData, Base64Utils.NO_WRAP);
    }

    public static String encrypt(String plainText) throws Exception {
        return encrypt(plainText, SECRET_KEY);
    }

    public static String decrypt(String plainText, String secretKey) throws Exception {
        if (plainText == null || plainText.length() == 0 || secretKey == null || secretKey.length() == 0) {
            return null;
        }
        byte[] byteMi = Base64Utils.decode(plainText, Base64Utils.NO_WRAP);
        IvParameterSpec zeroIv = new IvParameterSpec(IV.getBytes());
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte decryptedData[] = cipher.doFinal(byteMi);

        return new String(decryptedData);
    }

    public static String decrypt(String plainText) throws Exception {
        return decrypt(plainText, SECRET_KEY);
    }
}
