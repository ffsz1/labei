package com.juxiao.xchat.api.controller.item;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.item.HeadwearService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/headwear")
@Api(tags = "装扮商城")
public class HeadwearController {
    @Autowired
    private HeadwearService headwearService;

    @ApiOperation(value = "头饰商城(旧)", notes = "返回商城出售的头饰和用户拥有的头饰, 剩余时间等.")
    @Authorization
    @SignVerification
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public WebServiceMessage listHeadwears(@RequestParam("uid") Long uid,
                                           @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                           @RequestParam(value = "pageSize", required = false) Integer pageSize) throws WebServiceException {
        return WebServiceMessage.success(headwearService.listHeadwears(uid, pageNum, pageSize));
    }

    @ApiOperation(value = "头饰商城", notes = "需要登录ticket和加密, 只返回在商城出售的头饰")
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
        return WebServiceMessage.success(headwearService.listMall(uid, pageNum, pageSize));
    }

    @ApiOperation(value = "用户拥有的头饰", notes = "需要登录ticket和加密, 只返回用户拥有的头饰, 包括商城没有的头饰")
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
    public WebServiceMessage listUserHeadwear(@RequestParam("uid") Long uid,
                                              @RequestParam(value = "ticket") String ticket,
                                              @RequestParam(value = "queryUid", required = false) Long queryUid,
                                              @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                              @RequestParam(value = "pageSize", required = false) Integer pageSize) throws WebServiceException {
        return WebServiceMessage.success(headwearService.listUserHeadwear(uid, queryUid, pageNum, pageSize));
    }

    @ApiOperation(value = "购买头饰", notes = "需要登录ticket和加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "headwearId", value = "购买头饰ID", dataType = "string", required = true),
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
                                   @RequestParam("headwearId") Integer headwearId,
                                   @RequestParam("type") Integer type) throws WebServiceException {
        headwearService.purse(uid, headwearId, type);
        return WebServiceMessage.success(WebServiceCode.SUCCESS);
    }

    @Authorization
    @SignVerification
    @RequestMapping(value = "/use", method = RequestMethod.POST)
    public WebServiceMessage use(@RequestParam("uid") Long uid,
                                 @RequestParam("headwearId") Integer headwearId) throws WebServiceException {
        headwearService.use(uid, headwearId);
        return WebServiceMessage.success(null);
    }


    @ApiOperation(value = "赠送头饰", notes = "需要登录ticket和加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "headwearId", value = "赠送头饰ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "targetUid", value = "赠送目标用户ID", dataType = "string", required = true),
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
    @RequestMapping(value = "/give", method = RequestMethod.POST)
    public WebServiceMessage give(@RequestParam("uid") Long uid,
                                  @RequestParam("headwearId") Integer headwearId,
                                  @RequestParam("targetUid") Long targetUid) throws WebServiceException {
        headwearService.give(uid, headwearId, targetUid);
        return WebServiceMessage.success(WebServiceCode.SUCCESS);
    }

}
