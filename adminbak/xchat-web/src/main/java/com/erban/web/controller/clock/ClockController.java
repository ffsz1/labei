package com.erban.web.controller.clock;

import com.alibaba.fastjson.JSON;
import com.erban.main.service.clock.ClockAttendService;
import com.erban.main.service.clock.ClockResultService;
import com.erban.main.vo.ClockRecordVo;
import com.erban.web.common.BaseController;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/clock")
public class ClockController extends BaseController {
    @Autowired
    private ClockAttendService clockAttendService;
    @Autowired
    private ClockResultService clockResultService;

    @ResponseBody
    @RequestMapping(value = "/getStatus", method = RequestMethod.GET)
    public BusiResult getStatus(Long uid) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        logger.info("调用接口：/clock/getStatus；获取打卡活动参与状态，接口入参：uid:{}", uid);
        try {
            BusiResult busiResult = clockAttendService.getStatus(uid);
            logger.info("/clock/getStatus 接口出参：{}", JSON.toJSONString(busiResult));
            return busiResult;
        } catch (Exception e) {
            logger.error("/clock/getStatus error uid=" + uid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/resetStatus", method = RequestMethod.GET)
    public BusiResult resetStatus(Long uid) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try {
            return clockAttendService.resetStatus(uid);
        } catch (Exception e) {
            logger.error("/clock/resetStatus error uid=" + uid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getRecord", method = RequestMethod.GET)
    public BusiResult getRecord(Long uid) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        logger.info("调用接口：/clock/getRecord；获取打卡活动的情况，接口入参：uid:{}", uid);
        try {
            BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
            ClockRecordVo clockRecordVo = new ClockRecordVo();
            clockRecordVo = clockAttendService.getRecord(clockRecordVo, uid);
            clockRecordVo = clockResultService.getRecord(clockRecordVo);
            busiResult.setData(clockRecordVo);
            logger.info("/clock/getRecord 接口出参：{}", JSON.toJSONString(busiResult));
            return busiResult;
        } catch (Exception e) {
            logger.error("/clock/getRecord error ", e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/attendClock", method = RequestMethod.GET)
    public BusiResult attendClock(Long uid) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        logger.info("调用接口：/clock/attendClock；参与打卡活动，接口入参：uid:{}", uid);
        try {
            BusiResult busiResult = clockAttendService.attendClock(uid);
            logger.info("/clock/getStatus 接口出参：{}", JSON.toJSONString(busiResult));
            return busiResult;
        } catch (Exception e) {
            logger.error("/clock/attendClock error uid=" + uid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/doClock", method = RequestMethod.GET)
    public BusiResult doClock(Long uid) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        logger.info("调用接口：/clock/doClock；打卡，接口入参：uid:{}", uid);
        try {
            BusiResult busiResult = clockResultService.doClock(uid);
            logger.info("/clock/doClock 接口出参：{}", JSON.toJSONString(busiResult));
            return busiResult;
        } catch (Exception e) {
            logger.error("/clock/doClock error uid=" + uid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/doTestClock", method = RequestMethod.GET)
    public BusiResult doTestClock(Long uid) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        logger.info("调用接口：/clock/doClock；打卡，接口入参：uid:{}", uid);
        try {
            BusiResult busiResult = clockResultService.doTestClock(uid);
            logger.info("/clock/doClock 接口出参：{}", JSON.toJSONString(busiResult));
            return busiResult;
        } catch (Exception e) {
            logger.error("/clock/doClock error uid=" + uid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/myClockResult", method = RequestMethod.GET)
    public BusiResult myClockResult(Long uid, Integer pageNum, Integer pageSize) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        logger.info("调用接口：/clock/myClockResult；我打卡的记录，接口入参：uid:{}", uid);
        try {
            BusiResult busiResult = clockResultService.myClockResult(uid, pageNum, pageSize);
            logger.info("/clock/myClockResult 接口出参：{}", JSON.toJSONString(busiResult));
            return busiResult;
        } catch (Exception e) {
            logger.error("/clock/myClockResult error uid=" + uid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

}
