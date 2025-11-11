package com.erban.web.controller.room;

import com.alibaba.fastjson.JSON;
import com.erban.main.model.dto.UserReportRecordDTO;
import com.erban.main.service.room.RoomFlowServie;
import com.erban.main.service.user.UserReportService;
import com.erban.web.common.BaseController;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.oauth2.service.common.util.DESUtils;
import com.xchat.oauth2.service.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 房间流水统计
 */
@Controller
@RequestMapping("/stat/roomFlow")
public class RoomFlowController extends BaseController {

    private static final String statFlowSecret="roomFlowSecretFY";
    @Autowired
    private RoomFlowServie roomFlowServie;


    @Autowired
    private UserReportService reportService;

    /**
     * 获取某个房间的房间流水
     *
     * @param roomUid
     * @return
     */
    @RequestMapping(value = "getOne", method = RequestMethod.GET)
    @ResponseBody
    @Authorization
    public BusiResult getRoomFlow(String roomUid) {
        BusiResult busiResult=null;
        logger.info("接口调用(/stat/roomFlow/getOne),获取某个房间的房间流水,接口入参:roomUid{}", roomUid);
        if(StringUtils.isBlank(roomUid)){
            logger.error("接口入参错误");
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        //roomuid 解密
        try {
            String roomUidStr = DESUtils.DESAndBase64Decrypt(roomUid, statFlowSecret);
            if(StringUtils.isBlank(roomUidStr) || !roomUidStr.matches("[0-9]*")){
                logger.error("roomUid 不合法,roomUid:{}",roomUidStr);
                return new BusiResult(BusiStatus.PARAMETERILLEGAL);
            }
           busiResult = roomFlowServie.getOne(Long.valueOf(roomUidStr));
        } catch (Exception e) {
            logger.error("接口调用(/stat/roomFlow/getOne),获取某个房间的房间流水异常,报错:{}", e);
            return new BusiResult(BusiStatus.BUSIERROR);

        }
        logger.info("接口调用(/stat/roomFlow/getOne),获取某个房间的房间流水,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }


    /**
     * 获取交友房间的房间流水明细
     * @param roomUid
     * @param current
     * @param size
     * @param request
     * @return
     */
    @RequestMapping(value = "getRoomIdByDetail")
    @ResponseBody
    @Authorization
    public BusiResult getRoomIdByDetail(String roomUid, String date, @RequestParam(defaultValue = "1")Integer current, @RequestParam(defaultValue = "50") Integer size, HttpServletRequest request){
        BusiResult busiResult=null;
        logger.info("接口调用(/stat/roomFlow/getRoomIdByDetail),获取交友房间的房间流水明细,接口入参:date:{},roomUid{}",date, roomUid);
        if(StringUtils.isBlank(roomUid) || StringUtils.isBlank(date)){
            logger.error("接口入参错误,date:{},roomUid",date,roomUid);
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        if(DateTimeUtil.convertStrToDate(date,"yyyy-MM-dd")==null){
            logger.error("传入的日期格式不合法,date:{}",date);
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        //roomuid 解密
        try {
            String roomUidStr = DESUtils.DESAndBase64Decrypt(roomUid, statFlowSecret);
            if(StringUtils.isBlank(roomUidStr) || !roomUidStr.matches("[0-9]*")){
                logger.error("roomUid 不合法,roomUid:{}",roomUidStr);
                return new BusiResult(BusiStatus.PARAMETERILLEGAL);
            }
            current = (current - 1) * size;
            busiResult = reportService.selectRoomUidByDetail(date,Long.valueOf(roomUidStr),current,size);
        } catch (Exception e) {
            logger.error("接口调用(/stat/roomFlow/getRoomIdByDetail),获取交友房间的房间流水明细异常,报错:{}", e);
            return new BusiResult(BusiStatus.BUSIERROR);

        }
        logger.info("接口调用(/stat/roomFlow/getRoomIdByDetail),获取交友房间的房间流水明细,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }


    /**
     * 获取交友房间的列表
     *
     * @param roomUid
     * @return
     */
    @RequestMapping(value = "getRoomUidByList", method = RequestMethod.GET)
    @ResponseBody
    @Authorization
    public BusiResult getRoomUidByList(String roomUid) {
        BusiResult busiResult=null;
        logger.info("接口调用(/stat/roomFlow/getRoomUidByList),获取交友房间的房间流水列表,接口入参:roomUid{}", roomUid);
        if(StringUtils.isBlank(roomUid) ){
            logger.error("接口入参错误,roomUid",roomUid);
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        //roomuid 解密
        try {
            String roomUidStr = DESUtils.DESAndBase64Decrypt(roomUid, statFlowSecret);
            if(StringUtils.isBlank(roomUidStr) || !roomUidStr.matches("[0-9]*")){
                logger.error("roomUid 不合法,roomUid:{}",roomUidStr);
                return new BusiResult(BusiStatus.PARAMETERILLEGAL);
            }
            busiResult = reportService.selectRoomUidByList(Long.valueOf(roomUidStr));
        } catch (Exception e) {
            logger.error("接口调用(/stat/roomFlow/getRoomUid),获取交友房间的房间流水列表异常,报错:{}", e);
            return new BusiResult(BusiStatus.BUSIERROR);

        }
        logger.info("接口调用(/stat/roomFlow/getRoomUid),获取交友房间的房间流水列表,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }


    /**
     * 获取某个房间的房间流水明细
     *
     * @param roomUid
     * @return
     */
    @RequestMapping(value = "getDetail", method = RequestMethod.GET)
    @ResponseBody
    @Authorization
    public BusiResult getRoomFlowDetail(String date, String roomUid) {
        BusiResult busiResult=null;
        logger.info("接口调用(/stat/roomFlow/getDetail),获取某个房间的房间流水明细,接口入参:date:{},roomUid{}",date, roomUid);
        if(StringUtils.isBlank(roomUid) || StringUtils.isBlank(date)){
            logger.error("接口入参错误,date:{},roomUid",date,roomUid);
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        if(DateTimeUtil.convertStrToDate(date,"yyyy-MM-dd")==null){
            logger.error("传入的日期格式不合法,date:{}",date);
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        //roomuid 解密
        try {
            String roomUidStr = DESUtils.DESAndBase64Decrypt(roomUid, statFlowSecret);
            if(StringUtils.isBlank(roomUidStr) || !roomUidStr.matches("[0-9]*")){
                logger.error("roomUid 不合法,roomUid:{}",roomUidStr);
                return new BusiResult(BusiStatus.PARAMETERILLEGAL);
            }
            busiResult = roomFlowServie.getDetail(date,Long.valueOf(roomUidStr));
        } catch (Exception e) {
            logger.error("接口调用(/stat/roomFlow/getDetail),获取某个房间的房间流水明细异常,报错:{}", e);
            return new BusiResult(BusiStatus.BUSIERROR);

        }
        logger.info("接口调用(/stat/roomFlow/getDetail),获取某个房间的房间流水明细,接口出参:{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

}
