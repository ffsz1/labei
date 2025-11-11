package com.juxiao.xchat.api.controller.charge;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.user.dto.UserPurseExchangeDTO;
import com.juxiao.xchat.service.api.charge.DiamondExchangeService;
import com.juxiao.xchat.service.api.charge.vo.GiveGoldVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/change")
@Api(description = "钻石接口", tags = "钻石接口")
public class DiamondExchangeController {
    @Autowired
    private DiamondExchangeService exchangeService;

    @ApiOperation(value = "钻石兑换金币", notes = "需要加密、需要ticket")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "diamondNum", value = "钻石数量", dataType = "double", required = true),
            @ApiImplicitParam(name = "smsCode", value = "短信验证码", dataType = "string"),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本号", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "加密签名", dataType = "string", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = UserPurseExchangeDTO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "gold", method = RequestMethod.POST)
    public WebServiceMessage exchangeDiamondToGold(@RequestParam("uid") Long uid,
                                                   @RequestParam("diamondNum") Double diamondNum,
                                                   @RequestParam(value = "phone", required = false) String phone,
                                                   @RequestParam(value = "passwordSecond", required = false) String passwordSecond,
                                                   @RequestParam(value = "os", required = false) String os,
                                                   @RequestParam("appVersion") String appVersion) throws WebServiceException {
        return WebServiceMessage.success(exchangeService.exchange2Gold(uid, diamondNum, os, appVersion, phone, passwordSecond));
    }

    @ApiOperation(value = "金币转赠", notes = "需要加密、需要ticket")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "sendUid", value = "赠送者Id", dataType = "long", required = true),
            @ApiImplicitParam(name = "recvUid", value = "接收者Id", dataType = "long", required = true),
            @ApiImplicitParam(name = "goldNum", value = "金币数量", dataType = "long", required = true),
            @ApiImplicitParam(name = "smsCode", value = "短信验证码", dataType = "string", required = false),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = GiveGoldVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "gold2gold", method = RequestMethod.POST)
    public WebServiceMessage changeGoldToGold(@RequestParam("sendUid") Long sendUid,
                                              @RequestParam("recvUid") Long recvUid,
                                              @RequestParam("goldNum") Integer goldNum,
                                              @RequestParam(value = "smsCode", required = false) String smsCode) throws WebServiceException {
        return WebServiceMessage.success(exchangeService.userGiveGold(sendUid, recvUid, goldNum, smsCode));
    }

    @ApiOperation(value = "金币转赠权限校验", notes = "需要加密、需要ticket")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Boolean.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "givegoldcheck", method = RequestMethod.POST)
    public WebServiceMessage giveGoldCheck(@RequestParam("uid") Long uid) throws WebServiceException {
        return WebServiceMessage.success(exchangeService.giveGoldCheck(uid));
    }
}
