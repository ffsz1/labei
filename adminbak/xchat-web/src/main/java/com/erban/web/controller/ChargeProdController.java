package com.erban.web.controller;

import com.erban.main.service.ChargeProdService;
import com.erban.main.vo.ChargeProdVo;
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
@RequestMapping("/chargeprod")
public class ChargeProdController {
    private static final Logger logger = LoggerFactory.getLogger(ChargeProdController.class);
    @Autowired
    private ChargeProdService chargeProdService;

    /**
     * 获取产品充值列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public BusiResult getChargeProdList(int channelType, HttpServletRequest request) {
        logger.info("接口调用(/chargeprod/list),充值产品接口入参:channelType:{},url:{}", channelType, request.getRequestURL());
        String appVersion = request.getParameter("appVersion");
        String os = request.getParameter("os");
        BusiResult<List<ChargeProdVo>> busiResult = null;
        try {
            busiResult = chargeProdService.getAllChargeProdVoList(channelType, appVersion, os);
        } catch (Exception e) {
            logger.error("getChargeProdList Exception:" + e.getMessage());
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage("服务器好繁忙，请稍后重试！");
            return busiResult;
        }
        return busiResult;
    }

}
