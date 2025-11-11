package com.erban.web.controller;

import com.beust.jcommander.internal.Maps;
import com.erban.main.model.NeteaseChatroom;
import com.erban.main.model.NeteaseConversation;
import com.erban.main.mybatismapper.NeteaseChatroomMapper;
import com.erban.main.mybatismapper.NeteaseConversationMapper;
import com.erban.main.param.NetEaseBaseParam;
import com.erban.web.common.BaseController;
import com.google.gson.Gson;
import com.xchat.common.constant.Constant;
import com.xchat.common.netease.util.CheckSumBuilder;
import com.xchat.common.netease.util.NetEaseConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(value = "/recive")
public class ReciveNeteaseController extends BaseController {
    @Autowired
    private NeteaseChatroomMapper neteaseChatroomMapper;
    @Autowired
    private NeteaseConversationMapper neteaseConversationMapper;
    private Gson gson = new Gson();

    @RequestMapping(value = "msg", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> mockClient(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        Map<String, Object> result = Maps.newHashMap();
        try {
            // 获取请求体
            byte[] body = readBody(request);
            if (body == null) {
                logger.warn("request wrong, empty body!");
                result.put("code", 414);
                return result;
            }
            // 获取部分request header，并打印
            String ContentType = request.getContentType();
            String AppKey = request.getHeader("AppKey");
            String CurTime = request.getHeader("CurTime");
            String MD5 = request.getHeader("MD5");
            String CheckSum = request.getHeader("CheckSum");
            String[] data = {ContentType, AppKey, CurTime, MD5, CheckSum};
            // 将请求体转成String格式，并打印
            String requestBody = new String(body, "utf-8");
            // 获取计算过的md5及checkSum
            String verifyMD5 = CheckSumBuilder.getMD5(requestBody);
            String verifyChecksum = CheckSumBuilder.getCheckSum(NetEaseConstant.appSecret, verifyMD5, CurTime);
            if (!verifyMD5.equals(MD5)) {
                result.put("code", 400);
                return result;
            }
            if (!verifyChecksum.equals(CheckSum)) {
                result.put("code", 401);
                return result;
            }
            NetEaseBaseParam netEaseBaseParam = gson.fromJson(requestBody, NetEaseBaseParam.class);
            String eventType = netEaseBaseParam.getEventType();
            if (Constant.EventType.CHATROOM.equals(eventType)) {
                NeteaseChatroom neteaseChatroom = gson.fromJson(requestBody, NeteaseChatroom.class);
                if ("TEXT".equalsIgnoreCase(neteaseChatroom.getMsgType())) {// 保存房间的聊天信息
                    neteaseChatroomMapper.insert(neteaseChatroom);
                } else if ("CUSTOM".equalsIgnoreCase(neteaseChatroom.getMsgType()) && "22551544".equals(neteaseChatroom.getRoomId())) {// 保存线上公聊大厅的信息
                    neteaseChatroomMapper.insert(neteaseChatroom);
                }
            } else if (Constant.EventType.CONVERSATION.equals(eventType)) {
                NeteaseConversation neteaseConversation = gson.fromJson(requestBody, NeteaseConversation.class);
                if ("TEXT".equalsIgnoreCase(neteaseConversation.getMsgType())) {// 保存私聊的信息
                    neteaseConversation.setToStr(netEaseBaseParam.getTo());
                    neteaseConversationMapper.insert(neteaseConversation);
                }
            }
            result.put("code", 200);
            return result;
        } catch (Exception ex) {
//            logger.error(ex.getMessage(), ex);
            logger.error("Failed to received message. cause by " + ex.getMessage());
            result.put("code", 444);
            return result;
        }
    }

}
