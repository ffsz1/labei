package com.juxiao.xchat.service.api.user.impl;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.UserSettingDao;
import com.juxiao.xchat.dao.user.domain.UserSettingDO;
import com.juxiao.xchat.dao.user.dto.UserSettingDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.user.UserSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserSettingServiceImpl implements UserSettingService {
    @Autowired
    private UserSettingDao settingDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private UsersManager usersManager;

    @Override
    public void save(Long uid, Byte likedSend, Integer chatPermission) throws WebServiceException {
        if (likedSend == null && chatPermission == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        UserSettingDO settingDo = new UserSettingDO();
        settingDo.setUid(uid);
        settingDo.setLikedSend(likedSend);
        settingDo.setChatPermission(chatPermission);
        settingDao.save(settingDo);
        redisManager.hdel(RedisKey.user_setting.getKey(), String.valueOf(uid));
    }

    @Override
    public UserSettingDTO getUserSetting(Long uid) throws WebServiceException {
        if (uid == null || uid == 0) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UserSettingDTO dto = usersManager.getUserSetting(uid);
        if (dto != null && dto.getChatPermission() == null) {
            dto.setChatPermission(0);
        }
        return dto;
    }
}
