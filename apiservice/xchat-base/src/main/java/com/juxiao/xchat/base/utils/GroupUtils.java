package com.juxiao.xchat.base.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 按照时间戳进行分组
 *
 * @class: GroupUtils.java
 * @author: chenjunsheng
 * @date 2018/6/4
 */
public class GroupUtils {

    /**
     * 根据date字段对列表进行分组
     *
     * @param list
     * @return
     * @author: chenjunsheng
     * @date 2018/6/4
     */
    public static <T> List<Map<Long, List<T>>> groupByDate(List<T> list, String fieldName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Long key;
        Map<Long, List<T>> map = new TreeMap<>((o1, o2) -> -o1.compareTo(o2));
        String methodName = new StringBuffer("get").append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1)).toString();
        for (T object : list) {
            Method method = object.getClass().getMethod(methodName);
            key = (Long) method.invoke(object);
            List<T> grouplist = map.get(key);
            if (grouplist == null) {
                grouplist = new ArrayList<>();
            }
            grouplist.add(object);
            map.put(key, grouplist);
        }

        Set<Map.Entry<Long, List<T>>> set = map.entrySet();
        Iterator<Map.Entry<Long, List<T>>> iterator = set.iterator();
        Map.Entry<Long, List<T>> entry;
        Map<Long, List<T>> temp;
        List<Map<Long, List<T>>> result = new ArrayList<>();

        while (iterator.hasNext()) {
            entry = iterator.next();
            temp = new HashMap<>();
            temp.put(entry.getKey(), entry.getValue());
            result.add(temp);
        }
        return result;
    }
}


