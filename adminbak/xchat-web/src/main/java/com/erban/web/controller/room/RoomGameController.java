package com.erban.web.controller.room;

import com.erban.main.service.room.RoomGameService;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/room/game")
public class RoomGameController {
    private static final Logger logger = LoggerFactory.getLogger(RoomGameController.class);
    @Autowired
    private RoomGameService roomGameService;

    @ResponseBody
    @RequestMapping(value = "getState", method = RequestMethod.GET)
    @SignVerification
    public BusiResult getState(Long uid, Long roomId) {
        try {
            return roomGameService.getState(uid, roomId);
        } catch (Exception e) {
            logger.error("/room/game/getState error..uid:{},roomId:{}", uid, roomId, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @RequestMapping(value = "choose", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult choose(Long uid, Long roomId, Integer type, String result) {
        try {
            return roomGameService.choose(uid, roomId, type, result);
        } catch (Exception e) {
            logger.error("/room/game/choose error..uid:{},roomId:{}", uid, roomId, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @RequestMapping(value = "confirm", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult confirm(Long uid, Long roomId, Integer type, String result) {
        try {
            return roomGameService.confirm(uid, roomId, type, result);
        } catch (Exception e) {
            logger.error("/room/game/confirm error..uid:{},roomId:{}", uid, roomId, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

}
