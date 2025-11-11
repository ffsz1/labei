package com.juxiao.xchat.api.controller.room;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.room.dto.RoomUserinDTO;
import com.juxiao.xchat.service.api.room.RoomGameService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chris
 * @Title: 房间玩法
 */
@RestController
@RequestMapping("/room/game")
@Api(tags = "房间玩法接口")
public class RoomGameController {

    @Autowired
    private RoomGameService roomGameService;

    @ResponseBody
    @RequestMapping(value = "getState", method = RequestMethod.GET)
//    @Authorization
    public WebServiceMessage getState(Long uid, Long roomId) throws WebServiceException {
        return WebServiceMessage.success(roomGameService.getState(uid, roomId));
    }

    @RequestMapping(value = "choose", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public WebServiceMessage choose(Long uid, Long roomId, Integer type, String result) throws WebServiceException {
        return WebServiceMessage.success(roomGameService.choose(uid, roomId, type, result));
    }

    @RequestMapping(value = "confirm", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public WebServiceMessage confirm(Long uid, Long roomId, Integer type, String result) throws WebServiceException {
        return WebServiceMessage.success(roomGameService.confirm(uid, roomId, type, result));
    }

    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public WebServiceMessage cancel(Long uid, Long roomId, Integer type, String result) throws WebServiceException {
        return WebServiceMessage.success(roomGameService.cancel(uid, roomId, type, result));
    }

    /**
     * 根据uid获取个人房间游戏信息
     */
    @ApiOperation(value = "获取房间信息接口", notes = "根据uid获取房间信息", tags = {"房间接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "roomId", value = "用户所在房间UID", dataType = "long", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = RoomUserinDTO.class)
    })
    @SignVerification
    @RequestMapping(value = "getPerson", method = RequestMethod.GET)
    public WebServiceMessage getPersonGame(Long uid, Long roomId) throws WebServiceException {
        return WebServiceMessage.success(roomGameService.getPersonGame(uid, roomId));
    }
}
