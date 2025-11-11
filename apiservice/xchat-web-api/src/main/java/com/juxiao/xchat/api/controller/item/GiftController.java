package com.juxiao.xchat.api.controller.item;

import com.google.common.util.concurrent.RateLimiter;
import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.Client;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.constant.SendGiftType;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.common.item.vo.GiftSendVO;
import com.juxiao.xchat.service.api.item.GiftSendService;
import com.juxiao.xchat.service.api.item.GiftService;
import com.juxiao.xchat.service.common.sysconf.AppVersionService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/gift")
@Api(tags = "礼物接口")
public class GiftController {
    private static final Logger logger = LoggerFactory.getLogger(GiftController.class);
    private final RateLimiter sendAllLimiter = RateLimiter.create(1500.0); // 1秒1500并发
    private final RateLimiter sendOneLimiter = RateLimiter.create(2000.0); // 1秒2000并发
    @Autowired
    private AppVersionService versionService;
    @Autowired
    private GiftService giftService;
    @Autowired
    private GiftSendService giftSendService;
//    @Autowired
//    private NationalDayService nationalDayService;

    @ApiOperation(value = "给单个用户打call", notes = "需要ticket、加密验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "targetUid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "roomUid", value = "用户所在房间UID", dataType = "long"),
            @ApiImplicitParam(name = "giftId", value = "赠送的礼物ID", dataType = "int", required = true),
            @ApiImplicitParam(name = "giftNum", value = "赠送的礼物数量", dataType = "int", required = true),
            @ApiImplicitParam(name = "type", value = "赠送礼物类型：1，赠送给房主；2，私聊发礼物；3，房间内赠送给个人", dataType = "byte",
                    required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true),
            @ApiImplicitParam(name = "expressMessage", value = "表白礼物留言", dataType = "string")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = GiftSendVO.class)
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/callForUser", method = RequestMethod.POST)
    public WebServiceMessage callForUser(@RequestParam("uid") long uid,
                                         @RequestParam("targetUid") long targetUid,
                                         @RequestParam(value = "roomUid", required = false) Long roomUid,
                                         @RequestParam("giftId") int giftId,
                                         @RequestParam("giftNum") int giftNum,
                                         @RequestParam("type") byte type,
                                         @RequestParam("appVersion") String appVersion,
                                         @RequestParam(value = "expressMessage", required = false) String expressMessage) throws WebServiceException {
        WebServiceMessage message;
        if (uid == 0 || targetUid == 0 || giftNum <= 0 || giftNum > 9999999) {
            message = WebServiceMessage.failure(WebServiceCode.PARAMETER_ILLEGAL);
        } else if (!sendOneLimiter.tryAcquire(5, TimeUnit.SECONDS)) {
            message = WebServiceMessage.failure(WebServiceCode.SERVER_BUSY);
        } else {
            message = giftSendService.callForUserWithSendGift(uid, targetUid, roomUid, giftId, giftNum, type,
                    appVersion);
        }

        return message;
    }

    @ApiOperation(value = "给单个用户送礼物", notes = "需要ticket、加密验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "targetUid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "roomUid", value = "用户所在房间UID", dataType = "long"),
            @ApiImplicitParam(name = "giftId", value = "赠送的礼物ID", dataType = "int", required = true),
            @ApiImplicitParam(name = "giftNum", value = "赠送的礼物数量", dataType = "int", required = true),
            @ApiImplicitParam(name = "type", value = "赠送礼物类型: 1.赠送给房主; 2.私聊发礼物; 3.房间内赠送给个人", dataType = "byte", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true),
            @ApiImplicitParam(name = "expressMessage", value = "表白礼物留言", dataType = "string")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = GiftSendVO.class)
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/sendV3", method = RequestMethod.POST)
    public WebServiceMessage sendGift(@RequestParam("uid") long uid,
                                      @RequestParam("targetUid") long targetUid,
                                      @RequestParam(value = "roomUid", required = false) Long roomUid,
                                      @RequestParam("giftId") int giftId,
                                      @RequestParam("giftNum") int giftNum,
                                      @RequestParam("type") byte type,
                                      @RequestParam("appVersion") String appVersion,
                                      @RequestParam(value = "expressMessage", required = false) String expressMessage) throws WebServiceException {
        WebServiceMessage message;
        if (uid == 0 || targetUid == 0 || giftNum <= 0 || giftNum > 9999999 || uid == targetUid) {
            message = WebServiceMessage.failure(WebServiceCode.PARAMETER_ILLEGAL);
        } else if (!sendOneLimiter.tryAcquire(5, TimeUnit.SECONDS)) {
            message = WebServiceMessage.failure(WebServiceCode.SERVER_BUSY);
        } else if (SendGiftType.person == type) {
            message = giftSendService.sendGiftInPrivateChat(uid, targetUid, roomUid, giftId, giftNum, type);
            if (message.getCode() == 200) {
                // GiftSendVO giftSendVo = (GiftSendVO) message.getData();
                // if (giftSendVo != null) {
                //     if (giftSendVo.getUseGiftPurseGold().intValue() > 0) {
                //         nationalDayService.NationalDayGiveGifts(uid, roomUid, giftNum);
                //     }
                // }
            }
        } else if (SendGiftType.express == type) {
            message = giftSendService.sendExpressGift(uid, targetUid, roomUid, giftId, giftNum, type, expressMessage);
        } else {
            message = giftSendService.sendGiftInRoomChat(uid, targetUid, roomUid, giftId, giftNum, type, appVersion);
            if (message.getCode() == 200) {
                try {
                    // GiftSendVO giftSendVo = (GiftSendVO) message.getData();
                    // if (giftSendVo != null) {
                    //     if (giftSendVo.getUseGiftPurseGold().intValue() > 0) {
                    //         nationalDayService.NationalDayGiveGifts(uid, roomUid, giftNum);
                    //     }
                    // }
                    giftSendService.sendMsgAllRoom(uid, targetUid, roomUid, giftId, giftNum, null);
                } catch (Exception e) {
                    logger.error("[ 发送全房间礼物失败 ] causeBy:" + e.getMessage(), e);
                }
            }
        }
        return message;
    }

    @ApiOperation(value = "给单个用户送活动礼物", notes = "需要ticket、加密验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "targetUid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "roomUid", value = "用户所在房间UID", dataType = "long"),
            @ApiImplicitParam(name = "giftId", value = "赠送的礼物ID", dataType = "int", required = true),
            @ApiImplicitParam(name = "giftNum", value = "赠送的礼物数量", dataType = "int", required = true),
            @ApiImplicitParam(name = "type", value = "赠送礼物类型：1，赠送给房主；2，私聊发礼物；3，房间内赠送给个人", dataType = "byte",
                    required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true),
            @ApiImplicitParam(name = "expressMessage", value = "表白礼物留言", dataType = "string")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = GiftSendVO.class)
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/sendProp", method = RequestMethod.POST)
    public WebServiceMessage sendProp(@RequestParam("uid") long uid,
                                      @RequestParam("targetUid") long targetUid,
                                      @RequestParam(value = "roomUid", required = false) Long roomUid,
                                      @RequestParam("giftId") int giftId,
                                      @RequestParam("giftNum") int giftNum,
                                      @RequestParam("type") byte type,
                                      @RequestParam("appVersion") String appVersion,
                                      @RequestParam(value = "expressMessage", required = false) String expressMessage) throws WebServiceException {
        WebServiceMessage message;
        if (uid == 0 || targetUid == 0 || giftNum <= 0 || giftNum > 9999999 || uid == targetUid) {
            message = WebServiceMessage.failure(WebServiceCode.PARAMETER_ILLEGAL);
        } else if (!sendOneLimiter.tryAcquire(5, TimeUnit.SECONDS)) {
            message = WebServiceMessage.failure(WebServiceCode.SERVER_BUSY);
        } else if (SendGiftType.person == type) {// 私聊送
            message = giftSendService.sendGiftPropInPrivateChat(uid, targetUid, roomUid, giftId, giftNum, type);
        } else { // 房间送
            message = giftSendService.sendGiftPropInRoomChat(uid, targetUid, roomUid, giftId, giftNum, type,
                    appVersion);
        }
        return message;
    }


    @ApiOperation(value = "小程序给单个用户送礼物", notes = "需要ticket、加密验证", tags = {"礼物接口", "小程序接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "targetUid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "roomUid", value = "用户所在房间UID", dataType = "long"),
            @ApiImplicitParam(name = "giftId", value = "赠送的礼物ID", dataType = "int", required = true),
            @ApiImplicitParam(name = "giftNum", value = "赠送的礼物数量", dataType = "int", required = true),
            @ApiImplicitParam(name = "type", value = "赠送礼物类型：1，赠送给房主；2，私聊发礼物；3，房间内赠送给个人", dataType = "byte",
                    required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = GiftSendVO.class)
    })
    @Authorization
    @SignVerification(client = Client.WXAPP)
    @RequestMapping(value = "/v4/send", method = RequestMethod.POST)
    public WebServiceMessage sendGiftV4(@RequestParam("uid") long uid,
                                        @RequestParam("targetUid") long targetUid,
                                        @RequestParam(value = "roomUid", required = false) Long roomUid,
                                        @RequestParam("giftId") int giftId,
                                        @RequestParam("giftNum") int giftNum,
                                        @RequestParam("type") byte type,
                                        @RequestParam("appVersion") String appVersion) throws WebServiceException {
        return this.sendGift(uid, targetUid, roomUid, giftId, giftNum, type, appVersion, "");

    }

    @ApiOperation(value = "给全麦送礼物", notes = "需要ticket、加密验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "targetUids", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "roomUid", value = "用户所在房间UID", dataType = "long", required = true),
            @ApiImplicitParam(name = "giftId", value = "赠送的礼物ID", dataType = "int", required = true),
            @ApiImplicitParam(name = "giftNum", value = "赠送的礼物数量", dataType = "int", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = GiftSendVO.class)
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/sendWholeMicroV3", method = RequestMethod.POST)
    public WebServiceMessage sendWholeMicro(@RequestParam(value = "uid") long uid,
                                            @RequestParam(value = "targetUids") Long[] targetUids,
                                            @RequestParam(value = "roomUid") Long roomUid,
                                            @RequestParam(value = "giftId") int giftId,
                                            @RequestParam(value = "giftNum") int giftNum,
                                            @RequestParam(value = "appVersion") String appVersion) throws WebServiceException {
        WebServiceMessage webServiceMessage;
        if (giftNum <= 0 || giftNum > 9999 || targetUids == null || targetUids.length < 1) {
            return WebServiceMessage.failure(WebServiceCode.PARAMETER_ILLEGAL);
        }
        // 对接口限流
        if (!sendAllLimiter.tryAcquire(5, TimeUnit.SECONDS)) {
            return WebServiceMessage.failure(WebServiceCode.SERVER_BUSY);
        }
        webServiceMessage = giftSendService.sendGiftToAll(uid, targetUids, roomUid, giftId, giftNum, appVersion);
        try {
            if (webServiceMessage.getCode() == 200) {
                giftSendService.sendMsgAllRoom(uid, null, roomUid, giftId, giftNum, targetUids);
            }
        } catch (Exception e) {
            logger.error("[ 发送全房间礼物失败 ] causeBy:" + e.getMessage(), e);
        }
        return webServiceMessage;
    }

    @ApiOperation(value = "给全麦送礼物", notes = "需要ticket、加密验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "targetUids", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "roomUid", value = "用户所在房间UID", dataType = "long", required = true),
            @ApiImplicitParam(name = "giftId", value = "赠送的礼物ID", dataType = "int", required = true),
            @ApiImplicitParam(name = "giftNum", value = "赠送的礼物数量", dataType = "int", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = GiftSendVO.class)
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/sendPropWholeMicro", method = RequestMethod.POST)
    public WebServiceMessage sendPropWholeMicro(@RequestParam(value = "uid") long uid,
                                                @RequestParam(value = "targetUids") Long[] targetUids,
                                                @RequestParam(value = "roomUid") Long roomUid,
                                                @RequestParam(value = "giftId") int giftId,
                                                @RequestParam(value = "giftNum") int giftNum,
                                                @RequestParam(value = "appVersion") String appVersion) throws WebServiceException {
        WebServiceMessage webServiceMessage;
        if (giftNum <= 0 || giftNum > 9999999 || targetUids == null || targetUids.length < 1) {
            return WebServiceMessage.failure(WebServiceCode.PARAMETER_ILLEGAL);
        }
        // 对接口限流
        if (!sendAllLimiter.tryAcquire(5, TimeUnit.SECONDS)) {
            return WebServiceMessage.failure(WebServiceCode.SERVER_BUSY);
        }
        return giftSendService.sendGiftPropToAll(uid, targetUids, roomUid, giftId, giftNum, appVersion);
    }

    @ApiOperation(value = "小程序给全麦送礼物", notes = "需要ticket、加密验证", tags = {"礼物接口", "小程序接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "targetUids", value = "赠送全麦的用户UID", dataType = "long", required = true),
            @ApiImplicitParam(name = "roomUid", value = "用户所在房间UID", dataType = "long", required = true),
            @ApiImplicitParam(name = "giftId", value = "赠送的礼物ID", dataType = "int", required = true),
            @ApiImplicitParam(name = "giftNum", value = "赠送的礼物数量", dataType = "int", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = GiftSendVO.class)
    })
    @Authorization
    @SignVerification(client = Client.WXAPP)
    @RequestMapping(value = "/v4/sendWholeMicro", method = RequestMethod.POST)
    public WebServiceMessage sendWholeMicroV4(@RequestParam(value = "uid") long uid,
                                              @RequestParam(value = "targetUids") Long[] targetUids,
                                              @RequestParam(value = "roomUid") Long roomUid,
                                              @RequestParam(value = "giftId") int giftId,
                                              @RequestParam(value = "giftNum") int giftNum,
                                              @RequestParam(value = "appVersion") String appVersion) throws WebServiceException {
        return this.sendWholeMicro(uid, targetUids, roomUid, giftId, giftNum, appVersion);
    }

    @ApiOperation(value = "获取礼物列表,用户礼物列表", notes = "需要加密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = HashMap.class)
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "listV3", method = RequestMethod.GET)
    public WebServiceMessage listV3(@RequestParam(value = "uid", required = false) Long uid) {
        return WebServiceMessage.success(giftService.listGifts(uid, null));
    }

    @ApiOperation(value = "小程序获取礼物列表,带有礼物版本号和用户礼物列表", notes = "需要ticket和加密", tags = {"礼物接口", "小程序接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success")
    })
    @SignVerification(client = Client.WXAPP)
    @RequestMapping(value = "v3/list", method = RequestMethod.GET)
    public WebServiceMessage listGiftsV3(@RequestParam(value = "uid", required = false) Long uid) {
        return WebServiceMessage.success("成功");
    }
}
