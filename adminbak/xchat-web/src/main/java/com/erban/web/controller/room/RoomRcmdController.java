package com.erban.web.controller.room;

import com.erban.main.service.room.RoomRcmdService;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @class: RoomRecommendController.java
 * @author: chenjunsheng
 * @date 2018/8/13
 */
@Controller
@RequestMapping("/room/rcmd")
public class RoomRcmdController {
    @Autowired
    private RoomRcmdService rcmdService;

    /**
     * @return
     */
    @ResponseBody
    @SignVerification
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public BusiResult getRcmdRoom(@RequestParam("uid") Long uid) {
        BusiResult result = rcmdService.getUserRcmdRoom(uid);
        return result;
    }
}
