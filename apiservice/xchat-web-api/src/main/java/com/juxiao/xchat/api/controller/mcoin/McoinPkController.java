package com.juxiao.xchat.api.controller.mcoin;

import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.common.mcoin.McoinPkManager;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mcoin/pk")
@Api(tags = "点点币PK活动接口")
public class McoinPkController {


    @Autowired
    private McoinPkManager mcoinPkManager;

    /**
     * 获取本期萌币PK信息接口
     * @return
     * @throws WebServiceException
     */
    @ApiOperation(value = "获取本期点点币PK活动信息接口", notes = "获取本期点点币PK活动信息接口", tags = {"点点币PK活动接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户id", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "用户登录凭证", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success")
    })
    @Authorization
    @RequestMapping(value = "/getPkInfo", method = RequestMethod.GET)
    public WebServiceMessage getPkInfo(@RequestParam("uid") @ApiParam(value = "用户ID") Long uid,
                                       @RequestParam("ticket") @ApiParam(value = "登录凭证") String ticket) throws WebServiceException {

        JSONObject pkInfo = mcoinPkManager.getPkInfo(uid);

        return WebServiceMessage.success(pkInfo);
    }

    /**
     *点击支持PK接口
     *
     */
    @ApiOperation(value = "点击支持PK接口", notes = "点击支持PK接口", tags = {"萌币PK活动接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户id", dataType = "long", required = true),
            @ApiImplicitParam(name = "supportType", value = "支持队伍类别（1、为red队 2、为blue队）", dataType = "int", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success")
    })
    @Authorization
    @RequestMapping(value = "/supportPk", method = RequestMethod.POST)
    public WebServiceMessage supportPk(Long uid, Integer supportType) throws WebServiceException {

        mcoinPkManager.supportPk(uid,supportType);

        return WebServiceMessage.success(null);
    }

    /**
     *PK排行榜接口
     *
     */
    @ApiOperation(value = "PK排行榜接口", notes = "PK排行榜接口", tags = {"萌币PK活动接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户id", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "用户登录凭证", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success")
    })
    @Authorization
    @RequestMapping(value = "/rankingList", method = RequestMethod.GET)
    public WebServiceMessage rankingList(@RequestParam("uid") @ApiParam(value = "用户ID") Long uid,
                                         @RequestParam("ticket") @ApiParam(value = "登录凭证") String ticket) throws WebServiceException {

        JSONObject object = mcoinPkManager.rankingList(uid);
        return WebServiceMessage.success(object);
    }

    /**
     *PK往期回顾
     *
     */
    @ApiOperation(value = "PK往期回顾接口", notes = "PK往期回顾接口", tags = {"萌币PK活动接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户id", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "用户登录凭证", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success")
    })
    @Authorization
    @RequestMapping(value = "/pastPeriod", method = RequestMethod.GET)
    public WebServiceMessage pastPeriod(@RequestParam("uid") @ApiParam(value = "用户ID") Long uid,
                                        @RequestParam("ticket") @ApiParam(value = "登录凭证") String ticket) throws WebServiceException {

        JSONObject object = mcoinPkManager.pastPeriod(uid);
        return WebServiceMessage.success(object);
    }

    @ApiOperation(value = "发送MQ延迟消息", notes = "发送MQ延迟消息", tags = {"发送MQ延迟消息接口"})
    @GetMapping("sendDelayQueue")
    public String eventSendDelayQueueMessage(){
        mcoinPkManager.eventSendDelayQueueMessage();
        return "success";
    }
}
