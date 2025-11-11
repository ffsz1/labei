package com.juxiao.xchat.manager.external.netease.impl;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.juxiao.xchat.manager.external.netease.NetEaseManager;
import com.juxiao.xchat.manager.external.netease.conf.NetEaseConf;
import com.juxiao.xchat.manager.external.netease.ret.FriendRet;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseRet;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class NetEaseManagerImpl implements NetEaseManager {
    private static final String FRIEND_ADD_URL = "https://api.netease.im/nimserver/friend/add.action";
    private static final String FRIEND_DELETE_URL = "https://api.netease.im/nimserver/friend/delete.action";
    private static final String FRIEND_LIST_URL = "https://api.netease.im/nimserver/friend/get.action";
    private static final Logger logger = LoggerFactory.getLogger(NetEaseManager.class);
    private static Date createDate;
    @Autowired
    private NetEaseConf neteaseConf;
    @Autowired
    private Gson gson;


    static {
        createDate = new Date();
        createDate = DateUtils.setYears(createDate, 2018);
        createDate = DateUtils.setMonths(createDate, 0);
        createDate = DateUtils.setDays(createDate, 1);
        createDate = DateUtils.setHours(createDate, 0);
    }

    @Override
    public NetEaseRet addFriends(Long accid, Long faccid, Byte type, String msg) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(FRIEND_ADD_URL, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);
        param.put("faccid", faccid);
        param.put("type", type);
        param.put("msg", msg);
        String result = client.buildHttpPostParam(param).executePost();
        logger.info("[ 云信发送消息 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{}", FRIEND_ADD_URL, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseRet.class);
    }

    @Override
    public NetEaseRet deleteFriends(Long accid, Long faccid) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(FRIEND_DELETE_URL, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);
        param.put("faccid", faccid);
        String result = client.buildHttpPostParam(param).executePost();
        logger.info("[ 云信发送消息 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{}", FRIEND_DELETE_URL, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseRet.class);
    }

    @Override
    public FriendRet listFriends(Long accid) throws Exception {
        //
        return null;
//        return listFriends(accid, createDate);
    }

    @Override
    public FriendRet listFriends(Long accid, Date beginDate) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(FRIEND_LIST_URL, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("updatetime", beginDate.getTime());
        param.put("accid", accid);
        String result = client.buildHttpPostParam(param).executePost();
        logger.info("[ 云信发送消息 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{}", FRIEND_LIST_URL, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, FriendRet.class);
    }
}
