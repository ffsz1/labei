package com.xchat.oauth2.service.core.util;


import org.apache.commons.beanutils.PropertyUtils;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * @author liuguofu
 *         on 5/15/15.
 */
public class SortUtils {

    public static final String DESC = "desc";
    public static final String ASC = "asc";

    public static <T> void sort(List<T> list, final String sortBy,final String sort)  {
        if (list == null || sortBy == null ||sortBy.equals("")|| list.isEmpty())
            return;
        Collections.sort(list, new Comparator<T>() {
            @SuppressWarnings("unchecked")
            public int compare(T t1, T t2) {
                Object o1 = null;
                Object o2 = null;
                try {
                    o1 = PropertyUtils.getProperty(t1,sortBy);
                    o2 = PropertyUtils.getProperty(t2, sortBy);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int result = 0;
                if (o1 == null) {
                    result = -1;
                } else if (o2 == null) {
                    result = 1;
                }
                //字符串按照拼音排序
                else if (o1 instanceof String) {
                    result = Collator.getInstance(Locale.CHINA).compare(o1, o2);
                } else {
                    result = ((Comparable) o1).compareTo(o2);
                }

                if (DESC.equalsIgnoreCase(sort)) {
                    result = 0 - result;
                }
                return result;

            }
        });

    }
}
