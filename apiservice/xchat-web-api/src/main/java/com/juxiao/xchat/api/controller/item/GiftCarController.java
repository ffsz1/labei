package com.juxiao.xchat.api.controller.item;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.item.GiftCarService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/giftCar")
@Api(tags = "装扮商城")
public class GiftCarController {
    @Autowired
    private GiftCarService giftCarService;

    @Authorization
    @SignVerification
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public WebServiceMessage listUserCar(HttpServletRequest request,
                                         @RequestParam("uid") Long uid,
                                         @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                         @RequestParam(value = "os", required = false) String os,
                                         @RequestParam(value = "app", required = false) String app,
                                         @RequestParam(value = "appVersion", required = false) String appVersion) throws WebServiceException {
        String ip = HttpServletUtils.getRemoteIpV4(request);
        return WebServiceMessage.success(giftCarService.listGiftCars(uid, pageNum, pageSize, os, app, appVersion, ip));
    }

    @ApiOperation(value = "座驾商城", notes = "需要登录ticket和加密, 只返回在商城出售的座驾")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int"),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true)
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/listMall", method = RequestMethod.POST)
    public WebServiceMessage listMall(@RequestParam("uid") Long uid,
                                      @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                      @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                      @RequestParam(value = "ticket", required = false) String ticket) throws WebServiceException {
        return WebServiceMessage.success(giftCarService.listMall(uid, pageNum, pageSize));
    }

    @ApiOperation(value = "用户拥有的座驾", notes = "需要登录ticket和加密, 返回用户拥有的座驾, 包括商城没有的座机")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "queryUid", value = "查询的用户ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int"),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "int"),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "int"),
            @ApiImplicitParam(name = "sn", value = "加密签名", dataType = "int"),
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/user/list", method = RequestMethod.POST)
    public WebServiceMessage listUserGiftCar(@RequestParam("uid") Long uid,
                                             @RequestParam(value = "queryUid", required = false) Long queryUid,
                                             @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                             @RequestParam(value = "pageSize", required = false) Integer pageSize) throws WebServiceException {
        return WebServiceMessage.success(giftCarService.listUserGiftCar(uid, queryUid, pageNum, pageSize));
    }

    @ApiOperation(value = "购买座驾", notes = "需要登录ticket和加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "carId", value = "购买座驾ID", dataType = "int", required = true),
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
    @RequestMapping(value = "/purse", method = RequestMethod.POST)
    public WebServiceMessage purse(@RequestParam("uid") Long uid,
                                   @RequestParam("carId") Integer carId,
                                   @RequestParam(value = "type", required = false) Integer type) throws WebServiceException {
        giftCarService.purse(uid, carId, type);
        return WebServiceMessage.success(null);
    }

    /**
     * @param uid
     * @param carId
     * @return
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "/use", method = RequestMethod.POST)
    public WebServiceMessage use(@RequestParam("uid") Long uid,
                                 @RequestParam("carId") Integer carId) throws WebServiceException {
        giftCarService.use(uid, carId);
        return WebServiceMessage.success(null);
    }

    @ApiOperation(value = "赠送座驾", notes = "需要登录ticket和加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "carId", value = "赠送座驾ID", dataType = "int", required = true),
            @ApiImplicitParam(name = "targetUid", value = "赠送目标用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/give", method = RequestMethod.POST)
    public WebServiceMessage give(@RequestParam("uid") Long uid,
                                  @RequestParam("carId") Integer carId,
                                  @RequestParam("targetUid") Long targetUid) throws WebServiceException {
        giftCarService.give(uid, carId, targetUid);
        return WebServiceMessage.success(WebServiceCode.SUCCESS);
    }
}
