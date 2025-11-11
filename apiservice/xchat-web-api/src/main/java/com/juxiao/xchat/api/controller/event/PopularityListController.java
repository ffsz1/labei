package com.juxiao.xchat.api.controller.event;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.event.PopularityListService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/popularity/list")
public class PopularityListController {
    @Resource
    private PopularityListService popularityListService;

    @ApiOperation(value = "获取本周人气榜前二十名", notes = "需要登录ticket和加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @RequestMapping(value = "/getTop20List", method = RequestMethod.GET)
    public WebServiceMessage getTop20List() throws WebServiceException {
        return WebServiceMessage.success(popularityListService.getTop20List());
    }

    @ApiOperation(value = "获取上周人气榜男神女神榜前三名", notes = "需要登录ticket和加密")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @RequestMapping(value = "/getLastWeekList", method = RequestMethod.GET)
    public WebServiceMessage getLastWeekList() throws WebServiceException {
        return WebServiceMessage.success(popularityListService.getLastWeekRank());
    }

    @ApiOperation(value = "获取某个用户的星推官", notes = "需要登录ticket和加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @RequestMapping(value = "/getUserRecommend", method = RequestMethod.GET)
    public WebServiceMessage getUserRecommend(@RequestParam("userId") Long userId) throws WebServiceException {
        return WebServiceMessage.success(popularityListService.getUserRecommend(userId));
    }

    @ApiOperation(value = "获取某个用户本周的人气排名和收到/发出的人气票数", notes = "需要登录ticket和加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @RequestMapping(value = "/getMyRank", method = RequestMethod.GET)
    public WebServiceMessage getMyRank(@RequestParam("uid") Long uid) throws WebServiceException {
        return WebServiceMessage.success(popularityListService.getMyRank(uid));
    }
}
