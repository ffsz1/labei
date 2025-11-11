package com.juxiao.xchat.api.controller.sysconf;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.sysconf.domain.ClientSecurityInfoDO;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import com.juxiao.xchat.service.api.sysconf.ChannelService;
import com.juxiao.xchat.service.api.sysconf.ClientService;
import com.juxiao.xchat.service.api.sysconf.bo.ChannelAuditBO;
import com.juxiao.xchat.service.api.sysconf.enumeration.ChannelEnum;
import com.juxiao.xchat.service.api.sysconf.vo.AppInitVo;
import com.juxiao.xchat.service.api.sysconf.vo.WxAppInitVo;
import com.juxiao.xchat.service.api.user.bo.DeviceInfoBO;
import com.juxiao.xchat.service.common.bo.BaseParamBO;
import com.juxiao.xchat.service.common.sysconf.AppVersionService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/client")
@Api(tags = "客户端配置接口", description = "客户端配置接口")
public class ClientController {
    @Resource
    private AppVersionService appVersionService;

    @Resource
    private ClientService clientService;

    @Resource
    private SysConfManager sysconfManager;

    @Resource
    private ChannelService channelService;

    @Resource
    private AppVersionManager appVersionManager;

    @ApiOperation(value = "APP启动拉取的初始化数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "os", value = "客户端操作系统", dataType = "string"),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本号", dataType = "string")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = AppInitVo.class)
    })
    @RequestMapping(value = "/init", method = {RequestMethod.GET, RequestMethod.POST})
    public WebServiceMessage startInit(HttpServletRequest request,
                                       @RequestParam(value = "os", required = false) String os,
                                       @RequestParam(value = "appid", required = false) String appid,
                                       @RequestParam(value = "appVersion", required = false) String appVersion,
                                       @RequestParam(value = "uid", required = false) Long uid) throws WebServiceException {
        String ip = HttpServletUtils.getRemoteIpV4(request);
        return WebServiceMessage.success(clientService.init(os, appid, appVersion, ip, uid));
    }

    @ApiOperation(value = "小程序初始化数据", tags = {"客户端配置接口", "小程序接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appVersion", value = "客户端版本号", dataType = "string")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = WxAppInitVo.class)
    })
    @RequestMapping(value = "/wxapp/init", method = {RequestMethod.GET, RequestMethod.POST})
    public WebServiceMessage initWxapp(@RequestParam("appVersion") String appVersion, @RequestParam("uid") Long uid,
                                       @RequestParam(value = "appid") String appid) throws WebServiceException {
        boolean isAudit = appVersionManager.checkAuditingVersion("wxapp", appid, appVersion, "", uid);
        SysConfDTO confDto = sysconfManager.getSysConf(SysConfigId.wxapp_luxury_gift);
        return WebServiceMessage.success(new WxAppInitVo(isAudit, System.currentTimeMillis(), confDto == null ?
                "iPhone XS" : confDto.getConfigValue()));
    }

    @ApiOperation(value = "APP启动拉取的配置信息")
    @RequestMapping(value = "/configure", method = {RequestMethod.GET, RequestMethod.POST})
    public WebServiceMessage getConfigure(@RequestParam(value = "idfa", required = false) String idfa,
                                          @RequestParam(value = "appid", required = false) String appid,
                                          @RequestParam(value = "imei", required = false) String imei,
                                          DeviceInfoBO deviceInfo, HttpServletRequest request) {
        return WebServiceMessage.success(clientService.getConfig(idfa, imei, deviceInfo,
                HttpServletUtils.getRemoteIpV4(request), appid));
    }

    @ApiOperation(value = "获取渠道审核信息")
    @GetMapping("channel")
    @Authorization
    public WebServiceMessage channel(ChannelAuditBO channel) {
        if (channel == null) {
            return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        }
        ChannelEnum channelEnum;
        try {
            channelEnum = ChannelEnum.valueOf(channel.getChannel());
        } catch (Exception e) {
            channelEnum = ChannelEnum.xbd;
        }
        return WebServiceMessage.success(channelService.checkAudit(channelEnum, channel.getAppVersion(),
                channel.getUid()));
    }

    @ApiOperation(value = "保存客户端安全上报的信息", notes = "需要验证签名")
    @PostMapping("security/saveInfo")
    @SignVerification
    public WebServiceMessage saveInfo(ClientSecurityInfoDO securityInfoDO) {
        return WebServiceMessage.success(clientService.saveSecurityInfo(securityInfoDO));
    }

    @ApiOperation(value = "保存客户端上传的日志信息", notes = "需要验证签名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "日志文件地址", dataType = "string")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Boolean.class)
    })
    @PostMapping("log/save")
    @SignVerification
    public WebServiceMessage saveLogInfo(HttpServletRequest request, BaseParamBO paramBO, String url) {
        String ip = HttpServletUtils.getRealIp(request);
        return WebServiceMessage.success(clientService.saveLogInfo(paramBO, url, ip));
    }
}
