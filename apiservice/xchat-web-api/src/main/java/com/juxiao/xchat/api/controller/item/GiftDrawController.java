package com.juxiao.xchat.api.controller.item;

import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.bill.dto.BillGiftDrawDTO;
import com.juxiao.xchat.manager.mq.ActiveMqManager;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import com.juxiao.xchat.service.api.item.vo.GiftVO;
import com.juxiao.xchat.service.api.user.UserGiftPurseService;
import com.juxiao.xchat.service.api.user.vo.DrawConchVO;
import com.juxiao.xchat.service.api.user.vo.DrawGiftVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/giftPurse")
@Api(tags = "捡海螺接口")
public class GiftDrawController {
    @Autowired
    private UserGiftPurseService giftPurseService;

    @Autowired
    private ActiveMqManager activeMqManager;

    @ApiOperation(value = "捡海螺接口, 返回礼物图片等信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "type", value = "捡海螺类型：1，砸1次；2，砸10次", dataType = "int", required = true),
            @ApiImplicitParam(name = "roomId", value = "赠送目标用户ID", dataType = "long"),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = DrawConchVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "v2/draw", method = RequestMethod.POST)
    public WebServiceMessage doDrawV2(@RequestParam("uid") Long uid,
                                      @RequestParam("type") Integer type,
                                      @RequestParam(value = "roomId", required = false) Long roomId) throws WebServiceException {
        DrawConchVO drawConchVO = giftPurseService.doDraw(uid, type, roomId);
        JSONObject object = new JSONObject();
        object.put("uid", uid);
        activeMqManager.sendQueueMessage(MqDestinationKey.USER_DRAW_GIFT, object.toJSONString());
        return WebServiceMessage.success(drawConchVO);
    }


    @ApiOperation(value = "捡海螺接口, 返回礼物图片等信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "type", value = "捡海螺类型：1，砸1次；2，砸10次", dataType = "int", required = true),
            @ApiImplicitParam(name = "roomId", value = "赠送目标用户ID", dataType = "long"),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = DrawConchVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "v2/xqDraw", method = RequestMethod.POST)
    public WebServiceMessage xqDraw(@RequestParam("uid") Long uid,
                                    @RequestParam("type") Integer type,
                                    @RequestParam(value = "roomId", required = false) Long roomId) throws WebServiceException {
        DrawConchVO drawConchVO = giftPurseService.xqDraw(uid, type, roomId);
        JSONObject object = new JSONObject();
        object.put("uid", uid);
        activeMqManager.sendQueueMessage(MqDestinationKey.USER_DRAW_GIFT, object.toJSONString());
        return WebServiceMessage.success(drawConchVO);
    }

    @ApiOperation(value = "捡海螺接口, 返回礼物图片等信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "type", value = "捡海螺类型：1，砸1次；2，砸10次", dataType = "int", required = true),
            @ApiImplicitParam(name = "roomId", value = "赠送目标用户ID", dataType = "long"),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = DrawConchVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "v2/hdDraw", method = RequestMethod.POST)
    public WebServiceMessage hdDraw(@RequestParam("uid") Long uid,
                                    @RequestParam("type") Integer type,
                                    @RequestParam(value = "roomId", required = false) Long roomId) throws WebServiceException {
        DrawConchVO drawConchVO = giftPurseService.hdDraw(uid, type, roomId);
        JSONObject object = new JSONObject();
        object.put("uid", uid);
        activeMqManager.sendQueueMessage(MqDestinationKey.USER_DRAW_GIFT, object.toJSONString());
        return WebServiceMessage.success(drawConchVO);
    }

    @ApiOperation(value = "捡海螺接口, 返回礼物图片等信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "type", value = "捡海螺类型：1，砸1次；2，砸10次", dataType = "int", required = true),
            @ApiImplicitParam(name = "roomId", value = "赠送目标用户ID", dataType = "long"),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = DrawConchVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "dy/draw", method = RequestMethod.POST)
    public WebServiceMessage dyDraw(@RequestParam("uid") Long uid,
                                    @RequestParam("type") Integer type,
                                    @RequestParam(value = "roomId", required = false) Long roomId) throws WebServiceException {
        List<DrawGiftVO> list = giftPurseService.dyDraw(uid, type, roomId);
        JSONObject object = new JSONObject();
        object.put("uid", uid);
        activeMqManager.sendQueueMessage(MqDestinationKey.USER_DRAW_GIFT, object.toJSONString());
        return WebServiceMessage.success(list);
    }

    /**
     * 捡海螺排行榜
     *
     * @param type
     * @return
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "/getRank", method = RequestMethod.GET)
    public WebServiceMessage getRank(Integer type,
                                     @RequestParam(value = "giftType", required = false) Integer giftType) {
        List<?> list = giftPurseService.getRank(type, giftType);
        return WebServiceMessage.success(list);
    }

    /**
     * 查询用户捡海螺记录
     *
     * @param uid      用户ID
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取捡海螺记录", notes = "需要验证ticket")
    @GetMapping(value = "record")
    @Authorization
    @SignVerification
    public WebServiceMessage record(Long uid, Integer pageNum, Integer pageSize,
                                    @RequestParam(value = "giftType", required = false) Integer giftType) {
        List<BillGiftDrawDTO> list = giftPurseService.record(uid, giftType, pageNum, pageSize);
        return WebServiceMessage.success(list);
    }

    /**
     * 获取奖池礼物
     *
     * @param uid 用户ID
     * @return
     */
    @ApiOperation(value = "获取奖池礼物", notes = "需要验证ticket")
    @GetMapping(value = "getPrizePoolGift")
    @Authorization
    @SignVerification
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = GiftVO.class)
    })
    public WebServiceMessage getPrizePoolGift(Long uid,
                                              @RequestParam(value = "giftType", required = false) Integer giftType) {
        List<GiftVO> list = giftPurseService.getPrizePoolGift(uid, giftType);
        return WebServiceMessage.success(list);
    }


}
