package com.erban.web.controller;

import com.erban.main.service.HomeService;
import com.erban.main.vo.HomeVo;
import com.erban.main.vo.UserVo;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by liuguofu on 2017/6/26.
 */
@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private HomeService homeService;
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);


    @ResponseBody
    @SignVerification
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public BusiResult getHomeData(String os) {
        BusiResult<List<UserVo>> busiResult = null;
        try {
            busiResult = homeService.getHomeData(os);
        } catch (Exception e) {
            logger.error("get home error.....", e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    @ResponseBody
    @SignVerification
    @RequestMapping(value = "getV2", method = RequestMethod.GET)
    public BusiResult getHomeDataV2(String type, String os, String appVersion) {
        BusiResult<HomeVo> busiResult = null;
        try {
            busiResult = homeService.getHomeDataV2(type, os, appVersion);
        } catch (Exception e) {
            logger.error("getV2 home error.....", e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    @ResponseBody
    @SignVerification
    @RequestMapping(value = "iosaudit", method = RequestMethod.GET)
    public BusiResult getIosAuditing(Long uid) {
        BusiResult<List<UserVo>> busiResult = null;
        try {
            busiResult = homeService.getIOSAuditHomeData(uid);
        } catch (Exception e) {
            logger.error("getIosAuditing home error.....", e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }
}
