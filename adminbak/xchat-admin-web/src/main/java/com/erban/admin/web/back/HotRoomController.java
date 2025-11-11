package com.erban.admin.web.back;

import com.erban.admin.main.service.HotRoomService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hotroom")
public class HotRoomController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HotRoomController.class);
    @Autowired
    private HotRoomService hotRoomService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addHotRoom(Long erbanNo, Integer startTime, Integer endTime, Integer seqNo) {
        if (erbanNo == null || startTime == null || endTime == null || seqNo == null) {
            return "error";
        }
        try {
            hotRoomService.addHotRoom(erbanNo, startTime, endTime, seqNo);
        } catch (Exception e) {
            logger.error("addHotRoom error..erbanNo=" + erbanNo, e);
            return "error";
        }
        return "success";
    }

    @RequestMapping(value = "/viproom", method = RequestMethod.POST)
    @ResponseBody
    public String addVipRoom(Long erbanNo) {
        if (erbanNo == null) {
            return "error";
        }
        try {
            hotRoomService.addVipRoom(erbanNo);
        } catch (Exception e) {
            logger.error("viproom error..erbanNo=" + erbanNo, e);
            return "error:(" + e.getMessage() + ")";
        }
        return "success";
    }


}
