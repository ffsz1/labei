package com.juxiao.xchat.api.controller.mcoin;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.mcoin.dto.McoinDrawInvolvedUserDTO;
import com.juxiao.xchat.dao.mcoin.dto.McoinDrawIssuesDTO;
import com.juxiao.xchat.dao.mcoin.dto.McoinUserDrawRecordDTO;
import com.juxiao.xchat.dao.mcoin.dto.McoinUserHistoryRecordDTO;
import com.juxiao.xchat.service.api.mcoin.McoinDrawService;
import com.juxiao.xchat.service.api.mcoin.vo.McoinDrawIssueDetailVO;
import com.juxiao.xchat.service.api.mcoin.vo.McoinDrawResultVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/mcoin/draw")
@Api(tags = "点点币接口")
public class McoinDrawController {
    @Autowired
    private McoinDrawService mcoinDrawService;

    @ApiOperation(value = "1.4 参与点点币抽奖", notes = "用户在《点点币抽奖-物品页面》参与抽奖活动时调用")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success")
    })
    @Authorization
    @RequestMapping(value = "v1/saveUserDrawTicket", method = RequestMethod.POST)
    public WebServiceMessage saveUserDrawTicket(@RequestParam("uid") @ApiParam(value = "用户ID") Long uid,
                                                @RequestParam("issueId") @ApiParam(value = "期号ID") Long issueId,
                                                @RequestParam(value = "drawNum", required = false, defaultValue = "1") @ApiParam(value = "抽奖次数，默认为1") int drawNum) throws WebServiceException {
        Long recordId = mcoinDrawService.saveUserMcoinDrawTicket(uid, issueId, drawNum);
        Map<String, Object> result = new HashMap<>(1);
        result.put("recordId", recordId);
        return WebServiceMessage.success(result);
    }

    @ApiOperation(value = "1.1 点点币有效抽奖期列表", notes = "查询所有在管理后台中配置有效的点点币抽奖列表信息，主要用户《点点币抽奖》入口列表查询。")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = McoinDrawIssuesDTO.class)
    })
    @Authorization
    @RequestMapping(value = "v1/listIssues", method = RequestMethod.GET)
    public WebServiceMessage listIssues(@RequestParam("uid") @ApiParam(value = "用户ID") Long uid,
                                        @RequestParam("ticket") @ApiParam(value = "登录凭证") String ticket) throws WebServiceException {
        List<McoinDrawIssuesDTO> list = mcoinDrawService.listIssues();
        return WebServiceMessage.success(list);
    }

    @ApiOperation(value = "1.2 查询点点币抽奖期号详细信息", notes = "根据抽奖期号查询该次抽奖的详细信息，主要用于《点点币抽奖-物品页面》查询。")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = McoinDrawIssueDetailVO.class)
    })
    @Authorization
    @RequestMapping(value = "v1/getIssueDetail", method = RequestMethod.GET)
    public WebServiceMessage getIssueDetail(@RequestParam("issueId") @ApiParam(value = "期号ID") Long issueId,
                                            @RequestParam("uid") @ApiParam(value = "用户ID") Long uid,
                                            @RequestParam("ticket") @ApiParam(value = "登录凭证") String ticket) throws WebServiceException {
        McoinDrawIssueDetailVO detailVo = mcoinDrawService.getIssueDetail(issueId);
        return WebServiceMessage.success(detailVo);
    }

    @ApiOperation(value = "1.3 本期参与记录列表", notes = "在《萌币抽奖-物品页面-本期参与记录》页面中进行查询。")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = McoinDrawInvolvedUserDTO.class)
    })
    @Authorization
    @RequestMapping(value = "v1/listIssueRecords", method = RequestMethod.GET)
    public WebServiceMessage listIssueRecords(@RequestParam("issueId") @ApiParam(value = "期号ID") Long issueId,
                                              @RequestParam("uid") @ApiParam(value = "用户ID") Long uid,
                                              @RequestParam("ticket") @ApiParam(value = "登录凭证") String ticket,
                                              @RequestParam(value = "pageNum", required = false, defaultValue = "1") @ApiParam(value = "当前页码") Integer pageNum) throws WebServiceException {
        List<McoinDrawInvolvedUserDTO> list = mcoinDrawService.listIssueRecords(issueId, pageNum);
        return WebServiceMessage.success(list);
    }

    @ApiOperation(value = "1.5 用户参与记录列表", notes = "在《萌币抽奖-记录-已中奖》页面中进行查询。")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = McoinUserDrawRecordDTO.class)
    })
    @Authorization
    @RequestMapping(value = "v1/listInvolveRecords", method = RequestMethod.GET)
    public WebServiceMessage listInvolveRecords(@RequestParam("uid") @ApiParam(value = "用户ID") Long uid,
                                                @RequestParam("ticket") @ApiParam(value = "登录凭证") String ticket,
                                                @RequestParam("type") @ApiParam(value = "类型：1，进行中；2，已开奖；3，已中奖") Byte type,
                                                @RequestParam(value = "pageNum", required = false, defaultValue = "1") @ApiParam(value = "当前页码") Integer pageNum) throws WebServiceException {
        List<McoinUserDrawRecordDTO> list = mcoinDrawService.listInvolveRecords(uid, type, pageNum);
        return WebServiceMessage.success(list);
    }

    @ApiOperation(value = "1.6 订单详情接口", notes = "用户打开《萌币抽奖-记录-开奖记录（未中奖/已中奖）》调用查询接口列表")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = McoinDrawResultVO.class)
    })
    @Authorization
    @RequestMapping(value = "v1/getResult", method = RequestMethod.GET)
    public WebServiceMessage getResult(@RequestParam("recordId") @ApiParam(value = "记录ID") Long recordId,
                                       @RequestParam("uid") @ApiParam(value = "用户ID") Long uid,
                                       @RequestParam("ticket") @ApiParam(value = "登录凭证") String ticket) throws WebServiceException {
        McoinDrawResultVO resultVo = mcoinDrawService.getDrawResult(uid, recordId);
        return WebServiceMessage.success(resultVo);
    }


    @ApiOperation(value = "1.7 查询参与记录中的关联抽奖号码", notes = "用户在萌币抽奖-记录中查看我的抽奖号码")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = McoinDrawResultVO.class)
    })
    @Authorization
    @RequestMapping(value = "v1/listRecordTickets", method = RequestMethod.GET)
    public WebServiceMessage listRecordTickets(@RequestParam("recordId") @ApiParam(value = "期号ID") Long recordId,
                                               @RequestParam("uid") @ApiParam(value = "用户ID") Long uid,
                                               @RequestParam("ticket") @ApiParam(value = "登录凭证") String ticket) throws WebServiceException {
        List<String> list = mcoinDrawService.listRecordTickets(uid, recordId);
        Map<String, Object> tickets = Maps.newHashMap();
        tickets.put("tickets", list);
        return WebServiceMessage.success(tickets);
    }

    @ApiOperation(value = "查询往期得奖名单")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = McoinDrawResultVO.class)
    })
    @Authorization
    @RequestMapping(value = "v1/listIssueHistroyRecords", method = RequestMethod.GET)
    public WebServiceMessage listIssueHistroyRecords(@RequestParam("uid") @ApiParam(value = "用户ID") Long uid,
                                                     @RequestParam("ticket") @ApiParam(value = "登录凭证") String ticket,
                                                     @RequestParam(value = "pageNum", required = false, defaultValue = "1") @ApiParam(value = "当前页码") Integer pageNum) throws WebServiceException {
        List<McoinUserHistoryRecordDTO> list = mcoinDrawService.listIssueHistroyRecords(pageNum);
        return WebServiceMessage.success(list);
    }

    @ApiOperation(value = "1.8.萌币消息小红点接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @RequestMapping(value = "v1/getMcoinMessageCount", method = RequestMethod.POST)
    public WebServiceMessage getMcoinMessageCount(@RequestParam("uid") @ApiParam(value = "用户ID") Long uid,
                                                  @RequestParam("ticket") @ApiParam(value = "登录凭证") String ticket) throws WebServiceException {
        int messageCount = mcoinDrawService.getMcoinMessageCount(uid);
        JSONObject object = new JSONObject();
        object.put("messageCount", messageCount);
        return WebServiceMessage.success(object);
    }
}
