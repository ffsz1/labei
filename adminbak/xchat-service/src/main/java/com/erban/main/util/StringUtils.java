package com.erban.main.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author liuguofu
 *         on 12/8/14.
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static Integer splitAndGetInt(String str,String separator,Integer index){
        return NumberUtils.toInt(splitAndGetString(str,separator,index),0);
    }

    public static Integer splitAndGetInt(String str,String separator,int index, Integer defaultValue){
        return NumberUtils.toInt(splitAndGetString(str,separator,index),defaultValue);
    }

    public static Long splitAndGetLong(String str,String separator,int index){
        return NumberUtils.toLong(splitAndGetString(str,separator,index),0l);
    }

    public static Long splitAndGetLong(String str,String separator,int index, Long defaultValue){
        return NumberUtils.toLong(splitAndGetString(str,separator,index),defaultValue);
    }

    public static String splitAndGetString(String str,String separator,int index){
        return splitAndGetString(str,separator,index,null);
    }
    public static String splitAndGetString(String str,String separator,int index, String defaultValue){
        if(str == null) {
            return null;
        }
        String[] values = str.split(separator);
        if(index < values.length) {
            return values[index];
        }
        return defaultValue;
    }

    public static Map<String,String> splitToMap(String str,String separator){
        if(str == null) {
            return null;
        }
        String[] strArray = str.split(separator);
        Map<String,String> ret = Maps.newHashMap();
        for(int i = 0; i < strArray.length; i++){
            ret.put("key"+i,strArray[i]);
        }
        return ret;
    }

    public static Map<String,String> splitToMap(String str, String firstSeparator, String secondSeparator){
        if(str == null) {
            return null;
        }
        Map<String,String> ret = Maps.newHashMap();

        String[] strArray = str.split(firstSeparator);
        for(int i = 0; i < strArray.length; i++){
            String[] values = strArray[i].split(secondSeparator);
            ret.put(values[0],values[1]);
        }
        return ret;
    }

    public static List<String> splitToList(String str,String separator){
        if(str == null) {
            return null;
        }
        String[] strArray = str.split(separator);
        List<String> ret = Lists.newArrayList();
        for(String value : strArray){
            ret.add(value);
        }
        return ret;
    }

    public static Set<String> splitToSet(String str, String separator){
        if(str == null) {
            return null;
        }
        String[] strArray = str.split(separator);
        Set<String> ret = Sets.newHashSet();
        for(String value : strArray){
            ret.add(value);
        }
        return ret;
    }

    public static Map<Integer,List<String>> splitToMapList(String str, String firstSeparator, String secondSeparator){
        if(str == null) {
            return null;
        }
        Map<Integer,List<String>> ret = Maps.newHashMap();
        String[] strArray = str.split(firstSeparator);
        for(int i = 0; i < strArray.length; i++){
            String[] values = strArray[i].split(secondSeparator);
            for(int j = 0; j < values.length; j++){
                List<String> temp = ret.get(j);
                if(temp == null){
                    temp = Lists.newArrayList();
                    ret.put(j,temp);
                }
                temp.add(values[j]);
            }
        }
        return ret;
    }

    public static List<Map<String,String>> splitToListMap(String str, String listSeparator, String mapSeparator){
        if(str == null) {
            return null;
        }
        List<Map<String,String>> ret = Lists.newArrayList();

        String[] strArray = str.split(listSeparator);
        for(String temp : strArray){
            Map<String,String> map = Maps.newHashMap();
            int index = str.indexOf(mapSeparator);
            String key = str.substring(0,index < temp.length() ? index : temp.length());
            String value = str.substring(index < temp.length() - 1 ? index + 1 : temp.length());
            map.put(key,value);
            ret.add(map);
        }
        return ret;
    }

    public static Map<String, Map<String,String>> splitToMapMap(String str, String firstMapSeparator, String secondMapSeparator){
        if(str == null) {
            return null;
        }
        Map<String,Map<String,String>> ret = Maps.newHashMap();

        String[] strArray = str.split(firstMapSeparator);
        for(int i = 0; i < strArray.length; i++){
            String[] values = strArray[i].split(secondMapSeparator);
            Map<String,String> map = Maps.newHashMap();
            for(int j = 0; j < values.length; j++){
                map.put("key"+j,values[j]);
            }
            ret.put("key"+i,map);
        }
        return ret;
    }

    public static String join(Map<String,String> elements,String mapSeparator,String listSeparator){
        if(elements == null) {
            return null;
        }
        StringBuilder strb = new StringBuilder();

        for(Map.Entry<String,String> entry : elements.entrySet()){
            strb.append(entry.getKey());
            strb.append(mapSeparator);
            strb.append(entry.getValue());
            strb.append(listSeparator);
        }
        strb.deleteCharAt(strb.length()-1);
        return strb.toString();
    }
}
