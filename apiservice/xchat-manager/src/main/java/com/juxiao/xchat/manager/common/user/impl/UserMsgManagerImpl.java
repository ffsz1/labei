package com.juxiao.xchat.manager.common.user.impl;

import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.external.netease.NetEaseMsgManager;
import com.juxiao.xchat.manager.external.netease.bo.Attach;
import com.juxiao.xchat.manager.external.netease.bo.NeteaseMsgBO;
import com.juxiao.xchat.manager.external.netease.bo.Payload;
import com.juxiao.xchat.manager.external.netease.bo.PicWordMsgAttach;
import com.juxiao.xchat.manager.common.user.UserMsgManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 用户异步方法类
 *
 * @class: UserMsgManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
@Service
public class UserMsgManagerImpl implements UserMsgManager {
    private static final Logger logger = LoggerFactory.getLogger(UserMsgManagerImpl.class);
    @Autowired
    private NetEaseMsgManager neteaseMsgManager;
    @Autowired
    private SystemConf systemConf;

    @Async
    @Override
    public void sendDrawMsg(Long uid) {
//        Payload payload = new Payload();
//        payload.setSkiptype(Payload.SkipType.H5);
//
//        NeteaseMsgBO msgBo = new NeteaseMsgBO();
//        msgBo.setFrom(systemConf.getSecretaryUid());
//        msgBo.setOpe(0);
//        msgBo.setType(100);
//        msgBo.setTo(String.valueOf(uid));
//        msgBo.setPayload(payload);
//
//        PicWordMsgAttach picBO = new PicWordMsgAttach();
//        picBO.setTitle("充值成功！");
//        picBO.setDesc("恭喜您获得欢乐扭蛋机一次扭蛋机会！！");
//        picBO.setPicUrl("https://pic.173ing.com/draw_nd.png");
//        picBO.setWebUrl("");
//
//        Attach attach = new Attach();
//        attach.setFirst(Attach.MsgType.PointToPoint);
//        attach.setSecond(Attach.MsgType.PushPicWord);
//        attach.setData(picBO);
//        msgBo.setAttach(attach);
//        neteaseMsgManager.sendMsg(msgBo);
        NeteaseMsgBO msgBo = new NeteaseMsgBO();
        msgBo.setFrom(systemConf.getSecretaryUid());
        msgBo.setOpe(0);
        msgBo.setType(0);
        msgBo.setTo(uid.toString());
        msgBo.setBody("您获得了一次抽奖机会，快点去首页-幸运转盘抽奖吧！");
        neteaseMsgManager.sendMsg(msgBo);
    }

}
