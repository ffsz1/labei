package com.erban.web.controller;

/**
 * Created by liuguofu on 2017/5/26.
 */

import com.erban.main.service.record.AuctionRivalRecordService;
import com.erban.main.vo.AuctionCurVo;
import com.erban.main.vo.AuctionRivalRecordVo;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/auctrival")
public class AuctionRivalRecordController {

    private static final Logger logger = LoggerFactory.getLogger(AuctionRivalRecordController.class);
    @Autowired
    private AuctionRivalRecordService auctionRivalRecordService;

    @RequestMapping(value = "up",method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    public BusiResult saveOrUpdateAuctionRival(Long uid,Long roomUid,String auctId,String type,Long money,String ticket){
        if(auctId==null||uid==null||money==null||roomUid==null){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL,"参数异常");
        }
        BusiResult<AuctionCurVo> startAuctionResult=null;
        try {
            startAuctionResult= auctionRivalRecordService.saveOrUpdateAuctionRivalRecord( uid, roomUid, auctId, money);
        } catch (Exception e) {
            logger.error("openRoom error..auctId="+auctId,e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return startAuctionResult;
    }


    @RequestMapping(value = "list",method = RequestMethod.POST)
    @ResponseBody
    public BusiResult getAuctRivalList(Long uid){
        if(uid==null){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL,"参数异常");
        }
        BusiResult<List<AuctionRivalRecordVo>> startAuctionResult=null;
        try {
            startAuctionResult= auctionRivalRecordService.getRivalRecordListByUid(uid);
        } catch (Exception e) {
            logger.error("get error..uid="+uid,e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return startAuctionResult;
    }


}
