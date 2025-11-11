package com.juxiao.xchat.api.controller.sysconf;

import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.service.common.sysconf.AppVersionService;
import com.juxiao.xchat.service.common.sysconf.vo.AppVersionVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/version")
@Api(tags = "客户端配置接口", description = "客户端配置接口")
public class AppVersionController {
    @Autowired
    private AppVersionManager appVersionManager;

    @Autowired
    private AppVersionService appVersionService;


    @ApiOperation(value = "获取更新版本信息", notes = "AppVersionController：需要加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appVersion", value = "app版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = AppVersionVO.class)
    })
    @SignVerification
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public WebServiceMessage getVersionInfo(HttpServletRequest request,
                                            @RequestParam(value = "appid", required = false) String appid,
                                            @RequestParam(value = "uid", required = false) Long uid,
                                            @RequestParam(value = "appVersion", required = false) String appVersion,
                                            @RequestParam(value = "os", required = false) String os) throws WebServiceException {
        String ip = HttpServletUtils.getRemoteIpV4(request);
//        if (appVersionManager.checkAuditingVersion(os, appid, appVersion, ip,uid)) {
//            AppVersionVO versionVo = new AppVersionVO();
//            versionVo.setStatus((byte) 2);
//            return WebServiceMessage.success(versionVo);
//        }
        return WebServiceMessage.success(appVersionService.getAppVersionInfo(appVersion, os,appid));
    }

}
