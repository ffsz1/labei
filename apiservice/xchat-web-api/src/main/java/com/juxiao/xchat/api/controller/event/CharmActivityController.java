package com.juxiao.xchat.api.controller.event;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.event.CharmActivityService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chris
 * @date 2019-07-10
 */
@RestController
@RequestMapping("/charm/activity")
@Api(tags = "魅力活动榜单 接口")
public class CharmActivityController {

    @Autowired
    private CharmActivityService charmActivityService;

    @ApiOperation(value = "魅力活动榜单", notes = "需要登录ticket和加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @RequestMapping(value = "/getCharmByList", method = RequestMethod.GET)
    public WebServiceMessage getCharmByList(@RequestParam("uid") Long uid) throws WebServiceException {
        return WebServiceMessage.success(charmActivityService.getUsersCharmByPage(uid));
    }




}
