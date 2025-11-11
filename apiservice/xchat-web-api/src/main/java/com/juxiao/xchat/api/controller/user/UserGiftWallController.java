package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.Client;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.user.dto.UserGiftWallDTO;
import com.juxiao.xchat.service.api.user.UserGiftWallService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/giftwall")
@Api(tags = "用户信息接口",description = "用户信息接口")
public class UserGiftWallController {
    private final Logger logger = LoggerFactory.getLogger(UserGiftWallController.class);
    @Autowired
    protected UserGiftWallService giftWallService;

    /**
     * @param uid
     * @param orderType
     * @return
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public WebServiceMessage getUserWallListByUid(@RequestParam("uid") Long uid, @RequestParam("orderType") Integer orderType) throws WebServiceException {
        List<UserGiftWallDTO> list = giftWallService.listUserWalls(uid, orderType);
        return WebServiceMessage.success(list);
    }

    @ApiOperation(value = "收到神秘礼物的列表", notes = "需要验证ticket")
    @GetMapping(value = "listMystic")
    @Authorization
    public WebServiceMessage listMystic(@ApiParam(value = "登录用户的uid") @RequestParam("uid") Long uid,
                                        @ApiParam(value = "需要查询的用户的uid") @RequestParam("queryUid") Long queryUid,
                                        @ApiParam(value = "排序类型 1 礼物数量, 2 礼物价格") @RequestParam("orderType") Integer orderType) throws WebServiceException {
        List<UserGiftWallDTO> list = giftWallService.listMystic(queryUid, orderType);
        return WebServiceMessage.success(list);
    }

    @ApiOperation(value = "收到普通礼物的列表", notes = "需要验证ticket", tags = {"礼物墙", "小程序接口"})
    @GetMapping(value = "v2/get")
    @Authorization
    @SignVerification(client = Client.WXAPP)
    public WebServiceMessage getWxUserWall(@ApiParam(value = "登录用户的uid") @RequestParam("uid") Long uid,
                                           @ApiParam(value = "需要查询的用户的uid") @RequestParam("queryUid") Long queryUid,
                                           @ApiParam(value = "排序类型 1 礼物数量, 2 礼物价格") @RequestParam("orderType") Integer orderType,
                                           @ApiParam(value = "ticket") @RequestParam("ticket") String ticket)
            throws WebServiceException {
        //
        List<UserGiftWallDTO> list = giftWallService.listUserWalls(queryUid, orderType);
        return WebServiceMessage.success(list);
    }


    @ApiOperation(value = "收到神秘礼物的列表", notes = "需要验证ticket", tags = {"礼物墙", "小程序接口"})
    @GetMapping(value = "v2/listMystic")
    @Authorization
    @SignVerification(client = Client.WXAPP)
    public WebServiceMessage listWxMystic(@ApiParam(value = "登录用户的uid") @RequestParam("uid") Long uid,
                                          @ApiParam(value = "需要查询的用户的uid") @RequestParam("queryUid") Long queryUid,
                                          @ApiParam(value = "排序类型 1 礼物数量, 2 礼物价格") @RequestParam("orderType") Integer orderType,
                                          @ApiParam(value = "ticket") @RequestParam("ticket") String ticket)
            throws WebServiceException {
        //
        List<UserGiftWallDTO> list = giftWallService.listMystic(queryUid, orderType);
        return WebServiceMessage.success(list);
    }

}
