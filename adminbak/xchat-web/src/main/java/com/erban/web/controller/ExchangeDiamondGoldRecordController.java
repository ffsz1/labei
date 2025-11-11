package com.erban.web.controller;

import com.alibaba.fastjson.JSON;
import com.erban.main.model.SysConf;
import com.erban.main.service.SysConfService;
import com.erban.main.service.record.ExchangeDiamondGoldRecordService;
import com.erban.web.common.BaseController;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/change")
@Controller
public class ExchangeDiamondGoldRecordController extends BaseController {
    @Autowired
    private ExchangeDiamondGoldRecordService exchangeDiamondGoldRecordService;
    @Autowired
    private SysConfService sysConfService;

    /**
     *  兑换钻石成金币
     *
     * @param uid
     * @param diamondNum
     * @return
     */
    @ResponseBody
    @Authorization
    @SignVerification
    @RequestMapping(value = "gold",method = RequestMethod.POST)
    public BusiResult exchangeDiamondToGold(Long uid, double diamondNum, String os, String appVersion, String smsCode){
        logger.info("调用接口：（/change/gold）, 兑换钻石成金币接口，接口入参：uid:{},diamondNum:{}",uid,diamondNum);
        BusiResult busiResult=null;
        if(uid==null||uid==0L || diamondNum <= 0){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            SysConf isExchangeAwards = sysConfService.getSysConfById("is_exchange_awards");
            if(isExchangeAwards==null||"0".equals(isExchangeAwards.getConfigValue())){
                busiResult = exchangeDiamondGoldRecordService.exchangeDiamondToGold(uid, diamondNum, false, smsCode, os);
            }else{
                busiResult = exchangeDiamondGoldRecordService.exchangeDiamondToGold(uid, diamondNum, true, smsCode, os);
            }
        } catch (Exception e) {
            logger.error("exchangeDiamondToGold error..uid="+uid+"&diamondNum="+diamondNum,e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        logger.info("兑换钻石成金币接口(/change/gold),接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

}
