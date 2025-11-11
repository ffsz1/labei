package com.erban.main.service.user;

import com.erban.main.model.PrivatePhoto;
import com.erban.main.mybatismapper.PrivatePhotoMapper;
import com.erban.main.service.base.BaseService;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PrivatePhotoV2Service extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(PrivatePhotoV2Service.class);
    @Autowired
    private PrivatePhotoMapper privatePhotoMapper;

    public BusiResult upload(Long uid, String photoStr) {
        String[] photos = photoStr.split(",");
        int i = 1;
        Date date = new Date();
        for (String photo : photos) {
            try {
                PrivatePhoto privatePhoto = new PrivatePhoto();
                privatePhoto.setUid(uid);
                privatePhoto.setPhotoUrl(photo);
                privatePhoto.setSeqNo(i++);
                privatePhoto.setCreateTime(date);
                privatePhotoMapper.insertSelective(privatePhoto);
                String json = gson.toJson(privatePhoto);
                jedisService.hwrite(RedisKey.private_photo.getKey() + uid, privatePhoto.getPid().toString(), json);
            } catch (Exception e) {
                logger.error("upload error", e);
            }
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }

}
