package com.tongdaxing.xchat_framework.util.util.codec;

import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.security.MessageDigest;

public class SHAUtils {

    private static final String TAG = "SHAUtils";

    private static String digest(String s, String algorithm) {
        if (s == null || s.length() == 0 || algorithm == null || algorithm.length() == 0) {
            return null;
        }
        String sha = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(s.getBytes("UTF-8"));
            sha = bytesToHexString(digest.digest());
        } catch (Exception e) {
            MLog.error(TAG, "digest error! " + e.toString());
        }
        return sha;
    }

    public static String getSHA1(String s) {
        return digest(s, "SHA-1");
    }

    public static String getSHA256(String s) {
        return digest(s, "SHA-256");
    }

    public static String getSHA384(String s) {
        return digest(s, "SHA-384");
    }

    public static String getSHA512(String s) {
        return digest(s, "SHA-512");
    }

    public static String getSHA(String s, String algorithm) {
        return digest(s, algorithm);
    }

    /**
     * byte数组转为hex字符串
     *
     * @param bytes
     * @return
     */
    private static String bytesToHexString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
