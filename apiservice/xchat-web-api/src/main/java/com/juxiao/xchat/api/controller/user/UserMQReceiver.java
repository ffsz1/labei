package com.juxiao.xchat.api.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.PrivatePhotoDao;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.common.event.DutyManager;
import com.juxiao.xchat.manager.common.mcoin.McoinMissionManager;
import com.juxiao.xchat.manager.mq.bo.*;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import com.juxiao.xchat.service.api.event.DutyType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class UserMQReceiver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DutyManager dutyManager;

    @Autowired
    private Gson gson;

    @Autowired
    private McoinMissionManager missionManager;

    @Autowired
    private PrivatePhotoDao photoDao;

    /**
     * 接收关注某位用户信息
     *
     * @param message
     */
    @JmsListener(destination = MqDestinationKey.LIKE_SOME_BODY_QUEUE)
    public void receiveLikeSomeBody(String message) {
        if (StringUtils.isBlank(message)) {
            logger.warn("[ 消费关注用户信息 ]接收内容:>空,不作处理");
            return;
        }
        LikeSomeBodyMessageBO messageBO = gson.fromJson(message, LikeSomeBodyMessageBO.class);
        if (messageBO == null) {
            logger.warn("[ 消费关注用户信息 ]接收内容:>json 反序列化为null,不作处理");
            return;
        }
        try {
            missionManager.finish(messageBO.getLikeUid(), 2);
        } catch (Exception e) {
            logger.error("[ 消费关注用户信息 ]更新新手任务异常：", e);
        }
    }

    /**
     * 接收用户上传图片信息
     *
     * @param message
     */
    @JmsListener(destination = MqDestinationKey.PHOTO_UPLOAD_QUEUE)
    public void receivePhotoUpload(String message) {
        if (StringUtils.isBlank(message)) {
            logger.warn("[ 消费用户上传照片信息 ]接收内容:>空,不作处理");
            return;
        }
        PhotoUploadMessageBO bo = gson.fromJson(message, PhotoUploadMessageBO.class);
        if (bo == null) {
            logger.warn("[ 消费用户上传照片信息 ]接收内容:>json 反序列化为null,不作处理");
            return;
        }
        try {
            int photoCount = photoDao.countUserPhoto(bo.getUid());
            if (photoCount >= 4) {
                missionManager.finish(bo.getUid(), 1);
            }
        } catch (Exception e) {
            logger.error("[ 消费用户上传照片信息 ]更新新手任务异常：", e);
        }
    }

    /**
     * 接收用户绑定手机信息
     *
     * @param message
     */
    @JmsListener(destination = MqDestinationKey.BIND_PHONE_QUEUE)
    public void receiveBindPhone(String message) {
        if (StringUtils.isBlank(message)) {
            logger.warn("[ 消费用户绑定手机号 ]接收内容:>空,不作处理");
            return;
        }
        try {
            BindPhoneMessageBO messageBO = gson.fromJson(message, BindPhoneMessageBO.class);
            missionManager.finish(messageBO.getUid(), 4);
        } catch (Exception e) {
            logger.error("[ 消费用户绑定手机号 ]更新新手任务异常：", e);
        }
    }

    /**
     * 接收邀请注册奖励信息
     *
     * @param message
     */
    @JmsListener(destination = MqDestinationKey.SHARE_REGISTER_QUEUE)
    public void receiveShareRegister(String message) {
        if (StringUtils.isBlank(message)) {
            logger.warn("[ 消费邀请用户注册 ]接收内容:>空,不作处理");
            return;
        }
        try {
            ShareRegisterMessageBO messageBO = gson.fromJson(message, ShareRegisterMessageBO.class);
            missionManager.finish(messageBO.getUid(), messageBO.getMissionId());
        } catch (Exception e) {
            logger.error("[ 消费邀请用户注册 ]更新新手任务异常：", e);
        }
    }

    /**
     * 接收用户更新信息
     *
     * @param message
     */
    @JmsListener(destination = MqDestinationKey.UPDATE_USER_INFO_QUEUE)
    public void receiveUpdateInfo(String message) {
        if (StringUtils.isBlank(message)) {
            logger.warn("[ 消费更新用户信息 ]接收内容:>空,不作处理");
            return;
        }
        try {
            UpdateUserInfoMessageBO messageBo = gson.fromJson(message, UpdateUserInfoMessageBO.class);
            UsersDTO usersDto = messageBo.getUsersDTO();
            if (StringUtils.isNotBlank(usersDto.getUserDesc())) {
                missionManager.finish(usersDto.getUid(), 5);
            }
        } catch (Exception e) {
            logger.error("[ 消费更新用户信息 ]更新每日任务异常：", e);
        }
    }

    /**
     * 接收大厅发言信息
     *
     * @param message
     */
    @JmsListener(destination = MqDestinationKey.SPEAK_IN_PUBLIC_QUEUE)
    public void receiveSpeak(String message) {
        if (StringUtils.isBlank(message)) {
            logger.warn("[ 消费大厅发言信息 ]接收内容:>空,不作处理");
            return;
        }
        try {
            SpeakInPublicMessageBO messageBO = gson.fromJson(message, SpeakInPublicMessageBO.class);
            if (messageBO == null) {
                logger.warn("[ 消费大厅发言信息 ]接收内容:>json 反序列化为null,不作处理");
                return;
            }
            missionManager.finish(messageBO.getUid(), 3);
        } catch (Exception e) {
            logger.error("[ 消费大厅发言信息 ]更新每日任务异常：", e);
        }
    }

    /**
     * 接收用户分享信息
     *
     * @param message
     */
    @JmsListener(destination = MqDestinationKey.USER_SHARE_QUEUE)
    public void receiveShare(String message) {
        if (StringUtils.isBlank(message)) {
            logger.warn("[ 消费用户分享信息 ]接收内容:>空,不作处理");
            return;
        }
        JSONObject object = JSONObject.parseObject(message);
        try {
            int shareType = object.getIntValue("shareType");
            // 1.微信; 2.朋友圈; 3.qq; 4.qq空间
            if (2 == shareType) {
                missionManager.finish(object.getLong("uid"), 6);
            } else if (4 == shareType) {
                missionManager.finish(object.getLong("uid"), 7);
            }
        } catch (Exception e) {
            logger.error("[ 消费用户分享信息 ]更新每日任务异常：", e);
        }
    }

    /**
     * 接收用户捡海螺信息
     *
     * @param message
     */
    @JmsListener(destination = MqDestinationKey.USER_DRAW_GIFT, concurrency="2-10")
    public void receiveUserDrawGift(String message) {
        if (StringUtils.isBlank(message)) {
            logger.warn("[ 消费用户捡海螺信息 ]接收内容:>空,不作处理");
            return;
        }
        JSONObject object = JSONObject.parseObject(message);
        try {
            missionManager.finish(object.getLongValue("uid"), 10);
        } catch (WebServiceException e) {
            logger.error("[ 消费用户捡海螺信息 ]更新每日任务异常：", e);
        }
    }

    /**
     * 接收用戶实名认证信息
     *
     * @param message
     */
    @JmsListener(destination = MqDestinationKey.USER_REAL_QUEUE)
    public void receiveUserReal(String message) {
        if (StringUtils.isBlank(message)) {
            logger.warn("[ 消费用户实名认证信息 ]接收内容:>空,不作处理");
            return;
        }
        try {
            UserRealMessageBO messageBo = gson.fromJson(message, UserRealMessageBO.class);
            UsersDTO usersDto = messageBo.getUsersDTO();
            missionManager.finish(usersDto.getUid(), 24);
        } catch (Exception e) {
            logger.error("[ 消费用户实名认证信息 ]更新每日任务异常：", e);
        }
    }
}
