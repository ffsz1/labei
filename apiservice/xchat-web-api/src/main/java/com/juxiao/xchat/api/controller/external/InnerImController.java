package com.juxiao.xchat.api.controller.external;


import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.room.RoomCharmManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "inner/im")
@Api(description = "第三方接口", tags = "第三方接口")
public class InnerImController {

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private RoomCharmManager roomCharmManager;


    @ApiOperation(value = "im房间人数变更同步", notes = "im房间人数变更同步", tags = {"房间接口"})
    @ApiResponses({
            @ApiResponse(code = 200, message = "success")
    })
    @RequestMapping(value = "/v1/receiveRoomOnlineMsg", method = RequestMethod.POST)
    public WebServiceMessage receiveRoomOnlineMsg(Long roomUid, Long uid, Long onlineNum, Byte type, Long time) {
        if (roomUid == null || onlineNum == null || time == null) {
            return WebServiceMessage.success(null);
        }

        String str = redisManager.hget(RedisKey.im_online_num_time.getKey(), roomUid.toString());
        Long timestamp;
        try {
            timestamp = Long.valueOf(str);
        } catch (NumberFormatException e) {
            timestamp = 0L;
        }

        if (StringUtils.isBlank(str) || time > timestamp) {
            redisManager.hset(RedisKey.im_online_num_time.getKey(), roomUid.toString(), time.toString());
            redisManager.hset(RedisKey.im_online_num.getKey(), roomUid.toString(), onlineNum.toString());
        }

        if (uid == null || type == null) {
            return WebServiceMessage.success(null);
        }

        // type == 1则是用户进房，type==2则是用户退出房间
        if (type == 1) {
            roomCharmManager.deleteRoomCharm(roomUid, uid);
            roomCharmManager.sendRoomAllCharm(roomUid, uid);
        } else if (type == 2) {
            roomCharmManager.deleteRoomCharm(roomUid, uid);
            roomCharmManager.sendRoomAllCharm(roomUid, null);
            redisManager.hdel(RedisKey.daily_room_time.getKey(), uid + "_" + roomUid);
        }

        return WebServiceMessage.success(null);
    }

    /**
     * 麦序更新通知接口
     *
     * @param roomUid 房主ID
     * @param uid     麦序
     * @param type    用户操作：1，上麦；2，下麦
     * @return
     */
    @RequestMapping(value = "/v1/receiveRoomMicMsg", method = RequestMethod.GET)
    public WebServiceMessage receiveRoomMicMsg(@RequestParam(value = "roomUid", required = false) Long roomUid,
                                               @RequestParam(value = "uid", required = false) Long uid,
                                               @RequestParam(value = "type", required = false) Byte type) {
        if (roomUid == null || uid == null || type == null) {
            return WebServiceMessage.success(null);
        }

        try {
            log.info("[receiveRoomMicMsg]  麦序更新通知接口请求参数: roomUid:{},uid:{},type",roomUid,uid,type);
            roomCharmManager.updateMicHandler(roomUid, uid, type);
        } catch (Exception e) {
            log.error("[ 麦序更新 ]更新房间魅力值异常，roomUid:>{},uid:>{},type:>{}", roomUid, uid, type, e);
        }
        return WebServiceMessage.success(null);
    }
}
