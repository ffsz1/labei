package com.erban.web.controller.room;

import com.erban.main.service.room.MicroSeqService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.MicroSeqListVo;
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
 * Created by liuguofu on 2017/5/27.
 * 麦序控制
 * 一键置顶
 */
@Controller
@RequestMapping("/micro")
public class MicroSeqController {
    private static final Logger logger = LoggerFactory.getLogger(MicroSeqController.class);
    @Autowired
    private MicroSeqService microSeqService;

    /**
     * 用户直接上麦
     *
     * @param uid
     * @param roomUid
     * @param seqNo
     * @return
     */
    @ResponseBody
    @Authorization
    @SignVerification
    @RequestMapping(value = "upMicro", method = RequestMethod.POST)
    public BusiResult upMicro(Long uid, Long roomUid, int seqNo) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            busiResult = microSeqService.upMicro(uid, roomUid, seqNo);
        } catch (Exception e) {
            logger.info("upMicro Exception:" + e.getMessage() + "  uid=" + uid + ",roomUid=" + roomUid + ",seqNo=" + seqNo);
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage("服务器好繁忙，请稍后重试！");
            return busiResult;
        }
        return busiResult;
    }

    /**
     * 房主邀请用户上麦
     *
     * @param uid
     * @param applyUid
     * @return
     */
    @RequestMapping(value = "invite", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult inviteByRoomOwner(Long uid, Long applyUid) {

        return new BusiResult(BusiStatus.SUCCESS);
    }

    /**
     * 用户同意上麦
     *
     * @param uid
     * @param roomUid
     * @return
     */
    @RequestMapping(value = "accept", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult acceptToUpMicro(Long uid, Long roomUid) {

        return new BusiResult(BusiStatus.SUCCESS);
    }


    /**
     * 用户申请上麦
     *
     * @param uid     申请上麦用户uid
     * @param roomUid 当前房间房主Uid
     * @param ticket
     * @return
     */
    @RequestMapping(value = "apply", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult applyToMicroList(Long uid, Long roomUid, String ticket) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        return busiResult;
    }

    /**
     * 用户自行离开麦序申请列表（或者离开房间）
     *
     * @param uid
     * @param roomUid
     * @param ticket
     * @return
     */
    @RequestMapping(value = "leftapply", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult userLeaveApplyList(Long uid, Long roomUid, String ticket) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        return busiResult;
    }


    /**
     * 获取房间当前麦序列表
     *
     * @param uid  房主uid
     * @param type 获取麦序列表类型，1是上麦列表，2是申请列表，3是上麦列表+申请列表
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    @SignVerification
    public BusiResult getAllMicroList(Long uid, int type) {

        if (uid == null | uid == 0L) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            busiResult = microSeqService.getMicroVo(uid);
        } catch (Exception e) {
            logger.info("getAllMicroList Exception:" + e.getMessage() + "  uid=" + uid);
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage("服务器好繁忙，请稍后重试！");
            return busiResult;
        }
        return busiResult;
    }

    /**
     * 房主拒绝用户上麦申请，将用户从上麦申请列表中剔除
     *
     * @param uid
     * @param applyUid
     * @param ticket
     * @return
     */
    @RequestMapping(value = "denyapply", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult denyApplyUser(Long uid, Long applyUid, String ticket) {

        return new BusiResult(BusiStatus.SUCCESS);
    }


    /**
     * 房主修改麦序，全删全增。动作包括房主踢人下麦，置顶上麦，交换麦序
     * 同时需要全量更新上麦列表
     *
     * @param uid     房主uid
     * @param curUids 当前被操作用户uids
     * @param type    1上麦，2下麦
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult updateMicroSeq(Long uid, String curUids, int type, String ticket) {

        if (uid == null | uid == 0L || StringUtils.isEmpty(curUids)) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult<MicroSeqListVo> busiResult = new BusiResult<MicroSeqListVo>(BusiStatus.SUCCESS);

        try {
            busiResult = microSeqService.updateMicroSeqByRoomOwner(uid, curUids, type);
        } catch (Exception e) {
            logger.info("updateMicroSeq Exception:" + e.getMessage() + "  uid=" + uid + ",curUids=" + curUids);
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage("服务器好繁忙，请稍后重试！");
            return busiResult;
        }

        return busiResult;
    }

    /**
     * 用户自行离开麦麦序列表（自动下麦或者离开房间）
     *
     * @param uid
     * @param ticket
     * @return
     */

    @RequestMapping(value = "leftSeq", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult leaveMicroSeq(Long uid, Long roomUid, String ticket) {
        BusiResult busiResult = null;
        try {
            busiResult = microSeqService.userLeftMicroSeq(uid, roomUid);
        } catch (Exception e) {
            logger.info("leaveMicroSeq Exception:" + e.getMessage() + "  uid=" + uid + ",roomUid=" + roomUid);
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage("服务器好繁忙，请稍后重试！");
            return busiResult;
        }
        return busiResult;
    }


    public static void main(String args[]) {
        Long uid = null;
        int type = 0;
        MicroSeqController microSeqListVo = new MicroSeqController();
        microSeqListVo.getAllMicroList(null, 0);

    }


}
