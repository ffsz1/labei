package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.user.dto.UserSettingDTO;
import com.juxiao.xchat.service.api.user.UserSettingService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/setting")
@Api(tags = "用户配置接口", description = "用户配置接口")
public class UserSettingController {

    @Autowired
    private UserSettingService userSettingService;

    @ApiOperation(value = "保存用户设置信息接口", notes = "需要ticket和加密字段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long"),
            @ApiImplicitParam(name = "likedSend", value = "是否接受关注用户上线消息：1，发送；2，不发送", dataType = "Byte"),
            @ApiImplicitParam(name = "chatPermission", value = "私聊的权限 0关闭, 1 所有人, 2 10级以下用户", dataType = "Integer"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 1444, message = "非法的参数")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/v1/save", method = RequestMethod.POST)
    public WebServiceMessage save(@RequestParam(value = "uid") Long uid,
                                  @RequestParam(value = "likedSend", required = false) Byte likedSend,
                                  @RequestParam(value = "chatPermission", required = false) Integer chatPermission) throws WebServiceException {
        userSettingService.save(uid, likedSend, chatPermission);
        return WebServiceMessage.success(null);
    }

    @ApiOperation(value = "获取用户设置信息接口", notes = "需要ticke和加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", dataType = "Long"),
            @ApiImplicitParam(name = "queryUid", value = "查询的用户(返回的该用户的设置)", dataType = "Long"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = UserSettingDTO.class),
            @ApiResponse(code = 1900, message = "参数错误")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/v1/get", method = RequestMethod.POST)
    public WebServiceMessage getUserSetting(@RequestParam(value = "uid") Long uid,
                                            @RequestParam(value = "queryUid") Long queryUid) throws WebServiceException {
        UserSettingDTO settingDto;
        // 兼容旧版
        if (queryUid == null) {
            settingDto = userSettingService.getUserSetting(uid);
        } else {
            settingDto = userSettingService.getUserSetting(queryUid);
        }
        settingDto.setCreateTime(null);
        return WebServiceMessage.success(settingDto);
    }
}