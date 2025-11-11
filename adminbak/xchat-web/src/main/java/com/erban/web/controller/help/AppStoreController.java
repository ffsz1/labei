package com.erban.web.controller.help;

import com.erban.main.service.AppVersionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.erban.main.service.AppStoreService;
import com.erban.main.util.StringUtils;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liuguofu on 2017/7/10.
 */
@Controller
@RequestMapping("/appstore")
public class AppStoreController {
    private static final Logger logger = LoggerFactory.getLogger(AppStoreController.class);

    @Autowired
    private AppStoreService appStoreService;
    @Autowired
    AppVersionService appVersionService;

    @RequestMapping(value = "check",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult checkAuditingIosVersion(@RequestParam("version")String version, HttpServletRequest request){
        try {
            boolean result = appVersionService.checkIsAuditingVersion(version, request);
            BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
            busiResult.setData(result);
            return busiResult;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @RequestMapping(value = "forceupdate",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult checkIsNeedForceUpdate(String version){
        if(StringUtils.isBlank(version)){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL,"参数异常");
        }
        BusiResult busiResult=null;
        try {
            busiResult=appStoreService.checkIsNeedForceUpdate(version);
        } catch (Exception e) {
            logger.error("checkIsNeedForceUpdate error..version="+version,e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    @RequestMapping(value = "running",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult checkIsRunningVersion(String version){
        if(StringUtils.isBlank(version)){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL,"参数异常");
        }
        BusiResult busiResult=null;
        try {
            busiResult=appStoreService.checkIsNeedForceUpdate(version);
        } catch (Exception e) {
            logger.error("checkIsRunningVersion error..version="+version,e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }



    @RequestMapping(value = "get",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getVersionInfo(@RequestParam("version") String version, HttpServletRequest request){
        return checkAuditingIosVersion(version, request);
    }


}
