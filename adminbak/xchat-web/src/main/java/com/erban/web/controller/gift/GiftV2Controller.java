package com.erban.web.controller.gift;

import com.alibaba.fastjson.JSON;
import com.erban.main.service.duty.DutyService;
import com.erban.main.service.duty.DutyType;
import com.erban.main.service.gift.GiftSendService;
import com.erban.main.service.home.CheckExcessService;
import com.erban.main.service.user.UsersService;
import com.google.common.util.concurrent.RateLimiter;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;


@Controller
@RequestMapping("/gift")
public class GiftV2Controller {
    private static final Logger logger = LoggerFactory.getLogger(GiftV2Controller.class);
    @Autowired
    private DutyService dutyService;
    @Autowired
    private GiftSendService giftSendService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private CheckExcessService checkExcessService;
    private RateLimiter sendAllLimiter = RateLimiter.create(1500.0); // 1秒1500并发
    private RateLimiter sendOneLimiter = RateLimiter.create(2000.0); // 1秒2000并发

    /**
     * 给单个用户送礼物
     *
     * @param uid
     * @param giftId
     * @param targetUid
     * @param roomUid
     * @param type
     * @param giftNum
     * @return
     */
    @ResponseBody
    @Authorization
    @RequestMapping(value = "/sendV3")
    public BusiResult sendGiftV4(long uid, long targetUid, Long roomUid, byte type, int giftId, int giftNum, String expressMessage, HttpServletRequest request) {
        logger.info("接口调用：（/gift/sendV3），给单个用户送礼物,接口入参：uid:{},targetUid:{},roomUid:{},type:{},giftId:{},giftNum:{}", uid, targetUid, roomUid, type, giftId, giftNum);
        String appVersion = request.getParameter("appVersion");
        BusiResult busiResult = null;
        if (giftNum <= 0 || targetUid == 0) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        // 对接口限流
        if (!sendOneLimiter.tryAcquire(5, TimeUnit.SECONDS)) {
            return new BusiResult(BusiStatus.SERVERBUSY);
        }
        if (!usersService.isExist(targetUid)) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        try {
            if (Constant.SendGiftType.person == type) {
                busiResult = giftSendService.sendGiftInPrivateChat(uid, targetUid, roomUid, giftId, giftNum, type);
            } else if (Constant.SendGiftType.express == type) {
                // 表白
                busiResult = giftSendService.sendExpressGift(uid, targetUid, roomUid, giftId, giftNum, type, expressMessage);
            } else {
                busiResult = giftSendService.sendGiftInRoomChat(uid, targetUid, roomUid, giftId, giftNum, type, appVersion);
                try {
                    if (busiResult.getCode() == 200) {
                        giftSendService.sendMsgAllRoom(uid, targetUid, roomUid, giftId, giftNum, null);
                    }
                } catch (Exception e) {
                    logger.error("发送全房间礼物失败  causeby:" + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            logger.error("sendV4 error uid=" + uid + "&giftId=" + giftId + "&targetUid=" + targetUid + "&giftNum=+"
                    + giftNum + "&roomUid=" + roomUid + "  causeby:" + e.getMessage(), e);
            checkExcessService.sendSms("10000000");
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        logger.info("给单个用户送礼物,接口出参：{}", JSON.toJSONString(busiResult));

        if (busiResult.getCode() == 200) {
            try {
                dutyService.updateDailyDuty(uid, DutyType.gift_send.getDutyId());
            } catch (Exception e) {
                logger.error("[ 更新每日任务 ] 异常：", e);
            }
        }
        return busiResult;
    }

    /**
     * 给全麦送礼物
     *
     * @param uid
     * @param targetUids
     * @param roomUid
     * @param giftId
     * @param giftNum
     * @return
     */
    @ResponseBody
    @Authorization
    @RequestMapping(value = "/sendWholeMicroV3")
    public BusiResult sendWholeMicroV4(long uid, @RequestParam(value = "targetUids") Long[] targetUids
            , Long roomUid, int giftId, int giftNum, HttpServletRequest request) {
        logger.info("接口调用：（/gift/sendWholeMicroV3），给全麦送礼物,接口入参：uid:{},targetUids:{},roomUid:{},giftId:{},giftNum:{}", uid, targetUids, roomUid, giftId, giftNum);
        String appVersion = request.getParameter("appVersion");
        BusiResult busiResult = null;
        if (giftNum <= 0 || targetUids == null || targetUids.length < 1) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        // 对接口限流
        if (!sendAllLimiter.tryAcquire(5, TimeUnit.SECONDS)) {
            return new BusiResult(BusiStatus.SERVERBUSY);
        }
        try {
            busiResult = giftSendService.sendGiftToAll(uid, targetUids, roomUid, giftId, giftNum, appVersion);
            if (busiResult.getCode() == 200) {
                try {
                    giftSendService.sendMsgAllRoom(uid, null, roomUid, giftId, giftNum, targetUids);
                } catch (Exception e) {
                    logger.error("发送全房间礼物失败  causeby:" + e.getMessage(), e);
                }
                try {
                    dutyService.updateDailyDuty(uid, DutyType.gift_send.getDutyId());
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            logger.error("sendWholeMicroV4 error uid=" + uid + "&giftId=" + giftId + "&targetUid=" + targetUids
                    + "&giftNum=+" + giftNum + "&roomUid=" + roomUid + "  causeby:" + e.getMessage(), e);
            checkExcessService.sendSms("10000000");
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        logger.info("接口调用：（/gift/sendWholeMicroV3），给全麦送礼物,接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

}
