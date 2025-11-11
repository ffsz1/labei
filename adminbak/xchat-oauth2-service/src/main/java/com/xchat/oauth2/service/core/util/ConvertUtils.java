package com.xchat.oauth2.service.core.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author liuguofu
 *         on 12/9/14.
 */
public class ConvertUtils {

    public static <K,T> List<T> toList(Map<K,T> data, List<K> sort){
        if(data == null || sort == null) {
            return null;
        }
        List<T> ret = Lists.newArrayList();
        for(K key : sort){
            if(data.get(key)!=null){
                ret.add(data.get(key));
            }
        }
        return ret;
    }

    public static Set<String> toStringSet(Collection<Long> ids){
        if(ids == null){
            return null;
        }
        Set<String> ret = Sets.newHashSet();
        for(Long id : ids){
            if(id != null){
                ret.add(id.toString());
            }
        }
        return ret;
    }

}
