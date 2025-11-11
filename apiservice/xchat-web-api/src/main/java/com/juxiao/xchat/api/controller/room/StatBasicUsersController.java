package com.juxiao.xchat.api.controller.room;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.room.domain.StatBasicUsersDO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.service.api.room.StatBasicUsersService;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/basicusers")
@Api(description = "房间接口", tags = "房间接口")
public class StatBasicUsersController {
    @Autowired
    private StatBasicUsersService statBasicUsersService;

    @Autowired
    private RedisManager redisManager;

    @RequestMapping(value = "/record", method = RequestMethod.POST)
    public WebServiceMessage setBasicUser(@RequestParam(value = "uid", required = false) Long uid,
                                          @RequestParam(value = "roomUid", required = false) Long roomUid) throws WebServiceException {
        StatBasicUsersDO basicUsersDTO = statBasicUsersService.addBasicUser(uid, roomUid);
        return WebServiceMessage.success(basicUsersDTO);
    }

    @ApiOperation(value = "房间在线时长接口", notes = "房间在线时长接口", tags = {"房间接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户id", dataType = "long", required = true),
            @ApiImplicitParam(name = "time", value = "同步时间", dataType = "long", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/v2/record", method = RequestMethod.POST)
    public WebServiceMessage countRoomOnline(@ApiParam("房主ID") @RequestParam(value = "roomUid") Long roomUid,
                                             @ApiParam("客户端当前时间戳") @RequestParam(value = "time") Long time,
                                             @ApiParam("用户") @RequestParam(value = "uid") Long uid,
                                             @ApiParam("用户") @RequestParam(value = "ticket") String ticket) {
        if (roomUid == null || time == null || uid == null) {
            return WebServiceMessage.success(null);
        }

        //判断现在时间戳和客户端传来时间戳是否间隔小于15s
//        long timestamp = System.currentTimeMillis();
//        if (timestamp - time > 15000) {
//            log.warn("[ 房间在线时长 ] 时间戳相差过大，roomUid:>{}, uid:>{}, 当前:>{}, 客户端:>{}, 相差:>{}", roomUid, uid, timestamp, time, timestamp - time);
//            return WebServiceMessage.success(null);
//        }

        String redisKey = RedisKey.daily_online_lock.getKey(roomUid + "_" + uid);
        String lockVal = redisManager.lock(redisKey, 5000);
        if (StringUtils.isBlank(lockVal)) {
            return WebServiceMessage.success(null);
        }
        try {
            statBasicUsersService.countRoomOnline(roomUid, time, uid);
        } finally {
            redisManager.unlock(redisKey, lockVal);
        }

        return WebServiceMessage.success(null);
    }

}