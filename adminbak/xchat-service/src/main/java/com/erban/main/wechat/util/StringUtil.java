package com.erban.main.wechat.util;

import java.io.UnsupportedEncodingException;


/**
 * 字符串转换类
*    
* 项目名称：WeiChatService   
 */
public class StringUtil {
	
	/** 
     * 将字节数组转换为十六进制字符串 
     *  
     * @param byteArray 
     * @return 
     */  
    public static String byteToStr(byte[] byteArray) {  
        String strDigest = "";  
        for (int i = 0; i < byteArray.length; i++) {  
            strDigest += byteToHexStr(byteArray[i]);  
        }  
        return strDigest;  
    } 
	
	
	/** 
     * 将字节转换为十六进制字符串 
     *  
     * @param mByte 
     * @return 
     */  
    public static String byteToHexStr(byte mByte) {  
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };  
        char[] tempArr = new char[2];  
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];  
        tempArr[1] = Digit[mByte & 0X0F];  
  
        String s = new String(tempArr);  
        return s;  
    }  
    
    /** 
     * 计算采用utf-8编码方式时字符串所占字节数 
     *  
     * @param content 
     * @return 
     */  
    public static int getByteSize(String content) {  
        int size = 0;  
        if (null != content) {  
            try {  
                // 汉字采用utf-8编码时占3个字节   
                size = content.getBytes("unicode").length;  
            } catch (UnsupportedEncodingException e) {  
                e.printStackTrace();  
            }  
        }  
        return size;  
    }  



}
