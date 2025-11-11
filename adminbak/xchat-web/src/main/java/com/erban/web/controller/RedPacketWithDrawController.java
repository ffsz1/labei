package com.erban.web.controller;

import com.alibaba.fastjson.JSON;
import com.erban.main.service.RedPacketWithDrawService;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.apache.ibatis.annotations.Delete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 红包controller
 */
@Controller
@RequestMapping("/redpacket")
public class RedPacketWithDrawController {
    private static final Logger logger = LoggerFactory.getLogger(RedPacketWithDrawController.class);
    @Autowired
    private RedPacketWithDrawService redPacketWithDrawService;

    /**
     *获取有效的的红包(withdraw_packet_cash_prod)产品
     * @return
     */
    @ResponseBody
    @SignVerification
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BusiResult getWithDraw() {
        logger.info("接口调用：/redpacket/list,获取红包产品list，接口入参：无");
        BusiResult busiResult = null;
        try {
            busiResult = redPacketWithDrawService.findWithDrawList();
        } catch (Exception e) {
            logger.error("接口调用：/redpacket/list,获取红包产品list异常,报错：{}", e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        logger.info("获取红包产品list(/redpacket/list),接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }


    /**
     * 所有用户红包提现数据展示
     * @return
     */
    @ResponseBody
    @SignVerification
    @RequestMapping(value = "/drawlist", method = RequestMethod.GET)
    public BusiResult getUserWithDrawList() {
        logger.info("接口调用：/redpacket/drawlist,获取红包提现列表，接口入参：无");
        BusiResult busiResult = null;
        try {
            busiResult = redPacketWithDrawService.getUserWithDrawList();
        } catch (Exception e) {
            logger.error("接口调用：/redpacket/drawlist,获取红包提现列表异常，报错：{}", e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        logger.info("获取红包提现列表(/redpacket/drawlist),接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /**
     * 红包提现
     * @param uid
     * @param packetId
     * @return
     */
    @ResponseBody
    @Authorization
    @SignVerification
    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public BusiResult withDraw(Long uid, Integer packetId) {
        logger.info("接口调用：/redpacket/withdraw,红包提现，接口入参：uid:{},packetId:{}",uid,packetId);
        BusiResult busiResult = null;
        if (uid == null || packetId == null) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        try {
            busiResult = redPacketWithDrawService.withDraw(uid, packetId);
        } catch (Exception e) {
            logger.error("接口调用：/redpacket/withdraw,红包提现异常，uid：{}，报错：{}",uid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        logger.info("红包提现(/redpacket/drawlist),接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }
}
