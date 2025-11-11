package com.erban.web.controller.gift;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.erban.main.service.gift.GiftSendService;
import com.erban.main.util.StringUtils;
import com.xchat.common.annotation.SignVerification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.erban.main.service.gift.GiftService;
import com.erban.main.vo.GiftVo;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;

@Controller
@RequestMapping("/gift")
public class GiftController {
    private static final Logger logger = LoggerFactory.getLogger(GiftController.class);
    @Autowired
    private GiftService giftService;
    @Autowired
    private GiftSendService giftSendService;

    /**
     * 赠送礼物
     *
     * @param uid
     * @param giftId
     * @param targetUid
     * @param type
     * @return
     */
    @ResponseBody
    @Authorization
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public BusiResult sendGift(Long uid, Integer giftId, Long targetUid, int type) {
        logger.info("调用接口：/gift/send；赠送礼物，接口入参：uid:{},giftId:{},targetUid:{},type:{}", uid, giftId, targetUid, type);
        BusiResult busiResult = null;
        try {
            busiResult = giftService.sendGift(uid, giftId, targetUid, type);
        } catch (Exception e) {
            logger.error("sendGift error uid=" + uid + "&giftId=" + giftId + "&targetUid=" + targetUid + "  causeby:"
                    + e.getMessage(), e);
            busiResult = new BusiResult(BusiStatus.BUSIERROR);
            return busiResult;
        }
        logger.info("赠送礼物（send）接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /**
     * 赠送礼物
     *
     * @param uid
     * @param giftId
     * @param targetUid
     * @param roomUid
     * @param type
     * @param giftNum
     * @return
     */
    @ResponseBody
    @Authorization
    @RequestMapping(value = "/sendV2")
    public BusiResult sendGiftV2(Long uid, Integer giftId, Long targetUid, Long roomUid, int type, int giftNum) {
        logger.info("调用接口：/gift/sendV2；赠送礼物，接口入参：uid:{},giftId:{},targetUid:{},roomUid:{},type:{},giftNum:{}", uid, giftId, targetUid, roomUid, type, giftNum);
        if (giftNum <= 0 || uid == null || giftId == null || giftId == null || targetUid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        if (targetUid == 0) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        logger.info("sendV2  uid=" + uid + "&giftId=" + giftId + "&targetUid=" + targetUid + "&giftNum=+" + giftNum + "&roomUid=" + roomUid);
        BusiResult busiResult = null;
        try {
            busiResult = giftService.sendGiftV2(uid, giftId, targetUid, roomUid, giftNum, type);
        } catch (Exception e) {
            logger.error("sendV2 error uid=" + uid + "&giftId=" + giftId + "&targetUid=" + targetUid + "&giftNum=+" + giftNum + "&roomUid=" + roomUid + "  causeby:" + e.getMessage(), e);
            busiResult = new BusiResult(BusiStatus.BUSIERROR);
            return busiResult;
        }
        logger.info("赠送礼物（sendV2）接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    @ResponseBody
    @Authorization
    @RequestMapping(value = "/sendWholeMicro", method = RequestMethod.POST)
    public BusiResult sendGiftToWholeMicro(Long uid, Integer giftId, @RequestParam(value = "targetUids") Long[] targetUids, Long roomUid, int giftNum) {
        if (giftNum <= 0 || uid == null || giftId == null || giftId == null || targetUids == null || targetUids.length < 1) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult = null;
        try {
            busiResult = giftService.sendGiftToWholeMicro(uid, giftId, targetUids, roomUid, giftNum);
        } catch (Exception e) {
            logger.error("sendGiftToWholeMicro error uid=" + uid + "&giftId=" + giftId + "&targetUid=" + targetUids + "&giftNum=+"
                    + giftNum + "&roomUid=" + roomUid + "  causeby:" + e.getMessage(), e);
            busiResult = new BusiResult(BusiStatus.BUSIERROR);
            return busiResult;
        }
        return busiResult;
    }

//    @RequestMapping(value = "/sendV3")
//    @ResponseBody
//    @Authorization
//    public BusiResult sendGiftV3(Long uid, Integer giftId, Long targetUid,Long roomUid, int type, int giftNum) {
//        if (giftNum<=0||uid==null||giftId==null||giftId==null||targetUid==null) {
//            return  new BusiResult(BusiStatus.PARAMETERILLEGAL);
//        }
//        if(targetUid==0){
//            return  new BusiResult(BusiStatus.PARAMETERILLEGAL);
//        }
//        logger.info("sendV3  uid=" + uid + "&giftId=" + giftId + "&targetUid=" + targetUid + "&giftNum=+"+giftNum+"&roomUid="+roomUid);
//        BusiResult busiResult = null;
//        try {
//            busiResult = giftService.sendGiftV3( uid,  giftId,  targetUid, roomUid,  giftNum, type);
//        } catch (Exception e) {
//            logger.error("sendV3 error uid=" + uid + "&giftId=" + giftId + "&targetUid=" + targetUid + "&giftNum=+"+giftNum+"&roomUid="+roomUid+"  causeby:"+e.getMessage(),e);
//            busiResult = new BusiResult(BusiStatus.BUSIERROR);
//            return busiResult;
//        }
//        return busiResult;
//    }
//
//    @RequestMapping(value = "/sendWholeMicroV3")
//    @ResponseBody
//    @Authorization
//    public BusiResult sendWholeMicroV3(Long uid, Integer giftId, @RequestParam(value = "targetUids")Long [] targetUids, Long roomUid, int giftNum) {
//        if (giftNum<=0||uid==null||giftId==null||giftId==null||targetUids==null||targetUids.length<1) {
//            return  new BusiResult(BusiStatus.PARAMETERILLEGAL);
//        }
//        BusiResult busiResult = null;
//        try {
//            busiResult = giftService.sendGiftToWholeMicroV3( uid,  giftId, targetUids, roomUid,  giftNum);
//        } catch (Exception e) {
//            logger.error("sendWholeMicroV3 error uid=" + uid + "&giftId=" + giftId + "&targetUid=" + targetUids + "&giftNum=+"+giftNum+"&roomUid="+roomUid+"  causeby:"+e.getMessage(),e);
//            busiResult = new BusiResult(BusiStatus.BUSIERROR);
//            return busiResult;
//        }
//        return busiResult;
//    }


    /**
     * 获取礼物列表，无版本号
     *
     * @return
     */
    @ResponseBody
    @SignVerification
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public BusiResult<List<GiftVo>> getGfitList() {
        logger.info("调用接口：/gift/list；获取礼物，接口入参：{}", "无");
        BusiResult<List<GiftVo>> busiResult = null;
        try {
            busiResult = giftService.getGiftListVo();
        } catch (Exception e) {
            logger.error("getGfitList Exception:" + e.getMessage());
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage("服务器好繁忙，请稍后重试！");
            return busiResult;
        }
        logger.info("获取礼物（list）接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /**
     * 获取礼物列表,带有礼物版本号,其逻辑与list接口大致相同
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "listV2", method = RequestMethod.GET)
    public BusiResult<Map<String, Object>> getGfitListV2(Integer uid, Integer type) {
        logger.info("调用接口：/gift/listV2；获取礼物，接口入参：{}", "无");
        BusiResult<Map<String, Object>> busiResult = null;
        try {
            busiResult = giftService.getGiftListVoV2(uid, type);
        } catch (Exception e) {
            logger.error("getGfitListV2 Exception:" + e.getMessage(), e);
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage("服务器好繁忙，请稍后重试！");
            return busiResult;
        }
        logger.info("获取礼物（list）接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    @ResponseBody
    @SignVerification
    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    public BusiResult<Map<String, Object>> refreshGiftVersion(String version) {
        if (StringUtils.isEmpty(version)) {
            return new BusiResult<Map<String, Object>>(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult<Map<String, Object>> busiResult = new BusiResult<Map<String, Object>>(BusiStatus.SUCCESS);
        try {
            giftService.refreshGiftVersion(version);
        } catch (Exception e) {
            logger.error("refresh Exception:" + e.getMessage(), e);
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage("服务器好繁忙，请稍后重试！");
            return busiResult;
        }
        return busiResult;
    }

}
