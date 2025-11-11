package com.juxiao.xchat.base.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/***
 *  Created by eyre on 2018/2/1.
 */
@Slf4j
public class ListUtil {
    /***
     *  获取实体类class的单个方法
     * @param entityClass：实体类class
     * @param method：方法名
     */
    public static Method getMethod(Class<?> entityClass, String method) {
        try {
            return entityClass.getMethod(method);
        } catch (NoSuchMethodException e) {
            log.error("ListUtil.getMethod获取失败,异常信息:{}", e);
            throw new RuntimeException("ListUtil.getMethod获取失败！！！");
        }
    }

    /***
     *  拼接实体列表的某个字段，用逗号做间隔符
     * @param objectList：实体列表
     * @param method：方法
     * @param <T>：实体类
     */
    public static <T> String listEntityToString(List<T> objectList, Method method) {
        if (objectList == null || objectList.size() == 0) {
            return null;
        }
        StringBuffer str = new StringBuffer();
        try {
            for (int i = 0; i < objectList.size(); i++) {
                if (i == 0) {
                    str.append(method.invoke(objectList.get(i)));
                } else {
                    str.append("," + method.invoke(objectList.get(i)));
                }
            }
        } catch (InvocationTargetException e) {
            log.error("ListUtil.listToString转换失败,异常信息:{}", e);
            throw new RuntimeException("ListUtil.listToString转换失败！！！");
        } catch (IllegalAccessException e) {
            log.error("ListUtil.listToString转换失败,异常信息:{}", e);
            throw new RuntimeException("ListUtil.listToString转换失败！！！");
        }
        return str.toString();
    }

    /***
     *  拼接实体列表的某个字段，用逗号做间隔符
     * @param objectList：实体列表
     * @param method：方法名
     * @param <T>：实体类
     */
    public static <T> String listEntityToString(List<T> objectList, String method) {
        if (objectList == null || objectList.size() == 0) {
            return null;
        }
        return listEntityToString(objectList, getMethod(objectList.get(0).getClass(), method));
    }

    /**
     * 集合分页
     *
     * @param source   需要分页的集合
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @param <T>
     * @return
     */
    public static <T> List<T> page(List<T> source, Integer pageNum, Integer pageSize) {
        if (source == null) {
            return Lists.newArrayList();
        }
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null || pageSize > 50 ? 10 : pageSize;
        int begin = (pageNum - 1) * pageSize;
        if (begin > source.size()) {
            return Lists.newArrayList();
        }
        int end = pageNum * pageSize;
        if (end > source.size()) {
            end = source.size();
        }
        return source.subList(begin, end);
    }
}
