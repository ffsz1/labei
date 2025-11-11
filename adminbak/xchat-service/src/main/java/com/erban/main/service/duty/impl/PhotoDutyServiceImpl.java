package com.erban.main.service.duty.impl;

import com.erban.main.model.PrivatePhoto;
import com.erban.main.service.duty.DutyResultService;
import com.erban.main.service.user.PrivatePhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private PrivatePhotoService photoService;

    @Override
    public Byte checkUserDutyStatus(Long uid) {
        List<PrivatePhoto> list = photoService.getPrivatePhoto(uid);
        if (list == null || list.size() < 5) {
            return 1;
        }
        return 2;
    }
}
