package com.juxiao.xchat.api.controller.play;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.play.MoraService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chris
 * @Title: 猜拳
 * @date 2019-06-01
 * @time 22:09
 */
@Api(tags = "猜拳接口")
@RestController
@RequestMapping("/play/mora")
public class MoraController {

    @Autowired
    private MoraService moraService;


    /**
     * 获取配置信息
     * @param uid  用户ID
     */
    @ApiOperation(value = "获取配置信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "roomId", value = "赠送目标用户ID", dataType = "long"),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "getState", method = RequestMethod.GET)
    @Authorization
    @SignVerification
    public WebServiceMessage getState(@RequestParam(value = "uid") Long uid,
                                      @RequestParam(value = "roomId") Long roomId)throws WebServiceException {
        return WebServiceMessage.success(moraService.getState(uid,roomId));
    }


    /**
     * 获取房间内未被PK的猜拳消息
     * @param uid  用户ID
     */
    @ApiOperation(value = "获取房间内未被PK的猜拳消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "roomId", value = "赠送目标用户ID", dataType = "long"),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "getMoraRecord", method = RequestMethod.GET)
    @Authorization
    @SignVerification
    public WebServiceMessage getMoraRecord(@RequestParam(value = "uid") Long uid,
                                           @RequestParam(value = "roomId") Long roomId)throws WebServiceException {
        return WebServiceMessage.success(moraService.getMoraRecord(uid,roomId));
    }


    /**
     * 获取猜拳信息
     * @param uid  用户ID
     */
    @ApiOperation(value = "获取猜拳信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "probability", value = "概率(1.高 2.中 3.低) 默认低", dataType = "int", required = true),
            @ApiImplicitParam(name = "roomId", value = "房间ID", dataType = "long"),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "getMoraInfo", method = RequestMethod.GET)
    @Authorization
    @SignVerification
    public WebServiceMessage getMoraInfo(@RequestParam(value = "uid") Long uid,
                                         @RequestParam(value = "roomId") Long roomId,
                                         @RequestParam(value = "probability") Integer probability)throws WebServiceException {
        return WebServiceMessage.success(moraService.getMoraInfo(uid,roomId,probability));
    }


    /**
     * 获取猜拳概率
     * @param uid  用户ID
     */
    @ApiOperation(value = "获取猜拳概率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "roomId", value = "房间ID", dataType = "long"),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "getProbability", method = RequestMethod.GET)
    @Authorization
    @SignVerification
    public WebServiceMessage getProbability(@RequestParam(value = "uid") Long uid,
                                         @RequestParam(value = "roomId") Long roomId)throws WebServiceException {
        return WebServiceMessage.success(moraService.getProbability(uid,roomId));
    }

    /**
     * 确认发起Pk
     * @param uid  用户ID
     */
    @ApiOperation(value = "确认发起Pk接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "roomId", value = "房间ID", dataType = "long"),
            @ApiImplicitParam(name = "probability", value = "概率(1.高 2.中 3.低)", dataType = "int", required = true),
            @ApiImplicitParam(name = "choose", value = "选择(1.剪刀 2.石头 3.布)", dataType = "int", required = true),
            @ApiImplicitParam(name = "giftId", value = "礼物ID", dataType = "int", required = true),
            @ApiImplicitParam(name = "giftNum", value = "礼物数量", dataType = "int", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "confirmPk", method = RequestMethod.POST)
    @Authorization
    @SignVerification
    public WebServiceMessage confirmPk(@RequestParam(value = "uid") Long uid,
                                       @RequestParam(value = "roomId") Long roomId,
                                       @RequestParam(value = "probability") Integer probability,
                                       @RequestParam(value = "choose")Integer choose,
                                       @RequestParam(value = "giftId")Integer giftId,
                                       @RequestParam(value = "giftNum")Integer giftNum)throws WebServiceException {
        return WebServiceMessage.success(moraService.confirmPk(uid,roomId,probability,choose,giftId,giftNum));
    }

    /**
     * 加入参与PK
     * @param uid  用户ID
     */
    @ApiOperation(value = "加入参与PK接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "recordId", value = "发起记录ID", dataType = "int", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "joinPk", method = RequestMethod.POST)
    @Authorization
    @SignVerification
    public WebServiceMessage joinPK(@RequestParam(value = "uid") Long uid,
                                    @RequestParam(value = "recordId") String recordId)throws WebServiceException {
        return WebServiceMessage.success(moraService.joinPk(uid,recordId));
    }

    /**
     * 加入参与PK
     * @param uid  用户ID
     */
    @ApiOperation(value = "确认加入PK接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "recordId", value = "发起记录ID", dataType = "int", required = true),
            @ApiImplicitParam(name = "choose", value = "选择(1.剪刀 2.石头 3.布)", dataType = "int", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "confirmJoinPk", method = RequestMethod.POST)
    @Authorization
    @SignVerification
    public WebServiceMessage confirmJoinPk(@RequestParam(value = "uid") Long uid,
                                           @RequestParam(value = "recordId") String recordId,
                                           @RequestParam(value = "choose") Integer choose)throws WebServiceException {
        moraService.confirmJoinPk(uid,recordId,choose);
        return WebServiceMessage.success(null);
    }


    /**
     * 记录
     * @param uid  用户ID
     */
    @ApiOperation(value = "猜拳记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密", dataType = "string", required = true),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "string", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "record", method = RequestMethod.GET)
    @Authorization
    @SignVerification
    public WebServiceMessage getRecord(@RequestParam(value = "uid") Long uid,
                                       @RequestParam(value = "current")Integer current,
                                       @RequestParam(value = "pageSize")Integer pageSize)throws WebServiceException {
        return WebServiceMessage.success(moraService.getMoraRecord(uid,current,pageSize));
    }
}
