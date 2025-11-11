package com.juxiao.xchat.api.controller.external;


import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.external.zego.ZegoManager;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "zego")
@Api(description = "第三方接口", tags = "第三方接口")
public class ZegoController {

    @Autowired
    private ZegoManager zegoManager;


    @ApiOperation(value = "获取即构thirdToken接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomUid", value = "用户进入房间房主UID", dataType = "long", required = true),
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "int"),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "int"),
            @ApiImplicitParam(name = "appVersion", value = "当前版本", dataType = "int"),
            @ApiImplicitParam(name = "t", value = "时间戳", dataType = "int"),
            @ApiImplicitParam(name = "sn", value = "签名", dataType = "int"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回内容：{\"thirdToken\":xxxx}")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/v1/getThirdToken", method = RequestMethod.POST)
    public WebServiceMessage getThirdToken(@RequestParam("roomUid") Long roomUid, @RequestParam("uid") Long uid) throws Exception {
        String thirdToken = zegoManager.generateThirdToken(roomUid, uid);
        JSONObject object = new JSONObject();
        object.put("thirdToken", thirdToken);
        return WebServiceMessage.success(object);
    }

    @ApiOperation(value = "获取即构thirdToken接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回内容：{\"accessToken\":xxxx}")
    })
    @RequestMapping(value = "/v1/getAccessToken", method = RequestMethod.GET)
    public WebServiceMessage getAccessToken() throws Exception {
        String accessToken = zegoManager.getAccessToken();
        JSONObject object = new JSONObject();
        object.put("accessToken", accessToken);
        return WebServiceMessage.success(object);
    }
}
