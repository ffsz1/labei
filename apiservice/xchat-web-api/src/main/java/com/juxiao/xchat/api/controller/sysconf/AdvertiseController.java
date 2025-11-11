package com.juxiao.xchat.api.controller.sysconf;

import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.sysconf.AdvertiseService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/advertise")
@Api(description = "其他接口", tags = "其他")
public class AdvertiseController {
    @Autowired
    private AdvertiseService advertiseService;

    @SignVerification
    @RequestMapping(value = "getList", method = RequestMethod.GET)
    public WebServiceMessage getList(HttpServletRequest request,
                                     @RequestParam(value = "uid", required = false) Long uid,
                                     @RequestParam(value = "os", required = false) String os,
                                     @RequestParam(value = "appid", required = false) String appid,
                                     @RequestParam(value = "appVersion", required = false) String appVersion) throws WebServiceException {
        return WebServiceMessage.success(advertiseService.list(os, appid, appVersion, HttpServletUtils.getRemoteIpV4(request),uid));
    }

}
