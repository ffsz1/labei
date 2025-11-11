package com.erban.web.controller;

import com.alibaba.fastjson.JSON;
import com.erban.main.service.ChargeService;
import com.erban.main.service.home.CheckExcessService;
import com.erban.main.util.IpUtil;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.UserVo;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/charge")
public class ChargeController {
    private static final Logger logger = LoggerFactory.getLogger(ChargeController.class);
    @Autowired
    private ChargeService chargeService;
    @Autowired
    private CheckExcessService checkExcessService;

    /**
     * 发起充值
     *
     * @param uid
     * @param chargeProdId 充值产品ID
     * @param payChannel      充值渠道
     * @param clientIp     充值客户端IP
     * @return
     */
    @ResponseBody
    @Authorization
    @RequestMapping(value = "apply", method = RequestMethod.POST)
    public BusiResult applyCharge(Long uid, String chargeProdId, String payChannel, String clientIp,String successUrl,HttpServletRequest request) {
        logger.info("发起充值支付/apply/charge..uid="+uid+"&chargeProdId="+chargeProdId+"&payChannel="+payChannel+"&clientIp="+clientIp+"&successUrl="+successUrl);
        if(StringUtils.isEmpty(payChannel)||uid==null||uid==0L||StringUtils.isEmpty(chargeProdId)){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        BusiResult<List<UserVo>> busiResult=null;
        try {
            if(StringUtils.isEmpty(clientIp)){
                clientIp= IpUtil.getRemoteIp(request);
            }
            busiResult=chargeService.applyCharge(uid, chargeProdId, payChannel, clientIp,successUrl);
        } catch (Exception e) {
            logger.error("发起充值支付接口（/apply/charge）异常，Exception:"+e.getMessage()+"  uid="+uid+",chargeProdId="+chargeProdId+",payChannel="+payChannel,e);
            checkExcessService.sendSms("10000000");
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        logger.info("发起充值支付接口（/apply/charge）,接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

}
