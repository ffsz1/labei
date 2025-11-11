package com.xchat.oauth2.service.core.util;

import com.google.common.collect.Lists;
import com.xchat.oauth2.service.core.encoder.Base64Utils;
import com.xchat.oauth2.service.core.encoder.MD5Utils;

import java.util.List;
import java.util.Random;

/**
 * 码 生成工具
 * @author liuguofu
 * on 2015/1/23.
 */
public class CoderUtils {

    /**
     * id序列+随机5位字母
     * @return
     */
    public static String simpleId(){
        Long id = Sequence.nextId();
        StringBuilder randomAlpha = new StringBuilder();
        for(int i=0;i<5;i++){
            int intValue=(int)(Math.random()*26+97);
            randomAlpha.append((char)intValue);
        }
        return id+randomAlpha.toString();
    }

    public static String idWithMD5(){
        return MD5Utils.getMD5String(simpleId());
    }

    public static String idWithBase64(){
        return Base64Utils.encode(simpleId().getBytes());
    }

    public static String nLenRandomStr(int len){
        //62
        String[] table = {  "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
                "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
                "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i",
                "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
                "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6",
                "7", "8", "9" };
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for(int i=0;i<len;i++){
            int pos= random.nextInt(62);
            result.append(table[pos]);
        }
        return result.toString();

    }

    /**
     * @param len
     * @return
     */
    public static String nLenRandomStrWithNoUppCase(int len){
        return nLenRandomStr(len).toLowerCase();
    }

    /**
     * 产生不重复的N位字符串
     * @param length
     * @param size
     * @return
     */
    public static List<String> uniqueStrListWithNoUppCase(int length,int size){
        List<String> result = Lists.newArrayListWithCapacity(size);
        String elem;
        for(int i=0;i<size;i++){
            elem = nLenRandomStrWithNoUppCase(length);
            if(!result.contains(elem)){
                result.add(elem);
            }
        }
        return result;
    }
    /**
     * 产生不重复的N位字符串
     * @param length
     * @param size
     * @return
     */
    public static List<String> uniqueStrList(int length,int size){
        List<String> result = Lists.newArrayListWithCapacity(size);
        String elem;
        for(int i=0;i<size;i++){
            elem = nLenRandomStr(length);
            if(!result.contains(elem)){
                result.add(elem);
            }
        }
        return result;
    }

    /**
     * 产生与指定集合里不重复的N位字符串
     * @param length
     * @param size
     * @return
     */
    public static List<String> uniqueStrList(int length,int size,List<String> existsList){
        List<String> result = Lists.newArrayListWithCapacity(size);
        String elem;
        for(int i=0;i<size;i++){
            elem = nLenRandomStr(length);
            if(!existsList.contains(elem) && !result.contains(elem)){
                result.add(elem);
            }
        }
        return result;
    }

    /**
     * 产生N位数字串
     * @param length 数字串位数
     * @return
     */
    public static String nLenWithNum(int length){
        int len = 0;
        String result = "";
        do{
//            result += String.valueOf(RandomUtils.nextInt(1,10));
            len ++;
        }while (len<length);
        return result;
    }

    public static void main(String[] args) {
        /*System.out.println("simpleId:"+simpleId());
        System.out.println("idWithMD5:"+idWithMD5());
        System.out.println("idWithBase64:"+idWithBase64());*/
//        System.out.println(Base64Utils.encode("123456".getBytes()));

        List<String> list = uniqueStrListWithNoUppCase(6,650);
        for (String str:list){
            System.out.println(str);

        }
    }
}
