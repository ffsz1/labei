package com.juxiao.xchat.api.controller.sysconf;

import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceMessage;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @class: AppCrashController.java
 * @author: chenjunsheng
 * @date 2018/7/5
 */

@RestController
@RequestMapping(value = "/app/crash")
@Api(tags = "客户端配置接口", description = "客户端配置接口")
public class AppCrashController {
    // 输出另外一个日志文件
    private final Logger logger = LoggerFactory.getLogger("com.juxiao.xchat.api.controller.crash");

    @ApiOperation(value = "收集客户端异常信息", notes = "需要加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "osVersion", value = "操作系统版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "model", value = "机型", dataType = "string", required = true),
            @ApiImplicitParam(name = "deviceId", value = "设备ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "app版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "crash", value = "错误信息", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名", dataType = "string")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @SignVerification
    @RequestMapping(value = "/v1/save", method = RequestMethod.POST)
    public WebServiceMessage saveError(@RequestParam("os") String os,
                                       @RequestParam("osVersion") String osVersion,
                                       @RequestParam("model") String model,
                                       @RequestParam("deviceId") String deviceId,
                                       @RequestParam("appVersion") String appVersion,
                                       @RequestParam("crash") String crash) {
        logger.info("{}\t{}\t{}\t{}\t{}\t{}", os, osVersion, model, deviceId, appVersion, crash.replaceAll("\\n", "").replaceAll("\\t", ""));
        return WebServiceMessage.success(null);
    }
}
