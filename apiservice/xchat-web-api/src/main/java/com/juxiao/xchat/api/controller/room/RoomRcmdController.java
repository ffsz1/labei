package com.juxiao.xchat.api.controller.room;

import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.common.room.vo.RoomVo;
import com.juxiao.xchat.service.api.room.RoomRcmdService;
import com.juxiao.xchat.service.api.room.vo.RoomRcmdVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @class: RoomRecommendController.java
 * @author: chenjunsheng
 * @date 2018/8/13
 */
@RestController
@RequestMapping("/room/rcmd")
@Api(description = "房间接口", tags = "房间接口")
public class RoomRcmdController {

    @Autowired
    private RoomRcmdService rcmdService;

    @ApiOperation("获取推荐给新用户的房间接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前请求用户UID", dataType = "long", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = RoomRcmdVO.class),
            @ApiResponse(code = 1701, message = "当前时间暂时没有推荐房间"),
            @ApiResponse(code = 1702, message = "当前时间暂时没有推荐房间"),
            @ApiResponse(code = 1703, message = "推荐房间功能已经关闭"),
            @ApiResponse(code = 1707, message = "该用户不是新的用户"),
    })
    @SignVerification
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public WebServiceMessage getRcmdRoom(@RequestParam("uid") Long uid) throws WebServiceException {
        RoomVo result = rcmdService.getUserRcmdRoom(uid);
        return WebServiceMessage.success(result);
    }
}
