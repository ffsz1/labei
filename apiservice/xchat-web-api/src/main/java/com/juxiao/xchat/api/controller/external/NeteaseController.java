package com.juxiao.xchat.api.controller.external;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.Client;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.external.netease.NetEaseChatroomManager;
import com.juxiao.xchat.manager.external.netease.NetEaseRoomManager;
import com.juxiao.xchat.manager.external.netease.ret.AddrNetEaseRet;
import com.juxiao.xchat.manager.external.netease.ret.NeteaseReqAddrRet;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @class: NeteaseController.java
 * @author: chenjunsheng
 * @date 2018/7/5
 */
@RestController
@RequestMapping(value = "/netease")
@Api(description = "其他接口", tags = "其他")
public class NeteaseController {
    @Autowired
    private NetEaseRoomManager neteaseRoomManager;
    @Autowired
    private NetEaseChatroomManager chatroomManager;
    @Autowired
    private RoomManager roomManager;

    /**
     * 变更聊天室的角色信息
     *
     * @param roomId
     * @param uid
     * @param ext
     * @return
     * @throws Exception
     */
    @SignVerification
    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    public WebServiceMessage updateRole(@RequestParam("roomId") Long roomId,
                                        @RequestParam("uid") Long uid,
                                        @RequestParam(value = "ext", required = false) String ext) throws Exception {
//        int ret = chatroomManager.updateRole(roomId, String.valueOf(uid), true, ext);
//        if (ret == 200) {
//            return WebServiceMessage.success(null);
//        }

        return WebServiceMessage.failure(WebServiceCode.ERROR2);
    }

    @RequestMapping(value = "inRoom", method = RequestMethod.POST)
    public WebServiceMessage inRoom(Long roomId, String accId) {
        if (roomId == null || accId == null) {
            return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        }
        try {
//            AddrNetEaseRet addrNetEaseRet = neteaseRoomManager.inRoom(roomId, accId, 1);
//            if (addrNetEaseRet.getCode() == 200) {
//                return WebServiceMessage.success(addrNetEaseRet.getAddr());
//            } else {
                return WebServiceMessage.failure(WebServiceCode.NO_AUTHORITY);
//            }
        } catch (Exception e) {
            return WebServiceMessage.failure(WebServiceCode.SERVER_ERROR);
        }
    }

    @ApiOperation(value = "获取云信聊天室地址", notes = "请求云信聊天室地址与令牌，只做中转", tags = {"其他", "小程序接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "roomUid", value = "进入房间的房主UID", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "t", value = "当前时间戳", dataType = "long", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = NeteaseReqAddrRet.class)
    })
    @Authorization
    @SignVerification(client = Client.WXAPP)
    @RequestMapping(value = "chatroom/addr", method = RequestMethod.POST)
    public NeteaseReqAddrRet getChatroomRequestAddr(HttpServletRequest request,
                                                    @RequestParam("uid") Long uid,
                                                    @RequestParam("roomUid") Long roomUid) throws Exception {
        if (uid == null || uid == 0 || roomUid == null || roomUid == 0) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        RoomDTO roomDto = roomManager.getUserRoom(roomUid);
        if (roomDto == null || roomDto.getRoomId() == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        String ip = HttpServletUtils.getRemoteIpV4(request);
        return null;
//        return chatroomManager.requestAddr(roomDto.getRoomId(), uid, 3, ip);
    }
}
