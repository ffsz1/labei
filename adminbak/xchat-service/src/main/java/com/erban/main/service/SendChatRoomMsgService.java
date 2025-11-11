package com.erban.main.service;

import com.google.gson.Gson;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Attach;
import com.xchat.common.netease.neteaseacc.result.RubbishRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by liuguofu on 2017/6/6.
 */
@Service
public class SendChatRoomMsgService {
    private static final Logger logger = LoggerFactory.getLogger(SendChatRoomMsgService.class);
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;
    private static int defMsgType=100;
    private Gson gson=new Gson();


    public int sendSendChatRoomMsg(Long roomId, String fromAccid, int first, int second, Object data) throws Exception{
        Attach attach=new Attach();
        attach.setFirst(first);
        attach.setSecond(second);
        attach.setData(data);
        String attachStr=gson.toJson(attach);
        logger.info("attachStr="+attachStr);
        String msgId= UUIDUitl.get();
        RubbishRet rubbishRet=erBanNetEaseService.sendChatRoomMsg(roomId,msgId,fromAccid,defMsgType,attachStr);

        if(rubbishRet.getCode()!=200){
            logger.info("发送聊天室消息失败，失败code="+rubbishRet.getCode());
            logger.error("发送聊天室消息失败，失败code="+rubbishRet.getCode());
            throw new Exception("发送聊天室消息失败，失败code="+rubbishRet.getCode());
        }
        return 0;

    }
}
