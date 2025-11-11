package com.juxiao.xchat.service.api.user.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.PrivatePhotoDao;
import com.juxiao.xchat.dao.user.domain.PrivatePhotoDO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.manager.external.qiniu.QiniuManager;
import com.juxiao.xchat.manager.mq.ActiveMqManager;
import com.juxiao.xchat.manager.mq.bo.PhotoUploadMessageBO;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import com.juxiao.xchat.service.api.user.PrivatePhotoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PrivatePhotoServiceImpl implements PrivatePhotoService {
    private final Logger logger = LoggerFactory.getLogger(PrivatePhotoService.class);
    @Autowired
    private Gson gson;
    @Autowired
    private PrivatePhotoDao photoDao;
    @Autowired
    private RedisManager redisManager;

    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;

    @Autowired
    private QiniuManager qiniuManager;

    @Autowired
    private ActiveMqManager activeMqManager;

    @Override
    public void uploadV2(Long uid, String photoStr) throws WebServiceException {
        if (uid == null || uid == 0 || StringUtils.isBlank(photoStr)) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        String[] photos = photoStr.split(",");
        if (photos.length > 8 || photos.length == 0) {
            throw new WebServiceException(WebServiceCode.PRIVATE_PHOTOS_UP_MAX);
        }

        int i = 1;
        Date date = new Date();
        PrivatePhotoDO privatePhoto;
        for (String photo : photos) {
            try {
                boolean isCan = qiniuManager.imageCensor(photo);
                if (isCan) {
                    privatePhoto = new PrivatePhotoDO();
                    privatePhoto.setUid(uid);
                    privatePhoto.setPhotoUrl(photo);
                    privatePhoto.setSeqNo(i++);
                    privatePhoto.setCreateTime(date);
                    privatePhoto.setPhotoStatus(new Byte("1"));
                    photoDao.save(privatePhoto);
                    String json = gson.toJson(privatePhoto);
                    redisManager.hset(RedisKey.private_photo.getKey() + uid, privatePhoto.getPid().toString(), json);
                } else {
                    privatePhoto = new PrivatePhotoDO();
                    privatePhoto.setUid(uid);
                    privatePhoto.setPhotoUrl(photo);
                    privatePhoto.setSeqNo(i++);
                    privatePhoto.setCreateTime(date);
                    privatePhoto.setPhotoStatus(new Byte("2"));// 审核不通过
                    photoDao.save(privatePhoto);
                    asyncNetEaseTrigger.sendMsg(uid.toString(),"您上传的相片并未通过审核，请遵守平台规则，共建绿色平台！");
                }
            } catch (Exception e) {
                logger.error("upload error", e);
            }
        }

        PhotoUploadMessageBO bo = new PhotoUploadMessageBO(uid, photoStr);
        activeMqManager.sendQueueMessage(MqDestinationKey.PHOTO_UPLOAD_QUEUE, gson.toJson(bo));
    }


    @Override
    public void delete(Long uid, Long pid) throws WebServiceException {
        if (uid == null || pid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        photoDao.delete(pid);
        redisManager.hdel(RedisKey.private_photo.getKey() + uid, String.valueOf(pid));
    }
}
