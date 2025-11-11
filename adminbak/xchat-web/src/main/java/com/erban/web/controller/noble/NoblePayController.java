package com.erban.web.controller.noble;

import com.erban.main.model.NobleRight;
import com.erban.main.model.NobleUsers;
import com.erban.main.service.ChargeService;
import com.erban.main.service.noble.NoblePayService;
import com.erban.main.service.noble.NobleRightService;
import com.erban.main.service.noble.NobleUsersService;
import com.erban.web.common.BaseController;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.oauth2.service.http.HttpUitls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 贵族开通或者续费
 *
 */
@Controller
@RequestMapping("/noble/pay")
public class NoblePayController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(NoblePayController.class);

    @Autowired
    private ChargeService chargeService;
    @Autowired
    private NoblePayService noblePayService;
    @Autowired
    private NobleRightService nobleRightService;
    @Autowired
    private NobleUsersService nobleUsersService;


    /**
     * 通过支付开通贵族
     *
     * @return
     */
    @RequestMapping(value = "/open/bymoney")
    @ResponseBody
    @Authorization
    public BusiResult openByMoney(HttpServletRequest request,Long uid, Long roomUid, Integer nobleId, String payChannel
            , String successUrl) {
        if (BlankUtil.isBlank(payChannel) || BlankUtil.isBlank(successUrl) || uid == null || nobleId == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        logger.info("openByMoney param===>>uid:{}, nobleId:{}, payChannel:{}, roomUid:{}", uid, nobleId, payChannel, roomUid);
        // 判断是否存在该类型贵族
        NobleRight nobleRight = nobleRightService.getNobleRight(nobleId);
        if (nobleRight == null) {
            return new BusiResult(BusiStatus.NOBLENOTEXISTS);
        }
        // 同一等级贵族不能重复开通
        NobleUsers nobleUsers = nobleUsersService.getNobleUser(uid);
        if (nobleUsers != null && DateTimeUtil.compareTime(nobleUsers.getExpire(), new Date()) > 0) {
            if (nobleUsers.getNobleId() >= nobleId) {
                return new BusiResult(BusiStatus.NOBLEUSEREXIST);
            }
        }

        try {
            String payDesc = "开通" + nobleRight.getName();
            String clientIp = HttpUitls.getRealIpAddress(request);
            return chargeService.noblePay(uid, nobleId, roomUid, nobleRight.getOpenGold(), payDesc, payChannel
                    , Constant.PayBussType.openNoble, clientIp, successUrl);
        } catch (Exception e) {
            logger.error("openByMoney error, uid=" + uid + ", nobleId=" + nobleId + ",payChannel=" + payChannel, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }


    /**
     * 通过金币开通贵族
     *
     * @return
     */
    @RequestMapping(value = "/open/bygold")
    @ResponseBody
    @Authorization
    public BusiResult openByGold(HttpServletRequest request, Long uid, Long roomUid, Integer nobleId) {
        if (uid == null || nobleId == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        logger.info("openByGold param===>>uid:{}, roomUid:{}, nobleId:{}, payChannel:{}", uid, roomUid
                , nobleId);
        NobleRight nobleRight = nobleRightService.getNobleRight(nobleId);
        if (nobleRight == null) {
            return new BusiResult(BusiStatus.NOBLENOTEXISTS);
        }
        // 同一等级贵族不能重复开通
        NobleUsers nobleUsers = nobleUsersService.getNobleUser(uid);
        if (nobleUsers != null && DateTimeUtil.compareTime(nobleUsers.getExpire(), new Date()) > 0) {
            if (nobleUsers.getNobleId() >= nobleId) {
                return new BusiResult(BusiStatus.NOBLEUSEREXIST);
            }
        }

        try {
            String clientIp = HttpUitls.getRealIpAddress(request);
            return noblePayService.openNobleByGold(uid, roomUid, nobleRight,clientIp);
        } catch (Exception e) {
            logger.error("openByGold error:" + e.getMessage() + "  uid=" + uid + ",nobleId=" + nobleId, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 通过支付续费贵族
     *
     * @return
     */
    @RequestMapping(value = "/renew/bymoney")
    @ResponseBody
    @Authorization
    public BusiResult renewByMoney(HttpServletRequest request,Long uid, Long roomUid, String payChannel, String successUrl) {
        if (BlankUtil.isBlank(payChannel) || uid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        logger.info("renewByMoney param===>>uid:{}, roomUid:{}, payChannel:{}", uid, roomUid, payChannel);

        // 判断该用户是否已开通贵族且未过期
        NobleUsers nobleUsers = nobleUsersService.getNobleUser(uid);
        if(nobleUsers == null) {
            return new BusiResult(BusiStatus.NOBLEEXPIRE);
        }

        try {
            NobleRight nobleRight = nobleRightService.getNobleRight(nobleUsers.getNobleId());
            String payDesc = "续费" + nobleRight.getName();
            String clientIp = HttpUitls.getRealIpAddress(request);
            return chargeService.noblePay(uid, nobleRight.getId(), roomUid, nobleRight.getRenewGold(), payDesc
                    , payChannel, Constant.PayBussType.renewNoble, clientIp, successUrl);
        } catch (Exception e) {
            logger.error("renewByMoney error, uid=" + uid + ", payChannel=" + payChannel, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }


    /**
     * 通过金币续费贵族
     *
     * @return
     */
    @RequestMapping(value = "/renew/bygold")
    @ResponseBody
    @Authorization
    public BusiResult renewByGold(HttpServletRequest request, Long uid, Long roomUid) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        logger.info("renewByGold param===>>uid:{}, roomUid:{}", uid, roomUid);

        // 判断该用户是否已开通贵族且未过期
        NobleUsers nobleUsers = nobleUsersService.getNobleUser(uid);
        if(nobleUsers == null || DateTimeUtil.compareTime(nobleUsers.getExpire(), new Date()) < 0) {
            return new BusiResult(BusiStatus.NOBLEEXPIRE);
        }

        try {
            NobleRight nobleRight = nobleRightService.getNobleRight(nobleUsers.getNobleId());
            String clientIp = HttpUitls.getRealIpAddress(request);
            return noblePayService.renewNobleByGold(uid, roomUid, nobleRight, clientIp);
        } catch (Exception e) {
            logger.error("renewByGold error, uid=" + uid, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }
}
