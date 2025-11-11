package com.erban.web.controller.statis;

import com.erban.main.service.statis.StatBasicRoomService;
import com.erban.main.service.statis.StatSumRoomService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@RequestMapping("/basicroom")
@Controller
public class StatBasicRoomController {
    private static final Logger logger = LoggerFactory.getLogger(StatBasicRoomController.class);

    @Autowired
    private StatSumRoomService statSumRoomService;

    @Autowired
    private StatBasicRoomService statBasicRoomService;

    @RequestMapping(value = "/getsummoods", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getMoods(Long roomUid) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (roomUid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        busiResult = statSumRoomService.queryMoods(roomUid);
        return busiResult;
    }

    @RequestMapping(value = "/getsumopentime", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getOpenTime(Long roomUid) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (roomUid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        busiResult = statSumRoomService.getOpenTime(roomUid);
        return busiResult;
    }

    @RequestMapping(value = "/getmoods", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getMoods(Long roomUid, Long time) throws Exception{
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (roomUid == null && time == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        Date times = new Date(time);
        busiResult = statBasicRoomService.getMoods(roomUid, times);
        return busiResult;
    }

    @RequestMapping(value = "/getopentime", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getOpenTime(Long roomUid, Long time)  {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (roomUid == null && time == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        Date times = new Date(time);
        busiResult = statBasicRoomService.getOpenTime(roomUid, times);
        return busiResult;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult list(Long time) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (time == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        Date times = new Date(time);
        busiResult = statBasicRoomService.getRooms(times);
        return busiResult;
    }

    @RequestMapping(value = "/avgtime")
    @ResponseBody
    public BusiResult avgTime(Long roomUid, Long time) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (roomUid == null && time == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        Date times = new Date(time);
        busiResult = statBasicRoomService.getAvgTime(roomUid, times);
        return busiResult;
    }
}
