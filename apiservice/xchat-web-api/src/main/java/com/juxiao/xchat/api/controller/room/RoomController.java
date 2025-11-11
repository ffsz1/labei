package com.juxiao.xchat.api.controller.room;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.Client;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.room.dto.RoomUserinDTO;
import com.juxiao.xchat.manager.common.room.RoomCharmManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.service.api.room.RoomService;
import com.juxiao.xchat.service.api.room.bo.RoomAdminParamBO;
import com.juxiao.xchat.service.api.room.bo.RoomParamBO;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/room")
@Api(description = "房间接口", tags = "房间接口")
public class RoomController {
    @Autowired
    private RoomManager roomManager;
    @Autowired
    private RoomService roomService;
    @Autowired
    private AppVersionManager versionService;

    @Autowired
    private RoomCharmManager roomCharmManager;

    @ApiOperation("用户下麦回调通知")
    @SignVerification
    @RequestMapping(value = "roomMicDown", method = {RequestMethod.GET, RequestMethod.POST})
    public WebServiceMessage roomMicDown(Long roomId, Long uid) throws Exception {
        roomService.roomMicDown(roomId, uid);
        return WebServiceMessage.success(null);
    }

    @ApiOperation("新用户开房间")
    @SignVerification
    @RequestMapping(value = "open", method = {RequestMethod.GET, RequestMethod.POST})
    public WebServiceMessage openRoom(RoomParamBO room) throws Exception {
        return roomService.openRoom(room);
    }

    /**
     * 用户修改房间信息
     */
    @SignVerification
    @RequestMapping(value = "update", method = {RequestMethod.GET, RequestMethod.POST})
    public WebServiceMessage updateRoomWhileRunning(RoomParamBO roomParam) throws Exception {
        return WebServiceMessage.success(roomService.updateRoomByRunning(roomParam));
    }

    /**
     * 管理员修改房间信息
     */
    @SignVerification
    @RequestMapping(value = "updateByAdmin", method = {RequestMethod.GET, RequestMethod.POST})
    public WebServiceMessage updateRoomWhileRunningByAdmin(RoomAdminParamBO roomAdminParam) throws Exception {
        return WebServiceMessage.success(roomService.updateRoomByAdmin(roomAdminParam));
    }

    /**
     * 根据uid获取房间信息
     */
    @ApiOperation(value = "获取房间信息接口", notes = "根据uid获取房间信息", tags = {"房间接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "要查看的用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "visitorUid", value = "当前用户", dataType = "long", required = true),
            @ApiImplicitParam(name = "os", value = "用户所在房间UID", dataType = "long", required = true),
            @ApiImplicitParam(name = "app", value = "赠送的礼物ID", dataType = "int", required = true),
            @ApiImplicitParam(name = "appVersion", value = "赠送的礼物数量", dataType = "int", required = true),
            @ApiImplicitParam(name = "t", value = "当前时间戳", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = RoomUserinDTO.class)
    })
    @SignVerification
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public WebServiceMessage getRoomByUid(HttpServletRequest request,
                                          @RequestParam(value = "uid", required = false) Long uid,
                                          @RequestParam(value = "visitorUid", required = false) Long visitorUid,
                                          @RequestParam(value = "os", required = false) String os,
                                          @RequestParam(value = "appid", required = false) String appid,
                                          @RequestParam(value = "appVersion", required = false) String appVersion) throws Exception {
        String ip = HttpServletUtils.getRemoteIpV4(request);
        RoomUserinDTO userinDto = roomService.getRoom(uid, visitorUid, os, appid, appVersion, ip);
        if (userinDto != null && "ios".equalsIgnoreCase(os)) {
            String pic = userinDto.getBackPic();
            if (StringUtils.isNotBlank(pic) && StringUtils.isNumeric(pic)) {
                if (Integer.valueOf(pic) > 10) {
                    userinDto.setBackPic("0");
                    userinDto.setBackPicUrl("");
                }
            }
        }
        return WebServiceMessage.success(userinDto);
    }

    @ApiOperation(value = "获取房间信息接口", notes = "根据uid获取房间信息", tags = {"房间接口", "小程序接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "要查看的用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "visitorUid", value = "当前用户", dataType = "long", required = true),
            @ApiImplicitParam(name = "os", value = "用户所在房间UID", dataType = "long", required = true),
            @ApiImplicitParam(name = "app", value = "赠送的礼物ID", dataType = "int", required = true),
            @ApiImplicitParam(name = "appVersion", value = "赠送的礼物数量", dataType = "int", required = true),
            @ApiImplicitParam(name = "t", value = "当前时间戳", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = RoomUserinDTO.class)
    })
    @SignVerification(client = Client.WXAPP)
    @RequestMapping(value = "v2/get", method = RequestMethod.GET)
    public WebServiceMessage getRoomByUidV2(HttpServletRequest request,
                                            @RequestParam(value = "uid", required = false) Long uid,
                                            @RequestParam(value = "visitorUid", required = false) Long visitorUid,
                                            @RequestParam(value = "os", required = false) String os,
                                            @RequestParam(value = "appid", required = false) String appid,
                                            @RequestParam(value = "appVersion", required = false) String appVersion) throws Exception {
        return this.getRoomByUid(request, uid, visitorUid, os, appid, appVersion);
    }

    /**
     * 根据uid获取房间信息
     */
    @ApiOperation(value = "获取房间信息接口", notes = "根据uid获取房间信息", tags = {"房间接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "要查看的用户ID", dataType = "long", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = RoomUserinDTO.class)
    })
    @RequestMapping(value = "/v3/get", method = RequestMethod.GET)
    public WebServiceMessage getRoomV3(@RequestParam(value = "roomId") Long roomId) throws WebServiceException {
        return WebServiceMessage.success(roomService.getRoomByRoomId(roomId));
    }

    @SignVerification
    @RequestMapping(value = "close", method = RequestMethod.POST)
    public WebServiceMessage closeRoom(@RequestParam(value = "uid", required = false) Long uid) throws WebServiceException {
        roomManager.close(uid);
        return WebServiceMessage.success(null);
    }

    @RequestMapping(value = "chatInfo", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    public String selectChatInfo() {
        return roomService.selectChatInfo();
    }

    @RequestMapping(value = "malice", method = RequestMethod.GET)
    public WebServiceMessage selectMalice() throws WebServiceException {
        return WebServiceMessage.success(roomService.selectMalice());
    }


    @ApiOperation(value = "获取连接池")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户UID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "ticket", value = "用户登录凭证", required = true, dataType = "String"),
            @ApiImplicitParam(name = "os", value = "操作系统", required = true, dataType = "String"),
            @ApiImplicitParam(name = "app", value = "应用名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appVersion", value = "应用版本", required = true, dataType = "String"),
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "linkPool", method = RequestMethod.GET)
    public WebServiceMessage linkPool(HttpServletRequest request,
                                      @RequestParam("uid") Long uid,
                                      @RequestParam(value = "os", required = false) String os,
                                      @RequestParam(value = "app", required = false) String app,
                                      @RequestParam(value = "appVersion", required = false) String appVersion) throws WebServiceException {
        String ip = HttpServletUtils.getRemoteIpV4(request);
        if (versionService.checkAuditingVersion(os, app, appVersion, ip, uid)) {
            return WebServiceMessage.success(roomService.listAuditLinkPool(uid));
        }
        return WebServiceMessage.success(roomService.linkPool(uid));
    }

    @RequestMapping(value = "link", method = RequestMethod.GET)
    @SignVerification
    @Authorization
    @ApiOperation(value = "获取连麦用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "uid", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "ticket", value = "ticket", required = true, dataType = "String"),
    })
    public WebServiceMessage link(HttpServletRequest request,
                                  @RequestParam("uid") Long uid,
                                  @RequestParam(value = "os", required = false) String os,
                                  @RequestParam(value = "app", required = false) String app,
                                  @RequestParam(value = "appVersion", required = false) String appVersion) throws WebServiceException {
        String ip = HttpServletUtils.getRemoteIpV4(request);
        if (versionService.checkAuditingVersion(os, app, appVersion, ip, uid)) {
            return WebServiceMessage.success(roomService.listAuditLinkPool(uid));
        }
        return WebServiceMessage.success(roomService.link(uid));
    }

    @Authorization
    @SignVerification
    @ApiOperation(value = "获取房间推荐列表数据", notes = "获取房间推荐列表数据")
    @GetMapping("getRoomRecommendList")
    public WebServiceMessage getRoomRecommendList(@RequestParam(value = "uid", required = false) Long uid,
                                                  @RequestParam(value = "os", required = false) String os,
                                                  @RequestParam(value = "appVersion", required = false) String appVersion,
                                                  @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                  @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                  @RequestParam(value = "appid", required = false) String appid) {
        return WebServiceMessage.success(roomService.getRoomRecommendList(uid, os, appVersion, appid, pageNum, pageSize));
    }


    @Authorization
    @SignVerification
    @ApiOperation(value = "获取大厅聊天数据", notes = "获取房间推荐列表数据")
    @GetMapping("getLobbyChatInfo")
    public WebServiceMessage getLobbyChatInfo(@RequestParam(value = "uid", required = false) Long uid,
                                              @RequestParam(value = "os", required = false) String os,
                                              @RequestParam(value = "appVersion", required = false) String appVersion,
                                              @RequestParam(value = "app", required = false) String app,
                                              HttpServletRequest request) throws WebServiceException {
        String ip = HttpServletUtils.getRemoteIpV4(request);
        return WebServiceMessage.success(roomService.getLobbyChatInfo(uid, os, appVersion, app, ip));
    }


    /**
     * 清除魅力值接口
     *
     * @param roomUid 房主ID
     * @param uid     麦序
     * @return
     */
    @Authorization
    @SignVerification
    @ApiOperation(value = "清除魅力值接口", notes = "清除魅力值接口")
    @GetMapping("receiveRoomMicMsg")
    public WebServiceMessage receiveRoomMicMsg(@RequestParam(value = "roomUid", required = true) Long roomUid,
                                               @RequestParam(value = "uid", required = true) Long uid) throws WebServiceException {
        if (roomUid == null || uid == null) {
            return WebServiceMessage.success(null);
        }
        roomCharmManager.clearMicCharm(roomUid, uid);
        return WebServiceMessage.success(null);
    }


}
