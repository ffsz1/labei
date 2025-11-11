package com.yingtao.ndklib;

import android.content.Context;

public class JniUtils {

    static {
        System.loadLibrary("ndklib-yingtao");
    }

    /**
     * @param type 1为测试环境，0为正式环境
     */
    public native static String getDk(Object context, int type);

    public native static String getAk(Object context);

    public native static String getAkIv(Object context);

    /**
     * AES加密
     */
    public static String encryptAes(Context context, String content) throws Exception {
        return EncryptUtils.encryptAes(content, getAk(context), getAkIv(context));
    }

    /**
     * AES解密
     */
    public static String decryptAes(Context context, String content) throws Exception {
        return EncryptUtils.decryptAes(content, getAk(context), getAkIv(context));
    }
}
