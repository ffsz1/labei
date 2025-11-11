package com.juxiao.xchat.api.controller.room;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.Client;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.room.dto.RoomUserinDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.room.RoomService;
import com.juxiao.xchat.service.api.room.UserRoomService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/room")
@Api(description = "房间接口", tags = "房间接口")
public class UserRoomController {
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UserRoomService userRoomService;

    /**
     * @param uid
     * @param roomUid
     * @return
     */
    @ApiOperation(value = "小程序用户进入房间", notes = "", tags = {"小程序接口", "房间接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "roomUid", value = "进入房主UID", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "当前客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "t", value = "当前时间戳", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = RoomUserinDTO.class)
    })
    @Authorization
    @SignVerification(client = Client.WXAPP)
    @RequestMapping(value = "v2/in", method = RequestMethod.POST)
    public WebServiceMessage userIntoRoom(@RequestParam(value = "uid", required = false) Long uid,
                                          @RequestParam(value = "roomUid", required = false) Long roomUid) throws Exception {
        RoomUserinDTO userinDto = userRoomService.userIntoRoom(uid, roomUid);
        if (uid != null && uid.equals(roomUid)) {
            UsersDTO usersDto = usersManager.getUser(uid);
            if (usersDto != null) {
                roomService.sendOpenRoomNoticeToFollowers(usersDto);
            }
        }
        return WebServiceMessage.success(userinDto);
    }

    @ApiOperation(value = "小程序用户退出房间", notes = "", tags = {"小程序接口", "房间接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "当前客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "t", value = "当前时间戳", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success")
    })
    @Authorization
    @SignVerification(client = Client.WXAPP)
    @RequestMapping(value = "v2/out", method = RequestMethod.POST)
    public WebServiceMessage userOutRoom(@RequestParam(value = "uid", required = false) Long uid) throws WebServiceException {
        userRoomService.userOutRoom(uid);
        return WebServiceMessage.success(null);
    }

    @ApiOperation(value = "小程序用户当前所在房间接口", notes = "获取指定用户当前所在房间", tags = {"小程序接口", "房间接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "当前客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "t", value = "当前时间戳", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = RoomUserinDTO.class)
    })
    @SignVerification(client = Client.WXAPP)
    @RequestMapping(value = "v2/get", method = RequestMethod.GET)
    public WebServiceMessage getUserInRoomInfo(Long uid) throws WebServiceException {
        return WebServiceMessage.success(userRoomService.getRoomByUid(uid));
    }

}
