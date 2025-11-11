package com.erban.web.controller.room;

import com.erban.main.service.room.RoomMicService;
import com.erban.web.common.BaseController;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 房间麦序的控制器：
 * 1：控制房间的开锁麦、上下麦操作
 * 2：进房间时获取房间的麦的状态
 */
@Controller
@RequestMapping("/room/mic")
public class RoomMicController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(RoomMicController.class);

    @Autowired
    private RoomMicService roomMicService;


    /**
     * 获取房间的麦序列表
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult getMicList(long uid) {
        try {
            Map<String, String> map = roomMicService.getRoomMicByUid(uid);
            return new BusiResult(BusiStatus.SUCCESS, map);
        } catch (Exception e) {
            logger.error("getMicList error, uid:" + uid, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }


    /**
     * 上麦操作
     *
     * @param micUid   上麦用户
     * @param roomId   上麦房间
     * @param position 麦序位置
     * @return
     */
    @RequestMapping(value = "/upmic")
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult upMic(long micUid, long roomId, int position) {
        logger.info("upMic param===>>>micUid:{}, roomId:{}, position:{}", micUid, roomId, position);
        try {
            // 对同一房间同一麦序加分布式锁，3秒后自动释放，防止多个用户同时抢麦
            String tmpLock = jedisLockService.lock(RedisKey.room_pos_lock.getKey(roomId + "_" + position)
                    , 2000, 3000);
            if (BlankUtil.isBlank(tmpLock)) {
                return new BusiResult(BusiStatus.NOAUTHORITY);
            }
            int result = roomMicService.upMic(micUid, roomId, position);
            return new BusiResult(BusiStatus.SUCCESS, result);
        } catch (Exception e) {
            logger.error("upMic error, micUid:" + micUid + ", roomId:" + roomId + ", position:" + position, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 邀请上麦操作
     *
     * @param micUid   上麦用户
     * @param roomUid  房主或管理员UID
     * @param roomId   上麦房间
     * @param position 麦序位置
     * @return
     */
    @RequestMapping(value = "/invitemic")
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult inviteMic(long micUid, long roomUid, long roomId, int position) {
        logger.info("inviteMic param===>>>micUid:{}, roomId:{}, position:{}", micUid, roomId, position);
        try {
            int result = roomMicService.sendInviteMicMessage(micUid, roomUid, roomId, position);
            return new BusiResult(BusiStatus.SUCCESS, result);
        } catch (Exception e) {
            logger.error("inviteMic error, micUid:" + micUid + ", roomId:" + roomId + ", position:" + position, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 下麦操作
     *
     * @param micUid   下麦用户
     * @param roomId   下麦房间
     * @param position 麦序位置
     * @return
     */
    @RequestMapping(value = "/downmic")
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult downMic(long micUid, long roomId, int position) {
        logger.info("downMic param===>>>micUid:{}, roomId:{}, position:{}", micUid, roomId, position);
        try {
            int result = roomMicService.downMic(micUid, roomId, position);
            return new BusiResult(BusiStatus.SUCCESS, result);
        } catch (Exception e) {
            logger.error("downMic error, micUid:" + micUid + ", roomId:" + roomId + ", position:" + position, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 踢下麦操作
     *
     * @param micUid   下麦用户
     * @param roomUid  房主或者管理员
     * @param roomId   下麦房间
     * @param position 麦序位置
     * @return
     */
    @RequestMapping(value = "/kickmic")
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult kickMic(long micUid, long roomUid, long roomId, int position) {
        logger.info("kickMic param===>>>micUid:{}, roomId:{}, position:{}", micUid, roomId, position);
        try {
            int result = roomMicService.downMic(micUid, roomId, position);
            if (result == 200) {
                roomMicService.sendKickTipMessage(roomId, roomUid);
            }
            return new BusiResult(BusiStatus.SUCCESS, result);
        } catch (Exception e) {
            logger.error("kickMic error, micUid:" + micUid + ", roomId:" + roomId + ", position:" + position, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 锁麦/开麦操作
     *
     * @param roomUid  房主UID
     * @param position 麦序位置
     * @param state    状态，1：锁麦，0开麦（即取消锁麦）
     * @return
     */
    @RequestMapping(value = "/lockmic")
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult lockMic(long roomUid, int position, int state) {
        logger.info("lockMic param===>>>roomUid:{}, position:{}, state:{}", roomUid, position, state);
        try {
            int result = roomMicService.lockMic(roomUid, position, state);
            return new BusiResult(BusiStatus.SUCCESS, result);
        } catch (Exception e) {
            logger.error("lockMic error, roomUid:" + roomUid + ", position:" + position + ", state:" + state, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 锁坑位/取消操作
     *
     * @param roomUid  房主UID
     * @param position 麦序位置
     * @param state    状态，1：锁坑位，0取消锁（即取消锁坑位）
     * @return
     */
    @RequestMapping(value = "/lockpos")
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult lockPos(long roomUid, int position, int state) {
        logger.info("lockPos param===>>>roomUid:{}, position:{}, state:{}", roomUid, position, state);
        try {
            int result = roomMicService.lockPos(roomUid, position, state);
            return new BusiResult(BusiStatus.SUCCESS, result);
        } catch (Exception e) {
            logger.error("lockPos error, roomUid:" + roomUid + ", position:" + position + ", state:" + state, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

}
