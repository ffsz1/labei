package com.juxiao.xchat.manager.external.netease.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.external.netease.NetEaseChatroomManager;
import com.juxiao.xchat.manager.external.netease.NetEaseRoomManager;
import com.juxiao.xchat.manager.external.netease.conf.NetEaseConf;
import com.juxiao.xchat.manager.external.netease.ret.NeteaseReqAddrRet;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NetEaseChatroomManagerImpl implements NetEaseChatroomManager {
    /**
     * 请求聊天室地址与令牌
     */
    private final String MEMBER_ROLE_SET_URL = "https://api.netease.im/nimserver/chatroom/updateMyRoomRole.action";
    /**
     * 请求聊天室地址
     */
    private final String REQUEST_ADDR_URL = "https://api.netease.im/nimserver/chatroom/requestAddr.action";
    private final Logger logger = LoggerFactory.getLogger(NetEaseRoomManager.class);
    @Autowired
    private NetEaseConf neteaseConf;


    @Override
    public NeteaseReqAddrRet requestAddr(Long roomId, Long accid, int clientType, String ip) throws Exception {
        NetEaseClient client = new NetEaseClient(REQUEST_ADDR_URL, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("accid", accid);
        param.put("clienttype", clientType);
        param.put("clientip", ip);
        String result = client.buildHttpPostParam(param).executePost();
        logger.info("[ 获取聊天室地址 ] 接口:>{},请求:>{},返回:{}", MEMBER_ROLE_SET_URL, param, result);
        if (StringUtils.isEmpty(result)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }

        return JSON.parseObject(result, NeteaseReqAddrRet.class);
    }

    @Override
    public int updateRole(Long roomId, String accid, Boolean save, String ext) throws Exception {
        NetEaseClient client = new NetEaseClient(MEMBER_ROLE_SET_URL, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("accid", accid);
        param.put("save", save);
        param.put("ext", ext);
        String result = client.buildHttpPostParam(param).executePost();
        logger.info("[ 变更角色信息 ] 接口:>{},请求:>{},返回:{}", MEMBER_ROLE_SET_URL, param, result);
        if (StringUtils.isEmpty(result)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        JSONObject object = JSON.parseObject(result);
        return object.getIntValue("code");
    }
}
