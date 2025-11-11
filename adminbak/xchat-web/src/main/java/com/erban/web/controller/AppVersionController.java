package com.erban.web.controller;

import com.erban.main.service.AppVersionService;
import com.erban.main.util.StringUtils;
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
 * Created by liuguofu on 2017/7/10.
 */
@Controller
@RequestMapping("/version")
public class AppVersionController {
    @Autowired
    private AppVersionService appVersionService;
    private static final Logger logger = LoggerFactory.getLogger(AppVersionController.class);


    @ResponseBody
    @SignVerification
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public BusiResult getVersionInfo(String appVersion, String os) {
        if (StringUtils.isBlank(appVersion) || StringUtils.isBlank(os)) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL, "参数异常");
        }
        BusiResult busiResult;
        try {
            busiResult = appVersionService.getAppVersionInfo(appVersion, os);
        } catch (Exception e) {
            logger.error("getVersionInfo error..appVersion=" + appVersion, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    @ResponseBody
    @SignVerification
    @RequestMapping(value = "update", method = RequestMethod.GET)
    public BusiResult updateVersion(String version, Byte status) {
        if (StringUtils.isBlank(version) || status == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL, "参数异常");
        }
        BusiResult busiResult = null;
        try {
            busiResult = appVersionService.updateVersion(version, status);
        } catch (Exception e) {
            logger.error("getVersionInfo error..version=" + version, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    @ResponseBody
    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    public BusiResult refreshAllVersionInfo() {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            appVersionService.cleanCache();
        } catch (Exception e) {
            logger.error("refreshAllVersionInfo error..version=", e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public BusiResult getVersionInfoList() {
        BusiResult busiResult = null;
        try {
            busiResult = appVersionService.getAllAppVersionList();
        } catch (Exception e) {
            logger.error("getVersionInfoList error.....", e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public BusiResult addVersion() {
        BusiResult busiResult = null;
        try {
            busiResult = appVersionService.getAllAppVersionList();
        } catch (Exception e) {
            logger.error("addVersion error.....", e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public BusiResult updateVersion() {
        BusiResult busiResult = null;
        try {
            busiResult = appVersionService.getAllAppVersionList();
        } catch (Exception e) {
            logger.error("updateVersion error.....", e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }


}
