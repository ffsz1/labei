package com.juxiao.xchat.manager.external.netease.impl;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.juxiao.xchat.manager.external.netease.NetEaseAccManager;
import com.juxiao.xchat.manager.external.netease.conf.NetEaseConf;
import com.juxiao.xchat.manager.external.netease.conf.NetEaseUrl;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @class: NetEaseAccManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/22
 */
@Service
public class NetEaseAccManagerImpl implements NetEaseAccManager {
    private static final String UPDATE_UINFO_URL = "https://api.netease.im/nimserver/user/updateUinfo.action";
    private static final String USER_BLOCK_URL = "https://api.netease.im/nimserver/user/block.action";
    private static final String USER_UNBLOCK_URL = "https://api.netease.im/nimserver/user/unblock.action";
    private static final String BASIC_URL = "https://api.netease.im/nimserver";
    private final Logger logger = LoggerFactory.getLogger(NetEaseAccManager.class);
    @Autowired
    private NetEaseConf neteaseConf;
    @Autowired
    private Gson gson;

    @Override
    public NetEaseRet updateUserGender(String accid, int gender) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(UPDATE_UINFO_URL, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);
        param.put("gender", gender);
        String result = client.buildHttpPostParam(param).executePost();
        logger.info("[ 云信发送消息 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{}", UPDATE_UINFO_URL, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseRet.class);
    }

    @Override
    public NetEaseRet updateUserInfo(String accid, String name, String icon) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(UPDATE_UINFO_URL, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);
        param.put("name", name);
        param.put("icon", icon);
        String result = client.buildHttpPostParam(param).executePost();
        logger.info("[ 云信发送消息 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{}", UPDATE_UINFO_URL, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseRet.class);
    }

    @Override
    public void block(String accid, String needkick) throws Exception {
        long startTime = System.currentTimeMillis();

        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);
        param.put("needkick", needkick);

        NetEaseClient client = new NetEaseClient(USER_BLOCK_URL, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        String result = client.buildHttpPostParam(param).executePost();
        logger.info("[ 云信用户消息 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{}", USER_BLOCK_URL, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
    }

    @Override
    public void unblock(String accid) throws Exception {
        long startTime = System.currentTimeMillis();

        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);

        NetEaseClient client = new NetEaseClient(USER_UNBLOCK_URL, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        String result = client.buildHttpPostParam(param).executePost();
        logger.info("[ 云信用户消息 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{}", USER_BLOCK_URL, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
    }

    @Override
    public void createNetEaseAcc(String accid, String token, String props) throws Exception{
        createNetEaseAcc(accid,token,props,"","");
    }

    public void createNetEaseAcc(String accid,String token,String props,String name,String icon) throws Exception{
        String url= BASIC_URL+ NetEaseUrl.CREATE_USER;
        NetEaseClient netEaseBaseClient=new NetEaseClient(url, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String,Object> param= Maps.newHashMap();
        param.put("accid",accid);
        param.put("token",token);
        param.put("name",name);
        param.put("props",props);
        param.put("icon",icon);
        String result=netEaseBaseClient.buildHttpPostParam(param).executePost();
        logger.info("注册网易云账号,accid="+accid+"&token="+token+"&props="+props+"&name="+name+"&icon="+icon+"|&result="+result);
    }

}
