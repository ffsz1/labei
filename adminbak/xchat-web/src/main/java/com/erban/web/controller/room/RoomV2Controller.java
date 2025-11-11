package com.erban.web.controller.room;

import com.erban.main.model.Room;
import com.erban.main.service.room.RoomCleanService;
import com.erban.main.service.room.RoomService;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 版本 v2
 */
@Controller
@RequestMapping("/room/v2")
public class RoomV2Controller {
    private static final Logger logger = LoggerFactory.getLogger(RoomV2Controller.class);
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomCleanService roomCleanService;

    @RequestMapping(value = "open")
    @ResponseBody
    @Authorization
    public BusiResult openRoom(Room room) {
        if (room == null || room.getUid() == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL, "参数异常");
        }
        try {
            return  roomService.openRoom(room);
        } catch (Exception e) {
            logger.error("openRoom error..uid=" + room.getUid(), e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 清除僵尸房间
     *
     * @return
     */
    @RequestMapping(value = "/clean")
    @ResponseBody
    public BusiResult cleanInvalidRoom() {
        try {
            roomCleanService.cleanInvalidRoom();
        } catch (Exception e) {
            logger.error("cleanInvalidRoom error", e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }


}
