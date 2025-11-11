package com.juxiao.xchat.manager.common.user.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.user.PrivatePhotoDao;
import com.juxiao.xchat.dao.user.domain.PrivatePhotoDO;
import com.juxiao.xchat.dao.user.dto.PrivatePhotoDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.user.PrivatePhotoManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author: chenjunsheng
 * @date 2018/6/15
 */
@Service
public class PrivatePhotoManagerImpl implements PrivatePhotoManager {
    @Autowired
    private PrivatePhotoDao photoDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Gson gson;

    @Override
    public List<PrivatePhotoDTO> listUserPrivatePhoto(Long uid) {
        Map<String, String> map = redisManager.hgetAll(RedisKey.private_photo.getKey() + uid);
        List<PrivatePhotoDTO> photos;
        if (map != null && map.size() != 0) {
            photos = new ArrayList<>();
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            Map.Entry<String, String> entry;
            while (iterator.hasNext()) {
                entry = iterator.next();
                String value = entry.getValue();
                if (StringUtils.isBlank(value)) {
                    continue;
                }
                photos.add(gson.fromJson(value, PrivatePhotoDTO.class));
            }
            return photos;
        }

        photos = photoDao.listUserPrivatePhoto(uid,1);
        if (photos != null && photos.size() > 0) {
            for (PrivatePhotoDTO photo : photos) {
                redisManager.hset(RedisKey.private_photo.getKey() + uid, photo.getPid().toString(), gson.toJson(photo));
            }
        }
        return photos;
    }
}
