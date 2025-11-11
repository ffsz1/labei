package com.juxiao.xchat.manager.external.netease.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.DefMsgType;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.item.vo.OfficialMsgVO;
import com.juxiao.xchat.manager.common.item.vo.OfficialUserVO;
import com.juxiao.xchat.manager.external.netease.NetEaseRoomManager;
import com.juxiao.xchat.manager.external.netease.bo.Attach;
import com.juxiao.xchat.manager.external.netease.conf.NetEaseConf;
import com.juxiao.xchat.manager.external.netease.conf.NetEaseUrl;
import com.juxiao.xchat.manager.external.netease.ret.AddrNetEaseRet;
import com.juxiao.xchat.manager.external.netease.ret.MicUserResult;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseRet;
import com.juxiao.xchat.manager.external.netease.ret.RoomResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 网易对接接口操作
 *
 * @class: NetEaseManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
@Service
public class NetEaseRoomManagerImpl implements NetEaseRoomManager {
    private Gson gson = new Gson();
    private final String BASE_ROOM_URL = "https://api.netease.im/nimserver/msg/sendMsg.action";

    private final Logger logger = LoggerFactory.getLogger(NetEaseRoomManager.class);

    @Autowired
    private NetEaseConf neteaseConf;
    @Autowired
    private SystemConf systemConf;

    @Override
    public RoomResult openRoom(String accid, String name, String ext) {
        String url = NetEaseUrl.URL_PREFIX + NetEaseUrl.ROOM_CREATE;
        Map<String, Object> param = Maps.newHashMap();
        param.put("creator", accid);
        param.put("name", name);
        param.put("ext", ext);
        try {
            return executePost(url, param, RoomResult.class);
        } catch (Exception e) {
            logger.error("[ 网易接口 - 开通房间] 请求异常：", e);
        }
        return new RoomResult(500, "接口调用异常...");
    }

    @Override
    public RoomResult getRoomMessage(Long roomId) {
        String url = NetEaseUrl.URL_PREFIX + NetEaseUrl.ROOM_GET;
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("needOnlineUserCount", true);
        try {
            return executePost(url, param, RoomResult.class);
        } catch (Exception e) {
            logger.error("[ 网易接口 - 开通房间] 请求异常：", e);
        }
        return new RoomResult(500, "接口调用异常...");
    }

    @Override
    public RoomResult toggleRoomStatus(Long roomId, String operator, boolean valid) {
        String url = NetEaseUrl.URL_PREFIX + NetEaseUrl.ROOM_TOGGLE_STAT;
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("operator", operator);
        param.put("valid", String.valueOf(valid));
        try {
            return executePost(url, param, RoomResult.class);
        } catch (Exception e) {
            logger.error("[ 网易接口 - 切换房间状态] 请求异常：", e);
        }
        return new RoomResult(500, "接口调用异常...");
    }

    @Override
    public MicUserResult queueList(Long roomId) {
        String url = NetEaseUrl.URL_PREFIX + NetEaseUrl.ROOM_QUEUE_LIST;
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        try {
            return executePost(url, param, MicUserResult.class);
        } catch (Exception e) {
            logger.error("[ 网易接口 - 列出队列元素--上麦的人] 请求异常：", e);
        }
        return new MicUserResult(500, "接口调用异常...");
    }

    @Override
    public NetEaseRet queuePoll(Long roomId, String position) {
        String url = NetEaseUrl.URL_PREFIX + NetEaseUrl.ROOM_QUEUE_POLL;
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("key", position);
        try {
            return executePost(url, param, NetEaseRet.class);
        } catch (Exception e) {
            logger.error("[ 网易接口 - 弹出队列元素--下麦] 请求异常：", e);
        }
        return new NetEaseRet(500, "接口调用异常...");
    }

    @Override
    public NetEaseRet queueOffer(Long roomId, String position, String value, String operator, boolean transie) {
        String url = NetEaseUrl.URL_PREFIX + NetEaseUrl.ROOM_QUEUE_OFFER;
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("key", position);
        param.put("value", value);
        param.put("transient", String.valueOf(transie));
        if (StringUtils.isNotBlank(operator)) {
            param.put("operator", operator);
        }
        try {
            return executePost(url, param, NetEaseRet.class);
        } catch (Exception e) {
            logger.error("[ 网易接口 - 更新队列元素--上麦] 请求异常：", e);
        }
        return new NetEaseRet(500, "接口调用异常...");
    }

    @Override
    public NetEaseRet sendChatRoomMsg(Long roomId, String msgId, String fromAccid, int msgType, String attach) {
        String url = NetEaseUrl.URL_PREFIX + NetEaseUrl.ROOM_SEND_MSG;
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("msgId", msgId);
        param.put("fromAccid", fromAccid);
        param.put("msgType", msgType);
        param.put("attach", attach);
        try {
            return executePost(url, param, NetEaseRet.class);
        } catch (Exception e) {
            logger.error("[ 网易接口 - 发送聊天室消息] 请求异常：", e);
        }
        return new NetEaseRet(500, "接口调用异常...");
    }

    @Override
    public NetEaseRet updateRoomInfo(Long roomId, String name, String ext, String notifyExt) {
        String url = NetEaseUrl.URL_PREFIX + NetEaseUrl.ROOM_UPDATE;
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("name", name);
        param.put("ext", ext);
        if (StringUtils.isNotBlank(notifyExt)) {
            param.put("notifyExt", notifyExt);
        }
        try {
            return executePost(url, param, NetEaseRet.class);
        } catch (Exception e) {
            logger.error("[ 网易接口 - 更新房间信息] 请求异常：", e);
        }
        return new NetEaseRet(500, "接口调用异常...");
    }

    @Override
    public NetEaseRet queueDrop(Long roomId) {
        String url = NetEaseUrl.URL_PREFIX + NetEaseUrl.ROOM_QUEUE_DROP;
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        try {
            return executePost(url, param, NetEaseRet.class);
        } catch (Exception e) {
            logger.error("[ 网易接口 - 清空队列--关闭房间] 请求异常：", e);
        }
        return new NetEaseRet(500, "接口调用异常...");
    }



    @Override
    public void deleteRobot(Long roomId, String id) {
        String url = NetEaseUrl.URL_PREFIX + NetEaseUrl.REMOVE_ROBOT;
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("accids", id);
        try {
            executePost(url, param, NetEaseRet.class);
        } catch (Exception e) {
            logger.error("[ 网易接口 - 发送聊天室消息] 请求异常：", e);
        }
    }

    @Override
    public void addRobot(long roomId, String accids) {
        String url = NetEaseUrl.URL_PREFIX + NetEaseUrl.ADD_ROBOT;
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("accids", accids);
        try {
            executePost(url, param, NetEaseRet.class);
        } catch (Exception e) {
            logger.error("[ 网易接口 - 发送聊天室消息] 请求异常：", e);
        }
    }

    /**
     * 创建post请求, 根据请求的clazz参数,将返回值转换成改类型
     *
     * @param url   请求地址
     * @param param 请求参数
     * @param clazz 返回值类型
     * @param <T>   返回值的泛型
     * @return
     * @throws Exception 接口调用失败会抛出异常
     */
    private <T> T executePost(String url, Map<String, Object> param, Class<T> clazz) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(url, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        String result = client.buildHttpPostParam(param).executePost();
        logger.info("[ 云信接口请求 ] 接口:>{},参数:>{},返回:>{},耗时:>{}", url, param, result, (System.currentTimeMillis() - startTime));
        return JSONObject.parseObject(result, clazz);
    }

    /**
     * 获取聊天室地址
     *
     * @param accId      房主账号
     * @param roomId     房间
     * @param clientType 1:weblink（客户端为web端时使用）; 2:commonlink（客户端为非web端时使用）;3:wechatlink(微信小程序使用), 默认1
     * @return
     * @throws Exception
     */
    @Override
    public AddrNetEaseRet inRoom(long roomId, String accId, int clientType) throws Exception {
        NetEaseClient netEaseBaseClient = new NetEaseClient(BASE_ROOM_URL, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("accid", accId);
        param.put("clienttype", clientType);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        return gson.fromJson(result, AddrNetEaseRet.class);
    }

    @Override
    public NetEaseRet sendOfficialMsg(String message) {
        OfficialUserVO userVO = new OfficialUserVO();
        userVO.setAvatar(systemConf.getOfficialAvatar());
        // userVO.setCharmLevel(null);
        userVO.setNick(systemConf.getOfficialNick());
        userVO.setUid(systemConf.getOfficialUid());
        userVO.setTxtColor("#" + systemConf.getTxtColor());
        OfficialMsgVO msgVO = new OfficialMsgVO();
        msgVO.setMsg(message);
        msgVO.setParams(userVO);
        Attach attach = new Attach();
        attach.setFirst(DefMsgType.roomOfficialMsg);
        attach.setSecond(DefMsgType.roomOfficialMsg);
        attach.setData(msgVO);
        return sendChatRoomMsg(systemConf.getOfficialRoomId(), UUIDUtils.get(), systemConf.getOfficialUid().toString(), 100, JSONObject.toJSONString(attach));
    }

    @Override
    public NetEaseRet queryMembers(long roomId, String accids) {
        String url = NetEaseUrl.URL_PREFIX + NetEaseUrl.ROOM_QUERY_MEMBERS;
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("accids", accids);
        try {
            return executePost(url, param, NetEaseRet.class);
        } catch (Exception e) {
            logger.error("[ 网易接口 - 查询成员] 请求异常：", e);
        }
        return new NetEaseRet(500, "接口调用异常...");
    }
}
