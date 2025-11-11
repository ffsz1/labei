package com.erban.web.controller;

import com.erban.web.common.BaseController;
import com.xchat.common.annotation.SignVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.erban.main.service.BannerService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;

@Controller
@RequestMapping("/banner")
public class BannerController extends BaseController {
    @Autowired
    private BannerService bannerService;

    @SignVerification
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getBannerList(Long uid, String os) {
        try {
            return new BusiResult(BusiStatus.SUCCESS, bannerService.getBannerList(uid, os));
        } catch (Exception e) {
            logger.error("getBannerList Exception:" + e.getMessage());
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    @SignVerification
    @RequestMapping(value = "/getBanner", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getBanner(Integer bannerId) {
        try {
            return new BusiResult(BusiStatus.SUCCESS, bannerService.getOneByJedisId(bannerId.toString()));
        } catch (Exception e) {
            logger.error("getBannerList Exception:" + e.getMessage());
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }
}
