package com.erban.main.service.noble;

import com.alibaba.fastjson.JSONObject;
import com.erban.main.config.SystemConfig;
import com.erban.main.model.NobleRight;
import com.erban.main.model.Users;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.base.BaseService;
import com.xchat.common.constant.Attach;
import com.xchat.common.constant.Constant;
import com.xchat.common.utils.PropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 贵族相关的小秘书提醒
 *
 */
@Service
public class NobleHelperService extends BaseService {

    @Autowired
    private SendSysMsgService sendSysMsgService;

    /**
     * 开通贵族的小秘书提醒
     * @param uid
     * @param nobleName
     */
    public void sendNobleOpenMess(Long uid, String nobleName) {
        String mess = "恭喜您成为“"+nobleName+"”贵族，您可以在“我的”-“我的贵族特权”查看和管理你的特权";
        sendNobleHelperMess(uid, Constant.DefineProtocol.CUSTOM_MESS_SUB_OPENNOBLE, mess);
    }

    /**
     * 续费贵族的小秘书提醒
     *
     * @param uid
     * @param nobleName
     */
    public void sendNobleRenewMess(Long uid, String nobleName) {
        String mess = "恭喜您成为“"+nobleName+"”贵族，您可以在“我的”-“我的贵族特权”查看和管理你的特权";
        sendNobleHelperMess(uid, Constant.DefineProtocol.CUSTOM_MESS_SUB_RENEWNOBLE, mess);
    }

    /**
     * 贵族即将过期提醒
     *
     * @param uid
     * @param nobleName
     */
    public void sendNobleWillExpireMess(Long uid, String nobleName) {
        String mess = "您的“"+nobleName+"”贵族称号即将到期，尊享贵族特权，请您及时续费~";
        sendNobleHelperMess(uid, Constant.DefineProtocol.CUSTOM_MESS_SUB_WILLEXPIRE, mess);
    }

    /**
     * 贵族过期提醒
     *
     * @param uid
     * @param nobleName
     */
    public void sendNobleHadExpireMess(Long uid, String nobleName) {
        String mess = "您的“"+nobleName+"”贵族称号已到期~";
        sendNobleHelperMess(uid, Constant.DefineProtocol.CUSTOM_MESS_SUB_HADEXPIRE, mess);
    }

    /**
     * 申请靓号成功
     *
     * @param uid
     */
    public void sendNobleGoodNumSuccessMess(Long uid) {
        String mess = "您提交的靓号已审核通过，立即生效";
        sendNobleHelperMess(uid, Constant.DefineProtocol.CUSTOM_MESS_SUB_GOODNUM_OK, mess);
    }

    /**
     * 申请靓号失败
     *
     * @param uid
     */
    public void sendNobleGoodNumFailMess(Long uid) {
        String mess = "您提交的靓号审核未通过，点击请重新设置>>";
        sendNobleHelperMess(uid, Constant.DefineProtocol.CUSTOM_MESS_SUB_GOODNUM_NOTOK, mess);
    }

    /**
     * 推荐热门房间生效提醒
     * @param uid
     */
    public void sendNobleRecomRoomMess(Long uid) {
        String mess = "您推荐的房间已出现在热门，感谢您的推荐";
        sendNobleHelperMess(uid, Constant.DefineProtocol.CUSTOM_MESS_SUB_RECOM_ROOM, mess);
    }

    /**
     * 房主获得分成提醒
     *
     * @param uid
     */
    public void sendNobleRoomIncomeMess(Long uid, Users users, NobleRight nobleRight, Long goldNum) {
        String mess = String.format("%s在你的房间开通“%s”贵族，奖励您%d金币已到账，请查收", users.getNick()
                , nobleRight.getName(), goldNum) ;
        logger.info("sendNobleRoomIncomeMess uid:{},nick:{},nobleName:{},mess:{}", uid, users.getNick()
                , nobleRight.getName(), mess);
        sendNobleHelperMess(uid, Constant.DefineProtocol.CUSTOM_MESS_SUB_ROOM_INCOME, mess);
    }

    public void sendNobleHelperMess(Long uid, int second, String mess) {
        Attach attach = new Attach();
        attach.setFirst(Constant.DefineProtocol.CUSTOM_MESS_HEAD_NOBLE);
        attach.setSecond(second);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", mess);
        attach.setData(jsonObject);

        NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
        neteaseSendMsgParam.setType(Constant.DefineProtocol.CUSTOM_MESS_DEFINE);
        neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
        neteaseSendMsgParam.setOpe(0);
        neteaseSendMsgParam.setTo(uid.toString());
        neteaseSendMsgParam.setAttach(attach);
        sendSysMsgService.sendMsg(neteaseSendMsgParam);
    }


}
