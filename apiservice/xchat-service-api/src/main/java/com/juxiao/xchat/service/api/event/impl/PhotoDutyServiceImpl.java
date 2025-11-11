package com.juxiao.xchat.service.api.event.impl;

import com.juxiao.xchat.dao.user.PrivatePhotoDao;
import com.juxiao.xchat.service.api.event.DutyResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 上传图片新人任务
 *
 * @class: PhotoDutyServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/8/9
 */
@Service("PhotoDutyService")
public class PhotoDutyServiceImpl implements DutyResultService {

    @Autowired
    private PrivatePhotoDao privatePhotoDao;

    @Override
    public Byte checkUserDutyStatus(Long uid) {
        int photoCount = privatePhotoDao.countUserPhoto(uid);
        if (photoCount < 5) {
            return 1;
        }
        return 2;
    }
}
