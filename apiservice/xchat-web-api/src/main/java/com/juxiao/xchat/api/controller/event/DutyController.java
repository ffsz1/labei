package com.juxiao.xchat.api.controller.event;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.event.DutyService;
import com.juxiao.xchat.service.api.event.vo.DutiesVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/duty")
@Api(tags = "活动接口")
public class DutyController {
    @Autowired
    private DutyService dutyService;

    /**
     * 获取任务列表
     *
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public WebServiceMessage listDuties(@RequestParam("uid") Long uid) throws WebServiceException {
        //DutiesVO dutiesVo = dutyService.getUserDuties(uid);
        return WebServiceMessage.success(new DutiesVO());
    }

    @ApiOperation(value = "获取任务奖励", notes = "需要登录ticket和加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dutyId", value = "任务ID", dataType = "int", required = true),
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/achieve", method = RequestMethod.POST)
    public WebServiceMessage achieve(@RequestParam("dutyId") Integer dutyId, @RequestParam("uid") Long uid) throws WebServiceException {
        // dutyService.achieve(dutyId, uid);
        return WebServiceMessage.success(null);
    }

    /**
     * 大厅发言保存接口
     *
     * @param uid
     * @return
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "/fresh/public", method = RequestMethod.POST)
    public WebServiceMessage speakPublic(@RequestParam("uid") Long uid) throws WebServiceException {
        //dutyService.speakPublic(uid);
        return WebServiceMessage.success(null);
    }
}
