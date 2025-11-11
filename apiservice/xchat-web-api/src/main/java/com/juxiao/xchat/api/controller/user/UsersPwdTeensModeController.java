package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.user.UsersPwdTeensModeService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 青少年模式
 *
 * @author chris
 * @date 2019-07-03
 */
@RestController
@RequestMapping("/users/teens/mode")
@Api(tags = "青少年模式接口", description = "青少年模式接口")
public class UsersPwdTeensModeController {
    @Resource
    private UsersPwdTeensModeService usersPwdTeensModeService;

    @ApiOperation(value = "获取青少年接口信息", notes = "需要ticket、需要加密", tags = "APP接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", dataType = "long", required = true),
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
    @GetMapping("/getUsersTeensMode")
    public WebServiceMessage getUsersTeensMode(Long uid) throws WebServiceException {
        return WebServiceMessage.success(usersPwdTeensModeService.getUsersTeensMode(uid));
    }

    @ApiOperation(value = "校验青少年模式密码", notes = "需要ticket、需要加密", tags = "APP接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "cipherCode", value = "密码", dataType = "long", required = true),
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
    @GetMapping("/checkCipherCode")
    public WebServiceMessage checkCipherCode(Long uid, String cipherCode) throws WebServiceException {
        return usersPwdTeensModeService.checkCipherCode(uid, cipherCode);
    }

    @ApiOperation(value = "设置青少年模式密码", notes = "需要ticket、需要加密", tags = "APP接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "cipherCode", value = "密码", dataType = "long", required = true),
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
    @PostMapping("/save")
    public WebServiceMessage saveOrUpdate(Long uid, String cipherCode) throws WebServiceException {
        return usersPwdTeensModeService.saveOrUpdate(uid, cipherCode);
    }

    @ApiOperation(value = "关闭青少年模式密码", notes = "需要ticket、需要加密", tags = "APP接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", dataType = "long", required = true),
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
    @PostMapping("/closeTeensMode")
    public WebServiceMessage closeTeensMode(Long uid) throws WebServiceException {
        return usersPwdTeensModeService.closeTeensMode(uid);
    }
}
