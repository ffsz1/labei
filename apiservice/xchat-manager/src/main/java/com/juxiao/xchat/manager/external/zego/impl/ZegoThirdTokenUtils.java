package com.juxiao.xchat.manager.external.zego.impl;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class ZegoThirdTokenUtils {
    static final private int IV_LENGTH = 16;

    static final private String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    public static byte[] encrypt(String content, byte[] secretKey) throws Exception {
        SecretKeySpec key = new SecretKeySpec(secretKey, "AES");
        SecureRandom rnd = new SecureRandom();
        byte[] newSeed = rnd.generateSeed(IV_LENGTH);
        rnd.setSeed(newSeed);
        byte[] ivBytes = new byte[IV_LENGTH];
        rnd.nextBytes(ivBytes);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] contentBytes = cipher.doFinal(content.getBytes("UTF-8"));
        byte[] encryptedBytes = new byte[ivBytes.length + contentBytes.length];
        System.arraycopy(ivBytes, 0, encryptedBytes, 0, ivBytes.length);
        System.arraycopy(contentBytes, 0, encryptedBytes, ivBytes.length, contentBytes.length);
        return Base64.getEncoder().encode(encryptedBytes);

    }
}
