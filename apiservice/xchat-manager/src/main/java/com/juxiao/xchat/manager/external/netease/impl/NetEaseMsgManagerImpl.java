package com.juxiao.xchat.manager.external.netease.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import com.juxiao.xchat.manager.external.netease.NetEaseMsgManager;
import com.juxiao.xchat.manager.external.netease.bo.Body;
import com.juxiao.xchat.manager.external.netease.bo.NeteaseBatchMsgBO;
import com.juxiao.xchat.manager.external.netease.bo.NeteaseMsgBO;
import com.juxiao.xchat.manager.external.netease.bo.NeteasePushBO;
import com.juxiao.xchat.manager.external.netease.conf.NetEaseConf;
import com.juxiao.xchat.manager.external.netease.ret.RubbishRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @class: NetEaseMsgManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/22
 */
@Service
public class NetEaseMsgManagerImpl implements NetEaseMsgManager {
    private static final String MSG_SEND_URL = "https://api.netease.im/nimserver/msg/sendMsg.action";
    private static final String BATCH_MSG_SEND_URL = "https://api.netease.im/nimserver/msg/sendBatchMsg.action";
    private static final String ATTACH_MSG_URL = "https://api.netease.im/nimserver/msg/sendAttachMsg.action";
    private final Logger logger = LoggerFactory.getLogger(NetEaseMsgManager.class);
    @Autowired
    private NetEaseConf neteaseConf;
    @Autowired
    private SystemConf systemConf;
    @Autowired
    private Gson gson;
    @Autowired
    private SysConfManager sysConfManager;

    /**
     * @see com.juxiao.xchat.manager.external.netease.NetEaseMsgManager#sendMsg(NeteaseMsgBO)
     */
    @Override
    public RubbishRet sendMsg(NeteaseMsgBO msgBo) {
        // 过滤审核账号
        if (systemConf.getAuditAccountList().contains(msgBo.getTo())) {
            return new RubbishRet();
        }

        Long toUid = Long.valueOf(msgBo.getTo());
        // 若uid超出范围则不发送
        if ((toUid > 30000 * 10000 && toUid <= 40000 * 10000) // 正式服
                || toUid > (4 * 10000 * 10000 + 100000)) {// 测试服
            return new RubbishRet();
        }

        String bodyStr;
        long startTime = System.currentTimeMillis();
        switch (msgBo.getType()) {
            case 0:
                JSONObject json = new JSONObject();
                json.put("msg", msgBo.getBody());
                bodyStr = json.toJSONString();
                break;
            case 1:
                bodyStr = msgBo.getPicture() == null ? "" : gson.toJson(msgBo.getPicture());
                break;
            case 100:
                bodyStr = msgBo.getBody() == null ? "" : msgBo.getBody();
                break;
            default:
                bodyStr = "";
                break;
        }

        String payloadStr = msgBo.getPayload() == null ? "" : gson.toJson(msgBo.getPayload());
        String optionStr = msgBo.getOption() == null ? "" : gson.toJson(msgBo.getOption());
        NetEaseClient client = new NetEaseClient(MSG_SEND_URL, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("from", msgBo.getFrom());
        param.put("ope", msgBo.getOpe());
        param.put("to", msgBo.getTo());
        param.put("type", msgBo.getType());
        param.put("body", bodyStr);
        param.put("pushcontent", msgBo.getPushcontent());
        param.put("payload", payloadStr);
        param.put("option", optionStr);
        try {
            String result = client.buildHttpPostParam(param).executePost();
            logger.info("[ 云信发送消息 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{}", MSG_SEND_URL, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
            return gson.fromJson(result, RubbishRet.class);
        } catch (Exception e) {
            logger.error("[ 云信发送消息 ]接口:>{}，请求:>{}，异常信息：", MSG_SEND_URL, gson.toJson(param), e);
            return null;
        }
    }

    /**
     * @see com.juxiao.xchat.manager.external.netease.NetEaseMsgManager#sendBatchMsg(NeteaseBatchMsgBO)
     */
    @Override
    public RubbishRet sendBatchMsg(NeteaseBatchMsgBO batchMsgBo) {
        SysConfDTO confDTO = sysConfManager.getSysConf(SysConfigId.batch_msg_option);
        if (confDTO != null && Boolean.FALSE.toString().equals(confDTO.getConfigValue())) {
            // 群发消息的开关, 被关闭了, 不发消息出去
            return new RubbishRet();
        }
        String attachStr = "";
        long startTime = System.currentTimeMillis();
        if (batchMsgBo.getType() == 0 || batchMsgBo.getType() == 1) {//图片或者文字
            String content = batchMsgBo.getContent();
            JSONObject json = new JSONObject();
            json.put("msg", content);
            attachStr = json.toJSONString();
        } else if (batchMsgBo.getType() == 100) {//图文
            Body body = batchMsgBo.getBody();
            attachStr = gson.toJson(body);
        }

        NetEaseClient netEaseBaseClient = new NetEaseClient(BATCH_MSG_SEND_URL, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("fromAccid", batchMsgBo.getFromAccid());
        param.put("toAccids", gson.toJson(batchMsgBo.getToAccids()));
        param.put("type", batchMsgBo.getType());
        param.put("body", attachStr);
        param.put("pushcontent", batchMsgBo.getPushcontent());
        param.put("payload", batchMsgBo.getPayload() == null ? "" : gson.toJson(batchMsgBo.getPayload()));
        param.put("option", batchMsgBo.getOption() == null ? "" : gson.toJson(batchMsgBo.getOption()));

        try {
            String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
            logger.info("[ 云信批量发送消息 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{}", BATCH_MSG_SEND_URL, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
            return gson.fromJson(result, RubbishRet.class);
        } catch (Exception e) {
            logger.error("[ 云信批量发送消息 ]接口:>{}，请求:>{}，异常信息：", BATCH_MSG_SEND_URL, gson.toJson(param), e);
            return null;
        }
    }

    @Override
    public RubbishRet sendAttachMsg(NeteasePushBO pushBo) {
        long startTime = System.currentTimeMillis();
        NetEaseClient netEaseBaseClient = new NetEaseClient(ATTACH_MSG_URL, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("from", pushBo.getFrom());
        param.put("msgtype", pushBo.getMsgtype());
        param.put("to", pushBo.getTo());
        param.put("attach", gson.toJson(pushBo.getAttach()));
        param.put("pushcontent", pushBo.getPushcontent());
        param.put("payload", pushBo.getPayload() == null ? "" : gson.toJson(pushBo.getPayload()));
        param.put("sound", pushBo.getSound());
        param.put("save", pushBo.getSave());
        param.put("option", pushBo.getOption() == null ? "" : gson.toJson(pushBo));
        try {
            String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
            logger.info("[ 云信推送消息 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{}", ATTACH_MSG_URL, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
            return gson.fromJson(result, RubbishRet.class);
        } catch (Exception e) {
            logger.error("[ 云信推送消息 ]接口:>{}，请求:>{}，异常信息：", ATTACH_MSG_URL, gson.toJson(param), e);
            return null;
        }
    }
}
