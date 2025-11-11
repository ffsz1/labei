package com.erban.web.controller;

import com.erban.main.service.OrderServService;
import com.erban.main.util.StringUtils;
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

/**
 * Created by liuguofu on 2017/6/9.
 */
@Controller
@RequestMapping("/order")
public class OrderServController {
    private static final Logger logger = LoggerFactory.getLogger(OrderServController.class);
    @Autowired
    private OrderServService orderServService;

    /**
     * 根据uid获取订单列表
     *
     * @param uid
     * @param type 订单类型：1创建订单，2订单已完成，3订单异常
     * @return
     */
    @ResponseBody
    @SignVerification
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public BusiResult getOrderServList(Long uid, int type) {
        if (uid == null | uid == 0L) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            busiResult = orderServService.getOrderServList(uid);
        } catch (Exception e) {
            logger.info("getOrderServList Exception:" + e.getMessage() + "  uid=" + uid);
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage("服务器好繁忙，请稍后重试！");
            return busiResult;
        }
        return busiResult;
    }

    /**
     * 根据订单ID获取订单
     *
     * @param orderId
     * @return
     */
    @ResponseBody
    @SignVerification
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public BusiResult getOrderServById(Long orderId) {

        if (orderId == null | orderId == 0L) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            busiResult = orderServService.getOrderServListById(orderId);
        } catch (Exception e) {
            logger.info("getOrderServById Exception:" + e.getMessage() + "  orderServId=" + orderId);
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage("服务器好繁忙，请稍后重试！");
            return busiResult;
        }
        return busiResult;
    }

    /**
     * 结束订单,用户主动结束订单
     *
     * @param uid
     * @param orderId
     * @return
     */
    @ResponseBody
    @SignVerification
    @RequestMapping(value = "finish", method = RequestMethod.POST)
    public BusiResult userFinishOrder(Long uid, Long orderId) {
        if (orderId == null | orderId == 0L) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            busiResult = orderServService.finishOrderServ(uid, orderId);
        } catch (Exception e) {
            logger.info("userFinishOrder Exception:" + e.getMessage() + "  orderId=" + orderId);
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage("服务器好繁忙，请稍后重试！");
            return busiResult;
        }
        return busiResult;
    }

    /**
     * 开始服务，拨通电话
     *
     * @param uid
     * @param orderId
     * @param channelId 频道号
     * @return
     */
    @ResponseBody
    @Authorization
    @SignVerification
    @RequestMapping(value = "serv", method = RequestMethod.POST)
    public BusiResult doSerivce(Long uid, Long orderId, String channelId) {

        if (orderId == null | orderId == 0L || StringUtils.isEmpty(channelId)) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            busiResult = orderServService.doOrderServ(uid, orderId, channelId);
        } catch (Exception e) {
            logger.info("doSerivce Exception:" + e.getMessage() + "  orderId=" + orderId);
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage("服务器好繁忙，请稍后重试！");
            return busiResult;
        }
        return busiResult;
    }

    @ResponseBody
    @SignVerification
    @RequestMapping(value = "/evaluate", method = RequestMethod.POST)
    public BusiResult evalOrder(Long uid, Long orderId, Integer score, String desc) {
        if (orderId == null || uid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            orderServService.evalOrder(uid, orderId, score, desc);
        } catch (Exception e) {
            logger.info("doService Exception:" + e.getMessage() + "orderId=" + orderId);
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage("服务器好繁忙，请稍后重试！");
            return busiResult;
        }
        return busiResult;
    }

}
