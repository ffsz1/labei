package com.juxiao.xchat.service.api.user;


import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.dto.UserSettingDTO;

public interface UserSettingService {
    /**
     * 保存用户个人配置
     *
     * @param uid
     * @param likedSend
     */
    void save(Long uid, Byte likedSend, Integer chatPermission) throws WebServiceException;

    /**
     * 获取用户个人设置
     *
     * @param uid
     * @return
     */
    UserSettingDTO getUserSetting(Long uid) throws WebServiceException;
}
