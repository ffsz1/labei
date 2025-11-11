package com.erban.web.controller.room;

import com.erban.main.service.room.MicroSeqV2Service;
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
 * 7月份麦序改版
 * Created by liuguofu on 2017/7/27.
 * 麦序控制
 */
@Controller
@RequestMapping("/microV2")
public class MicroSeqV2Controller {
    private static final Logger logger = LoggerFactory.getLogger(MicroSeqV2Controller.class);
    @Autowired
    private MicroSeqV2Service microSeqV2Service;

    /**
     * 房主邀请用户上麦
     * @param uid
     * @param invitedUid
     * @return
     */
    @RequestMapping(value = "invite",method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult inviteByRoomOwner(Long uid,Long invitedUid){
        if(uid==null||invitedUid==null){
            return  new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult=null;
        try {
            busiResult=microSeqV2Service.inviteByRoomOwner(uid,invitedUid);
        } catch (Exception e) {
            logger.error("inviteByRoomOwner error...uid="+uid+"&invitedUid="+invitedUid+"& cause by="+e.getMessage());
            return new BusiResult(BusiStatus.BUSIERROR);
        }

        return busiResult;
    }

    /**
     * 用户同意上麦
     * @param uid
     * @param roomUid
     * @return
     */
    @RequestMapping(value = "accept",method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult acceptToUpMicro(Long uid,Long roomUid){
        BusiResult busiResult=null;
        try {
            busiResult=microSeqV2Service.acceptToUpMicro(uid,roomUid);
        } catch (Exception e) {
            logger.error("acceptToUpMicro error...uid="+uid+"&roomUid="+roomUid+"& cause by="+e.getMessage());
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return  busiResult;
    }

    @RequestMapping(value = "kick",method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult kickUserDownMicro(Long uid,Long kickedUid){

        if(uid==null||kickedUid==null){
            return  new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult=null;
        try {
            busiResult=microSeqV2Service.kickUserDownMicro(uid,kickedUid);
        } catch (Exception e) {
            logger.error("kickUserDownMicro error...uid="+uid+"&kickedUid="+kickedUid+"& cause by="+e.getMessage());
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;

    }

    /**
     * 房主置顶用户麦序，置为首麦
     * @param uid
     * @param topUid
     * @return
     */
    @RequestMapping(value = "top",method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult topUserMicro(Long uid,Long topUid){

        if(uid==null||topUid==null){
            return  new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult=null;
        try {
            busiResult=microSeqV2Service.topUserMicro(uid,topUid);
        } catch (Exception e) {
            logger.error("topUserMicro error...uid="+uid+"&topUid="+topUid+"& cause by="+e.getMessage());
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }


    /**
     * 用户自行离开麦序
     * @param uid
     * @param roomUid
     * @return
     */
    @RequestMapping(value = "userleft",method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult userLeftMicroSeq(Long uid,Long roomUid){
        if(uid==null||roomUid==null){
            return  new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult=null;
        try {
            busiResult=microSeqV2Service.userLeftMicroSeq(uid,roomUid);
        } catch (Exception e) {
            logger.error("userleft error...uid="+uid+"&topUid="+roomUid+"& cause by="+e.getMessage());
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }


    /**获取房间当前麦序列表
     *
     * @param uid 房主uid
     * @return
     */
    @RequestMapping(value = "list",method = RequestMethod.GET)
    @ResponseBody
    @SignVerification
    public BusiResult getAllMicroList(Long uid){

        if(uid==null|uid==0L){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        try {
            busiResult =microSeqV2Service.getAllMicroList(uid);
        } catch (Exception e) {
            logger.info("getAllMicroList Exception:"+e.getMessage()+"  uid="+uid);
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage("服务器好繁忙，请稍后重试！");
            return busiResult;
        }
        return busiResult;
    }



}
