package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.common.user.UserRealNameManager;
import com.juxiao.xchat.service.api.user.UserRealNameService;
import com.juxiao.xchat.service.api.user.vo.UserRealNameVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user/realname")
@Api(tags = "实名认证接口", description = "用户信息接口")
public class UserRealNameController {
    @Resource
    private UserRealNameManager realNameManager;

    @Resource
    private UserRealNameService realnameService;

    @ApiOperation(value = "3.1.6. 实名认证短信验证码接口")
    @Authorization
    @RequestMapping(value = "v1/getSmsCode", method = RequestMethod.GET)
    public WebServiceMessage getSmsCode(@ApiParam(value = "用户ID") @RequestParam(value = "uid") Long uid,
                                        @ApiParam(value = "手机号") @RequestParam(value = "phone") String phone,
                                        @ApiParam(value = "设备唯一标识") @RequestParam(value = "deviceId") String deviceId,
                                        @ApiParam(value = "登录ticket") @RequestParam(value = "ticket") String ticket) throws Exception {
        realnameService.getSmsCode(uid, phone, deviceId);
        return WebServiceMessage.success(null);
    }

    @ApiOperation(value = "3.1.7. 实名认证信息保存接口")
    @Authorization
    @SignVerification
    @RequestMapping(value = "v1/save", method = RequestMethod.POST)
    public WebServiceMessage saveUserRealName(@ApiParam(value = "用户UID") @RequestParam(value = "uid") Long uid,
                                              @ApiParam(value = "用户真实名字") @RequestParam(value = "realName") String realName,
                                              @ApiParam(value = "身份证号码") @RequestParam(value = "idcardNo") String idcardNo,
                                              @ApiParam(value = "身份证正面照") @RequestParam(value = "idcardFront") String idcardFront,
                                              @ApiParam(value = "身份证反面照") @RequestParam(value = "idcardOpposite") String idcardOpposite,
                                              @ApiParam(value = "手持身份证照") @RequestParam(value = "idcardHandheld", required = false) String idcardHandheld,
                                              @ApiParam(value = "用户登录ticket") @RequestParam(value = "ticket") String ticket,
                                              @ApiParam(value = "系统") @RequestParam(value = "os") String os,
                                              @ApiParam(value = "客户端版本") @RequestParam(value = "appVersion") String appVersion,
                                              @ApiParam(value = "签名字符串") @RequestParam(value = "sn", required = false) String sn) throws WebServiceException {
        realnameService.saveUserRealName(uid, realName, idcardNo, idcardFront, appVersion, idcardOpposite, idcardHandheld);
        return WebServiceMessage.success(null);
    }

    @ApiOperation(value = "3.1.8. 获取实名认证信息接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = UserRealNameVO.class)
    })
    @Authorization
    @RequestMapping(value = "v1/get", method = RequestMethod.GET)
    public WebServiceMessage getUserRealName(@ApiParam(value = "用户UID") @RequestParam(value = "uid") Long uid,
                                             @ApiParam(value = "用户登录ticket") @RequestParam(value = "ticket") String ticket) throws WebServiceException {
        UserRealNameVO realNameVo = realnameService.getUserRealName(uid);
        return WebServiceMessage.success(realNameVo);
    }

    @RequestMapping(value = "v1/getUserRealNameStatus", method = RequestMethod.GET)
    public WebServiceMessage getUserRealNameStatus(@ApiParam(value = "用户UID") @RequestParam(value = "uid") Long uid,
                                                   @ApiParam(value = "开关类型") @RequestParam(value = "type") String type) throws WebServiceException {
        realNameManager.verifyUserRealName(uid, type);
        return WebServiceMessage.success(null);
    }

    @ApiOperation(value = "app兑换金币页面实名验证接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = UserRealNameVO.class)
    })
    @Authorization
    @RequestMapping(value = "v1/getAppUserRealName", method = RequestMethod.GET)
    public WebServiceMessage getValidateUserRealName(@ApiParam(value = "用户UID") @RequestParam(value = "uid") Long uid,
                                                     @ApiParam(value = "用户登录ticket") @RequestParam(value = "ticket") String ticket) throws WebServiceException {
        UserRealNameVO realNameVo = realnameService.getValidateUserRealName(uid);
        return WebServiceMessage.success(realNameVo);
    }
}
