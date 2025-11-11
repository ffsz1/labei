package com.juxiao.xchat.api.controller.event;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.event.WeekStarGiftService;
import com.juxiao.xchat.service.api.event.vo.WeekStarRankVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chris
 * @Title: 周星榜
 * @date 2019-05-20 16:22
 */
@RestController
@RequestMapping("/week/star")
public class WeekStarController {
    @Resource
    private WeekStarGiftService weekStarGiftService;

    @ApiOperation(value = "获取本周礼物,周星奖励,礼物预告", notes = "需要登录ticket和加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @RequestMapping(value = "/getWeekStarGift", method = RequestMethod.GET)
    public WebServiceMessage getWeekStarGift(@RequestParam("uid") Long uid) throws WebServiceException {
        return WebServiceMessage.success(weekStarGiftService.getWeekStarGift());
    }

    @ApiOperation(value = "获取周星榜排名", notes = "需要登录ticket和加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "giftId", value = "礼物Id", dataType = "string", required = true),
            @ApiImplicitParam(name = "type", value = "类型(1.本周排名 2.上周排名)", dataType = "string", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @RequestMapping(value = "/getStartList", method = RequestMethod.GET)
    public WebServiceMessage getStartList(@RequestParam("uid") Long uid,
                                          @RequestParam("giftId") Integer giftId,
                                          @RequestParam("type") Integer type) throws WebServiceException {
        if (type != 1 && type != 2) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        WeekStarRankVO weekStarRankVO = new WeekStarRankVO();
        if (type == 1) {
            weekStarRankVO.setWeekStartVO(weekStarGiftService.getWeekStartRank(giftId));
        } else {
            weekStarRankVO.setWeekStartVO(weekStarGiftService.getLastWeekStarList(giftId));
        }
        weekStarRankVO.setMyWeekStartVO(weekStarGiftService.getMyWeekStar(uid, giftId));
        return WebServiceMessage.success(weekStarRankVO);
    }
}
