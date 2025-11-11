package com.erban.web.controller.room;

import com.alibaba.fastjson.JSON;
import com.erban.main.model.Room;
import com.erban.main.param.RoomAdminParam;
import com.erban.main.param.RoomParam;
import com.erban.main.service.room.RoomService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.RoomVo;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/room")
public class RoomController {
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
    @Autowired
    private RoomService roomService;

    /**
     * 新用户开房间
     *
     * @param room
     * @return
     */
    @RequestMapping(value = "open")
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult openRoom(Room room) {
        logger.info("接口调用：（/room/open）,没有房间时，需要开房间，接口入参：room:{}", JSON.toJSONString(room));
        BusiResult busiResult = null;
        if (room == null || room.getUid() == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL, "参数异常");
        }
        try {
            busiResult = roomService.openRoom(room);
        } catch (Exception e) {
            logger.error("openRoom error..uid=" + room.getUid(), e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        logger.info("接口调用：（/room/open）,没有房间时，需要开房间，接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    @RequestMapping(value = "update")
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult updateRoomWhileRunning(RoomParam roomParam, String ticket) {
        if (roomParam == null || roomParam.getUid() == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL, "参数异常");
        }
        BusiResult<RoomVo> busiResult = null;
        Room room = convertRoomParamToRoom(roomParam);
        try {
            busiResult = roomService.updateRunningRoom(room);
        } catch (Exception e) {
            logger.error("updateRoomWhileRunning error..uid=" + roomParam.getUid(), e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    @RequestMapping(value = "updateByAdmin")
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult updateRoomWhileRunningByAdmin(RoomAdminParam roomAdminParam) {
        Long adminUid = roomAdminParam.getUid();
        if (roomAdminParam == null || roomAdminParam.getUid() == null || roomAdminParam.getRoomUid() == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL, "参数异常");
        }
        BusiResult<RoomVo> busiResult = null;
        Room room = convertRoomParamToRoom(roomAdminParam);
        try {
            busiResult = roomService.updateRunningRoomByAdmin(room, adminUid);
        } catch (Exception e) {
            logger.error("updateRoomWhileRunningByAdmin error..uid=" + roomAdminParam.getUid() + "roomUid=" + roomAdminParam.getRoomUid(), e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    @RequestMapping(value = "close", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult closeRoom(Long uid) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL, "参数异常");
        }
        BusiResult<RoomVo> busiResult = null;
        try {
            busiResult = roomService.closeRoom(uid);
        } catch (Exception e) {
            logger.error("close room error..uid=" + uid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    /**/
    @ResponseBody
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public BusiResult getRoomByUid(Long uid, String os, String appVersion, HttpServletRequest request) {
        if (uid == null) {
            return new BusiResult<>(BusiStatus.PARAMETERILLEGAL, "参数异常");
        }
        BusiResult<RoomVo> busiResult = null;
        long startTime = System.currentTimeMillis();
        try {
            busiResult = roomService.queryRoomByUid(uid, os, appVersion, request);
        } catch (Exception e) {
            logger.error("getRoomByUid error..uid=" + uid, e);
            busiResult = new BusiResult<>(BusiStatus.BUSIERROR);
        } finally {
            logger.info("[ room/get ]请求：os={}&uid={}&appVersion={}，返回:>{}，耗时：{}", os, uid, appVersion, busiResult, System.currentTimeMillis() - startTime);
        }
        return busiResult;
    }

    @RequestMapping(value = "getwhole", method = RequestMethod.GET)
    @ResponseBody
    @SignVerification
    public BusiResult getWholeRoomByUid(Long uid) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL, "参数异常");
        }
        BusiResult<RoomVo> busiResult = null;
        try {
            busiResult = roomService.getWholeRoomVoWithUsersByUid(uid);
        } catch (Exception e) {
            logger.error("getRoomByUid error..uid=" + uid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }


    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    @SignVerification
    public BusiResult getRoomListByUids(String uids) {
        if (StringUtils.isEmpty(uids)) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL, "参数异常");
        }
        BusiResult<List<RoomVo>> busiResult = null;
        try {
            busiResult = roomService.queryRoomListByUids(uids);
        } catch (Exception e) {
            logger.error("queryRoomListByUids error..uids=" + uids, e);
            busiResult = new BusiResult<List<RoomVo>>(BusiStatus.BUSIERROR);
            return busiResult;
        }
        return busiResult;
    }

    @RequestMapping(value = "/viproom", method = RequestMethod.POST)
    @ResponseBody
    @SignVerification
    public BusiResult addVipRoom(Long erbanNo) {
        BusiResult busiResult = null;
        if (erbanNo == null) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        try {
            busiResult = roomService.addVipRoom(erbanNo);
        } catch (Exception e) {
            logger.error("viproom error..erbanNo=" + erbanNo, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    @RequestMapping(value = "onlines", method = RequestMethod.GET)
    @ResponseBody
    @SignVerification
    public BusiResult getRoomOnlines(String uids) {
        BusiResult busiResult = null;
        if (StringUtils.isEmpty(uids)) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        try {
            busiResult = roomService.getRoomOnlines(uids);
        } catch (Exception e) {
            logger.error("onlines error..uids=" + uids, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    private Room convertRoomParamToRoom(RoomParam roomParam) {
        try {
            Room room = new Room();
            room.setUid(roomParam.getUid());
            room.setTitle(roomParam.getTitle());
            room.setBackPic(roomParam.getBackPic());
            room.setOperatorStatus(Constant.RoomOptStatus.in);
            room.setType(roomParam.getType());
            room.setRoomId(roomParam.getRoomId());
            room.setRoomDesc(roomParam.getRoomDesc());
            room.setRoomNotice(roomParam.getRoomNotice());
            room.setRoomPwd(roomParam.getRoomPwd());
            room.setRoomTag(roomParam.getRoomTag());
            room.setTagId(roomParam.getTagId());
            return room;
        } catch (Exception e) {
            return null;
        }
    }

    private Room convertRoomParamToRoom(RoomAdminParam roomAdminParam) {
        try {
            Room room = new Room();
            room.setUid(roomAdminParam.getRoomUid());
            room.setTitle(roomAdminParam.getTitle());
            room.setBackPic(roomAdminParam.getBackPic());
            room.setOperatorStatus(Constant.RoomOptStatus.in);
            room.setType(roomAdminParam.getType());
            room.setRoomId(roomAdminParam.getRoomId());
            room.setRoomDesc(roomAdminParam.getRoomDesc());
            room.setRoomNotice(roomAdminParam.getRoomNotice());
            room.setRoomPwd(roomAdminParam.getRoomPwd());
            room.setRoomTag(roomAdminParam.getRoomTag());
            room.setTagId(roomAdminParam.getTagId());
            return room;
        } catch (Exception e) {
            return null;
        }
    }

}
