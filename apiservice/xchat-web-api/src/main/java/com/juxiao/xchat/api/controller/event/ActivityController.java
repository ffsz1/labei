package com.juxiao.xchat.api.controller.event;

import com.google.common.collect.Lists;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.service.api.event.ActivityService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 活动入口
 *
 * @class: ActivityController.java
 * @author: chenjunsheng
 * @date 2018/6/6
 */
@RestController
@RequestMapping("/activity")
@Api(tags = "活动接口")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @Autowired
    private AppVersionManager versionService;

    /**
     * 查询活动接口
     *
     * @param type 弹窗位置
     * @param uid  用户ID
     * @return
     * @author: chenjunsheng
     * @date 2018/6/7
     */
    @SignVerification
    @RequestMapping(value = "query", method = RequestMethod.GET)
    public WebServiceMessage listWinLocActivity(HttpServletRequest request,
                                                @RequestParam(value = "type", required = false) Integer type,
                                                @RequestParam(value = "uid", required = false) Long uid,
                                                @RequestParam(value = "os", required = false) String os,
                                                @RequestParam(value = "appid", required = false) String appid,
                                                @RequestParam(value = "appVersion", required = false) String appVersion) throws WebServiceException {
        String ip = HttpServletUtils.getRemoteIpV4(request);
        if (versionService.checkAuditingVersion(os, appid, appVersion, ip, uid)) {
            return WebServiceMessage.success(Lists.newArrayList());
        }
        return WebServiceMessage.success(activityService.listWinActivity(type, uid));
    }

    /**
     * 查询活动接口
     *
     * @param uid 用户ID
     * @return
     * @author: chenjunsheng
     * @date 2018/6/7
     */
    @SignVerification
    @RequestMapping(value = "queryAll", method = RequestMethod.GET)
    public WebServiceMessage listWinLocActivity(HttpServletRequest request,
                                                @RequestParam(value = "uid", required = false) Long uid,
                                                @RequestParam(value = "os", required = false) String os,
                                                @RequestParam(value = "appid", required = false) String appid,
                                                @RequestParam(value = "appVersion", required = false) String appVersion) throws WebServiceException {
        String ip = HttpServletUtils.getRemoteIpV4(request);
        if (versionService.checkAuditingVersion(os, appid, appVersion, ip, uid)) {
            return WebServiceMessage.success(Lists.newArrayList());
        }
        return WebServiceMessage.success(activityService.listAllActivity(uid));
    }

}
