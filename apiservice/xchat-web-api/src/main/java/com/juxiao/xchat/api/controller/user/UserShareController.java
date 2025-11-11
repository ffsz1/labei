package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.Client;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.user.UserShareService;
import com.juxiao.xchat.service.api.user.vo.WxappShareInfoVO;
import io.swagger.annotations.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/share")
@Api(tags = "用户信息接口", description = "用户信息接口")
public class UserShareController {

    @Autowired
    private UserShareService shareService;

    @ApiOperation(value = "微信小程序邀请旧用户保存接口", notes = "需要ticket、需要加密", tags = "小程序接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shareUid", value = "分享用户uid", dataType = "long", required = true),
            @ApiImplicitParam(name = "uid", value = "当前用户uid", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "用户登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "app版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "t", value = "当前时间戳", dataType = "long", required = true),
            @ApiImplicitParam(name = "sn", value = "签名", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 401, message = "需要登录"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/v1/save", method = RequestMethod.POST)
    public WebServiceMessage saveWxappShare(@Param("shareUid") Long shareUid, @Param("uid") Long uid) throws WebServiceException {
//        shareService.saveWxappShare(shareUid, uid);
        return WebServiceMessage.success(null);
    }

    @ApiOperation(value = "获取本人免费捡海螺及其邀请用户列表接口", notes = "需要ticket、需要加密", tags = "小程序接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户uid", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "用户登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "app版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "t", value = "当前时间戳", dataType = "long", required = true),
            @ApiImplicitParam(name = "sn", value = "签名", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = WxappShareInfoVO.class),
            @ApiResponse(code = 401, message = "需要登录"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification(client = Client.WXAPP)
    @RequestMapping(value = "/v1/get", method = RequestMethod.GET)
    public WebServiceMessage getUserWxappShareInfo(@Param("uid") Long uid) throws WebServiceException {
        WxappShareInfoVO shareinfo = shareService.getUserWxappShareInfo(uid);
        return WebServiceMessage.success(shareinfo);
    }
}
