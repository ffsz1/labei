package com.juxiao.xchat.api.controller.room;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.room.RoomMicService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 房间麦序的控制器：
 * 1：控制房间的开锁麦、上下麦操作
 * 2：进房间时获取房间的麦的状态
 */
@RestController
@RequestMapping("/room/mic")
@Api(description = "房间接口", tags = "房间接口")
public class RoomMicController {
    @Autowired
    private RoomMicService roomMicService;

    /**
     * 获取房间的麦序列表
     *
     * @param uid
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/list")
    public WebServiceMessage getMicList(@RequestParam("uid") Long uid) {
        return WebServiceMessage.success(roomMicService.getRoomMicByUid(uid));
    }

    /**
     * 上麦操作
     *
     * @param micUid   上麦用户
     * @param roomId   上麦房间
     * @param position 麦序位置
     * @param operator 操作人
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/upmic")
    public WebServiceMessage upMic(@RequestParam("micUid") long micUid,
                                   @RequestParam("roomId") long roomId,
                                   @RequestParam("position") int position,
                                   @RequestParam("operator") Long operator) throws WebServiceException {
        return WebServiceMessage.success(roomMicService.upMic(micUid, roomId, position, operator));
    }

    /**
     * 下麦操作
     *
     * @param micUid   下麦用户
     * @param roomId   下麦房间
     * @param position 麦序位置
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/downmic")
    public WebServiceMessage downMic(@RequestParam("micUid") long micUid,
                                     @RequestParam("roomId") long roomId,
                                     @RequestParam("position") int position) throws WebServiceException {
        return WebServiceMessage.success(roomMicService.downMic(micUid, roomId, position));
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
    @SignVerification
    @RequestMapping(value = "/invitemic")
    public WebServiceMessage inviteMic(@RequestParam("micUid") long micUid,
                                       @RequestParam("roomUid") long roomUid,
                                       @RequestParam("roomId") long roomId,
                                       @RequestParam("position") int position) throws WebServiceException {
        return WebServiceMessage.success(roomMicService.sendInviteMicMessage(micUid, roomUid, roomId, position));
    }

    /**
     * 踢用户下麦操作
     *
     * @param roomUid  房主或者管理员
     * @param micUid   下麦用户
     * @param roomId   下麦房间
     * @param position 麦序位置
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/kickmic")
    public WebServiceMessage kickMic(@RequestParam("roomUid") long roomUid,
                                     @RequestParam("micUid") long micUid,
                                     @RequestParam("roomId") long roomId,
                                     @RequestParam("position") int position) throws WebServiceException {
        return WebServiceMessage.success(roomMicService.kickMic(micUid, roomUid, roomId, position));
    }


    /**
     * 锁麦/开麦操作
     *
     * @param roomUid  房主UID
     * @param uid      当前操作用户ID
     * @param position 麦序位置
     * @param state    状态，1：锁麦，0开麦（即取消锁麦）
     * @return
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "/lockmic")
    public WebServiceMessage lockMic(@RequestParam("roomUid") Long roomUid,
                                     @RequestParam("uid") Long uid,
                                     @RequestParam("position") Integer position,
                                     @RequestParam("state") Integer state) throws Exception {
        return WebServiceMessage.success(roomMicService.lockMic(roomUid, uid, position, state));
    }

    /**
     * 锁坑位/取消操作
     *
     * @param roomUid  房主UID
     * @param position 麦序位置
     * @param state    状态，1：锁坑位，0取消锁（即取消锁坑位）
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/lockpos")
    public WebServiceMessage lockPos(@RequestParam("roomUid") Long roomUid,
                                     @RequestParam("uid") Long uid,
                                     @RequestParam("position") Integer position,
                                     @RequestParam("state") Integer state) throws Exception {
        return WebServiceMessage.success(roomMicService.lockPos(roomUid, uid, position, state));
    }

}
