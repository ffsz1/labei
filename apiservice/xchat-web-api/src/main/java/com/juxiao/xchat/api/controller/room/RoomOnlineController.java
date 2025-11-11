package com.juxiao.xchat.api.controller.room;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/room/online")
@Api(description = "房间接口", tags = "房间接口")
public class RoomOnlineController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisManager redisManager;

    @ApiOperation(value = "im房间人数变更同步", notes = "im房间人数变更同步", tags = {"房间接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "房间房主uid", dataType = "long", required = true),
            @ApiImplicitParam(name = "onlineNum", value = "房间人数", dataType = "long", required = true),
            @ApiImplicitParam(name = "time", value = "同步时间", dataType = "long", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success")
    })
    @RequestMapping(value = "sync", method = RequestMethod.POST)
    public WebServiceMessage getRoomByUid(Long uid, Long onlineNum, Long time) {
        logger.info("{},{},{}", uid, onlineNum, time);
        boolean isCan = false;
        String str = redisManager.hget(RedisKey.im_online_num_time.getKey(), uid.toString());
        if (StringUtils.isNotBlank(str)) {
            if (time > Long.valueOf(str)) {
                isCan = true;
            }
        } else {
            isCan = true;
        }
        if (isCan) {
            redisManager.hset(RedisKey.im_online_num_time.getKey(), uid.toString(), time.toString());
            redisManager.hset(RedisKey.im_online_num.getKey(), uid.toString(), onlineNum==null?"0":onlineNum.toString());
        }
        return WebServiceMessage.success(null);
    }

}
