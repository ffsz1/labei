package com.juxiao.xchat.service.api.room.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.juxiao.xchat.dao.sysconf.enumeration.NetEaseMsgEventType;
import com.juxiao.xchat.manager.external.netease.conf.NetEaseConf;
import com.juxiao.xchat.manager.external.netease.impl.CheckSumBuilder;
import com.juxiao.xchat.service.api.netease.NetEaseLogoutMsgService;
import com.juxiao.xchat.service.api.room.NetEaseReciveService;
import com.juxiao.xchat.service.api.room.bo.LogoutMsgBO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @class: NetEaseReciveServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/27
 */
@Service
public class NetEaseReciveServiceImpl implements NetEaseReciveService {

    private final Logger logger = LoggerFactory.getLogger(NetEaseReciveService.class);
    @Autowired
    private NetEaseConf easeConf;
    @Autowired
    private NetEaseLogoutMsgService netEaseLogoutMsgService;
    @Autowired
    private Gson gson;


    @Override
    public int reciveNetEaseMsg(String curTime, String md5, String checkSum, String requestBody) {
        // 获取请求体，将请求体转成String格式，并打印
        if (StringUtils.isBlank(requestBody)) {
            logger.warn("[ 接收云信信息 ] 请求内容为空");
            return 414;
        }

        // 获取计算过的md5及checkSum
        String verifyMD5 = CheckSumBuilder.getMD5(requestBody);
        if (!verifyMD5.equals(md5)) {
            logger.warn("[ 接收云信信息 ] MD5验证错误，接收的MD5:>{}，本地生成MD5:>{}", md5, verifyMD5);
            return 400;
        }

        String verifyChecksum = CheckSumBuilder.getCheckSum(easeConf.getAppSecret(), verifyMD5, curTime);
        if (!verifyChecksum.equals(checkSum)) {
            logger.warn("[ 接收云信信息 ] checkSum验证证错误，接收的checkSum:>{}，本地生成verifyChecksum:>{}", checkSum, verifyChecksum);
            return 401;
        }

        JSONObject object = JSON.parseObject(requestBody);
        Integer eventType = object.getInteger("eventType");
//        if (NetEaseMsgEventType.CHATROOM.compareValue(eventType)) {
//            NetEaseChatroomDO chatroomDo = gson.fromJson(requestBody, NetEaseChatroomDO.class);
//            // 保存房间的聊天信息+保存线上公聊大厅的信息
//            if ("TEXT".equalsIgnoreCase(chatroomDo.getMsgType()) || ("CUSTOM".equalsIgnoreCase(chatroomDo.getMsgType()) && "22551544".equals(chatroomDo.getRoomId()))) {
//                try{
//                    chatroomDo.setCreateTime(new Date(Long.valueOf(chatroomDo.getMsgTimestamp())));
//                } catch (Exception e) {
//                }
//                try {
//                    chatroomDao.save(chatroomDo);
//                } catch (Exception e) {
//                }
//            }
//
//            return 200;
//        }
//
//        if (NetEaseMsgEventType.CONVERSATION.compareValue(eventType)) {
//            NetEaseConversationDO conversationDo = gson.fromJson(requestBody, NetEaseConversationDO.class);
//            // 保存私聊的信息
//            if ("TEXT".equalsIgnoreCase(conversationDo.getMsgType())) {
//                conversationDo.setToStr(object.getString("to"));
//                try{
//                    conversationDo.setCreateTime(new Date(Long.valueOf(conversationDo.getMsgTimestamp())));
//                } catch (Exception e) {
//                }
//                try {
//                    conversationDao.save(conversationDo);
//                } catch (Exception e) {
//                }
//            }
//        }
        if (NetEaseMsgEventType.LOGOUT.compareValue(eventType)) {
            // 用户退出或者掉线
            LogoutMsgBO msgBO = gson.fromJson(requestBody, LogoutMsgBO.class);
            netEaseLogoutMsgService.logout(msgBO);
        }


        return 200;
    }
}
