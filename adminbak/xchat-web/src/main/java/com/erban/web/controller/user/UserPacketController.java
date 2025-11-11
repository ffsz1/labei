package com.erban.web.controller.user;

import com.erban.main.service.user.UserPacketService;
import com.erban.main.service.user.UserShareRecordService;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by liuguofu on 2017/9/15.
 */
@Controller
@RequestMapping("/packet")
public class UserPacketController {
    private static final Logger logger = LoggerFactory.getLogger(UserPacketController.class);
    @Autowired
    private UserShareRecordService userShareRecordService;
    @Autowired
    private UserPacketService userPacketService;

    /**
     * 首次领取红包接口，在用户首次注册补全资料成功之后，客户端调用改接口
     * @param uid
     * @return
     */
    @RequestMapping(value = "first",method = RequestMethod.GET)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult checkAndGetFirsetPacket(Long uid){
        logger.info("UserPacketController checkAndGetFirsetPacket ");
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        if(uid==null||uid==0L){
            busiResult.setCode(BusiStatus.PARAMETERILLEGAL.value());
            busiResult.setMessage("参数异常");
            return busiResult;
        }
        try {
            busiResult=userPacketService.checkAndGetFirsetPacket(uid);
        } catch (Exception e) {
            logger.error("checkAndGetFirsetPacket error",e);
        }
        return busiResult;
    }

//    @RequestMapping(value = "query",method = RequestMethod.GET)
//    @ResponseBody
////    @Authorization
//    public BusiResult query(Long uid,String shareType,int sharePageId,Long targetUid){
//        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
//        if(uid==null||uid==0L){
//            busiResult.setCode(BusiStatus.PARAMETERILLEGAL.value());
//            busiResult.setMessage("参数异常");
//            return busiResult;
//        }
////        busiResult=userPacketService.saveUserShareRecord(uid,shareType,sharePageId,targetUid);
//        return busiResult;
//    }



}
